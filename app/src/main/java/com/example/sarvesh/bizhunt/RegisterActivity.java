package com.example.sarvesh.bizhunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText mDisplayName,mEmail,mPassword;
    private Button mCreateBtn;
    private ProgressDialog mRegProgress;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegProgress=new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();



        mDisplayName=(EditText) findViewById(R.id.reg_display_name);
        mEmail=(EditText) findViewById(R.id.reg_email);
        mPassword=(EditText) findViewById(R.id.reg_password);

        mCreateBtn=(Button)findViewById(R.id.reg_create_btn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String str = eText.getText().toString();

                String display_name=mDisplayName.getText().toString();
                String email=mEmail.getText().toString();
                String password=mPassword.getText().toString();

                //Log.i("RegisterActivity","sarvesh patel........." );
                if(!TextUtils.isEmpty(display_name)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password))
                {
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("please wait");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    register_user(display_name,email,password);
                }


            }
        });

    }

    private void register_user(final String display_name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                    String uid=current_user.getUid();

                    mDatabase=FirebaseDatabase.getInstance().getReference().child("User").child(uid);

                    HashMap<String,String> usermap=new HashMap<>();
                    usermap.put("Name",display_name);
                    usermap.put("Status","Hi am sarvesh");
                    usermap.put("Image","Default");
                    usermap.put("Thumb_image","Default");

                    mDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                mRegProgress.dismiss();
                                Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    });
                }
                else
                {
                    mRegProgress.hide();
                    Toast.makeText(RegisterActivity.this,"you got some error",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
