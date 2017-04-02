package com.alleviate.eyescan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScanResp {

    @SerializedName("percent")
    @Expose
    private Double percent;

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

}