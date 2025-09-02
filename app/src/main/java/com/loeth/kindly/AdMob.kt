package com.loeth.kindly

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


@Composable
fun BannerAd(modifier: Modifier){

    val testBannerAdId = Constants.TEST_BANNER_ID
    val liveBannerAdId = Constants.LIVE_BANNER_ID

    AndroidView(modifier = Modifier.fillMaxWidth().height(50.dp),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = liveBannerAdId
                loadAd(AdRequest.Builder().build())
                this.adListener = object : AdListener() {
                    override fun onAdClicked() {
                        // Code to be executed when the user clicks on an ad.
                    }

                    override fun onAdClosed() {
                        // Code to be executed when the user is about to return
                        // to the app after tapping on an ad.
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        // Code to be executed when an ad request fails.
                    }

                    override fun onAdImpression() {
                        // Code to be executed when an impression is recorded
                        // for an ad.
                    }

                    override fun onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                    }

                    override fun onAdOpened() {
                        // Code to be executed when an ad opens an overlay that
                        // covers the screen.
                    }
                }
            }

        }
    )
}

fun showInterstitialAd(context: Context, onAdClosed: () -> Unit) {
    var mInterstitialAd: InterstitialAd? = null
    val testInterstitialAdId = "ca-app-pub-3940256099942544/1033173712"
    //ca-app-pub-3940256099942544/1033173712
    val liveInterstitialAdId = "ca-app-pub-7193800847795810/8489704421"

    val adRequest = AdRequest.Builder().build()

    InterstitialAd.load(context, testInterstitialAdId, adRequest, object : InterstitialAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
            Log.d("AdDebug", "Ad failed to load: ${adError.message}")
        }

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            Log.d("AdDebug", "Ad was loaded.")
            mInterstitialAd = interstitialAd

            interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("AdDebug", "Ad dismissed fullscreen content.")
                    onAdClosed() // Perform action after the ad is closed
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    Log.e("AdDebug", "Ad failed to show fullscreen content.")
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("AdDebug", "Ad showed fullscreen content.")
                }
            }

            interstitialAd.show(context as Activity)
        }
    })
}


