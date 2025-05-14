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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.db.DatabaseHelper;


public class RegistrFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private EditText loginEt;
    private EditText passEt1;
    private EditText passEt2;
    private EditText user_nameEt;
    private EditText cityEt;
     private AppCompatButton regBt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        databaseHelper = new DatabaseHelper(getContext());
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
                    if(databaseHelper.isLoginUnique(login)){
                        if(pass1.equals(pass2)){
                            if (city.equals("Москва")|| city.equals("москва")){

                                databaseHelper.addUser(login,pass1, city, user_name);
                                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("id_user", Context.MODE_PRIVATE);
                                sharedPreferences.edit().putLong("user_id", databaseHelper.getUserIdByLogin(login)).apply();
                                Bundle bundle = new Bundle();
                                bundle.putLong("id_user", databaseHelper.getUserIdByLogin(login));
                                navController.navigate(R.id.profilfragment, bundle);

                            }
                            else {
                                Toast.makeText(view.getContext(), "Город не поддреживается", Toast.LENGTH_LONG).show();
                            }

                        }
                        else {
                            Toast.makeText(view.getContext(), "Пароли не совпадают", Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        Toast.makeText(view.getContext(), "Логин уже занят", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }
}