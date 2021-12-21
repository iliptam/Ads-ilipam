package com.iliptam.adnetwork;

import static com.iliptam.adnetwork.utils.Global.IMAGE_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class InterstitialAd {

    Context context;
    String type;
    List<Campaign> adsList;
    AdsViewModel adsViewModel;

    private static AdListener mAdListener;

    private static Campaign adCampaign;
    private static CamTitle camTitle;
    private static CamBody camBody;

    public static boolean isLoaded = false;

    public InterstitialAd(Context context, String type) {
        this.context = context;
        this.type = type;
        adsList = new ArrayList<>();
        adsViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(AdsViewModel.class);
    }

    public void loadAds() {
        if (Global.isConnectedToInternet(context)) {
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

    private void setValues(Campaign adCampaign, CamBody camBody, CamTitle camTitle) {
        if (adCampaign != null && camBody != null && camTitle != null) {
            isLoaded = true;
        }
    }

    public void setAdListener(AdListener adListener) {
        this.mAdListener = adListener;
    }

    public void show(Context mcontext) {
        mcontext.startActivity(new Intent(this.context, InterstitialActivity.class));
        if (mcontext instanceof AppCompatActivity) {
            ((AppCompatActivity) mcontext).overridePendingTransition(0, 0);
        }
    }

    public static class InterstitialActivity extends Activity {

        public InterstitialActivity() {
        }

        public ImageView iv_ads_cancel, iv_ads_logo, iv_ads_photo;
        public TextView tv_ads_title, tv_ads_title2, tv_price, tv_ads_desc;
        public RatingBar stars;
        public Button btn_install, btn_close;
        public LinearLayout rating_container;
        RelativeLayout interstitial_parent;
        PrefManager prefManager;
        List<Integer> layoutlist;

        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            layoutlist = new ArrayList(Arrays.asList(R.layout.interstitial_ad_1, R.layout.interstitial_ad_2));

            Random random = new Random();
            int layout = layoutlist.get(random.nextInt(layoutlist.size()));
            this.setContentView(layout);
            prefManager = new PrefManager(InterstitialActivity.this);
            iv_ads_cancel = (ImageView) this.findViewById(R.id.iv_ads_cancel);
            iv_ads_logo = (ImageView) this.findViewById(R.id.iv_ad_logo);
            iv_ads_photo = (ImageView) this.findViewById(R.id.iv_ads_photo);
            tv_ads_title = (TextView) this.findViewById(R.id.ads_title);
            tv_ads_title2 = (TextView) this.findViewById(R.id.ads_title2);
            btn_close = (Button) this.findViewById(R.id.ads_close);
            tv_price = (TextView) this.findViewById(R.id.price);
            tv_ads_desc = (TextView) this.findViewById(R.id.ads_description);
            stars = (RatingBar) this.findViewById(R.id.ads_rating);
            btn_install = (Button) this.findViewById(R.id.ads_button);
            rating_container = (LinearLayout) this.findViewById(R.id.rating_container);
            interstitial_parent = (RelativeLayout) this.findViewById(R.id.interstitial_parent);

//            Picasso.get().load(IMAGE_URL + adCampaign.adIcon).into(iv_ads_logo);
//            Picasso.get().load(IMAGE_URL + adCampaign.adHeaderImage).into(iv_ads_photo);
            Glide.with(this).load(IMAGE_URL + adCampaign.adIcon).into(iv_ads_logo);
            Glide.with(this).load(IMAGE_URL + adCampaign.adHeaderImage).into(iv_ads_photo);

            btn_install.setText(adCampaign.adCtaText);
            tv_ads_title.setText(camTitle.title);
            tv_ads_title2.setText(camTitle.title);
            tv_ads_desc.setText(camBody.body);
            tv_price.setText(adCampaign.adPrice);
            btn_install.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(adCampaign.adBgColor)));
            btn_install.setTextColor(Color.parseColor(adCampaign.adTextColor));

//            interstitial_parent.setBackgroundColor(Color.parseColor(adCampaign.adTextColor));
//            tv_ads_title.setTextColor(Color.parseColor(adCampaign.adBgColor));
//            tv_ads_title2.setTextColor(Color.parseColor(adCampaign.adBgColor));
//            tv_price.setTextColor(Color.parseColor(adCampaign.adBgColor));
//            tv_ads_desc.setTextColor(Color.parseColor(adCampaign.adBgColor));

            if (adCampaign.adRating == 0) {
                rating_container.setVisibility(View.GONE);
            } else {
                rating_container.setVisibility(View.VISIBLE);
            }
            stars.setRating(adCampaign.adRating);

            btn_install.setOnClickListener((view) -> {
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(adCampaign.adUrl));
                this.startActivity(browse);
                if (mAdListener != null) {
                    mAdListener.onAdShown();
                }
                prefManager.setClick("clc" + adCampaign.cam_name,
                        prefManager.getClick("clc" + adCampaign.cam_name) + 1);
                this.finish();

            });
            iv_ads_photo.setOnClickListener((view) -> {
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(adCampaign.adUrl));
                this.startActivity(browse);
                if (mAdListener != null) {
                    mAdListener.onAdShown();
                }
                prefManager.setClick("clc" + adCampaign.cam_name,
                        prefManager.getClick("clc" + adCampaign.cam_name) + 1);
                this.finish();

            });
            iv_ads_cancel.setOnClickListener((view) -> {
                this.finish();
                isLoaded = false;
                if (mAdListener != null) {
                    mAdListener.onAdClosed();
                }
            });

            btn_close.setOnClickListener((view) -> {
                this.finish();
                isLoaded = false;
                if (mAdListener != null) {
                    mAdListener.onAdClosed();
                }
            });

            prefManager.setImpration("imp" + adCampaign.cam_name,
                    prefManager.getImpration("imp" + adCampaign.cam_name) + 1);

            Log.i("Adsiliptam", "imp " + adCampaign.cam_name
                    + " " + prefManager.getImpration("imp" + adCampaign.cam_name));

            mAdListener.onAdLoaded();

        }

        public void onBackPressed() {
            return;
        }
    }
}
