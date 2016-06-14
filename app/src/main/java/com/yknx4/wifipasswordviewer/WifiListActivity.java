package com.yknx4.wifipasswordviewer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

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
        getMarshmallowPermissions();
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3331856249448205~6446532006");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpAds();
        checkForCrashes();



//        RootManager rm = RootManager.getInstance();
//        rm.obtainPermission();

    }

    private void getMarshmallowPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.INTERNET},
                        0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterManagers();

    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void checkForUpdates() {
        // Remove this for store builds!

        UpdateManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterManagers();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    public void showIntersitial(){
        if(mInterstitialAd!=null && mInterstitialAd.isLoaded()){
            if (Math.random() > .7) mInterstitialAd.show();
        }

    }

    private void setUpAds() {
        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(Constants.DEVICE_HASH)
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3331856249448205/1876731601");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
               requestNewInterstitial();
            }

            @Override
            public void onAdLoaded() {

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
