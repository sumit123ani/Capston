package com.example.capston.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.capston.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment {

    FirebaseAuth firebaseAuth;

    private StorageReference storageReference;
    DatabaseReference databaseReference;
    String address, dob, email, gender, name, phonenumber, pincode, uid, adharNo;

    String profile_url = "https://capston-8b276.firebaseio.com/User/FmRrAsXU2oMdsl7TO3CwPSIyGu43/UserDetail/";
    RequestQueue requestQueue;


    ImageView dp;
    TextView full_Name, mobile_No, doB, email_id, adharid, addrss, gender_p, post_p;


    int year;
    private Uri uriProfileImage;

    private TextView back_to_home;

    public FragmentProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_profile, container, false);

        back_to_home = view.findViewById(R.id.back_to_home);
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));


        year = Calendar.getInstance().get(Calendar.YEAR);
        final LayoutInflater factory = getLayoutInflater();
        final View textEntryView = factory.inflate(R.layout.content_profile, null);

        dp = view.findViewById(R.id.image_view);

        full_Name = view.findViewById(R.id.patient_full_name);
        mobile_No = view.findViewById(R.id.patient_mobile);
        doB = view.findViewById(R.id.patient_dob);
        email_id = view.findViewById(R.id.patient_full_email);
//        state_p = view.findViewById(R.id.patient_state);
        adharid = view.findViewById(R.id.patient_adharid);
        addrss = view.findViewById(R.id.address);
//        father_p = view.findViewById(R.id.patient_father_name);
        gender_p = view.findViewById(R.id.gender);
//        post_p = view.findViewById(R.id.patient_post);


        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentHome newfragment = new FragmentHome();
                FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment, newfragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        Toast.makeText(getContext(), "" + uid, Toast.LENGTH_SHORT).show();
        String main = profile_url + ".json";
        profileData(main);

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);

                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1, 1);
            }
        });

        return view;
    }

    private void profileData(final String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    name = response.getString("name");
                    Toast.makeText(getContext(), "" + name, Toast.LENGTH_SHORT).show();
                    Log.d("name", name);
                    address = response.getString("address");
                    dob = response.getString("doB");
                    email = response.getString("email");
//                    gender = response.getString("gender");
                    adharNo = response.getString("adhar_no");
                    phonenumber = response.getString("mobile_No");
//                    pincode = response.getString("pincode");
//                    post = response.getString("post");
//                    state = response.getString("state");
//                    uid = response.getString("uid");

                    Log.d("url", url);
                    full_Name.setText(name);
                    mobile_No.setText(phonenumber);
                    doB.setText(dob);
                    email_id.setText(email);
//                    state_p.setText(state);
                    adharid.setText(adharNo);
                    addrss.setText(address);
//                    faher_p.setText(father);
                    gender_p.setText(gender);
//                 post_p.setText(post);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            if (resultCode == RESULT_OK) {
                uriProfileImage = data.getData();
                dp.setImageURI(uriProfileImage);
            }
    }
}
