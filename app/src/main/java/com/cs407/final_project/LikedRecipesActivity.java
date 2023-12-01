package com.cs407.final_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.List;

public class LikedRecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liked_recipe); // Ensure this layout has a ScrollView

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Set the Toolbar as the ActionBar

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView likedText = findViewById(R.id.liked_text);
        likedText.setText("Liked Recipe");
        // Show the back arrow button in the Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // Optional: if you want to use your own icon
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.arrow)); // Replace with your own icon resource
        }


        String category = getIntent().getStringExtra("category");
        DatabaseHelper db = new DatabaseHelper(this);
        List<Recipe> likedRecipes = db.getRecipesByCategory(category);

        TextView likedTextView = findViewById(R.id.liked_text);
        likedTextView.setText("Liked " + category + " Recipes:");

        LinearLayout recipesContainer = findViewById(R.id.likedItemsContainer); // Assuming this is your LinearLayout inside ScrollView

        // Clear existing views in the container
        recipesContainer.removeAllViews();

        // Populate the container with recipe names
        for (Recipe recipe : likedRecipes) {
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
                Intent intent = new Intent(LikedRecipesActivity.this, DetailActivity.class);
                intent.putExtra("RECIPE_ID", recipe.getId());
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
                    DatabaseHelper db = new DatabaseHelper(LikedRecipesActivity.this);
                    db.deleteRecipe(recipeId);
                    refreshRecipeList(); // Refresh the list to reflect the deletion
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshRecipeList() {
        String category = getIntent().getStringExtra("category");
        DatabaseHelper db = new DatabaseHelper(this);
        List<Recipe> likedRecipes = db.getRecipesByCategory(category);

        LinearLayout recipesContainer = findViewById(R.id.likedItemsContainer); // Assuming this is your LinearLayout inside ScrollView

        // Clear existing views in the container
        recipesContainer.removeAllViews();

        for (Recipe recipe : likedRecipes) {
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

            // Click listener for each TextView
            recipeNameTextView.setOnClickListener(view -> {
                Intent intent = new Intent(LikedRecipesActivity.this, DetailActivity.class);
                intent.putExtra("RECIPE_ID", recipe.getId());
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

}
