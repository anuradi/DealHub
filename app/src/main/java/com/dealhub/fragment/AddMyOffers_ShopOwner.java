package com.dealhub.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.dealhub.R;
import com.dealhub.activity.MainActivity;
import com.dealhub.dialogs.DatePickerDialog;
import com.dealhub.models.MyShops;
import com.dealhub.notifications.Client;
import com.dealhub.notifications.Data;
import com.dealhub.notifications.MyResponse;
import com.dealhub.notifications.Sender;
import com.dealhub.notifications.Token;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddMyOffers_ShopOwner extends Fragment {

    AppCompatImageView offerimg;
    AppCompatSpinner shopname;
    AppCompatEditText description, price, discount,expdate;
    AppCompatButton postoffer;
    ArrayAdapter<String> adapter;
    ArrayList<String> sinpperArrayList;
    DatabaseReference databaseReference;
    DatabaseReference shopReference;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    private StorageTask uplaodTask;
    String str_shopname;
    private static final int OFFERIMAGE = 102;
    private Uri URIofferimg;
    ProgressDialog pd;
    int offerid;
    APIService apiService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_my_offers_shop_owner, container, false);
        offerimg = view.findViewById(R.id.pro_pic);
        shopname = view.findViewById(R.id.shopname);
        description = view.findViewById(R.id.description);
        expdate=view.findViewById(R.id.exp_date);
        price = view.findViewById(R.id.price);
        discount = view.findViewById(R.id.discount);
        postoffer = view.findViewById(R.id.post);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Offers").child(firebaseUser.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference("Offers").child(firebaseUser.getUid());
        shopReference = FirebaseDatabase.getInstance().getReference("Shops").child(firebaseUser.getUid());
        sinpperArrayList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, sinpperArrayList);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        shopname.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadShopName();
        shopname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                str_shopname = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        expdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(getActivity().getSupportFragmentManager(), "datepicker");
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
                    Toast.makeText(getActivity(), "Select your shop name", Toast.LENGTH_SHORT).show();
                }else if(URIofferimg==null){
                    Toast.makeText(getActivity(), "Select image for the offers", Toast.LENGTH_SHORT).show();
                }else if (str_description.isEmpty() || str_discount.isEmpty() || str_price.isEmpty() || str_expdate.isEmpty()) {
                    Toast.makeText(getActivity(), "All fields required", Toast.LENGTH_SHORT).show();
                }else{
                    postOffer(str_description,str_price,str_discount,str_expdate);
                }
            }
        });

        return view;
    }

    private void postOffer(final String str_description, final String str_price, final String str_discount, final String str_expdate) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Adding your Offer");
        pd.show();
        pd.setCanceledOnTouchOutside(false);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    offerid = 0;
                } else {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        int key = Integer.parseInt(snap.getKey());
                        if (offerid < key) {
                            offerid = key;
                        }
                    }
                }
                offerid++;

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("offerid", offerid);
                hashMap.put("shopname", str_shopname);
                hashMap.put("expdate", str_expdate);
                hashMap.put("offerdescription", str_description);
                hashMap.put("offerprice", str_price);
                hashMap.put("offerdiscount", str_discount);
                hashMap.put("status", "Active");
                hashMap.put("likes", 0);
                databaseReference.child("" + offerid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        uploadImages(offerid);
                        manageSendingNotification(str_shopname,str_description);
                        //pd.dismiss();
                        //Toast.makeText(getActivity(), "Shop Added. We will let you know once its verified", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void manageSendingNotification(final String str_shopname, final String str_description) {
        final DatabaseReference follow = FirebaseDatabase.getInstance().getReference("Follow");
        follow.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap:dataSnapshot.getChildren()) {
                    final String receiver=snap.getKey();
                    DatabaseReference following = snap.getRef().child("Following");
                    following.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            for (DataSnapshot snap1 : dataSnapshot1.getChildren()) {
                                if(snap1.getKey().equals(str_shopname)){
                                    sendNotification(str_shopname,str_description,receiver);
                                }
                            }
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

    private void sendNotification(final String str_shopname, final String str_description, final String receiver) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.mipmap.ic_launcher, str_shopname + ": " + str_description, "New Offer Released",
                            receiver);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        System.out.println(response.body().success);
                                        if (response.body().success != 1) {
                                            Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadImages(final int offerid) {
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

                    DatabaseReference reference = databaseReference.child("" + offerid);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("offerimageurl", "" + myUrl);

                    reference.updateChildren(hashMap);
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Offer Added Successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(getActivity(), "Failed to upload offer image", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        offerimg.setBackgroundResource(R.drawable.add);
        offerimg.setImageResource(R.drawable.button_black);
        URIofferimg=null;
        str_shopname=null;
        description.setText("");
        price.setText("");
        discount.setText("");
        loadShopName();
    }

    private void loadShopName() {
        sinpperArrayList.clear();
        shopReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sinpperArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MyShops myShops = snapshot.getValue(MyShops.class);
                    if (myShops.getStatus().equals("Active")) {
                        sinpperArrayList.add(myShops.getShopname());
                    }
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