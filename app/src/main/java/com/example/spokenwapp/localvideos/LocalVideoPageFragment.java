package com.example.spokenwapp.localvideos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spokenwapp.R;

public class LocalVideoPageFragment extends Fragment {

    private LocalVideoPageViewModel localVideoPageViewModel;
    public static final String ARG_VIDEO = "Local Videos";
    private View root;

    public static LocalVideoPageFragment newInstance() {
        return new LocalVideoPageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.local_video_page_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        localVideoPageViewModel = ViewModelProviders.of(this).get(LocalVideoPageViewModel.class);
        final TextView textView = root.findViewById(R.id.localvideo);
        localVideoPageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
    }

}
