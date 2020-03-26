package com.example.helloar;


import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class GPS extends Fragment {
    private Button btnLoc;
    private TextView textView;
    double lat =0;
    double lon = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.gps, container, false);
        btnLoc = rootView.findViewById(R.id.btn);
        textView =rootView.findViewById(R.id.text_view);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        btnLoc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                gps_tracker gt = new gps_tracker(getContext());
                Location l = gt.getLocation();
                if (l == null)
                {
                    Toast.makeText(getContext(), "GPS unable to get Value", Toast.LENGTH_SHORT).show();
                } else
                    {
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();
                    Toast.makeText(getContext(), "GPS Lat = " + lat + "\n lon = " + lon, Toast.LENGTH_SHORT).show();
                    }
            }
        });
        return rootView;
    }
}





