package com.example.spokenwapp.data.repository;
import android.app.Application;
import com.example.spokenwapp.data.database.AppLocalDatabase;
import com.example.spokenwapp.data.model.LocalAudioDao;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.data.model.LocalVideoDao;
import com.example.spokenwapp.data.model.LocalVideoEntity;
import java.util.List;

import io.reactivex.Flowable;


public class LocalVideoRepository {

    private LocalVideoDao localVideoDao;
    private LocalAudioDao localAudioDao;
    public Flowable<List<LocalVideoEntity>> allLocalVideos;
    public Flowable<List<LocalAudioEntity>> allLocalAudio;

    public LocalVideoRepository(Application application) {
        AppLocalDatabase localVideoDb = AppLocalDatabase.getInstance(application);
        localVideoDao = localVideoDb.localVideoDao();
        allLocalVideos = localVideoDao.getLocalVideoCatalog();
        localAudioDao = localVideoDb.localAudioDao();
        allLocalAudio = localAudioDao.getLocalAudioCatalog();
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

    public long insertAllLocalAudios(LocalAudioEntity localAudioEntity) {
        return localAudioDao.insertLocalAudio(localAudioEntity);
    }

    public void deleteAllAudio(){
        localAudioDao.deleteAudioTable();
    }

}
