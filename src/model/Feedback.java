package model;

import enums.FeedbackTypeEnum;

public class Feedback {
    private char letter;
    private FeedbackTypeEnum type;

    public Feedback(char letter, FeedbackTypeEnum type) {
        this.letter = letter;
        this.type = type;
    }

    public char getLetter() { return letter; }
    public FeedbackTypeEnum getType() { return type; }
}