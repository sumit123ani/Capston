package com.example.capston.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capston.Fragment.LoginFragment;
import com.example.capston.MainActivity;
import com.example.capston.R;
import com.example.capston.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    EditText editText1, editText2, editTextName, editTextMobile, editTextAdhar, editTextAddress, editTextdob;
    Button buttonSignUp;
    ScrollView scrollView;
    TextView accountText, userName;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    MainActivity mainActivity;
    NavigationView navigationView;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, databaseReference1;


    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_signup, container, false);

//        mainActivity = new MainActivity();
//        navigationView = getActivity().findViewById(R.id.nav);
//        View headerView = navigationView.getHeaderView(0);
//        userName = headerView.findViewById(R.id.txt3);
//        mainActivity = new MainActivity();
//        View headerView = navigationView.getHeaderView(0);
//        userName = headerView.findViewById(R.id.txt3);

        fragmentManager = getFragmentManager();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        accountText = view.findViewById(R.id.have_acc);

        editText1 = view.findViewById(R.id.email);
        editText2 = view.findViewById(R.id.password);
        editTextName = view.findViewById(R.id.name);
        editTextMobile = view.findViewById(R.id.mobile);
        editTextAdhar = view.findViewById(R.id.adhar);
        editTextAddress = view.findViewById(R.id.address);
        editTextdob = view.findViewById(R.id.dob);

        buttonSignUp = view.findViewById(R.id.sign_up);
        scrollView = view.findViewById(R.id.scroll);

        accountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frag, new LoginFragment()).commit();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        return view;
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // Check auth on Activity start
//        if (firebaseAuth.getCurrentUser() != null) {
//            onAuthSuccess(firebaseAuth.getCurrentUser());
//        }
//    }

    private void signUp() {

        String email = editText1.getText().toString();
        String password = editText2.getText().toString();

        if(validateForm()) {

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());

                            if (task.isSuccessful()) {
                                onAuthSuccess(task.getResult().getUser());
                            } else {
                                Toast.makeText(getContext(), "Sign Up Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

        public void onAuthSuccess(FirebaseUser user){
            String username = editTextName.getText().toString();
            String mobile = editTextMobile.getText().toString();
            String adhar = editTextAdhar.getText().toString();
            String addres = editTextAddress.getText().toString();
            String doB = editTextdob.getText().toString();
            String email = editText1.getText().toString();

            writeNewUser(user.getUid(), username, email, mobile, adhar, addres, doB);

            // Go to MainActivit
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("name", username);
            startActivity(intent);
//            userName.setText(username);
        }

        private String usernameFromEmail (String email){
            if (email.contains("@")) {
                return email.split("@")[0];
            } else {
                return email;
            }
        }

        private boolean validateForm () {
            boolean result = true;
            if (TextUtils.isEmpty(editTextName.getText().toString())) {
                editTextName.setError("Name Required");
                result = false;
            } else {
                editTextName.setError(null);
            }

            if (TextUtils.isEmpty(editTextMobile.getText().toString())) {
                editTextMobile.setError("Mobile N0. Required");
                result = false;
            }
            else if (editTextMobile.getText().toString().length() != 10){
                  editTextMobile.setError("ente 10 digits");
                  result = false;}
            else {
                editTextMobile.setError(null);
            }

            if (TextUtils.isEmpty(editTextAdhar.getText().toString())) {
                editTextAdhar.setError("Mobile N0. Required");
                result = false;
            }
            else if (editTextAdhar.getText().toString().length() != 16){
                 editTextAdhar.setError("enetr 16 digits");
                 result = false; }

            else {
                editText2.setError(null);
            }

            if (TextUtils.isEmpty(editTextAddress.getText().toString())) {
                editTextAddress.setError("Address Required");
                result = false;
            } else {
                editTextAddress.setError(null);
            }

            if (TextUtils.isEmpty(editTextdob.getText().toString())) {
                editTextdob.setError("Dob Required");
                result = false;
            } else {
                editTextdob.setError(null);
            }

            if (TextUtils.isEmpty(editText1.getText().toString())) {
                editText1.setError("Address Required");
                result = false;
            } else {
                editText1.setError(null);
            }

            if (TextUtils.isEmpty(editText2.getText().toString())) {
                editText2.setError("Address Required");
                result = false;
            } else {
                editText2.setError(null);
            }

            return result;
        }

        // [START basic_write]
        private void writeNewUser (String userId, String name, String email, String mobile, String adhar, String address, String doB){
            UserDetails user = new UserDetails(email, name, mobile, adhar, address, doB);

            databaseReference.child(userId).child("UserDetail").setValue(user);
        }

    }


//    public void storeUser()
//    {
//        final String name = editTextName.getText().toString();
//        final String mobile_no = editTextMobile.getText().toString();
//        final String adhar_no = editTextAdhar.getText().toString();
//        final String email = editText1.getText().toString();
//        final String password = editText2.getText().toString();
//
//        final String id = databaseReference.push().getKey();
//
//        if(name.isEmpty())
//        {
//            editTextName.setError("enter name");
//            editTextName.requestFocus();
//        }
//        else if(mobile_no.isEmpty())
//        {
//            editTextMobile.setError("enter mobile no.");
//            editTextMobile.requestFocus();
//        }
//        else if(mobile_no.length() < 10)
//        {
//            editTextMobile.setError("enter 10 digits");
//            editTextMobile.requestFocus();
//        }
//
//        else if(adhar_no.isEmpty())
//        {
//            editTextAdhar.setError("enter adhar no.");
//            editTextAdhar.requestFocus();
//        }
////        else if (adhar_no.length() < 16)
////        {
////            editTextAdhar.setError("enter valid adhar no.");
////            editTextAdhar.requestFocus();
////        }
//        else if (email.isEmpty()) {
//            editText1.setError("provide email");
//            editText1.requestFocus();
//        }
//        else if (!email.contains("@gmail.com")) {
//            editText1.setError("not valid email");
//            editText1.requestFocus();
//        }
//        else if (password.isEmpty()) {
//            editText2.setError("enter password");
//            editText1.requestFocus();
//        }
//        else {
//            firebaseAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (!task.isSuccessful())
//                                Snackbar.make(scrollView, "SignUp failed", Snackbar.LENGTH_SHORT).show();
//                            else {
//                                Toast.makeText(getContext(), "Signed Up successfully", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getActivity(), MainActivity.class);
//                                startActivity(intent);
//
//                                UserDetails userDetails = new UserDetails(email, name, mobile_no, adhar_no);
//                                databaseReference.child(id).setValue(userDetails);
//                                Toast.makeText(getContext(), "Data saved", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        }
//    }
//
//    public void userDetailsStore()

