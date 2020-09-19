package com.dealhub.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dealhub.R;
import com.dealhub.adapters.CommentsAdapter;
import com.dealhub.adapters.MyOffersAdapter_ShopOwner;
import com.dealhub.models.Comments;
import com.dealhub.models.ShopOwners;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentDialog extends DialogFragment {
    RecyclerView commentlist;
    CircleImageView userdp;
    EditText cmnt;
    TextView postcmnt;
    ArrayList<Comments> comments;

    FirebaseUser firebaseUser;
    DatabaseReference userReference;
    DatabaseReference databaseReference;
    CommentsAdapter adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_comment, null);

        Bundle bundle = getArguments();
        final String offer = bundle.getString("offer");
        alert.setView(view);

        final AlertDialog alertDialog = alert.create();
        commentlist = view.findViewById(R.id.recycler_view);
        userdp = view.findViewById(R.id.image_profile);
        cmnt = view.findViewById(R.id.addcomment);
        postcmnt = view.findViewById(R.id.post);
        comments = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference("Shop Owners").child(firebaseUser.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference("Comments").child(offer);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ShopOwners shpowner=dataSnapshot.getValue(ShopOwners.class);
                Picasso.get().load(shpowner.getImageurl()).into(userdp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new CommentsAdapter(getContext());
        commentlist.setAdapter(adapter);
        commentlist.setLayoutManager(new LinearLayoutManager(getActivity()));

//        Query query = databaseReference.getRef().orderByChild("offerid").equalTo(Integer.parseInt(offer));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.clear();
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    Comments cmn=snap.getValue(Comments.class);
                    comments.add(cmn);
                }
                adapter.loadComments(comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postcmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_cmnt = cmnt.getText().toString();
                if (!str_cmnt.isEmpty()) {
                    DatabaseReference owners = FirebaseDatabase.getInstance().getReference("Shop Owners").child(firebaseUser.getUid());
                    owners.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ShopOwners shpowner=dataSnapshot.getValue(ShopOwners.class);
                                String imageurl = "";

                                    imageurl = shpowner.getImageurl().toString();
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("profileurl", imageurl);
                                    hashMap.put("offerid", offer);
                                    hashMap.put("publisher", firebaseUser.getUid());
                                    hashMap.put("comment", cmnt.getText().toString());
                                    hashMap.put("publishername", shpowner.getFname()+" "+shpowner.getLname());
                                    databaseReference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            cmnt.setText("");
                                        }
                                    });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return alertDialog;
    }
}
