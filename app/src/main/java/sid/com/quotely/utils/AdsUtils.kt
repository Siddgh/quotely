package sid.com.quotely.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.google.android.gms.ads.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import sid.com.quotely.ProgressDialog
import sid.com.quotely.R
import sid.com.quotely.constants.FirebaseConstants
import sid.com.quotely.constants.GenericConstants

object AdsUtils {

    private fun initAds(context: Context) {
        MobileAds.initialize(context.applicationContext) {}
    }

    fun displayBannerAd(context: Context, adView: AdView) {
        showAdsOrNot(context) {
            if (it) {
                // IS A PREMIUM USER
                adView.visibility = View.GONE
            } else {
                val from = context.javaClass.name
                adView.visibility = View.VISIBLE
                initAds(context = context)
                adView.loadAd(AdRequest.Builder().build())
                adView.adListener = object : AdListener() {
                    override fun onAdClicked() {
                        super.onAdClicked()
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        FirebaseAnalyticsUtils.logAdsDisplay(
                            FirebaseConstants.FirebaseAnalyticsAdsBanner
                        )
                    }

                    override fun onAdFailedToLoad(p0: Int) {
                        super.onAdFailedToLoad(p0)
                        FirebaseCrashlytics.getInstance()
                            .log("Failed to load Banner Ads --> $p0 in $from")
                    }
                }
            }
        }
    }


    fun displayInterstitialAd(context: Context, dialog: ProgressDialog, onSuccess: () -> Unit) {

        showAdsOrNot(context) {
            if (it) {
                onSuccess()
            } else {
                dialog.startLoadingDialog()
                val from = context.javaClass.name
                initAds(context)
                val mInterstitialAd = InterstitialAd(context)
                mInterstitialAd.adUnitId = context.getString(R.string.admob_interstitial_id)
                mInterstitialAd.loadAd(AdRequest.Builder().build())
                mInterstitialAd.adListener = object : AdListener() {
                    override fun onAdClicked() {
                        super.onAdClicked()
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        if (mInterstitialAd.isLoaded) {
                            dialog.dismissDialog()
                            FirebaseAnalyticsUtils.logAdsDisplay(
                                FirebaseConstants.FirebaseAnalyticsAdsInterstitial
                            )
                            mInterstitialAd.show()
                        }
                    }

                    override fun onAdFailedToLoad(p0: Int) {
                        super.onAdFailedToLoad(p0)
                        if (p0 == 3) {
                            dialog.dismissDialog()
                            onSuccess()
                        } else {
                            if (!CommonViewsUtils.isConnected()) {
                                FirebaseCrashlytics.getInstance()
                                    .log("Failed to load Interstitial Ads due to Network Connectivity Issue")
                                dialog.dismissDialog()
                                Toast.makeText(
                                    context,
                                    GenericConstants.NoInternetConnection,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                FirebaseCrashlytics.getInstance()
                                    .log("Failed to load Interstitial Ads --> $p0 in $from")
                            }
                        }
                    }

                    override fun onAdClosed() {
                        super.onAdClosed()
                        onSuccess()
                    }
                }

            }
        }
    }

    fun showAdsOrNot(context: Context, onSuccess: (Boolean) -> Unit) {
        val billingClient =
            BillingClient.newBuilder(context).enablePendingPurchases()
                .setListener { _, _ -> }.build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                FirebaseCrashlytics.getInstance()
                    .log("Billing Service was disconnected while performing Premium/Freemium Checks")
                onSuccess(false)
            }

            override fun onBillingSetupFinished(p0: BillingResult) {
                if (p0.responseCode == BillingClient.BillingResponseCode.OK) {
                    val purchasesResult: Purchase.PurchasesResult =
                        billingClient.queryPurchases(BillingClient.SkuType.INAPP)
                    if (purchasesResult.purchasesList != null) {
                        if (purchasesResult.purchasesList?.size ?: 0 > 0) {
                            for (purchase in purchasesResult.purchasesList!!) {
                                if (purchase != null) {
                                    onSuccess(true)
                                    FirebaseAnalyticsUtils.setAppVersionType(FirebaseConstants.FirebaseAnalyticsPremiumUser)
                                } else {
                                    onSuccess(false)
                                    FirebaseAnalyticsUtils.setAppVersionType(FirebaseConstants.FirebaseAnalyticsFreemiumUser)
                                }
                            }
                        } else {
                            onSuccess(false)
                            FirebaseAnalyticsUtils.setAppVersionType(FirebaseConstants.FirebaseAnalyticsFreemiumUser)
                        }
                    } else {
                        onSuccess(false)
                    }
                } else {
                    onSuccess(false)
                    FirebaseAnalyticsUtils.setAppVersionType(FirebaseConstants.FirebaseAnalyticsFreemiumUser)
                }
            }
        })
    }

}