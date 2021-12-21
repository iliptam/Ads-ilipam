package com.iliptam.adnetwork;

public class NativeAd {

    public String campaignName;
    public String adTitle;
    public String adBody;
    public String adIcon;
    public String adHeaderImage;
    public String adUrl;
    public float adRating;
    public String adCtaText;
    public String adBgColor;
    public String adTextColor;
    public String adPrice;
    public boolean isLoad;

    public NativeAd(String campaignName, String adTitle, String adBody, String adIcon, String adHeaderImage, String adUrl, float adRating, String adCtaText, String adBgColor, String adTextColor, String adPrice, boolean isLoad) {
        this.campaignName = campaignName;
        this.adTitle = adTitle;
        this.adBody = adBody;
        this.adIcon = adIcon;
        this.adHeaderImage = adHeaderImage;
        this.adUrl = adUrl;
        this.adRating = adRating;
        this.adCtaText = adCtaText;
        this.adBgColor = adBgColor;
        this.adTextColor = adTextColor;
        this.adPrice = adPrice;
        this.isLoad = isLoad;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdBody() {
        return adBody;
    }

    public void setAdBody(String adBody) {
        this.adBody = adBody;
    }

    public String getAdIcon() {
        return adIcon;
    }

    public void setAdIcon(String adIcon) {
        this.adIcon = adIcon;
    }

    public String getAdHeaderImage() {
        return adHeaderImage;
    }

    public void setAdHeaderImage(String adHeaderImage) {
        this.adHeaderImage = adHeaderImage;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public float getAdRating() {
        return adRating;
    }

    public void setAdRating(float adRating) {
        this.adRating = adRating;
    }

    public String getAdCtaText() {
        return adCtaText;
    }

    public void setAdCtaText(String adCtaText) {
        this.adCtaText = adCtaText;
    }

    public String getAdBgColor() {
        return adBgColor;
    }

    public void setAdBgColor(String adBgColor) {
        this.adBgColor = adBgColor;
    }

    public String getAdTextColor() {
        return adTextColor;
    }

    public void setAdTextColor(String adTextColor) {
        this.adTextColor = adTextColor;
    }

    public String getAdPrice() {
        return adPrice;
    }

    public void setAdPrice(String adPrice) {
        this.adPrice = adPrice;
    }
}
