package com.example.spokenwapp.localaudio;

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

public class LocalAudioPageFragment extends Fragment {

    private LocalAudioPageViewModel localAudioPageViewModel;
    public static final String ARG_AUDIO = "Local Audio";
    private View root;

    public static LocalAudioPageFragment newInstance() {
        return new LocalAudioPageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.local_audio_page_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        localAudioPageViewModel = ViewModelProviders.of(this).get(LocalAudioPageViewModel.class);
        final TextView textView = root.findViewById(R.id.localaudio);
        localAudioPageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
    }

}
