package com.cs407.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class HistoryRecipeDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_recipe_detail);
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        // Inflate the popup layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_note, null);
        // Create the PopupWindow
        int width = LinearLayout.LayoutParams.MATCH_PARENT;  // Full width of the parent
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        ImageButton arrow = findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryRecipeDetail.this, Histories.class);
                startActivity(intent);
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryRecipeDetail.this, activity_catagories.class);
                startActivity(intent);
            }
        });

        ImageButton btnProfile = findViewById(R.id.btnUserProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryRecipeDetail.this, Profile.class);
                startActivity(intent);
            }
        });
        ImageButton btnHome = findViewById(R.id.btnHome);

        // Set the OnClickListener for Home Button
        btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(HistoryRecipeDetail.this, Home.class);
            startActivity(intent);
        });

        int historyId = getIntent().getIntExtra("HISTORY_ID", -1);
        Log.d("HistoryID", "" + historyId);
        DatabaseHelper db = new DatabaseHelper(this);
        Recipe recipe = db.getHistoryById(historyId); // Implement this method in DatabaseHelper

        TextView nameText = findViewById(R.id.name_text);
        TextView ingredientText = findViewById(R.id.output_text);
        TextView instructionText = findViewById(R.id.instruction_intro_text);

        if (recipe != null) {
            nameText.setText(recipe.getName());
            ingredientText.setText(recipe.getIngredients());
            instructionText.setText(recipe.getInstructions());
        } else {
            nameText.setText("");
            ingredientText.setText("Details not available.");
            instructionText.setText("Details not available.");
        }

        ImageButton btnLike = findViewById(R.id.btnLike);
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper db = new DatabaseHelper(HistoryRecipeDetail.this);

                long recipeId = db.addRecipe(recipe);
                if (recipeId == -1) {
                    Toast.makeText(HistoryRecipeDetail.this, "Error adding recipe.", Toast.LENGTH_LONG).show();
                } else if (recipeId == -2) {
                    Toast.makeText(HistoryRecipeDetail.this, "This recipe already exists.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(HistoryRecipeDetail.this, "Recipe added successfully.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Test
    public void testGetRecipeDetails() {
        // Given a recipe ID
        int recipeId = 123;

        // Given a mock Recipe object
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(recipeId);
        mockRecipe.setName("Test Recipe");
        mockRecipe.setIngredients("Ingredient 1, Ingredient 2");
        mockRecipe.setInstructions("Step 1, Step 2");

        // When the repository returns the mock recipe for the given ID
        when(mockRepository.getRecipeById(recipeId)).thenReturn(mockRecipe);

        // When the presenter is asked to retrieve recipe details
        RecipeDetails recipeDetails = presenter.getRecipeDetails(recipeId);

        // Then verify that the returned recipe details match the mock recipe
        assertEquals(recipeId, recipeDetails.getId());
        assertEquals("Test Recipe", recipeDetails.getName());
        assertEquals("Ingredient 1, Ingredient 2", recipeDetails.getIngredients());
        assertEquals("Step 1, Step 2", recipeDetails.getInstructions());
    }
}