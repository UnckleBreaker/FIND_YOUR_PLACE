package com.example.helloar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final String GOOGLE_KEY = "AIzaSyC74_Uw8EmP5Quoyv_aklogzKpWB8umkfM";
    String temp = null;
    List<GooglePlace> venuesList;
    SQLiteDatabase mDatabase;
    ContentValues contentValues ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast toast= Toast.makeText(getApplicationContext(),"Выберите нужное заведение", Toast.LENGTH_LONG);
        toast.show();
        Dhelper dbHelper =new Dhelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        new Async().execute();
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
    private class Async extends AsyncTask<View, Void,String>{

        @Override
        protected String doInBackground(View... views) {
            try {

                temp = makeCall("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=53.903908,27.560423&radius=200500&type=bar&keyword=cruise&key=" + GOOGLE_KEY);
                venuesList = parseGoogleParse(temp);

                for (int i = 0; i < venuesList.size(); i++){
                    String name = venuesList.get(i).getName();
                    String category = venuesList.get(i).getCategory();
                    String openNow = venuesList.get(i).getOpenNow();
                    String rating = venuesList.get(i).getRating();
                    String vicinity = venuesList.get(i).getVicinity();
                    String lat = venuesList.get(i).getLat();
                    String lng = venuesList.get(i).getLng();
                    contentValues = new ContentValues();
                    contentValues.put(DBContract.PaymentEntry.COLUMN_NAME, name);
                    contentValues.put(DBContract.PaymentEntry.COLUMN_RATING, rating);
                    contentValues.put(DBContract.PaymentEntry.COLUMN_ISOPEN, openNow);
                    contentValues.put(DBContract.PaymentEntry.COLUMN_CATEGORY, category);
                    contentValues.put(DBContract.PaymentEntry.COLUMN_VICINITY, vicinity);
                    contentValues.put(DBContract.PaymentEntry.COLUMN_LAT, lat);
                    contentValues.put(DBContract.PaymentEntry.COLUMN_LNG, lng);
                    mDatabase.insert(DBContract.PaymentEntry.TABLE,null , contentValues);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return temp;
        }
        public String makeCall(String url) throws IOException {
            String data = "";
            StringBuffer buffer_string = new StringBuffer(url);
            String replyString = "";

            try {
                URL myurl = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) myurl.openConnection();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader buffredReader = new BufferedReader(new InputStreamReader(is));
                StringBuilder stringBuilder = new StringBuilder();

                String line = "";
                while ((line = buffredReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                data = stringBuilder.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        private  ArrayList<GooglePlace> parseGoogleParse(final String response) {

            ArrayList<GooglePlace> temp = new ArrayList<GooglePlace>();
            try {

                // make an jsonObject in order to parse the response
                JSONObject jsonObject = new JSONObject(response);

                // make an jsonObject in order to parse the response
                if (jsonObject.has("results")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        GooglePlace poi = new GooglePlace();
                        if (jsonArray.getJSONObject(i).has("name")) {
                            poi.setName(jsonArray.getJSONObject(i).optString("name"));
                            poi.setRating(jsonArray.getJSONObject(i).optString("rating"));


                            if (jsonArray.getJSONObject(i).has("opening_hours")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                                    if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                        poi.setOpenNow("YES");
                                    } else {
                                        poi.setOpenNow("NO");
                                    }
                                }
                            } else {
                                poi.setOpenNow("Not Known");
                            }
                            if (jsonArray.getJSONObject(i).has("types")) {
                                JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");

                                for (int j = 0; j < typesArray.length(); j++) {
                                    poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                                }
                            }
                            if(jsonArray.getJSONObject(i).has("vicinity")){
                                poi.setVicinity(jsonArray.getJSONObject(i).optString("vicinity"));
                            }
                            if((jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").has("lat")))
                            {
                                poi.setLat(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optString("lat"));
                            }
                            if((jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").has("lng")))
                            {
                                poi.setLng(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optString("lng"));
                            }
                        }
                        temp.add(poi);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<GooglePlace>();
            }
            return temp;

        }
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
                Intent intent2 = new Intent(this, History.class);
                startActivity(intent2);break;
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