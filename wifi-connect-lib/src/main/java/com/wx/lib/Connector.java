package com.wx.lib;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.List;

public class Connector {

    private Context mContext;
    private WifiManager mWifiManager;
    private Listener mListener;
    private Scanner mScanner;

    private String mSSID;
    private String mPassword;

    public Connector(Context context) {
        this.mContext = context;
        this.mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.mScanner = new Scanner(context, mWifiManager);
    }

    public void connect(String ssid, String password, Listener listener) {
        this.mSSID = ssid;
        this.mPassword = password;
        this.mListener = listener;
        this.mScanner.scan(new Scanner.Listener() {
            @Override
            public void scanFinish(List<ScanResult> results) {
                handleScanResult(results);
            }
        });
    }

    private void handleScanResult(List<ScanResult> results) {
        if (results != null && !results.isEmpty()) {
            for (ScanResult result : results) {
                if (find(result)) {
                    if (isAdHoc(result)) {
                        mListener.onFoundAdHoc(result);
                    } else {
                        mListener.onFoundHotspot(result);
                        connect(result);
                    }
                    break;
                }
            }
        } else {
            System.out.println("result is null.");
            mListener.onNotFountHotspot();
        }
    }

    private boolean find(ScanResult result) {
        System.out.println(result.SSID);
        return result != null && !TextUtils.isEmpty(result.SSID) && result.SSID.equals(mSSID);
    }

    private boolean isAdHoc(ScanResult result) {
        return Wifi.isAdHoc(result);
    }

    private boolean forget(ScanResult scanResult) {
        final String security = Wifi.ConfigSec.getScanResultSecurity(scanResult);
        final WifiConfiguration config = Wifi.getWifiConfiguration(mWifiManager, scanResult, security);
        boolean result = false;
        if(config != null) {
            result = mWifiManager.removeNetwork(config.networkId) && mWifiManager.saveConfiguration();
        }
        return result;
    }

    private void connect(ScanResult scanResult) {
        forget(scanResult);
        final String security = Wifi.ConfigSec.getScanResultSecurity(scanResult);
        final WifiConfiguration config = Wifi.getWifiConfiguration(mWifiManager, scanResult, security);
        if(config == null) {
            String scanResultSecurity = Wifi.ConfigSec.getScanResultSecurity(scanResult);
            boolean isOpenNetwork = Wifi.ConfigSec.isOpenNetwork(scanResultSecurity);
            int mNumOpenNetworksKept =  Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.WIFI_NUM_OPEN_NETWORKS_KEPT, 10);
            boolean connResult;
            if (isOpenNetwork) {
                connResult = Wifi.connectToNewNetwork(mContext, mWifiManager, scanResult, null, mNumOpenNetworksKept);
            } else {
                connResult = Wifi.connectToNewNetwork(mContext, mWifiManager, scanResult, mPassword, mNumOpenNetworksKept);
            }
            mListener.onConnectResult(connResult);
        } else {
            boolean connResult = Wifi.connectToConfiguredNetwork(mContext, mWifiManager, config, false);
            mListener.onConnectResult(connResult);
        }
    }

    public void stop() {
        mScanner.stop();
    }

    public interface Listener {
        void onNotFountHotspot();
        void onFoundHotspot(ScanResult result);
        void onFoundAdHoc(ScanResult result);
        void onConnectResult(boolean result);
    }
}
