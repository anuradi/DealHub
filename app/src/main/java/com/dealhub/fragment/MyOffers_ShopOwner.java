package com.dealhub.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dealhub.R;
import com.dealhub.adapters.MyOffersAdapter_ShopOwner;
import com.dealhub.adapters.MyShopsAdapter_ShopOwner;
import com.dealhub.models.MyOffers;
import com.dealhub.models.MyShops;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class MyOffers_ShopOwner extends Fragment {

    ArrayList<MyOffers> myoffers;

    FirebaseUser firebaseUser;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    DatabaseReference shopReference;
    MyOffersAdapter_ShopOwner adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_offers_shop_owner, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Offers").child(firebaseUser.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference("Offers").child(firebaseUser.getUid());
        shopReference = FirebaseDatabase.getInstance().getReference("Shops").child(firebaseUser.getUid());
        myoffers=new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.myofferdata);
        FragmentManager fragmentManager = getFragmentManager();
        adapter=new MyOffersAdapter_ShopOwner(getActivity(),fragmentManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    MyOffers myOffers_shopOwner = snap.getValue(MyOffers.class);
                    myoffers.add(myOffers_shopOwner);
                }
                adapter.loadMyOffers(myoffers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}