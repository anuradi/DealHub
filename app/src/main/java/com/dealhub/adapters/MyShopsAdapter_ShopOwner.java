package com.dealhub.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.dealhub.R;
import com.dealhub.models.MyShops;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyShopsAdapter_ShopOwner extends RecyclerView.Adapter<MyShopsAdapter_ShopOwner.ViewHolder>{

    private ArrayList myshops;
    private Context context;

    public MyShopsAdapter_ShopOwner( Context context) {
        myshops=new ArrayList();
        this.context = context;
    }

    public void loadMyShops(ArrayList output) {
        this.myshops = output;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myshop_adapter, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyShops ms= (MyShops) myshops.get(position);
        Picasso.get().load(ms.getLogourl()).into(holder.shoplogo);
        holder.shopname.setText(ms.getShopname());
        Picasso.get().load(ms.getShopimageurl()).into(holder.shopimage);
        holder.shopaddress.setText(ms.getAddress());
        holder.email.setText(ms.getEmail());
        holder.contactno.setText(ms.getContactno());
        holder.status.setText(ms.getStatus());
        if (ms.getStatus().equals("Need Update")){
            holder.updatebtn.setVisibility(View.VISIBLE);
            holder.updatebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Need to be updated", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return myshops.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView shoplogo;
        AppCompatTextView shopname;
        AppCompatImageView shopimage;
        TextView shopaddress,email,contactno,status;
        AppCompatButton updatebtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shoplogo=itemView.findViewById(R.id.shop_logo);
            shopname=itemView.findViewById(R.id.shop_name);
            shopimage=itemView.findViewById(R.id.shop_image);
            shopaddress=itemView.findViewById(R.id.shop_address);
            email=itemView.findViewById(R.id.email);
            contactno=itemView.findViewById(R.id.contact_no);
            status=itemView.findViewById(R.id.status);
            updatebtn=itemView.findViewById(R.id.updatebtn);

        }
    }

}
