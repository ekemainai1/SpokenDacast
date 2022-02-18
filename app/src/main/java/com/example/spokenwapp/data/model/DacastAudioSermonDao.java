package com.example.spokenwapp.data.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface DacastAudioSermonDao {
    @Query("SELECT COUNT(*) FROM dacastaudioSermonlog_table")
    int getDacastVideoSermonCount();

    @Query("SELECT * FROM dacastaudioSermonlog_table")
    Flowable<List<DacastAudioSermonEntity>> getDacastVideoSermonsCatalog();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDacastVideoSermon(DacastAudioSermonEntity dacastAudioSermonEntity);

    @Update
    void updateDacastVideoSermon(DacastAudioSermonEntity... dacastVideoSermonEntities);

    @Query("DELETE FROM dacastaudioSermonlog_table")
    void deleteDacastVideoSermonTable();
}
