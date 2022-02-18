package com.example.spokenwapp.ui.equalizer;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.spokenwapp.R;
import com.example.spokenwapp.base.SpokenMainScreen;
import com.example.spokenwapp.base.ViewModelFactory;
import com.google.gson.Gson;
import javax.inject.Inject;
import dagger.android.support.DaggerFragment;
import static com.example.spokenwapp.services.SpokenMediaBrowserService.sessionId;
public class SpokenEqualizerFragment extends DaggerFragment {

    private SpokenEqualizerViewModel spokenEqualizerViewModel;
    @Inject
    ViewModelFactory viewModelFactory;
    View root;

    public static SpokenEqualizerFragment newInstance() {
        return new SpokenEqualizerFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.spoken_fragment_equalizer, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spokenEqualizerViewModel = new ViewModelProvider(this, viewModelFactory).get(SpokenEqualizerViewModel.class);

        loadEqualizerSettings();

        EqualizerFragment equalizerFragment = EqualizerFragment.newBuilder()
                .setAccentColor(Color.parseColor("#4caf50"))
                .setAudioSessionId(sessionId)
                .build();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.eqFrame, equalizerFragment);
        transaction.commit();

    }


    @Override
    public void onStop() {
        super.onStop();

        saveEqualizerSettings();
    }

    private void saveEqualizerSettings(){
        if (Settings.equalizerModel != null){
            EqualizerSettings settings = new EqualizerSettings();
            settings.bassStrength = Settings.equalizerModel.getBassStrength();
            settings.presetPos = Settings.equalizerModel.getPresetPos();
            settings.reverbPreset = Settings.equalizerModel.getReverbPreset();
            settings.seekbarpos = Settings.equalizerModel.getSeekbarpos();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());

            Gson gson = new Gson();
            preferences.edit()
                    .putString(PREF_KEY, gson.toJson(settings))
                    .apply();
        }
    }

    private void loadEqualizerSettings(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());

        Gson gson = new Gson();
        EqualizerSettings settings = gson.fromJson(preferences.getString(PREF_KEY, "{}"), EqualizerSettings.class);
        EqualizerModel model = new EqualizerModel();
        model.setBassStrength(settings.bassStrength);
        model.setPresetPos(settings.presetPos);
        model.setReverbPreset(settings.reverbPreset);
        model.setSeekbarpos(settings.seekbarpos);

        Settings.isEqualizerEnabled = true;
        Settings.isEqualizerReloaded = true;
        Settings.bassStrength = settings.bassStrength;
        Settings.presetPos = settings.presetPos;
        Settings.reverbPreset = settings.reverbPreset;
        Settings.seekbarpos = settings.seekbarpos;
        Settings.equalizerModel = model;
    }

    public static final String PREF_KEY = "equalizer";
}
