package com.example.spokenwapp.di.modules;

import android.app.Application;
import android.content.Context;
import com.example.spokenwapp.adapters.ChurchListAdapter;
import com.example.spokenwapp.data.model.Church;
import java.util.ArrayList;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module(includes = ViewModelModule.class)
public class ApplicationModule {

    private static final String BASE_URL = "https://api.github.com/";
    private Context context;

    @Provides
    @Singleton
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    static ArrayList<Church> provideArrayList(){
        return new ArrayList<Church>();
    }

    @Provides
    @Singleton
    ChurchListAdapter provideChurchListAdapter(ArrayList<Church> list, Context context){
        return new ChurchListAdapter(list, context);
    }

    @Provides
    @Singleton
    Context provideContext(){
        return context;
    }


}
