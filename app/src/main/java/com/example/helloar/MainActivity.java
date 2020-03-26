package com.example.helloar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.nav_open_drawer,R.string.hello_blank_fragment);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView =findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
        Fragment fragment = new City_places();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_frame,fragment);
        ft.commit();
    }




    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment=null;

        switch (menuItem.getItemId())
        {
            case (R.id.nav_gps_test):
                fragment = new GPS();break;
            case (R.id.nav_real_time):
                Intent intent = new Intent(this, Real_time_activity.class);
                startActivity(intent);break;
            case (R.id.nav_place_history):
                fragment= new History();break;
            case (R.id.nav_city_places):
                fragment=new City_places();break;


         default:   new City_places();
        }
        if(fragment!=null)
        {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,fragment);
                ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}