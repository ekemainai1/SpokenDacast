package com.example.spokenwapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.spokenwapp.R;
import com.example.spokenwapp.data.model.Church;

import java.util.ArrayList;

import javax.inject.Inject;

public class ChurchListAdapter extends RecyclerView.Adapter<ChurchListAdapter.ChurchViewHolder> {

    @Inject
    ArrayList<Church> list;
    @Inject
    Context context;


    public ChurchListAdapter(ArrayList<Church> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ChurchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.church_list, parent, false);
        return new ChurchViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ChurchViewHolder holder, int position) {

        Church church = list.get(position);
        holder.textView.setText(church.getChurchName());
        holder.imageView.setImageResource(church.getChurchLogo());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ChurchViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        public ChurchViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.churchName);
            imageView = itemView.findViewById(R.id.churchLogo);
        }
    }

}
