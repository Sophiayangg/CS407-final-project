package com.cs407.final_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class Histories extends AppCompatActivity {
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
        email = preferences.getString("email", "");
        Log.d("history email",email);
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Histories.this, activity_catagories.class);
                startActivity(intent);
            }
        });

        ImageButton arrow = findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Histories.this, Profile.class);
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
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar); // Set the Toolbar as the ActionBar

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
        List<Recipe> histories = db.getHistories(email);

        LinearLayout recipesContainer = findViewById(R.id.historiesContainer); // Assuming this is your LinearLayout inside ScrollView

        // Clear existing views in the container
        recipesContainer.removeAllViews();

        for (Recipe recipe : histories) {

            LinearLayout recipeLayout = new LinearLayout(this);
            recipeLayout.setOrientation(LinearLayout.HORIZONTAL);
            recipeLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            // Create an ImageView
            //ImageView recipeImageView = new ImageView(this);
            // Set the image for the ImageView (adjust this based on your data)
            //recipeImageView.setImageResource(R.drawable.recipe); // Replace with your image resource
            // Layout parameters for the ImageView
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    100,  // width in pixels (adjust as needed)
                    100); // height in pixels (adjust as needed)
            imageParams.setMargins(10, 10, 10, 10); // Optional margins
            //recipeImageView.setLayoutParams(imageParams);

            // Create a TextView for the recipe name
            TextView recipeNameTextView = new TextView(this);
            recipeNameTextView.setText(recipe.getName());
            recipeNameTextView.setTextSize(18);
            recipeNameTextView.setTextColor(Color.WHITE);
            recipeNameTextView.setGravity(Gravity.CENTER); // Centers text horizontally
            recipeNameTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_background));
            // Set the drawable on the left side of the text
            Drawable resizedDrawable = resizeDrawable(this, R.drawable.recipe, 40, 40); // Adjust the size as needed
            recipeNameTextView.setCompoundDrawablesWithIntrinsicBounds(resizedDrawable, null, null, null);
            recipeNameTextView.setCompoundDrawablePadding(8);

            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // Makes the TextView take full width of its parent
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textParams.setMargins(10, 15, 10, 15);
            recipeNameTextView.setLayoutParams(textParams);

            // Add ImageView and TextView to the LinearLayout
            //recipeLayout.addView(recipeImageView);
            recipeLayout.addView(recipeNameTextView);

            // Click listener for each TextView
            recipeLayout.setOnClickListener(view -> {
                Intent intent1 = new Intent(Histories.this, HistoryRecipeDetail.class);
                intent1.putExtra("HISTORY_ID", recipe.getId());
                startActivity(intent1);
            });

//            recipeLayout.setOnLongClickListener(view -> {
//                confirmAndDeleteRecipe(recipe.getId(), recipeLayout);
//                return true;
//            });

            // Add the LinearLayout to your container
            recipesContainer.addView(recipeLayout);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(Histories.this, Profile.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmAndDeleteRecipe(final int recipeId, final LinearLayout recipeLayout) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Recipe")
                .setMessage("Are you sure you want to delete this recipe?")
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    DatabaseHelper db = new DatabaseHelper(Histories.this);
                    db.deleteHistory(recipeId);
                    LinearLayout recipesContainer = findViewById(R.id.likedItemsContainer);
                    recipesContainer.removeView(recipeLayout); // Remove the entire recipe item
                    refreshRecipeList(); // Optionally refresh the list
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshRecipeList() {

        DatabaseHelper db = new DatabaseHelper(this);
        List<Recipe> histories = db.getHistories(email);

        LinearLayout recipesContainer = findViewById(R.id.historiesContainer); // Assuming this is your LinearLayout inside ScrollView

        // Clear existing views in the container
        recipesContainer.removeAllViews();

        // Populate the container with recipe names
        for (Recipe recipe : histories) {

            LinearLayout recipeLayout = new LinearLayout(this);
            recipeLayout.setOrientation(LinearLayout.HORIZONTAL);
            recipeLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            // Create an ImageView
            //ImageView recipeImageView = new ImageView(this);
            // Set the image for the ImageView (adjust this based on your data)
            //recipeImageView.setImageResource(R.drawable.recipe); // Replace with your image resource
            // Layout parameters for the ImageView
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    100,  // width in pixels (adjust as needed)
                    100); // height in pixels (adjust as needed)
            imageParams.setMargins(10, 10, 10, 10); // Optional margins
            //recipeImageView.setLayoutParams(imageParams);

            // Create a TextView for the recipe name
            TextView recipeNameTextView = new TextView(this);
            recipeNameTextView.setText(recipe.getName());
            recipeNameTextView.setTextSize(18);
            recipeNameTextView.setTextColor(Color.WHITE);
            recipeNameTextView.setGravity(Gravity.CENTER); // Centers text horizontally
            recipeNameTextView.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_background));
            // Set the drawable on the left side of the text
            Drawable resizedDrawable = resizeDrawable(this, R.drawable.recipe, 40, 40); // Adjust the size as needed
            recipeNameTextView.setCompoundDrawablesWithIntrinsicBounds(resizedDrawable, null, null, null);
            recipeNameTextView.setCompoundDrawablePadding(8);

            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // Makes the TextView take full width of its parent
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            textParams.setMargins(10, 15, 10, 15);
            recipeNameTextView.setLayoutParams(textParams);

            // Add ImageView and TextView to the LinearLayout
            //recipeLayout.addView(recipeImageView);
            recipeLayout.addView(recipeNameTextView);

            // Click listener for each TextView
            recipeLayout.setOnClickListener(view -> {
                Intent intent1 = new Intent(Histories.this, HistoryRecipeDetail.class);
                intent1.putExtra("HISTORY_ID", recipe.getId());
                startActivity(intent1);
            });

            recipeLayout.setOnLongClickListener(view -> {
                confirmAndDeleteRecipe(recipe.getId(), recipeLayout);
                return true;
            });

            // Add the LinearLayout to your container
            recipesContainer.addView(recipeLayout);
        }
    }

    private Drawable resizeDrawable(Context context, int drawableId, int width, int height) {
        Drawable image = ContextCompat.getDrawable(context, drawableId);
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, false);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }
}