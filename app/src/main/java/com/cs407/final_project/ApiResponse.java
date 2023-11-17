package com.cs407.final_project;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {
    @SerializedName("choices")
    private List<Choice> choices;

    // Getter and setter for choices
    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public static class Choice {
        @SerializedName("text")
        private String text;

        // Getter and setter for text
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
