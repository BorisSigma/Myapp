package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.domain.Client;
import com.example.myapplication.res.ApiClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrFragment extends Fragment {
    private EditText loginEt;
    private EditText passEt1;
    private EditText passEt2;
    private EditText user_nameEt;
    private EditText cityEt;
    private AppCompatButton regBt;
    private boolean isPasswordVisible = false;
    private  void togglePasswordVisibility(EditText editText) {
        if (isPasswordVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        isPasswordVisible = !isPasswordVisible;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.registr_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginEt = view.findViewById(R.id.reg_log_new);
        passEt1 = view.findViewById(R.id.reg_pass_new);
        passEt2 = view.findViewById(R.id.reg_pass_rep);
        user_nameEt = view.findViewById(R.id.reg_user_name);
        cityEt = view.findViewById(R.id.reg_city_new);
        regBt = view.findViewById(R.id.bt_reg_reg);
        ImageView eyes = view.findViewById(R.id.eyes2);
        ImageView eyes1 = view.findViewById(R.id.eyes3);
        eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility(passEt1);
            }
        });
        eyes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility(passEt2);
            }
        });
        eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility(passEt1);
            }
        });
        @SuppressLint("ResourceType") NavController navController = Navigation.findNavController(view);
        regBt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onClick(View view) {
                String login = loginEt.getText().toString().trim();
                String pass1 = passEt1.getText().toString().trim();
                String pass2 = passEt2.getText().toString().trim();
                String city = cityEt.getText().toString().trim();
                String user_name = user_nameEt.getText().toString().trim();
                if(login.isEmpty() || pass1.isEmpty() || pass2.isEmpty() || city.isEmpty() || user_name.isEmpty()){
                    Toast.makeText(view.getContext(), "Заполните все поля", Toast.LENGTH_LONG).show();
                }
                else {
                    ApiClient.Users.getService().getClientByLogin(login).enqueue(new Callback<Client>() {
                        @Override
                        public void onResponse(Call<Client> call, Response<Client> response) {
                            Toast.makeText(view.getContext(), "Логин уже занят", Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onFailure(Call<Client> call, Throwable t) {
                            if (pass1.equals(pass2)) {
                                new Thread(() -> {
                                    try {
                                        Geocoder geocoder = new Geocoder(getContext(), new Locale("ru", "RU"));
                                        List<Address> addresses = geocoder.getFromLocationName(city + ", RU", 1);
                                        if (addresses == null || addresses.isEmpty()) {
                                            showError("Город не найден");
                                            return;
                                        }
                                        requireActivity().runOnUiThread(() -> {
                                            Client client = new Client(login, pass1, city, user_name);
                                            ApiClient.Users.getService().addClient(client).enqueue(new Callback<Client>() {
                                                @Override
                                                public void onResponse(Call<Client> call, Response<Client> response) {
                                                    if (response.isSuccessful() && response.body() != null) {
                                                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("id_user", Context.MODE_PRIVATE);
                                                        sharedPreferences.edit().putLong("user_id", response.body().getId()).apply();
                                                        Bundle bundle = new Bundle();
                                                        bundle.putLong("id_user", response.body().getId());
                                                        navController.navigate(R.id.profilfragment, bundle);
                                                    } else {
                                                        Toast.makeText(view.getContext(), "Ошибка при добавлении клиента", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<Client> call, Throwable t) {
                                                    System.out.println(t.getMessage());
                                                    Toast.makeText(view.getContext(), "Проверьте интернет соединение", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        });

                                    } catch (IOException e) {
                                        showError("Ошибка подключения");
                                    }
                                }).start();

                                    }


                        }
                    });
                }
            }});
    }
    private void showError(String message) {
        requireActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        });
    }
}