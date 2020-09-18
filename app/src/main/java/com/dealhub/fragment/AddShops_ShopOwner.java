package com.dealhub.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dealhub.R;
import com.dealhub.activity.RegisterShopOwner;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;


public class AddShops_ShopOwner extends Fragment {

    private Uri URIidphoto, URIbrphoto, URIshoplogo, URIshopphoto;
    private static final int ID_PHOTO = 101;
    private static final int BR_PHOTO = 102;
    private static final int SHOP_LOGO = 103;
    private static final int SHOP_PHOTO_CODE = 100;
    AppCompatImageView shopimg;
    AppCompatEditText shopname, address, brno, email, contact, ownernic, nicphoto, brphoto, shoplogo;
    AppCompatButton register;
    int shopid = 0;
    int counttask = 0;
    ProgressDialog pd;

    FirebaseUser firebaseUser;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private StorageTask uplaodTasknic;
    private StorageTask uplaodTaskbr;
    private StorageTask uplaodTasklogo;
    private StorageTask uplaodTaskshopimage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Shops").child(firebaseUser.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference("Shops").child(firebaseUser.getUid());
        View view = inflater.inflate(R.layout.fragment_add_shops_shop_owner, container, false);
        shopimg = view.findViewById(R.id.shop_img);
        shopname = view.findViewById(R.id.shop_name);
        address = view.findViewById(R.id.shop_address);
        brno = view.findViewById(R.id.br_no);
        email = view.findViewById(R.id.email);
        contact = view.findViewById(R.id.contact_no);
        ownernic = view.findViewById(R.id.shop_owner_id);
        nicphoto = view.findViewById(R.id.shop_owner_id_photo);
        brphoto = view.findViewById(R.id.br_photo);
        shoplogo = view.findViewById(R.id.shop_logo);
        register = view.findViewById(R.id.register);

        shopimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an Intent with action as ACTION_PICK
                Intent intent = new Intent(Intent.ACTION_PICK);
                // Sets the type as image/*. This ensures only components of type image are selected
                intent.setType("image/*");
                //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                // Launching the Intent
                startActivityForResult(intent, SHOP_PHOTO_CODE);
            }
        });

        nicphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, ID_PHOTO);
            }
        });

        brphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, BR_PHOTO);
            }
        });

        shoplogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, SHOP_LOGO);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_shopname = shopname.getText().toString();
                String str_address = address.getText().toString();
                String str_brno = brno.getText().toString();
                String str_email = email.getText().toString();
                String str_contact = contact.getText().toString();
                String str_ownernic = ownernic.getText().toString();
                if (TextUtils.isEmpty(str_shopname) || TextUtils.isEmpty(str_address) || TextUtils.isEmpty(str_brno) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_contact) || TextUtils.isEmpty(str_ownernic)) {
                    Toast.makeText(getActivity(), "All the fields are required", Toast.LENGTH_SHORT).show();
                } else if (URIshopphoto == null) {
                    Toast.makeText(getActivity(), "Image of shop is required", Toast.LENGTH_SHORT).show();
                } else if (URIbrphoto == null || URIidphoto == null || URIshoplogo == null) {
                    Toast.makeText(getActivity(), "All documents required", Toast.LENGTH_SHORT).show();
                } else {
                    addShop(str_shopname, str_address, str_brno, str_email, str_contact, str_ownernic);
                }
            }
        });

        return view;
    }

    private void addShop(final String shop_name, final String shop_address, final String shop_brno, String shop_email, final String shop_contact, final String shop_ownernic) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("Adding your Shop");
        pd.show();
        pd.setCanceledOnTouchOutside(false);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    shopid = 0;
                } else {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        int key = Integer.parseInt(snap.getKey());
                        if (shopid < key) {
                            shopid = key;
                        }
                    }
                }
                shopid++;

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("shopid", shopid);
                hashMap.put("shopname", shop_name);
                hashMap.put("shopbrno", shop_brno);
                hashMap.put("address", shop_address);
                hashMap.put("email", shop_brno);
                hashMap.put("contactno", shop_contact);
                hashMap.put("ownernic", shop_ownernic);
                hashMap.put("status", "Pending");
                databaseReference.child("" + shopid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        uploadImages(shopid);
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

    private void uploadImages(final int shopid) {
        final StorageReference filerefrenceshopimg = storageReference.child("" + shopid).child("shopimg.jpg");
        uplaodTaskshopimage = filerefrenceshopimg.putFile(URIshopphoto);

        uplaodTaskshopimage.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {

                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filerefrenceshopimg.getDownloadUrl();
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

                    DatabaseReference reference = databaseReference.child("" + shopid);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("shopimageurl", "" + myUrl);

                    reference.updateChildren(hashMap);
                } else {
                    Toast.makeText(getActivity(), "Failed to upload shop image", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        final StorageReference filerefrencenic = storageReference.child("" + shopid).child("nic.jpg");
        uplaodTasknic = filerefrencenic.putFile(URIidphoto);

        uplaodTasknic.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {

                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filerefrencenic.getDownloadUrl();
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

                    DatabaseReference reference = databaseReference.child("" + shopid);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("nicurl", "" + myUrl);

                    reference.updateChildren(hashMap);
                } else {
                    Toast.makeText(getActivity(), "Failed to upload nic", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        final StorageReference filerefrencebr = storageReference.child("" + shopid).child("br.jpg");
        uplaodTaskbr = filerefrencebr.putFile(URIbrphoto);

        uplaodTaskbr.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {

                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filerefrencebr.getDownloadUrl();
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

                    DatabaseReference reference = databaseReference.child("" + shopid);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("brurl", "" + myUrl);

                    reference.updateChildren(hashMap);
                } else {
                    Toast.makeText(getActivity(), "Failed to upload br image", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        final StorageReference filerefrencelogo = storageReference.child("" + shopid).child("logo.jpg");
        uplaodTasklogo = filerefrencelogo.putFile(URIshoplogo);

        uplaodTasklogo.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {

                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return filerefrencelogo.getDownloadUrl();
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

                    DatabaseReference reference = databaseReference.child("" + shopid);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("logourl", "" + myUrl);

                    reference.updateChildren(hashMap);
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Shop Added. We will let you know once its verified", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(getActivity(), "Failed to upload logo", Toast.LENGTH_SHORT).show();
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
        shopname.setText("");
        address.setText("");
        brno.setText("");
        email.setText("");
        contact.setText("");
        ownernic.setText("");
        nicphoto.setText("");
        brphoto.setText("");
        shoplogo.setText("");
        shopimg.setBackgroundResource(R.drawable.add);
        shopimg.setImageResource(R.drawable.button_black);
        nicphoto.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cloud_upload_24, 0);
        brphoto.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cloud_upload_24, 0);
        shoplogo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cloud_upload_24, 0);
        URIshopphoto=null;
        URIidphoto=null;
        URIshoplogo=null;
        URIbrphoto=null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case SHOP_PHOTO_CODE:
                    URIshopphoto = data.getData();
                    shopimg.setImageURI(URIshopphoto);
                    //Toast.makeText(DriverPersonalDocument.this, "" + URIinsurance, Toast.LENGTH_SHORT).show();
                    break;
                case ID_PHOTO:
                    URIidphoto = data.getData();
                    //  driverVehicle.setImageURI(URIvehicle);
                    nicphoto.setText("Uploaded");
                    nicphoto.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_24dp, 0, 0, 0);
                    //Toast.makeText(DriverPersonalDocument.this, "" + URInic, Toast.LENGTH_SHORT).show();
                    break;

                case BR_PHOTO:
                    URIbrphoto = data.getData();
                    brphoto.setText("Uploaded");
                    brphoto.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_24dp, 0, 0, 0);
                    break;

                case SHOP_LOGO:
                    URIshoplogo = data.getData();
                    shoplogo.setText("Uploaded");
                    shoplogo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_24dp, 0, 0, 0);
                    break;
            }
    }

}