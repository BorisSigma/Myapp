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

import com.example.myapplication.R;


public class StartFragment extends Fragment {
    private AppCompatButton logInBt;
    private AppCompatButton regBt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.start_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        regBt = view.findViewById(R.id.bt_main_reg);
        logInBt = view.findViewById(R.id.bt_main_in);

        @SuppressLint("ResourceType")NavController navController = Navigation.findNavController(view);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("id_user", Context.MODE_PRIVATE);
        long id = sharedPreferences.getLong("user_id", -1);
        if (id != -1){
            Bundle bundle = new Bundle();
            bundle.putLong("id_user", id);
            navController.navigate(R.id.profilfragment, bundle);
        }
        logInBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.blankFragment2);

            }
        });
        regBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.blankFragment3);

            }
        });

    }
}