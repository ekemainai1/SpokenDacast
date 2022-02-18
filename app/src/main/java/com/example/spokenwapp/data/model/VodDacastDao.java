package com.example.spokenwapp.data.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;


@Dao
public interface VodDacastDao {

    @Query("SELECT COUNT(*) FROM dacastvodlog_table")
    int getLocalDacastVodCount();

    @Query("SELECT * FROM dacastvodlog_table")
    Flowable<List<VodDacastLogEntity>> getDacastVodCatalog();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDacastVodLog(VodDacastLogEntity vodDacastLogEntity);

    @Update
    void updateDacastVodLog(VodDacastLogEntity vodDacastLogEntity);

    @Query("DELETE FROM dacastvodlog_table")
    void deleteDacastVodLogTable();

}
