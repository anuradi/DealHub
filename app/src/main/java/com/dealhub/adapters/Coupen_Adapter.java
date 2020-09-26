package com.dealhub.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dealhub.R;
import com.dealhub.models.MyCoupens;
import com.dealhub.models.MyOffers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Coupen_Adapter extends RecyclerView.Adapter<Coupen_Adapter.ViewHolder> {

    private ArrayList coupens;
    private Context context;

    public Coupen_Adapter(Context context) {
        coupens = new ArrayList();
        this.context = context;
    }

    public void loadCoupens(ArrayList output) {
        this.coupens = output;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_coupon_adapter, parent, false);
        Coupen_Adapter.ViewHolder vh = new Coupen_Adapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyCoupens myCoupens = (MyCoupens) coupens.get(position);
        holder.date.setText(myCoupens.getCrrdate().split(" ")[0]);
        holder.time.setText(myCoupens.getCrrdate().split(" ")[1]);
        holder.shpname.setText(myCoupens.getShopname());
        holder.count.setText(myCoupens.getCount());
        holder.mobile.setText(myCoupens.getPhone());
    }

    @Override
    public int getItemCount() {
        return coupens.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView date,time,shpname,count,mobile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.coup_date);
            time=itemView.findViewById(R.id.coup_time);
            shpname=itemView.findViewById(R.id.coup_shopname);
            count=itemView.findViewById(R.id.coupen_count);
            mobile=itemView.findViewById(R.id.coup_mobile);
        }
    }
}