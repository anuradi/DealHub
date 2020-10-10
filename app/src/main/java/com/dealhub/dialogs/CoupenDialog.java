package com.dealhub.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dealhub.R;
import com.dealhub.adapters.CommentsAdapter;
import com.dealhub.models.Comments;
import com.dealhub.models.ShopOwners;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CoupenDialog extends DialogFragment {
    FirebaseUser firebaseUser;
    DatabaseReference userReference;
    DatabaseReference databaseReference;
    AppCompatEditText phoneNumber;
    AppCompatButton getcoupen;


    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    String phonenum;
    String count;
    String price;
    double totalprice;
    AlertDialog alertDialog;

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.activity_get_coupon, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        getcoupen = view.findViewById(R.id.getcoupen);
//        phoneNumber = view.findViewById(R.id.phonenumber);
//
//        Bundle bundle = getArguments();
//        final String offer = bundle.getString("offer");
//        final String shopname = bundle.getString("shopname");
//        final String login = bundle.getString("login");
//        count = bundle.getString("count");
//
//        if (getDialog() != null && getDialog().getWindow() != null) {
//            getDialog().setCanceledOnTouchOutside(false);
//        }
//
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (login.equals("shopowner")) {
//            userReference = FirebaseDatabase.getInstance().getReference("Shop Owners").child(firebaseUser.getUid());
//        } else if (login.equals("customer")) {
//            userReference = FirebaseDatabase.getInstance().getReference("Customers").child(firebaseUser.getUid());
//        }
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Coupens").child(firebaseUser.getUid()).child(shopname).child(offer);
//
////start the process of phone verify
//        init();
//
//
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_get_coupon, null);
        getcoupen = view.findViewById(R.id.getcoupen);
        phoneNumber = view.findViewById(R.id.phonenumber);

        Bundle bundle = getArguments();
        final String offer = bundle.getString("offer");
        final String shopname = bundle.getString("shopname");
        final String login = bundle.getString("login");
        count = bundle.getString("count");
        price = bundle.getString("price");
        totalprice=Double.parseDouble(price)*Double.parseDouble(count);
        alert.setView(view);

        alertDialog = alert.create();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (login.equals("shopowner")) {
            userReference = FirebaseDatabase.getInstance().getReference("Shop Owners").child(firebaseUser.getUid());
        } else if (login.equals("customer")) {
            userReference = FirebaseDatabase.getInstance().getReference("Customers").child(firebaseUser.getUid());
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Coupens").child(firebaseUser.getUid()).child(shopname).child(offer);

//start the process of phone verify
        init();

        return alertDialog;
    }

    private void init() {

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        setupCallbacks();
        // [END phone_auth_callbacks]

        getcoupen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonenum = phoneNumber.getText().toString();
                setupVerifyPhoneNumber(phonenum);
            }
        });


    }

    private void setupCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("TAG", "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
//                updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
//                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.d("TAG", "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
//                    mPhoneNumberField.setError("Invalid phone number.");
                    Toast.makeText(getActivity(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Toast.makeText(getActivity(), "Quota exceeded", Toast.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
//                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("TAG", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                String crrdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("crrdate", crrdate);
                hashMap.put("phone", phonenum);
                hashMap.put("count", count);
                hashMap.put("price", totalprice);
                databaseReference.push().setValue(hashMap);
                Toast.makeText(getActivity(), "Coupen code sent", Toast.LENGTH_SHORT).show();
                if (getDialog() != null) {
                    getDialog().dismiss();
                }
            }
        };
    }


    // [START start_phone_auth]
    private void setupVerifyPhoneNumber(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+94" + mobile,
                2,
                TimeUnit.MINUTES,
                getActivity(),
                mCallbacks);

        mVerificationInProgress = true;
    }
    // [END start_phone_auth]


}
