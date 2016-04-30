package com.yknx4.wifipasswordviewer


import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_wifi_list.*
import kotlinx.android.synthetic.main.content_wifi_list.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread

class WifiListActivity : AppCompatActivity() {
    private var adapter: WifiItemAdapter? = null
    private var mWifis: WifiController? = null

    override fun onResume() {
        super.onResume()

        rv_wifi.layoutManager = LinearLayoutManager(this)
        rv_wifi.isClickable = true

        async() {
            val newWifiController = WifiController(this@WifiListActivity)
            newWifiController.init()
            uiThread {
                txt_loading.visibility = View.GONE
                mWifis = newWifiController
                adapter = WifiItemAdapter(this@WifiListActivity, (mWifis as WifiController).networks)
                rv_wifi.adapter = adapter
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_list)
        setSupportActionBar(toolbar)
        setUpAds()
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)

    }

    private var mInterstitialAd: InterstitialAd? = null

    private fun setUpAds() {
        val adRequest = AdRequest.Builder().addTestDevice(Constants.DEVICE_HASH).build()
        adView.loadAd(adRequest)
        //        adView.setVisibility(View.GONE);
        mInterstitialAd = InterstitialAd(this)
        if(mInterstitialAd != null){
            mInterstitialAd!!.adUnitId = "ca-app-pub-3331856249448205/1876731601"
            mInterstitialAd!!.adListener = object : AdListener() {

                override fun onAdLoaded() {
                    if (Math.random() > .6) mInterstitialAd!!.show()
                }

            }
        }

        requestNewInterstitial()
    }

    private fun requestNewInterstitial() {
        val adRequest = AdRequest.Builder().addTestDevice(Constants.DEVICE_HASH).build()
        mInterstitialAd?.loadAd(adRequest)
    }

    companion object {

        private val TAG = WifiListActivity::class.java.simpleName
    }

}
