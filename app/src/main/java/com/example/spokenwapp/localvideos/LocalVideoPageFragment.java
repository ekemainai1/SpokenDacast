package com.example.spokenwapp.localvideos;

import androidx.lifecycle.ViewModelProvider;
import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.LocalVideoListAdapter;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.base.ViewModelFactory;
import com.example.spokenwapp.data.model.LocalVideoEntity;
import com.example.spokenwapp.data.model.VideosList;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerFragment;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class LocalVideoPageFragment extends DaggerFragment {

    public static final String ARG_VIDEO = "Local Videos";
    private View rootView;

    ApplicationComponent applicationComponent;
    ViewModelFactory viewModelFactory;
    private RecyclerView recyclerViewVideos;


    CompositeDisposable compositeDisposable;
    LocalVideoListAdapter localVideoListAdapter;
    LocalVideoPageViewModel localVideoPageViewModel;


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

        localVideoPageViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(LocalVideoPageViewModel.class);
        recyclerViewVideos = rootView.findViewById(R.id.localVideoList);
        RecyclerView.LayoutManager videosLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerViewVideos.setLayoutManager(videosLayoutManager);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(localVideoPageViewModel.getVideos(this.getActivity().getApplication())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LocalVideoEntity>>() {
                    @Override
                    public void accept(List<LocalVideoEntity> localVideoEntities) throws Exception {
                        if(localVideoEntities != null) {
                            localVideoListAdapter = new LocalVideoListAdapter(localVideoEntities,this );
                            recyclerViewVideos.setAdapter(localVideoListAdapter);
                            Log.e("RetrieveVideos", ""+localVideoEntities.size());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("LocalVideoPageFragment", "exception getting videos");
                    }
                })
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        //dispose subscriptions
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }



}
