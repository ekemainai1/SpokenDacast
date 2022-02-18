package com.example.spokenwapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spokenwapp.R;
import com.example.spokenwapp.church.ChurchPageViewModel;
import com.example.spokenwapp.data.model.LocalVideoEntity;
import com.example.spokenwapp.localvideos.LocalVideoPageViewModel;
import com.example.spokenwapp.utilities.SpokenBitmapImageProcessor;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.StatsSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class LocalVideoListAdapter extends RecyclerView.Adapter<LocalVideoListAdapter.LocalVideoListViewHolder> {

    @Inject
    Context context;
    List<LocalVideoEntity> localVideoList = new ArrayList<>();
    SpokenBitmapImageProcessor spokenBitmapImageProcessor = new SpokenBitmapImageProcessor();

    @Inject
    public LocalVideoListAdapter(LocalVideoPageViewModel viewModel, LifecycleOwner lifecycleOwner,
                                 Context context) {
        this.context = context;
        viewModel.getLocalVideoRepos().observe(lifecycleOwner, repos -> {
            if (localVideoList != null) {
                localVideoList.clear();
            }

            if (repos != null) {
                assert localVideoList != null;
                localVideoList.addAll(repos);
                notifyDataSetChanged();
            }
        });
        setHasStableIds(true);
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
        LocalVideoEntity localVideoEntity = localVideoList.get(position);
        Bitmap bitmap;

        Glide
                .with(context)
                .load(localVideoEntity.getLocalVideoFilePath())
                .centerCrop()
                .placeholder(R.drawable.church)
                .into(holder.videoThumbnail);

        //Log.e("BitMapSize", ""+bitmap.toString());
        holder.videoTitle.setText(localVideoEntity.getLocalVideoTitle());
        holder.videoArtist.setText(localVideoEntity.getLocalVideoArtist());
        holder.videoFileSize.setText(localVideoEntity.getLocalVideoSize());
    }

    @Override
    public long getItemId(int position) {
        return localVideoList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return localVideoList.size();
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

    public String getTitle(int pos){
        return localVideoList.get(pos).getLocalVideoTitle();
    }

    public String getId(int pos){
        return String.valueOf(localVideoList.get(pos).getId());
    }

    public String getVideoPath(int pos){
        return localVideoList.get(pos).getLocalVideoFilePath();
    }







}
