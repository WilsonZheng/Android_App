package com.wilson.networkingexample.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CSS on 7/6/2016.
 */
public class ChildResponseModel extends BaseResponseModel {

    @SerializedName("Data")
    private ChildModel childModel;

    public ChildModel getChildModel() {
        return childModel;
    }
    public void setChildModel(ChildModel childModel) {
        this.childModel = childModel;
    }
}
