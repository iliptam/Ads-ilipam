package com.iliptam.adnetwork.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("success")
    @Expose
    public boolean success;

    @SerializedName("message")
    @Expose
    public String message;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
