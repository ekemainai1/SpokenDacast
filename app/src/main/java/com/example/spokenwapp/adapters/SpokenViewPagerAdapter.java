package com.example.spokenwapp.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.spokenwapp.church.ChurchPageFragment;
import com.example.spokenwapp.localaudio.LocalAudioPageFragment;
import com.example.spokenwapp.localvideos.LocalVideoPageFragment;

public class SpokenViewPagerAdapter extends FragmentStateAdapter {
    private Fragment fragment = new Fragment();
    public SpokenViewPagerAdapter(@NonNull Fragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle args = new Bundle();
        // Return a NEW fragment instance in createFragment(int)
        if(position == 0) {
            fragment = ChurchPageFragment.newInstance();
            args.putInt(ChurchPageFragment.ARG_CHURCH, position + 1);
            fragment.setArguments(args);
        }else if(position == 1){
            fragment = LocalAudioPageFragment.newInstance();
            args.putInt(LocalAudioPageFragment.ARG_AUDIO, position + 2);
            fragment.setArguments(args);
        }else if(position == 2){
            fragment = LocalVideoPageFragment.newInstance();
            args.putInt(LocalVideoPageFragment.ARG_VIDEO, position + 3);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
