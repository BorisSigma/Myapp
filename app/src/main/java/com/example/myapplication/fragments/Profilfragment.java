package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.example.myapplication.domain.Event;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.profil_fragmernt, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long id = getArguments().getLong("id_user", -1);
        dbHelper = new DatabaseHelper(getContext());
        @SuppressLint("ResourceType") NavController navController = Navigation.findNavController(view);
        double stars_vvalue = dbHelper.getUserStarsById(id);
        starsValue = view.findViewById(R.id.profil_stars);
        starsValue.setText("(" + stars_vvalue + ")");
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("id_user", Context.MODE_PRIVATE);
        user_name = view.findViewById(R.id.profil_name);


        String user_name_1 = dbHelper.getUsernameById(id);
        user_name.setText(user_name_1);
        goAwayImg = view.findViewById(R.id.profil_out);
        toMapBt = view.findViewById(R.id.my_city_bt);
        addEventBt = view.findViewById(R.id.add_event_bt);
        eventLst = view.findViewById(R.id.event_list_bt);
        editProfilbt = view.findViewById(R.id.profil_edit_bt);
        myProfilbt = view.findViewById(R.id.my_profil_bt);
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
                                dbHelper.updateUsername(id, user_name_new);
                                user_name.setText(user_name_new);
                                dialog2.dismiss();
                                Toast.makeText(dialog2.getContext(), "Данные обновленны", Toast.LENGTH_LONG).show();
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
                                if(dbHelper.isLoginUnique(new_login_str)){
                                    dbHelper.updateUserLogin(id, new_login_str);
                                }
                                else {
                                    help_text.setText("Логин не уникален");
                                }
                                dialog1.dismiss();
                                Toast.makeText(dialog1.getContext(), "Данные обновленны", Toast.LENGTH_LONG).show();
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