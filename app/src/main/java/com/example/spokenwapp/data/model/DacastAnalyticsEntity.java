package com.example.spokenwapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "dacastanalytics_table", indices = {@Index(value = {"channel"}, unique = true)} )
public class DacastAnalyticsEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "analytics_id")
    public int analytics_id;

    @ColumnInfo(name = "visitors")
    public Integer visitors;

    @ColumnInfo(name = "hits")
    public Integer hits;

    @ColumnInfo(name = "channel")
    public String channel;

    @Ignore
    public DacastAnalyticsEntity(int analytics_id, Integer visitors, Integer hits, String channel) {
        this.analytics_id = analytics_id;
        this.visitors = visitors;
        this.hits = hits;
        this.channel = channel;
    }

    public DacastAnalyticsEntity(Integer visitors, Integer hits, String channel) {
        this.visitors = visitors;
        this.hits = hits;
        this.channel = channel;
    }

    public int getAnalytics_id() {
        return analytics_id;
    }

    public void setAnalytics_id(int analytics_id) {
        this.analytics_id = analytics_id;
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
