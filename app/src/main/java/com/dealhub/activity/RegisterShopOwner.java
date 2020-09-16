package com.dealhub.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.dealhub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterShopOwner extends AppCompatActivity {

    AppCompatMultiAutoCompleteTextView fname, lname, uname, email, contactno, password, confirmpass;
    AppCompatButton registerbtn;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop_owner);

        fname = findViewById(R.id.first_name);
        lname = findViewById(R.id.last_name);
        uname = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        contactno = findViewById(R.id.contact_no);
        password = findViewById(R.id.password);
        confirmpass = findViewById(R.id.confirm_password);
        registerbtn = findViewById(R.id.registerowner);

        auth = FirebaseAuth.getInstance();

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_fname = fname.getText().toString();
                String str_lname = lname.getText().toString();
                String str_uname = uname.getText().toString();
                String str_email = email.getText().toString();
                String str_contact = contactno.getText().toString();
                String str_pw = password.getText().toString();
                String str_confirmpw = confirmpass.getText().toString();

                if (TextUtils.isEmpty(str_fname) || TextUtils.isEmpty(str_lname) || TextUtils.isEmpty(str_uname) ||TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_contact) || TextUtils.isEmpty(str_pw) || TextUtils.isEmpty(str_confirmpw)){
                    Toast.makeText(RegisterShopOwner.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else if(str_pw.length()<6){
                    Toast.makeText(RegisterShopOwner.this, "Password must have at least 6 characters", Toast.LENGTH_SHORT).show();
                }else if(!(str_pw.equals(str_confirmpw))){
                    Toast.makeText(RegisterShopOwner.this, "Confirm password didn't match", Toast.LENGTH_SHORT).show();
                }else{
                    pd = new ProgressDialog(RegisterShopOwner.this);
                    pd.setMessage("Please Wait....");
                    pd.show();
                    pd.setCanceledOnTouchOutside(false);

                    register(str_uname, str_fname, str_lname, str_email, str_pw,str_contact);
                }

            }
        });

    }

    private void register(final String username, final String fname, final String lname, String email, String password, final String contact) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterShopOwner.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = null;
                    if (firebaseUser != null) {
                        userid = firebaseUser.getUid();
                    }

                    if (userid != null) {
                        reference = FirebaseDatabase.getInstance().getReference().child("Shop Owners").child(userid);
                    }

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username.toLowerCase());
                    hashMap.put("fname", fname);
                    hashMap.put("lname", lname);
                    hashMap.put("contactno", contact);
                    hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/dealhub-ffd45.appspot.com/o/shopowner%2Ficons8-administrator-male-48%20(1).png?alt=media&token=9a2e1574-5f1b-4d15-bb34-2137bc9f5c4d");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            pd.dismiss();
                                            Toast.makeText(RegisterShopOwner.this, "Email verification  was send", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(RegisterShopOwner.this, Login.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterShopOwner.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }
                    });


                } else {
                    pd.dismiss();
                    Toast.makeText(RegisterShopOwner.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}