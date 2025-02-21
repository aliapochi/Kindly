package com.loeth.kindly

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
fun BannerAd(){
    val bannerAdId = "ca-app-pub-3940256099942544/9214589741"
    AndroidView(modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = bannerAdId
                loadAd(AdRequest.Builder().build())
                this.adListener = object: AdListener() {
                    override fun onAdClicked() {
                        // Code to be executed when the user clicks on an ad.
                    }

                    override fun onAdClosed() {
                        // Code to be executed when the user is about to return
                        // to the app after tapping on an ad.
                    }

                    override fun onAdFailedToLoad(adError : LoadAdError) {
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

//@Composable
//fun InterstitialAd(context: Context) {
//
//    var mInterstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
//    val updatedContext by rememberUpdatedState(context)
//    val interstitialAdId = "ca-app-pub-3940256099942544/1033173712" // Correct test ad ID
//
//    fun loadAd() {
//        val adRequest = AdRequest.Builder().build()
//
//        InterstitialAd.load(updatedContext, interstitialAdId, adRequest, object : InterstitialAdLoadCallback() {
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                Log.d("AdDebug", "Ad failed to load: ${adError.message}")
//                mInterstitialAd = null
//            }
//
//            override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                Log.d("AdDebug", "Ad was loaded.")
//                mInterstitialAd = interstitialAd
//            }
//        })
//    }
//
//    LaunchedEffect(Unit) {
//        loadAd()
//    }
//
//    Button(onClick = {
//        val ad = mInterstitialAd
//        if (ad != null) {
//            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
//                override fun onAdDismissedFullScreenContent() {
//                    Log.d("AdDebug", "Ad dismissed fullscreen content.")
//                    loadAd()
//                }
//
//                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                    Log.e("AdDebug", "Ad failed to show fullscreen content.")
//                    loadAd()
//                }
//
//                override fun onAdShowedFullScreenContent() {
//                    Log.d("AdDebug", "Ad showed fullscreen content.")
//                    mInterstitialAd = null // Clear reference after showing
//                }
//            }
//            ad.show(updatedContext as Activity)
//        } else {
//            Toast.makeText(updatedContext, "Ad is loading, Please try again", Toast.LENGTH_LONG).show()
//            loadAd()
//        }
//    }) {
//        Text(text = "Show Interstitial Ad")
//    }
//}

fun showInterstitialAd(context: Context, onAdClosed: () -> Unit) {
    var mInterstitialAd: InterstitialAd? = null
    val interstitialAdId = "ca-app-pub-3940256099942544/1033173712"

    val adRequest = AdRequest.Builder().build()

    InterstitialAd.load(context, interstitialAdId, adRequest, object : InterstitialAdLoadCallback() {
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


