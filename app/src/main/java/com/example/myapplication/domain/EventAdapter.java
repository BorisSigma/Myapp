package com.example.myapplication.domain;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.db.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{


    private LayoutInflater inflater;
    private ArrayList<Event> events;
    private DatabaseHelper databaseHelper;
    private Context context;

    private AdapterView.OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = (AdapterView.OnItemClickListener) listener;
    }
    public EventAdapter(Context context, ArrayList<Event> events) {
        this.context = context;

        this.events = events;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_adapt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Event event = events.get(position);

        databaseHelper = new DatabaseHelper(context);
        holder.time.setText(databaseHelper.getEventTimestampById(event.getId()));
        holder.name.setText(event.getEventName());
        Picasso.get().load(databaseHelper.getEventUrlById(event.getId())).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                Event event = events.get(position);
                long event_id = event.getId();
                Activity activity = (Activity) context;
                SharedPreferences sharedPreferences = activity.getSharedPreferences("id_user", Context.MODE_PRIVATE);
                long id = sharedPreferences.getLong("user_id", -1);
                Bundle bundle = new Bundle();
                bundle.putLong("id_user", id);
                bundle.putLong("id_event", event_id);
                bundle.putInt("exit", 1);
                navController.navigate(R.id.eventcheck, bundle);
            }
        });


    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView name;
        ImageView image;

        TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.textView7);
            image = itemView.findViewById(R.id.imageView4);
            time = itemView.findViewById(R.id.time_lst);

        }
    }
}
