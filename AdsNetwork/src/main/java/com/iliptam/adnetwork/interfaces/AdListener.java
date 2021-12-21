package com.iliptam.adnetwork.interfaces;

public interface AdListener {
    void onAdLoadFailed(String var1);

    void onAdLoaded();

    void onAdClosed();

    void onAdShown();

}
