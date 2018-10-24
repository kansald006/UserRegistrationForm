package com.example.deepak.userregistrationform;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    RecyclerAdapter recyclerAdapter;
    int resource;
    ArrayList<User> objects;
    CustomItemClickListner listener;

    public RecyclerAdapter(Context context, int resource, ArrayList<User> objects) {

        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    public void registerCustomitemclicklistner(CustomItemClickListner listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(resource,parent, false);
       final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,holder.getPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        User user = (User)objects.get(pos);

        holder.txtName.setText(user.name);
        holder.txtEmail.setText(user.email);
        holder.txtDate.setText(user.CDate);
        holder.txtCity.setText(user.city);

    }


    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void removeAllItems() {
       objects.clear();
        recyclerAdapter.notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtName;
        TextView txtEmail;
        TextView txtDate;
        TextView txtCity;
        TextView txtGender;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.textViewName);
            txtEmail = itemView.findViewById(R.id.textViewEmail);
            txtDate= itemView.findViewById(R.id.textViewDate);
            txtCity= itemView.findViewById(R.id.textViewCity);
            txtGender= itemView.findViewById(R.id.textViewGender);
        }
    }
}