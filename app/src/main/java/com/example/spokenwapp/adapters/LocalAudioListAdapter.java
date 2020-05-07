package com.example.spokenwapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spokenwapp.R;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.data.model.LocalVideoEntity;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

public class LocalAudioListAdapter extends RecyclerView.Adapter<LocalAudioListAdapter.LocalAudioListViewHolder>{

    @Inject
    Context context;
    @Inject
    List<LocalAudioEntity> audioEntities;
    Consumer<List<LocalAudioEntity>> consumer;

    @Inject
    public LocalAudioListAdapter(List<LocalAudioEntity> audioEntities,
                                 Consumer<List<LocalAudioEntity>> consumer) {
        this.audioEntities = audioEntities;
        this.consumer = consumer;
    }

    @NonNull
    @Override
    public LocalAudioListAdapter.LocalAudioListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_audio_list,
                parent, false);
        return new LocalAudioListAdapter.LocalAudioListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalAudioListAdapter.LocalAudioListViewHolder holder, int position) {
        LocalAudioEntity localAudioEntity = audioEntities.get(position);

        Bitmap bitmap;

        try {
            bitmap = retrieveAudioFrameFromAudio(localAudioEntity.getLocalAudioFilePath());
            bitmap = Bitmap.createScaledBitmap(bitmap, 150, 100, false);
            holder.audioThumbnail.setImageBitmap(bitmap);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
            //Log.e("BitMapSize", ""+bitmap.toString());
            holder.audioTitle.setText(localAudioEntity.getLocalAudioTitle());
            holder.audioArtist.setText(localAudioEntity.getLocalAudioArtist());
            holder.audioFileSize.setText(localAudioEntity.getLocalAudioSize());
            holder.audioTime.setText(localAudioEntity.getLocalAudioDuration());
            holder.audioOptions.setImageResource(R.drawable.ic_expand_more_black_24dp);
    }

    @Override
    public int getItemCount() {
        return audioEntities.size();
    }

    static class LocalAudioListViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView audioThumbnail;
        private TextView audioTitle;
        private TextView audioArtist;
        private  TextView audioFileSize;
        private TextView audioTime;
        private ImageView audioOptions;


        LocalAudioListViewHolder(@NonNull View itemView) {
            super(itemView);

            audioThumbnail = itemView.findViewById(R.id.audioThumbnail);
            audioTitle = itemView.findViewById(R.id.audioTitle);
            audioArtist = itemView.findViewById(R.id.audioArtist);
            audioFileSize = itemView.findViewById(R.id.audioSize);
            audioTime = itemView.findViewById(R.id.audioTime);
            audioOptions = itemView.findViewById(R.id.audioChoice);
            }
        }

    public static Bitmap retrieveAudioFrameFromAudio(String audioPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(audioPath, new HashMap<String, String>());
            mediaMetadataRetriever.setDataSource(audioPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retrieveAudioFrameFromAudio(String audioPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}
