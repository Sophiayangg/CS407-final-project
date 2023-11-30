package com.cs407.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
        for (int i = 0; i < likedRecipes.size()&& i < items.length; i++) {
            Recipe recipe = likedRecipes.get(i);
            items[i].setText(recipe.getName());
            items[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LikedRecipesActivity.this, DetailActivity.class);
                    intent.putExtra("RECIPE_ID", recipe.getId());
                    startActivity(intent);
                }
            });
        }

    }


}
