package com.example.spokenwapp.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.widget.ImageView;

import com.example.spokenwapp.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.StatsSnapshot;

import java.io.File;
import java.util.HashMap;

public class SpokenBitmapImageProcessor {
    Context context;

    public SpokenBitmapImageProcessor() {

    }

    // Direct bitmap retrieval
    public Bitmap retrieveVideoFrameFromVideo(String filePath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(filePath, new HashMap<String, String>());
            mediaMetadataRetriever.setDataSource(filePath);

            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public void retrieveThumbnail(ImageView imageView, String filePath){
        Picasso.get()
                .load(new File(filePath))
                .placeholder(R.drawable.branham)
                .error(R.drawable.branham)
                .into(imageView);
    }

    public void loadImage(ImageView imageView, String productImage){
        //print picasso snap stats
        StatsSnapshot ss = Picasso.get().getSnapshot();
        Log.d("download image stats", ""+ss.cacheHits);
        Log.d("download image stats", ""+ss.cacheMisses);
        Log.d("download image stats", ""+ss.downloadCount);
        Log.d("download image stats", ""+ss.totalDownloadSize);

        //clear cache and cancel pending requests
        Picasso.get().invalidate(productImage);
        Picasso.get().cancelRequest(imageView);

        //set image rotation and placeholder image
        RequestCreator rc = Picasso.get().load(productImage);
        rc.rotate(0).placeholder(R.drawable.church)
                //set error image, memory policy
                .error(R.drawable.churchnews)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                //resize and crop
                .fit()
                //load image to imageview
                .into(imageView);
    }

}
