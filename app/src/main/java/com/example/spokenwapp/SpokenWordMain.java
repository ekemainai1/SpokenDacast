package com.example.spokenwapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.spokenwapp.adapters.SpokenViewPagerAdapter;
import com.example.spokenwapp.ui.church.ChurchPageFragment;
import com.example.spokenwapp.ui.localaudio.LocalAudioPageFragment;
import com.example.spokenwapp.ui.localvideos.LocalVideoPageFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

public class SpokenWordMain extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    ViewPager2 viewPager2Spoken;
    TabLayout tabLayoutSpoken;
    SpokenViewPagerAdapter spokenViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoken_word_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view,
                "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        viewPager2Spoken = findViewById(R.id.pager);
        tabLayoutSpoken = findViewById(R.id.tab_layout);
        spokenViewPagerAdapter = new SpokenViewPagerAdapter(SpokenWordMain.this);
        viewPager2Spoken.setAdapter(spokenViewPagerAdapter);

        new TabLayoutMediator(tabLayoutSpoken, viewPager2Spoken, (tab, position) -> {
            if(position == 0){
                tab.setText(ChurchPageFragment.ARG_CHURCH);
            }else if(position == 1){
                tab.setText(LocalAudioPageFragment.ARG_AUDIO);
            }else if(position == 2){
                tab.setText(LocalVideoPageFragment.ARG_VIDEO);
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.spoken_word_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }
}
