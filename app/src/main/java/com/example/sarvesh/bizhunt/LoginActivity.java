package com.example.sarvesh.bizhunt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button mButton;
    private EditText email,password;
    ProgressDialog mprogress;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mprogress=new ProgressDialog(this);

        email=(EditText)findViewById(R.id.login_email);
        password=(EditText)findViewById(R.id.login_password);

        mButton=(Button)findViewById(R.id.login_btn);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail=email.getText().toString();
                String passwd=password.getText().toString();

                if(!TextUtils.isEmpty(mail)||!TextUtils.isEmpty(passwd))
                {
                    mprogress.setTitle("Logging In");
                    mprogress.setMessage("Please wait");
                    mprogress.setCanceledOnTouchOutside(false);
                    mprogress.show();

                    loginuser(mail,passwd);
                }

            }
        });
    }

    private void loginuser(String mail, String passwd) {

        mAuth.signInWithEmailAndPassword(mail, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    mprogress.dismiss();

                    Intent mainintent=new Intent(LoginActivity.this,MainActivity.class);
                    mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainintent);
                    finish();
                }
                else
                {
                    mprogress.hide();
                    Toast.makeText(LoginActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
