package com.example.spokenwapp.church;

import androidx.lifecycle.ViewModelProviders;
import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.ChurchListAdapter;
import com.example.spokenwapp.adapters.DividerItemDecorator;
import com.example.spokenwapp.adapters.OnItemTouchListener;
import com.example.spokenwapp.adapters.RecyclerViewOnItemTouchListener;
import com.example.spokenwapp.base.SpokenBaseApplication;
import com.example.spokenwapp.base.ViewModelFactory;
import com.example.spokenwapp.data.model.Church;
import com.example.spokenwapp.di.components.ApplicationComponent;
import com.example.spokenwapp.di.components.DaggerApplicationComponent;
import com.example.spokenwapp.di.modules.ApplicationModule;
import com.example.spokenwapp.di.modules.LocalVideoRepoModule;
import java.util.ArrayList;
import javax.inject.Inject;
import dagger.android.support.DaggerFragment;


public class ChurchPageFragment extends DaggerFragment {

    private ChurchPageViewModel churchPageViewModel;
    public static final String ARG_CHURCH = "Church";
    private View root;
    RecyclerView churchGridView;
    private RecyclerView.LayoutManager manager;
    Church church;
    @Inject
    ArrayList<Church> list;
    ApplicationComponent applicationComponent;
    @Inject
    ViewModelFactory viewModelFactory;


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

        churchPageViewModel = ViewModelProviders.of(this, viewModelFactory).get(ChurchPageViewModel.class);

        loadAvailableChurches();
        churchGridView.addOnItemTouchListener(new RecyclerViewOnItemTouchListener( this
                .getActivity().getApplicationContext(),
               churchGridView, new OnItemTouchListener() {
            @Override
            public void onClick(View view, int position) {
                Navigation.findNavController(view).navigate(R.id.spokenChooseURLFragment);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

    }

    private void loadAvailableChurches() {
        churchGridView = root.findViewById(R.id.churchGridview);
        list = getChurchList();
        churchGridView.setHasFixedSize(true);
        manager = new GridLayoutManager(getActivity(), 2);
        churchGridView.setLayoutManager(manager);
        churchGridView.addItemDecoration(new DividerItemDecorator(2));
        churchGridView.setAdapter(new ChurchListAdapter(list,getContext()));
    }

    private ArrayList<Church> getChurchList(){
        int[] logos = {R.drawable.church,
                R.drawable.churchofgod,
                R.drawable.smalltownchurch,
                R.drawable.dominicanchurch,
                R.drawable.unnamed,
                R.drawable.usachurch,
                R.drawable.images,
                R.drawable.indianchurch,
                R.drawable.meckarchitekten,
                R.drawable.gettyimages};
        String[] churchNameList = {
                "Spoken Word",
                "Beaulah",
                "Branham",
                "Golden Grain",
                "Original Seed",
                "Eternal Home",
                "Abraham Seed",
                "Gideon 300",
                "Beautiful Gate",
                "Jasper Tabernacle"
        };

        for(int i=0; i<churchNameList.length; i++ ){
            church = new Church(logos[i],churchNameList[i]);
            list.add(church);
        }
        Log.e("Size", ""+list.size());
        return list;
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);

    }

}
