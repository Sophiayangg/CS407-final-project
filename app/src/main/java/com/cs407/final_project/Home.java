package com.cs407.final_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Home extends AppCompatActivity {

    // A set to keep track of selected search terms
    private Set<String> selectedSearchTerms = new HashSet<>();
    private ExpandableListView expandableListView;
    private CustomExpandableListAdapter expandableListAdapter;
    private HashMap<String, List<String>> expandableListDetail;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final TextView tvGreeting = findViewById(R.id.tvGreeting);
        final TextView tvCookingPrompt = findViewById(R.id.tvCookingPrompt);

        // Initially hide the tvCookingPrompt
        tvCookingPrompt.setVisibility(View.INVISIBLE);

        // Set the text from the intent or default
        String name = getIntent().getStringExtra("USER_NAME");
        tvGreeting.setText(name != null ? "Hello, " + name + "!" : "Hello, User!");

        // Apply the scale up and fade in animation to tvGreeting
        Animation scaleUpFadeInForGreeting = AnimationUtils.loadAnimation(this, R.anim.scale_up_fade_in);
        tvGreeting.startAnimation(scaleUpFadeInForGreeting);

        // Animation listener for tvGreeting's animation
        scaleUpFadeInForGreeting.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation started for tvGreeting
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Make tvCookingPrompt visible and start its animation
                tvCookingPrompt.setVisibility(View.VISIBLE);
                Animation scaleUpFadeInForPrompt = AnimationUtils.loadAnimation(Home.this, R.anim.scale_up_fade_in);
                tvCookingPrompt.startAnimation(scaleUpFadeInForPrompt);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeats for tvGreeting
            }
        });

        // Inflate the popup layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_note, null);

        // Create the PopupWindow
        int width = LinearLayout.LayoutParams.MATCH_PARENT;  // Full width of the parent
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Show the PopupWindow anchored to the Note button
        ImageButton noteButton = findViewById(R.id.btnNote);
        noteButton.setOnClickListener(v -> {
            popupWindow.showAsDropDown(noteButton, 0, 0);
        });

        initializeData();

        // Setup ExpandableListView
        expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // If the group is expanded, collapse it, otherwise expand it
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
                    parent.expandGroup(groupPosition);
                }
                return true; // This tells the OS you've handled this click
            }
        });
        expandableListView.setVisibility(View.GONE);

        expandableListAdapter = new CustomExpandableListAdapter(this, new ArrayList<>(expandableListDetail.keySet()), expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        Button btnReset = findViewById(R.id.btnReset);
        Button btnDone = findViewById(R.id.btnDone);
        // Initially hide the buttons
        btnReset.setVisibility(View.GONE);
        btnDone.setVisibility(View.GONE);

        ImageButton filterButton = findViewById(R.id.filterButton);
        final ScrollView scrollViewFilterExpandable = findViewById(R.id.scrollViewFilterExpandable);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableListView.getVisibility() == View.GONE) {
                    // If currently hidden, show the ExpandableListView and collapse all groups
                    expandableListView.setVisibility(View.VISIBLE);
                    btnReset.setVisibility(View.VISIBLE);
                    btnDone.setVisibility(View.VISIBLE);
                    scrollViewFilterExpandable.setBackgroundResource(R.drawable.search_background); // Set desired background
                    int groupCount = expandableListAdapter.getGroupCount();
                    for (int i = 0; i < groupCount; i++) {
                        expandableListView.collapseGroup(i);
                    }
                } else {
                    // If currently visible, hide the ExpandableListView
                    expandableListView.setVisibility(View.GONE);
                    btnReset.setVisibility(View.GONE);
                    btnDone.setVisibility(View.GONE);
                    scrollViewFilterExpandable.setBackgroundResource(android.R.color.transparent);

                }
            }
        });


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // uncheck all checkboxes
                expandableListAdapter.resetAllCheckBoxes();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle done logic (e.g., apply filters and hide the ScrollView)
                expandableListView.setVisibility(View.GONE);
            }
        });



    }

//    // Methods to expand and collapse the ExpandableListView
//    private void expandExpandableListView() {
//        int groupCount = expandableListAdapter.getGroupCount();
//        //String s=Integer.toString(groupCount);
//        //Log.d("show", s);
//        for (int i = 0; i < groupCount; i++) {
//            expandableListView.expandGroup(i);
//        }
//    }
//
//
//    private void collapseExpandableListView() {
//        int groupCount = expandableListAdapter.getGroupCount();
//
//        for (int i = 0; i < groupCount; i++) {
//            expandableListView.collapseGroup(i);
//        }
//        //expandableListView.collapseGroup(0); // Collapse the first group (main group)
//    }

    private void initializeData() {
        expandableListDetail = new HashMap<>();

        List<String> cuisineTypes = new ArrayList<>();
        cuisineTypes.add("Italian");
        cuisineTypes.add("Chinese");
        cuisineTypes.add("American");
        cuisineTypes.add("Mediterranean");
        cuisineTypes.add("Mexican");
        cuisineTypes.add("Indian");
        expandableListDetail.put("Cuisine Type", cuisineTypes);

        List<String> Restrictions = new ArrayList<>();
        Restrictions.add("Vegetarian");
        Restrictions.add("Vegan");
        Restrictions.add("Gluten-Free");
        Restrictions.add("Dairy-Free");
        Restrictions.add("Keto");
        expandableListDetail.put("Dietary Restrictions", Restrictions);

        List<String> Type = new ArrayList<>();
        Type.add("Breakfast");
        Type.add("Lunch");
        Type.add("Dinner");
        Type.add("Snacks");
        Type.add("Desserts");
        expandableListDetail.put("Meal Type", Type);

        List<String> Time = new ArrayList<>();
        Time.add("Quick (under 30 minutes)");
        Time.add("Medium (30-60 minutes)");
        Time.add("Lengthy (over 1 hour)");
        expandableListDetail.put("Preparation Time", Time);

        List<String> Method = new ArrayList<>();
        Method.add("Baking");
        Method.add("Grilling");
        Method.add("Slow Cooking");
        Method.add("No-Cook");
        Method.add("Stir-Frying:");
        expandableListDetail.put("Cooking Method", Method);

        List<String> Nutri = new ArrayList<>();
        Nutri.add("Low-Calorie");
        Nutri.add("Low-Carb");
        Nutri.add("High-Protein");
        Nutri.add("Low-Fat");
        expandableListDetail.put("Nutritional Information", Nutri);

        List<String> Seasonal = new ArrayList<>();
        Seasonal.add("Spring");
        Seasonal.add("Summer");
        Seasonal.add("Fall");
        Seasonal.add("Winter");
        expandableListDetail.put("Seasonal/Local Ingredients", Seasonal);

        List<String> skill = new ArrayList<>();
        skill.add("Beginner");
        skill.add("Intermediate");
        skill.add("Advanced");
        expandableListDetail.put("Skill Level", skill);

        List<String> Rating = new ArrayList<>();
        Rating.add("Top Rated");
        Rating.add("Most Popular");
        expandableListDetail.put("Rating and Popularity", Rating);

        List<String> size = new ArrayList<>();
        size.add("Single Serving");
        size.add("Family Size");
        expandableListDetail.put("Serving Size", size);

        List<String> Allergens = new ArrayList<>();
        Allergens.add("Gluten-Free");
        Allergens.add("Nut-Free");
        expandableListDetail.put("Allergens", Allergens);
    }



    // The click handler for popular search terms
    public void onPopularTermClick(View view) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            String term = textView.getText().toString();

            // Check if the term is already selected
            if (selectedSearchTerms.contains(term)) {
                // If it is, remove it from the set and change the background to indicate it's not selected
                selectedSearchTerms.remove(term);
                textView.setBackground(getDrawable(R.drawable.popular_term_background));
            } else {
                // If it's not, add it to the set and change the background to indicate it's selected
                selectedSearchTerms.add(term);
                textView.setBackground(getDrawable(R.drawable.popular_term_background_selected));
            }

            updateSearchBar();
        }
    }

    // Update the search bar with the selected terms
    private void updateSearchBar() {
        EditText etSearch = findViewById(R.id.etSearch);
        // Join the selected terms with a space and set it to the search bar
        String searchTerms = TextUtils.join(" ", selectedSearchTerms);
        etSearch.setText(searchTerms);
    }



}