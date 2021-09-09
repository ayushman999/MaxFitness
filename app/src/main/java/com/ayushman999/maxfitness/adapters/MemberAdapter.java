package com.ayushman999.maxfitness.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayushman999.maxfitness.R;
import com.ayushman999.maxfitness.model.UserCheck;

import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberHolder> {
    ArrayList<UserCheck> data;
    Context context;

    public MemberAdapter(ArrayList<UserCheck> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView= LayoutInflater.from(context).inflate(R.layout.attendance_element,parent,false);
        MemberHolder holder=new MemberHolder(myView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.MemberHolder holder, int position) {
        holder.uniqueId.setText(data.get(position).getUniqueID());
        holder.name.setText(data.get(position).getName());
        holder.checkIn.setText(data.get(position).getCheckIn());
        holder.checkOut.setText(data.get(position).getCheckOut());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MemberHolder extends RecyclerView.ViewHolder {
        TextView uniqueId,name,checkIn,checkOut;
        public MemberHolder(@NonNull View itemView) {
            super(itemView);
            uniqueId=(TextView) itemView.findViewById(R.id.member_id);
            name=(TextView) itemView.findViewById(R.id.memeber_name);
            checkIn=(TextView) itemView.findViewById(R.id.member_checkIn);
            checkOut=(TextView) itemView.findViewById(R.id.member_ceckOut);

        }
    }
}
