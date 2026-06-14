package com.muslimu.jarvisai;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.util.Log;

public class JarvisAccessibilityService extends AccessibilityService {
    private static final String TAG = "JarvisAI";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (source == null) return;

        checkScreenNodes(source);
    }

    private void checkScreenNodes(AccessibilityNodeInfo node) {
        if (node == null) return;

        if (node.getText() != null) {
            String text = node.getText().toString().toLowerCase();
            
            if (text.contains("washa wifi")) {
                setWifiState(true);
            } 
            else if (text.contains("zima wifi")) {
                setWifiState(false);
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            checkScreenNodes(node.getChild(i));
        }
    }

    private void setWifiState(boolean enable) {
        try {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                wifiManager.setWifiEnabled(enable);
                Log.d(TAG, "JARVIS: WiFi action executed: " + enable);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error toggling WiFi", e);
        }
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "JARVIS Service Interrupted");
    }
}