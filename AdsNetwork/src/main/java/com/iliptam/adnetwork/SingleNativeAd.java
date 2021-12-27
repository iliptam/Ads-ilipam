package com.iliptam.adnetwork;

import static com.iliptam.adnetwork.utils.Global.IMAGE_URL;
import static com.iliptam.adnetwork.utils.Global.SETIMP;
import static com.iliptam.adnetwork.utils.Global.checkIMP;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.iliptam.adnetwork.models.CamBody;
import com.iliptam.adnetwork.models.CamTitle;
import com.iliptam.adnetwork.models.Campaign;
import com.iliptam.adnetwork.utils.Global;
import com.iliptam.adnetwork.utils.PrefManager;
import com.iliptam.adnetwork.viewmodels.AdsViewModel;
import com.iliptam.adnetwork.viewmodels.CmpViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SingleNativeAd {

    Context mContext;
    int numAdsRequested;
    AdsViewModel adsViewModel;
    List<Campaign> adsList;
    String type;

    private static Campaign adCampaign;
    private static CamTitle camTitle;
    private static CamBody camBody;

    public static boolean isLoaded = false;

    NativeAd nativeAd;
    Listener mListener;
    PrefManager prefManager;
    public static CmpViewModel cmpViewModel;

    public SingleNativeAd(Context context, String type) {
        this.mContext = context;
        this.numAdsRequested = numAdsRequested;
        this.type = type;
        prefManager = new PrefManager(mContext);
        adsList = new ArrayList<>();
        adsViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(AdsViewModel.class);
        cmpViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(CmpViewModel.class);
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void loadAds() {
        if (Global.isConnectedToInternet(mContext)) {
            prefManager.setBoolean("ISL_N", true);
            adsViewModel.getCampaign(type).observe((LifecycleOwner) mContext, new Observer<List<Campaign>>() {
                @Override
                public void onChanged(List<Campaign> campaignList) {
                    if (campaignList != null && campaignList.size() > 0) {
                        adsList.clear();
                        adsList.addAll(campaignList);
                        Random rand = new Random();
                        adCampaign = adsList.get(rand.nextInt(adsList.size()));
                        setTitle(adCampaign);
                    } else {
                        mListener.onAdError("Load Failed");
                    }
                }
            });
        }
    }

    private void setTitle(Campaign adCampaign) {
        adsViewModel.getAdTitle(adCampaign.campaignId).observe((LifecycleOwner) mContext, new Observer<List<CamTitle>>() {
            @Override
            public void onChanged(List<CamTitle> adTitleList) {
                if (adTitleList != null && adTitleList.size() > 0) {

                    Random rand = new Random();
                    camTitle = adTitleList.get(rand.nextInt(adTitleList.size()));
                    setBody(adCampaign);
                }
            }
        });
    }

    private void setBody(Campaign adCampaign) {
        adsViewModel.getAdBody(adCampaign.campaignId).observe((LifecycleOwner) mContext, new Observer<List<CamBody>>() {
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
            if (prefManager.getBoolean("ISL_N")) {
                prefManager.setBoolean("ISL_N", false);
                nativeAd = new NativeAd(adCampaign.cam_name, camTitle.title, camBody.body,
                        adCampaign.adIcon, adCampaign.adHeaderImage,
                        adCampaign.adUrl, adCampaign.adRating, adCampaign.adCtaText, adCampaign.adBgColor,
                        adCampaign.adTextColor, adCampaign.adPrice, false);

                prefManager.setImpration("imp" + adCampaign.cam_name,
                        prefManager.getImpration("imp" + adCampaign.cam_name) + 1);

                Log.i("Adsiliptam", "imp " + adCampaign.cam_name
                        + " " + prefManager.getImpration("imp" + adCampaign.cam_name));

                if (checkIMP(mContext, SETIMP)) {
                    setDataServer();
                }
                mListener.onAdsLoaded();
            }
        }
    }

    private void setDataServer() {
        cmpViewModel.setDataServer();
    }

    public interface Listener {
        void onAdsLoaded();

        void onAdError(String var1);
    }

    public void registerViewForInteraction(Context context, NativeAd nativeAd,
                                           ImageView icon, ImageView header, Button button) {

        nativeAd.setLoad(true);
        Glide.with(mContext).load(IMAGE_URL + nativeAd.adIcon).into(icon);
        Glide.with(mContext).load(IMAGE_URL + nativeAd.adHeaderImage).into(header);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(nativeAd.adUrl));
                mContext.startActivity(browse);
                prefManager.setClick("clc" + nativeAd.campaignName,
                        prefManager.getClick("clc" + nativeAd.campaignName) + 1);
            }
        });
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(nativeAd.adUrl));
                mContext.startActivity(browse);
                prefManager.setClick("clc" + nativeAd.campaignName,
                        prefManager.getClick("clc" + nativeAd.campaignName) + 1);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(nativeAd.adUrl));
                mContext.startActivity(browse);
                prefManager.setClick("clc" + nativeAd.campaignName,
                        prefManager.getClick("clc" + nativeAd.campaignName) + 1);
            }
        });

    }

    public NativeAd getnativeAd() {
        if (nativeAd != null) {
            return nativeAd;
        } else {
            return null;
        }
    }



}
