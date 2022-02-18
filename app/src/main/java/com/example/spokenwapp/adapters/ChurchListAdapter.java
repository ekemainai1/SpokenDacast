package com.example.spokenwapp.adapters;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.example.spokenwapp.church.ChurchPageViewModel;
import com.example.spokenwapp.data.model.DacastChannelAnalyticsEntity;
import com.example.spokenwapp.data.model.DacastLogEntity;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.StatsSnapshot;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.ACTIVITY_SERVICE;

public class ChurchListAdapter extends RecyclerView.Adapter<ChurchListAdapter.ChurchViewHolder> {


    List<DacastChannelAnalyticsEntity> list = new ArrayList<>();;
    @Inject
    Context context;


    public ChurchListAdapter(ChurchPageViewModel viewModel, LifecycleOwner lifecycleOwner,
                             Context context) {
        this.context = context;
        viewModel.getRepos().observe(lifecycleOwner, repos -> {
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
    public ChurchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.church_list, parent, false);
        return new ChurchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChurchViewHolder holder, int position) {

        DacastChannelAnalyticsEntity church = list.get(position);
        Log.e("MVVMO", church.getThumbnail().get(0));
        holder.textView.setText(context.getResources().getString(R.string.contact));
        holder.churchDescription.setText(church.getDescription());
        loadImage(holder.imageView, church.getThumbnail().get(0));
        holder.churchLiveName.setText(church.getTitle());
        holder.churchViewers.setText(String.format(Locale.ENGLISH, "%d views", church.getVisitors()));

        if(church.getOnline()) {
            holder.churchLive.setText(R.string.service_on);
            holder.imageViewLive.setColorFilter(Color.RED);
            holder.churchViewers.setVisibility(View.VISIBLE);
        }else {
            holder.churchLive.setText(R.string.service_offline);
            holder.imageViewLive.setColorFilter(Color.GREEN);
            holder.churchViewers.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ChurchViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView churchDescription;
        private ImageView imageView;
        private TextView churchLiveName;
        private TextView churchViewers;
        private TextView churchLive;
        private CircleImageView imageViewLive;

        public ChurchViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.churchName);
            imageView = itemView.findViewById(R.id.churchLogo);
            churchDescription = itemView.findViewById(R.id.churchDescription);
            churchLiveName = itemView.findViewById(R.id.textViewChurch);
            churchViewers = itemView.findViewById(R.id.textViewViewers);
            churchLive = itemView.findViewById(R.id.textViewLive);
            imageViewLive = itemView.findViewById(R.id.live_icon);
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

    private void loadImage(ImageView imageView, String productImage){
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

    public String getContentTitle(int pos){
        return list.get(pos).getTitle();
    }

    public String getContentId(int pos){
        return list.get(pos).getContentId();
    }

    public String getChurchThumbnail(int pos){
        return list.get(pos).getThumbnail().get(0);
    }

    public boolean getIsOnline(int pos){
        return list.get(pos).getOnline();
    }



}
