package com.example.spokenwapp.church;


import androidx.lifecycle.ViewModelProvider;
import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.ChurchListAdapter;
import com.example.spokenwapp.adapters.DividerItemDecorator;
import com.example.spokenwapp.adapters.OnItemTouchListener;
import com.example.spokenwapp.adapters.RecyclerViewOnItemTouchListener;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.base.SpokenMainScreen;
import com.example.spokenwapp.base.ViewModelFactory;
import com.example.spokenwapp.data.model.DacastChannelAnalyticsEntity;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;
import com.example.spokenwapp.utilities.SpokenSharedPreferences;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.android.support.DaggerFragment;


public class ChurchPageFragment extends DaggerFragment {

    public static final String ARG_CHURCH = "Church";
    private View root;
    RecyclerView churchGridView;
    List<DacastChannelAnalyticsEntity> list;
    ApplicationComponent applicationComponent;
    @Inject
    ViewModelFactory viewModelFactory;
    ChurchPageViewModel churchPageViewModel;
    TextView errorTextView;
    ProgressBar progressBar;
    SpokenSharedPreferences spokenSharedPreferences;
    ChurchListAdapter churchListAdapter;
    TextView toolbarTitle;


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
        applicationComponent = DaggerApplicationComponent.builder()
                .application(new SpokenBaseApplication())
                .applicationModule(new ApplicationModule())
                .localVideoRepoModule(new LocalVideoRepoModule())
                .build();
        applicationComponent.inject(this);

        churchPageViewModel = new ViewModelProvider(this, viewModelFactory).get(ChurchPageViewModel.class);

        churchGridView = root.findViewById(R.id.churchGridview);
        errorTextView = root.findViewById(R.id.churchErrorText);
        progressBar = root.findViewById(R.id.progressBar2);
        list = new ArrayList<DacastChannelAnalyticsEntity>();

        churchListAdapter = new ChurchListAdapter(churchPageViewModel,this,
                this.requireActivity());

        list = getChurchList();
        churchGridView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this.requireActivity());
        churchGridView.setLayoutManager(manager);
        churchGridView.addItemDecoration(new DividerItemDecorator(1));
        churchGridView.setAdapter(churchListAdapter);

        spokenSharedPreferences = new SpokenSharedPreferences(requireActivity());


        churchGridView.addOnItemTouchListener(new RecyclerViewOnItemTouchListener(this
                .requireActivity().getApplicationContext(),
                churchGridView, new OnItemTouchListener() {
            @Override
            public void onClick(View view, int position) {
                spokenSharedPreferences.saveSysIPAddress(churchListAdapter.getContentId(position));
                spokenSharedPreferences.saveDacastContentTitle(churchListAdapter.getContentTitle(position));
                spokenSharedPreferences.saveChurchOnlineThumbnail(churchListAdapter
                        .getChurchThumbnail(position));
                spokenSharedPreferences.saveChurchOnline(churchListAdapter.getIsOnline(position));
                Navigation.findNavController(view).navigate(R.id.selectedChurchFragment);
                //Intent intent = new Intent(requireActivity(), FullscreenActivity.class);
                //Intent is used to switch from one activity to another.
                ((SpokenMainScreen)requireActivity()).changeToolbarTitle(churchListAdapter
                        .getContentTitle(position));

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        observableViewModel();

        if(isAdded()) {
            ((SpokenMainScreen) requireActivity()).changePlayerInvisibility();
        }

    }


    private List<DacastChannelAnalyticsEntity> getChurchList() {
        list = churchPageViewModel.getRepos().getValue();
        return list;
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);

    }

    private void observableViewModel() {
        churchPageViewModel.getRepos().observe(getViewLifecycleOwner(), repos -> {
            if(repos != null) churchGridView.setVisibility(View.VISIBLE);
        });

        churchPageViewModel.getError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                if(isError) {
                    errorTextView.setVisibility(View.VISIBLE);
                    churchGridView.setVisibility(View.INVISIBLE);
                    errorTextView.setText(R.string.errorChurchLoad);
                }
            }else {
                errorTextView.setVisibility(View.INVISIBLE);
                errorTextView.setText(null);
            }
        });

        churchPageViewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
                if (isLoading) {
                    errorTextView.setVisibility(View.INVISIBLE);
                    churchGridView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }



}
