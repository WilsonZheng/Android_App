package com.wilson.networkingexample.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CSS on 8/6/2016.
 */
public class BaseResponseModel {

    @SerializedName("Success")
    private boolean mSuccess;

    @SerializedName("ErrorMessage")
    private String mErrorMessage;

    public boolean isSuccess() {
        return mSuccess;
    }

    public String getErrorMessaage() {
        return mErrorMessage;
    }
}
