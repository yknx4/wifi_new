package com.yknx4.wifipasswordviewer;

/**
 * Created by yknx4 on 2016. 2. 12..
 */
public abstract class Constants {
    public static final String[] CONF_FILE_NAMES= {"wpa_supplicant.conf","wpasupplicant.conf","bcm_supp.conf"};
    public static final String[] CONF_FILE_DIRS = {"/data/wifi/","/data/misc/wifi/","/data/etc/wifi/"};
    public static final String DEVICE_HASH = "C6DB0D367C9DD881A77EC76A3ED66162";
    public static final String NETWORKS_REGEXP = "/network=\\{\\n\\s*ssid=\"(?<ssid>.*)\"\\n\\s*psk=\"(?<psk>.*)\"\\n\\s*key_mgmt=(?<key_mgmt>.*)\\n\\s*sim_slot=\"(?<sim_slot>.*)\"\\n\\s*imsi=\"(?<imsi>.*)\"\\n\\s*priority=(?<priority>.*)\\n}/m";
}
