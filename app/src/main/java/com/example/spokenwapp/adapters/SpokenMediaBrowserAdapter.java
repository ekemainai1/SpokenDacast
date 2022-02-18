package com.example.spokenwapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.spokenwapp.R;
import com.example.spokenwapp.utilities.SpokenBitmapImageProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import de.hdodenhof.circleimageview.CircleImageView;

public class SpokenMediaBrowserAdapter extends RecyclerView.Adapter<SpokenMediaBrowserAdapter.SpokenMediaBrowserViewHolder>{

    @Inject
    Context context;
    List<MediaBrowserCompat.MediaItem> mediaItemList = new ArrayList<>();
    SpokenBitmapImageProcessor imageProcessor = new SpokenBitmapImageProcessor();
    private int mTrackPlaying = -1;
    private SparseBooleanArray selectedItems;

    @Inject
    public SpokenMediaBrowserAdapter(Context context, List<MediaBrowserCompat.MediaItem> mediaItemList) {
        this.context = context;
        this.mediaItemList = mediaItemList;
        selectedItems = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public SpokenMediaBrowserAdapter.SpokenMediaBrowserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_audio_list,
                parent, false);
        return new SpokenMediaBrowserAdapter.SpokenMediaBrowserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpokenMediaBrowserViewHolder holder, int position) {
       MediaBrowserCompat.MediaItem mediaItem = mediaItemList.get(position);
        if(mTrackPlaying == position) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light));
        } else {
            // Here, you must restore the color because the view is reused.. so, you may receive a reused view with wrong colors
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        }


        Glide
                .with(context)
                .load(mediaItem.getDescription().getMediaUri())
                .centerCrop()
                .placeholder(R.drawable.branham)
                .into(holder.audioThumbnail);
        //holder.audioThumbnail.setImageBitmap(bitmap);

        String time = Objects.requireNonNull(mediaItem.getDescription().getExtras())
                .getString(MediaMetadataCompat.METADATA_KEY_DURATION);
        String size = Objects.requireNonNull(mediaItem.getDescription().getExtras())
                .getString(MediaMetadataCompat.METADATA_KEY_COMPOSER);
        //Log.e("BitMapSize", ""+bitmap.toString());
        holder.audioTitle.setText(mediaItem.getDescription().getTitle());
        holder.audioArtist.setText(mediaItem.getDescription().getSubtitle());
        holder.audioFileSize.setText(size);
        holder.audioTime.setText(time);
        holder.audioOptions.setImageResource(R.drawable.ic_baseline_menu_open_24);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(Objects.requireNonNull(mediaItemList.get(position).getMediaId()));
    }

    @Override
    public int getItemCount() {
        return mediaItemList.size();
    }

    static class SpokenMediaBrowserViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView audioThumbnail;
        private TextView audioTitle;
        private TextView audioArtist;
        private  TextView audioFileSize;
        private TextView audioTime;
        private ImageView audioOptions;

        SpokenMediaBrowserViewHolder(@NonNull View itemView) {
            super(itemView);

            audioThumbnail = itemView.findViewById(R.id.audioThumbnail);
            audioTitle = itemView.findViewById(R.id.audioTitle);
            audioArtist = itemView.findViewById(R.id.audioArtist);
            audioFileSize = itemView.findViewById(R.id.audioSize);
            audioTime = itemView.findViewById(R.id.audioTime);
            audioOptions = itemView.findViewById(R.id.audioChoice);
        }
    }

    public String getTitle(int pos){
        return Objects.requireNonNull(mediaItemList.get(pos).getDescription().getTitle()).toString();
    }

    public String getId(int pos){
        return String.valueOf(mediaItemList.get(pos).getMediaId());
    }

    public String getSongPath(int pos){
        return Objects.requireNonNull(mediaItemList.get(pos).getDescription().getMediaUri()).getPath();
    }

    public String getSongGenre(int pos){
        return Objects.requireNonNull(mediaItemList.get(pos).getDescription().getSubtitle()).toString();
    }

    public void setTrackPlaying(int position) {
        if(mTrackPlaying == position){
            mTrackPlaying = -1;
            notifyDataSetChanged();
            return;
        }
        mTrackPlaying = position;
        notifyDataSetChanged();

    }



}
