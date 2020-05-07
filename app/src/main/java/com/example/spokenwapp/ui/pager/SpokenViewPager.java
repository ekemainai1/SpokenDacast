package com.example.spokenwapp.ui.pager;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.SpokenViewPagerAdapter;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.base.ViewModelFactory;
import com.example.spokenwapp.church.ChurchPageFragment;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.localaudio.LocalAudioPageFragment;
import com.example.spokenwapp.localvideos.LocalVideoPageFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import javax.inject.Inject;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;


public class SpokenViewPager extends DaggerFragment {

    ViewPager2 viewPager2Spoken;
    TabLayout tabLayoutSpoken;
    private View root;


    SpokenViewPagerAdapter spokenViewPagerAdapter;
    SpokenViewPagerViewModel spokenViewPagerViewModel;
    ApplicationComponent applicationComponent;


    public SpokenViewPager() {
        // Required empty public constructor
    }

    @Inject
    ViewModelFactory viewModelFactory;

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

        applicationComponent = DaggerApplicationComponent.builder()
                .application(new SpokenBaseApplication())
                .applicationModule(new ApplicationModule())
                .build();
        setRetainInstance(true);
        viewPager2Spoken = root.findViewById(R.id.pager);
        tabLayoutSpoken = root.findViewById(R.id.tab_layout);
        spokenViewPagerViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(SpokenViewPagerViewModel.class);


        //spokenViewPagerViewModel = new ViewModelProvider(requireActivity()).get(SpokenViewPagerViewModel.class);

        spokenViewPagerAdapter = new SpokenViewPagerAdapter(this);
        viewPager2Spoken.setAdapter(spokenViewPagerAdapter);

        new TabLayoutMediator(tabLayoutSpoken,viewPager2Spoken, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                if(position == 0){
                    spokenViewPagerAdapter.createFragment(position);
                    tab.setText(ChurchPageFragment.ARG_CHURCH);
                }else if(position == 1){
                    spokenViewPagerAdapter.createFragment(position);
                    tab.setText(LocalAudioPageFragment.ARG_AUDIO);
                }else if(position == 2){
                    spokenViewPagerAdapter.createFragment(position);
                    tab.setText(LocalVideoPageFragment.ARG_VIDEO);
                }
            }
        }).attach();

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);

    }



}
