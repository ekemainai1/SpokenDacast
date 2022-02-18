package com.example.spokenwapp.base;

import com.example.spokenwapp.dacast.Json;
import com.example.spokenwapp.dacastanalytics.DacastChannelAnalytics;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("/v2/channel?apikey=171612_dc6e1b769c4e1168451a&order_by=title&sort=ASC")
    @Json
    Call<ResponseBody> getDacastLogData();

    @GET("/v2/vod?apikey=171612_dc6e1b769c4e1168451a&order_by=title&sort=ASC")
    @Json
    Call<ResponseBody> getDacastVoDLogData();

    @GET("/v2/analytics/channel/visitorspercontent?apikey=171612_dc6e1b769c4e1168451a&startdate=2020-06-01&enddate=2020-07-01")
    @Json
    Call<List<DacastChannelAnalytics>> getDacastChannelAnalytics();
}

