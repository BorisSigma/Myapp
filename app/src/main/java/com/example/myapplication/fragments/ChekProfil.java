package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.domain.Client;
import com.example.myapplication.res.ApiClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChekProfil extends Fragment {
    private ImageView back_img;
    private TextView user_name;
    private TextView login;
    private TextView city;
    private boolean isPasswordVisible = false;
    private TextView pass;
    private TextView stars;
    private ImageView eye;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


    return inflater.inflate(R.layout.fragment_chek_profil, container, false);
    }
    private  void togglePasswordVisibility() {
        if (isPasswordVisible) {
            pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        isPasswordVisible = !isPasswordVisible;
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        @SuppressLint("ResourceType")
        NavController navController = Navigation.findNavController(view);
        back_img = view.findViewById(R.id.profil_chek_back);
        user_name = view.findViewById(R.id.chek_profil_user_name);
        login = view.findViewById(R.id.profil_chek_login);
        city = view.findViewById(R.id.profil_chek_city);
        stars = view.findViewById(R.id.profil_chek_stars);
        pass = view.findViewById(R.id.profil_chek_pass);
        eye = view.findViewById(R.id.eyes);
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility();
            }
        });
        long id = getArguments().getLong("id_user", -1);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putLong("id_user", id);
                navController.navigate(R.id.profilfragment, bundle);
            }
        });
        System.out.println(id);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ApiClient.Users.getService().getClientById(id).enqueue(new Callback<Client>() {
                    @Override
                    public void onResponse(Call<Client> call, Response<Client> response) {
                        user_name.setText(response.body().getUsername());
                        login.setText(response.body().getLogin());
                        city.setText(response.body().getCity());
                        stars.setText(String.valueOf(response.body().getStarsValue()));
                        pass.setText(response.body().getPassword());

                    }

                    @Override
                    public void onFailure(Call<Client> call, Throwable t) {

                    }
                });

            }
        });
    }
}