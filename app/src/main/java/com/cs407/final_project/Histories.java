package com.cs407.final_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class Histories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_recipe);
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Histories.this, activity_catagories.class);
                startActivity(intent);
            }
        });

        ImageButton btnProfile = findViewById(R.id.btnUserProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Histories.this, Profile.class);
                startActivity(intent);
            }
        });
        ImageButton btnHome = findViewById(R.id.btnHome);

        // Set the OnClickListener for Home Button
        btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(Histories.this, Home.class);
            startActivity(intent);
        });

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Set the Toolbar as the ActionBar

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView likedText = findViewById(R.id.history_text);
        likedText.setText("Histories");
        // Show the back arrow button in the Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // Optional: if you want to use your own icon
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.arrow)); // Replace with your own icon resource
        }

        DatabaseHelper db = new DatabaseHelper(this);
        List<Recipe> histories = db.getHistories();

        LinearLayout recipesContainer = findViewById(R.id.historiesContainer); // Assuming this is your LinearLayout inside ScrollView

        // Clear existing views in the container
        recipesContainer.removeAllViews();

        // Populate the container with recipe names
        for (Recipe recipe : histories) {
            TextView recipeNameTextView = new TextView(this);
            recipeNameTextView.setText(recipe.getName());
            recipeNameTextView.setTextSize(18);
            recipeNameTextView.setTextColor(Color.WHITE); // White text for better contrast with blue background

            // Set the custom blue background
            recipeNameTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_background));

            // Layout parameters (margins, etc.)
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 15, 10, 15);
            recipeNameTextView.setLayoutParams(params);
            // Set other TextView attributes as needed

            // Click listener for each TextView
            recipeNameTextView.setOnClickListener(view -> {
                Intent intent = new Intent(Histories.this, HistoryRecipeDetail.class);
                intent.putExtra("HISTORY_ID", recipe.getId());
                startActivity(intent);
            });

            // Long click listener for deletion
            recipeNameTextView.setOnLongClickListener(view -> {
                confirmAndDeleteRecipe(recipe.getId(), recipeNameTextView);
                return true;
            });

            recipesContainer.addView(recipeNameTextView);
        }
    }
    private void confirmAndDeleteRecipe(final int recipeId, final TextView textView) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Recipe")
                .setMessage("Are you sure you want to delete this recipe?")
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    DatabaseHelper db = new DatabaseHelper(Histories.this);
                    db.deleteHistory(recipeId);
                    refreshRecipeList(); // Refresh the list to reflect the deletion
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshRecipeList() {

        DatabaseHelper db = new DatabaseHelper(this);
        List<Recipe> histories = db.getHistories();

        LinearLayout recipesContainer = findViewById(R.id.historiesContainer); // Assuming this is your LinearLayout inside ScrollView

        // Clear existing views in the container
        recipesContainer.removeAllViews();

        // Populate the container with recipe names
        for (Recipe recipe : histories) {
            TextView recipeNameTextView = new TextView(this);
            recipeNameTextView.setText(recipe.getName());
            recipeNameTextView.setTextSize(18);
            recipeNameTextView.setTextColor(Color.WHITE); // White text for better contrast with blue background

            // Set the custom blue background
            recipeNameTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_background));

            // Layout parameters (margins, etc.)
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 15, 10, 15);
            recipeNameTextView.setLayoutParams(params);
            // Set other TextView attributes as needed

            // Click listener for each TextView
            recipeNameTextView.setOnClickListener(view -> {
                Intent intent1 = new Intent(Histories.this, HistoryRecipeDetail.class);
                intent1.putExtra("HISTORY_ID", recipe.getId());
                startActivity(intent1);
            });

            // Long click listener for deletion
            recipeNameTextView.setOnLongClickListener(view -> {
                confirmAndDeleteRecipe(recipe.getId(), recipeNameTextView);
                return true;
            });

            recipesContainer.addView(recipeNameTextView);
        }
    }
}