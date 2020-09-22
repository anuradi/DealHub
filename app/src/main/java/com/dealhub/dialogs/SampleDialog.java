package com.dealhub.dialogs;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dealhub.R;
import com.dealhub.activity.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SampleDialog extends DialogFragment {

    DatabaseReference databaseReferenceoffer;
    DatabaseReference databaseReferencecmnt;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_logout_cofirmation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        final String offer = bundle.getString("offer");
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getDialog().setCanceledOnTouchOutside(false);
        }
        if (offer.equals("")) {
            Button btnOk = view.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getDialog() != null) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getActivity(), Login.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        getDialog().dismiss();
                        getActivity().finish();
                    }
                }
            });

            Button btnCancel = view.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getDialog() != null) {
                        getDialog().dismiss();
                    }
                }
            });
        } else {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            databaseReferencecmnt = FirebaseDatabase.getInstance().getReference("Comments").child(offer);
            databaseReferenceoffer = FirebaseDatabase.getInstance().getReference("Offers").child(firebaseUser.getUid()).child(offer);

            if (getDialog() != null && getDialog().getWindow() != null) {
                getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                getDialog().setCanceledOnTouchOutside(false);
            }

            Button btnOk = view.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getDialog() != null) {
                        databaseReferencecmnt.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                databaseReferenceoffer.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        getDialog().dismiss();
                                    }
                                });
                            }
                        });
                    }
                }
            });

            Button btnCancel = view.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getDialog() != null) {
                        getDialog().dismiss();
                    }
                }
            });
        }

    }
}
