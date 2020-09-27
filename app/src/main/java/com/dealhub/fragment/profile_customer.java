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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.dealhub.R;
import com.dealhub.activity.Login;
import com.dealhub.dialogs.SampleDialog;
import com.dealhub.models.Customers;
import com.dealhub.models.ShopOwners;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class profile_customer extends Fragment {

    CircleImageView user_img;
    AppCompatTextView name,email;
    AppCompatEditText bio;
    AppCompatButton update;
    ImageView logout;

    FirebaseUser firebaseUser;
    StorageReference storageReference;

    private Uri mImageUri;
    private StorageTask uplaodTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        bio=view.findViewById(R.id.bio);
        user_img=view.findViewById(R.id.user_image);
        update=view.findViewById(R.id.edit_profile);
        logout=view.findViewById(R.id.logout_menu);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Customers");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Customers").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customers customers = dataSnapshot.getValue(Customers.class);
                email.setText(firebaseUser.getEmail());
                if (customers.getFname() != null && customers.getLname()!=null) {
                    name.setText(customers.getFname()+" "+customers.getLname());
                }
                if (customers.getBio()!=null){
                    bio.setText(customers.getBio());
                }

                if (customers.getImageurl() != null) {
                    Picasso.get().load(customers.getImageurl()).into(user_img);
                }
                //Glide.with(getApplicationContext()).load(user.getImageUrl()).into(imag_profile_edit);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.log:
                                Bundle bundle = new Bundle();
                                bundle.putString("logout", "logout");
                                bundle.putString("offer", "");
                                bundle.putString("notification", "");
                                SampleDialog smpDialog = new SampleDialog();
                                smpDialog.setArguments(bundle);
                                smpDialog.show(getFragmentManager(), "sure_dialog");
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.logout);

                popupMenu.show();
            }
        });

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setCropShape(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ? CropImageView.CropShape.RECTANGLE : CropImageView.CropShape.OVAL)
                        .start(getActivity());
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bio.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Nothing to update", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Customers").child(firebaseUser.getUid());
                    HashMap<String, Object> hashMap = new HashMap<>();

                    hashMap.put("bio", bio.getText().toString());

                    reference.updateChildren(hashMap);
                    Toast.makeText(getActivity(), "Successfully updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (result != null) {
                mImageUri = result.getUri();
            }

            uploadImage();
        } else {
            Toast.makeText(getActivity(), "Something gone wrong..! ", Toast.LENGTH_SHORT).show();

        }
    }



    private void uploadImage() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Updating your Profile");
        pd.show();
        pd.setCanceledOnTouchOutside(false);


        if (mImageUri != null) {
            final StorageReference filerefrence = storageReference.child(firebaseUser.getUid()+ ".jpg");
            uplaodTask = filerefrence.putFile(mImageUri);

            uplaodTask = filerefrence.putFile(mImageUri);

            uplaodTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filerefrence.getDownloadUrl();
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

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Customers").child(firebaseUser.getUid());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageurl", "" + myUrl);

                        reference.updateChildren(hashMap);
                        pd.dismiss();


                    } else {
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            Toast.makeText(getActivity(), "No image selected ", Toast.LENGTH_SHORT).show();
            pd.dismiss();

        }
    }
}

