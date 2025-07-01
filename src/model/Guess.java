package model;

import java.util.List;

public class Guess {
    private String word;
    private List<Feedback> feedbackList;

    public Guess(String word, List<Feedback> feedbackList) {
        this.word = word;
        this.feedbackList = feedbackList;
    }

    public String getWord() { return word; }
    public List<Feedback> getFeedbackList() { return feedbackList; }
}