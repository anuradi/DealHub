package com.dealhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dealhub.R;
import com.dealhub.models.ShopNotifications;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopOwner_NotificationAdapter extends RecyclerView.Adapter<ShopOwner_NotificationAdapter.ViewHolder>{

    private ArrayList shopnotifications;
    private Context context;
    private FragmentManager fragmentManager;

    public ShopOwner_NotificationAdapter(Context context, FragmentManager fragmentManager) {
        shopnotifications=new ArrayList();
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public void loadNotification(ArrayList output) {
        this.shopnotifications = output;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_notification_adapter, parent, false);
        ShopOwner_NotificationAdapter.ViewHolder vh = new ShopOwner_NotificationAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ShopNotifications not = (ShopNotifications) shopnotifications.get(position);
        Picasso.get().load(not.getImgurl()).into(holder.shopimage);
        holder.description.setText(not.getDescription());
    }

    @Override
    public int getItemCount() {
        return shopnotifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView description;
        AppCompatImageView shopimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopimage=itemView.findViewById(R.id.shop_image);
            description=itemView.findViewById(R.id.description);
        }
    }
}
