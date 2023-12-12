package com.cs407.final_project;

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

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int PICK_IMAGE = 1002;
    private static final String TAG = "EditProfileActivity";

    private String email;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
        email = preferences.getString("email", "");

        ImageView profileImageView = findViewById(R.id.avatarImageView);
        profileImageView.setOnClickListener(this);

        requestPermissions();

        Button saveButton = findViewById(R.id.save_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.avatarImageView) {
            uploadImage();
        }
    }

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

    private void uploadImage() {
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
                    openCamera();
                } else {
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
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView profileImageView = findViewById(R.id.avatarImageView);
        if (resultCode == RESULT_OK) {
            handleImageResult(requestCode, data, profileImageView);
        }
    }

    private void handleImageResult(int requestCode, Intent data, ImageView profileImageView) {
        if (requestCode == IMAGE_CAPTURE_CODE) {
            if (imageUri != null) {
                setAndSaveImage(imageUri, profileImageView);
            }
        } else if (requestCode == PICK_IMAGE && data != null) {
            imageUri = data.getData();
            setAndSaveImage(imageUri, profileImageView);
        }
    }

    private void setAndSaveImage(Uri uri, ImageView imageView) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            imageView.setImageBitmap(bitmap);
            saveToInternalStorage(bitmap, email);
        } catch (IOException e) {
            Log.e(TAG, "Error handling image result: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveToInternalStorage(Bitmap bitmapImage, String fileName) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, fileName + ".jpg");

        if (mypath.exists()) {
            mypath.delete();
        }

        try (FileOutputStream fos = new FileOutputStream(mypath)) {
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 60, fos);
        } catch (IOException e) {
            Log.e(TAG, "Error saving image to internal storage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateProfile() {
        EditText editTextFirstname = findViewById(R.id.firstName);
        EditText editTextLastname = findViewById(R.id.lastName);
        EditText editTextPassword = findViewById(R.id.Password);

        String firstname = String.valueOf(editTextFirstname.getText());
        String lastname = String.valueOf(editTextLastname.getText());
        String password = String.valueOf(editTextPassword.getText());

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
            showErrorToast(message);
        } else {
            Intent intent = new Intent(EditProfile.this, Profile.class);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("firstname", firstname);
            editor.putString("lastname", lastname);
            editor.apply();
            startActivity(intent);
        }
    }

    private boolean isPasswordStrong(String password) {
        // Password must be at least 6 characters long, contain at least one number, and one lowercase letter
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z]).{6,}$";
        return password.matches(passwordPattern);
    }

    private void showErrorToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
