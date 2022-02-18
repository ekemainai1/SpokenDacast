package com.example.spokenwapp.data.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
@Dao
public interface DacastAnalyticsDao {

    @Query("SELECT COUNT(*) FROM dacastanalytics_table")
    int getDacastAnalyticsCount();

    @Query("SELECT * FROM dacastanalytics_table")
    Flowable<List<DacastAnalyticsEntity>> getDacastAnalyticsCatalog();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDacastAnalytics(DacastAnalyticsEntity dacastAnalyticsEntity);

    @Update
    void updateDacastAnalytics(DacastAnalyticsEntity... dacastAnalyticsEntities);

    @Query("DELETE FROM dacastanalytics_table")
    void deleteDacastAnalyticsTable();
}
