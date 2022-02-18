package com.example.spokenwapp.adapters;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spokenwapp.R;
import com.example.spokenwapp.data.model.VodDacastLogEntity;
import com.example.spokenwapp.selectedchurch.SelectedChurchViewModel;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.StatsSnapshot;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.ACTIVITY_SERVICE;

public class DacastVODOnlineAdapter extends RecyclerView.Adapter<DacastVODOnlineAdapter.DacastVODOnlineViewHolder>{

    List<VodDacastLogEntity> list = new ArrayList<>();;
    @Inject
    Context context;


    public DacastVODOnlineAdapter(SelectedChurchViewModel viewModel, LifecycleOwner lifecycleOwner,
                             Context context) {
        this.context = context;
        viewModel.getVodRepos().observe(lifecycleOwner, repos -> {
            if (list != null) {
                list.clear();
            }

            if (repos != null) {
                assert list != null;
                list.addAll(repos);
                notifyDataSetChanged();
            }
        });
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public DacastVODOnlineAdapter.DacastVODOnlineViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                               int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.selected_church_online_list_item,
                parent, false);
        return new DacastVODOnlineAdapter.DacastVODOnlineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DacastVODOnlineAdapter.DacastVODOnlineViewHolder holder, int position) {

        VodDacastLogEntity churchVod = list.get(position);
        Log.e("MVVMO", churchVod.getThumbnail().get(0));
        holder.textViewTitle.setText(churchVod.getTitle());
        holder.textViewChurch.setText(churchVod.getDescription());
        loadImage(holder.imageViewOnline, churchVod.getThumbnail().get(0));
        holder.textViewViewArtist.setText(churchVod.getContentIds());
        loadImage(holder.circleImageViewArtist, churchVod.getThumbnail().get(0));
        holder.textViewViewers.setText(churchVod.getCategoryId()+" views");
        holder.textViewDuration.setText(churchVod.getDuration());

    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class DacastVODOnlineViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewChurch;
        private ImageView imageViewOnline;
        private TextView textViewViewArtist;
        private CircleImageView circleImageViewArtist;
        private TextView textViewViewers;
        ImageView imageViewMenu;
        TextView textViewDuration;


        public DacastVODOnlineViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewContentTitle);
            textViewChurch = itemView.findViewById(R.id.textViewChurchName);
            imageViewOnline = itemView.findViewById(R.id.imageViewOnline);
            textViewViewArtist= itemView.findViewById(R.id.textViewArtist);
            circleImageViewArtist = itemView.findViewById(R.id.contentArtist);
            textViewViewers = itemView.findViewById(R.id.textViewContViewers);
            imageViewMenu = itemView.findViewById(R.id.imageViewIcon);
            textViewDuration = itemView.findViewById(R.id.textViewDuration);
        }
    }

    public static class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }

    private Picasso getCustomPicasso(){
        Picasso.Builder builder = new Picasso.Builder(this.context);
        //set 12% of available app memory for image cache
        builder.memoryCache(new LruCache(getBytesForMemCache()));
        //set request transformer
        Picasso.RequestTransformer requestTransformer =  new Picasso.RequestTransformer() {
            @Override
            public Request transformRequest(Request request) {
                Log.d("image request", request.toString());
                return request;
            }
        };
        builder.requestTransformer(requestTransformer);

        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, @NonNull Uri uri,
                                          Exception exception) {
                Log.d("Image load error", Objects.requireNonNull(uri.getPath()));
            }
        });

        return builder.build();
    }

    //returns the given percentage of available memory in bytes
    private int getBytesForMemCache(){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(ACTIVITY_SERVICE);
        assert activityManager != null;
        activityManager.getMemoryInfo(mi);

        double availableMemory= mi.availMem;

        return (int)(12 *availableMemory/100);
    }

    private void loadImage(ImageView imageViewOnline, String productImage){
        //print picasso snap stats
        StatsSnapshot ss = Picasso.get().getSnapshot();
        Log.d("download image stats", ""+ss.cacheHits);
        Log.d("download image stats", ""+ss.cacheMisses);
        Log.d("download image stats", ""+ss.downloadCount);
        Log.d("download image stats", ""+ss.totalDownloadSize);

        //clear cache and cancel pending requests
        Picasso.get().invalidate(productImage);
        Picasso.get().cancelRequest(imageViewOnline);

        //set image rotation and placeholder image
        RequestCreator rc = Picasso.get().load(productImage);
        rc.rotate(0).placeholder(R.drawable.church)
                //set error image, memory policy
                .error(R.drawable.churchnews)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                //resize and crop
               .fit()
                //load image to imageview
                .into(imageViewOnline);
    }

    public String getContentTitle(int pos){
        return list.get(pos).getTitle();
    }

    public String getContentId(int pos){
        return list.get(pos).getContentIds();
    }



}
