package com.wx.lib;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Ping {

    private static final String TAG = "Ping";

    public boolean ping(String str) {
        Process p;
        try {
            p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + str);
            int status = p.waitFor();
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
            Log.d(TAG, "Ping: " + buffer.toString());
            return status == 0;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IOException.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "InterruptedException.");
        }
        return false;
    }
}
