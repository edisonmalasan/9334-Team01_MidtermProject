package Client.Admin.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import java.util.List;
//TODO to be revised den
public class QuestionModelAdmin {
    private final StringProperty category;
    private final StringProperty text;
    private final SimpleListProperty<String> choices;
    private final StringProperty answer;
    private final SimpleIntegerProperty score;
    private final SimpleBooleanProperty visible;

    public QuestionModelAdmin(String category, String text, List<String> choices, String answer, int score) {
        this.category = new SimpleStringProperty(category);
        this.text = new SimpleStringProperty(text);
        this.choices = new SimpleListProperty<>(FXCollections.observableArrayList(choices));
        this.answer = new SimpleStringProperty(answer);
        this.score = new SimpleIntegerProperty(score);
        this.visible = new SimpleBooleanProperty(true);
    }


    public String getCategory() {
        return category.get();
    }

    public String getText() {
        return text.get();
    }

    public List<String> getChoices() {
        return choices.get();
    }

    public String getAnswer() {
        return answer.get();
    }

    public int getScore() {
        return score.get();
    }

    public boolean isVisible() {
        return visible.get();
    }


    public void setVisible(boolean visible) {
        this.visible.set(visible);
    }


    public StringProperty categoryProperty() {
        return category;
    }

    public StringProperty textProperty() {
        return text;
    }

    public SimpleListProperty<String> choicesProperty() {
        return choices;
    }

    public StringProperty answerProperty() {
        return answer;
    }

    public SimpleIntegerProperty scoreProperty() {
        return score;
    }

    public SimpleBooleanProperty visibleProperty() {
        return visible;
    }
}