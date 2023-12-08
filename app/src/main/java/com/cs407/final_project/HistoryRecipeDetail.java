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

        ImageButton noteButton = findViewById(R.id.btnNote);
        noteButton.setOnClickListener(v -> {
            // Find the EditText and Done button in the popup
            EditText noteEditText = popupView.findViewById(R.id.noteEditText);
            AppCompatButton doneButton = popupView.findViewById(R.id.doneButton);
            // Set the click listener for the Done button
            doneButton.setOnClickListener(view -> {
                // Get the current recipe details
                //TextView tvChatGPTOutput = findViewById(R.id.tvChatGPTOutput);
                //Recipe recipe = parseRecipeFromTextView(tvChatGPTOutput);

                //DatabaseHelper db = new DatabaseHelper(Home.this);
                //long newRecipeIdLong = db.addRecipe(recipe);
                //int newRecipeId = (int) newRecipeIdLong; // Cast to int

                String noteContent = noteEditText.getText().toString();
                int recipeId = recipe.getId();
                Log.d("note", noteContent);
                if (db.isRecipeLiked(recipeId)) {
                    // If the recipe is already liked, insert to note directly
                    Note note = new Note(recipeId, noteContent);
                    db.addNote(note);
                    //db.updateNoteForRecipe(recipeId, noteContent);
                    Toast.makeText(HistoryRecipeDetail.this, "Note updated for the liked recipe!", Toast.LENGTH_SHORT).show();
                } else {
                    // If the recipe is not liked, save the recipe and the note
                    long newRecipeIdLong = db.addRecipe(recipe);
                    int newRecipeId = (int) newRecipeIdLong;

                    //db.addRecipe(recipe);
                    Note note = new Note(newRecipeId, noteContent);
                    db.addNote(note);
                    Toast.makeText(HistoryRecipeDetail.this, "Recipe liked and note saved!", Toast.LENGTH_SHORT).show();
                }
                // Dismiss the popup window after saving the note
                popupWindow.dismiss();
                //Toast.makeText(Home.this, "Note saved!", Toast.LENGTH_SHORT).show();
            });
            // Show the PopupWindow anchored to the Note button
            popupWindow.showAsDropDown(noteButton, 0, -900);
            // Display a Toast message

        });
    }
}