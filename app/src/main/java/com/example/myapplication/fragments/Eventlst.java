package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.domain.Event;
import com.example.myapplication.domain.EventAdapter;
import com.example.myapplication.res.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Eventlst extends Fragment {
    private EventAdapter eventAdapter;
    private ImageView to_profil;
    private ImageView filter_image;
    private TextView filter_text;
    private RadioGroup radioGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eventlst, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.event_lst_view);
        filter_image = view.findViewById(R.id.filter_image);
        filter_text = view.findViewById(R.id.textView8);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        @SuppressLint("ResourceType") NavController navController = Navigation.findNavController(view);
        ApiClient.Events.getService().getAllEvents().enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                eventAdapter = new EventAdapter(getContext(), response.body());
                recyclerView.setAdapter(eventAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {

            }
        });
        to_profil = view.findViewById(R.id.to_profil_event_lst);
        long id = getArguments().getLong("id_user", -1);
        to_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong("id_user", id);
                navController.navigate(R.id.profilfragment, bundle);

            }
        });
        filter_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.filter_event);
                RadioGroup radioGroup = dialog.findViewById(R.id.categoryRadioGroup);

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        dialog.dismiss();

                        String category = "";
                        if (checkedId == R.id.radioTransport) {
                            category = "Транспорт";
                        } else if (checkedId == R.id.radioAll) {
                            ApiClient.Events.getService().getAllEvents().enqueue(new Callback<ArrayList<Event>>() {
                                @Override
                                public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                                    eventAdapter = new EventAdapter(getContext(), response.body());
                                    recyclerView.setAdapter(eventAdapter);
                                }

                                @Override
                                public void onFailure(Call<ArrayList<Event>> call, Throwable t) {

                                }
                            });
                            return;
                        } else if (checkedId == R.id.radioSecurity) {
                            category = "Безопасность";
                        } else if (checkedId == R.id.radioCommunication) {
                            category = "Коммуникации";
                        }

                        ApiClient.Events.getService().getEventsByCategory(category).enqueue(new Callback<ArrayList<Event>>() {
                            @Override
                            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                                eventAdapter = new EventAdapter(getContext(), response.body());
                                recyclerView.setAdapter(eventAdapter);
                            }

                            @Override
                            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {

                            }
                        });
                    }
                });

                dialog.setCancelable(true);
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.BOTTOM);
                    window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                }
                dialog.show();
            }
        });
        filter_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.filter_event);
                RadioGroup radioGroup = dialog.findViewById(R.id.categoryRadioGroup);

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        dialog.dismiss();

                        String category = "";
                        if (checkedId == R.id.radioTransport) {
                            category = "Транспорт";
                        } else if (checkedId == R.id.radioAll) {
                            ApiClient.Events.getService().getAllEvents().enqueue(new Callback<ArrayList<Event>>() {
                                @Override
                                public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                                    eventAdapter = new EventAdapter(getContext(), response.body());
                                    recyclerView.setAdapter(eventAdapter);
                                }

                                @Override
                                public void onFailure(Call<ArrayList<Event>> call, Throwable t) {

                                }
                            });
                            return;
                        } else if (checkedId == R.id.radioSecurity) {
                            category = "Безопасность";
                        } else if (checkedId == R.id.radioCommunication) {
                            category = "Коммуникации";
                        }

                        ApiClient.Events.getService().getEventsByCategory(category).enqueue(new Callback<ArrayList<Event>>() {
                            @Override
                            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                                eventAdapter = new EventAdapter(getContext(), response.body());
                                recyclerView.setAdapter(eventAdapter);
                            }

                            @Override
                            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {

                            }
                        });
                    }
                });

                dialog.setCancelable(true);
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.BOTTOM);
                    window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                }
                dialog.show();
            }

        });
    }
}