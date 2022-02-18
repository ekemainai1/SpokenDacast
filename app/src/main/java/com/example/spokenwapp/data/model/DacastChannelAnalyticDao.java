package com.example.spokenwapp.data.model;

import androidx.room.Dao;
import androidx.room.Query;
import java.util.List;
import io.reactivex.Flowable;

@Dao
public interface DacastChannelAnalyticDao {

    @Query("SELECT dacastlog_table.id AS id, dacastlog_table.contentId AS contentId, " +
            "dacastlog_table.description AS description, dacastlog_table.countdown_date AS countdown_date, dacastlog_table.creation_date AS creation_date, " +
            "dacastlog_table.hds AS hds, dacastlog_table.hls AS hls, dacastlog_table.thumbnail AS thumbnail, " +
            "dacastlog_table.splashscreen AS splashscreen, dacastlog_table.facebook AS facebook, dacastlog_table.tweeter AS tweeter, " +
            "dacastlog_table.gplus AS gplus, dacastlog_table.online AS online, dacastlog_table.publish_on_dacast AS publish_on_dacast," +
            "dacastlog_table.ads AS ads, dacastlog_table.associated_packages AS associated_pakages, " +
            "dacastlog_table.category_id AS category_id, dacastlog_table.company_url AS company_url, " +
            "dacastlog_table.countdown_timezone AS countdown_timezone, dacastlog_table.counter_live_limit AS counter_live_limit, " +
            "dacastlog_table.countries_id AS countries_id, dacastlog_table.custom_data AS custom_data, " +
            "dacastlog_table.noframe_security AS noframe_security, dacastlog_table.autoplay AS autoplay, " +
            "dacastlog_table.enable_coupon AS enable_coupon, dacastlog_table.enable_payperview AS enable_payperview, " +
            "dacastlog_table.enable_subscription AS enable_subscription, dacastlog_table.external_video_page AS external_video_page, " +
            "dacastlog_table.google_analytics AS google_analytics, dacastlog_table.is_secure AS is_secure, " +
            "dacastlog_table.password AS password, dacastlog_table.player_height AS player_height, " +
            "dacastlog_table.player_width AS player_width, dacastlog_table.player_size_id AS player_size_id, " +
            "dacastlog_table.referers_id AS referers_id, dacastlog_table.rtmp AS rtmp, " +
            "dacastlog_table.save_date AS save_date,  dacastlog_table.schedule AS schedule, dacastlog_table.splashscreen_id AS splashscreen_id, " +
            "dacastlog_table.stream_tech AS stream_tech, dacastlog_table.theme_id AS theme_id, " +
            "dacastlog_table.thumbnail_id AS thumbnail_id, dacastlog_table.title AS title, " +
            "dacastlog_table.web_dvr AS web_dvr, dacastlog_table.google_analytics AS google_analytics, dacastanalytics_table.analytics_id AS analytics_id, " +
            "dacastanalytics_table.visitors AS visitors, dacastanalytics_table.hits AS hits, dacastanalytics_table.channel AS channel " +
            "FROM dacastlog_table, dacastanalytics_table " )
    Flowable<List<DacastChannelAnalyticsEntity>> getDacastLogAnalyticsItems();
}