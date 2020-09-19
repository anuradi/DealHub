package com.dealhub.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.dealhub.R;
import com.dealhub.dialogs.DatePickerDialog;
import com.dealhub.models.MyOffers;
import com.dealhub.models.MyShops;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class Edit_Offers extends AppCompatActivity {
    AppCompatImageView offerimg;
    Spinner shopname;
    AppCompatEditText description, price, discount,expdate;
    AppCompatButton postoffer;
    DatabaseReference databaseReference;
    DatabaseReference shopReference;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    private StorageTask uplaodTask;
    ArrayAdapter<String> adapter;
    ArrayList<String> sinpperArrayList;
    private static final int OFFERIMAGE = 102;
    private Uri URIofferimg;
    ProgressDialog pd;
    String str_shopname;
    String offerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__offers);
        offerid = getIntent().getStringExtra("offerid");
        offerimg = findViewById(R.id.pro_pic);
        shopname = findViewById(R.id.shopname);
        description = findViewById(R.id.description);
        expdate=findViewById(R.id.exp_date);
        price = findViewById(R.id.price);
        discount = findViewById(R.id.discount);
        postoffer = findViewById(R.id.post);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        shopReference = FirebaseDatabase.getInstance().getReference("Shops").child(firebaseUser.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("Offers").child(firebaseUser.getUid());
        sinpperArrayList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, sinpperArrayList);
        shopname.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadShopName();
        databaseReference = FirebaseDatabase.getInstance().getReference("Offers").child(firebaseUser.getUid()).child(offerid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MyOffers offers=dataSnapshot.getValue(MyOffers.class);
                Picasso.get().load(offers.getOfferimageurl()).into(offerimg);
                shopname.setSelection(adapter.getPosition(offers.getShopname()));
                description.setText(offers.getOfferdescription());
                expdate.setText(offers.getExpdate());
                price.setText(offers.getOfferprice());
                discount.setText(offers.getOfferdiscount());
                str_shopname=offers.getShopname();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        expdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(getSupportFragmentManager(), "datepicker");
            }
        });
        shopname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                str_shopname = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        offerimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, OFFERIMAGE);
            }
        });
        postoffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_description = description.getText().toString();
                String str_price = price.getText().toString();
                String str_discount = discount.getText().toString();
                String str_expdate = expdate.getText().toString();
                if (shopname == null) {
                    Toast.makeText(getApplicationContext(), "Select your shop name", Toast.LENGTH_SHORT).show();
                }else if (str_description.isEmpty() || str_discount.isEmpty() || str_price.isEmpty() || str_expdate.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT).show();
                }else{
                    postOffer(str_description,str_price,str_discount,str_expdate);
                }
            }
        });
    }

    private void postOffer(final String str_description, final String str_price, final String str_discount, final String str_expdate) {
        pd = new ProgressDialog(Edit_Offers.this);
        pd.setMessage("Updating your Offer");
        pd.show();
        pd.setCanceledOnTouchOutside(false);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("offerid", Integer.parseInt(offerid));
                hashMap.put("shopname", str_shopname);
                hashMap.put("expdate", str_expdate);
                hashMap.put("offerdescription", str_description);
                hashMap.put("offerprice", str_price);
                hashMap.put("offerdiscount", str_discount);
                hashMap.put("status", "Active");
                hashMap.put("likes", 0);
                databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        uploadImages(offerid);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadImages(final String offerid) {
        if (URIofferimg!=null){
            final StorageReference filerefrenceoffer = storageReference.child("" + offerid).child("offerimg.jpg");
            uplaodTask = filerefrenceoffer.putFile(URIofferimg);

            uplaodTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filerefrenceoffer.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String myUrl = null;
                        if (downloadUri != null) {
                            myUrl = downloadUri.toString();
                        }

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("offerimageurl", "" + myUrl);

                        databaseReference.updateChildren(hashMap);
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Offer Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to upload offer image", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            pd.dismiss();
            Toast.makeText(getApplicationContext(), "Offer Updated Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadShopName() {
        sinpperArrayList.clear();
        shopReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sinpperArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MyShops myShops = snapshot.getValue(MyShops.class);
                    sinpperArrayList.add(myShops.getShopname());

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case OFFERIMAGE:
                    URIofferimg = data.getData();
                    offerimg.setImageURI(URIofferimg);
                    //Toast.makeText(DriverPersonalDocument.this, "" + URIinsurance, Toast.LENGTH_SHORT).show();
                    break;

            }
    }
}