package com.yknx4.wifipasswordviewer;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.chrisplus.rootmanager.RootManager;
import com.chrisplus.rootmanager.container.Result;
import com.google.android.gms.location.places.ui.PlacePicker;

import com.yknx4.wifipasswordviewer.model.WifiNetwork;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yknx4 on 2016. 2. 12..
 */
public class WifiController {

    private static final String TAG = WifiController.class.getSimpleName();
    WeakReference<Context> context;

    protected Context getContext() {
        return context.get();
    }

    public List<WifiNetwork> getNetworks() {
        Collections.sort(networks, new Comparator<WifiNetwork>(){
            @Override
            public int compare(WifiNetwork lhs, WifiNetwork rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }

        });
        return networks;
    }

    public WifiController(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void init() {
        loadFiles();
    }

    private void loadFiles() {
        RootManager rm = RootManager.getInstance();
        if (rm.obtainPermission() && rm.hasRooted()) {
            for (String dir : Constants.CONF_FILE_DIRS) {
                for (String file : Constants.CONF_FILE_NAMES) {
                    Log.d(TAG, "Trying to get " + dir + file);


//                Result r = rm.runCommand("cp " + dir + file + " " + getContext().getFilesDir().getAbsolutePath() + dir);
//                rm.runCommand("chmod -R 666 " + getContext().getFilesDir().getAbsolutePath() + dir + file);
                    Result r = rm.runCommand("cat " + dir + file);
                    if (r.getResult() && r.getStatusCode() == 0) {
                        String data = r.getMessage();
                        Log.d(TAG, data);
                        parseFile(data);
                    }

                }

            }
        } else {
//            Toast.makeText(getContext(), "Couldn\'t get root permissions. Won\'t show anything..", Toast.LENGTH_LONG).show();
        }
    }


    final List<WifiNetwork> networks = new ArrayList<>();


    //    final Pattern pattern = Pattern.compile(Constants.NETWORKS_REGEXP,Patern.LITERAL);
    Matcher matcher;


    private void secondParte(String DATA) {
//       matcher=  pattern.matcher(DATA);
//        while(matcher.find()){
//            Log.d(TAG,"New network");
//            Log.d(TAG,"-----------");
//            Log.d(TAG, "SSID: " + matcher.group());
//            Log.d(TAG, "PSK: " + matcher.group());
//            Log.d(TAG, "KEY MGMT: " + matcher.group());
//            Log.d(TAG, "SIM SLOT: " + matcher.group());
//            Log.d(TAG, "IMSI: " + matcher.group());
//            Log.d(TAG, "PRIORITY: " + matcher.group());
//        }
    }

    private void parseFile(String rawData) {
        Log.v(TAG, rawData);
//        secondParte(rawData);
        try {
            Log.d(TAG, "Parsing: " + rawData);

//            rawData = rawData.replace('$', ' ');
//            rawData = rawData.replace("network=", "$");
            Log.d(TAG, "Parsing " + rawData.length() + " characters.");

            String[] nets = rawData.split("network=");
            WifiNetwork mWifi;
            for (String cNet : nets) {
                mWifi = null;
                int positionS = cNet.indexOf("ssid=") + 5;
                //int position_final = cNet.indexOf("\"",position);
                try {
                String ssid = cNet.substring(positionS);

                    ssid = ssid.substring(1, ssid.indexOf("\"", 1));
                    if (cNet.contains("wep_key0=")) {

                        int position = cNet.indexOf("wep_key0=") + 9;
                        //int position_final = cNet.indexOf("\"",position);
                        String key = cNet.substring(position);
                        if (key.charAt(0) == '"') {
                            int posicionf = key.indexOf("\"", 1);
                            key = key.substring(1, posicionf);
                        } else {
                            int posicionf = key.indexOf("\n", 1);
                            key = key.substring(0, posicionf);
                        }
                        mWifi = new WifiNetwork(ssid, key, WifiNetwork.security.wep);
                    } else if (cNet.contains("psk=")) {
                        int position = cNet.indexOf("psk=") + 4;
                        //int position_final = cNet.indexOf("\"",position);
                        String key = cNet.substring(position);
                        int posicionf = key.indexOf("\"", 1);
                        key = key.substring(1, posicionf);
                        mWifi = new WifiNetwork(ssid, key, WifiNetwork.security.wpa);
                    }
                } catch (Exception e) {
                }

                if (null != mWifi) networks.add(mWifi);
            }
        } catch (Exception e) {
            Log.d(TAG, "Couldn\' load data.");
            Log.v(TAG, rawData);
            Log.e(TAG, "Exception: " + e.getMessage(), e);
        }
    }
}
