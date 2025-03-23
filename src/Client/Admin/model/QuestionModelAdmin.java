package Client.Admin.model;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
//TODO to be revise
// fix the connection  with controllers
import java.util.List;
public class QuestionModelAdmin {
    private final StringProperty category; // The category of the question
    private final StringProperty text; // The text of the question
    private final List<String> choices; // The list of answer choices
    private final StringProperty answer; // The correct answer
    private final SimpleIntegerProperty score; // The score for the question

    public QuestionModelAdmin(String category, String text, List<String> choices, String answer, int score) {
        this.category = new SimpleStringProperty(category);
        this.text = new SimpleStringProperty(text);
        this.choices = choices;
        this.answer = new SimpleStringProperty(answer);
        this.score = new SimpleIntegerProperty(score);
    }

    // Getters
    public String getCategory() {
        return category.get();
    }

    public String getText() {
        return text.get();
    }

    public List<String> getChoices() {
        return choices;
    }

    public String getAnswer() {
        return answer.get();
    }

    public int getScore() {
        return score.get();
    }

    // Property methods
    public StringProperty categoryProperty() {
        return category;
    }

    public StringProperty textProperty() {
        return text;
    }

    public StringProperty answerProperty() {
        return answer;
    }

    public SimpleIntegerProperty scoreProperty() {
        return score;
    }
}
