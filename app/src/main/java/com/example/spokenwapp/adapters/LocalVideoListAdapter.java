package com.example.spokenwapp.adapters;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.spokenwapp.R;
import com.example.spokenwapp.data.model.LocalVideoEntity;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class LocalVideoListAdapter extends RecyclerView.Adapter<LocalVideoListAdapter.LocalVideoListViewHolder> {
    @Inject
    List<LocalVideoEntity> videoEntities;
    Consumer<List<LocalVideoEntity>> consumer;

    @Inject
    public LocalVideoListAdapter(List<LocalVideoEntity> videoEntities,
                                 Consumer<List<LocalVideoEntity>> consumer) {
        this.videoEntities = videoEntities;
        this.consumer = consumer;
    }

    @NonNull
    @Override
    public LocalVideoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_video_list,
                parent, false);
        return new LocalVideoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalVideoListViewHolder holder, int position) {
        LocalVideoEntity localVideoEntity = videoEntities.get(position);
        Bitmap bitmap;

        try {
            bitmap = retrieveVideoFrameFromVideo(localVideoEntity.getLocalVideoFilePath());
            bitmap = Bitmap.createScaledBitmap(bitmap, 150, 100, false);
            holder.videoThumbnail.setImageBitmap(bitmap);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        //Log.e("BitMapSize", ""+bitmap.toString());
        holder.videoTitle.setText(localVideoEntity.getLocalVideoTitle());
        holder.videoArtist.setText(localVideoEntity.getLocalVideoArtist());
        holder.videoFileSize.setText(localVideoEntity.getLocalVideoSize());
    }

    @Override
    public int getItemCount() {
        return videoEntities.size();
    }

    static class LocalVideoListViewHolder extends RecyclerView.ViewHolder {
        private ImageView videoThumbnail;
        private TextView videoTitle;
        private TextView videoArtist;
        private  TextView videoFileSize;
        LocalVideoListViewHolder(@NonNull View itemView) {
            super(itemView);

            videoThumbnail = itemView.findViewById(R.id.videoThumbNail);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoArtist = itemView.findViewById(R.id.videoArtist);
            videoFileSize = itemView.findViewById(R.id.videoFileSize);
        }
    }

    public static Bitmap retrieveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
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
}
