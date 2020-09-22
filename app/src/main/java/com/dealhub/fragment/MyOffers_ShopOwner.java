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
    ArrayList<MyOffers> offersfinal;
    ArrayList<String> shopnamelist;

    FirebaseUser firebaseUser;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    DatabaseReference shopReference;
    DatabaseReference offerReference;
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
        offerReference = FirebaseDatabase.getInstance().getReference("Offers");
        myoffers=new ArrayList<>();
        offersfinal = new ArrayList<>();
        shopnamelist = new ArrayList<>();
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
//                    myoffers.add(myOffers_shopOwner);
                    shopnamelist.add(myOffers_shopOwner.getShopname());
                }
                showOffers();
//                adapter.loadMyOffers(myoffers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void showOffers() {
        offerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myoffers.clear();
                for (DataSnapshot snap1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snap2 : snap1.getChildren()) {
                        final MyOffers offers_shopOwner = snap2.getValue(MyOffers.class);
                        for(String shpname:shopnamelist){
                            if (offers_shopOwner.getShopname().equals(shpname)) {

                                myoffers.add(offers_shopOwner);
                            }
                        }
                    }
                }
                for (final MyOffers off:myoffers){
                    offersfinal.clear();
                    DatabaseReference shops = FirebaseDatabase.getInstance().getReference("Shops");
                    shops.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snp1:dataSnapshot.getChildren()){
                                for(DataSnapshot snp2:snp1.getChildren()){
                                    MyShops msp=snp2.getValue(MyShops.class);
                                    if (off.getShopname().equals(msp.getShopname())) {
                                        off.setShoplogourl(msp.getLogourl());
                                        boolean exist=false;
                                        for(MyOffers finalo:offersfinal){
                                            if (finalo.getOfferid()==off.getOfferid()){
                                                exist=true;
                                            }
                                        }
                                        if (!exist){
                                            offersfinal.add(off);
                                        }
                                    }
                                }
                            }
                            adapter.loadMyOffers(offersfinal);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}