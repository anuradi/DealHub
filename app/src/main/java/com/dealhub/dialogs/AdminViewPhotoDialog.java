package com.dealhub.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dealhub.R;
import com.dealhub.adapters.CommentsAdapter;
import com.dealhub.models.Comments;
import com.dealhub.models.ShopOwners;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminViewPhotoDialog extends DialogFragment {
    AppCompatImageView img;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        Bundle bundle = getArguments();
        final String shopimage = bundle.getString("shopimage");
        final String brphoto = bundle.getString("brphoto");
        final String nicphoto = bundle.getString("nicphoto");
        final String shoplogo = bundle.getString("shoplogo");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.shop_photo_view_dialog, null);
        if (shopimage != "") {
            view = LayoutInflater.from(getContext()).inflate(R.layout.shop_photo_view_dialog, null);
            img = view.findViewById(R.id.shop_photo);
            Picasso.get().load(shopimage).into(img);
        } else if (brphoto != "") {
            view = LayoutInflater.from(getContext()).inflate(R.layout.business_registration_photo_view_dialog, null);
            img = view.findViewById(R.id.br_pic);
            Picasso.get().load(brphoto).into(img);
        } else if (nicphoto != "") {
            view = LayoutInflater.from(getContext()).inflate(R.layout.shopowner_idphoto_view_dialog, null);
            img = view.findViewById(R.id.shop_owner_id);
            Picasso.get().load(nicphoto).into(img);
        } else if (shoplogo != "") {
            view = LayoutInflater.from(getContext()).inflate(R.layout.shop_logo_photo_view_dialog, null);
            img = view.findViewById(R.id.shop_logo_pic);
            Picasso.get().load(shoplogo).into(img);
        }


        alert.setView(view);

        final AlertDialog alertDialog = alert.create();
//        if (shopimage!=""){
//
//        }else if(brphoto!=""){
//
//        }else if (nicphoto!=""){
//
//        }else if (shoplogo!=""){
//
//        }

        return alertDialog;
    }
}
