package com.iliptam.adnetwork;

import static com.iliptam.adnetwork.utils.Global.IMAGE_URL;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.iliptam.adnetwork.interfaces.AdListener;
import com.iliptam.adnetwork.models.CamBody;
import com.iliptam.adnetwork.models.CamTitle;
import com.iliptam.adnetwork.models.Campaign;
import com.iliptam.adnetwork.utils.Global;
import com.iliptam.adnetwork.utils.PrefManager;
import com.iliptam.adnetwork.viewmodels.AdsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BannerAd extends RelativeLayout {

    Context context;
    AdsViewModel adsViewModel;
    List<Campaign> adsList;
    Campaign adCampaign;
    CamTitle camTitle;
    CamBody camBody;

    ImageView imageView;
    TextView textViewName, textViewDescription;
    RatingBar ratingBar;
    Button button;
    AdListener adListener;
    String type;
    int bannerCount = 0;
    PrefManager prefManager;

    public BannerAd(Context context, String type) {
        super(context);
        this.context = context;
        this.type = type;
        prefManager = new PrefManager(context.getApplicationContext());
        adsList = new ArrayList<>();
        adsViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(AdsViewModel.class);

    }

    private void setData(String type) {
        adsViewModel.getCampaign(type).observe((LifecycleOwner) context, new Observer<List<Campaign>>() {
            @Override
            public void onChanged(List<Campaign> campaignList) {
                if (campaignList != null && campaignList.size() > 0) {
                    adsList.clear();
                    adsList.addAll(campaignList);

                    Random rand = new Random();
                    adCampaign = adsList.get(rand.nextInt(adsList.size()));
                    setTitle(adCampaign);
                    setValues(adCampaign, camBody, camTitle);
                }
            }
        });
    }

    private void setTitle(Campaign adCampaign) {
        adsViewModel.getAdTitle(adCampaign.campaignId).observe((LifecycleOwner) context, new Observer<List<CamTitle>>() {
            @Override
            public void onChanged(List<CamTitle> adTitleList) {
                if (adTitleList != null && adTitleList.size() > 0) {

                    Random rand = new Random();
                    camTitle = adTitleList.get(rand.nextInt(adTitleList.size()));
                    setValues(adCampaign, camBody, camTitle);
                }
            }
        });

        adsViewModel.getAdBody(adCampaign.campaignId).observe((LifecycleOwner) context, new Observer<List<CamBody>>() {
            @Override
            public void onChanged(List<CamBody> adBodyList) {
                if (adBodyList != null && adBodyList.size() > 0) {
                    Random rand = new Random();
                    camBody = adBodyList.get(rand.nextInt(adBodyList.size()));
                    setValues(adCampaign, camBody, camTitle);
                }
            }
        });


    }

    public void setAdListener(AdListener adListener) {
        this.adListener = adListener;
    }

    public void loadAds() {
        if (Global.isConnectedToInternet(context)) {
            inflate(context, R.layout.banner_ad_1, this);
            imageView = findViewById(R.id.app_image);
            textViewName = findViewById(R.id.app_name);
            textViewDescription = findViewById(R.id.app_desc);
            ratingBar = findViewById(R.id.stars);
            button = findViewById(R.id.button);

            setData(type);
//        setValues();
        }
    }

    private void setValues(Campaign adCampaign, CamBody camBody, CamTitle camTitle) {
//        Log.e("FF", "FFFF");
        this.setBackgroundColor(Color.parseColor("#F5F5F5"));

        if (adCampaign != null && camBody != null && camTitle != null) {
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(adCampaign.adUrl));
                    context.startActivity(browse);
                    prefManager.setClick("clc" + adCampaign.cam_name,
                            prefManager.getClick("clc" + adCampaign.cam_name) + 1);
                }
            });

            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(adCampaign.adUrl));
                    context.startActivity(browse);
                    prefManager.setClick("clc" + adCampaign.cam_name,
                            prefManager.getClick("clc" + adCampaign.cam_name) + 1);
                }
            });

            Glide.with(context).load(IMAGE_URL + adCampaign.adIcon).into(imageView);
            textViewName.setText("" + camTitle.title);
            textViewDescription.setText("" + camBody.body);
            ratingBar.setRating(adCampaign.adRating);
            button.setText("" + adCampaign.adCtaText);
            button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(adCampaign.adBgColor)));
            button.setTextColor(Color.parseColor(adCampaign.adTextColor));
            if (adCampaign.adRating == 0) {
                ratingBar.setVisibility(GONE);
            } else {
                ratingBar.setVisibility(VISIBLE);
            }
            adListener.onAdLoaded();
            prefManager.setImpration("imp" + adCampaign.cam_name,
                    prefManager.getImpration("imp" + adCampaign.cam_name) + 1);

            Log.i("Adsiliptam", "imp " + adCampaign.cam_name
                    + " " + prefManager.getImpration("imp" + adCampaign.cam_name));
        } else {
            adListener.onAdLoadFailed("Not Load");
        }
    }

    public void refreshAds(final int intervalSeconds, int count) {
            if (count < 5) {
                final Handler handler = new Handler(Looper.getMainLooper());
                int finalCount = count + 1;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetAd();
                        refreshAds(intervalSeconds, finalCount);
                    }
                }, intervalSeconds * 1000);
            }
    }

    private void resetAd() {
        if (adsList != null && adsList.size() > 0) {
            CamTitle camTitle = getAdTitle(adsList.get(bannerCount));
            CamBody camBody = getAdBody(adsList.get(bannerCount));

            setValues(adsList.get(bannerCount), camBody, camTitle);

            incrementAndSaveCounter();
        }
    }

    private CamBody getAdBody(Campaign adCampaign) {
        adsViewModel.getAdBody(adCampaign.campaignId).observe((LifecycleOwner) context, new Observer<List<CamBody>>() {
            @Override
            public void onChanged(List<CamBody> adBodyList) {
                if (adBodyList != null && adBodyList.size() > 0) {
                    Random rand = new Random();
                    camBody = adBodyList.get(rand.nextInt(adBodyList.size()));
                }
            }
        });
        return camBody;
    }

    private void incrementAndSaveCounter() {
        bannerCount++;
        if (bannerCount == adsList.size()) {
            bannerCount = 0;
        }
    }

    private CamTitle getAdTitle(Campaign adCampaign) {
        adsViewModel.getAdTitle(adCampaign.campaignId).observe((LifecycleOwner) context, new Observer<List<CamTitle>>() {
            @Override
            public void onChanged(List<CamTitle> adTitleList) {
                if (adTitleList != null && adTitleList.size() > 0) {
                    Random rand = new Random();
                    camTitle = adTitleList.get(rand.nextInt(adTitleList.size()));
                }
            }
        });
        return camTitle;
    }

}
