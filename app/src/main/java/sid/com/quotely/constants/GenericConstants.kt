package sid.com.quotely.constants

object GenericConstants {

    // Formats
    const val dateFormat = "dd-MM-yyyy"
    const val shareFormat = "text/plain"

    // Type of Content
    const val MOVIE = "Movie"
    const val TVSHOW = "TV Show"
    const val ANIME = "Anime"
    const val BOOK = "Book"
    const val LEGEND = "Legend"
    const val ALL = "All"
    const val FROM = "From"
    const val FROMID = "FromId"

    const val CONTENT = "Content"
    const val TYPE = "Type"
    const val TAG = "Tag"
    const val TAGANDTYPE = "TagAndType"
    const val TAGANDCONTENT = "TagAndContent"

    // Passing Data Constants
    const val PassQuotesInformation = "PASSQUOTESINFORMATION"
    const val WidgetsToAppRequestCode = 101

    // Success Messages
    const val UserSignedOut = "Successfully Signed Out"
    const val clipboardCopySuccess = "Quote Copied"
    const val UserAccountDeleted = "Your Account has been deleted, Please sign-In to start over"

    // Error Messages
    const val FailedToGetMovieName = "failed to get movie name"
    const val FailedToGetQuote = "failed to get quote"
    const val NoInternetConnection = "No Internet Connection"

    // Titles
    const val caringIsSharingTitle = "Sharing is caring"
    const val eraseYourAccount = "Delete Account?"
    const val erasePositive = "Yes"
    const val eraseNegative = "No"
    const val eraseYourAccountDesc =
        "\n\nAre you sure?\n\nThis will delete your account entirely including all your liked quotes collection.\n\nThis action can't be reversed\n\n"

}