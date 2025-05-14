package com.example.myapplication.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.db.DatabaseHelper;
import com.example.myapplication.domain.Event;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment{
    private FusedLocationProviderClient fusedLocationClient;

    private static boolean isTouch = false;
    private static LatLng firstLatLng =  new LatLng(55.751244, 37.618423);
    private static final int request_Code = 1;
    private DatabaseHelper databaseHelper;
    private ImageView imghome;
    private ImageView event_lst;
    private ImageView img_add_event;
    private ImageView img_lsstt_event;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            databaseHelper = new DatabaseHelper(getContext());
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION}, request_Code);
            }
            if (isTouch) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 10));
            }
            try {
                googleMap.setMyLocationEnabled(true);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 10));
                            }
                        });
            } catch (Exception e) {
                Log.e("CannotSetMyLocation", e.getMessage());
            }
            ArrayList<Event> events = databaseHelper.getAllEvents();
            try {
                for (int i = 0; i < events.size(); i++) {
                    try {
                        LatLng latLng = getLocationFromAddress(events.get(i).getEventLocation(), googleMap);
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(events.get(i).getEventLocation()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        long event_id = databaseHelper.getEventIdByLocation(marker.getTitle());
                        long user_id = getArguments().getLong("id_user", -1);
                        Bundle bundle1 = new Bundle();
                        bundle1.putLong("id_user", user_id);
                        bundle1.putLong("id_event", event_id);
                        bundle1.putInt("exit", 0);
                        @SuppressLint("ResourceType")
                        NavController navController = Navigation.findNavController(getView());
                        navController.navigate(R.id.eventcheck, bundle1);
                        isTouch = true;
                        return true;
                    }
                });
            } catch (Exception e) {
                Log.e("MAP LATLNG", e.getMessage());
            }
        }
    };

        @Nullable
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_maps, container, false);
        }

        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            @SuppressLint("ResourceType")
            NavController navController = Navigation.findNavController(view);
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(callback);
            }
            imghome = view.findViewById(R.id.logo_maps);
            img_lsstt_event = view.findViewById(R.id.event_lst_maps);
            img_lsstt_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long user_id = getArguments().getLong("id_user", -1);
                    Bundle bundle = new Bundle();
                    bundle.putLong("id_user", user_id);
                    navController.navigate(R.id.eventlst, bundle);
                }
            });
            imghome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long user_id = getArguments().getLong("id_user", -1);
                    Bundle bundle = new Bundle();
                    bundle.putLong("id_user", user_id);
                    navController.navigate(R.id.profilfragment, bundle);
                }
            });
        }

        public LatLng getLocationFromAddress(String strAddress, GoogleMap mMap) {
            Geocoder coder = new Geocoder(getContext());
            LatLng latLng = null;
            List<Address> address;

            try {
                address = coder.getFromLocationName(strAddress, 5);
                if (address == null) {
                    return latLng;
                }
                Address location = address.get(0);
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return latLng;
        }
    }

