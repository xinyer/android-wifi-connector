package com.stockholm.wificonnect;

import android.net.wifi.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wx.lib.Connector;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    private EditText etSSID;
    private EditText etPassword;

    private Connector connector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        connector = new Connector(this);
    }

    private void findViews() {
        etSSID = (EditText) findViewById(R.id.et_ssid);
        etPassword = (EditText) findViewById(R.id.et_password);
    }

    public void connect(View view) {
        String ssid = etSSID.getText().toString();
        String password = etPassword.getText().toString();
        if (!TextUtils.isEmpty(ssid)) {
            connector.connect(ssid, password, new Connector.Listener() {
                @Override
                public void onNotFountHotspot() {
                    Log.d(TAG, "not found hotspot");
                }

                @Override
                public void onFoundHotspot(ScanResult result) {
                    Log.d(TAG, "found hotspot:" + result);
                }

                @Override
                public void onFoundAdHoc(ScanResult result) {
                    Log.d(TAG, "found AdHoc");
                }

                @Override
                public void onConnectResult(boolean result) {
                    Log.d(TAG, "connect result:" + result);
                    Toast.makeText(MainActivity.this, "连接结果:" + result, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
