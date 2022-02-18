package com.example.spokenwapp.search;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.DividerItemDecorator;
import com.example.spokenwapp.adapters.LocalAudioListSearchAdapter;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.base.ViewModelFactory;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.support.DaggerFragment;
import io.reactivex.disposables.CompositeDisposable;

public class SpokenLocalAudioSearchFragment extends DaggerFragment implements
        LocalAudioListSearchAdapter.LocalAudioListSearchAdapterListener {

    View view;
    SpokenLocalAudioSearchViewModel spokenLocalAudioSearchViewModel;
    @Inject
    ViewModelFactory viewModelFactory;
    private LocalAudioListSearchAdapter listSearchAdapter;
    private List<LocalAudioEntity> localAudioEntities;
    RecyclerView recyclerViewLocalAudioSearch;
    EditText editTextLocalAudioSearch;

    public static SpokenLocalAudioSearchFragment newInstance() {
        return new SpokenLocalAudioSearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.spoken_local_audio_search_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        spokenLocalAudioSearchViewModel = new ViewModelProvider(this, viewModelFactory)
                .get(SpokenLocalAudioSearchViewModel.class);

        // Call layout items by ids
        editTextLocalAudioSearch = view.findViewById(R.id.input_search);
        recyclerViewLocalAudioSearch = view.findViewById(R.id.localAudioList_Search);


        // Create data objects containers
        localAudioEntities = new ArrayList<>();
        // Create adapter for the search
        listSearchAdapter = new LocalAudioListSearchAdapter(requireActivity(), localAudioEntities,
                this);
        // Fetch data
        spokenLocalAudioSearchViewModel.getAudioListForSearch(listSearchAdapter, localAudioEntities);

        // Call search logic from ViewModel
        spokenLocalAudioSearchViewModel.executeAudioListSearch(editTextLocalAudioSearch,
                listSearchAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        recyclerViewLocalAudioSearch.setLayoutManager(layoutManager);
        recyclerViewLocalAudioSearch.setItemAnimator(new DefaultItemAnimator());

        int[] ATTRS = new int[]{android.R.attr.listDivider};
        TypedArray a = requireActivity().obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        int inset = getResources().getDimensionPixelSize(R.dimen.rec_margin);
        InsetDrawable insetDivider = new InsetDrawable(divider, inset, 0, inset, 0);
        a.recycle();
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(),
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(insetDivider);
        recyclerViewLocalAudioSearch.addItemDecoration(itemDecoration);
        recyclerViewLocalAudioSearch.setAdapter(listSearchAdapter);

    }

    @Override
    public void onLocalAudioListSearchAdapterListener(LocalAudioEntity localAudioEntity) {

    }

    public void whiteNotificationBar(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            requireActivity().getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(@NonNull Activity activity) {
        AndroidInjection.inject(activity);
        super.onAttach(activity);

    }
}