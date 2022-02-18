package com.example.spokenwapp.data.database;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.spokenwapp.data.model.DacastAnalyticsDao;
import com.example.spokenwapp.data.model.DacastAnalyticsEntity;
import com.example.spokenwapp.data.model.DacastChannelAnalyticDao;
import com.example.spokenwapp.data.model.DacastSpecialsSermonDao;
import com.example.spokenwapp.data.model.DacastSpecialSermonEntity;
import com.example.spokenwapp.data.model.DacastLogDao;
import com.example.spokenwapp.data.model.DacastLogEntity;
import com.example.spokenwapp.data.model.DacastAudioSermonDao;
import com.example.spokenwapp.data.model.DacastAudioSermonEntity;
import com.example.spokenwapp.data.model.LocalAudioDao;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.data.model.LocalVideoDao;
import com.example.spokenwapp.data.model.LocalVideoEntity;
import com.example.spokenwapp.data.model.VodDacastDao;
import com.example.spokenwapp.data.model.VodDacastLogEntity;


@Database(entities = {LocalVideoEntity.class,
        LocalAudioEntity.class,
        DacastLogEntity.class,
        VodDacastLogEntity.class,
        DacastAudioSermonEntity.class,
        DacastSpecialSermonEntity.class,
        DacastAnalyticsEntity.class}, version = 1)
public abstract class AppLocalDatabase extends RoomDatabase {

    private static volatile AppLocalDatabase INSTANCE;
    public abstract LocalVideoDao localVideoDao();
    public abstract LocalAudioDao localAudioDao();
    public abstract DacastLogDao dacastLogDao();
    public abstract VodDacastDao vodDacastDao();
    public abstract DacastAudioSermonDao dacastVideoSermonDao();
    public abstract DacastSpecialsSermonDao dacastAudioSermonDao();
    public abstract DacastAnalyticsDao dacastAnalyticsDao();
    public abstract DacastChannelAnalyticDao dacastChannelAnalyticDao();

    public static AppLocalDatabase getInstance(final Application context) {
        if (INSTANCE == null) {
                synchronized (AppLocalDatabase.class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppLocalDatabase.class, "AppLocalDb.db")
                                .fallbackToDestructiveMigration()
                                .build();
                    }
                }
            }
            return INSTANCE;
        }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };


}
