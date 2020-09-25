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
import com.dealhub.dialogs.CommentDialog;
import com.dealhub.dialogs.CompoundDialog;
import com.dealhub.models.MyCart;
import com.dealhub.models.MyOffers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.ViewHolder> {

    private ArrayList cart;
    private Context context;
    private FragmentManager fragmentManager;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    public Cart_Adapter(Context context, FragmentManager fragmentManager) {
        cart = new ArrayList();
        this.context = context;
        this.fragmentManager = fragmentManager;
        databaseReference = FirebaseDatabase.getInstance().getReference("Offers");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void loadCart(ArrayList output) {
        this.cart = output;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cart_adapter, parent, false);
        Cart_Adapter.ViewHolder vh = new Cart_Adapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MyOffers ofr = (MyOffers) cart.get(position);
        Picasso.get().load(ofr.getShoplogourl()).into(holder.shoplogo);
        holder.shopname.setText(ofr.getShopname());
        Picasso.get().load(ofr.getOfferimageurl()).into(holder.shopimage);
        holder.likes.setText(ofr.getLikes() + " Likes");
        holder.description.setText(ofr.getOfferdescription());
        holder.price.setText("Price: " + ofr.getOfferprice() + " | Discount: " + ofr.getOfferdiscount() + " | Expiration Date: " + ofr.getExpdate());
        final DatabaseReference cartitem = FirebaseDatabase.getInstance().getReference("Cart").child(firebaseUser.getUid()).child(ofr.getShopname()).child("" + ofr.getOfferid());
        cartitem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    holder.count.setText(snap.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(holder.count.getText().toString());
                count++;
                holder.count.setText("" + count);
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(holder.count.getText().toString());
                if (count != 1) {
                    count--;
                }
                holder.count.setText("" + count);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartitem.setValue(null);
            }
        });

        holder.compound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("offer", "" + ofr.getOfferid());
                bundle.putString("shopname", "" + ofr.getShopname());
                bundle.putString("login", "" + "customer");
                CompoundDialog cmntDialog = new CompoundDialog();
                cmntDialog.setArguments(bundle);
                cmntDialog.show(fragmentManager, "comment_dialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return cart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView shoplogo;
        TextView shopname;
        AppCompatImageView shopimage;
        TextView likes, description, price;
        ImageView plus, minus, delete;
        AppCompatTextView count;
        AppCompatButton compound;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shoplogo = itemView.findViewById(R.id.shop_logo);
            shopname = itemView.findViewById(R.id.shop_name);
            shopimage = itemView.findViewById(R.id.shop_image);
            plus = itemView.findViewById(R.id.add);
            minus = itemView.findViewById(R.id.min);
            count = itemView.findViewById(R.id.count);
            delete = itemView.findViewById(R.id.delete);
            likes = itemView.findViewById(R.id.likes);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            compound = itemView.findViewById(R.id.compound);

        }
    }
}