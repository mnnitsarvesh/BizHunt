package com.example.sarvesh.bizhunt;

import android.app.ApplicationErrorReport;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    private CircleImageView mDisplay_image;
    private TextView mName, mStatus;

    private Button mStatusbtn, mImagebtn;
    private final int GALLARY_PIC = 1;

    private ProgressDialog mProgressbar;

    private StorageReference mImagestorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mDisplay_image = (CircleImageView) findViewById(R.id.settings_image);
        mName = (TextView) findViewById(R.id.display_name);
        mStatus = (TextView) findViewById(R.id.setting_status);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


        mImagebtn = (Button) findViewById(R.id.setting_image_btn);
        mStatusbtn = (Button) findViewById(R.id.settign_status_btn);


        String Curreent_uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(Curreent_uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("Name").getValue().toString();
                String image = dataSnapshot.child("Image").getValue().toString();
                String status = dataSnapshot.child("Status").getValue().toString();
                String thumb_image = dataSnapshot.child("Thumb_image").getValue().toString();

                mImagestorage = FirebaseStorage.getInstance().getReference();

                mName.setText(name);
                mStatus.setText(status);
                Picasso.with(SettingActivity.this).load(image).into(mDisplay_image);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Error to Retriewing data", Toast.LENGTH_LONG).show();

            }
        });

        mStatusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status_value = mStatus.getText().toString();

                Intent intent_status = new Intent(SettingActivity.this, StatusActivity.class);
                intent_status.putExtra("status_value", status_value);
                startActivity(intent_status);
            }
        });


        mImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("Image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"SELECT IMAGE"), GALLARY_PIC);

                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(SettingActivity.this);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLARY_PIC&&requestCode==RESULT_OK) {

            Uri imageUri=data.getData();
            CropImage.activity(imageUri).setAspectRatio(1,1).start(this);

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mProgressbar=new ProgressDialog(SettingActivity.this);
                mProgressbar.setTitle("Uploading");
                mProgressbar.setMessage("Please Wait");
                mProgressbar.setCanceledOnTouchOutside(false);
                mProgressbar.show();


                Uri resultUri = result.getUri();
                String current_userid=mCurrentUser.getUid();
                StorageReference filepath=mImagestorage.child("profile_images").child(current_userid +".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            String download_uri=task.getResult().getDownloadUrl().toString();
                            mUserDatabase.child("Image").setValue(download_uri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        mProgressbar.dismiss();
                                        Toast.makeText(SettingActivity.this,"Succesfully Uploaded",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }else {
                            Toast.makeText(SettingActivity.this,"Error in uploading.........",Toast.LENGTH_LONG).show();
                            mProgressbar.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


}

























