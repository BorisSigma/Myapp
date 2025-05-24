package com.example.myapplication.fragments;

import static android.widget.Toast.LENGTH_LONG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.db.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Eventcheck extends Fragment {
    private ImageView imageView;
    private AppCompatButton stars_up, stars_down;
    private DatabaseHelper databaseHelper;
    private TextView name_event;
    private TextView event_description;
    private TextView event_categary;
    private TextView event_stars_value;
    private TextView user_stars_value;
    private TextView user_name;
    private TextView evnet_time;
    private ImageView img_back;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eventcheck, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());
        @SuppressLint("ResourceType") NavController navController = Navigation.findNavController(view);
        long event_id = getArguments().getLong("id_event", -1);
        long user_id_1 = getArguments().getLong("id_user", -1);
        stars_up = view.findViewById(R.id.event_chek_stars_up);
        stars_down = view.findViewById(R.id.event_chek_stars_down);
        imageView = view.findViewById(R.id.event_chel_image);
        name_event = view.findViewById(R.id.event_chek_event_name);
        event_description = view.findViewById(R.id.event_chek_event_description);
        event_categary = view.findViewById(R.id.event_chek_event_category);
        event_stars_value =view.findViewById(R.id.event_chek_stars_value_event);
        user_stars_value = view.findViewById(R.id.event_chek_stars_value_user);
        user_name = view.findViewById(R.id.event_chek_usser_name);
        evnet_time = view.findViewById(R.id.event_chek_time);
        img_back = view.findViewById(R.id.event_chek_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                int exit = getArguments().getInt("exit", -1);
                if(exit == 0){
                    bundle.putLong("id_user", user_id_1);
                    navController.navigate(R.id.mapsFragment, bundle);
                }
                else {
                    bundle.putLong("id_user", user_id_1);
                    navController.navigate(R.id.eventlst, bundle);
                }

            }
        });
        ExecutorService executor = Executors.newSingleThreadExecutor();
        stars_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(databaseHelper.getUserIdByEventId(event_id) == user_id_1){
                    Toast.makeText(getContext(), "Вы не можете оценивать свои события", LENGTH_LONG).show();
                }
                else {
                    double currentEventStars = databaseHelper.getEventStarsValue(event_id);
                    if(currentEventStars >= 0.25f) {
                        double newEventValue = currentEventStars - 0.25f;
                        databaseHelper.updateEventStars(event_id, String.valueOf(newEventValue));
                        event_stars_value.setText(String.format("%.2f", newEventValue));
                        long userId = databaseHelper.getUserIdByEventId(event_id);
                        double currentUserStars = databaseHelper.getUserStarsById(userId);
                        if(currentUserStars >= 0.2f) {
                            double newUserValue = (Math.round((currentUserStars - 0.1f) * 10) / 10.0);
                            databaseHelper.updateUserStars(userId, String.valueOf(newUserValue));
                            user_stars_value.setText(String.format("%.1f", newUserValue));
                        } else {
                            databaseHelper.updateUserStars(userId, String.valueOf(0.0f));
                            user_stars_value.setText("0.0");
                            Toast.makeText(getContext(), "Пользователь будет скоро удален", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

        stars_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(databaseHelper.getUserIdByEventId(event_id) == user_id_1){
                    Toast.makeText(getContext(), "Вы не можете оценивать свои события", LENGTH_LONG).show();
                }
                else {
                    double currentEventStars = databaseHelper.getEventStarsValue(event_id);
                    double newEventValue = currentEventStars;

                    if (currentEventStars <= 4.75f) {
                        newEventValue = currentEventStars + 0.25f;
                    } else {
                        newEventValue = 5.0f;
                    }

                    databaseHelper.updateEventStars(event_id, String.valueOf(newEventValue));
                    event_stars_value.setText(String.format("%.2f", newEventValue));
                    long userId = databaseHelper.getUserIdByEventId(event_id);
                    double currentUserStars = databaseHelper.getUserStarsById(userId);

                    if (currentUserStars <= 4.9f) {
                        double newUserValue = (Math.round((currentUserStars + 0.1f) * 10) / 10.0);
                        databaseHelper.updateUserStars(userId, String.valueOf(newUserValue));
                        user_stars_value.setText(String.format("%.1f", newUserValue));
                    } else {
                        databaseHelper.updateUserStars(userId, String.valueOf(5.0f));
                        user_stars_value.setText("5.0");
                    }
                }
            }
        });

        Picasso.get().load(databaseHelper.getEventUrlById(event_id)).into(imageView);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                name_event.setText(databaseHelper.getEventNameById(event_id));
                evnet_time.setText(databaseHelper.getEventTimestampById(event_id));
                event_description.setText(databaseHelper.getEventDescriptionById(event_id));
                event_categary.setText(databaseHelper.getEventCategoryById(event_id));
                event_stars_value.setText(String.valueOf(databaseHelper.getEventStarsValue(event_id)));
                user_name.setText(databaseHelper.getUsernameById(user_id_1));
                user_stars_value.setText(String.valueOf(databaseHelper.getUserStarsById(user_id_1)));

            }
        });



    }
}