package com.example.spokenwapp.church;

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

public class ChurchPageFragment extends Fragment {

    private ChurchPageViewModel churchPageViewModel;
    public static final String ARG_CHURCH = "Church";
    private View root;

    public static ChurchPageFragment newInstance() {
        return new ChurchPageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.church_page_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        churchPageViewModel = ViewModelProviders.of(this).get(ChurchPageViewModel.class);
        final TextView textView = root.findViewById(R.id.church);
        churchPageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
    }

}
