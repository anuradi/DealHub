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
import com.dealhub.adapters.MyFavouritesAdapter_Customer;
import com.dealhub.models.Favourites;
import com.dealhub.models.MyOffers;
import com.dealhub.models.MyShops;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class favorite_customer extends Fragment {
    ArrayList<MyOffers> offers;
    ArrayList<MyOffers> offersfinal;
    ArrayList<Favourites> favouritelist;

    FirebaseUser firebaseUser;
    DatabaseReference favouriteReference;
    DatabaseReference offerReference;
    MyFavouritesAdapter_Customer adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        favouriteReference = FirebaseDatabase.getInstance().getReference("Favourites");
        offerReference = FirebaseDatabase.getInstance().getReference("Offers");
        offers = new ArrayList<>();
        offersfinal = new ArrayList<>();
        favouritelist = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.favouritedata);
        FragmentManager fragmentManager = getFragmentManager();
        adapter = new MyFavouritesAdapter_Customer(getActivity(), fragmentManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        favouriteReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favouritelist.clear();
                for (DataSnapshot snap1:dataSnapshot.getChildren()){
                    for (DataSnapshot snap2:snap1.getChildren()){

                        for (DataSnapshot snap3:snap2.getChildren()){
                            Favourites fav=new Favourites();
                            fav.setShopname(snap3.getKey());
                            for (DataSnapshot snap4:snap3.getChildren()){
                                fav.setOfferid(snap4.getKey());
                                favouritelist.add(fav);
                            }
                        }
                    }
                }
                showFavourites();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void showFavourites() {
        offerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                offers.clear();
                for (DataSnapshot snap1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snap2 : snap1.getChildren()) {
                        final MyOffers offers_shopOwner = snap2.getValue(MyOffers.class);
                        for (Favourites fav:favouritelist){
                            if (offers_shopOwner.getShopname().equals(fav.getShopname()) && fav.getOfferid().equals(String.valueOf(offers_shopOwner.getOfferid()))) {
                                offers.add(offers_shopOwner);
                            }
                        }

                    }
                }
                for (final MyOffers off:offers){
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
                                        offersfinal.add(off);
                                    }
                                }
                            }
                            adapter.loadMyFavourite(offersfinal);
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