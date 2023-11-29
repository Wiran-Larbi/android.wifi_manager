package com.dotcipher.wifimanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button wifiOnBtn;
    Button wifiOffBtn;
    Button wifiSettingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiOnBtn = findViewById(R.id.startWifiBtn);
        wifiOffBtn = findViewById(R.id.stopWifiBtn);
        wifiSettingsBtn = findViewById(R.id.showWifiSettingsBtn);

        wifiOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionToWifiConnectionReceiver(WifiConnectionReceiver.ACTION_WIFI_ON);
                Toast.makeText(MainActivity.this, "Started Wifi ..", Toast.LENGTH_SHORT).show();
            }
        });

        wifiOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionToWifiConnectionReceiver(WifiConnectionReceiver.ACTION_WIFI_OFF);
                Toast.makeText(MainActivity.this, "Stopped Wifi ..", Toast.LENGTH_SHORT).show();
            }
        });

        wifiSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActionToWifiConnectionReceiver(WifiConnectionReceiver.ACTION_SHOW_SETTINGS);
            }
        });



    }

    private void sendActionToWifiConnectionReceiver(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }
}