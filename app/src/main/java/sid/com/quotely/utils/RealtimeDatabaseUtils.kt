package sid.com.quotely.utils

import android.util.Log
import androidx.paging.PagedList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import sid.com.quotely.constants.FirebaseConstants
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.models.data.*
import sid.com.quotely.utils.StringUtils.movieNameAsId

object RealtimeDatabaseUtils {

    val firebaseDatabaseInstance: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }

    val userDatabaseReference: DatabaseReference by lazy {
        firebaseDatabaseInstance.getReference(FirebaseConstants.UserDatabaseReference)
    }

    val quotesInformationDatabaseReference: DatabaseReference by lazy {
        firebaseDatabaseInstance.getReference(FirebaseConstants.quotesInformationDatabaseReference)
    }

    val requestsDatabaseReference: DatabaseReference by lazy {
        firebaseDatabaseInstance.getReference(FirebaseConstants.requestsDatabaseReference)
    }

    val recentlyAddedDatabaseReference: DatabaseReference by lazy {
        quotesInformationDatabaseReference.child(FirebaseConstants.recentlyAddedDatabaseReference)
    }

    val individualUserDatabaseReference: DatabaseReference by lazy {
        userDatabaseReference.child(AuthUtils.getUserMeta().uid ?: FirebaseConstants.anon)
    }

    val tagsDatabaseReference: DatabaseReference by lazy {
        quotesInformationDatabaseReference.child(FirebaseConstants.tagsListDatabaseReference)
    }

    val metaInformationDatabaseReference: DatabaseReference by lazy {
        quotesInformationDatabaseReference.child(FirebaseConstants.metaInformationDatabaseReference)
    }

    val searchListDatabaseReference: DatabaseReference by lazy {
        quotesInformationDatabaseReference.child(FirebaseConstants.searchListDatabaseReference)
    }

    val allTagsListDatabaseReference: DatabaseReference by lazy {
        quotesInformationDatabaseReference.child(FirebaseConstants.allTagsListDatabaseReference)
    }

    val pagingConfig =
        PagedList.Config.Builder().setEnablePlaceholders(false).setPrefetchDistance(5)
            .setPageSize(5).build()


    fun fetchMoviesBySearchFirebaseDatabase(searchText: String, type: String): Query {
        return searchListDatabaseReference.child(type)
            .child(searchText.movieNameAsId().first().toString())
            .child(searchText.movieNameAsId().take(2))
            .orderByChild(FirebaseConstants.movieIdKey)
            .startAt(searchText.movieNameAsId())
            .endAt(searchText.movieNameAsId() + "\uf8ff")
    }

    fun fetchTagsForTypeDatabaseReference(type: String): Query {
        return tagsDatabaseReference.child(type)
    }

    fun fetchTypeSectionListDatabaseReference(type: String): Query {
        return metaInformationDatabaseReference.child(type)
    }

    fun getQuoteHighlightDatabaseReference(): DatabaseReference {
        val reference =
            firebaseDatabaseInstance.getReference(FirebaseConstants.quoteHighlightDatabaseReference)
        reference.keepSynced(true)
        return reference
    }

    fun writeUserToFirebaseDatabase(userMeta: UserMeta) {
        userDatabaseReference.child(userMeta.uid ?: FirebaseConstants.anon)
            .child(FirebaseConstants.profileInformationReference).setValue(userMeta)
    }

    fun hasUserLikedQuote(quoteId: String, onSuccess: (Boolean) -> Unit) {
        individualUserDatabaseReference.child(FirebaseConstants.likedQuotesRefernece).child(quoteId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    onSuccess(false)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        onSuccess(true)
                    } else {
                        onSuccess(false)
                    }
                }

            })
    }

    fun writeUserLikedQuoteToFirebaseDatabase(favouritesMeta: FavouritesMeta) {
        individualUserDatabaseReference
            .child(FirebaseConstants.likedQuotesRefernece)
            .child(favouritesMeta.quoteId).setValue(favouritesMeta)
    }

    fun removeUserLikedQuoteFromFirebaseDatabase(quoteId: String) {
        individualUserDatabaseReference
            .child(FirebaseConstants.likedQuotesRefernece)
            .child(quoteId).setValue(null)
    }

    fun fetchUserLikedQuotes(type: String): Query {
        return if (type == GenericConstants.ALL)
            individualUserDatabaseReference.child(FirebaseConstants.likedQuotesRefernece)
        else individualUserDatabaseReference.child(FirebaseConstants.likedQuotesRefernece)
            .orderByChild(FirebaseConstants.typeKey)
            .equalTo(type)
    }

    fun fetchRecentlyAddedQuotes(type: String): Query {
        return if (type == GenericConstants.ALL)
            recentlyAddedDatabaseReference
        else recentlyAddedDatabaseReference
            .orderByChild(FirebaseConstants.typeKey)
            .equalTo(type)
    }

    fun getTagsFromMovies(movieId: String, type: String): DatabaseReference {
        return allTagsListDatabaseReference.child(type).child(movieId)
    }

    fun writePremiumPurchaseInformationToFirebase(paidMeta: PaidMeta) {
        individualUserDatabaseReference.child(FirebaseConstants.premiumPurchases).setValue(paidMeta)
    }

    fun writeRequestsToFirebase(
        type: String,
        title: String,
        comment: String,
        onSuccess: (Boolean) -> Unit
    ) {
        val requestsMeta = RequestsMeta(type = type, title = title, comment = comment)
        requestsDatabaseReference.child(type).child(title).setValue(requestsMeta)
        onSuccess(true)
    }

    fun moveOldLikedQuotesToNew() {
        var count = 0
        userDatabaseReference.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (user in p0.children) {
                        user.children.forEachIndexed { index, dataSnapshot ->
                            if (dataSnapshot.key == "likedquotes") {
                                Log.d("DEBUGGING", "${user.key}->${dataSnapshot.childrenCount}")
                                for (likedQuote in dataSnapshot.children) {
                                    val oldQuotesMeta =
                                        likedQuote.getValue(OldQuotesMeta::class.java)
                                    count += 1
                                    FirestoreUtils.quotesFirestoreReference.whereEqualTo(
                                        "quote",
                                        oldQuotesMeta?.quote
                                    ).get().addOnSuccessListener {
                                        for (document in it.documents) {
                                            val quotesMeta =
                                                document.toObject(QuotesMeta::class.java)
                                            RealtimeDatabaseUtils.userDatabaseReference.child(
                                                user?.key ?: ""
                                            ).child(
                                                "liked-quotes"
                                            ).child(quotesMeta?.quoteId ?: "").setValue(
                                                FavouritesMeta(
                                                    quoteId = quotesMeta?.quoteId ?: "",
                                                    type = quotesMeta?.type ?: ""
                                                )
                                            )
                                        }
                                        Log.d(
                                            "DEBUGGING",
                                            "${user.key}->${dataSnapshot.childrenCount} Quotes Written"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

        })
    }

}