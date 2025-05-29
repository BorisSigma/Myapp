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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.domain.Client;
import com.example.myapplication.domain.Event;
import com.example.myapplication.res.ApiClient;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Eventcheck extends Fragment {
    private ImageView imageView;
    private AppCompatButton stars_up, stars_down;
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

        stars_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiClient.Events.getService().getEventById(event_id).enqueue(new Callback<Event>() {
                    @Override
                    public void onResponse(Call<Event> call, Response<Event> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            Client client1 = response.body().getClient();
                            if (client1.getId() == user_id_1) {
                                Toast.makeText(getContext(), "Вы не можете оценивать свои события", LENGTH_LONG).show();
                            } else {
                                double currentEventStars = response.body().getEvent_stars_value();
                                if (currentEventStars >= 0.25) {
                                    double newEventValue = currentEventStars - 0.25;
                                    Event event = response.body();
                                    event.setEvent_stars_value(newEventValue);
                                    ApiClient.Events.getService().updateEvent(event).enqueue(new Callback<Event>() {
                                        @Override
                                        public void onResponse(Call<Event> call, Response<Event> response) {
                                            if (response.isSuccessful()) {
                                                event_stars_value.setText(String.valueOf(response.body().getEvent_stars_value()));
                                                Client client = response.body().getClient();
                                                if (client.getStarsValue() >= 0.1) {
                                                    double newClientStarsValue = client.getStarsValue() - 0.1;
                                                    System.out.println(newClientStarsValue);
                                                    client.setStarsValue(newClientStarsValue);
                                                    System.out.println(client);
                                                    ApiClient.Users.getService().updateClient(client).enqueue(new Callback<Client>() {
                                                        @Override
                                                        public void onResponse(Call<Client> call, Response<Client> response) {
                                                            if (response.isSuccessful()) {
                                                                System.out.println(response.body().toString());
                                                                user_stars_value.setText(String.valueOf(response.body().getStarsValue()));
                                                            } else {
                                                                Log.e("Error", "Ошибка обновления пользователя: " + response.message());
                                                                Toast.makeText(getContext(), "Ошибка обновления пользователя", LENGTH_LONG).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Client> call, Throwable t) {
                                                            Log.e("Error", "Ошибка сети при обновлении пользователя: " + t.getMessage());
                                                            Toast.makeText(getContext(), "Ошибка сети при обновлении пользователя", LENGTH_LONG).show();
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(getContext(), "Пользователь скоро будет удален", LENGTH_LONG).show();
                                                }
                                            } else {
                                                Log.e("Error", "Ошибка обновления события: " + response.message());
                                                Toast.makeText(getContext(), "Ошибка обновления события", LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Event> call, Throwable t) {
                                            Log.e("Error", "Ошибка сети при обновлении события: " + t.getMessage());
                                            Toast.makeText(getContext(), "Ошибка сети при обновлении события", LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), "Рейтинг события не может быть меньше 0", LENGTH_LONG).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Event> call, Throwable t) {
                        Log.e("Error", "Ошибка сети при получении события: " + t.getMessage());
                        Toast.makeText(getContext(), "Ошибка сети при получении события", LENGTH_LONG).show();
                    }
                });
            }
        });


        stars_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiClient.Events.getService().getEventById(event_id).enqueue(new Callback<Event>() {
                    @Override
                    public void onResponse(Call<Event> call, Response<Event> response) {
                        if(response.body() != null && response.isSuccessful()){
                            Client client1 = response.body().getClient();
                            if (client1.getId() == user_id_1) {
                                Toast.makeText(getContext(), "Вы не можете оценивать свои события", LENGTH_LONG).show();
                            }
                            else {
                                double currentEventStars = response.body().getEvent_stars_value();
                                if (currentEventStars <= 4.75) {
                                    double newEventValue = currentEventStars + 0.25;
                                    Event event = response.body();
                                    event.setEvent_stars_value(newEventValue);
                                    ApiClient.Events.getService().updateEvent(event).enqueue(new Callback<Event>() {
                                        @Override
                                        public void onResponse(Call<Event> call, Response<Event> response) {
                                            if(response.isSuccessful()){
                                                event_stars_value.setText(String.valueOf(response.body().getEvent_stars_value()));
                                                Client client = response.body().getClient();
                                                if(client.getStarsValue() <= 4.9){
                                                    client.setStarsValue(client.getStarsValue() + 0.1);
                                                    ApiClient.Users.getService().updateClient(client).enqueue(new Callback<Client>() {
                                                        @Override
                                                        public void onResponse(Call<Client> call, Response<Client> response) {
                                                            if(response.isSuccessful()){
                                                                user_stars_value.setText(String.valueOf(response.body().getStarsValue()));

                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Client> call, Throwable t) {

                                                        }
                                                    });
                                                }
                                                else {
                                                    event_stars_value.setText(String.valueOf(5.0));
                                                }

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Event> call, Throwable t) {

                                        }
                                    });
                                }
                                else {
                                    event_stars_value.setText(String.valueOf(5.0));

                                }

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Event> call, Throwable t) {

                    }
                });
            }
        });


        ApiClient.Events.getService().getEventById(event_id).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                Glide.with(requireContext()).load(response.body().getEventUrl()).into(imageView);
                name_event.setText(response.body().getEventName());
                System.out.println(response.body().getId());
                evnet_time.setText(response.body().getEvent_time());
                event_description.setText(response.body().getEventDescription());
                event_categary.setText(response.body().getCategory());
                event_stars_value.setText(String.valueOf(response.body().getEvent_stars_value()));
                Client client = response.body().getClient();
                user_name.setText(client.getUsername());
                user_stars_value.setText(String.valueOf(client.getStarsValue()));
            }
            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                System.out.println(t.getMessage());}
        });
    }


}