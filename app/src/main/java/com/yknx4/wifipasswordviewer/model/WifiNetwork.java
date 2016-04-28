package com.yknx4.wifipasswordviewer.model;

/**
 * Created by yknx4 on 2016. 2. 12..
 */
public class WifiNetwork {
    private String Name;
    private String Key;
    private security Type;

    public WifiNetwork(String name, String key, security type) {
        Name = name;
        Key = key;
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public String getKey() {
        return Key;
    }

    public security getType() {
        return Type;
    }


    public enum security{
        wep,wpa,open
    }

    public static String securityToString(WifiNetwork.security security){
        switch (security){
            case wep: return "WEP";
            case wpa: return "WPA";
            case open: return "OPEN";
        }
        return "";

    }
}
