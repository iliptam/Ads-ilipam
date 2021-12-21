package com.iliptam.adnetwork.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdDescription {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("title")
    @Expose
    public String title;

    public AdDescription(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
