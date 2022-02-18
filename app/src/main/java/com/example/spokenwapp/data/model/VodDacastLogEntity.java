package com.example.spokenwapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.spokenwapp.dacastvod.VodPictures;

import java.util.List;


@Entity(tableName = "dacastvodlog_table", indices = {@Index(value = {"contentIds"}, unique = true)})
public class VodDacastLogEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "contentIds")
    private String contentIds;

    @ColumnInfo(name = "totalCount")
    private String totalCount;

    @ColumnInfo(name = "abitrate")
    private Integer abitrate;

    @ColumnInfo(name = "acodec")
    private String acodec;

    @ColumnInfo(name ="ads")
    private String ads;

    @ColumnInfo(name = "associated_packages")
    private String associatedPackages;

    @ColumnInfo(name = "category_id")
    private Integer categoryId;

    @ColumnInfo(name = "container")
    private String container;

    @ColumnInfo(name = "countries_id")
    private Integer countriesId;

    @ColumnInfo(name = "creation_date")
    private String creationDate;

    @ColumnInfo(name = "custom_data")
    private String customData;

    @ColumnInfo(name = "noframe_security")
    private Integer noframeSecurity;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "disk_usage")
    private String diskUsage;

    @ColumnInfo(name = "duration")
    private String duration;

    @ColumnInfo(name = "autoplay")
    private Boolean autoplay;

    @ColumnInfo(name = "enable_coupon")
    private Boolean enableCoupon;

    @ColumnInfo(name = "online")
    private Boolean online;

    @ColumnInfo(name = "enable_payperview")
    private Boolean enablePayperview;

    @ColumnInfo(name = "publish_on_dacast")
    private Boolean publishOnDacast;

    @ColumnInfo(name = "enable_subscription")
    private Boolean enableSubscription;

    @ColumnInfo(name = "external_video_page")
    private String externalVideoPage;

    @ColumnInfo(name = "filename")
    private String filename;

    @ColumnInfo(name = "filesize")
    private Integer filesize;

    @ColumnInfo(name = "google_analytics")
    private Integer googleAnalytics;

    @ColumnInfo(name = "group_id")
    private Integer groupId;

    @ColumnInfo(name = "hds")
    private String hds;

    @ColumnInfo(name = "hls")
    private String hls;

    @ColumnInfo(name = "is_secured")
    private Boolean isSecured;

    @ColumnInfo(name = "original_id")
    private Integer originalId;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "player_height")
    private Integer playerHeight;

    @ColumnInfo(name = "player_width")
    private Integer playerWidth;

    @ColumnInfo(name = "player_size_id")
    private Integer playerSizeId;

    @ColumnInfo(name = "referers_id")
    private Integer referersId;

    @ColumnInfo(name = "save_date")
    private String saveDate;

    @ColumnInfo(name = "splashscreen_id")
    private Integer splashscreenId;

    @ColumnInfo(name = "streamable")
    private Integer streamable;

    @ColumnInfo(name = "subtitles")
    private String subtitles;

    @ColumnInfo(name = "template_id")
    private Integer templateId;

    @ColumnInfo(name = "theme_id")
    private Integer themeId;

    @ColumnInfo(name = "thumbnail_id")
    private Integer thumbnailId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "vbitrate")
    private Integer vbitrate;

    @ColumnInfo(name = "vcodec")
    private String vcodec;

    @ColumnInfo(name = "video_height")
    private Integer videoHeight;

    @ColumnInfo(name = "video_width")
    private Integer videoWidth;

    @ColumnInfo(name = "thumbnail")
    @TypeConverters(ListConverters.class)
    private List<String> thumbnail;

    @ColumnInfo(name = "splashscreen")
    @TypeConverters(ListConverters.class)
    private List<String> splashscreen;

    @ColumnInfo(name = "facebook")
    private String facebook;

    @ColumnInfo(name = "tweeter")
    private String tweeter;

    @Ignore
    public VodDacastLogEntity(int id, String contentIds, String totalCount, Integer abitrate,
                              String acodec, String ads, String associatedPackages, Integer categoryId,
                              String container, Integer countriesId, String creationDate, String customData,
                              Integer noframeSecurity, String description, String diskUsage, String duration,
                              Boolean autoplay, Boolean enableCoupon, Boolean online, Boolean enablePayperview,
                              Boolean publishOnDacast, Boolean enableSubscription, String externalVideoPage,
                              String filename, Integer filesize, Integer googleAnalytics, Integer groupId,
                              String hds, String hls, Boolean isSecured, Integer originalId, String password,
                              VodPictures pictures, Integer playerHeight, Integer playerWidth,
                              Integer playerSizeId, Integer referersId, String saveDate, Integer splashscreenId,
                              Integer streamable, String subtitles, Integer templateId, Integer themeId,
                              Integer thumbnailId, String title, Integer vbitrate, String vcodec,
                              Integer videoHeight, Integer videoWidth, List<String> thumbnail,
                              List<String> splashscreen, String facebook, String tweeter) {
        this.id = id;
        this.contentIds = contentIds;
        this.totalCount = totalCount;
        this.abitrate = abitrate;
        this.acodec = acodec;
        this.ads = ads;
        this.associatedPackages = associatedPackages;
        this.categoryId = categoryId;
        this.container = container;
        this.countriesId = countriesId;
        this.creationDate = creationDate;
        this.customData = customData;
        this.noframeSecurity = noframeSecurity;
        this.description = description;
        this.diskUsage = diskUsage;
        this.duration = duration;
        this.autoplay = autoplay;
        this.enableCoupon = enableCoupon;
        this.online = online;
        this.enablePayperview = enablePayperview;
        this.publishOnDacast = publishOnDacast;
        this.enableSubscription = enableSubscription;
        this.externalVideoPage = externalVideoPage;
        this.filename = filename;
        this.filesize = filesize;
        this.googleAnalytics = googleAnalytics;
        this.groupId = groupId;
        this.hds = hds;
        this.hls = hls;
        this.isSecured = isSecured;
        this.originalId = originalId;
        this.password = password;
        this.playerHeight = playerHeight;
        this.playerWidth = playerWidth;
        this.playerSizeId = playerSizeId;
        this.referersId = referersId;
        this.saveDate = saveDate;
        this.splashscreenId = splashscreenId;
        this.streamable = streamable;
        this.subtitles = subtitles;
        this.templateId = templateId;
        this.themeId = themeId;
        this.thumbnailId = thumbnailId;
        this.title = title;
        this.vbitrate = vbitrate;
        this.vcodec = vcodec;
        this.videoHeight = videoHeight;
        this.videoWidth = videoWidth;
        this.thumbnail = thumbnail;
        this.splashscreen = splashscreen;
        this.facebook = facebook;
        this.tweeter = tweeter;
    }

    public VodDacastLogEntity(String contentIds, String totalCount, Integer abitrate,
                              String acodec, String ads, String associatedPackages, Integer categoryId,
                              String container, Integer countriesId, String creationDate, String customData,
                              Integer noframeSecurity, String description, String diskUsage, String duration,
                              Boolean autoplay, Boolean enableCoupon, Boolean online, Boolean enablePayperview,
                              Boolean publishOnDacast, Boolean enableSubscription, String externalVideoPage,
                              String filename, Integer filesize, Integer googleAnalytics, Integer groupId,
                              String hds, String hls, Boolean isSecured, Integer originalId, String password,
                              Integer playerHeight, Integer playerWidth, Integer playerSizeId, Integer referersId,
                              String saveDate, Integer splashscreenId, Integer streamable, String subtitles,
                              Integer templateId, Integer themeId, Integer thumbnailId, String title, Integer vbitrate,
                              String vcodec, Integer videoHeight, Integer videoWidth, List<String> thumbnail,
                              List<String> splashscreen, String facebook, String tweeter) {
        this.contentIds = contentIds;
        this.totalCount = totalCount;
        this.abitrate = abitrate;
        this.acodec = acodec;
        this.ads = ads;
        this.associatedPackages = associatedPackages;
        this.categoryId = categoryId;
        this.container = container;
        this.countriesId = countriesId;
        this.creationDate = creationDate;
        this.customData = customData;
        this.noframeSecurity = noframeSecurity;
        this.description = description;
        this.diskUsage = diskUsage;
        this.duration = duration;
        this.autoplay = autoplay;
        this.enableCoupon = enableCoupon;
        this.online = online;
        this.enablePayperview = enablePayperview;
        this.publishOnDacast = publishOnDacast;
        this.enableSubscription = enableSubscription;
        this.externalVideoPage = externalVideoPage;
        this.filename = filename;
        this.filesize = filesize;
        this.googleAnalytics = googleAnalytics;
        this.groupId = groupId;
        this.hds = hds;
        this.hls = hls;
        this.isSecured = isSecured;
        this.originalId = originalId;
        this.password = password;
        this.playerHeight = playerHeight;
        this.playerWidth = playerWidth;
        this.playerSizeId = playerSizeId;
        this.referersId = referersId;
        this.saveDate = saveDate;
        this.splashscreenId = splashscreenId;
        this.streamable = streamable;
        this.subtitles = subtitles;
        this.templateId = templateId;
        this.themeId = themeId;
        this.thumbnailId = thumbnailId;
        this.title = title;
        this.vbitrate = vbitrate;
        this.vcodec = vcodec;
        this.videoHeight = videoHeight;
        this.videoWidth = videoWidth;
        this.thumbnail = thumbnail;
        this.splashscreen = splashscreen;
        this.facebook = facebook;
        this.tweeter = tweeter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentIds() {
        return contentIds;
    }

    public void setContentIds(String contentIds) {
        this.contentIds = contentIds;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getAbitrate() {
        return abitrate;
    }

    public void setAbitrate(Integer abitrate) {
        this.abitrate = abitrate;
    }

    public String getAcodec() {
        return acodec;
    }

    public void setAcodec(String acodec) {
        this.acodec = acodec;
    }

    public String getAds() {
        return ads;
    }

    public void setAds(String ads) {
        this.ads = ads;
    }

    public String getAssociatedPackages() {
        return associatedPackages;
    }

    public void setAssociatedPackages(String associatedPackages) {
        this.associatedPackages = associatedPackages;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public Integer getCountriesId() {
        return countriesId;
    }

    public void setCountriesId(Integer countriesId) {
        this.countriesId = countriesId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public Integer getNoframeSecurity() {
        return noframeSecurity;
    }

    public void setNoframeSecurity(Integer noframeSecurity) {
        this.noframeSecurity = noframeSecurity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(String diskUsage) {
        this.diskUsage = diskUsage;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Boolean getAutoplay() {
        return autoplay;
    }

    public void setAutoplay(Boolean autoplay) {
        this.autoplay = autoplay;
    }

    public Boolean getEnableCoupon() {
        return enableCoupon;
    }

    public void setEnableCoupon(Boolean enableCoupon) {
        this.enableCoupon = enableCoupon;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getEnablePayperview() {
        return enablePayperview;
    }

    public void setEnablePayperview(Boolean enablePayperview) {
        this.enablePayperview = enablePayperview;
    }

    public Boolean getPublishOnDacast() {
        return publishOnDacast;
    }

    public void setPublishOnDacast(Boolean publishOnDacast) {
        this.publishOnDacast = publishOnDacast;
    }

    public Boolean getEnableSubscription() {
        return enableSubscription;
    }

    public void setEnableSubscription(Boolean enableSubscription) {
        this.enableSubscription = enableSubscription;
    }

    public String getExternalVideoPage() {
        return externalVideoPage;
    }

    public void setExternalVideoPage(String externalVideoPage) {
        this.externalVideoPage = externalVideoPage;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getFilesize() {
        return filesize;
    }

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    public Integer getGoogleAnalytics() {
        return googleAnalytics;
    }

    public void setGoogleAnalytics(Integer googleAnalytics) {
        this.googleAnalytics = googleAnalytics;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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

    public Boolean getSecured() {
        return isSecured;
    }

    public void setSecured(Boolean secured) {
        isSecured = secured;
    }

    public Integer getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Integer originalId) {
        this.originalId = originalId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPlayerHeight() {
        return playerHeight;
    }

    public void setPlayerHeight(Integer playerHeight) {
        this.playerHeight = playerHeight;
    }

    public Integer getPlayerWidth() {
        return playerWidth;
    }

    public void setPlayerWidth(Integer playerWidth) {
        this.playerWidth = playerWidth;
    }

    public Integer getPlayerSizeId() {
        return playerSizeId;
    }

    public void setPlayerSizeId(Integer playerSizeId) {
        this.playerSizeId = playerSizeId;
    }

    public Integer getReferersId() {
        return referersId;
    }

    public void setReferersId(Integer referersId) {
        this.referersId = referersId;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public Integer getSplashscreenId() {
        return splashscreenId;
    }

    public void setSplashscreenId(Integer splashscreenId) {
        this.splashscreenId = splashscreenId;
    }

    public Integer getStreamable() {
        return streamable;
    }

    public void setStreamable(Integer streamable) {
        this.streamable = streamable;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    public Integer getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(Integer thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getVbitrate() {
        return vbitrate;
    }

    public void setVbitrate(Integer vbitrate) {
        this.vbitrate = vbitrate;
    }

    public String getVcodec() {
        return vcodec;
    }

    public void setVcodec(String vcodec) {
        this.vcodec = vcodec;
    }

    public Integer getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(Integer videoHeight) {
        this.videoHeight = videoHeight;
    }

    public Integer getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(Integer videoWidth) {
        this.videoWidth = videoWidth;
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


}
