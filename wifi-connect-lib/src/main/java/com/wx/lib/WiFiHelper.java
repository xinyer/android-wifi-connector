package com.wx.lib;


import android.content.Context;
import android.net.wifi.WifiManager;

public class WiFiHelper {

    private Context mContext;
    private WifiManager mWifiManager;

    public WiFiHelper(Context context) {
        this.mContext = context;
        this.mWifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void openWiFi() {
        mWifiManager.setWifiEnabled(true);
    }

    public void closeWiFi() {
        mWifiManager.setWifiEnabled(false);
    }

    public boolean isWiFiEnable() {
        return mWifiManager.isWifiEnabled();
    }
}
