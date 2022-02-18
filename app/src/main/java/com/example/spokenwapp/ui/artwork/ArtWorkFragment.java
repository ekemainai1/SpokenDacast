package com.example.spokenwapp.ui.artwork;

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

public class ArtWorkFragment extends DaggerFragment {

    private ArtWorkViewModel artWorkViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        artWorkViewModel =
                ViewModelProviders.of(this).get(ArtWorkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_artwork, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        artWorkViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
