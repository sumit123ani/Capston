package com.example.capston;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static com.example.capston.MainActivity.VIDEO_CAPTURE;

public class VideoViewActivity extends AppCompatActivity {

    ImageButton playButton, resumeButton, uploadbutton, recordButton, downloadButton;
    VideoView videoView;
    Uri videoUri;
    private static int REQUEST_CODE = 1;
    int stopPosition;
    ProgressBar progressBar;
    public long progress = 0;
    Handler handler;
    String url;
    EditText editText;
    String title;
    String getTitle;
    ArrayList<String> list;

    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference, databaseReference1;


//    String VideoUrl ="https://capston-8b276.firebaseio.com/User/fvzn6w0I14MfbGUkQ6ZGNrpjW5z2/.json";
    StorageReference videoRef, reference;
//    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

//        requestQueue = Volley.newRequestQueue(this);
//        showVideo(VideoUrl);

        handler = new Handler();

        playButton = findViewById(R.id.play);
        downloadButton = findViewById(R.id.download);
        resumeButton = findViewById(R.id.resume);
        uploadbutton = findViewById(R.id.upload);
        videoView = findViewById(R.id.video_view);
        recordButton = findViewById(R.id.record);
        progressBar = findViewById(R.id.progress);
        editText = findViewById(R.id.video_title);

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();
        videoRef = storageReference.child("/video/"+title+"userIntro.3gp");

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference1 =FirebaseDatabase.getInstance().getReference("User");

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

//        String vid = getIntent().getStringExtra("video");
//        videoUri = vid.get

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.start();
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.resume();
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    final File downloadFile = File.createTempFile("userIntro", "3gp");
//                    videoRef.getFile(downloadFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(VideoViewActivity.this, "Download Succesfull", Toast.LENGTH_SHORT).show();
//                            videoView.setVideoURI(Uri.fromFile(downloadFile));
//                            videoView.start();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(VideoViewActivity.this, "Download Failed "+getTitle, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                downLoad();
            }
        });

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = editText.getText().toString();
                videoRef = storageReference.child("/video/"+title+"userIntro.3gp");

                if(videoUri != null)
                {
                    UploadTask uploadTask = videoRef.putFile(videoUri);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VideoViewActivity.this, "Upload Failed" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            Task<Uri> url = taskSnapshot.getStorage().getDownloadUrl();
                            while (!url.isSuccessful());
                            Uri downloadUri = url.getResult();
                            Toast.makeText(VideoViewActivity.this, "Uploaded Succefully "+downloadUri.toString(), Toast.LENGTH_SHORT).show();

                            if(url != null) {
                                   String uid = firebaseUser.getUid();
                                UserDetails userDetails = new UserDetails(downloadUri.toString());

//                                String fileName = getFile

//                                Toast.makeText(VideoViewActivity.this, ""+url, Toast.LENGTH_LONG).show();
                                databaseReference.child(uid).child("VideoLink").child(""+title).setValue(userDetails);
                            }
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            updateProgress(taskSnapshot);
                        }
                    });
//                   String url = uri.toString();
//                    Toast.makeText(VideoViewActivity.this, ""+uri, Toast.LENGTH_SHORT).show();

                }
            }
        });

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                 if(intent.resolveActivity(getPackageManager()) != null) {
                     startActivityForResult(intent, REQUEST_CODE);
                 }
            }
        });
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
        }

    }
    public void updateProgress(UploadTask.TaskSnapshot taskSnapshot)
    {
        final long fileSize = taskSnapshot.getTotalByteCount();
        final long uploadedBytes = taskSnapshot.getBytesTransferred();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progress < 100)
                {
                    progress =  progress + (100*uploadedBytes)/fileSize;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress((int) progress);
                        }
                    });

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void downLoad()
    {
        reference = storageReference.child("hellouserIntro");
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadFile(getApplicationContext(), "hello", ".3gp", DIRECTORY_DOWNLOADS, url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VideoViewActivity.this, "Download Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destination, String url)
    {
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destination, fileName+fileExtension);
    }

//    private  void showVideo(String VideoUrl)
//    {
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, VideoUrl, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//                    JSONObject video = response.getJSONObject("VideoLink");
////                    Log.d("links", String.valueOf(video));
//                    String []videoLink = String.valueOf(video).split(",");
////                    Log.d("link", videoLink[0]);
//                    String v = videoLink[0].substring(14, videoLink[0].length()-2);
//                    Toast.makeText(VideoViewActivity.this, ""+v, Toast.LENGTH_SHORT).show();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//
//            }
//        });
//        requestQueue.add(jsonObjectRequest);
//    }
}
