package com.iliptam.adnetwork.api;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdCampaign {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("cam_name")
    @Expose
    public String camName;
    @SerializedName("ad_category_id")
    @Expose
    public int adCategoryId;
    @SerializedName("ad_category_name")
    @Expose
    public String adCategoryName;
    @SerializedName("ad_title")
    @Expose
    public List<AdTitle> adTitle = null;
    @SerializedName("ad_description")
    @Expose
    public List<AdDescription> adDescription = null;
    @SerializedName("ad_icon")
    @Expose
    public String adIcon;
    @SerializedName("ad_header_image")
    @Expose
    public String adHeaderImage;
    @SerializedName("ad_url")
    @Expose
    public String adUrl;
    @SerializedName("ad_rating")
    @Expose
    public String adRating;
    @SerializedName("ad_cta_txt")
    @Expose
    public String adCtaTxt;
    @SerializedName("ad_txt_color")
    @Expose
    public String adTxtColor;
    @SerializedName("ad_bg_color")
    @Expose
    public String adBgColor;
    @SerializedName("ad_price")
    @Expose
    public String adPrice;
    @SerializedName("ad_type")
    @Expose
    public String adType;

    public AdCampaign(int id, String camName, int adCategoryId, String adCategoryName, List<AdTitle> adTitle,
                      List<AdDescription> adDescription, String adIcon, String adHeaderImage, String adUrl,
                      String adRating, String adCtaTxt, String adTxtColor, String adBgColor,
                      String adPrice, String adType) {
        this.id = id;
        this.camName = camName;
        this.adCategoryId = adCategoryId;
        this.adCategoryName = adCategoryName;
        this.adTitle = adTitle;
        this.adDescription = adDescription;
        this.adIcon = adIcon;
        this.adHeaderImage = adHeaderImage;
        this.adUrl = adUrl;
        this.adRating = adRating;
        this.adCtaTxt = adCtaTxt;
        this.adTxtColor = adTxtColor;
        this.adBgColor = adBgColor;
        this.adPrice = adPrice;
        this.adType = adType;
    }

    public AdCampaign(int id, int adCategoryId, String adCategoryName,
                      String adIcon, String adHeaderImage, String adUrl, String adRating,
                      String adCtaTxt, String adTxtColor, String adBgColor, String adPrice,
                      String adType) {
        this.id = id;
        this.adCategoryId = adCategoryId;
        this.adCategoryName = adCategoryName;
        this.adIcon = adIcon;
        this.adHeaderImage = adHeaderImage;
        this.adUrl = adUrl;
        this.adRating = adRating;
        this.adCtaTxt = adCtaTxt;
        this.adTxtColor = adTxtColor;
        this.adBgColor = adBgColor;
        this.adPrice = adPrice;
        this.adType = adType;
    }

    public List<AdTitle> getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(List<AdTitle> adTitle) {
        this.adTitle = adTitle;
    }

    public List<AdDescription> getAdDescription() {
        return adDescription;
    }

    public void setAdDescription(List<AdDescription> adDescription) {
        this.adDescription = adDescription;
    }
}
