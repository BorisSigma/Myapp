package com.example.myapplication.fragments;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.myapplication.db.DatabaseHelper;
import com.example.myapplication.domain.Client;
import com.example.myapplication.res.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment { ;
    private DatabaseHelper databaseHelper;
    private EditText loginEt;
    private EditText passwordEt;
    private AppCompatButton button;
    private boolean isPasswordVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }
    private  void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        isPasswordVisible = !isPasswordVisible;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginEt = view.findViewById(R.id.logo_in_login);
        passwordEt = view.findViewById(R.id.logo_in_pass);
        button = view.findViewById(R.id.bt_logo_in);
        databaseHelper = new DatabaseHelper(getContext());
        ImageView eyes = view.findViewById(R.id.eyes1);
        eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility();
            }
        });


        @SuppressLint("ResourceType") NavController navController = Navigation.findNavController(view);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onClick(View view) {
                String login = loginEt.getText().toString().trim();
                String pass = passwordEt.getText().toString().trim();
                if (login.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(view.getContext(), "Введите логин и пароль", Toast.LENGTH_LONG).show();
                }

                else {
                    ApiClient.Users.getService().getClientByLogin(login).enqueue(new Callback<Client>() {
                        @Override
                        public void onResponse(Call<Client> call, Response<Client> response) {
                            if(response.body() != null){
                                if(response.body().getPassword().equals(pass)){
                                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("id_user", Context.MODE_PRIVATE);
                                    sharedPreferences.edit().putLong("user_id", response.body().getId()).apply();
                                    Bundle bundle = new Bundle();
                                    System.out.println(response.body().getId());
                                    bundle.putLong("id_user", response.body().getId());
                                    navController.navigate(R.id.profilfragment, bundle);

                                }
                                else Toast.makeText(view.getContext(), "Логин или пароль введен не верно", Toast.LENGTH_LONG).show();

                            }
                            else {
                                Toast.makeText(view.getContext(), "Логин или пароль введен не верно", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Client> call, Throwable t) {
                            Toast.makeText(view.getContext(), "Логин или пароль введен не верно", Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }
        });
    }
}