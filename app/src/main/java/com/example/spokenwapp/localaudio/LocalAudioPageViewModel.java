package com.example.spokenwapp.localaudio;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.data.repository.LocalVideoRepository;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.Flowable;

public class LocalAudioPageViewModel extends ViewModel {

    @Inject
    public LocalAudioPageViewModel() {
    }

    @Inject
    LocalVideoRepository localVideoRepository;

    public Flowable<List<LocalAudioEntity>> getAudios(Application application){
        return new LocalVideoRepository(application).allLocalAudio;
    }

    public long insertAudios(LocalAudioEntity localAudioEntity){
        return localVideoRepository.insertAllLocalAudios(localAudioEntity);
    }

    public void deleteAudios(){
        localVideoRepository.deleteAllAudio();
    }
}
