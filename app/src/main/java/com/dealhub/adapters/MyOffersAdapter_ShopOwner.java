package com.dealhub.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.dealhub.activity.Edit_Offers;
import com.dealhub.dialogs.CommentDialog;
import com.dealhub.dialogs.SampleDialog;
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
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyOffersAdapter_ShopOwner extends RecyclerView.Adapter<MyOffersAdapter_ShopOwner.ViewHolder> {

    private ArrayList myoffers;
    private Context context;
    private FragmentManager fragmentManager;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    public MyOffersAdapter_ShopOwner(Context context, FragmentManager fragmentManager) {
        myoffers = new ArrayList();
        this.context = context;
        this.fragmentManager=fragmentManager;
        databaseReference = FirebaseDatabase.getInstance().getReference("Offers");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void loadMyOffers(ArrayList output) {
        this.myoffers = output;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_offers_adapter, parent, false);
        MyOffersAdapter_ShopOwner.ViewHolder vh = new MyOffersAdapter_ShopOwner.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MyOffers ms = (MyOffers) myoffers.get(position);
        Picasso.get().load(ms.getShoplogourl()).into(holder.shoplogo);
        holder.shopname.setText(ms.getShopname());
        Picasso.get().load(ms.getOfferimageurl()).into(holder.shopimage);
        holder.likes.setText(ms.getLikes() + " Likes");
        holder.description.setText(ms.getOfferdescription());
        holder.price.setText("Price: " + ms.getOfferprice() + " | Discount: " + ms.getOfferdiscount() + " | Expiration Date: " + ms.getExpdate());
        DatabaseReference comments = FirebaseDatabase.getInstance().getReference("Comments").child(ms.getShopname()).child("" + ms.getOfferid());
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
                bundle.putString("offer", "" + ms.getOfferid());
                bundle.putString("shopname", "" + ms.getShopname());
                bundle.putString("login", "" + "shopowner");
                CommentDialog cmntDialog = new CommentDialog();
                cmntDialog.setArguments(bundle);
                cmntDialog.show(fragmentManager, "comment_dialog");
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, Edit_Offers.class);
                i.putExtra("offerid","" + ms.getOfferid());
                context.startActivity(i);
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("offer", "" + ms.getOfferid());
                bundle.putString("shopname", "" + ms.getShopname());
                bundle.putString("login", "" + "customer");
                CommentDialog cmntDialog = new CommentDialog();
                cmntDialog.setArguments(bundle);
                cmntDialog.show(fragmentManager, "comment_dialog");
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("offer", "" + ms.getOfferid());
                SampleDialog smpDialog = new SampleDialog();
                smpDialog.setArguments(bundle);
                smpDialog.show(fragmentManager, "sure_dialog");
            }
        });

        DatabaseReference following = FirebaseDatabase.getInstance().getReference().child("Likes").child(firebaseUser.getUid()).child("Following").child(ms.getShopname());
        following.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    holder.like.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_gray));
                    holder.like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(firebaseUser.getUid()).child("Following").child(ms.getShopname()).child("" + ms.getOfferid()).setValue(true);
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
                                            if (offers_shopOwner.getShopname().equals(ms.getShopname())) {
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
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(firebaseUser.getUid()).child("Following").child(ms.getShopname()).child("" + ms.getOfferid()).setValue(null);
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
                                            if (offers_shopOwner.getShopname().equals(ms.getShopname())) {
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

    }

    @Override
    public int getItemCount() {
        return myoffers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView shoplogo;
        AppCompatTextView shopname;
        AppCompatImageView shopimage;
        TextView likes, description, price, comments;
        ImageView like, delete, comment;
        AppCompatButton edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shoplogo = itemView.findViewById(R.id.shop_logo);
            shopname = itemView.findViewById(R.id.shop_name);
            shopimage = itemView.findViewById(R.id.shop_image);
            likes = itemView.findViewById(R.id.likes);
            comment = itemView.findViewById(R.id.comment);
            like = itemView.findViewById(R.id.like);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            comments = itemView.findViewById(R.id.comments_);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);

        }
    }
}
