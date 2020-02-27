package com.example.spokenwapp.ui.pager;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.SpokenViewPagerAdapter;
import com.example.spokenwapp.church.ChurchPageFragment;
import com.example.spokenwapp.localaudio.LocalAudioPageFragment;
import com.example.spokenwapp.localvideos.LocalVideoPageFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpokenViewPager#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpokenViewPager extends Fragment {

    private View root;

    public SpokenViewPager() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SpokenViewPager.
     */
    // TODO: Rename and change types and number of parameters
    public static SpokenViewPager newInstance() {
        return new SpokenViewPager();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_spoken_view_pager, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ViewPager2 viewPager2Spoken = root.findViewById(R.id.pager);
        TabLayout tabLayoutSpoken = root.findViewById(R.id.tab_layout);
        SpokenViewPagerAdapter spokenViewPagerAdapter = new SpokenViewPagerAdapter(SpokenViewPager.this);
        viewPager2Spoken.setAdapter(spokenViewPagerAdapter);

        new TabLayoutMediator(tabLayoutSpoken, viewPager2Spoken,
                (tab, position) -> {
                    if(position == 0){
                        tab.setText(ChurchPageFragment.ARG_CHURCH);
                    }else if(position == 1){
                        tab.setText(LocalAudioPageFragment.ARG_AUDIO);
                    }else if(position == 2){
                        tab.setText(LocalVideoPageFragment.ARG_VIDEO);
                    }
                }).attach();
    }
}
