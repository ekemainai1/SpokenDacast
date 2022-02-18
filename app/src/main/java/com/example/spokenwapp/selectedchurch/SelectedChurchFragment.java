package com.example.spokenwapp.selectedchurch;

import androidx.lifecycle.ViewModelProvider;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.DacastAudioSermonAdapter;
import com.example.spokenwapp.adapters.DacastSpecialsAdapter;
import com.example.spokenwapp.adapters.DacastVODOnlineAdapter;
import com.example.spokenwapp.adapters.DividerItemDecorator;
import com.example.spokenwapp.adapters.OnItemTouchListener;
import com.example.spokenwapp.adapters.RecyclerViewOnItemTouchListener;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.base.SpokenMainScreen;
import com.example.spokenwapp.base.ViewModelFactory;
import com.example.spokenwapp.data.model.DacastAudioSermonEntity;
import com.example.spokenwapp.data.model.DacastSpecialSermonEntity;
import com.example.spokenwapp.data.model.VodDacastLogEntity;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;
import com.example.spokenwapp.utilities.SpokenSharedPreferences;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Formatter;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class SelectedChurchFragment extends DaggerFragment {

    View root;
    @Inject
    ViewModelFactory viewModelFactory;
    ApplicationComponent applicationComponent;
    SelectedChurchViewModel selectedChurchViewModel;
    ImageView selectedChurchPage;
    RecyclerView recyclerViewOnlineSermon;
    RecyclerView recyclerViewAudioSermon;
    RecyclerView recyclerViewSpecials;
    ProgressBar progressBar;
    List<VodDacastLogEntity> list;
    List<DacastSpecialSermonEntity> dacastSpecialSermonEntityList;
    List<DacastAudioSermonEntity> dacastAudioSermonEntityList;

    DacastVODOnlineAdapter dacastVODOnlineAdapter;
    DacastAudioSermonAdapter dacastAudioSermonAdapter;
    DacastSpecialsAdapter dacastSpecialsAdapter;
    SpokenSharedPreferences spokenSharedPreferences;
    ImageView imageViewChurch;
    TextView textViewLive;
    TextView textViewViewers;
    TextView textViewTimer;
    CircleImageView circleImageViewLive;
    TextView textViewAbout;
    TextView textViewEvents;
    TextView textViewShare;

    public static SelectedChurchFragment newInstance() {
        return new SelectedChurchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.selected_church_fragment, container, false);
        return root;
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
        selectedChurchViewModel = new ViewModelProvider(this, viewModelFactory).get(SelectedChurchViewModel.class);

        // Call layout components
        recyclerViewOnlineSermon = root.findViewById(R.id.recyclerViewOnlineSermon);
        recyclerViewAudioSermon = root.findViewById(R.id.recyclerViewAudioSermon);
        recyclerViewSpecials = root.findViewById(R.id.recyclerViewSpecial);
        progressBar = root.findViewById(R.id.progressBarVod);
        imageViewChurch = root.findViewById(R.id.imageViewChurch);
        textViewLive = root.findViewById(R.id.textViewLiveSelect);
        textViewViewers = root.findViewById(R.id.textViewViewers);
        textViewTimer = root.findViewById(R.id.textViewTimer);
        circleImageViewLive = root.findViewById(R.id.live_icon_select);
        textViewAbout = root.findViewById(R.id.textViewAbout);
        textViewEvents = root.findViewById(R.id.textViewEvents);
        textViewShare = root.findViewById(R.id.textViewShare);

        if(isAdded()) {
            ((SpokenMainScreen) requireActivity()).changePlayerInvisibility();
        }

        list = getChurchVodList();
        dacastSpecialSermonEntityList = getChurchSpecialsList();
        dacastAudioSermonEntityList = getChurchAudioList();

        dacastVODOnlineAdapter = new DacastVODOnlineAdapter(selectedChurchViewModel, this,
                this.requireActivity());

       dacastAudioSermonAdapter = new DacastAudioSermonAdapter(selectedChurchViewModel, this,
                this.requireActivity());

       dacastSpecialsAdapter = new DacastSpecialsAdapter(selectedChurchViewModel, this,
                this.requireActivity());

        recyclerViewOnlineSermon.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.requireActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewOnlineSermon.setLayoutManager(manager);
        recyclerViewOnlineSermon.addItemDecoration(new DividerItemDecorator(1));
        recyclerViewOnlineSermon.setAdapter(dacastVODOnlineAdapter);

        recyclerViewAudioSermon.setHasFixedSize(true);
        RecyclerView.LayoutManager managerAudio = new LinearLayoutManager(this.requireActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAudioSermon.setLayoutManager(managerAudio);
        recyclerViewAudioSermon.addItemDecoration(new DividerItemDecorator(1));
        recyclerViewAudioSermon.setAdapter(dacastAudioSermonAdapter);

        recyclerViewSpecials.setHasFixedSize(true);
        RecyclerView.LayoutManager managerSpecials = new LinearLayoutManager(this.requireActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSpecials.setLayoutManager(managerSpecials);
        recyclerViewSpecials.addItemDecoration(new DividerItemDecorator(1));
        recyclerViewSpecials.setAdapter(dacastSpecialsAdapter);

        vodObservableViewModel();
        audioObservableViewModel();
        specialsObservableViewModel();

        spokenSharedPreferences = new SpokenSharedPreferences(requireActivity());

        recyclerViewOnlineSermon.addOnItemTouchListener(new RecyclerViewOnItemTouchListener(this
                .requireActivity().getApplicationContext(),
                recyclerViewOnlineSermon, new OnItemTouchListener() {
            @Override
            public void onClick(View view, int position) {

                String title = dacastVODOnlineAdapter.getContentTitle(position);
                spokenSharedPreferences.saveDacastContentTitle(title);
                spokenSharedPreferences.saveSysIPAddress(dacastVODOnlineAdapter.getContentId(position));
                Navigation.findNavController(view).navigate(R.id.spokenOnlineService);
                // Intent intent = new Intent(requireActivity(), FullscreenActivity.class);
                // startActivity(intent);
                ((SpokenMainScreen) requireActivity()).updateCurrentContentText();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


        recyclerViewAudioSermon.addOnItemTouchListener(new RecyclerViewOnItemTouchListener(this
                .requireActivity().getApplicationContext(),
                recyclerViewAudioSermon, new OnItemTouchListener() {
            @Override
            public void onClick(View view, int position) {

                String title = dacastAudioSermonAdapter.getContentTitle(position);
                spokenSharedPreferences.saveDacastContentTitle(title);
                spokenSharedPreferences.saveSysIPAddress(dacastAudioSermonAdapter.getContentId(position));
                Navigation.findNavController(view).navigate(R.id.spokenOnlineService);
                // Intent intent = new Intent(requireActivity(), FullscreenActivity.class);
                // startActivity(intent);
                ((SpokenMainScreen) requireActivity()).updateCurrentContentText();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        recyclerViewSpecials.addOnItemTouchListener(new RecyclerViewOnItemTouchListener(this
                .requireActivity().getApplicationContext(),
                recyclerViewSpecials, new OnItemTouchListener() {
            @Override
            public void onClick(View view, int position) {

                String title = dacastSpecialsAdapter.getContentTitle(position);
                spokenSharedPreferences.saveDacastContentTitle(title);
                spokenSharedPreferences.saveSysIPAddress(dacastSpecialsAdapter.getContentId(position));
                Navigation.findNavController(view).navigate(R.id.spokenOnlineService);
                // Intent intent = new Intent(requireActivity(), FullscreenActivity.class);
                // startActivity(intent);
                ((SpokenMainScreen) requireActivity()).updateCurrentContentText();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        loadChurchOnlineThumbnail(imageViewChurch);

        imageViewChurch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.spokenOnlineService);
                ((SpokenMainScreen) requireActivity()).updateCurrentContentText();
            }
        });

        // Change state of live button peep
        changeStateLiveButton();

        // Retrieve actionBar title and set church name
        ActionBar actionBar = requireActivity().getActionBar();
        if(actionBar != null) {
            actionBar.setTitle(spokenSharedPreferences.getDacastContentTitle());
        }
    }

    @Override
    public void onDestroy () {
        ((SpokenMainScreen) requireActivity()).changeToolbarTitle(requireActivity()
                .getResources().getString(R.string.app_name));
        super.onDestroy();
    }

    private void changeStateLiveButton() {
        if(spokenSharedPreferences.getChurchOnline()) {
            textViewLive.setText(R.string.service_on);
            circleImageViewLive.setColorFilter(Color.RED);
            //textViewViewers.setText(spokenSharedPreferences.+" views");
            //textViewTimer.setText(list.get(0).getAbitrate());
        }else {
            textViewLive.setText(R.string.service_offline);
            circleImageViewLive.setColorFilter(Color.GREEN);
            //textViewViewers.setVisibility(View.INVISIBLE);
        }

    }

    private void vodObservableViewModel() {
        selectedChurchViewModel.getVodRepos().observe(getViewLifecycleOwner(), repos -> {
            if(repos != null) {
                recyclerViewOnlineSermon.setVisibility(View.VISIBLE);
            }
        });

        selectedChurchViewModel.getVodError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                if(isError) {
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerViewSpecials.setVisibility(View.INVISIBLE);

                }
            }else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        selectedChurchViewModel.getVodLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
                if (isLoading) {
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerViewOnlineSermon.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void audioObservableViewModel() {
        selectedChurchViewModel.getAudioRepos().observe(getViewLifecycleOwner(), repos -> {
            if(repos != null) {
                recyclerViewAudioSermon.setVisibility(View.VISIBLE);
            }
        });

        selectedChurchViewModel.getAudioError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                if(isError) {
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerViewAudioSermon.setVisibility(View.INVISIBLE);

                }
            }else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        selectedChurchViewModel.getAudioLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
                if (isLoading) {
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerViewAudioSermon.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void specialsObservableViewModel() {
        selectedChurchViewModel.getSpecialsRepos().observe(getViewLifecycleOwner(), repos -> {
            if(repos != null) {
                recyclerViewSpecials.setVisibility(View.VISIBLE);
            }
        });

        selectedChurchViewModel.getSpecialsError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                if(isError) {
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerViewSpecials.setVisibility(View.INVISIBLE);

                }
            }else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        selectedChurchViewModel.getSpecialsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
                if (isLoading) {
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerViewSpecials.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private List<VodDacastLogEntity> getChurchVodList() {
        list = selectedChurchViewModel.getVodRepos().getValue();
        return list;
    }

    private List<DacastAudioSermonEntity> getChurchAudioList() {
        dacastAudioSermonEntityList = selectedChurchViewModel.getAudioRepos().getValue();
        return dacastAudioSermonEntityList;
    }

    private List<DacastSpecialSermonEntity> getChurchSpecialsList() {
        dacastSpecialSermonEntityList = selectedChurchViewModel.getSpecialsRepos().getValue();
        return dacastSpecialSermonEntityList;
    }

    public void loadChurchOnlineThumbnail(ImageView imageView){

        Picasso.get()
                .load(spokenSharedPreferences.getChurchOnlineThumbnail())
                .placeholder(R.drawable.dominicanchurch)
                .resize(300, 200)
                .centerCrop()
                .into(imageView);

    }


    public void printDifference(Date startDate, Date endDate){

        // Milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        //long elapsedDays = different / daysInMilli;
        //different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d hours, %d minutes, %d seconds%n",
                elapsedHours, elapsedMinutes, elapsedSeconds);
        //textViewTimer.setText(Math.toIntExact(elapsedHours + elapsedMinutes + elapsedSeconds));

    }

}
