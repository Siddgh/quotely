package sid.com.quotely.constants

object FirebaseConstants {

    // Realtime Database References
    const val quoteHighlightDatabaseReference = "quote-highlight"
    const val quotesInformationDatabaseReference = "quotes-information"
    const val tagsListDatabaseReference = "tags-list"
    const val recentlyAddedDatabaseReference = "recently-added"
    const val metaInformationDatabaseReference = "meta-information"
    const val searchListDatabaseReference = "search-list"
    const val allTagsListDatabaseReference = "all-tags-list"
    const val premiumPurchases = "premium-purchase-information"
    const val requestsDatabaseReference = "requests-by-users"

    // User Reference
    const val UserDatabaseReference = "users"
    const val profileInformationReference = "profile-info"
    const val likedQuotesRefernece = "liked-quotes"

    // Firestore Database Reference
    const val quotesCollectionReference = "quotes"

    // Storage References
    const val posterStorageReference = "posters"

    // Firebase Analytics Keys
    const val FirebaseAnalyticsLogAdsType = "ads_display_type"
    const val FirebaseAnalyticsLogPremiumVersion = "bought_premium_version"
    const val FirebaseAnalyticsUserProperty = "app_version"
    const val FirebaseAnalyticsLogTypeClick = "type_viewed"
    const val FirebaseAnalyticsLogTagsClick = "tag_viewed"
    const val FirebaseAnalyticsLogQuoteViewClick = "quote_viewed"
    const val FirebaseAnalyticsUsingWidgetUserProperty = "widget_usage"
    const val FirebaseAnalyticsLogActionOnQuote = "actiion_on_quote"

    // Firebase Analytics Values
    const val FirebaseAnalyticsAdsBanner = "Banner Ad"
    const val FirebaseAnalyticsAdsInterstitial = "Interstitial Ad"
    const val FirebaseAnalyticsAdsReward = "Reward Ad"
    const val FirebaseAnalyticsFreemiumUser = "Freemium"
    const val FirebaseAnalyticsPremiumUser = "Premium"
    const val FirebaseAnalyticsWidgetUsed = "Widget On"
    const val FirebaseAnalyticsWidgetNotUsed = "Widget Off"
    const val FirebaseAnalyticsLogViewTypeCard = "Card"
    const val FirebaseAnalyticsLogViewTypeText = "Text"
    const val FirebaseAnalyticsLogClickType = "type"
    const val FirebaseAnalyticsActionOnQuoteCopy = "Copy"
    const val FirebaseAnalyticsActionOnQuoteShare = "Share"
    const val FirebaseAnalyticsActionOnQuoteLike = "Like"
    const val FirebaseAnalyticsActionOnQuoteUnLike = "Unliked"
    const val FirebaseAnalyticsActionOnQuoteDownload = "Download"


    // Generic Keys
    const val quoteIdKey = "quoteId"
    const val likesKey = "likes"
    const val typeKey = "type"
    const val tagKey = "tag"
    const val movieIdKey = "fromId"
    const val fromKey = "from"

    // Default Values
    const val anon = "anon"


}