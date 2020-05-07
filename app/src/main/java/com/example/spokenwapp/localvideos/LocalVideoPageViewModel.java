package com.example.spokenwapp.localvideos;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import com.example.spokenwapp.data.model.LocalVideoEntity;
import com.example.spokenwapp.data.repository.LocalVideoRepository;
import java.util.List;
import javax.inject.Inject;

import io.reactivex.Flowable;


public class LocalVideoPageViewModel extends ViewModel {

    @Inject
    LocalVideoRepository localVideoRepository;

    public Flowable<List<LocalVideoEntity>> getVideos(Application application){
        return new LocalVideoRepository(application).allLocalVideos;
    }

    public long insertVideos(LocalVideoEntity localVideoEntity){
        return localVideoRepository.insertAllLocalVideos(localVideoEntity);
    }

    public void deleteVideos(){
        localVideoRepository.deleteAllVideos();
    }

}
