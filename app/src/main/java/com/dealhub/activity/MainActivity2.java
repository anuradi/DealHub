package com.dealhub.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.dealhub.R;
import com.dealhub.fragment.AddMyOffers_ShopOwner;
import com.dealhub.fragment.AddShops_ShopOwner;
import com.dealhub.fragment.Cart_Admin;
import com.dealhub.fragment.MyOffers_ShopOwner;
import com.dealhub.fragment.MyShops_ShopOwner;
import com.dealhub.fragment.Notifications_ShopOwner;
import com.dealhub.fragment.Profile_ShopOwner;
import com.dealhub.fragment.favorite_customer;
import com.dealhub.fragment.offers_customer;
import com.dealhub.fragment.profile_customer;
import com.dealhub.fragment.shops_customer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theartofdev.edmodo.cropper.CropImage;

public class MainActivity2 extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        id = getIntent().getStringExtra("id");
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.nav_my_shop:
                    selectedFragment = new MyShops_ShopOwner();
                    break;

                case R.id.nav_my_offers:
                    selectedFragment = new MyOffers_ShopOwner();
                    break;

                case R.id.nav_add:
                    AlertDialog alertDialog =new AlertDialog.Builder(MainActivity2.this).create();
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Add Shop", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedFragment = new AddShops_ShopOwner();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            dialog.dismiss();

                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Offer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedFragment = new AddMyOffers_ShopOwner();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();

                    break;

                case R.id.nav_profile:
                    selectedFragment = new Profile_ShopOwner();
                    break;


                case R.id.nav_notifications:
                    selectedFragment = new Notifications_ShopOwner();
                    break;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        }


    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}