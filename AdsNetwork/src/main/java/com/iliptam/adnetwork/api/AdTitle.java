package com.iliptam.adnetwork.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdTitle {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("title")
    @Expose
    public String title;

    public AdTitle(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
