package com.yknx4.wifipasswordviewer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chrisplus.rootmanager.RootManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


import java.util.ArrayList;
import java.util.List;

public class WifiListActivity extends AppCompatActivity {

    private static final String TAG = WifiListActivity.class.getSimpleName();
    WifiController mWifis;
    private RecyclerView mRecyclerView;
    private WifiItemAdapter adapter;
    InterstitialAd mInterstitialAd;


    @Override
    protected void onResume() {
        super.onResume();





            AsyncTask<Context,Void,WifiController> task = new AsyncTask<Context,Void,WifiController>(){
                @Override
                protected WifiController doInBackground(Context[] params) {
                    WifiController newWifiController = new WifiController(params[0]);
                    newWifiController.init();
                    return newWifiController;
                }

                @Override
                protected void onPostExecute(WifiController o) {
                    super.onPostExecute(o);
                    TextView t = (TextView) findViewById(R.id.txt_loading);
                    if(t!=null) t.setVisibility(View.GONE);
                    mWifis = o;
                    adapter = new WifiItemAdapter(WifiListActivity.this, mWifis.getNetworks());
                    mRecyclerView.setAdapter(adapter);

                }
            };
            mRecyclerView = (RecyclerView) findViewById(R.id.rv_wifi);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setClickable(true);
            task.execute(this);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpAds();

//        RootManager rm = RootManager.getInstance();
//        rm.obtainPermission();

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    private void setUpAds() {
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(Constants.DEVICE_HASH).build();
        adView.loadAd(adRequest);
//        adView.setVisibility(View.GONE);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3331856249448205/1876731601");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
//                requestNewInterstitial();
            }

            @Override
            public void onAdLoaded() {
                if (Math.random() > .6) mInterstitialAd.show();
            }


        });

        requestNewInterstitial();

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(Constants.DEVICE_HASH)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

}
