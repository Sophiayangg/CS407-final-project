package com.cs407.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, activity_catagories.class);
                startActivity(intent);
            }
        });

        ImageButton btnProfile = findViewById(R.id.btnUserProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Profile.class);
                startActivity(intent);
            }
        });
        ImageButton btnHome = findViewById(R.id.btnHome);

        // Set the OnClickListener for Home Button
        btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(Profile.this, Home.class);
            startActivity(intent);
        });

        SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
        String firstname = preferences.getString("firstname", "User");
        String lastname = preferences.getString("lastname","");
        TextView nameView = findViewById(R.id.profile_name);
        nameView.setText(firstname + " " +lastname);

        LinearLayout btnHistory = findViewById(R.id.my_history_bg);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Histories.class);
                startActivity(intent);
            }
        });

        LinearLayout btnEdit = findViewById(R.id.edit_profile_bg);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                startActivity(intent);
            }
        });

        LinearLayout btnLogOut = findViewById(R.id.logout_bg);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
            }
        });
    }
}