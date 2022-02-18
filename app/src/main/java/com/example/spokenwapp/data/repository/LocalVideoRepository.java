package com.example.spokenwapp.data.repository;
import android.app.Application;
import android.util.Log;

import androidx.paging.DataSource;

import com.example.spokenwapp.data.database.AppLocalDatabase;
import com.example.spokenwapp.data.model.DacastAnalyticsDao;
import com.example.spokenwapp.data.model.DacastAnalyticsEntity;
import com.example.spokenwapp.data.model.DacastAudioSermonDao;
import com.example.spokenwapp.data.model.DacastAudioSermonEntity;
import com.example.spokenwapp.data.model.DacastChannelAnalyticDao;
import com.example.spokenwapp.data.model.DacastChannelAnalyticsEntity;
import com.example.spokenwapp.data.model.DacastSpecialsSermonDao;
import com.example.spokenwapp.data.model.DacastSpecialSermonEntity;
import com.example.spokenwapp.data.model.DacastLogDao;
import com.example.spokenwapp.data.model.DacastLogEntity;
import com.example.spokenwapp.data.model.LocalAudioDao;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.data.model.LocalVideoDao;
import com.example.spokenwapp.data.model.LocalVideoEntity;
import com.example.spokenwapp.data.model.VodDacastDao;
import com.example.spokenwapp.data.model.VodDacastLogEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;


public class LocalVideoRepository {

    private LocalVideoDao localVideoDao;
    private LocalAudioDao localAudioDao;
    private DacastLogDao dacastLogDao;
    private VodDacastDao vodDacastDao;
    private DacastAnalyticsDao dacastAnalyticsDao;
    private DacastSpecialsSermonDao dacastSpecialsSermonDao;
    private DacastAudioSermonDao dacastAudioSermonDao;
    private DacastChannelAnalyticDao dacastChannelAnalyticDao;
    public Flowable<List<LocalVideoEntity>> allLocalVideos;
    public Flowable<List<LocalAudioEntity>> allLocalAudio;
    public Flowable<List<DacastLogEntity>> allDacastLogs;
    public Flowable<List<VodDacastLogEntity>> allDacastVodLogs;
    public Flowable<List<DacastSpecialSermonEntity>> allDacastAudioSermons;
    public Flowable<List<DacastAudioSermonEntity>> allDacastVideoSermons;
    public Flowable<List<DacastAnalyticsEntity>> allDacastAnalytics;
    public Flowable<List<DacastChannelAnalyticsEntity>> allDacastChannelAnalytics;
    public Single<LocalAudioEntity> allLocalAudioEntity;

    @Inject
    public LocalVideoRepository(Application application) {
        AppLocalDatabase localVideoDb = AppLocalDatabase.getInstance(application);
        localVideoDao = localVideoDb.localVideoDao();
        allLocalVideos = localVideoDao.getLocalVideoCatalog();
        localAudioDao = localVideoDb.localAudioDao();
        allLocalAudio = localAudioDao.getLocalAudioCatalog();
        dacastLogDao = localVideoDb.dacastLogDao();
        allDacastLogs = dacastLogDao.getDacastCatalog();
        vodDacastDao = localVideoDb.vodDacastDao();
        allDacastVodLogs = vodDacastDao.getDacastVodCatalog();
        dacastSpecialsSermonDao = localVideoDb.dacastAudioSermonDao();
        allDacastAudioSermons = dacastSpecialsSermonDao.getDacastAudioSermonsCatalog();
        dacastAudioSermonDao = localVideoDb.dacastVideoSermonDao();
        allDacastVideoSermons = dacastAudioSermonDao.getDacastVideoSermonsCatalog();
        dacastAnalyticsDao = localVideoDb.dacastAnalyticsDao();
        allDacastAnalytics = dacastAnalyticsDao.getDacastAnalyticsCatalog();
        dacastChannelAnalyticDao = localVideoDb.dacastChannelAnalyticDao();
        allDacastChannelAnalytics = dacastChannelAnalyticDao.getDacastLogAnalyticsItems();
        allLocalAudioEntity = localAudioDao.getLocalAudioList();

    }

    // Room executes all queries on a separate thread.
    // Observed Observables will notify the observer when the data has changed.
    public Flowable<List<LocalVideoEntity>> getAllLocalVideoCatalog() {
        return allLocalVideos;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public long insertAllLocalVideos(LocalVideoEntity localVideoEntity) {
        return localVideoDao.insertLocalVideo(localVideoEntity);
    }

    // Delete all data in local video table
    public void deleteAllVideos(){
        localVideoDao.deleteVideosTable();
    }

    public Flowable<List<LocalAudioEntity>> getAllLocalAudioCatalog() {
        return allLocalAudio;
    }

    public Single<LocalAudioEntity> getAllLocalAudioListCat() {
        return allLocalAudioEntity;
    }

    public long insertAllLocalAudios(LocalAudioEntity localAudioEntity) {
        return localAudioDao.insertLocalAudio(localAudioEntity);
    }

    public void deleteAllAudio(){
        localAudioDao.deleteAudioTable();
    }

    // Insert Dacast Logs
    public long insertAllDacastLogsList(DacastLogEntity dacastLogEntity) {
        return dacastLogDao.insertDacastLog(dacastLogEntity);
    }

    public Flowable<List<DacastLogEntity>> getAllDacastServerLogs() {
        return allDacastLogs;
    }

    public int updateDacastLogRepo(boolean online){
       return dacastLogDao.updateColDacastLog(online);
    }

    // Dacast VOD Logs
    public long insertAllDacastVodLogsList(VodDacastLogEntity vodDacastLogEntity) {
        return vodDacastDao.insertDacastVodLog(vodDacastLogEntity);
    }

    public Flowable<List<VodDacastLogEntity>> getAllDacastVodServerLogs() {
        return allDacastVodLogs;
    }

    // Dacast videos
    public long insertAllDacastVideoSermonList(DacastAudioSermonEntity dacastAudioSermonEntity) {
        return dacastAudioSermonDao.insertDacastVideoSermon(dacastAudioSermonEntity);
    }

    public Flowable<List<DacastAudioSermonEntity>> getAllDacastVideoSermonList() {
        return allDacastVideoSermons;
    }

    // Dacast audios
    public long insertAllDacastAudioSermonList(DacastSpecialSermonEntity dacastSpecialSermonEntity) {
        return dacastSpecialsSermonDao.insertDacastAudioSermon(dacastSpecialSermonEntity);
    }

    public Flowable<List<DacastSpecialSermonEntity>> getAllDacastAudioSermonsList() {
        return allDacastAudioSermons;
    }

    // Dacast analytics
    public long insertAllDacastAnalyticsList(DacastAnalyticsEntity dacastAnalyticsEntity) {
        return dacastAnalyticsDao.insertDacastAnalytics(dacastAnalyticsEntity);
    }

    public Flowable<List<DacastAnalyticsEntity>> getAllDacastAnalyticsList() {
        return allDacastAnalytics;
    }

    public Flowable<List<DacastChannelAnalyticsEntity>> getAllDacastChannelAnalyticsList() {
        return allDacastChannelAnalytics;
    }




}
