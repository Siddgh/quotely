package sid.com.quotely.utils

import android.util.Log
import androidx.paging.PagedList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import sid.com.quotely.constants.FirebaseConstants
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.models.data.MoviesMeta
import sid.com.quotely.models.data.OldQuotesMeta
import sid.com.quotely.utils.StringUtils.movieNameAsId
import sid.com.quotely.models.data.QuotesMeta

object FirestoreUtils {

    val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    val quotesFirestoreReference: CollectionReference by lazy {
        firestoreInstance.collection(FirebaseConstants.quotesCollectionReference)
    }

    val quotesConfigFetch: PagedList.Config by lazy {
        PagedList.Config.Builder().setEnablePlaceholders(false).setPrefetchDistance(5)
            .setPageSize(1).build()
    }

    fun readQuoteOfTheWeek(onSuccess: (QuotesMeta?) -> Unit) {
        RealtimeDatabaseUtils.getQuoteHighlightDatabaseReference()
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val quoteId = p0.getValue(QuotesMeta::class.java)
                        quotesFirestoreReference.document(
                            quoteId?.quoteId ?: FirebaseConstants.anon
                        ).get().addOnSuccessListener {
                            val quotesMeta = it.toObject(QuotesMeta::class.java)
                            onSuccess(quotesMeta)
                        }
                    }
                }
            })
    }

    fun addToSearch(movieMeta: MoviesMeta, type: String) {
        Log.d("Movie -> ", movieMeta.from)
        if (movieMeta.fromId!!.length >= 2) {
            RealtimeDatabaseUtils.searchListDatabaseReference.child(type).child(
                movieMeta.fromId.first().toString()
            )
                .child(movieMeta.fromId.take(2)).child(movieMeta.fromId)
                .setValue(movieMeta)
            Log.d("WRITING-MOVIES", "Writing ${movieMeta.from}")
        } else {
            RealtimeDatabaseUtils.searchListDatabaseReference.child(type).child(
                movieMeta.fromId.first().toString()
            )
                .child(movieMeta.fromId)
                .setValue(movieMeta)
            Log.d("WRITING-MOVIES", "Writing ${movieMeta.from}")
        }
    }

    fun addALike(quoteId: String) {
        quotesFirestoreReference.document(quoteId)
            .update(FirebaseConstants.likesKey, FieldValue.increment(1))
    }

    fun removeALike(quoteId: String) {
        quotesFirestoreReference.document(quoteId)
            .update(FirebaseConstants.likesKey, FieldValue.increment(-1))
    }

    fun fetchPopularQuotes(type: String): Query {
        return if (type == GenericConstants.ALL) quotesFirestoreReference.orderBy(
            FirebaseConstants.likesKey,
            Query.Direction.DESCENDING
        ).limit(5) else quotesFirestoreReference.whereEqualTo(
            FirebaseConstants.typeKey,
            type
        ).orderBy(FirebaseConstants.likesKey, Query.Direction.DESCENDING).limit(5)
    }

    fun fetchQuotesByTags(tag: String, type: String): Query {
        return quotesFirestoreReference.whereEqualTo(FirebaseConstants.typeKey, type).whereEqualTo(
            FirebaseConstants.tagKey,
            tag
        ).orderBy(FirebaseConstants.likesKey, Query.Direction.DESCENDING)
    }

    fun getQuotesFromMovieAndTagFirestore(from: String, tag: String): Query {
        return if (tag == GenericConstants.ALL) quotesFirestoreReference.whereEqualTo(
            FirebaseConstants.fromKey,
            from
        ).orderBy(
            FirebaseConstants.likesKey,
            Query.Direction.DESCENDING
        ) else quotesFirestoreReference.whereEqualTo(FirebaseConstants.fromKey, from)
            .whereEqualTo(FirebaseConstants.tagKey, tag)
            .orderBy(FirebaseConstants.likesKey, Query.Direction.DESCENDING)
    }

    fun getQuoteFromFirestore(quoteId: String, onSuccess: (QuotesMeta?) -> Unit) {
        quotesFirestoreReference.document(quoteId).get().addOnSuccessListener {
            if (it.exists()) {
                val quotesMeta = it.toObject(QuotesMeta::class.java)
                onSuccess(quotesMeta)
            }
        }
    }

    fun addQuotesfromRealtimeDatabaseToFirestore(onSuccess: (Boolean) -> Unit) {
        RealtimeDatabaseUtils.firebaseDatabaseInstance.getReference("quotedata")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (documents in p0.children) {

                            documents.children.forEachIndexed { index, dataSnapshot ->
                                var oldQuotesMeta =
                                    dataSnapshot.getValue(OldQuotesMeta::class.java)

                                if (oldQuotesMeta?.from ?: "" == "Fate/stay night") {
                                    oldQuotesMeta?.from = "Fate"
                                }

                                val quoteData = QuotesMeta(
                                    from = oldQuotesMeta?.from ?: "",
                                    quoteId = "${oldQuotesMeta?.from?.movieNameAsId()}$index",
                                    quote = oldQuotesMeta?.quote ?: "",
                                    likes = oldQuotesMeta?.likes ?: 0,
                                    type = StringUtils.oldTypeToNewType(
                                        oldQuotesMeta?.type ?: ""
                                    ),
                                    tag = oldQuotesMeta?.category ?: ""
                                )

                                Log.d("DEBUGGING", "Writing to Database")
//                                RealtimeDatabaseUtils.allTagsListDatabaseReference.child(quoteData.type)
//                                    .child(quoteData.from.movieNameAsId()).child(quoteData.tag)
//                                    .setValue(quoteData.tag)

                                RealtimeDatabaseUtils.tagsDatabaseReference.child(quoteData.type)
                                    .child(quoteData.tag)
                                    .child("tag")
                                    .setValue(quoteData.tag)

                                //quotesFirestoreReference.document(quoteData.quoteId).set(quoteData)
                            }
                        }
                        onSuccess(true)
                    }
                }

            })
    }

}