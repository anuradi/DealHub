package com.dealhub.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dealhub.R;
import com.dealhub.fragment.Cart_Admin;
import com.dealhub.fragment.favorite_customer;
import com.dealhub.fragment.offers_customer;
import com.dealhub.fragment.profile_customer;
import com.dealhub.fragment.shops_customer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    static String id;
    public  Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id = getIntent().getStringExtra("id");
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_shop:
                    selectedFragment = new shops_customer();
                    break;

                case R.id.nav_offers:
                    selectedFragment = new offers_customer();
                    break;

                case R.id.nav_favorite:
                    selectedFragment = new favorite_customer();
                    break;

                case R.id.nav_profile:
                    selectedFragment = new profile_customer();
                    break;


                case R.id.nav_cart:
                    selectedFragment = new Cart_Admin();
                    break;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        }


    };
}