package com.example.sarvesh.bizhunt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


//        mToolbar=findViewById(R.id.users_appBar);
//        setSupportActionBar(mToolbar);
//
//        getSupportActionBar().setTitle("All Users");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//

        mUserList=findViewById(R.id.users_list);


    }


}
