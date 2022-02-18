package com.example.spokenwapp.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.spokenwapp.R;
import com.example.spokenwapp.adapters.LocalAudioListSearchAdapter;
import com.example.spokenwapp.data.model.LocalAudioEntity;
import com.example.spokenwapp.data.repository.LocalVideoRepository;
import com.example.spokenwapp.data.repository.RepositoryService;
import com.example.spokenwapp.events.SpokenEvents;
import com.example.spokenwapp.localaudio.LocalAudioPageFragment;
import com.example.spokenwapp.search.SpokenLocalAudioSearchFragment;
import com.example.spokenwapp.services.LoadLocalVideoService;
import com.example.spokenwapp.services.SpokenAnalyticsWorkManager;
import com.example.spokenwapp.services.SpokenDacastVodWorkManager;
import com.example.spokenwapp.services.SpokenDacastWorkManager;
import com.example.spokenwapp.services.SpokenMediaBrowserService;
import com.example.spokenwapp.ui.equalizer.DialogEqualizerFragment;
import com.example.spokenwapp.utilities.SpokenSeekBarUtils;
import com.example.spokenwapp.utilities.SpokenSharedPreferences;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.example.spokenwapp.services.SpokenMediaBrowserService.sessionId;


public class SpokenMainScreen extends DaggerAppCompatActivity implements
        SeekBar.OnSeekBarChangeListener, LocalAudioListSearchAdapter.LocalAudioListSearchAdapterListener {

    private static final String TAG_SYNC_DATA = "DACAST LIVE URL";
    private static final String SYNC_DATA_WORK_NAME = "DACAST LIVE";
    private static final String TAG_SYNC_DATA_VOD = "DACAST VOD URL";
    private static final String SYNC_DATA_WORK_NAME_VOD = "DACAST VOD";
    private static final String TAG_SYNC_DATA_ANALYTICS = "DACAST ANALYTICS URL";
    private static final String SYNC_DATA_WORK_NAME_ANALYTICS = "DACAST ANALYTICS";
    private AppBarConfiguration mAppBarConfiguration;
    private static final int PERMISSIONS_REQUEST_CODE = 200;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView currentContentTitle;
    TextView toolbarTitle;
    TextView progressTime;
    TextView totalTime;
    SeekBar mediaSeekBar;
    BottomNavigationView bottomNav;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RelativeLayout relativeLayout;
    public ImageView playButton;
    SpokenSharedPreferences spokenSharedPreferences;
    @Inject
    RepositoryService repositoryService;
    @Inject
    LocalVideoRepository localVideoRepository;
    MediaControllerCompat mediaControllerCompat;
    MediaMetadataCompat metadata;
    PlaybackStateCompat playbackState;
    BroadcastReceiver spokenMessageBroadcastReceiver;
    IntentFilter intentFilterSpokenMessage;
    private Handler mHandler;
    SpokenSeekBarUtils utils;
    public long totalDuration;
    public long currentDuration;
    NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoken_main_screen);

        // If All permission is enabled successfully then this block will execute.
        if(CheckingPermissionIsEnabledOrNot()) {
            Intent intent = new Intent(SpokenMainScreen.this, LoadLocalVideoService.class);
            this.getApplicationContext().startService(intent);
        } else {
            //Calling method to enable permission.
            requestMultiplePermission();
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        appBarLayout = (AppBarLayout) findViewById(R.id.mainScreenAppBar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        relativeLayout = (RelativeLayout) findViewById(R.id.main_relative);

        setSupportActionBar(toolbar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_artwork, R.id.nav_scanapp, R.id.nav_equalizer, R.id.spokenViewPager,
                R.id.churchPageFragment, R.id.localAudioPageFragment, R.id.localVideoPageFragment,
                R.id.spokenLocalAudioSearchFragment)
                .setOpenableLayout(drawer)
                .build();

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        //navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);
        NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);

        // UI components for activity
        currentContentTitle = findViewById(R.id.churchLastTitle);
        playButton = findViewById(R.id.churchPlayButton);
        mediaSeekBar = findViewById(R.id.seekBar);

        // Schedule background work
        scheduleSpokenDacastVod();
        scheduleSpokenDacastLive();
        scheduleSpokenDacastAnalytics();

        // Fetch Videos URL and store in sharedPreferences
        spokenSharedPreferences = new SpokenSharedPreferences(getApplication());

        // Call play from mediaSession and attach to button
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaControllerCompat = MediaControllerCompat.getMediaController(SpokenMainScreen.this);
                if(mediaControllerCompat != null) {
                    metadata = mediaControllerCompat.getMetadata();
                    playbackState = mediaControllerCompat.getPlaybackState();
                }
                // Display the initial state
                assert mediaControllerCompat != null;
                if (playbackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
                    mediaControllerCompat.getTransportControls().pause();
                    playButton.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                } else {
                    if(mediaControllerCompat.getMetadata() == null){
                        mediaControllerCompat.getTransportControls().playFromMediaId(
                                spokenSharedPreferences.getSongMediaId(),null);
                        playButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    }else {
                        mediaControllerCompat.getTransportControls().play();
                        playButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                        currentContentTitle.setText(Objects.requireNonNull(getMediaController().getMetadata())
                                .getDescription().getTitle());
                        performLineAnimation1(currentContentTitle);
                    }

                }

            }
        });

        // Broadcast receiver
        spokenMessageBroadcastReceiver = new SpokenMessageBroadcastReceiver();
        intentFilterSpokenMessage = new IntentFilter(LocalAudioPageFragment.SEND_SPOKEN_ACTION);
        intentFilterSpokenMessage.addAction(Intent.EXTRA_TEXT);
        getApplicationContext().registerReceiver(spokenMessageBroadcastReceiver, intentFilterSpokenMessage);

        // Show and animate song title
        // Animate current content title
        currentContentTitle.setText(spokenSharedPreferences.getSongMediaTitle());
        performLineAnimation1(currentContentTitle);

        // MediaPlayer seekBar
        mediaSeekBar.setOnSeekBarChangeListener(this);
        mHandler = new Handler();
        utils = new SpokenSeekBarUtils();

    }

    public void changeToolbarTitle(String text) {
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(text);
        toolbarTitle.setTextSize(16.0f);
    }

    private void showInDialog() {
        if (sessionId > 0) {
            DialogEqualizerFragment fragment = DialogEqualizerFragment.newBuilder()
                    .setAudioSessionId(sessionId)
                    .title(R.string.app_name)
                    .themeColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryColor))
                    .textColor(ContextCompat.getColor(getApplicationContext(), R.color.textColor))
                    .accentAlpha(ContextCompat.getColor(getApplicationContext(), R.color.playingCardColor))
                    .darkColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryDarkColor))
                    .setAccentColor(ContextCompat.getColor(getApplicationContext(), R.color.secondaryColor))
                    .build();
            fragment.show(getSupportFragmentManager(), "eq");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spoken_main_screen, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.spokenLocalAudioSearchFragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }


    private void requestMultiplePermission() {

        // Creating String Array with Permissions.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(SpokenMainScreen.this, new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.ACCESS_MEDIA_LOCATION
                    }, PERMISSIONS_REQUEST_CODE);
        }

    }

    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean ReadExternalStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean WriteExternalStoragePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean SendSMSPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                boolean RecordAudioPermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                boolean AccessMediaLocationPermission = grantResults[5] == PackageManager.PERMISSION_GRANTED;

                if (CameraPermission && ReadExternalStoragePermission && WriteExternalStoragePermission &&
                        SendSMSPermission && RecordAudioPermission && AccessMediaLocationPermission) {

                    Toast.makeText(SpokenMainScreen.this, "Permission Granted",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SpokenMainScreen.this, "Permission Denied",
                            Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SEND_SMS);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO);
        int SixthPermissionResult = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_MEDIA_LOCATION);
        }

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SixthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onDestroy () {
        super.onDestroy();

        //getApplicationContext().unregisterReceiver(spokenMessageBroadcastReceiver);
    }

    @Override
    protected void onResume () {
        updateCurrentContentText();
        super.onResume();

        mediaControllerCompat = LocalAudioPageFragment.mediaController;
        if(mediaControllerCompat != null) {
            metadata = mediaControllerCompat.getMetadata();
            playbackState = mediaControllerCompat.getPlaybackState();
        }
        if(metadata != null) {
            currentContentTitle.setText(metadata.getDescription().getTitle());
            performLineAnimation1(currentContentTitle);
        }

        getApplicationContext().registerReceiver(spokenMessageBroadcastReceiver, intentFilterSpokenMessage);
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentContentTitle.setText(spokenSharedPreferences.getSongMediaTitle());
        mediaControllerCompat = LocalAudioPageFragment.mediaController;
        if(mediaControllerCompat != null) {
            metadata = mediaControllerCompat.getMetadata();
            playbackState = mediaControllerCompat.getPlaybackState();
        }
        if(metadata != null) {
            currentContentTitle.setText(metadata.getDescription().getTitle());
            performLineAnimation1(currentContentTitle);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        getApplicationContext().unregisterReceiver(spokenMessageBroadcastReceiver);
    }

    public void scheduleSpokenDacastVod() {

        Constraints constraintsvod = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(true)
                .build();

        WorkManager workManagerVod = WorkManager.getInstance(getApplicationContext());
        PeriodicWorkRequest periodicSyncDataWorkVod =
                new PeriodicWorkRequest.Builder(SpokenDacastVodWorkManager.class,
                        15, TimeUnit.MINUTES)
                        .addTag(TAG_SYNC_DATA_VOD)
                        .setConstraints(constraintsvod)
                        // setting a backoff on case the work needs to retry
                        .setBackoffCriteria(BackoffPolicy.LINEAR,
                                PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        workManagerVod.enqueueUniquePeriodicWork(
                SYNC_DATA_WORK_NAME_VOD,
                ExistingPeriodicWorkPolicy.KEEP, //Existing Periodic Work policy
                periodicSyncDataWorkVod //work request
        );

    }

    public void scheduleSpokenDacastLive() {

        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(true)
                .build();

        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        PeriodicWorkRequest periodicSyncDataWork =
                new PeriodicWorkRequest.Builder(SpokenDacastWorkManager.class,
                        15, TimeUnit.MINUTES)
                        .addTag(TAG_SYNC_DATA)
                        .setConstraints(constraints)
                        // setting a backoff on case the work needs to retry
                        .setBackoffCriteria(BackoffPolicy.LINEAR,
                                PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        workManager.enqueueUniquePeriodicWork(
                SYNC_DATA_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, //Existing Periodic Work policy
                periodicSyncDataWork //work request
        );
    }

    public void scheduleSpokenDacastAnalytics() {

        Constraints constraintsanalytics = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(true)
                .build();

        WorkManager workManagerAnalytics = WorkManager.getInstance(getApplicationContext());
        PeriodicWorkRequest periodicSyncDataWorkAnalytics =
                new PeriodicWorkRequest.Builder(SpokenAnalyticsWorkManager.class,
                        15, TimeUnit.MINUTES)
                        .addTag(TAG_SYNC_DATA_ANALYTICS)
                        .setConstraints(constraintsanalytics)
                        // setting a backoff on case the work needs to retry
                        .setBackoffCriteria(BackoffPolicy.LINEAR,
                                PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        workManagerAnalytics.enqueueUniquePeriodicWork(
                SYNC_DATA_WORK_NAME_ANALYTICS,
                ExistingPeriodicWorkPolicy.KEEP, //Existing Periodic Work policy
                periodicSyncDataWorkAnalytics //work request
        );

    }

    public static void performLineAnimation1(TextView view) {
        Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
        TranslateAnimation moveLeftToRight = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF,
                0.1f, TranslateAnimation.RELATIVE_TO_SELF,-0.08f, TranslateAnimation.ABSOLUTE,
                0f, TranslateAnimation.ABSOLUTE,0f);
        moveLeftToRight.setDuration(5000);
        moveLeftToRight.setFillAfter(true);
        moveLeftToRight.setRepeatCount(-1);
        moveLeftToRight.setRepeatMode(Animation.INFINITE);
        moveLeftToRight.setInterpolator(mInterpolator);
        view.startAnimation(moveLeftToRight);

    }

    public void updateCurrentContentText(){

        Observer<String> observer = new Observer<String>() {

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String text) {
                Log.d("UI DATA", "DATA retrieved here: "+text);
                currentContentTitle.setText(text);
            }
        };
        uiObservable().subscribe(observer);
    }

    // UI text observables
    Observable<String> uiObservable () {
        String title = spokenSharedPreferences.getDacastContentTitle();
        return Observable.just(title);
    }

    // SeekBar Listener
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        updateProgressBar();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        utils.progressToTimer(mediaSeekBar.getProgress(), (int) totalDuration);

        // forward or backward to certain seconds
        //mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            // Displaying Total Duration time
            totalTime.setText(utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            progressTime.setText(utils.milliSecondsToTimer(currentDuration));
            mediaSeekBar.setMax((int) totalDuration);
            // Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            mediaSeekBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    public void receiveMessageFromBus(){

        ((SpokenBaseApplication)getApplication()).spokenRxEventBus().toObjectObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        String title = ((SpokenEvents.Message) object).getMessage();
                        currentContentTitle.setText(title);
                        currentDuration = ((SpokenEvents.Message) object).getCurrentDuration();
                        totalDuration = ((SpokenEvents.Message) object).getTotalDuration();
                        Log.e("Receive one", ""+currentDuration);
                        Log.e("Receive Two", ""+totalDuration);

                    }
                });
    }

    public void updateTitle(String title){
        currentContentTitle.setText(title);
        playButton.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
    }

    public void changePlayerVisibility(){
        mediaSeekBar.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
        currentContentTitle.setVisibility(View.GONE);
        bottomNav.setVisibility(View.GONE);
        appBarLayout.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        collapsingToolbarLayout.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.GONE);
        drawer.setBackgroundColor(Color.BLACK);
    }

    public void changePlayerInvisibility(){
        mediaSeekBar.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.VISIBLE);
        currentContentTitle.setVisibility(View.VISIBLE);
        bottomNav.setVisibility(View.VISIBLE);
        appBarLayout.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);
        collapsingToolbarLayout.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLocalAudioListSearchAdapterListener(LocalAudioEntity localAudioEntity) {

    }

    public class SpokenMessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String extra = intent.getAction();
            assert extra != null;
            if (extra.equals(LocalAudioPageFragment.SEND_SPOKEN_ACTION)) {
               Log.e("BROAD ONE","BroadcastReceiver fired ...");
               String extraTitles = intent.getStringExtra("My Title");
               updateTitle(extraTitles);

            }
        }
     }

    /*@Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.headofchrist)
                .setTitle("SpokenWordApp")
                .setMessage("Are you sure you want to close SpokenWordApp?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }*/


    }






