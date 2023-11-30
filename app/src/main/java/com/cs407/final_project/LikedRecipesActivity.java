package com.cs407.final_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class LikedRecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liked_recipe);

        String category = getIntent().getStringExtra("category");
        DatabaseHelper db = new DatabaseHelper(this);
        List<Recipe> likedRecipes = db.getRecipesByCategory(category);

        TextView likedTextView = findViewById(R.id.liked_text);
        StringBuilder likedRecipesText = new StringBuilder("Liked " + category + " Recipes:\n");
        likedTextView.setText(likedRecipesText.toString());

        EditText[] items = new EditText[] {
                findViewById(R.id.item1),
                findViewById(R.id.item2),
                findViewById(R.id.item3),
                findViewById(R.id.item4),
                findViewById(R.id.item5),
                // ... add more EditText references
        };

        // Inside your loop where you are listing the recipes
        for (int i = 0; i < likedRecipes.size() && i < items.length; i++) {
            final int index = i; // Create a final copy of i
            final Recipe recipe = likedRecipes.get(index);
            final EditText currentItem = items[index];

            currentItem.setText(recipe.getName());

            currentItem.setOnClickListener(view -> {
                Intent intent = new Intent(LikedRecipesActivity.this, DetailActivity.class);
                intent.putExtra("RECIPE_ID", recipe.getId());
                startActivity(intent);
            });

            currentItem.setOnLongClickListener(view -> {
                confirmAndDeleteRecipe(recipe.getId(), currentItem);
                return true;
            });
        }




    }

    private void confirmAndDeleteRecipe(final int recipeId, final EditText editText) {
        new AlertDialog.Builder(LikedRecipesActivity.this)
                .setTitle("Delete Recipe")
                .setMessage("Are you sure you want to delete this recipe?")
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    DatabaseHelper db = new DatabaseHelper(LikedRecipesActivity.this);
                    db.deleteRecipe(recipeId);
                    editText.setText(""); // Clear the EditText
                    refreshRecipeList(); // Refresh the list to reflect the deletion
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void refreshRecipeList() {
        String category = getIntent().getStringExtra("category");
        DatabaseHelper db = new DatabaseHelper(this);
        List<Recipe> likedRecipes = db.getRecipesByCategory(category);

        EditText[] items = new EditText[] {
                findViewById(R.id.item1),
                findViewById(R.id.item2),
                findViewById(R.id.item3),
                findViewById(R.id.item4),
                findViewById(R.id.item5),
        };

        // Clear all EditTexts first
        for (EditText item : items) {
            item.setText("");
            item.setOnClickListener(null);
            item.setOnLongClickListener(null);
        }

        // Populate the EditTexts with updated recipes
        for (int i = 0; i < likedRecipes.size() && i < items.length; i++) {
            final int index = i; // Final copy of i for use in inner classes
            final Recipe recipe = likedRecipes.get(index);

            items[index].setText(recipe.getName());

            items[index].setOnClickListener(view -> {
                Intent intent = new Intent(LikedRecipesActivity.this, DetailActivity.class);
                intent.putExtra("RECIPE_ID", recipe.getId());
                startActivity(intent);
            });

            items[index].setOnLongClickListener(view -> {
                confirmAndDeleteRecipe(recipe.getId(), items[index]);
                return true;
            });
        }
    }










}
