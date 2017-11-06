package com.wx.lib;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;


public class Scanner {

    private Context mContext;
    private Listener mListener;
    private WifiManager mWifiManager;

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> scanResults = mWifiManager.getScanResults();
                mListener.scanFinish(scanResults);
                stop();
            }
        }
    };

    public Scanner(Context context, WifiManager wifiManager) {
        this.mContext = context;
        this.mWifiManager = wifiManager;
    }

    public void scan(Listener listener) {
        this.mListener = listener;
        final IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mContext.registerReceiver(mScanReceiver, filter);
        mWifiManager.startScan();
    }

    private void stop() {
        mContext.unregisterReceiver(mScanReceiver);
    }

    interface Listener {
        void scanFinish(List<ScanResult> results);
    }
}
