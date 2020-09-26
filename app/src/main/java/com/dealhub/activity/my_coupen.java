package com.dealhub.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dealhub.R;
import com.dealhub.adapters.Coupen_Adapter;
import com.dealhub.adapters.MyFavouritesAdapter_Customer;
import com.dealhub.models.Favourites;
import com.dealhub.models.MyCoupens;
import com.dealhub.models.MyOffers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class my_coupen extends AppCompatActivity {
    ArrayList<MyCoupens> coupens;

    FirebaseUser firebaseUser;
    DatabaseReference coupenReference;
    Coupen_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        coupenReference = FirebaseDatabase.getInstance().getReference("Coupens").child(firebaseUser.getUid());

        coupens=new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.mycoupendata);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new Coupen_Adapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        coupenReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                coupens.clear();
                for (DataSnapshot snap1 : dataSnapshot.getChildren()) {
                    for (DataSnapshot snap2 : snap1.getChildren()) {
                        MyCoupens myc = snap2.getValue(MyCoupens.class);
                        myc.setShopname(snap1.getKey());
                        coupens.add(myc);
                    }
                }
                adapter.loadCoupens(coupens);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}