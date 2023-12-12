package com.cs407.final_project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


public class ImageUploadHelper {

    private static final int PICK_IMAGE = 1;


    public static void openImageChooser(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, PICK_IMAGE);
    }

    public static void uploadImage(ImageView imageView, Intent data) {
        if (data == null || data.getData() == null) {
            // No data or data URI available
            return;
        }

        Uri selectedImageUri = data.getData();
        try {
            // add additional logic to handle the uploaded image, sucha as sending image to a server.
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(imageView.getContext().getContentResolver(), selectedImageUri);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle the result of the image capture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the captured image
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Set the captured image to the avatarImageView
            avatarImageView.setImageBitmap(imageBitmap);
        }
    }


}
