package com.example.spokenwapp.utilities;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import com.example.spokenwapp.data.model.SpokenLocalMusicProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static com.example.spokenwapp.utilities.SpokenMediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE;

public class SpokenQueueHelper {
    private static final String TAG = "QueueHelper";

    public static List<MediaSessionCompat.QueueItem> getPlayingQueue(String mediaId,
                        SpokenLocalMusicProvider musicProvider) {
        // extract the category and unique music ID from the media ID:
        String[] category = SpokenMediaIDHelper.extractBrowseCategoryFromMediaID(mediaId);
        Log.e("CATEGORY", category[0]);
        Log.e("CAT SIZE", String.valueOf(category.length));
        // This sample only supports genre category.
        if (!category[0].equals(MEDIA_ID_MUSICS_BY_GENRE) || category.length != 3) {
            SpokenLogHelper.e(TAG, "Could not build a playing queue for this mediaId: ", mediaId);
            return null;
        }
        String categoryValue = category[2];
        SpokenLogHelper.e(TAG, "Creating playing queue for musics of genre ", categoryValue);

        /*List<MediaMetadataCompat> compatList = musicProvider.retrieveMedia(new SpokenLocalMusicProvider.Callback() {
            @Override
            public void onMusicCatalogReady(boolean success) {

            }
        });*/

        List<MediaSessionCompat.QueueItem> queue = convertToQueue(musicProvider.getMusicsByGenre(categoryValue));
        Log.e("QUEUE CAT", categoryValue);
        Log.e("QUEUE", String.valueOf(queue.size()));
        return queue;
    }

    public static List<MediaSessionCompat.QueueItem> getPlayingQueueFromSearch(String query,
                                   SpokenLocalMusicProvider musicProvider) {
        SpokenLogHelper.e(TAG, "Creating playing queue for musics from search ", query);
        return convertToQueue(musicProvider.searchMusics(query));
    }

    public static int getMusicIndexOnQueue(Iterable<MediaSessionCompat.QueueItem> queue,
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

    public static int getMusicIndexOnQueue(Iterable<MediaSessionCompat.QueueItem> queue,
                                                 long queueId) {
        int index = 0;
        for (MediaSessionCompat.QueueItem item: queue) {
            if (queueId == item.getQueueId()) {
                Log.e("INDEX", String.valueOf(index));
                Log.e("INDEX", String.valueOf(item.getQueueId()));
                return index;
            }
            index++;
        }
        return -1;
    }

    private static List<MediaSessionCompat.QueueItem> convertToQueue(Iterable<MediaMetadataCompat> tracks) {
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
    /**
     * Create a random queue. For simplicity sake, instead of a random queue, we create a
     * queue using the first genre,
     *
     * @param musicProvider
     * @return
     */
    public static List<MediaSessionCompat.QueueItem> getRandomQueue(SpokenLocalMusicProvider musicProvider) {
        Iterator<String> genres = musicProvider.getGenres().iterator();
        if (!genres.hasNext()) {
            return new ArrayList<>();
        }
        String genre = genres.next();
        Iterable<MediaMetadataCompat> tracks = musicProvider.getMusicsByGenre(genre);
        return convertToQueue(tracks);
    }


    public static boolean isIndexPlayable(int index, List<MediaSessionCompat.QueueItem> queue) {
        return (queue != null && index >= 0 && index < queue.size());
    }
}
