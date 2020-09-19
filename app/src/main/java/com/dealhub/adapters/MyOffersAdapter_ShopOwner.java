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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dealhub.R;
import com.dealhub.activity.Edit_Offers;
import com.dealhub.dialogs.CommentDialog;
import com.dealhub.dialogs.SampleDialog;
import com.dealhub.models.MyOffers;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyOffersAdapter_ShopOwner extends RecyclerView.Adapter<MyOffersAdapter_ShopOwner.ViewHolder> {

    private ArrayList myoffers;
    private Context context;
    private FragmentManager fragmentManager;

    public MyOffersAdapter_ShopOwner(Context context, FragmentManager fragmentManager) {
        myoffers = new ArrayList();
        this.context = context;
        this.fragmentManager=fragmentManager;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyOffers ms = (MyOffers) myoffers.get(position);
//        Picasso.get().load(ms.get).into(holder.shoplogo);
        holder.shopname.setText(ms.getShopname());
        Picasso.get().load(ms.getOfferimageurl()).into(holder.shopimage);
        holder.likes.setText(ms.getLikes() + " Likes");
        holder.description.setText(ms.getOfferdescription());
        holder.price.setText("Price: " + ms.getOfferprice() + " | Discount: " + ms.getOfferdiscount() + " | Expiration Date: " + ms.getExpdate());
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("offer", "" + ms.getOfferid());
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
        ImageView delete;
        AppCompatButton edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shoplogo = itemView.findViewById(R.id.shop_logo);
            shopname = itemView.findViewById(R.id.shop_name);
            shopimage = itemView.findViewById(R.id.shop_image);
            likes = itemView.findViewById(R.id.likes);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            comments = itemView.findViewById(R.id.comments_);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.edit);

        }
    }
}
