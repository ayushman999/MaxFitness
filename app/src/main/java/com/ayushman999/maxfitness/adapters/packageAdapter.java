package com.ayushman999.maxfitness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.model.GymUser;

import java.util.ArrayList;

public class packageAdapter extends RecyclerView.Adapter<packageAdapter.PackageHolder> {
    ArrayList<GymUser> data;
    Context context;

    public packageAdapter(ArrayList<GymUser> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public PackageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(context).inflate(R.layout.package_elements,parent,false);
        PackageHolder holder=new PackageHolder(myView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull packageAdapter.PackageHolder holder, int position) {
        holder.uniqueID.setText(data.get(position).getUniqueID());
        holder.name.setText(data.get(position).getName());
        holder.date.setText(data.get(position).getEndDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class PackageHolder extends RecyclerView.ViewHolder {
        TextView uniqueID,name,date;
        public PackageHolder(@NonNull View itemView) {
            super(itemView);
            uniqueID=(TextView) itemView.findViewById(R.id.admin_package_uniqueID);
            name=(TextView) itemView.findViewById(R.id.admin_package_name);
            date=(TextView) itemView.findViewById(R.id.admin_package_date);
        }
    }
}
