package com.example.spokenwapp.dacastanalytics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DacastChannelAnalytics {

    @SerializedName("visitors")
    @Expose
    private Integer visitors;
    @SerializedName("hits")
    @Expose
    private Integer hits;
    @SerializedName("channel")
    @Expose
    private String channel;

    public Integer getVisitors() {
        return visitors;
    }

    public void setVisitors(Integer visitors) {
        this.visitors = visitors;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

}
