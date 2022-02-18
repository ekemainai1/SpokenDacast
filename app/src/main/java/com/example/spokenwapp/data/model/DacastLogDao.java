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
public interface DacastLogDao {

    @Query("SELECT COUNT(*) FROM dacastlog_table")
    int getLocalDacastLiveCount();

    @Query("SELECT * FROM dacastlog_table")
    Flowable<List<DacastLogEntity>> getDacastCatalog();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDacastLog(DacastLogEntity dacastLogEntity);

    @Query("UPDATE  dacastlog_table SET online=:online")
    int updateColDacastLog(boolean online);

   @Query("DELETE FROM dacastlog_table")
    void deleteDacastLogTable();


}
