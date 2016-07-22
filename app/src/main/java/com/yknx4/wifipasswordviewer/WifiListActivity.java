package com.yknx4.wifipasswordviewer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.mobfox.sdk.bannerads.Banner;
import com.mobfox.sdk.bannerads.BannerListener;
import com.mobfox.sdk.interstitialads.InterstitialAd;
import com.mobfox.sdk.interstitialads.InterstitialAdListener;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class WifiListActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = WifiListActivity.class.getSimpleName();
    private static final int INTERNET_AND_STORAGE = 99;
    WifiController mWifis;
    private RecyclerView mRecyclerView;
    private WifiItemAdapter adapter;
    InterstitialAd interstitial;
    private Banner banner;


    @Override
    protected void onResume() {
        super.onResume();
        interstitial.onResume();
        banner.onResume();

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
                if(o.isHasRoot()){
                    if(t!=null) t.setVisibility(View.GONE);
                    mWifis = o;
                    adapter = new WifiItemAdapter(WifiListActivity.this, mWifis.getNetworks());
                    mRecyclerView.setAdapter(adapter);
                }
                else{
                    if(t!=null){
                        t.setVisibility(View.GONE);
                        t.setText(R.string.msg_no_root);
                        Toast.makeText(WifiListActivity.this, "You don't have root or denied the access.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        };

        task.execute(this);
        showIntersitial();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getMarshmallowPermissions();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_wifi);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setClickable(true);
        loadAdsWithPermissions();
        checkForCrashes();

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
        interstitial.onPause();
        banner.onPause();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    public boolean willShowIntersitial() {
        return Math.random() >= .5;
    }

    public void showIntersitial(){
        if(interstitial !=null && willShowIntersitial()){
            requestNewInterstitial();
        }

    }

    private void setUpAds() {
        banner = (Banner) findViewById(R.id.banner);

        final Activity self = this;
        banner.setListener(new BannerListener() {
            @Override
            public void onBannerError(View banner, Exception e) {
            }
            @Override
            public void onBannerLoaded(View banner) {
            }
            @Override
            public void onBannerClosed(View banner) {
            }
            @Override
            public void onBannerFinished() {
            }
            @Override
            public void onBannerClicked(View banner) {
            }
            @Override
            public void onNoFill(View banner) {
            }
        });
        banner.setInventoryHash(getString(R.string.mobfox_inventory_hash));
        banner.load();

        interstitial = new InterstitialAd(this);

        InterstitialAdListener listener = new InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(InterstitialAd interstitial) {
                //call show() to display the interstitial when its finished loading
                interstitial.show();
            }
            @Override
            public void onInterstitialFailed(InterstitialAd interstitial, Exception e) {
            }
            @Override
            public void onInterstitialClosed(InterstitialAd interstitial) {
            }
            @Override
            public void onInterstitialFinished() {
            }
            @Override
            public void onInterstitialClicked(InterstitialAd interstitial) {
            }
            @Override
            public void onInterstitialShown(InterstitialAd interstitial) {
            }
        };
        interstitial.setListener(listener);
        interstitial.setInventoryHash(getString(R.string.mobfox_inventory_hash));

    }

    private void requestNewInterstitial() {
        interstitial.load();
    }

    @AfterPermissionGranted(INTERNET_AND_STORAGE)
    private void loadAdsWithPermissions() {
        String[] perms;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            perms = new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
        }
        else
        {
            perms = new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
        }
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permissions_rationale),
                    INTERNET_AND_STORAGE, perms);
        }
        setUpAds();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        interstitial.onRequestPermissionsResult(requestCode, permissions, grantResults);
        banner.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }


}
