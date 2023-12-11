package com.cs407.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class Profile extends AppCompatActivity {

    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
        email = preferences.getString("email", "User");

        ImageView profile = findViewById(R.id.profile_img);
        //retrieve the photo:
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, email+".jpg");
        Log.e("filename2",email);


        // Check if the file exists
        if (mypath.exists()) {
            Bitmap retrievedBitmap = BitmapFactory.decodeFile(mypath.getAbsolutePath());
            profile.setImageBitmap(retrievedBitmap);
        } else {
            Log.e("profile doesn't exist",email);
            // File doesn't exist, default
        }




        ImageView arrowHistoryPage = findViewById(R.id.arrowHistoryPage);
        arrowHistoryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Histories.class);
                startActivity(intent);
            }
        });

        ImageView arrowEditProfilePage = findViewById(R.id.arrowEditProfilePage);
        arrowHistoryPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                startActivity(intent);
            }
        });


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
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("email");
                editor.remove("firstname");
                editor.remove("lastname");
                Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
            }
        });
    }
}