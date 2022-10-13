package sid.com.quotely.contents


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.merge_quote_highlight_header.*
import kotlinx.android.synthetic.main.merge_recyclerview_with_header.view.*
import sid.com.quotely.R
import sid.com.quotely.SignIn
import sid.com.quotely.bottomsheet.QuotesBottomSheet
import sid.com.quotely.constants.FirebaseConstants
import sid.com.quotely.constants.GenericConstants
import sid.com.quotely.models.data.*
import sid.com.quotely.utils.*
import sid.com.quotely.utils.StringUtils.quote


class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        FirebaseMessagingUtils.subscribeToNotificationTopic(this)

        displayBannerAds()

        main_toolbar.title = ""
        setSupportActionBar(main_toolbar)

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            main_drawer_layout,
            main_toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        main_drawer_layout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        main_navigation_menu.setNavigationItemSelectedListener {
            main_drawer_layout.closeDrawers()
            when (it.itemId) {
                R.id.nav_favourites -> {
                    startActivity(Intent(this, Favourites::class.java))
                }
                R.id.nav_movies -> {
                    NavigationUtils.goToTypeSection(this, GenericConstants.MOVIE)
                }
                R.id.nav_tvshows -> {
                    NavigationUtils.goToTypeSection(this, GenericConstants.TVSHOW)
                }
                R.id.nav_animes -> {
                    NavigationUtils.goToTypeSection(this, GenericConstants.ANIME)
                }
                R.id.nav_books -> {
                    NavigationUtils.goToTypeSection(this, GenericConstants.BOOK)
                }
                R.id.nav_legends -> {
                    NavigationUtils.goToTypeSection(this, GenericConstants.LEGEND)
                }
                R.id.nav_premium -> {
                    startActivity(Intent(this, Premium::class.java))
                }
                R.id.nav_instagram -> {
                    val uri = Uri.parse(getString(R.string.instagram_profile))
                    val goToInstagram = Intent(Intent.ACTION_VIEW, uri)
                    goToInstagram.setPackage(getString(R.string.instagram_package_name))
                    try {
                        startActivity(goToInstagram)
                    } catch (e: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(getString(R.string.instagram_profile_web))
                            )
                        )
                    }

                }
                R.id.nav_facebook -> {
                    val facebookIntent = getOpenFacebookIntent(this)
                    startActivity(facebookIntent)
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, Settings::class.java))
                }
                R.id.nav_logout -> {
                    AuthUI.getInstance().signOut(this).addOnCompleteListener {
                        Toast.makeText(this, GenericConstants.UserSignedOut, Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this, SignIn::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
            }
            true
        }


        FirestoreUtils.readQuoteOfTheWeek {
            val quotesMeta = it

            tv_quote_highlight_from.text =
                quotesMeta?.from ?: GenericConstants.FailedToGetMovieName
            tv_quote_highlight_quote.text =
                quotesMeta?.quote?.quote()
                    ?: GenericConstants.FailedToGetQuote
            tv_quote_highlight_like.text =
                StringUtils.displayLikes(quotesMeta?.likes ?: 0)
            tv_quote_highlight_tag.text = quotesMeta?.tag

            iv_quote_highlight_from.setImageResource(
                StringUtils.getTypeIcon(
                    it?.type ?: FirebaseConstants.anon
                )
            )

            cl_quote_highlight.setOnClickListener {
                val bottomSheetFragment = QuotesBottomSheet()
                NavigationUtils.showQuotesBottomSheetFragment(
                    supportFragmentManager,
                    bottomSheetFragment,
                    quotesMeta
                )
            }
        }
        setUpRecentlyAddedMovies()
        setUpPopularQuotes()
        setUpUserProfile(main_navigation_menu)
    }

    private fun displayBannerAds() {
        AdsUtils.displayBannerAd(this, include_recently_added_home.banner_list)
        AdsUtils.showAdsOrNot(this) {
            if (it) {
                val menu = main_navigation_menu.menu
                menu.findItem(R.id.nav_premium).isVisible = false
            }
        }
    }

    private fun setUpPopularQuotes() {

        val popularQuotesOptions =
            FirestoreRecyclerOptions.Builder<QuotesMeta>().setLifecycleOwner(this)
                .setQuery(
                    FirestoreUtils.fetchPopularQuotes(GenericConstants.ALL),
                    QuotesMeta::class.java
                )
                .build()

        CommonViewsUtils.setUpPopularQuotesSection(
            this,
            include_popular_quotes.tv_recyclerview_with_header,
            include_popular_quotes.rv_recyclerview_with_header,
            GenericConstants.ALL,
            popularQuotesOptions,
            supportFragmentManager
        )
    }

    private fun setUpRecentlyAddedMovies() {
        val options =
            FirebaseRecyclerOptions.Builder<MoviesMeta>().setLifecycleOwner(this)
                .setQuery(
                    RealtimeDatabaseUtils.fetchRecentlyAddedQuotes(GenericConstants.ALL),
                    MoviesMeta::class.java
                )
                .build()

        CommonViewsUtils.setUpRecentlyAddedSection(
            this,
            include_recently_added_home.tv_recyclerview_with_header,
            include_recently_added_home.rv_recyclerview_with_header,
            GenericConstants.ALL,
            options
        )
    }

    fun getOpenFacebookIntent(context: Context): Intent? {
        return try {
            context.packageManager
                .getPackageInfo(
                    getString(R.string.facebook_package),
                    0
                ) //Checks if FB is even installed.
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.facebook_profile))
            ) //Trys to make intent with FB's URI
        } catch (e: Exception) {
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.facebook_profile_web))
            ) //catches and opens a url to the desired page
        }
    }

    private fun setUpUserProfile(mainNavigationMenu: NavigationView?) {
        val headerView = mainNavigationMenu?.getHeaderView(0)
        AuthUtils.setUserProfile(
            headerView!!.findViewById(R.id.tv_user_name),
            headerView.findViewById(R.id.tv_user_email),
            headerView.findViewById(R.id.iv_user_profile),
            baseContext
        )
    }

}
