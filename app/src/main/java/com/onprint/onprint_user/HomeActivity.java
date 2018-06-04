package com.onprint.onprint_user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new HomeFragment(), HomeFragment.class.getSimpleName())
                .commit();
    }
}
