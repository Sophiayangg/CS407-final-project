package com.cs407.final_project;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Retrieve user information from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
        email = preferences.getString("email", "User");
        String firstname = preferences.getString("firstname", "User");
        String lastname = preferences.getString("lastname", "");

        // Initialize UI elements
        ImageView profileImageView = findViewById(R.id.profile_img);
        TextView nameView = findViewById(R.id.profile_name);
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        ImageButton btnProfile = findViewById(R.id.btnUserProfile);
        ImageButton btnHome = findViewById(R.id.btnHome);
        LinearLayout btnHistory = findViewById(R.id.my_history_bg);
        LinearLayout btnEdit = findViewById(R.id.edit_profile_bg);
        LinearLayout btnLogOut = findViewById(R.id.logout_bg);

        // Load user profile image
        loadProfileImage();

        // Set user's name
        nameView.setText(firstname + " " + lastname);

        // Button click listeners
        btnMenu.setOnClickListener(view -> navigateToActivity(activity_catagories.class));
        btnProfile.setOnClickListener(view -> navigateToActivity(Profile.class));
        btnHome.setOnClickListener(view -> navigateToActivity(Home.class));
        btnHistory.setOnClickListener(view -> navigateToActivity(Histories.class));
        btnEdit.setOnClickListener(view -> navigateToActivity(EditProfile.class));
        btnLogOut.setOnClickListener(view -> logoutUser());
    }

    // Load user profile image
    private void loadProfileImage() {
        ImageView profileImageView = findViewById(R.id.profile_img);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, email + ".jpg");

        // Check if the file exists
        if (mypath.exists()) {
            Bitmap retrievedBitmap = BitmapFactory.decodeFile(mypath.getAbsolutePath());
            profileImageView.setImageBitmap(retrievedBitmap);
        } else {
            Log.e("Profile doesn't exist", email);
            // File doesn't exist, handle accordingly
        }
    }

    // Navigate to another activity
    private void navigateToActivity(Class<?> cls) {
        Intent intent = new Intent(Profile.this, cls);
        startActivity(intent);
    }

    // Logout user
    private void logoutUser() {
        SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("email");
        editor.remove("firstname");
        editor.remove("lastname");
        editor.apply();

        Intent intent = new Intent(Profile.this, Login.class);
        startActivity(intent);
    }
}
