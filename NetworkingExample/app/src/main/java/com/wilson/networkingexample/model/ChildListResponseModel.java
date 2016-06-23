package com.wilson.networkingexample.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by CSS on 7/6/2016.
 */
public class ChildListResponseModel extends BaseResponseModel {

    @SerializedName("Data")
    private ArrayList<ChildModel> childModel;

    public ArrayList<ChildModel> getChildModel() {
        return childModel;
    }

    public void setChildModel(ArrayList<ChildModel> childModel) {
        this.childModel = childModel;
    }
}
