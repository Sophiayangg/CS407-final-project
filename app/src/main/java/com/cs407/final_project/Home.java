package com.cs407.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class Home extends AppCompatActivity {

    // A set to keep track of selected search terms
    private Set<String> selectedSearchTerms = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView tvGreeting = findViewById(R.id.tvGreeting);
        String name = getIntent().getStringExtra("USER_NAME"); // Get the name passed from MainActivity
        if (name != null) {
            tvGreeting.setText("Hello, " + name + "!");
        } else {
            tvGreeting.setText("Hello, User!");
        }
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