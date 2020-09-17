package com.dealhub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dealhub.R;

public class signup1 extends AppCompatActivity {

    TextView getdealtxt,givedealtxt;
    ImageView getdealimg,givedealimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);
        getdealtxt=findViewById(R.id.getdeals);
        givedealtxt=findViewById(R.id.givedeals);
        getdealimg=findViewById(R.id.imageView3);
        givedealimg=findViewById(R.id.imageView5);

        getdealimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signup1.this, RegisterCustomer.class));
                finish();
            }
        });

        getdealtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signup1.this, RegisterCustomer.class));
                finish();
            }
        });

        givedealimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signup1.this, RegisterShopOwner.class));
                finish();
            }
        });

        givedealtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signup1.this, RegisterShopOwner.class));
                finish();
            }
        });
    }
}