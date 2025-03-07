package Client.controller;
/**
 * Controls classic game mode
 */
import common.model.QuestionModel;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassicGameController extends GameController {
    @Override
    protected void showNextQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            questionLabel.setText("🎉 Game Over! All Questions Have Been Exhausted");
            choicesBox.getChildren().clear();
            bombImage.setVisible(false);
            bombUtility.stopBombAnimation();

            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> switchToScoreView());
            delay.play();

            return;
        }

        QuestionModel question = questions.get(currentQuestionIndex);
        questionLabel.setText(question.getQuestionText());
        choicesBox.getChildren().clear();
        choiceButtons.clear();

        List<String> shuffledChoices = new ArrayList<>(question.getChoices());
        Collections.shuffle(shuffledChoices);

        for (String choice : question.getChoices()) {
            Button choiceButton = new Button(choice);
            choiceButton.setPrefSize(146, 50);
            choiceButton.setStyle("-fx-font-family: 'Roboto Mono'; -fx-font-size: 15px;");
            choiceButton.setOnAction(e -> checkAnswer(choiceButton, choice, question));
            choicesBox.getChildren().add(choiceButton);
            choiceButtons.add(choiceButton);
        }

        if (!bombUtility.isRunning()) {
            Platform.runLater(() -> bombUtility.startBombAnimation(false)); // classic mode
        }

        qteUtility.triggerQuickTimeEvent(currentQuestionIndex);
        currentQuestionIndex++;
    }
}