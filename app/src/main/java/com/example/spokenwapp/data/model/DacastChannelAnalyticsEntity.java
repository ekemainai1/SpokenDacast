package com.example.spokenwapp.data.model;

import androidx.room.Ignore;
import androidx.room.TypeConverters;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DacastChannelAnalyticsEntity {

    @SerializedName("id")
    public int id;

    @SerializedName("contentId")
    public String contentId;

    @SerializedName("description")
    public String description;

    @SerializedName("countdown_date")
    public String countdown_date;

    @SerializedName("creation_date")
    public String creation_date;

    @SerializedName("hds")
    public String hds;

    @SerializedName("hls")
    public String hls;

    @SerializedName("thumbnail")
    @TypeConverters(ListConverters.class)
    public List<String> thumbnail;

    @SerializedName("splashscreen")
    @TypeConverters(ListConverters.class)
    public List<String> splashscreen;

    @SerializedName("facebook")
    public String facebook;

    @SerializedName("tweeter")
    public String tweeter;

    @SerializedName("gplus")
    public String gplus;

    @SerializedName("online")
    public boolean online;

    @SerializedName("publish_on_dacast")
    public boolean publish_on_dacast;

    @SerializedName("ads")
    public String ads;

    @SerializedName("associated_packages")
    public String associated_packages;

    @SerializedName("category_id")
    public int category_id;

    @SerializedName("company_url")
    public String company_url;

    @SerializedName("countdown_timezone")
    public int countdown_timezone;

    @SerializedName("counter_live_limit")
    public int counter_live_limit;

    @SerializedName("countries_id")
    public int countries_id;

    @SerializedName("custom_data")
    public String custom_data;

    @SerializedName("noframe_security")
    public int noframe_security;

    @SerializedName("autoplay")
    public boolean autoplay;

    @SerializedName("enable_coupon")
    public boolean enable_coupon;

    @SerializedName("enable_payperview")
    public boolean enable_payperview;

    @SerializedName("enable_subscription")
    public boolean enable_subscription;

    @SerializedName("external_video_page")
    public String external_video_page;

    @SerializedName("google_analytics")
    public int google_analytics;

    @SerializedName("is_secure")
    public int is_secure;

    @SerializedName("password")
    public String password;

    @SerializedName("player_height")
    public int player_height;

    @SerializedName("player_width")
    public int player_width;

    @SerializedName("player_size_id")
    public int player_size_id;

    @SerializedName("referers_id")
    public int referers_id;

    @SerializedName("rtmp")
    public String rtmp;

    @SerializedName("save_date")
    public String save_date;

    @SerializedName("schedule")
    public String schedule;

    @SerializedName("splashscreen_id")
    public int splashscreen_id;

    @SerializedName("stream_tech")
    public String stream_tech;

    @SerializedName("theme_id")
    public int theme_id;

    @SerializedName("thumbnail_id")
    public int thumbnail_id;

    @SerializedName("title")
    public String title;

    @SerializedName("web_dvr")
    public int web_dvr;

    @SerializedName("analytics_id")
    public int analytics_id;

    @SerializedName("visitors")
    public Integer visitors;

    @SerializedName("hits")
    public Integer hits;

    @SerializedName("channel")
    public String channel;

    @Ignore
    public DacastChannelAnalyticsEntity(int id, String contentId, String description,
                           String countdown_date, String creation_date, String hds, String hls,
                           List<String> thumbnail, List<String> splashscreen, String facebook,
                           String tweeter, String gplus, boolean online, boolean publish_on_dacast,
                           String ads, String associated_packages, int category_id, String company_url,
                           int countdown_timezone, int counter_live_limit, int countries_id,
                           String custom_data, int noframe_security, boolean autoplay, boolean enable_coupon,
                           boolean enable_payperview, boolean enable_subscription,
                           String external_video_page, int google_analytics, int is_secure,
                           String password, int player_height, int player_width,
                           int player_size_id, int referers_id, String rtmp,
                           String save_date, String schedule, int splashscreen_id,
                           String stream_tech, int theme_id, int thumbnail_id, String title, int web_dvr,
                                        int analytics_id, Integer visitors, Integer hits, String channel) {
        this.id = id;
        this.contentId = contentId;
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
        this.publish_on_dacast = publish_on_dacast;
        this.ads = ads;
        this.associated_packages = associated_packages;
        this.category_id = category_id;
        this.company_url = company_url;
        this.countdown_timezone = countdown_timezone;
        this.counter_live_limit = counter_live_limit;
        this.countries_id = countries_id;
        this.custom_data = custom_data;
        this.noframe_security = noframe_security;
        this.autoplay = autoplay;
        this.enable_coupon = enable_coupon;
        this.enable_payperview = enable_payperview;
        this.enable_subscription = enable_subscription;
        this.external_video_page = external_video_page;
        this.google_analytics = google_analytics;
        this.is_secure = is_secure;
        this.password = password;
        this.player_height = player_height;
        this.player_width = player_width;
        this.player_size_id = player_size_id;
        this.referers_id = referers_id;
        this.rtmp = rtmp;
        this.save_date = save_date;
        this.schedule = schedule;
        this.splashscreen_id = splashscreen_id;
        this.stream_tech = stream_tech;
        this.theme_id = theme_id;
        this.thumbnail_id = thumbnail_id;
        this.title = title;
        this.web_dvr = web_dvr;
        this.analytics_id = analytics_id;
        this.visitors = visitors;
        this.hits = hits;
        this.channel = channel;

    }

    public DacastChannelAnalyticsEntity(String contentId, String description, String countdown_date,
                           String creation_date, String hds, String hls, List<String> thumbnail,
                           List<String> splashscreen, String facebook, String tweeter, String gplus,
                           boolean online, boolean publish_on_dacast, String ads,
                           String associated_packages, int category_id, String company_url,
                           int countdown_timezone, int counter_live_limit, int countries_id,
                           String custom_data, int noframe_security, boolean autoplay,
                           boolean enable_coupon, boolean enable_payperview, boolean enable_subscription,
                           String external_video_page, int google_analytics, int is_secure,
                           String password, int player_height, int player_width,
                           int player_size_id, int referers_id, String rtmp,
                           String save_date, String schedule, int splashscreen_id,
                           String stream_tech, int theme_id, int thumbnail_id, String title, int web_dvr,
                                        int analytics_id, Integer visitors, Integer hits, String channel) {

        this.contentId = contentId;
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
        this.publish_on_dacast = publish_on_dacast;
        this.ads = ads;
        this.associated_packages = associated_packages;
        this.category_id = category_id;
        this.company_url = company_url;
        this.countdown_timezone = countdown_timezone;
        this.counter_live_limit = counter_live_limit;
        this.countries_id = countries_id;
        this.custom_data = custom_data;
        this.noframe_security = noframe_security;
        this.autoplay = autoplay;
        this.enable_coupon = enable_coupon;
        this.enable_payperview = enable_payperview;
        this.enable_subscription = enable_subscription;
        this.external_video_page = external_video_page;
        this.google_analytics = google_analytics;
        this.is_secure = is_secure;
        this.password = password;
        this.player_height = player_height;
        this.player_width = player_width;
        this.player_size_id = player_size_id;
        this.referers_id = referers_id;
        this.rtmp = rtmp;
        this.save_date = save_date;
        this.schedule = schedule;
        this.splashscreen_id = splashscreen_id;
        this.stream_tech = stream_tech;
        this.theme_id = theme_id;
        this.thumbnail_id = thumbnail_id;
        this.title = title;
        this.web_dvr = web_dvr;
        this.analytics_id = analytics_id;
        this.visitors = visitors;
        this.hits = hits;
        this.channel = channel;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
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

    public boolean getPublish_on_dacast() {
        return publish_on_dacast;
    }

    public void setPublish_on_dacast(boolean publish_on_dacast) {
        this.publish_on_dacast = publish_on_dacast;
    }

    public String getAds() {
        return ads;
    }

    public void setAds(String ads) {
        this.ads = ads;
    }

    public String getAssociated_packages() {
        return associated_packages;
    }

    public void setAssociated_packages(String associated_packages) {
        this.associated_packages = associated_packages;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCompany_url() {
        return company_url;
    }

    public void setCompany_url(String company_url) {
        this.company_url = company_url;
    }

    public int getCountdown_timezone() {
        return countdown_timezone;
    }

    public void setCountdown_timezone(int countdown_timezone) {
        this.countdown_timezone = countdown_timezone;
    }

    public int getCounter_live_limit() {
        return counter_live_limit;
    }

    public void setCounter_live_limit(int counter_live_limit) {
        this.counter_live_limit = counter_live_limit;
    }

    public int getCountries_id() {
        return countries_id;
    }

    public void setCountries_id(int countries_id) {
        this.countries_id = countries_id;
    }

    public String getCustom_data() {
        return custom_data;
    }

    public void setCustom_data(String custom_data) {
        this.custom_data = custom_data;
    }

    public int getNoframe_security() {
        return noframe_security;
    }

    public void setNoframe_security(int noframe_security) {
        this.noframe_security = noframe_security;
    }

    public boolean getAutoplay() {
        return autoplay;
    }

    public void setAutoplay(boolean autoplay) {
        this.autoplay = autoplay;
    }

    public boolean getEnable_coupon() {
        return enable_coupon;
    }

    public void setEnable_coupon(boolean enable_coupon) {
        this.enable_coupon = enable_coupon;
    }

    public boolean getEnable_payperview() {
        return enable_payperview;
    }

    public void setEnable_payperview(boolean enable_payperview) {
        this.enable_payperview = enable_payperview;
    }

    public boolean getEnable_subscription() {
        return enable_subscription;
    }

    public void setEnable_subscription(boolean enable_subscription) {
        this.enable_subscription = enable_subscription;
    }

    public String getExternal_video_page() {
        return external_video_page;
    }

    public void setExternal_video_page(String external_video_page) {
        this.external_video_page = external_video_page;
    }

    public int getGoogle_analytics() {
        return google_analytics;
    }

    public void setGoogle_analytics(int google_analytics) {
        this.google_analytics = google_analytics;
    }

    public int getIs_secure() {
        return is_secure;
    }

    public void setIs_secure(int is_secure) {
        this.is_secure = is_secure;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPlayer_height() {
        return player_height;
    }

    public void setPlayer_height(int player_height) {
        this.player_height = player_height;
    }

    public int getPlayer_width() {
        return player_width;
    }

    public void setPlayer_width(int player_width) {
        this.player_width = player_width;
    }

    public int getPlayer_size_id() {
        return player_size_id;
    }

    public void setPlayer_size_id(int player_size_id) {
        this.player_size_id = player_size_id;
    }

    public int getReferers_id() {
        return referers_id;
    }

    public void setReferers_id(int referers_id) {
        this.referers_id = referers_id;
    }

    public String getRtmp() {
        return rtmp;
    }

    public void setRtmp(String rtmp) {
        this.rtmp = rtmp;
    }

    public String getSave_date() {
        return save_date;
    }

    public void setSave_date(String save_date) {
        this.save_date = save_date;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getSplashscreen_id() {
        return splashscreen_id;
    }

    public void setSplashscreen_id(int splashscreen_id) {
        this.splashscreen_id = splashscreen_id;
    }

    public String getStream_tech() {
        return stream_tech;
    }

    public void setStream_tech(String stream_tech) {
        this.stream_tech = stream_tech;
    }

    public int getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(int theme_id) {
        this.theme_id = theme_id;
    }

    public int getThumbnail_id() {
        return thumbnail_id;
    }

    public void setThumbnail_id(int thumbnail_id) {
        this.thumbnail_id = thumbnail_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWeb_dvr() {
        return web_dvr;
    }

    public void setWeb_dvr(int web_dvr) {
        this.web_dvr = web_dvr;
    }
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
