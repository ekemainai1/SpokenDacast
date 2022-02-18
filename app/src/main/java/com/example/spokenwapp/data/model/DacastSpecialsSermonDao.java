package com.example.spokenwapp.data.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface DacastSpecialsSermonDao {
    @Query("SELECT COUNT(*) FROM dacastspecialsSermonlog_table")
    int getDacastAudioSermonCount();

    @Query("SELECT * FROM dacastspecialsSermonlog_table")
    Flowable<List<DacastSpecialSermonEntity>> getDacastAudioSermonsCatalog();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDacastAudioSermon(DacastSpecialSermonEntity dacastVideoSermonEntity);

    @Update
    void updateDacastAudioSermon(DacastSpecialSermonEntity... dacastVideoSermonEntities);

    @Query("DELETE FROM dacastspecialsSermonlog_table")
    void deleteDacastAudioSermonTable();
}
