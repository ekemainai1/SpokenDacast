package com.example.spokenwapp.data.model;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface LocalAudioDao {

    @Query("SELECT COUNT(*) FROM localAudio_table")
    int getLocalAudioCount();

    @Query("SELECT * FROM localAudio_table")
    Flowable<List<LocalAudioEntity>> getLocalAudioCatalog();

    // The Integer type parameter tells Room to use a PositionalDataSource
    // object, with position-based loading under the hood.
    @Query("SELECT * FROM localAudio_table")
    Single<LocalAudioEntity> getLocalAudioList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLocalAudio(LocalAudioEntity localAudioEntity);

    @Update
    void updateLocalAudio(LocalAudioEntity... localAudioEntities);

    @Query("DELETE FROM localAudio_table")
    void deleteAudioTable();

}
