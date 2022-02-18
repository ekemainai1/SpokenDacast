package com.example.spokenwapp.di.modules;

import android.app.Application;
import android.content.Context;
import com.example.spokenwapp.utilities.SpokenSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@Module(includes = ViewModelModule.class)
public class ApplicationModule {

    private Context context;
    Application application;
    SpokenSharedPreferences spokenSharedPreferences;
    private static String SYS_IP = "https://api.dacast.com/";
    private static final MediaType JSON = MediaType.get("application/xml; charset=utf-8");
    Gson gson = new GsonBuilder()
            //.registerTypeAdapter(Config.class,  new ConfigDeserializer<Config>())
            //.registerTypeAdapter(Data.class,  new DataDeserializer())
            //.registerTypeAdapter(Paging.class,  new PagingDeserializer())
            //.registerTypeAdapter(Pictures.class,  new PicturesDeserializer())
           // .registerTypeAdapter(ShareCode.class,  new ShareCodeDeserializer())
            //.registerTypeAdapter(DacastObject.class,  new DacastDeserializer<DacastObject>())
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setLenient()
            .create();

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        //spokenSharedPreferences = new SpokenSharedPreferences(application.getApplicationContext());
        String BASE_URI = SYS_IP;
        HttpLoggingInterceptor interceptorLog = new HttpLoggingInterceptor();
        Interceptor interceptor = chain -> {
            Request newRequest = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", String.valueOf(JSON))
            .build();
            return chain.proceed(newRequest);
    };

     interceptorLog.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .addInterceptor(interceptorLog)
                    .build();

        return new Retrofit.Builder()
            .baseUrl(BASE_URI)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build();

    }

    @Provides
    @Singleton
    Context provideContext(){
        return context;
    }



}
