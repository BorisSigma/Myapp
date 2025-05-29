package com.example.myapplication.fragments;

import static android.widget.Toast.LENGTH_LONG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;

import com.example.myapplication.domain.Client;
import com.example.myapplication.domain.Event;
import com.example.myapplication.res.ApiClient;
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
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment{
    private FusedLocationProviderClient fusedLocationClient;

    private static boolean isTouch = false;
    private static LatLng firstLatLng =  new LatLng(55.751244, 37.618423);
    private static final int request_Code = 1;
    private ImageView imghome;
    private ImageView event_lst;
    private ImageView img_add_event;
    private ArrayList<Event> events;
    private ImageView img_lsstt_event;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
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
                long user_id = getArguments().getLong("id_user", -1);
                ApiClient.Users.getService().getClientById(user_id).enqueue(new Callback<Client>() {
                    @Override
                    public void onResponse(Call<Client> call, Response<Client> response) {
                        if(response.body() != null && response.isSuccessful()) googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getCityLatLng(getContext(), response.body().getCity()), 10));

                    }

                    @Override
                    public void onFailure(Call<Client> call, Throwable t) {
                        Toast.makeText(getContext(), "Проверьте интернет соединение", LENGTH_LONG).show();

                    }
                });
            }
            try {
                googleMap.setMyLocationEnabled(true);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            long user_id = getArguments().getLong("id_user", -1);
                            if (location != null) {
                                LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
                            }
                            else {
                                ApiClient.Users.getService().getClientById(user_id).enqueue(new Callback<Client>() {
                                    @Override
                                    public void onResponse(Call<Client> call, Response<Client> response) {
                                        if(response.body() != null && response.isSuccessful()) googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getCityLatLng(getContext(), response.body().getCity()), 10));

                                    }

                                    @Override
                                    public void onFailure(Call<Client> call, Throwable t) {
                                        Toast.makeText(getContext(), "Проверьте интернет соединение", LENGTH_LONG).show();

                                    }
                                });
                            }
                        });
            } catch (Exception e) {
                Log.e("CannotSetMyLocation", e.getMessage());
            }
            ApiClient.Events.getService().getAllEvents().enqueue(new Callback<ArrayList<Event>>() {
                @Override
                public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                    if(response.isSuccessful() && response.body() != null) {
                        events = response.body();
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
                                    ApiClient.Events.getService().getEventByLatLng(marker.getTitle()).enqueue(new Callback<Event>() {
                                        @Override
                                        public void onResponse(Call<Event> call, Response<Event> response) {
                                            if (response.isSuccessful() && response.body() != null){
                                                long event_id = response.body().getId();
                                                long user_id = getArguments().getLong("id_user", -1);
                                                Bundle bundle1 = new Bundle();
                                                bundle1.putLong("id_user", user_id);
                                                bundle1.putLong("id_event", event_id);
                                                bundle1.putInt("exit", 0);
                                                isTouch = true;
                                                NavHostFragment.findNavController(MapsFragment.this).navigate(R.id.action_mapsFragment_to_eventcheck, bundle1);
                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<Event> call, Throwable t) {
                                            Log.e("Error", t.getMessage());


                                        }
                                    });
                                    return true;

                                }

                            });
                        } catch (Exception e) {
                            Log.e("MAP LATLNG", e.getMessage());
                        }
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                    Toast.makeText(getContext(), "Проверьте интернет соединение", LENGTH_LONG).show();

                }
            });
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
            img_add_event = view.findViewById(R.id.add_event_maps);
            img_add_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long user_id = getArguments().getLong("id_user", -1);
                    Bundle bundle = new Bundle();
                    bundle.putLong("id_user", user_id);
                    bundle.putInt("exit", 0);
                    navController.navigate(R.id.blankFragment4, bundle);
                }
            });
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
    public LatLng getCityLatLng(Context context, String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            Log.w("Geocoder", "Пустое название города");
            return null;
        }

        Geocoder geocoder = new Geocoder(context, new Locale("ru", "RU"));

        try {
            List<Address> addresses = geocoder.getFromLocationName(cityName + ", Россия", 5);

            if (addresses == null || addresses.isEmpty()) {
                Log.i("Geocoder", "Город не найден: " + cityName);
                return firstLatLng;
            }
            for (Address address : addresses) {
                if (address.getCountryCode() != null &&
                        address.getCountryCode().equalsIgnoreCase("RU") &&
                        address.hasLatitude() &&
                        address.hasLongitude()) {

                    return new LatLng(
                            address.getLatitude(),
                            address.getLongitude()
                    );
                }
            }
        } catch (IOException e) {
            Log.e("Geocoder", "Ошибка подключения: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("Geocoder", "Некорректные параметры: " + e.getMessage());
        }

        return null;
    }
}

