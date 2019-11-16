package com.example.capston.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capston.MainActivity;
import com.example.capston.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

  EditText editText1, editText2;
  Button buttonLogin;
  FirebaseAuth firebaseAuth;
  LinearLayout linearLayout;
  TextView signUpText;
  FragmentManager fragmentManager;
  FragmentTransaction fragmentTransaction;


  public LoginFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_login, container, false);

    firebaseAuth = FirebaseAuth.getInstance();

    fragmentManager = getFragmentManager();

    editText1 = view.findViewById(R.id.email);
    editText2 = view.findViewById(R.id.password);
    buttonLogin = view.findViewById(R.id.login);
    linearLayout = view.findViewById(R.id.linear_layout);
    signUpText = view.findViewById(R.id.new_user);

    buttonLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        final String email = editText1.getText().toString();
        String password = editText2.getText().toString();

        if (email.isEmpty()) {
          editText1.setError("provide email");
          editText1.requestFocus();
        } else if (!email.contains("@gmail.com")) {
          editText1.setError("not valid email");
          editText1.requestFocus();
        } else if (password.isEmpty()) {
          editText2.setError("enter password");
          editText2.requestFocus();
        } else {

          firebaseAuth.signInWithEmailAndPassword(email, password)
                  .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if (!task.isSuccessful()) {
                        Snackbar.make(linearLayout, "Wrong Creditional enterd", Snackbar.LENGTH_SHORT).show();
                      } else {
                        Toast.makeText(getContext(), "Loged In", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("key", email);
                        startActivity(intent);
                      }
                    }
                  });
        }
      }
    });

    signUpText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag, new SignupFragment()).commit();
      }
    });

    return view;
  }


}