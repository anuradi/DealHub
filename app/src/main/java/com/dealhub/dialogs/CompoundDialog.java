package com.dealhub.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CompoundDialog extends DialogFragment {
    FirebaseUser firebaseUser;
    DatabaseReference userReference;
    DatabaseReference databaseReference;
    AppCompatEditText phoneNumber, verifynumber;
    AppCompatButton getcoupen, verify;
    AppCompatTextView timer, timertext, resendtext,mainhead;


    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_get_coupon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getcoupen = view.findViewById(R.id.getcoupen);
        phoneNumber = view.findViewById(R.id.phonenumber);
        verifynumber = view.findViewById(R.id.verifynumber);
        verify = view.findViewById(R.id.verify);
        timer = view.findViewById(R.id.timer);
        timertext = view.findViewById(R.id.timerText);
        resendtext = view.findViewById(R.id.resendtext);
        mainhead = view.findViewById(R.id.textView5);

        Bundle bundle = getArguments();
        final String offer = bundle.getString("offer");
        final String shopname = bundle.getString("shopname");
        final String login = bundle.getString("login");

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().setCanceledOnTouchOutside(false);
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (login.equals("shopowner")) {
            userReference = FirebaseDatabase.getInstance().getReference("Shop Owners").child(firebaseUser.getUid());
        } else if (login.equals("customer")) {
            userReference = FirebaseDatabase.getInstance().getReference("Customers").child(firebaseUser.getUid());
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Comments").child(shopname).child(offer);

//start the process of phone verify
        init();

        getcoupen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupVerifyPhoneNumber(phoneNumber.getText().toString());
            }
        });
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_get_coupon, null);
//        getcoupen = view.findViewById(R.id.getcoupen);
//        phoneNumber = view.findViewById(R.id.phonenumber);
//        verifynumber = view.findViewById(R.id.verifynumber);
//        verify = view.findViewById(R.id.verify);
//        timer = view.findViewById(R.id.timer);
//        timertext = view.findViewById(R.id.timerText);
//        resendtext = view.findViewById(R.id.resendtext);
//        mainhead = view.findViewById(R.id.textView5);
//
//        Bundle bundle = getArguments();
//        final String offer = bundle.getString("offer");
//        final String shopname = bundle.getString("shopname");
//        final String login = bundle.getString("login");
//        alert.setView(view);
//
//        final AlertDialog alertDialog = alert.create();
//        System.out.println(getDialog());
//        System.out.println(getDialog().getWindow());
//        if (getDialog() != null && getDialog().getWindow() != null) {
//            System.out.println("%%%%%");
//            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
//        databaseReference = FirebaseDatabase.getInstance().getReference("Comments").child(shopname).child(offer);
//
////start the process of phone verify
//        init();
//
//        getcoupen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setupVerifyPhoneNumber(phoneNumber.getText().toString());
//            }
//        });
//
//        return alertDialog;
//    }

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
                String verify_code = verifynumber.getText().toString();
                // [START verify_with_code]
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verify_code);
                // [END verify_with_code]
                System.out.println("*************" + credential.getSmsCode());
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

                // [START_EXCLUDE]
                // Update UI
                setupTimer();
                // [END_EXCLUDE]
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

    private void setupTimer() {
        mainhead.setVisibility(View.GONE);
        phoneNumber.setVisibility(View.GONE);
        getcoupen.setVisibility(View.GONE);
        timertext.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
        verify.setVisibility(View.VISIBLE);
        verifynumber.setVisibility(View.VISIBLE);

        new CountDownTimer(30000, 1000) {
            public void onTick(final long millisUntilFinished) {
                timertext.post(new Runnable() {
                    @Override
                    public void run() {
                        timertext.setText("Seconds remaining: " + millisUntilFinished / 1000);
                    }
                });
            }

            public void onFinish() {
                timertext.post(new Runnable() {
                    @Override
                    public void run() {
                        resendtext.setVisibility(View.VISIBLE);
                        timertext.setVisibility(View.GONE);
                        timer.setText("Now you can request code again");
                        timer.setVisibility(View.GONE);

                    }
                });
            }
        }.start();
    }
}
