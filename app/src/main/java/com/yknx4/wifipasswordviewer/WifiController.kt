package com.yknx4.wifipasswordviewer

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast

import com.chrisplus.rootmanager.RootManager
import com.chrisplus.rootmanager.container.Result
import com.google.android.gms.location.places.ui.PlacePicker

import com.yknx4.wifipasswordviewer.model.WifiNetwork

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.StringReader
import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by yknx4 on 2016. 2. 12..
 */
class WifiController(context: Context) {
    internal var context: WeakReference<Context>

    protected fun getContext(): Context {
        return context.get()
    }

    fun getNetworks(): List<WifiNetwork> {
        Collections.sort(networks) { lhs, rhs -> lhs.name.compareTo(rhs.name, ignoreCase = true) }
        return networks
    }

    init {
        this.context = WeakReference(context)
    }

    fun init() {
        loadFiles()
    }

    private fun loadFiles() {
        val rm = RootManager.getInstance()
        if (rm.obtainPermission() && rm.hasRooted()) {
            for (dir in Constants.CONF_FILE_DIRS) {
                for (file in Constants.CONF_FILE_NAMES) {
                    Log.d(TAG, "Trying to get " + dir + file)

                    val r = rm.runCommand("cat " + dir + file)
                    if (r.result!! && r.statusCode == 0) {
                        val data = r.message
                        Log.d(TAG, data)
                        parseFile(data)
                    }

                }

            }
        }
    }


    internal val networks: MutableList<WifiNetwork> = ArrayList()


    private fun parseFile(rawData: String) {
        Log.v(TAG, rawData)
        try {
            Log.d(TAG, "Parsing: " + rawData)

            Log.d(TAG, "Parsing " + rawData.length + " characters.")

            val nets = rawData.split("network=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var mWifi: WifiNetwork?
            for (cNet in nets) {
                mWifi = null
                val positionS = cNet.indexOf("ssid=") + 5
                //int position_final = cNet.indexOf("\"",position);
                try {
                    var ssid = cNet.substring(positionS)

                    ssid = ssid.substring(1, ssid.indexOf("\"", 1))
                    if (cNet.contains("wep_key0=")) {

                        val position = cNet.indexOf("wep_key0=") + 9
                        //int position_final = cNet.indexOf("\"",position);
                        var key = cNet.substring(position)
                        if (key[0] == '"') {
                            val posicionf = key.indexOf("\"", 1)
                            key = key.substring(1, posicionf)
                        } else {
                            val posicionf = key.indexOf("\n", 1)
                            key = key.substring(0, posicionf)
                        }
                        mWifi = WifiNetwork(ssid, key, WifiNetwork.security.wep)
                    } else if (cNet.contains("psk=")) {
                        val position = cNet.indexOf("psk=") + 4
                        //int position_final = cNet.indexOf("\"",position);
                        var key = cNet.substring(position)
                        val posicionf = key.indexOf("\"", 1)
                        key = key.substring(1, posicionf)
                        mWifi = WifiNetwork(ssid, key, WifiNetwork.security.wpa)
                    }
                } catch (e: Exception) {
                }

                if (null != mWifi) networks.add(mWifi)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Couldn\' load data.")
            Log.v(TAG, rawData)
            Log.e(TAG, "Exception: " + e.message, e)
        }

    }

    companion object {

        private val TAG = WifiController::class.java.simpleName
    }
}
