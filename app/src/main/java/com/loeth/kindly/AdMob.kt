package com.loeth.kindly

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
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
fun BannerAd(modifier: Modifier = Modifier) {
    // Use test ID for development, swap to Constants.LIVE_BANNER_ID for production
    val adUnitId = Constants.TEST_BANNER_ID 

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        Log.d("AdMob", "Banner loaded successfully.")
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.e("AdMob", "Banner failed to load: ${adError.message}, Error Code: ${adError.code}")
                    }
                }
                
                loadAd(AdRequest.Builder().build())
            }
        },
        update = { _ ->
            // AdView is updated automatically when parameters change
        }
    )
}

fun showInterstitialAd(context: Context, onAdClosed: () -> Unit) {
    val adUnitId = "ca-app-pub-3940256099942544/1033173712" // Test ID
    val adRequest = AdRequest.Builder().build()

    InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
            Log.d("AdMob", "Interstitial failed to load: ${adError.message}")
            onAdClosed() // Continue app flow even if ad fails
        }

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            Log.d("AdMob", "Interstitial loaded successfully.")
            
            interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("AdMob", "Ad dismissed.")
                    onAdClosed() 
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e("AdMob", "Ad failed to show: ${adError.message}")
                    onAdClosed()
                }
            }

            if (context is Activity) {
                interstitialAd.show(context)
            } else {
                Log.e("AdMob", "Context is not an Activity, cannot show interstitial.")
                onAdClosed()
            }
        }
    })
}
