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
import com.dealhub.adapters.Cart_Adapter;
import com.dealhub.adapters.OffersAdapter_Customer;
import com.dealhub.models.Favourites;
import com.dealhub.models.MyCart;
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


public class Cart_Admin extends Fragment {

    ArrayList<MyOffers> offers;
    ArrayList<MyOffers> offersfinal;
    ArrayList<MyCart> cartlist;

    FirebaseUser firebaseUser;
    DatabaseReference cartReference;
    DatabaseReference offerReference;
    Cart_Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_admin, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        cartReference = FirebaseDatabase.getInstance().getReference("Cart").child(firebaseUser.getUid());
        offerReference = FirebaseDatabase.getInstance().getReference("Offers");
        offers = new ArrayList<>();
        offersfinal = new ArrayList<>();
        cartlist = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.cartdata);
        FragmentManager fragmentManager = getFragmentManager();
        adapter = new Cart_Adapter(getActivity(), fragmentManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartlist.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    for (DataSnapshot snap1:snap.getChildren()){
                        MyCart myCart=new MyCart();
                        myCart.setShopname(snap.getKey());
                        myCart.setOfferid(Integer.parseInt(snap1.getKey()));
                        cartlist.add(myCart);
                    }
                }
                showCart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void showCart() {
        offerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                offers.clear();
                for (DataSnapshot snap1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snap2 : snap1.getChildren()) {
                        final MyOffers offers_shopOwner = snap2.getValue(MyOffers.class);
                        for (MyCart cart : cartlist) {
                            if (offers_shopOwner.getShopname().equals(cart.getShopname()) && cart.getOfferid()==offers_shopOwner.getOfferid()) {
                                offers.add(offers_shopOwner);
                            }
                        }

                    }
                }
                for (final MyOffers off:offers){
                    offersfinal.clear();
                    DatabaseReference shops = FirebaseDatabase.getInstance().getReference("Shops");
                    shops.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            adapter.loadCart(offersfinal);
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