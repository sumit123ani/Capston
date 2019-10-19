package com.example.capston;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

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

    StorageReference videoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        handler = new Handler();

        playButton = findViewById(R.id.play);
        downloadButton = findViewById(R.id.download);
        resumeButton = findViewById(R.id.resume);
        uploadbutton = findViewById(R.id.upload);
        videoView = findViewById(R.id.video_view);
        recordButton = findViewById(R.id.record);
        progressBar = findViewById(R.id.progress);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        videoRef = storageReference.child("/video/"+uid+"userIntro.3gp");

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
                try {
                    final File downloadFile = File.createTempFile("userIntro", "3gp");
                    videoRef.getFile(downloadFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(VideoViewActivity.this, "Download Succesfull", Toast.LENGTH_SHORT).show();
                            videoView.setVideoURI(Uri.fromFile(downloadFile));
                            videoView.start();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(VideoViewActivity.this, "Download Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                            Toast.makeText(VideoViewActivity.this, "Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            updateProgress(taskSnapshot);
                        }
                    });
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

}
