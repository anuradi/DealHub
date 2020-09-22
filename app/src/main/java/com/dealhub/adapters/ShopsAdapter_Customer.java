package com.dealhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dealhub.R;
import com.dealhub.models.MyShops;
import com.dealhub.notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShopsAdapter_Customer extends RecyclerView.Adapter<ShopsAdapter_Customer.ViewHolder>{
    private ArrayList shops;
    private Context context;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    public ShopsAdapter_Customer( Context context) {
        shops=new ArrayList();
        this.context = context;
    }

    public void loadShops(ArrayList output) {
        this.shops = output;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shops_adapter, parent, false);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference().child("Follow");
        ShopsAdapter_Customer.ViewHolder vh = new ShopsAdapter_Customer.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MyShops shp= (MyShops) shops.get(position);
        Picasso.get().load(shp.getLogourl()).into(holder.shoplogo);
        holder.shopname.setText(shp.getShopname());
        Picasso.get().load(shp.getShopimageurl()).into(holder.shopimage);
        holder.shopaddress.setText(shp.getAddress());
        DatabaseReference following = databaseReference.child(firebaseUser.getUid()).child("Following").child(shp.getShopname());
        following.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()==null){
                    holder.subscribe.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.subscribe));
                    holder.subscribe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            databaseReference.child(firebaseUser.getUid()).child("Following").child(shp.getShopname()).setValue(true);
                            holder.subscribe.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.unsub));
                            updateToken(FirebaseInstanceId.getInstance().getToken());
                        }
                    });
                }else{
                    holder.subscribe.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.unsub));
                    holder.subscribe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            databaseReference.child(firebaseUser.getUid()).child("Following").child(shp.getShopname()).setValue(null);
                            holder.subscribe.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.subscribe));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView shoplogo;
        AppCompatTextView shopname;
        AppCompatImageView shopimage;
        TextView shopaddress;
        ImageView subscribe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shoplogo=itemView.findViewById(R.id.shop_logo);
            shopname=itemView.findViewById(R.id.shop_name);
            shopimage=itemView.findViewById(R.id.shop_image);
            shopaddress=itemView.findViewById(R.id.shop_address);
            subscribe=itemView.findViewById(R.id.subcribe);
        }
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }
}
