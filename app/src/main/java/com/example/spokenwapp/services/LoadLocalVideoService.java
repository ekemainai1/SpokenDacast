package com.example.spokenwapp.services;

import android.app.Application;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.data.model.LocalVideoEntity;
import com.example.spokenwapp.data.repository.LocalVideoRepository;
import com.example.spokenwapp.utilities.SpokenNetworkUtils;
import java.util.ArrayList;
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
    @Inject
    SpokenNetworkUtils spokenNetworkUtils;
    Handler mHandler;
    public LoadLocalVideoService() {
        super("LoadLocalVideoService");
    }


    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
        mHandler = new Handler();
        localVideoRepository = new LocalVideoRepository(getApplication());
    }

    protected void onHandleIntent(@Nullable Intent intent) {

        spokenNetworkUtils = new SpokenNetworkUtils();
        boolean network = spokenNetworkUtils.isNetworkOnline(getApplicationContext());
        boolean networkTrue = spokenNetworkUtils.getNetworksAvailable(getApplicationContext());

        if(network) {
            // Retrieve videos and store in database
            List<LocalVideoEntity> localVideoEntity = retrieveVideosFromDevice();
            Log.e("MyVideoSize", "" + localVideoEntity.size());
            for (int i = 0; i < localVideoEntity.size(); i++) {
                long videoNumbers = localVideoRepository.insertAllLocalVideos(localVideoEntity.get(i));
                Log.e("VideoNum", "" + videoNumbers);
            }

            // Retrieve audios and store in database
            List<LocalAudioEntity> localAudioEntity = retrieveAudiosFromDevice();
            Log.e("MyAudioSize", "" + localAudioEntity.size());
            for (int i = 0; i < localAudioEntity.size(); i++) {
                long audioNumbers = localVideoRepository.insertAllLocalAudios(localAudioEntity.get(i));
                Log.e("AudioNum", "" + audioNumbers);
            }

        }else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoadLocalVideoService.this,
                            "Network is not available!", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    // Method for retrieving files from external storage
    private List<LocalVideoEntity> retrieveVideosFromDevice() {

        List<LocalVideoEntity> videoList = new ArrayList<LocalVideoEntity>();
        LocalVideoEntity videos = null;

        String[] projection = new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Thumbnails._ID,
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
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int filPath = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int videoThumb = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails._ID);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST);
            int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int size = cursor.getInt(sizeColumn);
                int duration = cursor.getInt(durationColumn)/1000;
                String file = cursor.getString(filPath);
                String thumbnail = cursor.getString(videoThumb);
                String artist = cursor.getString(artistColumn);
                String album = cursor.getString(albumColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.

                videos = new LocalVideoEntity(id, name, formatSize(size),
                        duration, file, thumbnail, artist, album);
                videoList.add(videos);
            }
        }
        Log.e("VideoSize", ""+videoList.size());
        return videoList;
    }

    // Method for retrieving files from external storage
    public List<LocalAudioEntity> retrieveAudiosFromDevice() {

        List<LocalAudioEntity> audioList = new ArrayList<LocalAudioEntity>();
        LocalAudioEntity audios = null;


        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM_ARTIST,
                MediaStore.Audio.Media.DATE_ADDED
        };
        String selection = MediaStore.Audio.Media.DURATION +
                " >= ?";
        String[] selectionArgs = new String[]{
                String.valueOf(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES))
        };
        String sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC";
        String sortOrderGenre = MediaStore.Audio.Genres.DEFAULT_SORT_ORDER + " ASC";

        try(Cursor cursor = getApplicationContext().getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
            )) {
            // Cache column indices.
            assert cursor != null;
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
            int filePathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
            int genreCountColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
            int genreIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST);
            int genreNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED);
            while (cursor.moveToNext()) {
                // Get values of columns for a given audio.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                long duration = cursor.getLong(durationColumn);
                int size = cursor.getInt(sizeColumn);
                String filePath = cursor.getString(filePathColumn);
                String artist = cursor.getString(artistColumn);
                String album = cursor.getString(albumColumn);
                // Get values of columns for a given video.
                String genreId = cursor.getString(genreCountColumn);
                String genreName = cursor.getString(genreIdColumn);
                String genreKey = cursor.getString(genreNameColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                Log.e("AudioGenre", genreId);

                // Stores column values and the contentUri in a local object
                // that represents the media file.

                audios = new LocalAudioEntity(id, name, formatSize(size), formatSeconds(duration/1000),
                        duration, filePath, contentUri.toString(), artist, album, genreId, genreName, genreKey);
                audioList.add(audios);
            }
        }

            Log.e("AudioSize", ""+audioList.size());

        return audioList;
    }

    public static String formatSeconds(long timeInSeconds) {
        long hours = timeInSeconds / 3600;
        long secondsLeft = timeInSeconds - hours * 3600;
        long minutes = secondsLeft / 60;
        long seconds = secondsLeft - minutes * 60;

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

    public static String formatSize(int sizeInBytes) {
        String sizeString = null;
        int size;
        if(sizeInBytes < 1000){
           size = sizeInBytes;
           sizeString = size+" B";
        }
        if(sizeInBytes >= 1000 && sizeInBytes < 1000000){
            size = sizeInBytes/1000;
            sizeString = size+" KB";
        }
        if(sizeInBytes >= 1000000 && sizeInBytes < 1000000000){
            size = sizeInBytes/1000000;
            sizeString = size+" MB";
        }
        return sizeString;
    }

}
