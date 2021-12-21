package com.iliptam.adnetwork.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "campaigns")
public class Campaign {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "campaignId")
    public int campaignId;

    @ColumnInfo(name = "cam_name")
    public String cam_name;

    @ColumnInfo(name = "ad_category_id")
    public int adCategoryId;

    @ColumnInfo(name = "ad_category_name")
    public String adCategoryName;

    @ColumnInfo(name = "ad_icon")
    public String adIcon;

    @ColumnInfo(name = "ad_header_image")
    public String adHeaderImage;

    @ColumnInfo(name = "ad_url")
    public String adUrl;

    @ColumnInfo(name = "ad_rating")
    public float adRating;

    @ColumnInfo(name = "ad_cta_text")
    public String adCtaText;

    @ColumnInfo(name = "ad_bg_color")
    public String adBgColor;

    @ColumnInfo(name = "ad_text_color")
    public String adTextColor;

    @ColumnInfo(name = "ad_price")
    public String adPrice;

    @ColumnInfo(name = "ad_type")
    public String adType;

    public Campaign(int campaignId, String cam_name, int adCategoryId, String adCategoryName, String adIcon, String adHeaderImage, String adUrl, float adRating, String adCtaText, String adBgColor, String adTextColor, String adPrice, String adType) {
        this.campaignId = campaignId;
        this.cam_name = cam_name;
        this.adCategoryId = adCategoryId;
        this.adCategoryName = adCategoryName;
        this.adIcon = adIcon;
        this.adHeaderImage = adHeaderImage;
        this.adUrl = adUrl;
        this.adRating = adRating;
        this.adCtaText = adCtaText;
        this.adBgColor = adBgColor;
        this.adTextColor = adTextColor;
        this.adPrice = adPrice;
        this.adType = adType;
    }
}

