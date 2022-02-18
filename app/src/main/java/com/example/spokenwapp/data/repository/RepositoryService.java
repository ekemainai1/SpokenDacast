package com.example.spokenwapp.data.repository;


import android.app.Application;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.spokenwapp.base.APIInterface;
import com.example.spokenwapp.dacast.Config;
import com.example.spokenwapp.dacast.ConfigDeserializer;
import com.example.spokenwapp.dacast.DacastDeserializer;
import com.example.spokenwapp.dacast.DacastObject;
import com.example.spokenwapp.dacast.Data;
import com.example.spokenwapp.dacast.DataDeserializer;
import com.example.spokenwapp.dacast.Paging;
import com.example.spokenwapp.dacast.PagingDeserializer;
import com.example.spokenwapp.dacast.Pictures;
import com.example.spokenwapp.dacast.PicturesDeserializer;
import com.example.spokenwapp.dacast.ShareCode;
import com.example.spokenwapp.dacast.ShareCodeDeserializer;
import com.example.spokenwapp.dacastanalytics.DacastAnalyticsDeserializer;
import com.example.spokenwapp.dacastanalytics.DacastChannelAnalytics;
import com.example.spokenwapp.dacastvod.DacastVODObject;
import com.example.spokenwapp.dacastvod.DacastVODObjectDeserializer;
import com.example.spokenwapp.dacastvod.VodData;
import com.example.spokenwapp.dacastvod.VodDataDeserializer;
import com.example.spokenwapp.dacastvod.VodPaging;
import com.example.spokenwapp.dacastvod.VodPagingDeserializer;
import com.example.spokenwapp.dacastvod.VodPictures;
import com.example.spokenwapp.dacastvod.VodPicturesDeserializer;
import com.example.spokenwapp.dacastvod.VodShareCode;
import com.example.spokenwapp.dacastvod.VodShareCodeDeserializer;
import com.example.spokenwapp.data.model.DacastAnalyticsEntity;
import com.example.spokenwapp.data.model.DacastAudioSermonEntity;
import com.example.spokenwapp.data.model.DacastSpecialSermonEntity;
import com.example.spokenwapp.data.model.DacastLogEntity;
import com.example.spokenwapp.data.model.VodDacastLogEntity;
import com.example.spokenwapp.utilities.SpokenSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


@Singleton
public class RepositoryService {

    @Inject
    Application application;
    private DacastObject dacastObject;
    private DacastVODObject dacastVODObject;
    private List<DacastChannelAnalytics> dacastChannelAnalytics;
    private Call<ResponseBody> call;
    private Call<ResponseBody> callVod;
    private Call<List<DacastChannelAnalytics>> callAnalytics;
    private APIInterface apiInterface;
    List<DacastVODObject> dacastVODObjectList;
    List<DacastChannelAnalytics> dacastChannelAnalyticsCall = new ArrayList<>();

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Config.class,  new ConfigDeserializer<Config>())
            .registerTypeAdapter(Data.class,  new DataDeserializer())
            .registerTypeAdapter(Paging.class,  new PagingDeserializer())
            .registerTypeAdapter(Pictures.class,  new PicturesDeserializer())
            .registerTypeAdapter(ShareCode.class,  new ShareCodeDeserializer())
            .registerTypeAdapter(DacastObject.class,  new DacastDeserializer<DacastObject>())
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setLenient()
            .create();

    Gson gsonVod = new GsonBuilder()
            .registerTypeAdapter(VodData.class,  new VodDataDeserializer<VodData>())
            .registerTypeAdapter(VodPaging.class,  new VodPagingDeserializer<VodPaging>())
            .registerTypeAdapter(VodPictures.class,  new VodPicturesDeserializer<VodPictures>())
            .registerTypeAdapter(VodShareCode.class,  new VodShareCodeDeserializer<VodShareCode>())
            .registerTypeAdapter(DacastVODObject.class,  new DacastVODObjectDeserializer<DacastVODObject>())
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setLenient()
            .create();

    Gson gsonAnalytics = new GsonBuilder()
            .registerTypeAdapter(DacastChannelAnalytics.class,
                    new DacastAnalyticsDeserializer<DacastChannelAnalytics>())
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setLenient()
            .create();

    DacastLogEntity dacastLogEntity;
    VodDacastLogEntity vodDacastLogEntity;
    DacastAudioSermonEntity dacastAudioSermonEntity;
    DacastSpecialSermonEntity dacastSpecialSermonEntity;
    DacastAnalyticsEntity dacastAnalyticsEntity;
    SpokenSharedPreferences spokenSharedPreferences;
    Date currentTime = Calendar.getInstance().getTime();

    @Inject
    public RepositoryService(Retrofit retrofit) {
        apiInterface = retrofit.create(APIInterface.class);
        call = apiInterface.getDacastLogData();
        callVod = apiInterface.getDacastVoDLogData();
        callAnalytics = apiInterface.getDacastChannelAnalytics();
    }

    // Call to VOD Dacast
    public DacastVODObject invokeDacastVoDList(LocalVideoRepository localVideoRepository,
                                               Context context) {
        gsonVod = new Gson();
        callVod.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> callVod,
                                   @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    dacastVODObject = new DacastVODObject();
                    dacastVODObjectList = new ArrayList<>();
                    assert response.body() != null;
                    try {
                            dacastVODObject = gsonVod.fromJson(response.body().string(),
                                    DacastVODObject.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    for(int x=0; x<dacastVODObject.getData().size(); x++) {

                        if(dacastVODObject.getData().get(x).getDescription().equals("Video Sermons")) {
                            // Form content Ids for Dacast VOD items
                            String contentVodString = dacastVODObject.getData().get(x).getShareCode().getFacebook();
                            String[] paths = contentVodString.split("/");
                            String contentVodId = null;
                            for (int i = 4; i < paths.length; i++) {
                                Log.e("DACASTVODID", paths[i]);
                                contentVodId = paths[i - 2] + "_" + paths[i - 1] + "_" + paths[i];
                            }
                            assert contentVodId != null;
                            Log.e("DACASTVODID", contentVodId);

                            // Form dates and time for notification
                            String dateVodString = dacastVODObject.getData().get(x).getCreationDate();
                            String[] datePaths = dateVodString.split(" ");
                            String dateVodId = null;
                            String timeVodId = null;
                            for (int i = 1; i < datePaths.length; i++) {
                                Log.e("DACASTVOD", datePaths[i]);
                                dateVodId = datePaths[i - 1];
                                timeVodId = datePaths[i];
                            }

                            assert dateVodId != null;
                            Log.e("DACASTVOD", dateVodId);
                            Log.e("DACASTIME", timeVodId);

                            // Create data object for insertion into database
                            vodDacastLogEntity = new VodDacastLogEntity(
                                    contentVodId,
                                    dacastVODObject.getTotalCount(),
                                    dacastVODObject.getData().get(x).getAbitrate(),
                                    dacastVODObject.getData().get(x).getAcodec(),
                                    dacastVODObject.getData().get(x).getAds(),
                                    dacastVODObject.getData().get(x).getAssociatedPackages(),
                                    dacastVODObject.getData().get(x).getCategoryId(),
                                    dacastVODObject.getData().get(x).getContainer(),
                                    dacastVODObject.getData().get(x).getCountriesId(),
                                    dacastVODObject.getData().get(x).getCreationDate(),
                                    dacastVODObject.getData().get(x).getCustomData(),
                                    dacastVODObject.getData().get(x).getNoframeSecurity(),
                                    dacastVODObject.getData().get(x).getDescription(),
                                    dacastVODObject.getData().get(x).getDiskUsage(),
                                    dacastVODObject.getData().get(x).getDuration(),
                                    dacastVODObject.getData().get(x).getAutoplay(),
                                    dacastVODObject.getData().get(x).getEnableCoupon(),
                                    dacastVODObject.getData().get(x).getOnline(),
                                    dacastVODObject.getData().get(x).getEnablePayperview(),
                                    dacastVODObject.getData().get(x).getPublishOnDacast(),
                                    dacastVODObject.getData().get(x).getEnableSubscription(),
                                    dacastVODObject.getData().get(x).getExternalVideoPage(),
                                    dacastVODObject.getData().get(x).getFilename(),
                                    dacastVODObject.getData().get(x).getFilesize(),
                                    dacastVODObject.getData().get(x).getGoogleAnalytics(),
                                    dacastVODObject.getData().get(x).getGroupId(),
                                    dacastVODObject.getData().get(x).getHds(),
                                    dacastVODObject.getData().get(x).getHls(),
                                    dacastVODObject.getData().get(x).getIsSecured(),
                                    dacastVODObject.getData().get(x).getOriginalId(),
                                    dacastVODObject.getData().get(x).getPassword(),
                                    dacastVODObject.getData().get(x).getPlayerHeight(),
                                    dacastVODObject.getData().get(x).getPlayerWidth(),
                                    dacastVODObject.getData().get(x).getPlayerSizeId(),
                                    dacastVODObject.getData().get(x).getReferersId(),
                                    dacastVODObject.getData().get(x).getSaveDate(),
                                    dacastVODObject.getData().get(x).getSplashscreenId(),
                                    dacastVODObject.getData().get(x).getStreamable(),
                                    dacastVODObject.getData().get(x).getSubtitles(),
                                    dacastVODObject.getData().get(x).getTemplateId(),
                                    dacastVODObject.getData().get(x).getThemeId(),
                                    dacastVODObject.getData().get(x).getThumbnailId(),
                                    dacastVODObject.getData().get(x).getTitle(),
                                    dacastVODObject.getData().get(x).getVbitrate(),
                                    dacastVODObject.getData().get(x).getVcodec(),
                                    dacastVODObject.getData().get(x).getVideoHeight(),
                                    dacastVODObject.getData().get(x).getVideoWidth(),
                                    dacastVODObject.getData().get(x).getPictures().getThumbnail(),
                                    dacastVODObject.getData().get(x).getPictures().getSplashscreen(),
                                    dacastVODObject.getData().get(x).getShareCode().getFacebook(),
                                    dacastVODObject.getData().get(x).getShareCode().getTwitter()
                            );
                            // Log down Dacast VOD items id
                            Log.e("DACASTVODLOGLIST", String.valueOf(dacastVODObject.getData().get(x).getId()));


                            // Save VOD data for Notification
                            spokenSharedPreferences = new SpokenSharedPreferences(context);
                            spokenSharedPreferences.saveDacastLogsVodDetails(dateVodId, timeVodId,
                                    dacastVODObject.getData().get(x).getTitle());
                            spokenSharedPreferences.saveDacastVODOnlineState(dacastVODObject.getData().size());

                            Single.fromCallable(() -> localVideoRepository
                                    .insertAllDacastVodLogsList(vodDacastLogEntity))
                                    .subscribeOn(Schedulers.io())
                                    .subscribe();

                        }else if(dacastVODObject.getData().get(x).getDescription().equals("Audio Sermon")) {

                            // Form content Ids for Dacast VOD items
                            String contentVodString = dacastVODObject.getData().get(x).getShareCode().getFacebook();
                            String[] paths = contentVodString.split("/");
                            String contentVodId = null;
                            for (int i = 4; i < paths.length; i++) {
                                Log.e("DACASTVODID", paths[i]);
                                contentVodId = paths[i - 2] + "_" + paths[i - 1] + "_" + paths[i];
                            }
                            assert contentVodId != null;
                            Log.e("DACASTVODID", contentVodId);

                            // Form dates and time for notification
                            String dateVodString = dacastVODObject.getData().get(x).getCreationDate();
                            String[] datePaths = dateVodString.split(" ");
                            String dateVodId = null;
                            String timeVodId = null;
                            for (int i = 1; i < datePaths.length; i++) {
                                Log.e("DACASTVOD", datePaths[i]);
                                dateVodId = datePaths[i - 1];
                                timeVodId = datePaths[i];
                            }

                            assert dateVodId != null;
                            Log.e("DACASTVOD", dateVodId);
                            Log.e("DACASTIME", timeVodId);

                            // Create data object for insertion into database
                            dacastAudioSermonEntity = new DacastAudioSermonEntity(
                                    contentVodId,
                                    dacastVODObject.getTotalCount(),
                                    dacastVODObject.getData().get(x).getAbitrate(),
                                    dacastVODObject.getData().get(x).getAcodec(),
                                    dacastVODObject.getData().get(x).getAds(),
                                    dacastVODObject.getData().get(x).getAssociatedPackages(),
                                    dacastVODObject.getData().get(x).getCategoryId(),
                                    dacastVODObject.getData().get(x).getContainer(),
                                    dacastVODObject.getData().get(x).getCountriesId(),
                                    dacastVODObject.getData().get(x).getCreationDate(),
                                    dacastVODObject.getData().get(x).getCustomData(),
                                    dacastVODObject.getData().get(x).getNoframeSecurity(),
                                    dacastVODObject.getData().get(x).getDescription(),
                                    dacastVODObject.getData().get(x).getDiskUsage(),
                                    dacastVODObject.getData().get(x).getDuration(),
                                    dacastVODObject.getData().get(x).getAutoplay(),
                                    dacastVODObject.getData().get(x).getEnableCoupon(),
                                    dacastVODObject.getData().get(x).getOnline(),
                                    dacastVODObject.getData().get(x).getEnablePayperview(),
                                    dacastVODObject.getData().get(x).getPublishOnDacast(),
                                    dacastVODObject.getData().get(x).getEnableSubscription(),
                                    dacastVODObject.getData().get(x).getExternalVideoPage(),
                                    dacastVODObject.getData().get(x).getFilename(),
                                    dacastVODObject.getData().get(x).getFilesize(),
                                    dacastVODObject.getData().get(x).getGoogleAnalytics(),
                                    dacastVODObject.getData().get(x).getGroupId(),
                                    dacastVODObject.getData().get(x).getHds(),
                                    dacastVODObject.getData().get(x).getHls(),
                                    dacastVODObject.getData().get(x).getIsSecured(),
                                    dacastVODObject.getData().get(x).getOriginalId(),
                                    dacastVODObject.getData().get(x).getPassword(),
                                    dacastVODObject.getData().get(x).getPlayerHeight(),
                                    dacastVODObject.getData().get(x).getPlayerWidth(),
                                    dacastVODObject.getData().get(x).getPlayerSizeId(),
                                    dacastVODObject.getData().get(x).getReferersId(),
                                    dacastVODObject.getData().get(x).getSaveDate(),
                                    dacastVODObject.getData().get(x).getSplashscreenId(),
                                    dacastVODObject.getData().get(x).getStreamable(),
                                    dacastVODObject.getData().get(x).getSubtitles(),
                                    dacastVODObject.getData().get(x).getTemplateId(),
                                    dacastVODObject.getData().get(x).getThemeId(),
                                    dacastVODObject.getData().get(x).getThumbnailId(),
                                    dacastVODObject.getData().get(x).getTitle(),
                                    dacastVODObject.getData().get(x).getVbitrate(),
                                    dacastVODObject.getData().get(x).getVcodec(),
                                    dacastVODObject.getData().get(x).getVideoHeight(),
                                    dacastVODObject.getData().get(x).getVideoWidth(),
                                    dacastVODObject.getData().get(x).getPictures().getThumbnail(),
                                    dacastVODObject.getData().get(x).getPictures().getSplashscreen(),
                                    dacastVODObject.getData().get(x).getShareCode().getFacebook(),
                                    dacastVODObject.getData().get(x).getShareCode().getTwitter()
                            );
                            // Log down Dacast VOD items id
                            Log.e("DACASTVODLOGLIST", String.valueOf(dacastVODObject.getData().get(x).getId()));


                            // Save VOD data for Notification
                            spokenSharedPreferences = new SpokenSharedPreferences(context);
                            spokenSharedPreferences.saveDacastLogsVodDetails(dateVodId, timeVodId,
                                    dacastVODObject.getData().get(x).getTitle());
                            spokenSharedPreferences.saveDacastAudioOnlineState(dacastVODObject.getData().size());

                            Single.fromCallable(() -> localVideoRepository
                                   .insertAllDacastVideoSermonList(dacastAudioSermonEntity))
                                    .subscribeOn(Schedulers.io())
                                    .subscribe();
                        }else if(dacastVODObject.getData().get(x).getDescription().equals("Specials")) {

                            // Form content Ids for Dacast VOD items
                            String contentVodString = dacastVODObject.getData().get(x).getShareCode().getFacebook();
                            String[] paths = contentVodString.split("/");
                            String contentVodId = null;
                            for (int i = 4; i < paths.length; i++) {
                                Log.e("DACASTVODID", paths[i]);
                                contentVodId = paths[i - 2] + "_" + paths[i - 1] + "_" + paths[i];
                            }
                            assert contentVodId != null;
                            Log.e("DACASTVODID", contentVodId);

                            // Form dates and time for notification
                            String dateVodString = dacastVODObject.getData().get(x).getCreationDate();
                            String[] datePaths = dateVodString.split(" ");
                            String dateVodId = null;
                            String timeVodId = null;
                            for (int i = 1; i < datePaths.length; i++) {
                                Log.e("DACASTVOD", datePaths[i]);
                                dateVodId = datePaths[i - 1];
                                timeVodId = datePaths[i];
                            }

                            assert dateVodId != null;
                            Log.e("DACASTVOD", dateVodId);
                            Log.e("DACASTIME", timeVodId);

                            // Create data object for insertion into database
                            dacastSpecialSermonEntity = new DacastSpecialSermonEntity(
                                    contentVodId,
                                    dacastVODObject.getTotalCount(),
                                    dacastVODObject.getData().get(x).getAbitrate(),
                                    dacastVODObject.getData().get(x).getAcodec(),
                                    dacastVODObject.getData().get(x).getAds(),
                                    dacastVODObject.getData().get(x).getAssociatedPackages(),
                                    dacastVODObject.getData().get(x).getCategoryId(),
                                    dacastVODObject.getData().get(x).getContainer(),
                                    dacastVODObject.getData().get(x).getCountriesId(),
                                    dacastVODObject.getData().get(x).getCreationDate(),
                                    dacastVODObject.getData().get(x).getCustomData(),
                                    dacastVODObject.getData().get(x).getNoframeSecurity(),
                                    dacastVODObject.getData().get(x).getDescription(),
                                    dacastVODObject.getData().get(x).getDiskUsage(),
                                    dacastVODObject.getData().get(x).getDuration(),
                                    dacastVODObject.getData().get(x).getAutoplay(),
                                    dacastVODObject.getData().get(x).getEnableCoupon(),
                                    dacastVODObject.getData().get(x).getOnline(),
                                    dacastVODObject.getData().get(x).getEnablePayperview(),
                                    dacastVODObject.getData().get(x).getPublishOnDacast(),
                                    dacastVODObject.getData().get(x).getEnableSubscription(),
                                    dacastVODObject.getData().get(x).getExternalVideoPage(),
                                    dacastVODObject.getData().get(x).getFilename(),
                                    dacastVODObject.getData().get(x).getFilesize(),
                                    dacastVODObject.getData().get(x).getGoogleAnalytics(),
                                    dacastVODObject.getData().get(x).getGroupId(),
                                    dacastVODObject.getData().get(x).getHds(),
                                    dacastVODObject.getData().get(x).getHls(),
                                    dacastVODObject.getData().get(x).getIsSecured(),
                                    dacastVODObject.getData().get(x).getOriginalId(),
                                    dacastVODObject.getData().get(x).getPassword(),
                                    dacastVODObject.getData().get(x).getPlayerHeight(),
                                    dacastVODObject.getData().get(x).getPlayerWidth(),
                                    dacastVODObject.getData().get(x).getPlayerSizeId(),
                                    dacastVODObject.getData().get(x).getReferersId(),
                                    dacastVODObject.getData().get(x).getSaveDate(),
                                    dacastVODObject.getData().get(x).getSplashscreenId(),
                                    dacastVODObject.getData().get(x).getStreamable(),
                                    dacastVODObject.getData().get(x).getSubtitles(),
                                    dacastVODObject.getData().get(x).getTemplateId(),
                                    dacastVODObject.getData().get(x).getThemeId(),
                                    dacastVODObject.getData().get(x).getThumbnailId(),
                                    dacastVODObject.getData().get(x).getTitle(),
                                    dacastVODObject.getData().get(x).getVbitrate(),
                                    dacastVODObject.getData().get(x).getVcodec(),
                                    dacastVODObject.getData().get(x).getVideoHeight(),
                                    dacastVODObject.getData().get(x).getVideoWidth(),
                                    dacastVODObject.getData().get(x).getPictures().getThumbnail(),
                                    dacastVODObject.getData().get(x).getPictures().getSplashscreen(),
                                    dacastVODObject.getData().get(x).getShareCode().getFacebook(),
                                    dacastVODObject.getData().get(x).getShareCode().getTwitter()
                            );
                            // Log down Dacast VOD items id
                            Log.e("DACASTVODLOGLIST", String.valueOf(dacastVODObject.getData().get(x).getId()));


                            // Save VOD data for Notification
                            spokenSharedPreferences = new SpokenSharedPreferences(context);
                            spokenSharedPreferences.saveDacastLogsVodDetails(dateVodId, timeVodId,
                                    dacastVODObject.getData().get(x).getTitle());
                            spokenSharedPreferences.saveDacastSpecOnlineState(dacastVODObject.getData().size());

                            Single.fromCallable(() -> localVideoRepository
                                    .insertAllDacastAudioSermonList(dacastSpecialSermonEntity))
                                    .subscribeOn(Schedulers.io())
                                    .subscribe();
                        }

                    }
                        dacastVODObjectList.add(dacastVODObject);
                }
                Log.e("DACASTVODCOUNT", dacastVODObject.getTotalCount());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> callVod, @NonNull Throwable throwable) {
                Log.e("DacastVod Log Error", throwable.toString());
            }
        });
        return dacastVODObject;
    }

    // Dacast channels list
    public DacastObject invokeDacastChannelsList(LocalVideoRepository localVideoRepository,
                                                 Context context) {
        gson = new Gson();
        call.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    dacastObject = new DacastObject();

                    try {
                        assert response.body() != null;
                        dacastObject = gson.fromJson(response.body().string(), DacastObject.class);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    for(int y=0; y<dacastObject.getData().size(); y++) {
                        // Dacast live channels details for Notification
                        spokenSharedPreferences = new SpokenSharedPreferences(context);
                        spokenSharedPreferences.saveDacastLogsDetails(dacastObject.getData()
                                        .get(y).getCreation_date(),
                                dacastObject.getData().get(y).getCountdown_date(),
                                dacastObject.getData().get(y).getTitle());
                        spokenSharedPreferences.saveChurchOnline(dacastObject.getData()
                                               .get(y).getOnline());

                        // Form content Ids for live channels items
                        String contentString = dacastObject.getData().get(y).getShare_code().getFacebook();
                        String[] paths = contentString.split("/");
                        String contentId = null;
                        for (int i = 4; i < paths.length; i++) {
                            Log.e("DACASTID", paths[i]);
                            contentId = paths[i - 2] + "_" + paths[i - 1] + "_" + paths[i];
                        }
                        assert contentId != null;
                        Log.e("DACASTCONID", contentId);
                        spokenSharedPreferences.saveSysIPAddress(contentId);

                        dacastLogEntity = new DacastLogEntity(
                                dacastObject.getData().get(y).getId(),
                                contentId,
                                dacastObject.getData().get(y).getDescription(),
                                dacastObject.getData().get(y).getCountdown_date(),
                                dacastObject.getData().get(y).getCreation_date(),
                                dacastObject.getData().get(y).getHds(),
                                dacastObject.getData().get(y).getHls(),
                                dacastObject.getData().get(y).getPictures().getThumbnail(),
                                dacastObject.getData().get(y).getPictures().getSplashscreen(),
                                dacastObject.getData().get(y).getShare_code().getFacebook(),
                                dacastObject.getData().get(y).getShare_code().getTwitter(),
                                dacastObject.getData().get(y).getShare_code().getGplus(),
                                dacastObject.getData().get(y).getOnline(),
                                dacastObject.getData().get(y).getPublish_on_dacast(),
                                dacastObject.getData().get(y).getAds(),
                                dacastObject.getData().get(y).getAssociated_packages(),
                                dacastObject.getData().get(y).getCategory_id(),
                                dacastObject.getData().get(y).getCompany_url(),
                                dacastObject.getData().get(y).getCountdown_timezone(),
                                dacastObject.getData().get(y).getCounter_live_limit(),
                                dacastObject.getData().get(y).getCountries_id(),
                                dacastObject.getData().get(y).getCustom_data(),
                                dacastObject.getData().get(y).getNoframe_security(),
                                dacastObject.getData().get(y).getAutoplay(),
                                dacastObject.getData().get(y).getEnable_coupon(),
                                dacastObject.getData().get(y).getEnable_payperview(),
                                dacastObject.getData().get(y).getEnable_subscription(),
                                dacastObject.getData().get(y).getExternal_video_page(),
                                dacastObject.getData().get(y).getGoogle_analytics(),
                                dacastObject.getData().get(y).getIs_secure(),
                                dacastObject.getData().get(y).getPassword(),
                                dacastObject.getData().get(y).getPlayer_height(),
                                dacastObject.getData().get(y).getPlayer_width(),
                                dacastObject.getData().get(y).getPlayer_size_id(),
                                dacastObject.getData().get(y).getReferers_id(),
                                dacastObject.getData().get(y).getRtmp(),
                                dacastObject.getData().get(y).getSave_date(),
                                dacastObject.getData().get(y).getSchedule(),
                                dacastObject.getData().get(y).getSplashscreen_id(),
                                dacastObject.getData().get(y).getStream_tech(),
                                dacastObject.getData().get(y).getTheme_id(),
                                dacastObject.getData().get(y).getThumbnail_id(),
                                dacastObject.getData().get(y).getTitle(),
                                dacastObject.getData().get(y).getWeb_dvr()

                        );
                        Log.e("DACASTLOGLIST", String.valueOf(dacastObject.getData().get(y).getId()));
                        boolean online = dacastObject.getData().get(y).getOnline();

                        // Insert the data object into Room Database
                        Single.fromCallable(() -> localVideoRepository
                                .insertAllDacastLogsList(dacastLogEntity))
                                .subscribeOn(Schedulers.io())
                                .subscribe();

                        // Update Online column
                        Single.fromCallable(() -> localVideoRepository
                                .updateDacastLogRepo(online))
                                .subscribeOn(Schedulers.io())
                                .subscribe();

                    }

                }
                Log.e("DACAST", dacastObject.getTotalCount());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                Log.e("Dacast Log Error", throwable.toString());
            }
        });
        return dacastObject;
    }

    // Dacast channels analytics
    public List<DacastChannelAnalytics> invokeDacastAnalyticsList (LocalVideoRepository localVideoRepository) {
        gsonAnalytics = new Gson();
        callAnalytics.clone().enqueue(new Callback<List<DacastChannelAnalytics>>() {
            @Override
            public void onResponse(@NonNull Call<List<DacastChannelAnalytics>> callAnalytics,
                                   @NonNull Response<List<DacastChannelAnalytics>> response) {
                if (response.isSuccessful()) {
                    dacastChannelAnalytics = new ArrayList<>();
                        assert response.body() != null;
                        dacastChannelAnalytics = response.body();
                        Log.e("ANALYTICS VIEWS", String.valueOf(response.body()));
                        for(int z=0; z<dacastChannelAnalytics.size(); z++) {
                            dacastAnalyticsEntity = new DacastAnalyticsEntity(
                                    dacastChannelAnalytics.get(z).getVisitors(),
                                    dacastChannelAnalytics.get(z).getHits(),
                                    dacastChannelAnalytics.get(z).getChannel()
                            );

                            // Insert the data object into Room Database
                            Single.fromCallable(() -> localVideoRepository
                                    .insertAllDacastAnalyticsList(dacastAnalyticsEntity))
                                    .subscribeOn(Schedulers.io())
                                    .subscribe();
                            Log.e("DACAST ANALYTICS VIEWS",
                                    String.valueOf(dacastChannelAnalytics.get(z).getVisitors()));
                        }
                    Log.e("DACAST ANALYTICS SIZE",
                            String.valueOf(dacastChannelAnalytics.size()));
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<DacastChannelAnalytics>> callAnalytics,
                                  @NonNull Throwable throwable) {
                Log.e("Dacast Analytics Error", throwable.toString());
            }
        });
        return dacastChannelAnalytics;
    }

}





