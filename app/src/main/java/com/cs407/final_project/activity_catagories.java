package com.cs407.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class activity_catagories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories); // Replace 'your_layout' with your actual layout

        AppCompatButton asianButton = findViewById(R.id.AsianButton);
        asianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_catagories.this, LikedRecipesActivity.class);
                intent.putExtra("category", "Asian");
                startActivity(intent);
            }
        });

        // Other initializations if necessary
    }



}
