package com.nexdev.enyason.journalapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{



    FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    FirebaseAuth.AuthStateListener authStateListener;

    public static String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            email = firebaseAuth.getCurrentUser().getEmail();

        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if ( firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    finish();
                }
            }
        };

        loadFragment(new FragmentJournalEntry());


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view_home);



        bottomNavigationView.setOnNavigationItemSelectedListener(this);


    }



    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }

        return false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.menu_entry_list:
                fragment = new FragmentJournalEntry();
                break;
            case R.id.menu_fav:
                fragment = new FragmentFavourite();
                break;
            case R.id.menu_more:
                fragment = new FragmentMore();
                break;
        }

        return loadFragment(fragment);
    }




}


