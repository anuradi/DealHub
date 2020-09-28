package com.dealhub.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dealhub.R;
import com.dealhub.dialogs.AdminViewPhotoDialog;
import com.dealhub.dialogs.UpdateNotificationDialog;
import com.dealhub.fragment.admin_approval;
import com.dealhub.models.MyShops;
import com.dealhub.models.ShopNotifications;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminApprovalAdapter extends RecyclerView.Adapter<AdminApprovalAdapter.ViewHolder> {

    private ArrayList shops;
    private Context context;
    private FragmentManager fragmentManager;
    DatabaseReference databaseReference;

    public AdminApprovalAdapter( Context context, FragmentManager fragmentManager) {
        shops=new ArrayList();
        this.context = context;
        this.fragmentManager = fragmentManager;
        databaseReference = FirebaseDatabase.getInstance().getReference("Shops");
    }

    public void loadPendingShops(ArrayList output) {
        this.shops = output;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_approval_adapter, parent, false);
        AdminApprovalAdapter.ViewHolder vh = new AdminApprovalAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyShops shoplist = (MyShops) shops.get(position);
        holder.shopimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("shopimage", "" +shoplist.getShopimageurl());
                bundle.putString("nicphoto", "" );
                bundle.putString("brphoto", "" );
                bundle.putString("shoplogo", "" );
                AdminViewPhotoDialog updtDialog = new AdminViewPhotoDialog();
                updtDialog.setArguments(bundle);
                updtDialog.show(fragmentManager, "update_dialog");
            }
        });
        holder.brphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("shopimage", "" );
                bundle.putString("nicphoto", "" );
                bundle.putString("brphoto", "" +shoplist.getBrurl());
                bundle.putString("shoplogo", "" );
                AdminViewPhotoDialog updtDialog = new AdminViewPhotoDialog();
                updtDialog.setArguments(bundle);
                updtDialog.show(fragmentManager, "update_dialog");
            }
        });
        holder.nicphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("shopimage", "" );
                bundle.putString("nicphoto", "" +shoplist.getNicurl());
                bundle.putString("brphoto", "" );
                bundle.putString("shoplogo", "" );
                AdminViewPhotoDialog updtDialog = new AdminViewPhotoDialog();
                updtDialog.setArguments(bundle);
                updtDialog.show(fragmentManager, "update_dialog");
            }
        });
        holder.shoplogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("shopimage", "" );
                bundle.putString("nicphoto", "" );
                bundle.putString("brphoto", "" );
                bundle.putString("shoplogo", "" +shoplist.getLogourl());
                AdminViewPhotoDialog updtDialog = new AdminViewPhotoDialog();
                updtDialog.setArguments(bundle);
                updtDialog.show(fragmentManager, "update_dialog");
            }
        });
        holder.shopname.setText(shoplist.getShopname());
        holder.shopaddress.setText(shoplist.getAddress());
        holder.brno.setText(shoplist.getShopbrno());
        holder.email.setText(shoplist.getEmail());
        holder.contactno.setText(shoplist.getContactno());
        holder.nicno.setText(shoplist.getOwnernic());
        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap1:dataSnapshot.getChildren()){
                            for (DataSnapshot snap2:snap1.getChildren()){
                                System.out.println(snap2);
                                if (Integer.parseInt(snap2.getKey())==shoplist.getShopid()){
                                    MyShops shp=snap2.getValue(MyShops.class);
                                    if (shp.getShopimageurl().equals(shoplist.getShopimageurl())){
                                        HashMap<String, Object> hashMap = new HashMap<>();

                                        hashMap.put("status", "Active");
                                        snap2.getRef().updateChildren(hashMap);
                                        fragmentManager.beginTransaction().replace(R.id.fragment_container, new admin_approval()).commit();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap1:dataSnapshot.getChildren()){
                            for (DataSnapshot snap2:snap1.getChildren()){
                                System.out.println(snap2);
                                if (Integer.parseInt(snap2.getKey())==shoplist.getShopid()){
                                    MyShops shp=snap2.getValue(MyShops.class);
                                    if (shp.getShopimageurl().equals(shoplist.getShopimageurl())){
                                        snap2.getRef().setValue(null);
                                        fragmentManager.beginTransaction().replace(R.id.fragment_container, new admin_approval()).commit();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatEditText shopimage,nicphoto,brphoto,shoplogo,shopname,shopaddress,brno,email,contactno,nicno;
        AppCompatButton approve,delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopimage=itemView.findViewById(R.id.shop_image);
            nicphoto=itemView.findViewById(R.id.shop_owner_id_photo);
            brphoto=itemView.findViewById(R.id.br_photo);
            shoplogo=itemView.findViewById(R.id.shop_logo);
            shopname=itemView.findViewById(R.id.shop_name);
            shopaddress=itemView.findViewById(R.id.shop_address);
            brno=itemView.findViewById(R.id.br_no);
            email=itemView.findViewById(R.id.email);
            contactno=itemView.findViewById(R.id.contact_no);
            nicno=itemView.findViewById(R.id.shop_owner_id);
            approve=itemView.findViewById(R.id.Approve);
            delete=itemView.findViewById(R.id.Delete);

        }
    }
}
