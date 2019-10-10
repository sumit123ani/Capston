package com.example.capston;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    EditText editText1, editText2, editTextName, editTextMobile, editTextAdhar;
    Button buttonSignUp;
    ScrollView scrollView;
    TextView userName;

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
//        View headerView = navigationView.getHeaderView(0);
//        userName = headerView.findViewById(R.id.txt3);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        editText1 = view.findViewById(R.id.email);
        editText2 = view.findViewById(R.id.password);
        editTextName = view.findViewById(R.id.name);
        editTextMobile = view.findViewById(R.id.mobile);
        editTextAdhar = view.findViewById(R.id.adhar);

        buttonSignUp = view.findViewById(R.id.sign_up);
        scrollView = view.findViewById(R.id.scroll);



        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               storeUser();
            }
        });

        return view;
    }

    public void storeUser()
    {
        final String name = editTextName.getText().toString();
        final String mobile_no = editTextMobile.getText().toString();
        final String adhar_no = editTextAdhar.getText().toString();
        final String email = editText1.getText().toString();
        String password = editText2.getText().toString();

        final String id = databaseReference.push().getKey();

        if(name.isEmpty())
        {
            editTextName.setError("enter name");
            editTextName.requestFocus();
        }
        else if(mobile_no.isEmpty())
        {
            editTextMobile.setError("enter mobile no.");
            editTextMobile.requestFocus();
        }
        else if(mobile_no.length() < 10)
        {
            editTextMobile.setError("enter 10 digits");
            editTextMobile.requestFocus();
        }

        else if(adhar_no.isEmpty())
        {
            editTextAdhar.setError("enter adhar no.");
            editTextAdhar.requestFocus();
        }
//        else if (adhar_no.length() < 16)
//        {
//            editTextAdhar.setError("enter valid adhar no.");
//            editTextAdhar.requestFocus();
//        }
        else if (email.isEmpty()) {
            editText1.setError("provide email");
            editText1.requestFocus();
        }
        else if (!email.contains("@gmail.com")) {
            editText1.setError("not valid email");
            editText1.requestFocus();
        }
        else if (password.isEmpty()) {
            editText2.setError("enter password");
            editText1.requestFocus();
        }
        else {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful())
                                Snackbar.make(scrollView, "SignUp failed", Snackbar.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(getContext(), "Signed Up successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);

                                UserDetails userDetails = new UserDetails(email, name, mobile_no, adhar_no);
                                databaseReference.child(email).setValue(userDetails);
                                Toast.makeText(getContext(), "Data saved", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }
}
