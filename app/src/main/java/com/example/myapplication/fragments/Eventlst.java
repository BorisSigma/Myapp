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
import com.example.myapplication.db.DatabaseHelper;
import com.example.myapplication.domain.Event;
import com.example.myapplication.domain.EventAdapter;

import java.util.ArrayList;
import java.util.List;


public class Eventlst extends Fragment {
    private DatabaseHelper databaseHelper;
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
        databaseHelper = new DatabaseHelper(getContext());
        @SuppressLint("ResourceType") NavController navController = Navigation.findNavController(view);
        eventAdapter = new EventAdapter(getContext(), databaseHelper.getAllEvents());
        recyclerView.setAdapter(eventAdapter);
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
                            eventAdapter = new EventAdapter(getContext(), databaseHelper.getAllEvents());
                            recyclerView.setAdapter(eventAdapter);
                            return;
                        } else if (checkedId == R.id.radioSecurity) {
                            category = "Безопасность";
                        } else if (checkedId == R.id.radioCommunication) {
                            category = "Коммуникации";
                        }

                        eventAdapter = new EventAdapter(
                                getContext(),
                                databaseHelper.getEventsByCategory(category)
                        );
                        recyclerView.setAdapter(eventAdapter);
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
                            eventAdapter = new EventAdapter(getContext(), databaseHelper.getAllEvents());
                            recyclerView.setAdapter(eventAdapter);
                            return;
                        } else if (checkedId == R.id.radioSecurity) {
                            category = "Безопасность";
                        } else if (checkedId == R.id.radioCommunication) {
                            category = "Коммуникации";
                        }

                        eventAdapter = new EventAdapter(
                                getContext(),
                                databaseHelper.getEventsByCategory(category)
                        );
                        recyclerView.setAdapter(eventAdapter);
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