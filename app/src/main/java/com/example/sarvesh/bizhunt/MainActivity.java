package com.example.sarvesh.bizhunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private RelativeLayout main;

    private ViewPager mviewPager;
    private SectionPagerAdapter msectionPagerAdapter;

    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mytoolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mytoolbar);

        mAuth = FirebaseAuth.getInstance();

        mviewPager=(ViewPager)findViewById(R.id.tabPager);
        msectionPagerAdapter =new SectionPagerAdapter(getSupportFragmentManager());

        mviewPager.setAdapter(msectionPagerAdapter);

        mTabLayout=(TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mviewPager);



    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser==null)
        {
           sendtostart();
        }
    }

    private void sendtostart() {

        Intent intent=new Intent(MainActivity.this,StartActivity.class);
        startActivity(intent);
        finish();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_sign_out)
        {
            FirebaseAuth.getInstance().signOut();
            //Log.i("Tag","Hello");
            Toast.makeText(MainActivity.this,"Successfully Logout",Toast.LENGTH_LONG).show();
            sendtostart();
        }

        if(item.getItemId()==R.id.main_setting_btn)
        {
            Intent settingItem=new Intent(MainActivity.this,SettingActivity.class);
            startActivity(settingItem);
        }

        if(item.getItemId()==R.id.main_all_btn){


            Intent settingItem=new Intent(MainActivity.this,UsersActivity.class);
            startActivity(settingItem);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }




}
