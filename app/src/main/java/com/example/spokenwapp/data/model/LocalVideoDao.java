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
public interface LocalVideoDao {

    @Query("SELECT COUNT(*) FROM localVideos_table")
    LiveData<Integer> getLocalVideoCount();

    @Query("SELECT * FROM localVideos_table")
    Flowable<List<LocalVideoEntity>> getLocalVideoCatalog();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLocalVideo(LocalVideoEntity localVideoEntity);

    @Update
    void updateLocalVideos(LocalVideoEntity... localVideoEntities);


    @Query("DELETE FROM localVideos_table")
    void deleteVideosTable();


}
