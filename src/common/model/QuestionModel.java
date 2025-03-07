package common.model;

import java.io.Serializable;
import java.util.List;

public class QuestionModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String category;
    private String questionText;
    private List<String> choices;
    private String correctAnswer;
    private int score;

    public QuestionModel(String category, String questionText, List<String> choices, String correctAnswer, int score) {
        this.category = category;
        this.questionText = questionText;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getChoices() {
        return choices;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "QuestionModel{" +
                "category='" + category + '\'' +
                ", questionText='" + questionText + '\'' +
                ", choices=" + choices +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", score=" + score +
                '}';
    }
}
