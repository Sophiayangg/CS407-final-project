package com.cs407.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class activity_catagories extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<CategoryItem> categoryItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);
        // Replace 'your_layout' with your actual layout
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
        // Prepare data
        categoryItems = new ArrayList<>();
        // Add CategoryItems to the list
        categoryItems.add(new CategoryItem(R.drawable.img_5938, "ASIAN"));
        categoryItems.add(new CategoryItem(R.drawable.img_5941, "DRINKS"));
        categoryItems.add(new CategoryItem(R.drawable.img_5942, "MEXICAN"));
        categoryItems.add(new CategoryItem(R.drawable.img_5943, "WESTERN"));
        categoryItems.add(new CategoryItem(R.drawable.img_5940, "INDIAN"));
        categoryItems.add(new CategoryItem(R.drawable.img_5946, "DESSERTS"));
        categoryItems.add(new CategoryItem(R.drawable.img, "AFRICAN"));
        categoryItems.add(new CategoryItem(R.drawable.img_5945, "OTHER"));

        // Set adapter
        adapter = new CategoryAdapter(this, categoryItems);
        recyclerView.setAdapter(adapter);

        // ... add other categories


        //AppCompatButton asianButton = findViewById(R.id.AsianButton);

//        setupCategoryButton(R.id.AsianButton, "Asian");
//        setupCategoryButton(R.id.MexicanButton, "Mexican");
//        setupCategoryButton(R.id.WesternButton, "Western");
//        setupCategoryButton(R.id.OthersButton, "Others");
//        setupCategoryButton(R.id.DessertsButton, "Desserts");
//        setupCategoryButton(R.id.IndianButton, "Indian");
//        setupCategoryButton(R.id.AfricanButton, "African");
//        setupCategoryButton(R.id.DrinksButton, "Drinks");

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
