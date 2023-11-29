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

        //AppCompatButton asianButton = findViewById(R.id.AsianButton);

        setupCategoryButton(R.id.AsianButton, "Asian");
        setupCategoryButton(R.id.MexicanButton, "Mexican");
        setupCategoryButton(R.id.WesternButton, "Western");
        setupCategoryButton(R.id.OthersButton, "Others");
        setupCategoryButton(R.id.DessertsButton, "Desserts");
        setupCategoryButton(R.id.IndianButton, "Indian");
        setupCategoryButton(R.id.AfricanButton, "African");
        setupCategoryButton(R.id.DrinksButton, "Drinks");

    }

    private void setupCategoryButton(int buttonId, final String category) {
        AppCompatButton categoryButton = findViewById(buttonId);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_catagories.this, LikedRecipesActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });
    }



}
