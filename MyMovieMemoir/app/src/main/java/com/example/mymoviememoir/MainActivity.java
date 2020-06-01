package com.example.mymoviememoir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mymoviememoir.view.HomeFragment;
import com.example.mymoviememoir.view.MapFragment;
import com.example.mymoviememoir.view.MovieMemoirFragment;
import com.example.mymoviememoir.view.MovieSearchFragment;
import com.example.mymoviememoir.view.ReportFragment;
import com.example.mymoviememoir.view.WatchlistFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Navigation between fragments
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawNavigation();
    }

    // Draw Navigation
    private void drawNavigation() {
        // Add toolbar
        Toolbar toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);
        // Navigation
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.NavOpen, R.string.NavClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // show the top left icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new HomeFragment());
    }

    // change fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_content_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_movie_search:
                replaceFragment(new MovieSearchFragment());
                break;
            case R.id.nav_movie_memoir:
                replaceFragment(new MovieMemoirFragment());
                break;
            case R.id.nav_watchList:
                replaceFragment(new WatchlistFragment());
                break;
            case R.id.nav_report:
                replaceFragment(new ReportFragment());
                break;
            case R.id.nav_map:
                replaceFragment(new MapFragment());
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // Navigation top left icon
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}
