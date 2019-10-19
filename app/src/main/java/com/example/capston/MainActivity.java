package com.example.capston;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.capston.R.drawable.ic_play_circle;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    Uri videoUri;
    public static int VIDEO_CAPTURE =1;
    Button playButton, captureButton;

    VideoViewActivity videoViewActivity;

    ImageButton imageButton;
    TextView loginText, userText;
    ImageView imageView;
    BottomNavigationView bottomNavigationView;
    EditText loginEmail, loginPass;
    Fragment fragmentLogin;
    UserDetails userDetails;
    FloatingActionButton floatingActionButton;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    FragmentHome fragmentHome;
    FragmentPhoto fragmentPhoto;
    FragmentProfile fragmentProfile;
    FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        videoViewActivity = new VideoViewActivity();
//
//        videoView = videoViewActivity.findViewById(R.id.video_view);
        imageButton = findViewById(R.id.show_video);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VideoViewActivity.class);
//                intent.putExtra("video", videoUri);
                startActivity(intent);
            }
        });
//        playButton = findViewById(R.id.play);
//        captureButton = findViewById(R.id.capture);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        userDetails = new UserDetails();

        fragmentLogin = new LoginFragment();

//        loginPass = view.findViewById(R.id.password);

        final Toolbar toolbar = findViewById(R.id.tool);
        navigationView = findViewById(R.id.nav);
        floatingActionButton = findViewById(R.id.floating);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VideoViewActivity.class);
                startActivity(intent);
               // }
            }
        });

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

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            loginText.setText("Logout >>");
             final String uid = firebaseUser.getUid();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child(uid).child("name").getValue().toString();
                    userText.setText("Welcome "+name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(loginText.getText() != "Logout >>"){
                    Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
                    startActivity(intent);
                }

                else
                {
                  loginText.setText("Login");
                  userText.setText(null);
                  firebaseAuth.signOut();
                }
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        Intent intent = getIntent();
//        String name = intent.getStringExtra("name");
//        if(name != null)
//        {
////            userText.setVisibility(View.VISIBLE);
//            userText.setText("welcome "+name);
//        }
//    }

//    public void capture(View view)
//    {
//        Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
//        startActivity(intent);
//    }
//
//    public void play(View view)
//    {
//        videoView.setVideoURI(videoUri);
//        videoView.start();
//    }


//        @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == VIDEO_CAPTURE && resultCode == RESULT_OK)
//        {
//             videoUri = data.getData();
//
//             if(videoUri != null)
//             {
////                 videoView.setVideoURI(videoUri);
//                 imageButton.setBackgroundResource(R.drawable.ic_play_circle);
//
//             }
//        }
//    }
}
