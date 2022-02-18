package com.example.spokenwapp.ui.scan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.spokenwapp.R;

import dagger.android.support.DaggerFragment;

public class ScanAppFragment extends DaggerFragment {

    private ScanAppViewModel scanAppViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scanAppViewModel =
                ViewModelProviders.of(this).get(ScanAppViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scanapp, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        scanAppViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
