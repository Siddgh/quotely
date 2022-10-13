package sid.com.quotely.utils

import android.content.Context
import androidx.core.content.ContextCompat
import sid.com.quotely.R
import sid.com.quotely.constants.FirebaseConstants
import sid.com.quotely.constants.GenericConstants
import java.util.*

object StringUtils {

    fun displayLikes(noOfLikes: Int): String {
        return if (noOfLikes == 1) "$noOfLikes Like" else "$noOfLikes Likes"
    }

    fun String.quote(): String {
        return this.replace("... ", "\n\n")
    }

    fun welcomeMessage(name: String): String {
        return "Welcome to Quotely, $name"
    }

    fun getMoreTitles(type: String, data: List<String>): String {
        return when (type) {
            GenericConstants.TYPE, GenericConstants.CONTENT -> {
                "Quotes from ${data[0]}"
            }
            GenericConstants.TAG -> {
                "${data[0]} Quotes"
            }
            GenericConstants.TAGANDTYPE, GenericConstants.TAGANDCONTENT -> {
                "${data[0]} Quotes from ${data[1]}"
            }
            else -> FirebaseConstants.anon
        }
    }

    fun getEmojiByType(type: String): String {
        return when (type) {
            GenericConstants.MOVIE -> {
                "~\uD83C\uDFAC"
            }
            GenericConstants.TVSHOW -> {
                "~📺"
            }
            GenericConstants.ANIME -> {
                "~"
            }
            GenericConstants.BOOK -> {
                "~📖"
            }
            GenericConstants.LEGEND -> {
                "~🎤"
            }
            else -> "~"
        }
    }

    fun getTypeColor(context: Context, type: String): Int {
        return when (type) {
            GenericConstants.MOVIE -> {
                ContextCompat.getColor(context, R.color.colorMovie)
            }
            GenericConstants.TVSHOW -> {
                ContextCompat.getColor(context, R.color.colorTvshow)
            }
            GenericConstants.ANIME -> {
                ContextCompat.getColor(context, R.color.colorAnime)
            }
            GenericConstants.BOOK -> {
                ContextCompat.getColor(context, R.color.colorBook)
            }
            GenericConstants.LEGEND -> {
                ContextCompat.getColor(context, R.color.colorLegend)
            }
            else -> ContextCompat.getColor(context, R.color.colorPrimary)
        }
    }

    fun String.movieNameAsId(): String {
        return this.replace("\\s".toRegex(), "")
            .toLowerCase(Locale.ROOT)
            .replace("(", "")
            .replace(")", "")
            .replace("-", "")
            .replace("_", "")
            .replace(".", "")
            .replace("*", "")
            .replace("&", "")
            .replace("%", "")
            .replace("$", "")
            .replace("#", "")
            .replace("@", "")
            .replace("!", "")
            .replace("+", "")
            .replace(":", "")
            .replace("'", "")
            .replace("?", "")
    }

    fun getRecentlyAddedHeader(type: String): String {
        return if (type != GenericConstants.ALL) {
            "Recently Added $type Quotes"
        } else {
            "Recently Added"
        }
    }

    fun getPopularQuotesHeader(type: String): String {
        return if (type != GenericConstants.ALL) {
            "Popular Quotes from ${type}s"
        } else {
            "Popular Quotes"
        }
    }

    fun getJPGFileName(id: String): String {
        return "$id.jpg"
    }

    fun searchMessage(type: String): String {
        return "Search all your favourite ${type}s"
    }

    fun searchingForMessage(s: String): String {
        return "Searching for ${s.toString()}"
    }

    fun getTypeIcon(type: String): Int {
        return when (type) {
            GenericConstants.MOVIE -> {
                R.drawable.ic_movie
            }
            GenericConstants.TVSHOW -> {
                R.drawable.ic_tvshow
            }
            GenericConstants.ANIME -> {
                R.drawable.ic_anime
            }
            GenericConstants.BOOK -> {
                R.drawable.ic_books
            }
            GenericConstants.LEGEND -> {
                R.drawable.ic_legends
            }
            GenericConstants.ALL -> {
                R.drawable.ic_all
            }
            else -> R.drawable.ic_movie
        }
    }

    fun getFavouriteQuotesTitle(type: String): String {
        return if (type != GenericConstants.ALL) {
            "All Your Favourite Quotes"
        } else {
            "Favourite Quotes from ${type}s"
        }
    }

    fun getIndividualTypeSectionTitle(tag: String): String {
        return if (tag == GenericConstants.ALL) {
            "Listing All Quotes"
        } else {
            "Listing $tag Quotes"
        }
    }

    fun oldTypeToNewType(type: String): String {
        return when (type) {
            "Books" -> {
                GenericConstants.BOOK
            }
            "Movies" -> {
                GenericConstants.MOVIE
            }
            "Tvshows" -> {
                GenericConstants.TVSHOW
            }
            "Animes" -> {
                GenericConstants.ANIME
            }
            "Authors" -> {
                GenericConstants.LEGEND
            }
            else -> GenericConstants.MOVIE
        }
    }

}