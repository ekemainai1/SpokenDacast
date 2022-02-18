package com.example.spokenwapp.dacastvod;

import com.example.spokenwapp.dacast.Pictures;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VodData {

        @SerializedName("abitrate")
        @Expose
        private Integer abitrate;
        @SerializedName("acodec")
        @Expose
        private String acodec;
        @SerializedName("ads")
        @Expose
        private String ads;
        @SerializedName("associated_packages")
        @Expose
        private String associatedPackages;
        @SerializedName("category_id")
        @Expose
        private Integer categoryId;
        @SerializedName("container")
        @Expose
        private String container;
        @SerializedName("countries_id")
        @Expose
        private Integer countriesId;
        @SerializedName("creation_date")
        @Expose
        private String creationDate;
        @SerializedName("custom_data")
        @Expose
        private String customData;
        @SerializedName("noframe_security")
        @Expose
        private Integer noframeSecurity;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("disk_usage")
        @Expose
        private String diskUsage;
        @SerializedName("duration")
        @Expose
        private String duration;
        @SerializedName("autoplay")
        @Expose
        private Boolean autoplay;
        @SerializedName("enable_coupon")
        @Expose
        private Boolean enableCoupon;
        @SerializedName("online")
        @Expose
        private Boolean online;
        @SerializedName("enable_payperview")
        @Expose
        private Boolean enablePayperview;
        @SerializedName("publish_on_dacast")
        @Expose
        private Boolean publishOnDacast;
        @SerializedName("enable_subscription")
        @Expose
        private Boolean enableSubscription;
        @SerializedName("external_video_page")
        @Expose
        private String externalVideoPage;
        @SerializedName("filename")
        @Expose
        private String filename;
        @SerializedName("filesize")
        @Expose
        private Integer filesize;
        @SerializedName("google_analytics")
        @Expose
        private Integer googleAnalytics;
        @SerializedName("group_id")
        @Expose
        private Integer groupId;
        @SerializedName("hds")
        @Expose
        private String hds;
        @SerializedName("hls")
        @Expose
        private String hls;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("is_secured")
        @Expose
        private Boolean isSecured;
        @SerializedName("original_id")
        @Expose
        private Integer originalId;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("pictures")
        @Expose
        private VodPictures pictures;
        @SerializedName("player_height")
        @Expose
        private Integer playerHeight;
        @SerializedName("player_width")
        @Expose
        private Integer playerWidth;
        @SerializedName("player_size_id")
        @Expose
        private Integer playerSizeId;
        @SerializedName("referers_id")
        @Expose
        private Integer referersId;
        @SerializedName("save_date")
        @Expose
        private String saveDate;
        @SerializedName("share_code")
        @Expose
        private VodShareCode shareCode;
        @SerializedName("splashscreen_id")
        @Expose
        private Integer splashscreenId;
        @SerializedName("streamable")
        @Expose
        private Integer streamable;
        @SerializedName("subtitles")
        @Expose
        private String subtitles;
        @SerializedName("template_id")
        @Expose
        private Integer templateId;
        @SerializedName("theme_id")
        @Expose
        private Integer themeId;
        @SerializedName("thumbnail_id")
        @Expose
        private Integer thumbnailId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("vbitrate")
        @Expose
        private Integer vbitrate;
        @SerializedName("vcodec")
        @Expose
        private String vcodec;
        @SerializedName("video_height")
        @Expose
        private Integer videoHeight;
        @SerializedName("video_width")
        @Expose
        private Integer videoWidth;

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

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Boolean getIsSecured() {
            return isSecured;
        }

        public void setIsSecured(Boolean isSecured) {
            this.isSecured = isSecured;
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

        public VodPictures getPictures() {
            return pictures;
        }

        public void setPictures(VodPictures pictures) {
            this.pictures = pictures;
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

        public VodShareCode getShareCode() {
            return shareCode;
        }

        public void setShareCode(VodShareCode shareCode) {
            this.shareCode = shareCode;
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

}
