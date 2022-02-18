package com.example.spokenwapp.dacast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.Date;

public class Data {

    @SerializedName("ads")
    @Expose
    private String ads;
    @SerializedName("associated_packages")
    @Expose
    private String associated_packages;
    @SerializedName("category_id")
    @Expose
        private int category_id;
    @SerializedName("company_url")
    @Expose
        private String company_url;
    @SerializedName("countdown_date")
    @Expose
        private String countdown_date;
    @SerializedName("countdown_timezone")
    @Expose
        private int countdown_timezone;
    @SerializedName("counter_live_limit")
    @Expose
        private int counter_live_limit;
    @SerializedName("countries_id")
    @Expose
        private int countries_id;
    @SerializedName("creation_date")
    @Expose
        private String creation_date;
    @SerializedName("custom_data")
    @Expose
        private String custom_data;
    @SerializedName("noframe_security")
    @Expose
        private int noframe_security;
    @SerializedName("description")
    @Expose
        private String description;
    @SerializedName("autoplay")
    @Expose
        private boolean autoplay;
    @SerializedName("enable_coupon")
    @Expose
        private boolean enable_coupon;
    @SerializedName("online")
    @Expose
        private boolean online;
    @SerializedName("enable_payperview")
    @Expose
        private boolean enable_payperview;
    @SerializedName("publish_on_dacast")
    @Expose
        private boolean publish_on_dacast;
    @SerializedName("enable_subscription")
    @Expose
        private boolean enable_subscription;
    @SerializedName("external_video_page")
    @Expose
        private String external_video_page;
    @SerializedName("google_analytics")
    @Expose
        private int google_analytics;
    @SerializedName("hds")
    @Expose
        private String hds;
    @SerializedName("hls")
    @Expose
        private String hls;
    @SerializedName("id")
    @Expose
        private int id;
    @SerializedName("is_secure")
    @Expose
        private int is_secure;
    @SerializedName("password")
    @Expose
        private String password;
    @SerializedName("pictures")
    @Expose
        private Pictures pictures;
    @SerializedName("player_height")
    @Expose
        private int player_height;
    @SerializedName("player_width")
    @Expose
        private int player_width;
    @SerializedName("player_size_id")
    @Expose
        private int player_size_id;
    @SerializedName("config")
    @Expose
        private Config config;

    @SerializedName("referers_id")
    @Expose
        private int referers_id;

    @SerializedName("rtmp")
    @Expose
        private String rtmp;

    @SerializedName("save_date")
    @Expose
        private String save_date;
    @SerializedName("schedule")
    @Expose
        private String schedule;
    @SerializedName("share_code")
    @Expose
        private ShareCode share_code;
    @SerializedName("splashscreen_id")
    @Expose
        private int splashscreen_id;
    @SerializedName("stream_tech")
    @Expose
        private String stream_tech;
    @SerializedName("theme_id")
    @Expose
        private int theme_id;
    @SerializedName("umbnail_id")
    @Expose
        private int thumbnail_id;
    @SerializedName("title")
    @Expose
        private String title;
    @SerializedName("web_dvr")
    @Expose
        private int web_dvr;

        public void setAds(String ads){
            this.ads = ads;
        }
        public String getAds(){
            return this.ads;
        }
        public void setAssociated_packages(String associated_packages){
            this.associated_packages = associated_packages;
        }
        public String getAssociated_packages(){
            return this.associated_packages;
        }
        public void setCategory_id(int category_id){
            this.category_id = category_id;
        }
        public int getCategory_id(){
            return this.category_id;
        }
        public void setCompany_url(String company_url){
            this.company_url = company_url;
        }
        public String getCompany_url(){
            return this.company_url;
        }
        public void setCountdown_date(String countdown_date){
            this.countdown_date = countdown_date;
        }
        public String getCountdown_date(){
            return this.countdown_date;
        }
        public void setCountdown_timezone(int countdown_timezone){
            this.countdown_timezone = countdown_timezone;
        }
        public int getCountdown_timezone(){
            return this.countdown_timezone;
        }
        public void setCounter_live_limit(int counter_live_limit){
            this.counter_live_limit = counter_live_limit;
        }
        public int getCounter_live_limit(){
            return this.counter_live_limit;
        }
        public void setCountries_id(int countries_id){
            this.countries_id = countries_id;
        }
        public int getCountries_id(){
            return this.countries_id;
        }
        public void setCreation_date(String creation_date){
            this.creation_date = creation_date;
        }
        public String getCreation_date(){
            return this.creation_date;
        }
        public void setCustom_data(String custom_data){
            this.custom_data = custom_data;
        }
        public String getCustom_data(){
            return this.custom_data;
        }
        public void setNoframe_security(int noframe_security){
            this.noframe_security = noframe_security;
        }
        public int getNoframe_security(){
            return this.noframe_security;
        }
        public void setDescription(String description){
            this.description = description;
        }
        public String getDescription(){
            return this.description;
        }
        public void setAutoplay(boolean autoplay){
            this.autoplay = autoplay;
        }
        public boolean getAutoplay(){
            return this.autoplay;
        }
        public void setEnable_coupon(boolean enable_coupon){
            this.enable_coupon = enable_coupon;
        }
        public boolean getEnable_coupon(){
            return this.enable_coupon;
        }
        public void setOnline(boolean online){
            this.online = online;
        }
        public boolean getOnline(){
            return this.online;
        }
        public void setEnable_payperview(boolean enable_payperview){
            this.enable_payperview = enable_payperview;
        }
        public boolean getEnable_payperview(){
            return this.enable_payperview;
        }
        public void setPublish_on_dacast(boolean publish_on_dacast){
            this.publish_on_dacast = publish_on_dacast;
        }
        public boolean getPublish_on_dacast(){
            return this.publish_on_dacast;
        }
        public void setEnable_subscription(boolean enable_subscription){
            this.enable_subscription = enable_subscription;
        }
        public boolean getEnable_subscription(){
            return this.enable_subscription;
        }
        public void setExternal_video_page(String external_video_page){
            this.external_video_page = external_video_page;
        }
        public String getExternal_video_page(){
            return this.external_video_page;
        }
        public void setGoogle_analytics(int google_analytics){
            this.google_analytics = google_analytics;
        }
        public int getGoogle_analytics(){
            return this.google_analytics;
        }
        public void setHds(String hds){
            this.hds = hds;
        }
        public String getHds(){
            return this.hds;
        }
        public void setHls(String hls){
            this.hls = hls;
        }
        public String getHls(){
            return this.hls;
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setIs_secure(int is_secure){
            this.is_secure = is_secure;
        }
        public int getIs_secure(){
            return this.is_secure;
        }
        public void setPassword(String password){
            this.password = password;
        }
        public String getPassword(){
            return this.password;
        }
        public void setPictures(Pictures pictures){
            this.pictures = pictures;
        }
        public Pictures getPictures(){
            return this.pictures;
        }
        public void setPlayer_height(int player_height){
            this.player_height = player_height;
        }
        public int getPlayer_height(){
            return this.player_height;
        }
        public void setPlayer_width(int player_width){
            this.player_width = player_width;
        }
        public int getPlayer_width(){
            return this.player_width;
        }
        public void setPlayer_size_id(int player_size_id){
            this.player_size_id = player_size_id;
        }
        public int getPlayer_size_id(){
            return this.player_size_id;
        }
        public void setConfig(Config config){
            this.config = config;
        }
        public Config getConfig(){
            return this.config;
        }
        public void setReferers_id(int referers_id){
            this.referers_id = referers_id;
        }
        public int getReferers_id(){
            return this.referers_id;
        }
        public void setRtmp(String rtmp){
            this.rtmp = rtmp;
        }
        public String getRtmp(){
            return this.rtmp;
        }
        public void setSave_date(String save_date){
            this.save_date = save_date;
        }
        public String getSave_date(){
            return this.save_date;
        }
        public void setSchedule(String schedule){
            this.schedule = schedule;
        }
        public String getSchedule(){
            return this.schedule;
        }
        public void setShare_code(ShareCode share_code){
            this.share_code = share_code;
        }
        public ShareCode getShare_code(){
            return this.share_code;
        }
        public void setSplashscreen_id(int splashscreen_id){
            this.splashscreen_id = splashscreen_id;
        }
        public int getSplashscreen_id(){
            return this.splashscreen_id;
        }
        public void setStream_tech(String stream_tech){
            this.stream_tech = stream_tech;
        }
        public String getStream_tech(){
            return this.stream_tech;
        }
        public void setTheme_id(int theme_id){
            this.theme_id = theme_id;
        }
        public int getTheme_id(){
            return this.theme_id;
        }
        public void setThumbnail_id(int thumbnail_id){
            this.thumbnail_id = thumbnail_id;
        }
        public int getThumbnail_id(){
            return this.thumbnail_id;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setWeb_dvr(int web_dvr){
            this.web_dvr = web_dvr;
        }
        public int getWeb_dvr(){
            return this.web_dvr;
        }
}
