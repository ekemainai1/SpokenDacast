package com.example.spokenwapp.utilities;

import android.support.v4.media.MediaMetadataCompat;

import com.google.android.exoplayer2.util.Log;

public class SpokenMediaIDHelper {
    private static final String TAG = "SpokenMediaIDHelper";
    // Media IDs used on browseable items of MediaBrowser
    public static final String MEDIA_ID_ROOT = "__ROOT__";
    public static final String MEDIA_ID_MUSICS_BY_GENRE = "__BY_GENRE__";

    public static String createTrackMediaID(String categoryType, String categoryValue,
                                                  MediaMetadataCompat track) {
        // MediaIDs are of the form <categoryType>/<categoryValue>|<musicUniqueId>, to make it easy to
        // find the category (like genre) that a music was selected from, so we
        // can correctly build the playing queue. This is specially useful when
        // one music can appear in more than one list, like "by genre -> genre_1"
        // and "by artist -> artist_1".
        String s = categoryType + "/" + categoryValue + "|" +
                track.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
        Log.e("CAT ID",s);
        return s;
    }

    public static String createBrowseCategoryMediaID(String categoryType, String categoryValue) {
        return categoryType + "/" + categoryValue;
    }

    /**
     * Extracts unique musicID from the mediaID. mediaID is, by this sample's convention, a
     * concatenation of category (eg "by_genre"), categoryValue (eg "Classical") and unique
     * musicID. This is necessary so we know where the user selected the music from, when the music
     * exists in more than one music list, and thus we are able to correctly build the playing queue.
     *
     * @param musicID
     * @return
     */
    public static String extractMusicIDFromMediaID(String musicID) {
        String[] segments = musicID.split("\\|", 2);
        return segments.length == 2 ? segments[1] : segments[0];
    }

    /**
     * Extracts category and categoryValue from the mediaID. mediaID is, by this sample's
     * convention, a concatenation of category (eg "by_genre"), categoryValue (eg "Classical") and
     * mediaID. This is necessary so we know where the user selected the music from, when the music
     * exists in more than one music list, and thus we are able to correctly build the playing queue.
     *
     * @param mediaID
     * @return
     */
    public static String[] extractBrowseCategoryFromMediaID(String mediaID) {
        if (mediaID.indexOf('|') >= 0) {
            mediaID = mediaID.split("\\|")[0];
        }
        if (mediaID.indexOf('/') == 0) {
            return new String[]{mediaID, null};
        } else {
            return mediaID.split("/", 3);
        }
    }

    public static String extractBrowseCategoryValueFromMediaID(String mediaID) {
        String[] categoryAndValue = extractBrowseCategoryFromMediaID(mediaID);
        if (categoryAndValue.length == 3) {
            return categoryAndValue[2];
        }
        return null;
    }
}
