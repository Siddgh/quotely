package sid.com.quotely.models.data

data class QuotesMeta(
    val tag: String = "",
    var from: String = "",
    val quote: String = "",
    val quoteId: String = "",
    val type: String = "",
    val likes: Int = 0
)