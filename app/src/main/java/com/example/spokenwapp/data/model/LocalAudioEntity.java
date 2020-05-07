package com.example.spokenwapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "localAudio_table")
public class LocalAudioEntity {

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "audio_Id")
        private long id;

        @ColumnInfo(name = "audio_Title")
        private String localAudioTitle;

        @ColumnInfo(name = "audio_Size")
        private String localAudioSize;

        @ColumnInfo(name = "video_Duration")
        private String localAudioDuration;

        @ColumnInfo(name = "audio_FilePath")
        private String localAudioFilePath;

        @ColumnInfo(name = "audio_ThumbnailUri")
        private String localAudioThumbnailUri;

        @ColumnInfo(name = "artist_Artist")
        private String localAudioArtist;

        @ColumnInfo(name = "artist_Album")
        private String localAudioAlbum;

    public LocalAudioEntity(long id, String localAudioTitle, String localAudioSize,
                            String localAudioDuration, String localAudioFilePath,
                            String localAudioThumbnailUri, String localAudioArtist,
                            String localAudioAlbum) {
        this.id = id;
        this.localAudioTitle = localAudioTitle;
        this.localAudioSize = localAudioSize;
        this.localAudioDuration = localAudioDuration;
        this.localAudioFilePath = localAudioFilePath;
        this.localAudioThumbnailUri = localAudioThumbnailUri;
        this.localAudioArtist = localAudioArtist;
        this.localAudioAlbum = localAudioAlbum;
    }

    @Ignore
    public LocalAudioEntity(String localAudioTitle, String localAudioSize,
                            String localAudioDuration, String localAudioFilePath,
                            String localAudioThumbnailUri, String localAudioArtist,
                            String localAudioAlbum) {
        this.localAudioTitle = localAudioTitle;
        this.localAudioSize = localAudioSize;
        this.localAudioDuration = localAudioDuration;
        this.localAudioFilePath = localAudioFilePath;
        this.localAudioThumbnailUri = localAudioThumbnailUri;
        this.localAudioArtist = localAudioArtist;
        this.localAudioAlbum = localAudioAlbum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalAudioTitle() {
        return localAudioTitle;
    }

    public void setLocalAudioTitle(String localAudioTitle) {
        this.localAudioTitle = localAudioTitle;
    }

    public String getLocalAudioSize() {
        return localAudioSize;
    }

    public void setLocalAudioSize(String localAudioSize) {
        this.localAudioSize = localAudioSize;
    }

    public String getLocalAudioDuration() {
        return localAudioDuration;
    }

    public void setLocalAudioDuration(String localAudioDuration) {
        this.localAudioDuration = localAudioDuration;
    }

    public String getLocalAudioFilePath() {
        return localAudioFilePath;
    }

    public void setLocalAudioFilePath(String localAudioFilePath) {
        this.localAudioFilePath = localAudioFilePath;
    }

    public String getLocalAudioThumbnailUri() {
        return localAudioThumbnailUri;
    }

    public void setLocalAudioThumbnailUri(String localAudioThumbnailUri) {
        this.localAudioThumbnailUri = localAudioThumbnailUri;
    }

    public String getLocalAudioArtist() {
        return localAudioArtist;
    }

    public void setLocalVideoArtist(String localVideoArtist) {
        this.localAudioArtist = localAudioArtist;
    }

    public String getLocalAudioAlbum() {
        return localAudioAlbum;
    }

    public void setLocalAudioAlbum(String localAudioAlbum) {
        this.localAudioAlbum = localAudioAlbum;
    }
}
