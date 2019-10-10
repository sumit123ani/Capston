package com.example.capston;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView loginText, signText, userText, logoutText;
    ImageView imageView;
    BottomNavigationView bottomNavigationView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    FragmentHome fragmentHome;
    FragmentPhoto fragmentPhoto;
    FragmentProfile fragmentProfile;
    FragmentManager fragmentManager;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.tool);
        navigationView = findViewById(R.id.nav);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("News Reporter");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        bottomNavigationView = findViewById(R.id.bottom_nav);

        fragmentHome = new FragmentHome();
        fragmentPhoto = new FragmentPhoto();
        fragmentProfile = new FragmentProfile();
        fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragmentHome);
        fragmentTransaction.commit();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId())
                {
                    case R.id.home:
                        Toast.makeText(MainActivity.this, "home clicked", Toast.LENGTH_SHORT).show();
                        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
                        fragmentTransaction1.replace(R.id.fragment, fragmentHome).commit();
                        return true;

                    case R.id.photo:
                        FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                        fragmentTransaction2.replace(R.id.fragment, fragmentPhoto).commit();
                        return true;

                    case R.id.profile:
                        FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                        fragmentTransaction3.replace(R.id.fragment, fragmentProfile).commit();
                        return true;
                }

                return false;
            }
        });


          drawerLayout = findViewById(R.id.drawer);


          navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                  Toast.makeText(MainActivity.this, menuItem.getTitle()+"clicked", Toast.LENGTH_SHORT).show();
                  drawerLayout.closeDrawers();
                  return true;
              }
          });

        View headerView = navigationView.getHeaderView(0);

        loginText = headerView.findViewById(R.id.txt1);
        userText = headerView.findViewById(R.id.txt3);
        logoutText = headerView.findViewById(R.id.logout);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                intent.putExtra("k", 1);
                startActivity(intent);

                  if(!email.isEmpty()) {
                      databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

                      databaseReference.addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              String name = dataSnapshot.child(email).child("name").getValue().toString();
                              userText.setText(name);
                          }

                          @Override
                          public void onCancelled(@NonNull DatabaseError databaseError) {

                          }
                      });
                  }
//                loginText.setVisibility(View.INVISIBLE);
//                signText.setVisibility(View.INVISIBLE);
//                logoutText.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int k = item.getItemId();
        switch (k)
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}
