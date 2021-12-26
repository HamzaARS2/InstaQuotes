package com.reddevx.instaquotes

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.reddevx.instaquotes.ui.FeaturedQuotesActivity

class AdManager(val context: Context) {

    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    companion object {
        const val AD_INTERSTITIAL_SP_NAME = "interstitialADState"
        const val COUNTER_SP = "AdCounter"
        var mInterstitialAd: InterstitialAd? = null
        var AD_COUNTER = 0
    }


    fun prepareAdCounter() {
        sp = context.getSharedPreferences(
            AD_INTERSTITIAL_SP_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
        editor = sp.edit()
        AD_COUNTER = sp.getInt(COUNTER_SP, 0)
    }

    fun saveCounterValue() {
        editor.putInt(COUNTER_SP, AD_COUNTER)
        editor.apply()
    }

    fun loadAd() {

            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(
                context,
                "ca-app-pub-3940256099942544/1033173712",
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d("LoadAdsFailed", adError.message)
                        showToast("Failed")
                        mInterstitialAd = null
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        Log.d("LoadAdsFailed", "Ad was loaded.")
                        mInterstitialAd = interstitialAd


                    }
                })
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("TAG", "Ad was dismissed.")
                    mInterstitialAd = null
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    Log.d("TAG", "Ad failed to show.")
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("TAG", "Ad showed fullscreen content.")
                    mInterstitialAd = null
                }
            }
    }

    fun showInterstitialAd() {
        showToast("counter = $AD_COUNTER")
        if (mInterstitialAd != null) {
            if (AD_COUNTER == 2) {
                mInterstitialAd!!.show(context as Activity)
                AD_COUNTER = 0
            } else {
                AD_COUNTER = AD_COUNTER.inc()
            }
        } else {
            showToast("ad is null")
        }
    }


    fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }


}