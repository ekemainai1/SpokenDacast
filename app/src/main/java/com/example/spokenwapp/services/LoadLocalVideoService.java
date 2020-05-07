package com.example.spokenwapp.services;

import android.app.Application;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.data.model.LocalVideoEntity;
import com.example.spokenwapp.data.repository.LocalVideoRepository;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import dagger.android.AndroidInjection;
import dagger.android.DaggerIntentService;


public class LoadLocalVideoService extends DaggerIntentService {

    @Inject
    Application application;
    @Inject
    LocalVideoRepository localVideoRepository;

    public LoadLocalVideoService() {
        super("LoadLocalVideoService");
    }

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();

    }

    protected void onHandleIntent(@Nullable Intent intent) {

        // Retrieve videos and store in database
        List<LocalVideoEntity> localVideoEntity = retrieveVideosFromDevice();
        Log.e("MyVideoSize", ""+localVideoEntity.size());
        for(int i = 0; i < localVideoEntity.size(); i++) {
            long videoNumbers = localVideoRepository.insertAllLocalVideos(localVideoEntity.get(i));
            Log.e("VideoNum", ""+videoNumbers);
        }

        // Retrieve audios and store in database
        List<LocalAudioEntity> localAudioEntity = retrieveAudiosFromDevice();
        Log.e("MyAudioSize", ""+localAudioEntity.size());
        for(int i = 0; i < localAudioEntity.size(); i++) {
            long audioNumbers = localVideoRepository.insertAllLocalAudios(localAudioEntity.get(i));
            Log.e("AudioNum", ""+audioNumbers);
        }
    }

    // Method for retrieving files from external storage
    private List<LocalVideoEntity> retrieveVideosFromDevice() {

        List<LocalVideoEntity> videoList = new ArrayList<LocalVideoEntity>();
        LocalVideoEntity videos = null;

        String[] projection = new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.ARTIST,
                MediaStore.Video.Media.ALBUM
        };
        String selection = MediaStore.Video.Media.DURATION +
                " >= ?";
        String[] selectionArgs = new String[]{
                String.valueOf(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES))
        };
        String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";

        try (Cursor cursor = getApplicationContext().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        )) {
            // Cache column indices.
            assert cursor != null;
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int thumbColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST);
            int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn)/1000;
                int size = cursor.getInt(sizeColumn)/1000000;
                String thumbNail = cursor.getString(thumbColumn);
                String artist = cursor.getString(artistColumn);
                String album = cursor.getString(albumColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.

                videos = new LocalVideoEntity(id, name,size+" MB",
                        formatSeconds(duration), contentUri.toString(), thumbNail,album);
                videoList.add(videos);
            }
        }
        Log.e("VideoSize", ""+videoList.size());
        return videoList;
    }

    // Method for retrieving files from external storage
    private List<LocalAudioEntity> retrieveAudiosFromDevice() {

        List<LocalAudioEntity> audioList = new ArrayList<LocalAudioEntity>();
        LocalAudioEntity audios = null;

        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM
        };
        String selection = MediaStore.Audio.Media.DURATION +
                " >= ?";
        String[] selectionArgs = new String[]{
                String.valueOf(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES))
        };
        String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";

        try (Cursor cursor = getApplicationContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        )) {
            // Cache column indices.
            assert cursor != null;
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int filePathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn)/1000;
                int size = cursor.getInt(sizeColumn)/1000000;
                String filePath = cursor.getString(filePathColumn);
                String artist = cursor.getString(artistColumn);
                String album = cursor.getString(albumColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.

                audios = new LocalAudioEntity(id, name, size+" MB", formatSeconds(duration),
                        filePath, contentUri.toString(), artist, album);
                audioList.add(audios);
            }
        }
        Log.e("VideoSize", ""+audioList.size());
        return audioList;
    }

    public static String formatSeconds(int timeInSeconds) {
        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds ;

        return formattedTime;
    }

}
