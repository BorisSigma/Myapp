package com.example.myapplication.fragments;

import static android.app.Activity.RESULT_OK;
import static android.widget.Toast.LENGTH_LONG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.domain.Client;
import com.example.myapplication.domain.Event;
import com.example.myapplication.res.ApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class BlankFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private ImageView photoEvent, addPhoto, back;
    private String publicUrl;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private OkHttpClient client = new OkHttpClient();

    private Uri cameraImageUri;
    private File cameraImageFile;
    private static final String TAG = "SUPABASE_UPLOAD";
    private static final String SUPABASE_URL = "https://goedlwrfezsidasdiude.supabase.co";
    private static final String SUPABASE_BUCKET = "photo";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdvZWRsd3JmZXpzaWRhc2RpdWRlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDc4NDE5MTAsImV4cCI6MjA2MzQxNzkxMH0.UTZ2PfPYGhyHSqmY3beEn7ofqpDobSuzdjBVI12DWvI";



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photoEvent = view.findViewById(R.id.event_add_image);
        @SuppressLint("ResourceType") NavController navController = Navigation.findNavController(view);
        addPhoto = view.findViewById(R.id.add_photo);
        long id = getArguments().getLong("id_user", -1);
        AppCompatButton addEventBt = view.findViewById(R.id.event_add_event);
        back = view.findViewById(R.id.event_add_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                int num = getArguments().getInt("exit", -1);
                bundle.putLong("id_user", id);
                if(num == 1) navController.navigate(R.id.profilfragment, bundle);
                else navController.navigate(R.id.mapsFragment, bundle);

            }
        });

        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 101);
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.e(TAG, "photo made 1");
                    if (result.getResultCode() == RESULT_OK) {
                        if (cameraImageFile != null && cameraImageFile.exists()) {
                            Log.e(TAG, "photo made  2");
                            uploadImageToSupabase(cameraImageFile);
                        } else {
                            Log.e(TAG, "Файл снимка не найден");
                        }
                    }
                    else {
                        Log.e(TAG, "photo made3");
                    }
                });

        addPhoto.setOnClickListener(v -> {
            try {
                cameraImageFile = createImageFile();
                cameraImageUri = FileProvider.getUriForFile(
                        getContext(),
                        requireActivity().getPackageName() + ".provider",
                        cameraImageFile
                );
                Log.e(TAG, "camera showed");

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                cameraLauncher.launch(intent);
            } catch (IOException e) {
                Log.e(TAG, "Ошибка создания файла: " + e.getMessage(), e);
            }
        });




        EditText name_event = view.findViewById(R.id.event_add_event_name);
        AppCompatButton category = view.findViewById(R.id.event_add_event_category);
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        EditText description = view.findViewById(R.id.event_add_event_description);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.add_category);
                RadioGroup radioGroup = dialog.findViewById(R.id.categoryRadioGroup1);
                dialog.setCancelable(true);
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.BOTTOM);
                    window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                }
                dialog.show();
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        dialog.dismiss();
                        String category1 = "";
                        if (checkedId == R.id.radioTransport1) {
                            category1 = "Транспорт";
                        } else if (checkedId == R.id.radioSecurity1) {
                            category1 = "Безопасность";
                        } else if (checkedId == R.id.radioCommunication1) {
                            category1 = "Коммуникации";
                        }
                        category.setText(category1);
                    }
                });
            }
        });
        addEventBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = name_event.getText().toString().trim();
                String cat = category.getText().toString().trim();
                String disc = description.getText().toString().trim();
                String url = publicUrl;
                if (name == null || cat == null || disc == null || url == null) {
                    Toast.makeText(getContext(),
                            "Заполните всю информацию", Toast.LENGTH_SHORT).show();
                } else {
                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                        return;
                    }
                    fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                        if (location != null) {
                            String userLatLng = location.getLatitude() + "," + location.getLongitude();
                            System.out.println(userLatLng);
                            ApiClient.Users.getService().getClientById(id).enqueue(new retrofit2.Callback<Client>() {
                                @Override
                                public void onResponse(retrofit2.Call<Client> call, retrofit2.Response<Client> response) {
                                    if(response.isSuccessful() && response.body() != null){
                                        ApiClient.Users.getService().getClientById(id).enqueue(new retrofit2.Callback<Client>() {
                                            @Override
                                            public void onResponse(retrofit2.Call<Client> call, retrofit2.Response<Client> response) {
                                                LocalDateTime now = null;
                                                String time = null;
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    now = LocalDateTime.now();
                                                }
                                                DateTimeFormatter formatter = null;
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                                                }
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    time = now.format(formatter);
                                                }
                                                System.out.println(response.body().toString());
                                                Event event = new Event(response.body(), name, disc, userLatLng, cat, url ,response.body().getUsername(), time);
                                                ApiClient.Events.getService().addEvent(event).enqueue(new retrofit2.Callback<Event>() {
                                                    @Override
                                                    public void onResponse(retrofit2.Call<Event> call, retrofit2.Response<Event> response) {
                                                        if(response.isSuccessful() && response.body() != null){
                                                            Log.e("Error", response.body().toString());
                                                            Bundle bundle = new Bundle();
                                                            bundle.putLong("id_user", id);
                                                            navController.navigate(R.id.mapsFragment, bundle);
                                                        }

                                                    }

                                                    @Override
                                                    public void onFailure(retrofit2.Call<Event> call, Throwable t) {
                                                        Toast.makeText(view.getContext(), "Проверьте интернет соединение", LENGTH_LONG).show();

                                                    }
                                                });

                                            }

                                            @Override
                                            public void onFailure(retrofit2.Call<Client> call, Throwable t) {

                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onFailure(retrofit2.Call<Client> call, Throwable t) {
                                    Toast.makeText(view.getContext(), "Проверьте интернет соединение", LENGTH_LONG).show();

                                }
                            });

                        } else {
                            Toast.makeText(getContext(), "Не удалось получить местоположение", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void uploadImageToSupabase(File file) {
        String filename = System.currentTimeMillis() + "_" + file.getName();
        String path = "uploads/" + filename;

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", filename, RequestBody.create(file, MediaType.parse("image/*")))
                .build();

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/storage/v1/object/" + SUPABASE_BUCKET + "/" + path)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .addHeader("x-upsert", "true")
                .post(requestBody)
                .build();
        Log.e(TAG, "reqist 4");

        client.newCall(request).enqueue(new Callback() {
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(TAG, "Ошибка загрузки: " + e.getMessage(), e);
            }

            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "Код ответа: " + response.code());
                if (response.isSuccessful()) {
                    publicUrl = SUPABASE_URL + "/storage/v1/object/public/" + SUPABASE_BUCKET + "/" + path;
                    requireActivity().runOnUiThread(() -> {
                        photoEvent.setBackground(null);
                        addPhoto.setBackground(null);
                        Picasso.get()
                                .load(publicUrl)
                                .into(photoEvent);
                    });
                } else {
                    Log.e(TAG, "Ошибка ответа: " + response.message());
                    Log.e(TAG, "Тело ответа: " + response.body().string());
                }
            }
        });
    }


}