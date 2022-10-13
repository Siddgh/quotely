package sid.com.quotely.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import sid.com.quotely.R
import sid.com.quotely.SignIn
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.contents.Favourites
import sid.com.quotely.contents.TypeSection

object AppShortcutUtils {

    private fun getFavouritesIntent(context: Context): Intent {
        val intent: Intent?
        return if (FirebaseAuth.getInstance().currentUser != null) {
            intent = Intent(context, Favourites::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent
        } else {
            intent = Intent(context, SignIn::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent
        }

    }

    private fun getMoviesIntent(context: Context): Intent {
        val intent: Intent?
        return if (FirebaseAuth.getInstance().currentUser != null) {
            intent = Intent(context, TypeSection::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.putExtra(GenericConstants.TYPE, GenericConstants.MOVIE)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent
        } else {
            intent = Intent(context, SignIn::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent
        }
    }

    private fun getTvShowIntent(context: Context): Intent {
        val intent: Intent?
        return if (FirebaseAuth.getInstance().currentUser != null) {
            intent = Intent(context, TypeSection::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.putExtra(GenericConstants.TYPE, GenericConstants.TVSHOW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent
        } else {
            intent = Intent(context, SignIn::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent
        }
    }

    private fun getAnimeIntent(context: Context): Intent {
        val intent: Intent?
        return if (FirebaseAuth.getInstance().currentUser != null) {
            intent = Intent(context, TypeSection::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.putExtra(GenericConstants.TYPE, GenericConstants.ANIME)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent
        } else {
            intent = Intent(context, SignIn::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent
        }
    }


    private fun getBookIntent(context: Context): Intent {
        val intent: Intent?
        return if (FirebaseAuth.getInstance().currentUser != null) {
            intent = Intent(context, TypeSection::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.putExtra(GenericConstants.TYPE, GenericConstants.BOOK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent
        } else {
            intent = Intent(context, SignIn::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent
        }
    }

    private fun getLegendIntent(context: Context): Intent {
        val intent: Intent?
        return if (FirebaseAuth.getInstance().currentUser != null) {
            intent = Intent(context, TypeSection::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.putExtra(GenericConstants.TYPE, GenericConstants.LEGEND)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent
        } else {
            intent = Intent(context, SignIn::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent
        }
    }


    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun createFavouritesShortcut(context: Context): ShortcutInfo {
        return ShortcutInfo.Builder(context, context.getString(R.string.app_shortcut_favourites_id))
            .setShortLabel(context.getString(R.string.app_shortcut_favourites_title))
            .setLongLabel(context.getString(R.string.app_shortcut_favourites_title))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_fav_shortcut))
            .setIntent(getFavouritesIntent(context))
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun createMoviesShortcut(context: Context): ShortcutInfo {
        return ShortcutInfo.Builder(context, context.getString(R.string.app_shortcut_movies_id))
            .setShortLabel(context.getString(R.string.app_shortcut_movies_title))
            .setLongLabel(context.getString(R.string.app_shortcut_movies_title))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_movie_shortcut))
            .setIntent(getMoviesIntent(context))
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun createTVShowsShortcut(context: Context): ShortcutInfo {
        return ShortcutInfo.Builder(context, context.getString(R.string.app_shortcut_tvshows_id))
            .setShortLabel(context.getString(R.string.app_shortcut_tvshows_title))
            .setLongLabel(context.getString(R.string.app_shortcut_tvshows_title))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_tvshow_shortcut))
            .setIntent(getTvShowIntent(context))
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun createBooksShortcut(context: Context): ShortcutInfo {
        return ShortcutInfo.Builder(context, context.getString(R.string.app_shortcut_books_id))
            .setShortLabel(context.getString(R.string.app_shortcut_books_title))
            .setLongLabel(context.getString(R.string.app_shortcut_books_title))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_book_shortcut))
            .setIntent(getBookIntent(context))
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun createAnimesShortcut(context: Context): ShortcutInfo {
        return ShortcutInfo.Builder(context, context.getString(R.string.app_shortcut_animes_id))
            .setShortLabel(context.getString(R.string.app_shortcut_animes_title))
            .setLongLabel(context.getString(R.string.app_shortcut_animes_title))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_anime_shortcut))
            .setIntent(getAnimeIntent(context))
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun createLegendsShortcut(context: Context): ShortcutInfo {
        return ShortcutInfo.Builder(context, context.getString(R.string.app_shortcut_legends_id))
            .setShortLabel(context.getString(R.string.app_shortcut_legends_title))
            .setLongLabel(context.getString(R.string.app_shortcut_legends_title))
            .setIcon(Icon.createWithResource(context, R.drawable.ic_legend_shortcut))
            .setIntent(getLegendIntent(context))
            .build()
    }


    fun setUpAppShortcuts(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortCutManager = context.getSystemService(ShortcutManager::class.java)
            shortCutManager!!.dynamicShortcuts =
                mutableListOf(
                    createBooksShortcut(context),
                    createTVShowsShortcut(context),
                    createMoviesShortcut(context),
                    createFavouritesShortcut(context)
                )
        }
    }

}