package com.example.spokenwapp.adapters;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spokenwapp.R;
import com.example.spokenwapp.church.ChurchPageViewModel;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.data.model.LocalVideoEntity;
import com.example.spokenwapp.localaudio.LocalAudioPageViewModel;
import com.example.spokenwapp.utilities.SpokenBitmapImageProcessor;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.StatsSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

import static android.content.Context.ACTIVITY_SERVICE;

public class LocalAudioListAdapter extends RecyclerView.Adapter<LocalAudioListAdapter.LocalAudioListViewHolder>{

    @Inject
    Context context;
    List<LocalAudioEntity> audioEntities = new ArrayList<>();
    SpokenBitmapImageProcessor imageProcessor = new SpokenBitmapImageProcessor();
    private int mTrackPlaying = -1;



    @Inject
    public LocalAudioListAdapter(LocalAudioPageViewModel viewModel, LifecycleOwner lifecycleOwner,
                                Context context) {
        this.context = context;
        viewModel.getLocalAudioRepos().observe(lifecycleOwner, repos -> {
            if (audioEntities != null) {
                audioEntities.clear();
            }

            if (repos != null) {
                assert audioEntities != null;
                audioEntities.addAll(repos);
                notifyDataSetChanged();
            }
        });
        setHasStableIds(true);
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
        if(position == mTrackPlaying) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light));
        } else {
            // Here, you must restore the color because the view is reused.. so, you may receive a reused view with wrong colors
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        }

        Bitmap bitmap;

        Glide
                .with(context)
                .load(localAudioEntity.getLocalAudioFilePath())
                .centerCrop()
                .placeholder(R.drawable.branham)
                .into(holder.audioThumbnail);
        //holder.audioThumbnail.setImageBitmap(bitmap);


        //Log.e("BitMapSize", ""+bitmap.toString());
        holder.audioTitle.setText(localAudioEntity.getLocalAudioTitle());
        holder.audioArtist.setText(localAudioEntity.getLocalAudioArtist());
        holder.audioFileSize.setText(localAudioEntity.getLocalAudioSize());
        holder.audioTime.setText(localAudioEntity.getLocalAudioDuration());
        holder.audioOptions.setImageResource(R.drawable.ic_baseline_menu_open_24);
    }

    @Override
    public long getItemId(int position) {
        return audioEntities.get(position).getId();
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
