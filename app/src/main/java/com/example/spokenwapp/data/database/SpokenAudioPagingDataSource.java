package com.example.spokenwapp.data.database;

import android.support.v4.media.MediaBrowserCompat;
import androidx.annotation.NonNull;
import androidx.paging.PagingState;
import androidx.paging.rxjava2.RxPagingSource;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.data.repository.LocalVideoRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


public class SpokenAudioPagingDataSource extends RxPagingSource<Integer, LocalAudioEntity> {

    private MediaBrowserCompat mediaBrowserCompat;
    LocalVideoRepository localVideoRepository;
    private String rootId;
    private Set<Integer> loadedPages = new HashSet<>();

    public SpokenAudioPagingDataSource(LocalVideoRepository repository) {
        localVideoRepository = repository;
    }

    @NotNull
    @Override
    public Single<LoadResult<Integer, LocalAudioEntity>> loadSingle(@NotNull LoadParams<Integer> loadParams) {
        // Start refresh at page 1 if undefined.
        Integer nextPageNumber = loadParams.getKey();
        if (nextPageNumber == null) {
            nextPageNumber = 1;
        }

        return localVideoRepository.getAllLocalAudioListCat()
                .subscribeOn(Schedulers.io())
                .map(this::toLoadResult)
                .onErrorReturn(LoadResult.Error::new);
    }

    private LoadResult<Integer, LocalAudioEntity> toLoadResult(@NonNull LocalAudioEntity response) {
        return new LoadResult.Page<Integer, LocalAudioEntity>(
                (List<? extends LocalAudioEntity>) response,
                null, // Only paging forward.
                response.hashCode(),
                LoadResult.Page.COUNT_UNDEFINED,
                LoadResult.Page.COUNT_UNDEFINED);

    }


    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, LocalAudioEntity> pagingState) {
        return null;
    }
}
