package com.example.spokenwapp.data.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface LocalAudioDao {

    @Query("SELECT * FROM localAudio_table")
    Flowable<List<LocalAudioEntity>> getLocalAudioCatalog();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLocalAudio(LocalAudioEntity localAudioEntity);

    @Update
    void updateLocalAudio(LocalAudioEntity... localAudioEntities);


    @Query("DELETE FROM localAudio_table")
    void deleteAudioTable();

}
