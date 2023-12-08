package com.cs407.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import java.util.ArrayList;
import java.util.List;

public class EditProfile extends AppCompatActivity {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int PICK_IMAGE = 1002;
    private Uri image_uri;


    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
                // You can proceed with opening the camera or gallery
            } else {
                // Permission was denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                // Handle the feature without the permission or disable it
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ImageView profileImageView = findViewById(R.id.avatarImageView);
        requestPermissions();
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });



        Button saveButton = findViewById(R.id.svae_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextFirstname = findViewById(R.id.firstName);
                EditText editTextLastname = findViewById(R.id.lastName);
                EditText editTextEmail = findViewById(R.id.email);
                EditText editTextPassword = findViewById(R.id.Password);

                String  firstname= String.valueOf(editTextFirstname.getText());
                String  lastname = String.valueOf(editTextLastname.getText());
                String  email = String.valueOf(editTextEmail.getText());
                String  password = String.valueOf(editTextPassword.getText());


                if (!isValidEmail(email)) {
                    showErrorToast("Invalid email address.");
                    return;
                }

                if (!isPasswordStrong(password)) {
                    showErrorToast("Password must be at least 6 characters long, contain at least one number, and one lowercase letter.");
                    return;
                }
                Context context = getApplicationContext();
                SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("users", Context.MODE_PRIVATE, null);
                //get old email
                SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
                String oldEmail = preferences.getString("email", "default");

                DBHelper dbHelper = new DBHelper(sqLiteDatabase);
                dbHelper.deleteUser(oldEmail);
                String message = dbHelper.saveUser(firstname, lastname, email, password);
                Log.i("RegistrationTag", message);

                // Check if the message indicates a duplicate email and show a Toast in that case
                if (message.contains("email already registered")) {
                    //Toast.makeText(MainActivity.this, "duplicated email.", Toast.LENGTH_LONG).show();
                    showErrorToast(message);
                } else {
                    // Only proceed to the Home activity if the user was registered successfully
                    Intent intent = new Intent(EditProfile.this, Profile.class);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", email);
                    editor.putString("firstname",firstname);
                    editor.putString("lastname",lastname);
                    editor.apply();
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {
            ImageView profileImageView = findViewById(R.id.avatarImageView);
            // Set captured image to the ImageButton
            profileImageView.setImageURI(image_uri);
        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            // Set picked image to the ImageButton
            image_uri = data.getData();
            ImageView profileImageView = findViewById(R.id.avatarImageView);
            profileImageView.setImageURI(image_uri);
        }
    }

    private void showImagePickDialog() {
        List<String> options = new ArrayList<>();
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            options.add("Camera");
        }
        options.add("Gallery");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        builder.setItems(options.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ("Camera".equals(options.get(which))) {
                    // Camera option clicked
                    openCamera();
                } else {
                    // Gallery option clicked
                    openGallery();
                }
            }
        });
        builder.create().show();
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private boolean isValidEmail(String email) {
        // Check if email is valid
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isPasswordStrong(String password) {
        //  password that must be at least 6 characters long, contain at least one number, and one lowercase letter
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z]).{6,}$";
        return password.matches(passwordPattern);
    }

    private void showErrorToast(String message) {
        // Inflate the custom layout.
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout,
                (ViewGroup) findViewById(R.id.custom_toast_container)); // Replace with the ID of your container

        TextView text = (TextView) layout.findViewById(R.id.toast_text);
        text.setText(message); // Set your error message

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout); // Set the inflated layout
        toast.show();
    }
}