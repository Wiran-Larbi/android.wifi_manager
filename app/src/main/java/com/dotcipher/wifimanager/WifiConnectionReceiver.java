package com.dotcipher.wifimanager;

import static android.app.Activity.RESULT_OK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.Tag;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class WifiConnectionReceiver extends BroadcastReceiver {

    // Notifies the receiver to turn wifi on
    public static final String ACTION_WIFI_ON = "android.intent.action.WIFI_ON";
    // Notifies the receiver to turn wifi off
    public static final String ACTION_WIFI_OFF = "android.intent.action.WIFI_OFF";
    // Notifies the receiver to connect to a specified wifi
    public static final String ACTION_CONNECT_TO_WIFI = "android.intent.action.CONNECT_TO_WIFI";

    public static final String ACTION_SHOW_SETTINGS = "android.intent.action.SHOW_SETTINGS";
    private static final String TAG = "RECEIVED";

    WifiManager wifiManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.d(TAG, "onReceive() called with : intent = [" + intent + "]");
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        final String action = intent.getAction();

        if (!isTextNullOrEmpty(action)) {
            switch (action) {
                case ACTION_WIFI_ON:
                    // Turns wifi on
                    wifiManager.setWifiEnabled(true);
                    Toast.makeText(context, "inside Receiver", Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_WIFI_OFF:
                    // Turns wifi off
                    wifiManager.setWifiEnabled(false);
                    Toast.makeText(context, "inside Receiver", Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_SHOW_SETTINGS:
                    // getWifiSettings(context);
                    Toast.makeText(context,getWifiSettings(context) , Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_CONNECT_TO_WIFI:
                    // Connects to a specific wifi network
                    final String networkSSID = intent.getStringExtra("ssid");
                    final String networkPassword = intent.getStringExtra("password");
                    if (!isTextNullOrEmpty(networkSSID) && !isTextNullOrEmpty(networkPassword)) {
                        connectToWifi(networkSSID, networkPassword);
                    } else {
                        Log.e(TAG, "onReceive : cannot use " + ACTION_CONNECT_TO_WIFI + " without passing in a proper wifi SSID and Password.");
                    }
                    break;
            }
        }


    }

    private String getWifiSettings(Context context){
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                String ssid = wifiInfo.getSSID(); // Current SSID
                String bssid = wifiInfo.getBSSID(); // BSSID of the current access point
                int ipAddress = wifiInfo.getIpAddress(); // IP address in integer format

                // Convert IP address from integer to human-readable format
                String ipAddressString = Formatter.formatIpAddress(ipAddress);

                // Additional WiFi settings
                int networkId = wifiInfo.getNetworkId(); // Network ID
                int rssi = wifiInfo.getRssi(); // Received Signal Strength Indicator
                int linkSpeed = wifiInfo.getLinkSpeed(); // Link speed in Mbps
                //int frequency = wifiInfo.getFrequency(); // Frequency in MHz

                // Create a string with detailed WiFi settings
                String wifiDetails = "SSID: " + ssid + "\nBSSID: " + bssid + "\nIP Address: " + ipAddressString +
                        "\nNetwork ID: " + networkId + "\nRSSI: " + rssi + " dBm" +
                        "\nLink Speed: " + linkSpeed + " Mbps" + "\n";

                // Display the WiFi details in a Toast message
                // Intent intent = new Intent();
                // intent.putExtra("wifiSettingsInfo", wifiDetails);
                // context.sendBroadcast(intent);

                return wifiDetails;
            }

        }
        return "";
    }

    private void connectToWifi(String networkSSID, String networkPassword) {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = String.format("\"%s\"",networkSSID);
        conf.preSharedKey = String.format("\"%s\"",networkPassword);

        int netId = wifiManager.addNetwork(conf);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }

    private boolean isTextNullOrEmpty(String text) {
        if (text.isEmpty() || text.equals(null)){
            return true;
        }
        return false;
    }

    @NonNull
    public static IntentFilter getIntentFilterForWifiConnectionReceiver() {
        final IntentFilter randomIntentFilter = new IntentFilter(ACTION_WIFI_ON);
        randomIntentFilter.addAction(ACTION_WIFI_OFF);
        randomIntentFilter.addAction(ACTION_CONNECT_TO_WIFI);
        return randomIntentFilter;
    }
}