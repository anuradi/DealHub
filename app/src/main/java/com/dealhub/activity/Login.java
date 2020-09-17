package com.dealhub.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dealhub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText email, password, signup;
    Button login;

    FirebaseUser firebaseUser;
    FirebaseAuth auth;

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (isNetworkConnected()) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                redirectToHome();
            } else {
                setContentView(R.layout.activity_login);
                email = findViewById(R.id.email);
                password = findViewById(R.id.password);
                signup = findViewById(R.id.txt_signup);
                login = findViewById(R.id.login);

                auth = FirebaseAuth.getInstance();

                signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Login.this, signup1.class));
                        finish();
                    }
                });

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str_email = email.getText().toString();
                        String str_password = password.getText().toString();

                        if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)) {
                            Toast.makeText(Login.this, "All fields are required", Toast.LENGTH_SHORT).show();
                        } else {

                            final ProgressDialog pd = new ProgressDialog(Login.this);
                            pd.setCanceledOnTouchOutside(false);

                            auth.signInWithEmailAndPassword(str_email, str_password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                                            redirectToHome();
                                        } else {
                                            Toast.makeText(Login.this, "Please verify your Email.", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                });
            }
        } else {
            Toast.makeText(Login.this, "No Network Connection. Check Your Connection", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    private void redirectToHome() {
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (childSnapshot.child(firebaseUser.getUid()).getValue() != null) {
                        String parent = childSnapshot.getKey();
                        if (parent.equals("Shop Owners")) {
                            Intent intent = new Intent(Login.this, MainActivity2.class);
                            startActivity(intent);
                            finish();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}