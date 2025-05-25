package com.example.myapplication.fragments;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.myapplication.R.drawable.star_empty;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.db.DatabaseHelper;
import com.example.myapplication.domain.Client;
import com.example.myapplication.domain.Event;
import com.example.myapplication.exepion.NotFoundEventExecion;
import com.example.myapplication.res.ApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profilfragment extends Fragment {
    private TextView user_name;
    private DatabaseHelper dbHelper;
    private TextView starsValue;
    private AppCompatButton toMapBt;
    private AppCompatButton addEventBt;
    private AppCompatButton eventLst;
    private ImageView goAwayImg;
    private AppCompatButton myProfilbt;
    private AppCompatButton editProfilbt;
    private String user_name_1;
    private double stars_vvalue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.profil_fragmernt, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        long id = getArguments().getLong("id_user", -1);
        System.out.println(id);
        ApiClient.Users.getService().getClientById(id).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if(response.isSuccessful() && response.body() != null){
                    user_name_1 = response.body().getUsername();
                    stars_vvalue = response.body().getStarsvalue();
                }

            }
            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.e("Error", t.getMessage());

            }
        });
        @SuppressLint("ResourceType") NavController navController = Navigation.findNavController(view);

        starsValue = view.findViewById(R.id.profil_stars);
        starsValue.setText("(" + stars_vvalue + ")");
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("id_user", Context.MODE_PRIVATE);
        user_name = view.findViewById(R.id.profil_name);
        double roundedRating = Math.round(stars_vvalue * 2) / 2.0;
        ImageView[] stars = new ImageView[5];
        stars[0] = view.findViewById(R.id.star1);
        stars[1] = view.findViewById(R.id.star2);
        stars[2] = view.findViewById(R.id.star3);
        stars[3] = view.findViewById(R.id.star4);
        stars[4] = view.findViewById(R.id.star5);

        int fullStars = (int) roundedRating;
        boolean hasHalfStar = (roundedRating - fullStars) >= 0.5;

        for (int i = 0; i < 5; i++) {
            if (i < fullStars) {
                stars[i].setImageResource(R.drawable.star_full);
            } else if (i == fullStars && hasHalfStar) {
                stars[i].setImageResource(R.drawable.star_half);
            } else stars[i].setBackground(null);
        }
        user_name.setText(user_name_1);
        goAwayImg = view.findViewById(R.id.profil_out);
        toMapBt = view.findViewById(R.id.my_city_bt);
        addEventBt = view.findViewById(R.id.add_event_bt);
        eventLst = view.findViewById(R.id.event_list_bt);
        editProfilbt = view.findViewById(R.id.profil_edit_bt);
        myProfilbt = view.findViewById(R.id.my_profil_bt);
        addEventBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong("id_user", id);
                bundle.putInt("exit", 1);
                navController.navigate(R.id.blankFragment4, bundle);
            }
        });
        myProfilbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong("id_user", id);
                navController.navigate(R.id.chekProfil, bundle);
            }
        });
        eventLst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("exit", 1);
                bundle.putLong("id_user", id);
                navController.navigate(R.id.eventlst, bundle);
            }
        });
        editProfilbt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.edeng_choice);
                dialog.setCancelable(true);
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.BOTTOM);
                    window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                }
                dialog.show();
                ImageView img1 = dialog.findViewById(R.id.imageView5);
                ImageView img2 = dialog.findViewById(R.id.img6);
                ImageView img3 = dialog.findViewById(R.id.img7);
                img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Dialog dialog3 = new Dialog(view.getContext());
                        dialog3.setContentView(R.layout.city_edit);
                        dialog3.setCancelable(true);
                        Window window3 = dialog3.getWindow();
                        if (window3 != null) {
                            window3.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            window3.setGravity(Gravity.TOP);
                            window3.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        }
                        dialog3.show();
                        TextView help_text = dialog3.findViewById(R.id.change_city_help);
                        EditText new_city = dialog3.findViewById(R.id.et_new_city);
                        AppCompatButton change_city = dialog3.findViewById(R.id.change_city);
                        change_city.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String new_city_str = new_city.getText().toString().trim();
                                Geocoder geocoder = new Geocoder(getContext(), new Locale("ru", "RU"));
                                try {
                                    List<Address> addresses = geocoder.getFromLocationName(new_city_str + ", Россия", 1);
                                    if (addresses == null || addresses.isEmpty()) {
                                        help_text.setText("Город не поддерживарется");
                                    }
                                    else {
                                        Client client = new Client(id, dbHelper.getUserLoginById(id), new_city_str, dbHelper.getEventStarsValue(id), dbHelper.getUsernameById(id), dbHelper.getUserPasswordById(id));
                                        ApiClient.Users.getService().updateClient(client).enqueue(new Callback<Client>() {
                                            @Override
                                            public void onResponse(Call<Client> call, Response<Client> response) {
                                                if(response.isSuccessful()) {
                                                    Toast.makeText(dialog3.getContext(), "Данные обновленны", LENGTH_LONG).show();
                                                    dialog3.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Client> call, Throwable t) {
                                                Toast.makeText(view.getContext(), "Проверьте интернет соединение", LENGTH_LONG).show();
                                                dialog3.dismiss();
                                            }
                                        });

                                    }
                                } catch (IOException e) {
                                    throw new NotFoundEventExecion("Ты лох");
                                }

                            }
                        });

                    }
                });
                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Dialog dialog2 = new Dialog(view.getContext());
                        dialog2.setContentView(R.layout.user_name_edit);
                        dialog2.setCancelable(true);
                        Window window1 = dialog2.getWindow();
                        if (window1 != null) {
                            window1.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            window1.setGravity(Gravity.TOP);
                            window1.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        }
                        dialog2.show();
                        EditText name = dialog2.findViewById(R.id.et_new_user_name);
                        AppCompatButton change_name = dialog2.findViewById(R.id.change_user_name);
                        change_name.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String user_name_new = name.getText().toString().trim();
                                Client client = new Client(id, dbHelper.getUserLoginById(id), dbHelper.getUserCityById(id), dbHelper.getEventStarsValue(id), user_name_new, dbHelper.getUserPasswordById(id));
                                ApiClient.Users.getService().updateClient(client).enqueue(new Callback<Client>() {
                                    @Override
                                    public void onResponse(Call<Client> call, Response<Client> response) {
                                        if(response.isSuccessful()) {
                                            Toast.makeText(dialog2.getContext(), "Данные обновленны", LENGTH_LONG).show();
                                            dialog2.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Client> call, Throwable t) {
                                        Toast.makeText(view.getContext(), "Проверьте интернет соединение", LENGTH_LONG).show();
                                        dialog2.dismiss();
                                    }
                                });
                            }
                        });

                    }
                });
                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Dialog dialog1 = new Dialog(view.getContext());
                        dialog1.setContentView(R.layout.login_edit);
                        dialog1.setCancelable(true);
                        Window window2 = dialog1.getWindow();
                        if (window2 != null) {
                            window2.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            window2.setGravity(Gravity.TOP);
                            window2.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        }
                        dialog1.show();
                        TextView help_text = dialog1.findViewById(R.id.change_login_help);
                        EditText new_login = dialog1.findViewById(R.id.et_new_login);
                        AppCompatButton change_login = dialog1.findViewById(R.id.change_login);
                        change_login.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String new_login_str = new_login.getText().toString().trim();
                                Client client = new Client(id, new_login_str, dbHelper.getUserCityById(id), dbHelper.getEventStarsValue(id), dbHelper.getUsernameById(id), dbHelper.getUserPasswordById(id));
                                ApiClient.Users.getService().getClientByLogin(new_login_str).enqueue(new Callback<Client>() {
                                    @Override
                                    public void onResponse(Call<Client> call, Response<Client> response) {
                                        if(response.body() == null) {
                                            ApiClient.Users.getService().updateClient(client).enqueue(new Callback<Client>() {
                                                @Override
                                                public void onResponse(Call<Client> call, Response<Client> response) {
                                                    if (response.isSuccessful() && response.body() != null){
                                                        Toast.makeText(dialog1.getContext(), "Данные обновленны", LENGTH_LONG).show();
                                                        dialog1.dismiss();

                                                    }


                                                }

                                                @Override
                                                public void onFailure(Call<Client> call, Throwable t) {
                                                    Toast.makeText(view.getContext(), "Проверьте интернет соединение", LENGTH_LONG).show();
                                                    dialog1.dismiss();

                                                }
                                            });
                                        }else {
                                            Toast.makeText(getContext(), "Логин занят", LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Client> call, Throwable t) {
                                        Toast.makeText(view.getContext(), "Проверьте интернет соединение", LENGTH_LONG).show();
                                        dialog1.dismiss();

                                    }
                                });
                            }
                        });

                    }
                });
            }
        });



        toMapBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();
                bundle1.putLong("id_user", id);
                navController.navigate(R.id.mapsFragment, bundle1);
            }
        });
        goAwayImg.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putLong("user_id", -1).apply();
                navController.navigate(R.id.blankFragment);
            }
        });

    }
}