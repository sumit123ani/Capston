package com.example.capston;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class AuthenticationActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        fragmentManager = getSupportFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frag, new LoginFragment()).commit();
        Bundle bundle = getIntent().getExtras();
        int a = bundle.getInt("k");
//        int b = bundle.getInt("k1");

        if(a == 1)
        {
            fragmentManager.beginTransaction().replace(R.id.frag, new LoginFragment()).commit();
        }
        else {
            fragmentManager.beginTransaction().replace(R.id.frag, new SignupFragment()).commit();
        }
    }
}
