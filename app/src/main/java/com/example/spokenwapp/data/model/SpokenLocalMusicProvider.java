package com.example.spokenwapp.data.model;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.LocalAudioListSearchAdapter;
import com.example.spokenwapp.data.repository.LocalVideoRepository;
import com.example.spokenwapp.services.SpokenMediaBrowserService;
import com.example.spokenwapp.utilities.SpokenLogHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import javax.inject.Inject;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import static com.example.spokenwapp.services.SpokenMediaBrowserService.BROWSEABLE_ROOT;

public class SpokenLocalMusicProvider {

    public final static String TAG = "VIDEOLOG";
    // Categorized caches for music track data:
    private final HashMap<String, List<MediaMetadataCompat>> mMusicListByGenre;
    private final HashMap<String, MediaBrowserCompat.MediaItem> mMusicListPlayListByApp;
    private final HashMap<String, MediaMetadataCompat> mMusicListById;
    private final HashMap<String, List<MediaBrowserCompat.MediaItem>> mMusicPlayListByGenre;
    private final HashMap<String, List<MediaBrowserCompat.MediaItem>> mMusicPlayList;
    private final HashMap<String,  MediaBrowserCompat.MediaItem> videoList;
    private final HashMap<String,  MediaMetadataCompat> videoListItems;

    private Flowable<List<LocalAudioEntity>> deviceSongs;
    private Flowable<List<LocalVideoEntity>> deviceVideos;
    List<MediaMetadataCompat> lists;
    List<MediaBrowserCompat.MediaItem> mediaItemList;
    @Inject
    LocalVideoRepository localVideoRepository;
    private CompositeDisposable disposable;
    private CompositeDisposable compositeDisposable;
    private final ReentrantLock initializationLock = new ReentrantLock();

    enum State {
        NON_INITIALIZED, INITIALIZING, INITIALIZED;
    }

    private State mCurrentState = State.NON_INITIALIZED;

    public interface Callback {
        void onMusicCatalogReady(boolean success);
    }

    public SpokenLocalMusicProvider(Application application) {
        mMusicListByGenre = new HashMap<>();
        mMusicListById = new HashMap<>();
        mMusicPlayListByGenre = new HashMap<>();
        mMusicPlayList = new HashMap<>();
        mMusicListPlayListByApp = new HashMap<>();
        videoList = new HashMap<>();
        videoListItems = new HashMap<>();
        localVideoRepository = new LocalVideoRepository(application);
        disposable = new CompositeDisposable();
        compositeDisposable = new CompositeDisposable();
        lists = new ArrayList<>();
        mediaItemList = new ArrayList<>();
    }

    /**
     * Get an iterator over the list of genres
     *
     * @return
     */
    public Iterable<String> getGenres() {
        // Initial genre list
        Iterable<String> genreList = new ArrayList<String>(0);
        if (mMusicListByGenre.isEmpty()) {
            return genreList;
        }
        return mMusicListByGenre.keySet();
    }

    /**
     * Get music tracks of the given genre
     *
     * @return
     */
    public Iterable<MediaMetadataCompat> getMusicsByGenre(String genre) {
        if (!mMusicListByGenre.containsKey(genre)) {
            return new ArrayList<MediaMetadataCompat>();
        }
        return mMusicListByGenre.get(genre);
    }

    /*
        If you have the Id, get music file
     */
    public MediaMetadataCompat getMusic(String mediaId) {
        return mMusicListById.get(mediaId);
    }

    /*
       If you have the Id, get music file list
    */
    public List<MediaMetadataCompat> getAllMusicById() {
        List<MediaMetadataCompat> metadataCompat = new ArrayList<>();
        for(String key: mMusicListById.keySet()){
           MediaMetadataCompat mediaMetadataCompat =mMusicListById.get(key);
           metadataCompat.add(mediaMetadataCompat);
        }
        Collections.sort(metadataCompat, new Comparator<MediaMetadataCompat>() {
            @Override
            public int compare(MediaMetadataCompat localAudioEntity, MediaMetadataCompat textSort) {
                return Objects.requireNonNull(localAudioEntity.getDescription().getTitle()).toString()
                        .compareTo(Objects.requireNonNull(textSort.getDescription().getTitle()).toString());
            }
        });

        return metadataCompat;
    }

    public List<MediaMetadataCompat> getAllVideos() {
        List<MediaMetadataCompat> metadataCompat = new ArrayList<>();
        for(String key: videoListItems.keySet()){
            MediaMetadataCompat mediaMetadataCompat = videoListItems.get(key);
            metadataCompat.add(mediaMetadataCompat);
        }
        return metadataCompat;
    }

    /*
      If you have the Id, get music file list
   */
    public List<MediaBrowserCompat.MediaItem>  getAllVideosById() {

        List<MediaBrowserCompat.MediaItem> metadataCompat = new ArrayList<>();
        for(String key: videoList.keySet()){
            MediaBrowserCompat.MediaItem mediaItem = videoList.get(key);
            metadataCompat.add(mediaItem);
        }
        Collections.sort(metadataCompat, new Comparator<MediaBrowserCompat.MediaItem>() {
            @Override
            public int compare(MediaBrowserCompat.MediaItem localVideoEntity,MediaBrowserCompat.MediaItem videoEntity) {
                return Objects.requireNonNull(localVideoEntity.getDescription().getTitle()).toString()
                        .compareTo(Objects.requireNonNull(videoEntity.getDescription().getTitle()).toString());
            }
        });
        return metadataCompat;
    }

    /*
      If you have the Id, get music file list
   */
    public List<MediaBrowserCompat.MediaItem>  getAllSongsById() {

        List<MediaBrowserCompat.MediaItem> metadataCompat = new ArrayList<>();
        for(String key: mMusicListPlayListByApp.keySet()){
            MediaBrowserCompat.MediaItem mediaItem = mMusicListPlayListByApp.get(key);
            metadataCompat.add(mediaItem);
        }
        Collections.sort(metadataCompat, new Comparator<MediaBrowserCompat.MediaItem>() {
            @Override
            public int compare(MediaBrowserCompat.MediaItem localAudioEntity, MediaBrowserCompat.MediaItem textSort) {
                return Objects.requireNonNull(localAudioEntity.getDescription().getTitle()).toString()
                        .compareTo(Objects.requireNonNull(textSort.getDescription().getTitle()).toString());
            }
        });
        return metadataCompat;
    }

    private MediaBrowserCompat.MediaItem generatePlayableVideoMediaItem(LocalVideoEntity video,
                                                                        int flag, Context context) {
        if (video == null)
            return null;

        Bitmap bitmap = null;
        try {
            bitmap =retrieveAudioFrameFromAudio(context);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        MediaDescriptionCompat.Builder mediaDescriptionBuilder = new MediaDescriptionCompat.Builder();
        mediaDescriptionBuilder.setMediaId(String.valueOf(video.getId()));

        if (!TextUtils.isEmpty(video.getLocalVideoTitle()))
            mediaDescriptionBuilder.setTitle(video.getLocalVideoTitle());

        if (!TextUtils.isEmpty(video.getLocalVideoArtist()))
            mediaDescriptionBuilder.setSubtitle(video.getLocalVideoArtist());

        if (!TextUtils.isEmpty(video.getLocalVideoThumbnailUri()))
            mediaDescriptionBuilder.setIconUri(Uri.parse(video.getLocalVideoThumbnailUri()));
        if (bitmap != null) {
            if (!TextUtils.isEmpty(video.getLocalVideoThumbnailUri()))
                mediaDescriptionBuilder.setIconBitmap(bitmap);
        } else {
            if (!TextUtils.isEmpty(video.getLocalVideoThumbnailUri()))
                mediaDescriptionBuilder.setIconBitmap(BitmapFactory.decodeResource(
                        Resources.getSystem(), R.drawable.branham));
        }
        if (!TextUtils.isEmpty(video.getLocalVideoFilePath()))
            mediaDescriptionBuilder.setMediaUri(Uri.parse(video.getLocalVideoFilePath()));

        return new MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(),
                flag);
    }


    /*
       If you have the Id, get music file list
    */
    public List<MediaBrowserCompat.MediaItem> getMusicByIdForApp(String mediaId) {
        return mMusicPlayList.get(mediaId);
    }

    /**
     * Very basic implementation of a search that filter music tracks which title containing
     * the given query.
     *
     * @return
     */
    public Iterable<MediaMetadataCompat> searchMusics(String titleQuery) {
        ArrayList<MediaMetadataCompat> result = new ArrayList<>();

        titleQuery = titleQuery.toLowerCase();
        for (MediaMetadataCompat track : mMusicListById.values()) {
            if (track.getString(MediaMetadataCompat.METADATA_KEY_TITLE).toLowerCase()
                    .contains(titleQuery)) {
                result.add(track);
            }
        }
        return result;
    }

    /**
     * Generate media file structure for user when they click item on the Auto Browser
     */
    public List<MediaBrowserCompat.MediaItem> getMediaItemsById(String id) {
        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        if (SpokenMediaBrowserService.ANDROID_AUTO_PACKAGE_NAME.equalsIgnoreCase(id)) {
            for (String genres : getGenres()) {
                if (genres.equals(id)) {
                    mediaItems.add(generateBrowseableMediaItemByGenre(genres));
                }
            }
        } else if (!TextUtils.isEmpty(id)) {
            return getPlayableMediaItemByGenre(id);
        }
        return mediaItems;
    }


    /**
     * Generate browseable mediaItem
     */
    public MediaBrowserCompat.MediaItem generateBrowseableMediaItemByGenre(String genre) {
        MediaDescriptionCompat.Builder builder = new MediaDescriptionCompat.Builder();
        builder.setMediaId(genre);
        builder.setTitle(genre);
        builder.setIconBitmap(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.branham));
        return new MediaBrowserCompat.MediaItem(builder.build(),
                MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);
    }

    private MediaBrowserCompat.MediaItem generatePlayableMediaItem(LocalAudioEntity song, int flag, Context context) {
        Bundle bundle = new Bundle();
        if (song == null)
            return null;

        Bitmap bitmap = null;
        try {
            bitmap =retrieveAudioFrameFromAudio(context);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        MediaDescriptionCompat.Builder mediaDescriptionBuilder = new MediaDescriptionCompat.Builder();
        mediaDescriptionBuilder.setMediaId(String.valueOf(song.getId()));

        if (!TextUtils.isEmpty(song.getLocalAudioTitle()))
            mediaDescriptionBuilder.setTitle(song.getLocalAudioTitle());

        if (!TextUtils.isEmpty(song.getLocalAudioArtist()))
            mediaDescriptionBuilder.setSubtitle(song.getLocalAudioArtist());

        if (!TextUtils.isEmpty(song.getLocalAudioThumbnailUri()))
            mediaDescriptionBuilder.setIconUri(Uri.parse(song.getLocalAudioThumbnailUri()));
        if (bitmap != null) {
            if (!TextUtils.isEmpty(song.getLocalAudioThumbnailUri()))
                mediaDescriptionBuilder.setIconBitmap(bitmap);
        } else {
            if (!TextUtils.isEmpty(song.getLocalAudioThumbnailUri()))
                mediaDescriptionBuilder.setIconBitmap(BitmapFactory.decodeResource(
                        Resources.getSystem(), R.drawable.branham));
        }
        if (!TextUtils.isEmpty(song.getLocalAudioFilePath()))
            mediaDescriptionBuilder.setMediaUri(Uri.parse(song.getLocalAudioFilePath()));
        bundle.putString(MediaMetadataCompat.METADATA_KEY_DURATION, song.getLocalAudioDuration());
        bundle.putString(MediaMetadataCompat.METADATA_KEY_COMPOSER, song.getLocalAudioSize());
        mediaDescriptionBuilder.setExtras(bundle);

        return new MediaBrowserCompat.MediaItem(mediaDescriptionBuilder.build(),
                flag);
    }

    /**
     * Generate playable mediaItems
     */
    public List<MediaBrowserCompat.MediaItem> getPlayableMediaItemByGenre(String genre) {
        if (!mMusicPlayListByGenre.containsKey(genre)) {
            return new ArrayList<MediaBrowserCompat.MediaItem>();
        }
        return mMusicPlayListByGenre.get(genre);
    }

    public Bitmap retrieveAudioFrameFromAudio(Context context)
            throws Throwable {
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.branham);
        bitmap = Bitmap.createScaledBitmap(bitmap, 150, 100, false);
        return bitmap;
    }

    /**
     * Get the list of music tracks from a server and caches the track information
     * for future reference, keying tracks by mediaId and grouping by genre.
     */
    public void retrieveMedia(Callback callback, Context context) {

        // Asynchronously load the music catalog in a separate thread
        deviceSongs = localVideoRepository.getAllLocalAudioCatalog();

        disposable.add(deviceSongs
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribeWith(new DisposableSubscriber<List<LocalAudioEntity>>() {

                    @Override
                    public void onNext(List<LocalAudioEntity> localAudioEntities) {

                        retrieveMediaAsync(callback, localAudioEntities, context);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Discard observable when transaction error occurs
                        disposable.clear();
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        // Discard observable when transaction completes
                        disposable.clear();
                        disposable.dispose();
                    }

                }));
    }

    public void getVideoListForPlayBack(Callback callback,  Context context) {

        compositeDisposable.add(localVideoRepository.getAllLocalVideoCatalog()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<LocalVideoEntity>>() {

                    @Override
                    public void onNext(List<LocalVideoEntity> localVideoEntities) {

                        retrieveVideoMediaAsync(callback, localVideoEntities, context);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG,"FETCH SEARCH DATA: "+"completed");
                    }

                }));

    }

    private void retrieveVideoMediaAsync(Callback callback, List<LocalVideoEntity> localVideoEntities, Context context){
        try {
            if (mCurrentState == State.NON_INITIALIZED) {
                mCurrentState = State.INITIALIZING;
                Log.e("MVVM BROWSER", String.valueOf(localVideoEntities.size()));

                for (int j = 0; j < localVideoEntities.size(); j++) {
                    MediaMetadataCompat item = buildVideosSourceFromDatabase(localVideoEntities.get(j));
                    Log.e("VIDEO SORT", Objects.requireNonNull(item.getDescription().getTitle()).toString());
                    MediaBrowserCompat.MediaItem mediaItem = generatePlayableVideoMediaItem(localVideoEntities.get(j),
                            MediaBrowserCompat.MediaItem.FLAG_PLAYABLE, context);
                    String mediaId = String.valueOf(item.getDescription().getMediaId());
                    MediaBrowserCompat.MediaItem mediaItemsVideos = videoList.get(mediaId);

                    videoList.put(mediaId, mediaItem);
                    videoListItems.put(mediaId, item);
                }
                Log.e("VIDEOS LOADED", "videos by id loaded...");
                mCurrentState = State.INITIALIZED;
            }
        } catch (RuntimeException e) {
            SpokenLogHelper.e(TAG, e, "Could not retrieve video list");
        } finally {
            if (mCurrentState != State.INITIALIZED) {
                // Something bad happened, so we reset state to NON_INITIALIZED to allow
                // retries (eg if the network connection is temporary unavailable)
                mCurrentState = State.NON_INITIALIZED;
            }
            if (callback != null) {
                callback.onMusicCatalogReady(mCurrentState == State.INITIALIZED);
            }
        }

    }


    private void retrieveMediaAsync(Callback callback, List<LocalAudioEntity> localAudioEntities,
                                    Context context){
        try {
            if (mCurrentState == State.NON_INITIALIZED) {
                mCurrentState = State.INITIALIZING;
                Log.e("MVVM BROWSER", String.valueOf(localAudioEntities.size()));

                for (int j = 0; j < localAudioEntities.size(); j++) {

                    MediaMetadataCompat item = buildFromDatabase(localAudioEntities.get(j));
                    Log.e("MEDIA SORT", Objects.requireNonNull(item.getDescription().getTitle()).toString());
                    MediaBrowserCompat.MediaItem mediaItem = generatePlayableMediaItem(localAudioEntities.get(j),
                            MediaBrowserCompat.MediaItem.FLAG_BROWSABLE, context);
                    MediaBrowserCompat.MediaItem mediaItems = generatePlayableMediaItem(localAudioEntities.get(j),
                            MediaBrowserCompat.MediaItem.FLAG_PLAYABLE, context);
                    String genre = item.getString(MediaMetadataCompat.METADATA_KEY_GENRE);
                    String mediaId = String.valueOf(item.getDescription().getMediaId());
                    List<MediaMetadataCompat> list = mMusicListByGenre.get(genre);
                    List<MediaBrowserCompat.MediaItem> mediaItemList = mMusicPlayListByGenre.get(genre);
                    List<MediaBrowserCompat.MediaItem> mediaItemsPlay = mMusicPlayList.get(mediaId);
                    MediaBrowserCompat.MediaItem mediaItemsPlayApp = mMusicListPlayListByApp.get(mediaId);

                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    if (mediaItemList == null) {
                        mediaItemList = new ArrayList<>();
                    }
                    if (mediaItemsPlay == null) {
                        mediaItemsPlay = new ArrayList<>();
                    }


                    list.add(item);
                    mediaItemList.add(mediaItem);
                    mediaItemsPlay.add(mediaItems);
                    //mediaItemsPlayApp.add(mediaItems);

                    mMusicListByGenre.put(genre, list);
                    mMusicListById.put(item.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID), item);
                    mMusicPlayListByGenre.put(genre, mediaItemList);
                    mMusicPlayList.put(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaItemsPlay);
                    mMusicListPlayListByApp.put(mediaId, mediaItems);

                }
                Log.e("DATA LOADED", "songs by genre loaded...");

                mCurrentState = State.INITIALIZED;
            }
        } catch (RuntimeException e) {
            SpokenLogHelper.e(TAG, e, "Could not retrieve music list");
        } finally {
            if (mCurrentState != State.INITIALIZED) {
                // Something bad happened, so we reset state to NON_INITIALIZED to allow
                // retries (eg if the network connection is temporary unavailable)
                mCurrentState = State.NON_INITIALIZED;
            }

            if (callback != null) {
                callback.onMusicCatalogReady(mCurrentState == State.INITIALIZED);
            }
        }
    }

    private MediaMetadataCompat buildFromDatabase(LocalAudioEntity localAudioEntity) {
        String title = localAudioEntity.getLocalAudioTitle();
        String album = localAudioEntity.getLocalAudioAlbum();
        String artist = localAudioEntity.getLocalAudioArtist();
        long duration = localAudioEntity.getLocalLongDuration();
        String source = localAudioEntity.getLocalAudioFilePath();
        String iconUrl = localAudioEntity.getLocalAudioThumbnailUri();
        String genreId = localAudioEntity.getLocalGenresId();
        String genresName = localAudioEntity.getLocalGenresName();
        String genreKey = localAudioEntity.getLocalGenresKey(); // ms


        // Since we don't have a unique ID in the server, we fake one using the hashcode of
        // the music source. In a real world app, this could come from the server.
        String id = String.valueOf(localAudioEntity.getId());
        // Adding the music source to the MediaMetadata (and consequently using it in the
        // mediaSession.setMetadata) is not a good idea for a real world music app, because
        // the session metadata can be accessed by notification listeners. This is done in this
        // sample for convenience only.
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, source)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_DATE, genreId)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genresName)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, genreKey)
                .build();
    }

    private MediaMetadataCompat buildVideosSourceFromDatabase(LocalVideoEntity localVideoEntity) {
        String title = localVideoEntity.getLocalVideoTitle();
        String album = localVideoEntity.getLocalVideoAlbum();
        String artist = localVideoEntity.getLocalVideoArtist();
        long duration = localVideoEntity.getLocalVideoDuration();
        String source = localVideoEntity.getLocalVideoFilePath();
        String iconUrl = localVideoEntity.getLocalVideoThumbnailUri();
        String videoSize = localVideoEntity.getLocalVideoSize();



        // Since we don't have a unique ID in the server, we fake one using the hashcode of
        // the music source. In a real world app, this could come from the server.
        String id = String.valueOf(localVideoEntity.getId());
        // Adding the music source to the MediaMetadata (and consequently using it in the
        // mediaSession.setMetadata) is not a good idea for a real world music app, because
        // the session metadata can be accessed by notification listeners. This is done in this
        // sample for convenience only.
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, source)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_DATE, videoSize)
                .build();
    }

    public int getMusicIndexOnQueue(Iterable<MediaSessionCompat.QueueItem> queue,
                                           String mediaId) {
        int index = 0;
        for (MediaSessionCompat.QueueItem item: queue) {
            if (mediaId.equals(item.getDescription().getMediaId())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public boolean isInitialized() {
        return mCurrentState == State.INITIALIZED;
    }

    public List<MediaSessionCompat.QueueItem> convertToQueue(
            Iterable<MediaMetadataCompat> tracks) {
        List<MediaSessionCompat.QueueItem> queue = new ArrayList<>();
        int count = 0;
        for (MediaMetadataCompat track : tracks) {
            // We don't expect queues to change after created, so we use the item index as the
            // queueId. Any other number unique in the queue would work.
            MediaSessionCompat.QueueItem item = new MediaSessionCompat.QueueItem(
                    track.getDescription(), count++);
            queue.add(item);
        }
        return queue;
    }

}
