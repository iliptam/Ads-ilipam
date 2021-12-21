package com.iliptam.adnetwork.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iliptam.adnetwork.NativeAd;
import com.iliptam.adnetwork.NativeAdsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import adnetwork.myapplicationads.R;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.MyHolder> {

    Context context;
    List<String> stringList;
    private Boolean isAdLoaded = false;
    private NativeAdsManager mNativeAdsManager;
    private ArrayList<NativeAd> mNativeAdsFB = new ArrayList<>();

    public DemoAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if (stringList.get(position) != null) {
            holder.textView.setText(stringList.get(position));
            holder.textView.setVisibility(View.VISIBLE);
            holder.fl_native.setVisibility(View.GONE);
        } else {
            if (isAdLoaded) {
                holder.textView.setVisibility(View.GONE);
                holder.fl_native.setVisibility(View.VISIBLE);
                if (holder.fl_native.getChildCount() == 0) {
                    LinearLayout ll_fb_native = (LinearLayout) ((Activity) context).getLayoutInflater()
                            .inflate(R.layout.native_ad2, null);
                    ImageView mvAdMedia;
                    ImageView ivAdIcon;
                    TextView tvAdTitle;
                    TextView tvAdBody;
                    RatingBar ratingBar;
                    Button btnAdCallToAction;
                    LinearLayout adChoicesContainer, ll_main;

                    mvAdMedia = ll_fb_native.findViewById(R.id.native_ad_media);
                    tvAdTitle = ll_fb_native.findViewById(R.id.native_ad_title);
                    tvAdBody = ll_fb_native.findViewById(R.id.native_ad_body);
                    ratingBar = ll_fb_native.findViewById(R.id.stars);
                    btnAdCallToAction = ll_fb_native.findViewById(R.id.native_ad_call_to_action);
                    ivAdIcon = ll_fb_native.findViewById(R.id.native_ad_icon);

                    NativeAd ad;

                    if (mNativeAdsFB.size() >= 4) {
                        ad = mNativeAdsFB.get(new Random().nextInt(mNativeAdsFB.size()));
                    } else {
                        ad = mNativeAdsManager.nextNativeAd();
                        mNativeAdsFB.add(ad);
                    }

                    if (ad != null) {

                        tvAdTitle.setText(ad.adTitle);
                        tvAdBody.setText(ad.adBody);
                        btnAdCallToAction.setText(ad.adCtaText);
                        ratingBar.setRating(ad.adRating);

                        if (ad.adRating == 0) {
                         ratingBar.setVisibility(View.GONE);
                        }
                        mNativeAdsManager.registerViewForInteraction(context, ad, ivAdIcon,
                                mvAdMedia, btnAdCallToAction);

                        holder.fl_native.addView(ll_fb_native);

                    }
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView textView;
        FrameLayout fl_native;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.number);
            fl_native = itemView.findViewById(R.id.fl_native);
        }
    }

    public void setNativeAdManager(NativeAdsManager mNativeAdsManager) {
        this.mNativeAdsManager = mNativeAdsManager;
        isAdLoaded = true;
    }
}
