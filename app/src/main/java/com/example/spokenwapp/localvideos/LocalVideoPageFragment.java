package com.example.spokenwapp.localvideos;

import androidx.lifecycle.ViewModelProvider;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.LocalVideoListAdapter;
import com.example.spokenwapp.adapters.OnItemTouchListener;
import com.example.spokenwapp.adapters.RecyclerViewOnItemTouchListener;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.base.SpokenMainScreen;
import com.example.spokenwapp.base.ViewModelFactory;
import com.example.spokenwapp.data.model.LocalVideoEntity;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;
import com.example.spokenwapp.players.SpokenOnlinePlayerFragment;
import com.example.spokenwapp.utilities.SpokenSharedPreferences;

import java.util.List;
import javax.inject.Inject;
import dagger.android.support.DaggerFragment;


public class LocalVideoPageFragment extends DaggerFragment {

    public static final String ARG_VIDEO = "Local Videos";
    private View rootView;

    ApplicationComponent applicationComponent;
    @Inject
    ViewModelFactory viewModelFactory;
    private RecyclerView recyclerViewVideos;
    List<LocalVideoEntity> localVideoList;
    LocalVideoListAdapter localVideoListAdapter;
    LocalVideoPageViewModel localVideoPageViewModel;
    SpokenSharedPreferences spokenSharedPreferences;

    public static LocalVideoPageFragment newInstance() {
        return new LocalVideoPageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.local_video_page_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        applicationComponent = DaggerApplicationComponent.builder()
                .application(new SpokenBaseApplication())
                .applicationModule(new ApplicationModule())
                .localVideoRepoModule(new LocalVideoRepoModule())
                .build();
        applicationComponent.inject(this);

        localVideoPageViewModel = new ViewModelProvider(this, viewModelFactory).get(LocalVideoPageViewModel.class);
        localVideoListAdapter = new LocalVideoListAdapter(localVideoPageViewModel,this,
                this.requireActivity());

        ((SpokenMainScreen)requireActivity()).changePlayerInvisibility();

        localVideoList = getLocalVideoList();
        spokenSharedPreferences = new SpokenSharedPreferences(requireActivity());

        recyclerViewVideos = rootView.findViewById(R.id.localVideoList);
        recyclerViewVideos.setHasFixedSize(true);
        RecyclerView.LayoutManager videosLayoutManager = new LinearLayoutManager(requireActivity());
        recyclerViewVideos.setLayoutManager(videosLayoutManager);
        recyclerViewVideos.setAdapter(localVideoListAdapter);

        observableLocalVideoViewModel();

        recyclerViewVideos.addOnItemTouchListener(new RecyclerViewOnItemTouchListener(requireActivity(),
                recyclerViewVideos, new OnItemTouchListener() {
            @Override
            public void onClick(View view, int position) {
                Navigation.findNavController(view).navigate(R.id.spokenOnlinePlayerFragment);
                spokenSharedPreferences.saveMediaData(localVideoListAdapter.getId(position),
                        localVideoListAdapter.getVideoPath(position), localVideoListAdapter.getTitle(position));

                spokenSharedPreferences.saveTheoPlayerState("video");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private List<LocalVideoEntity> getLocalVideoList() {
        localVideoList = localVideoPageViewModel.getLocalVideoRepos().getValue();
        return localVideoList;
    }

    private void observableLocalVideoViewModel() {
        localVideoPageViewModel.getLocalVideoRepos().observe(getViewLifecycleOwner(), repos -> {
            if(repos != null) recyclerViewVideos.setVisibility(View.VISIBLE);
            Toast.makeText(requireActivity(), "Videos Loaded",Toast.LENGTH_LONG).show();
        });

        localVideoPageViewModel.getLocalVideoError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                if(isError) {
                    recyclerViewVideos.setVisibility(View.INVISIBLE);
                    Toast.makeText(requireActivity(), "Error Loading Videos ...",Toast.LENGTH_LONG).show();
                }
            }else {
                recyclerViewVideos.setVisibility(View.INVISIBLE);
            }
        });

        localVideoPageViewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                Toast.makeText(requireActivity(), "Videos Loading ...",Toast.LENGTH_LONG).show();
                if (isLoading) {
                    recyclerViewVideos.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

    }



}
