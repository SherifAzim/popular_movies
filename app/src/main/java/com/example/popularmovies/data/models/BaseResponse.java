package com.example.popularmovies.data.models;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @Ignore
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;
    @Ignore
    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @Ignore
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
