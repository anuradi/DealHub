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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dealhub.R;
import com.dealhub.dialogs.CommentDialog;
import com.dealhub.models.MyOffers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class OffersAdapter_Customer extends RecyclerView.Adapter<OffersAdapter_Customer.ViewHolder> {
    private ArrayList offers;
    private Context context;
    private FragmentManager fragmentManager;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    public OffersAdapter_Customer(Context context, FragmentManager fragmentManager) {
        offers = new ArrayList();
        this.context = context;
        this.fragmentManager = fragmentManager;
        databaseReference = FirebaseDatabase.getInstance().getReference("Offers");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void loadOffers(ArrayList output) {
        this.offers = output;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_adapter, parent, false);
        OffersAdapter_Customer.ViewHolder vh = new OffersAdapter_Customer.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MyOffers ofr = (MyOffers) offers.get(position);
        Picasso.get().load(ofr.getShoplogourl()).into(holder.shoplogo);
        holder.shopname.setText(ofr.getShopname());
        Picasso.get().load(ofr.getOfferimageurl()).into(holder.shopimage);
        holder.likes.setText(ofr.getLikes() + " Likes");
        holder.description.setText(ofr.getOfferdescription());
        holder.price.setText("Price: " + ofr.getOfferprice() + " | Discount: " + ofr.getOfferdiscount() + " | Expiration Date: " + ofr.getExpdate());
        DatabaseReference comments = FirebaseDatabase.getInstance().getReference("Comments").child(ofr.getShopname()).child("" + ofr.getOfferid());
        comments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    int commentcount = 0;
                    commentcount = (int) dataSnapshot.getChildrenCount();
                    holder.comments.setText("View All " + commentcount + " Comments");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("offer", "" + ofr.getOfferid());
                bundle.putString("shopname", "" + ofr.getShopname());
                bundle.putString("login", "" + "customer");
                CommentDialog cmntDialog = new CommentDialog();
                cmntDialog.setArguments(bundle);
                cmntDialog.show(fragmentManager, "comment_dialog");
            }
        });
        DatabaseReference following = FirebaseDatabase.getInstance().getReference().child("Likes").child(firebaseUser.getUid()).child(ofr.getShopname()).child(""+ofr.getOfferid());
        following.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    holder.like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_gray));
                    holder.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(firebaseUser.getUid()).child(ofr.getShopname()).child("" + ofr.getOfferid()).setValue(true);
                            holder.like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_red));
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String uid = "";
                                    String oid = "";
                                    for (DataSnapshot snap1 : dataSnapshot.getChildren()) {
                                        uid = snap1.getKey();
                                        for (DataSnapshot snap2 : snap1.getChildren()) {
                                            oid = snap2.getKey();
                                            final MyOffers offers_shopOwner = snap2.getValue(MyOffers.class);
                                            if (offers_shopOwner.getShopname().equals(ofr.getShopname()) && offers_shopOwner.getOfferid()==ofr.getOfferid()) {
                                                int likesoffer = offers_shopOwner.getLikes();
                                                likesoffer++;
                                                HashMap<String, Object> hashMap = new HashMap<>();

                                                hashMap.put("likes", likesoffer);
                                                databaseReference.child(uid).child(oid).updateChildren(hashMap);
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
                } else {
                    holder.like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_red));
                    holder.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(firebaseUser.getUid()).child(ofr.getShopname()).child("" + ofr.getOfferid()).setValue(null);
                            holder.like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_gray));
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String uid = "";
                                    String oid = "";
                                    for (DataSnapshot snap1 : dataSnapshot.getChildren()) {
                                        uid = snap1.getKey();
                                        for (DataSnapshot snap2 : snap1.getChildren()) {
                                            oid = snap2.getKey();
                                            final MyOffers offers_shopOwner = snap2.getValue(MyOffers.class);
                                            if (offers_shopOwner.getShopname().equals(ofr.getShopname()) && offers_shopOwner.getOfferid()==ofr.getOfferid()) {
                                                int likesoffer = offers_shopOwner.getLikes();
                                                likesoffer--;
                                                HashMap<String, Object> hashMap = new HashMap<>();

                                                hashMap.put("likes", likesoffer);
                                                databaseReference.child(uid).child(oid).updateChildren(hashMap);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("offer", "" + ofr.getOfferid());
                bundle.putString("shopname", "" + ofr.getShopname());
                bundle.putString("login", "" + "customer");
                CommentDialog cmntDialog = new CommentDialog();
                cmntDialog.setArguments(bundle);
                cmntDialog.show(fragmentManager, "comment_dialog");
            }
        });


        DatabaseReference favourites = FirebaseDatabase.getInstance().getReference().child("Favourites").child(firebaseUser.getUid()).child(ofr.getShopname()).child(""+ofr.getOfferid());
        favourites.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    holder.save.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_favorite_gray));
                    holder.save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference().child("Favourites").child(firebaseUser.getUid()).child(ofr.getShopname()).child("" + ofr.getOfferid()).setValue(true);
                            holder.save.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_star));
                        }
                    });
                } else {
                    holder.save.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.favorite_star));
                    holder.save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference().child("Favourites").child(firebaseUser.getUid()).child(ofr.getShopname()).child("" + ofr.getOfferid()).setValue(null);
                            holder.save.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.star_favorite_gray));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference cart = FirebaseDatabase.getInstance().getReference().child("Cart").child(firebaseUser.getUid()).child(ofr.getShopname()).child("" + ofr.getOfferid());
                cart.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()==null){
                            cart.child("count").setValue(1);
                        }else{
                            Toast.makeText(context, "This offer is already in the cart", Toast.LENGTH_SHORT).show();
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
        return offers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView shoplogo;
        TextView shopname;
        AppCompatImageView shopimage;
        TextView likes, description, price, comments;
        ImageView like, comment, save;
        AppCompatButton addtocart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shoplogo = itemView.findViewById(R.id.shop_logo);
            shopname = itemView.findViewById(R.id.shop_name);
            shopimage = itemView.findViewById(R.id.shop_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            addtocart = itemView.findViewById(R.id.add_to_cart);
            likes = itemView.findViewById(R.id.likes);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            comments = itemView.findViewById(R.id.comments_);

        }
    }
}
