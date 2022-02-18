package com.example.spokenwapp.dacast;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Pictures {

    @SerializedName("thumbnail")
    @Expose
    private List<String> thumbnail;
    @SerializedName("splashscreen")
    @Expose
    private List<String> splashscreen;

    public void setThumbnail(List<String> thumbnail){
        this.thumbnail = thumbnail;
    }
    public List<String> getThumbnail(){
        return this.thumbnail;
    }
    public void setSplashscreen(List<String> splashscreen){
        this.splashscreen = splashscreen;
    }
    public List<String> getSplashscreen() {
        return this.splashscreen;
    }
}
