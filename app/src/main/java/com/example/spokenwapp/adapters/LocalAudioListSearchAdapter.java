package com.example.spokenwapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.spokenwapp.R;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import de.hdodenhof.circleimageview.CircleImageView;


public class LocalAudioListSearchAdapter extends RecyclerView.Adapter<LocalAudioListSearchAdapter
        .LocalAudioListSearchViewHolder> implements Filterable {

        @Inject
        Context context;
        private List<LocalAudioEntity> audioEntities;
        private List<LocalAudioEntity> audioEntitiesFiltered;
        private LocalAudioListSearchAdapterListener listSearchAdapterListener;

        public LocalAudioListSearchAdapter(Context context,  List<LocalAudioEntity> audioEntities,
                                           LocalAudioListSearchAdapterListener listSearchAdapterListener){
            this.context = context;
            this.listSearchAdapterListener = listSearchAdapterListener;
            this.audioEntities = audioEntities;
            this.audioEntitiesFiltered = audioEntities;
        }

        @NonNull
        @Override
        public LocalAudioListSearchAdapter.LocalAudioListSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_audio_search_list,
                    parent, false);
            return new LocalAudioListSearchAdapter.LocalAudioListSearchViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LocalAudioListSearchAdapter.LocalAudioListSearchViewHolder holder,
                                     int position) {
            final LocalAudioEntity localAudioEntity = audioEntitiesFiltered.get(position);
            Bitmap bitmap;
            Glide
                    .with(context)
                    .load(localAudioEntity.getLocalAudioFilePath())
                    .centerCrop()
                    .placeholder(R.drawable.branham)
                    .into(holder.audioThumbnail);

            holder.audioTitle.setText(localAudioEntity.getLocalAudioTitle());
            holder.audioArtist.setText(localAudioEntity.getLocalAudioArtist());
            holder.audioFileSize.setText(localAudioEntity.getLocalAudioSize());
            holder.audioTime.setText(localAudioEntity.getLocalAudioDuration());
            holder.audioOptions.setImageResource(R.drawable.ic_baseline_menu_open_24);
        }

   @Override
   public long getItemId(int position) {
            return audioEntitiesFiltered.get(position).getId();
        }

   @Override
   public int getItemCount() {
            return audioEntitiesFiltered.size();
        }

    @Override
    public Filter getFilter() {
        return new Filter(){

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                String charString = charSequence.toString();
                if(charString.isEmpty() || charString.length() == 0){
                    filterResults.count = audioEntities.size();
                    filterResults.values = audioEntities;
                    audioEntitiesFiltered = audioEntities;
                }else {
                    List<LocalAudioEntity> filteredList = new ArrayList<>();
                    for(LocalAudioEntity row : audioEntities){
                        if(!charString.isEmpty() && charString.length() >= 1) {
                            if (row.getLocalAudioTitle().toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getLocalAudioArtist().contains(charString)) {
                                filteredList.add(row);
                            }
                        }
                    }
                    audioEntitiesFiltered = filteredList;

                }

                filterResults.count = audioEntitiesFiltered.size();
                filterResults.values = audioEntitiesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                audioEntitiesFiltered = (ArrayList<LocalAudioEntity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class LocalAudioListSearchViewHolder extends RecyclerView.ViewHolder {

            private CircleImageView audioThumbnail;
            private TextView audioTitle;
            private TextView audioArtist;
            private  TextView audioFileSize;
            private TextView audioTime;
            private ImageView audioOptions;


            LocalAudioListSearchViewHolder(@NonNull View itemView) {
                super(itemView);

                audioThumbnail = itemView.findViewById(R.id.audioThumbnail_search);
                audioTitle = itemView.findViewById(R.id.audioTitle_search);
                audioArtist = itemView.findViewById(R.id.audioArtist_search);
                audioFileSize = itemView.findViewById(R.id.audioSize_search);
                audioTime = itemView.findViewById(R.id.audioTime_search);
                audioOptions = itemView.findViewById(R.id.audioChoice_search);
            }
        }

        public interface LocalAudioListSearchAdapterListener{
            void onLocalAudioListSearchAdapterListener(LocalAudioEntity localAudioEntity);
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

}
