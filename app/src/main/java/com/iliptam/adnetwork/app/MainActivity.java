package com.iliptam.adnetwork.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iliptam.adnetwork.Adsiliptam;
import com.iliptam.adnetwork.BannerAd;
import com.iliptam.adnetwork.InterstitialAd;
import com.iliptam.adnetwork.interfaces.AdListener;
import com.iliptam.adnetwork.interfaces.OnInitializationCompleteListener;

import adnetwork.myapplicationads.R;

public class MainActivity extends AppCompatActivity {


    BannerAd bannerAd;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Adsiliptam.initialize(MainActivity.this, "", "https://iliptam.com/campaign/", 100,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(boolean success) {
                        if (success) {
                            Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "FAILURE", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        bannerAd = new BannerAd(MainActivity.this, "Banner");

        LinearLayout container = findViewById(R.id.banner_container);
        bannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoadFailed(String var1) {
                Log.i("Adsiliptam", var1);

            }

            @Override
            public void onAdLoaded() {
                container.removeAllViews();
                container.addView(bannerAd);
            }

            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdShown() {

            }

        });
        bannerAd.loadAds();
        bannerAd.refreshAds(15, 0);

        interstitialAd = new InterstitialAd(MainActivity.this, "Interstitial Ads");
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoadFailed(String var1) {

            }

            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdClosed() {
                Toast.makeText(MainActivity.this, "Close", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, MainActivity2.class));
            }

            @Override
            public void onAdShown() {

            }
        });
        interstitialAd.loadAds();

        Button btn_show = findViewById(R.id.btn_show);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd.isLoaded) {
                    interstitialAd.show(MainActivity.this);
                } else {
                    startActivity(new Intent(MainActivity.this, MainActivity2.class));
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!interstitialAd.isLoaded) {
            interstitialAd.loadAds();
        }
    }
}