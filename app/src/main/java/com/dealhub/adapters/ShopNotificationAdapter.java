package com.dealhub.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dealhub.R;
import com.dealhub.dialogs.CommentDialog;
import com.dealhub.dialogs.SampleDialog;
import com.dealhub.dialogs.UpdateNotificationDialog;
import com.dealhub.models.MyOffers;
import com.dealhub.models.ShopNotifications;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShopNotificationAdapter extends RecyclerView.Adapter<ShopNotificationAdapter.ViewHolder>{

    private ArrayList shopnotifications;
    private Context context;
    private FragmentManager fragmentManager;

    public ShopNotificationAdapter(Context context, FragmentManager fragmentManager) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_notification_admin_adapter, parent, false);
        ShopNotificationAdapter.ViewHolder vh = new ShopNotificationAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ShopNotifications not = (ShopNotifications) shopnotifications.get(position);
        Picasso.get().load(not.getImgurl()).into(holder.shopimage);
        holder.description.setText(not.getDescription());
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("id", "" + not.getId());
                bundle.putString("img_url", "" + not.getImgurl());
                bundle.putString("description", "" + not.getDescription());
                UpdateNotificationDialog updtDialog = new UpdateNotificationDialog();
                updtDialog.setArguments(bundle);
                updtDialog.show(fragmentManager, "update_dialog");
            }
        });
        
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("offer", "");
                bundle.putString("notification", "" + not.getId());
                SampleDialog smpDialog = new SampleDialog();
                smpDialog.setArguments(bundle);
                smpDialog.show(fragmentManager, "sure_dialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopnotifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView description;
        AppCompatImageView shopimage;
        AppCompatButton update,delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopimage=itemView.findViewById(R.id.shop_image);
            description=itemView.findViewById(R.id.description);
            update=itemView.findViewById(R.id.Update);
            delete=itemView.findViewById(R.id.Delete);
        }
    }
}
