package com.example.spokenwapp.dacast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareCode {

    @SerializedName("gplus")
    @Expose
    private String gplus;

    @SerializedName("twitter")
    @Expose
    private String twitter;

    @SerializedName("facebook")
    @Expose
    private String facebook;

        public void setFacebook(String facebook){
            this.facebook = facebook;
        }
        public String getFacebook(){
            return this.facebook;
        }
        public void setTwitter(String twitter){
            this.twitter = twitter;
        }
        public String getTwitter(){
            return this.twitter;
        }
        public void setGplus(String gplus){
            this.gplus = gplus;
        }
        public String getGplus(){
            return this.gplus;
        }

}
