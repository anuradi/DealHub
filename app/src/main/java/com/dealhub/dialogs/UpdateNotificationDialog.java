package com.dealhub.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;

import com.dealhub.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UpdateNotificationDialog extends DialogFragment {

    AppCompatImageView image;
    AppCompatEditText desciption;
    AppCompatButton update;
    private static final int NOTIFICATIONIMAGE = 102;
    private Uri URInotificationimg;
    ProgressDialog pd;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private StorageTask uplaodTask;
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_update__notification, null);

        image=view.findViewById(R.id.pro_pic);
        desciption=view.findViewById(R.id.description);
        update=view.findViewById(R.id.post);


        storageReference = FirebaseStorage.getInstance().getReference("Shop Notitfications");
        databaseReference = FirebaseDatabase.getInstance().getReference("Shop Notitfications");
        
        Bundle bundle = getArguments();
        final String img_url = bundle.getString("img_url");
        final String str_description = bundle.getString("description");
        final String id = bundle.getString("id");
        alert.setView(view);
        final AlertDialog alertDialog = alert.create();
        Picasso.get().load(img_url).into(image);
        desciption.setText(str_description);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_description=desciption.getText().toString();
                if (str_description.isEmpty()){
                    Toast.makeText(getActivity(), "Can't update without description", Toast.LENGTH_SHORT).show();
                }else{
                    pd = new ProgressDialog(getActivity());
                    pd.setMessage("Updating your notification");
                    pd.show();
                    pd.setCanceledOnTouchOutside(false);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("description", str_description);
                    hashMap.put("id", id);
                    databaseReference.child(id).updateChildren(hashMap);
                    if (URInotificationimg!=null){
                        uploadImage(id,alertDialog);
                    }else{
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Notification Updated Successfully", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                }
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, NOTIFICATIONIMAGE);
            }
        });

        return alertDialog;
    }

    private void uploadImage(final String key, final AlertDialog alertDialog) {
        final StorageReference filerefrenceoffer = storageReference.child("" + key).child("notificationimg.jpg");
        uplaodTask = filerefrenceoffer.putFile(URInotificationimg);

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

                    DatabaseReference reference = databaseReference.child("" + key);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("imgurl", "" + myUrl);

                    reference.updateChildren(hashMap);
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Notification Updated Successfully", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Failed to upload offer image", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case NOTIFICATIONIMAGE:
                    URInotificationimg = data.getData();
                    image.setImageURI(URInotificationimg);
                    break;

            }
    }
}
