package com.example.spokenwapp.data.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DacastLogModel {

    @SerializedName("id")
    private int id;

    @SerializedName("channel_id")
    private int channel_id;

    @SerializedName("description")
    private String description;

    @SerializedName("countdown_date")
    private String countdown_date;

    @SerializedName("creation_date")
    private String creation_date;

    @SerializedName("hds")
    private String hds;

    @SerializedName("hls")
    private String hls;

    @SerializedName("thumbnail")
    private List<String> thumbnail;

    @SerializedName("splashscreen")
    private List<String> splashscreen;

    @SerializedName("facebook")
    private String facebook;

    @SerializedName("tweeter")
    private String tweeter;

    @SerializedName("gplus")
    private String gplus;

    @SerializedName("online")
    private boolean online;

    public DacastLogModel(int channel_id, String description, String countdown_date, String
            creation_date, String hds, String hls, List<String> thumbnail, List<String> splashscreen,
                          String facebook, String tweeter, String gplus, boolean online) {
        this.channel_id = channel_id;
        this.description = description;
        this.countdown_date = countdown_date;
        this.creation_date = creation_date;
        this.hds = hds;
        this.hls = hls;
        this.thumbnail = thumbnail;
        this.splashscreen = splashscreen;
        this.facebook = facebook;
        this.tweeter = tweeter;
        this.gplus = gplus;
        this.online = online;
    }

    public DacastLogModel(int id, int channel_id, String description, String countdown_date, String
            creation_date, String hds, String hls, List<String> thumbnail, List<String> splashscreen,
                          String facebook, String tweeter, String gplus, boolean online) {
        this.id = id;
        this.channel_id = channel_id;
        this.description = description;
        this.countdown_date = countdown_date;
        this.creation_date = creation_date;
        this.hds = hds;
        this.hls = hls;
        this.thumbnail = thumbnail;
        this.splashscreen = splashscreen;
        this.facebook = facebook;
        this.tweeter = tweeter;
        this.gplus = gplus;
        this.online = online;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountdown_date() {
        return countdown_date;
    }

    public void setCountdown_date(String countdown_date) {
        this.countdown_date = countdown_date;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getHds() {
        return hds;
    }

    public void setHds(String hds) {
        this.hds = hds;
    }

    public String getHls() {
        return hls;
    }

    public void setHls(String hls) {
        this.hls = hls;
    }

    public List<String> getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(List<String> thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getSplashscreen() {
        return splashscreen;
    }

    public void setSplashscreen(List<String> splashscreen) {
        this.splashscreen = splashscreen;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTweeter() {
        return tweeter;
    }

    public void setTweeter(String tweeter) {
        this.tweeter = tweeter;
    }

    public String getGplus() {
        return gplus;
    }

    public void setGplus(String gplus) {
        this.gplus = gplus;
    }

    public boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
