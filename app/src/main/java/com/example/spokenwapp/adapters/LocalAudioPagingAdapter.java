package com.example.spokenwapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.spokenwapp.R;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.utilities.SpokenBitmapImageProcessor;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import de.hdodenhof.circleimageview.CircleImageView;

public class LocalAudioPagingAdapter extends PagingDataAdapter<LocalAudioEntity,
        LocalAudioPagingAdapter.LocalAudioPagingViewHolder> {

    @Inject
    Context context;
    List<LocalAudioEntity> audioEntities = new ArrayList<>();
    SpokenBitmapImageProcessor imageProcessor = new SpokenBitmapImageProcessor();
    private int mTrackPlaying = -1;
    LocalAudioEntity localAudioEntity;

    public LocalAudioPagingAdapter(@NotNull DiffUtil.ItemCallback<LocalAudioEntity> diffCallback) {
        super(diffCallback);

    }

    @NonNull
    @Override
    public LocalAudioPagingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_audio_list,
                parent, false);
        return new LocalAudioPagingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalAudioPagingViewHolder holder, int position) {
        localAudioEntity = audioEntities.get(position);
        if(position == mTrackPlaying) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light));
        } else {
            // Here, you must restore the color because the view is reused.. so, you may receive a reused view with wrong colors
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        }

        Bitmap bitmap;

        try {
            bitmap = imageProcessor.retrieveVideoFrameFromVideo(localAudioEntity.getLocalAudioFilePath());
            imageProcessor.loadImage(holder.audioThumbnail, String.valueOf(bitmap));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        //holder.audioThumbnail.setImageBitmap(bitmap);


        //Log.e("BitMapSize", ""+bitmap.toString());
        holder.audioTitle.setText(localAudioEntity.getLocalAudioTitle());
        holder.audioArtist.setText(localAudioEntity.getLocalAudioArtist());
        holder.audioFileSize.setText(localAudioEntity.getLocalAudioSize());
        holder.audioTime.setText(localAudioEntity.getLocalAudioDuration());
        holder.audioOptions.setImageResource(R.drawable.ic_baseline_menu_open_24);
    }

    public void submitData(Lifecycle lifecycle) {
        lifecycle.addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                lifecycle.getCurrentState();
            }
        });
    }

    static class LocalAudioPagingViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView audioThumbnail;
        private TextView audioTitle;
        private TextView audioArtist;
        private  TextView audioFileSize;
        private TextView audioTime;
        private ImageView audioOptions;


        LocalAudioPagingViewHolder(@NonNull View itemView) {
            super(itemView);

            audioThumbnail = itemView.findViewById(R.id.audioThumbnail);
            audioTitle = itemView.findViewById(R.id.audioTitle);
            audioArtist = itemView.findViewById(R.id.audioArtist);
            audioFileSize = itemView.findViewById(R.id.audioSize);
            audioTime = itemView.findViewById(R.id.audioTime);
            audioOptions = itemView.findViewById(R.id.audioChoice);
        }
    }

    public static class LocalAudioComparator extends DiffUtil.ItemCallback<LocalAudioEntity> {
        @Override
        public boolean areItemsTheSame(@NonNull LocalAudioEntity oldItem,
                                       @NonNull LocalAudioEntity newItem) {
            // Id is unique.
            return oldItem.getId() == (newItem.getId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull LocalAudioEntity oldItem,
                                          @NonNull LocalAudioEntity newItem) {
            return oldItem.equals(newItem);
        }
    }

    public String getTitle(int pos){
        return audioEntities.get(pos).getLocalAudioTitle();
    }

    public String getId(int pos){
        return String.valueOf(audioEntities.get(pos).getId());
    }

    public String getSongPath(int pos){
        return audioEntities.get(pos).getLocalAudioFilePath();
    }

    public String getSongGenre(int pos){
        return audioEntities.get(pos).getLocalGenresName();
    }

    public void setTrackPlaying(int position) {
        mTrackPlaying = position;
    }
}
