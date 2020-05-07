package com.example.spokenwapp.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "localVideos_table")
public class LocalVideoEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "video_Id")
    private long id;

    @ColumnInfo(name = "video_Title")
    private String localVideoTitle;

    @ColumnInfo(name = "video_Size")
    private String localVideoSize;

    @ColumnInfo(name = "video_Duration")
    private String localVideoDuration;

    @ColumnInfo(name = "video_FilePath")
    private String localVideoFilePath;

    @ColumnInfo(name = "video_ThumbnailUri")
    private String localVideoThumbnailUri;

    @ColumnInfo(name = "video_Artist")
    private String localVideoArtist;

    @Ignore
    public LocalVideoEntity(String localVideoTitle, String localVideoSize, String localVideoDuration,
                            String localVideoFilePath, String localVideoThumbnailUri,
                            String localVideoArtist) {
        this.localVideoTitle = localVideoTitle;
        this.localVideoSize = localVideoSize;
        this.localVideoDuration = localVideoDuration;
        this.localVideoFilePath = localVideoFilePath;
        this.localVideoThumbnailUri = localVideoThumbnailUri;
        this.localVideoArtist = localVideoArtist;
    }

    public LocalVideoEntity(long id, String localVideoTitle, String localVideoSize,
                            String localVideoDuration, String localVideoFilePath,
                            String localVideoThumbnailUri, String localVideoArtist) {
        this.id = id;
        this.localVideoTitle = localVideoTitle;
        this.localVideoSize = localVideoSize;
        this.localVideoDuration = localVideoDuration;
        this.localVideoFilePath = localVideoFilePath;
        this.localVideoThumbnailUri = localVideoThumbnailUri;
        this.localVideoArtist = localVideoArtist;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocalVideoTitle() {
        return localVideoTitle;
    }

    public void setLocalVideoTitle(String localVideoTitle) {
        this.localVideoTitle = localVideoTitle;
    }

    public String getLocalVideoSize() {
        return localVideoSize;
    }

    public void setLocalVideoSize(String localVideoSize) {
        this.localVideoSize = localVideoSize;
    }

    public String getLocalVideoDuration() {
        return localVideoDuration;
    }

    public void setLocalVideoDuration(String localVideoDuration) {
        this.localVideoDuration = localVideoDuration;
    }

    public String getLocalVideoArtist() {
        return localVideoArtist;
    }

    public void setLocalVideoArtist(String localVideoArtist) {
        this.localVideoArtist = localVideoArtist;
    }

    public String getLocalVideoFilePath() {
        return localVideoFilePath;
    }

    public void setLocalVideoFilePath(String localVideoFilePath) {
        this.localVideoFilePath = localVideoFilePath;
    }

    public String getLocalVideoThumbnailUri() {
        return localVideoThumbnailUri;
    }

    public void setLocalVideoThumbnailUri(String localVideoThumbnailUri) {
        this.localVideoThumbnailUri = localVideoThumbnailUri;
    }
}
