package com.cs407.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
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
import android.Manifest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.content.ContextWrapper;

public class EditProfile extends AppCompatActivity {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int PICK_IMAGE = 1002;

    private String email;

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
        SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
        email = preferences.getString("email", "");

        ImageView profileImageView = findViewById(R.id.avatarImageView);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, email+".jpg");

        // Check if the file exists
        if (mypath.exists()) {
            Bitmap retrievedBitmap = BitmapFactory.decodeFile(mypath.getAbsolutePath());
            profileImageView.setImageBitmap(retrievedBitmap);
        } else {
            //default
        }

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
                EditText editTextPassword = findViewById(R.id.Password);

                String  firstname= String.valueOf(editTextFirstname.getText());
                String  lastname = String.valueOf(editTextLastname.getText());
                String  password = String.valueOf(editTextPassword.getText());


                if (!isPasswordStrong(password)) {
                    showErrorToast("Password must be at least 6 characters long, contain at least one number, and one lowercase letter.");
                    return;
                }

                Context context = getApplicationContext();
                SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("users", Context.MODE_PRIVATE, null);

                DBHelper dbHelper = new DBHelper(sqLiteDatabase);
                dbHelper.deleteUser(email);
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
                    editor.putString("firstname",firstname);
                    editor.putString("lastname",lastname);
                    editor.apply();
                    startActivity(intent);
                }
            }
        });
    }


    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView profileImageView = findViewById(R.id.avatarImageView);
        if (resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {
            // Set captured image to the ImageButton
            profileImageView.setImageURI(image_uri);
            Bitmap bitmap = null;
            try {
                // Retrieve the image from the URI
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                // Save the image to local storage
                saveToInternalStorage(bitmap, email);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            // Set picked image to the ImageButton
            image_uri = data.getData();
            int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(image_uri, takeFlags);
            Bitmap bitmap = null;
            try {
                // Retrieve the image from the URI
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image_uri);
                // Save the image to local storage
                saveToInternalStorage(bitmap, email);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveToInternalStorage(Bitmap bitmapImage, String fileName) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, fileName + ".jpg");
        Log.e("filename1",fileName);
        FileOutputStream fos = null;
        if(mypath.exists()){
            mypath.delete();
        }
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("profile1",e.getMessage());
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    Log.e("profile2",e.getMessage());
                    e.printStackTrace();
                }
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