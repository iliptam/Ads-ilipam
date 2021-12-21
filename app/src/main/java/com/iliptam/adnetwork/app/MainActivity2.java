package com.iliptam.adnetwork.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.iliptam.adnetwork.NativeAdsManager;

import java.util.ArrayList;
import java.util.List;

import adnetwork.myapplicationads.R;

public class MainActivity2 extends AppCompatActivity implements NativeAdsManager.Listener {

    RecyclerView recyclerview;
    DemoAdapter demoAdapter;
    NativeAdsManager nativeAdsManager;
    List<String> stringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        stringList = new ArrayList<>();
        recyclerview = findViewById(R.id.rec);
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        int count = 0;
        for (int i = 0; i < 50; i++) {
            if (count >= 10) {
                count =0;
                stringList.add(null);
            } else {
                count++;
                stringList.add(String.valueOf(i));
            }
        }

        demoAdapter = new DemoAdapter(MainActivity2.this, stringList);
        recyclerview.setAdapter(demoAdapter);

        nativeAdsManager = new NativeAdsManager(MainActivity2.this, "Native Ads", 4);
        nativeAdsManager.setListener(this);
        nativeAdsManager.loadAds();

    }


    @Override
    public void onAdsLoaded() {
        demoAdapter.setNativeAdManager(nativeAdsManager);
        demoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAdError(String var1) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}