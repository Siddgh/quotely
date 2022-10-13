package sid.com.quotely.bottomsheet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.bottom_sheet_quotes.*
import sid.com.quotely.R
import sid.com.quotely.constants.FirebaseConstants
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.models.data.FavouritesMeta
import sid.com.quotely.models.groupie.QuotesMoreGroupieModel
import sid.com.quotely.utils.*
import sid.com.quotely.utils.StringUtils.quote

class QuotesBottomSheet() : BottomSheetDialogFragment() {

    lateinit var quotesData: ArrayList<String>
    lateinit var quote: String
    lateinit var from: String
    lateinit var tags: String
    lateinit var quoteId: String
    lateinit var likes: String
    lateinit var type: String
    private var isLiked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_quotes, container, false)
        quotesData =
            arguments?.getStringArrayList(GenericConstants.PassQuotesInformation) ?: ArrayList()
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpViews()

        displayBannerAds()
        FirebaseAnalyticsUtils.logQuoteViewedClicks(from, type)
        RealtimeDatabaseUtils.hasUserLikedQuote(quoteId) {
            isLiked = it
            if (isLiked) {
                fab_bottom_sheet_quotes_like.setImageResource(R.drawable.ic_fav_filled)
            } else {
                fab_bottom_sheet_quotes_like.setImageResource(R.drawable.ic_fav)
            }

            fab_bottom_sheet_quotes_like.setOnClickListener {
                isLiked = if (!isLiked) {
                    RealtimeDatabaseUtils.writeUserLikedQuoteToFirebaseDatabase(
                        FavouritesMeta(
                            quoteId,
                            type
                        )
                    )
                    FirestoreUtils.addALike(quoteId = quoteId)
                    FirebaseAnalyticsUtils.logQuoteAction(FirebaseConstants.FirebaseAnalyticsActionOnQuoteLike)
                    fab_bottom_sheet_quotes_like.setImageResource(R.drawable.ic_fav_filled)
                    true
                } else {
                    RealtimeDatabaseUtils.removeUserLikedQuoteFromFirebaseDatabase(quoteId)
                    FirestoreUtils.removeALike(quoteId = quoteId)
                    FirebaseAnalyticsUtils.logQuoteAction(FirebaseConstants.FirebaseAnalyticsActionOnQuoteUnLike)
                    fab_bottom_sheet_quotes_like.setImageResource(R.drawable.ic_fav)
                    false
                }
            }
        }

        fab_bottom_sheet_quotes_copy.setOnClickListener {
            copyToClipboard()
        }

        fab_bottom_sheet_quotes_share.setOnClickListener {
            startShare()
        }


        CommonViewsUtils.setUpMoreOptions(
            type = type,
            tags = tags,
            from = from,
            activity = requireActivity(),
            recyclerView = rv_bottom_sheet_quotes_more
        )
    }

    private fun setUpViews() {

        quote = quotesData[0].quote()
        from = quotesData[1]
        tags = quotesData[2]
        likes = quotesData[3]
        quoteId = quotesData[4]
        type = quotesData[5]

        tv_bottom_sheet_text_quote.text = quote
        tv_more_movie_info_bottom_sheet_quotes_tags.text = tags
        tv_movie_info_bottom_sheet_quotes.text = from
        iv_movie_info_bottom_sheet_quotes.setImageResource(StringUtils.getTypeIcon(type))

        setUpColorsForView(StringUtils.getTypeColor(requireContext(), type))

    }

    private fun setUpColorsForView(color: Int) {
        fab_bottom_sheet_quotes_copy.backgroundTintList = ColorStateList.valueOf(color)
        fab_bottom_sheet_quotes_like.backgroundTintList = ColorStateList.valueOf(color)
        fab_bottom_sheet_quotes_share.backgroundTintList = ColorStateList.valueOf(color)
        cv_more_movie_info_bottom_sheet_quotes_tags.setCardBackgroundColor(color)
    }

    private fun copyToClipboard() {
        val toCopy = "$quote\n\n${StringUtils.getEmojiByType(type)}  $from"
        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(GenericConstants.clipboardCopySuccess, toCopy)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(activity, GenericConstants.clipboardCopySuccess, Toast.LENGTH_SHORT)
            .show()
        FirebaseAnalyticsUtils.logQuoteAction(FirebaseConstants.FirebaseAnalyticsActionOnQuoteCopy)
    }


    private fun startShare() {
        FirebaseAnalyticsUtils.logQuoteAction(FirebaseConstants.FirebaseAnalyticsActionOnQuoteShare)
        val toShare = "$quote\n\n${StringUtils.getEmojiByType(type)} $from"
        val chooserTitle = GenericConstants.caringIsSharingTitle
        ShareCompat.IntentBuilder
            .from(requireActivity())
            .setText(toShare)
            .setType(GenericConstants.shareFormat)
            .setChooserTitle(chooserTitle)
            .startChooser()


    }

    private fun displayBannerAds() {
        AdsUtils.displayBannerAd(requireContext(), banner_bottom_sheet_text_quotes)
    }
}