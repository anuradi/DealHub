package com.dealhub.fragment;

import android.content.Context;
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
import com.dealhub.adapters.ShopNotificationAdapter;
import com.dealhub.adapters.ShopOwner_NotificationAdapter;
import com.dealhub.models.ShopNotifications;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notifications_ShopOwner extends Fragment {
    ArrayList<ShopNotifications> notification;

    ShopOwner_NotificationAdapter adapter;

    DatabaseReference notificationReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notificationReference = FirebaseDatabase.getInstance().getReference("Shop Notitfications");
        notification=new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.notificationdata);
        FragmentManager fragmentManager = getFragmentManager();
        adapter = new ShopOwner_NotificationAdapter(getActivity(), fragmentManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notification.clear();
                for (DataSnapshot snap1:dataSnapshot.getChildren()){
                    ShopNotifications not=snap1.getValue(ShopNotifications.class);
                    notification.add(not);
                }
                adapter.loadNotification(notification);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}