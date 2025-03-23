package Client.User.controller;
/**
 * Controls endless game mode
 */
import common.model.QuestionModel;
import javafx.application.Platform;
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class EndlessGameController extends GameController {
    @Override
    protected void showNextQuestion() {
        if (bombUtility.hasExploded()) {
            logger.info("\nEndlessGameController: Bomb exploded! Ending game...");
            switchToScoreView();
            return;
        }

        if (currentQuestionIndex >= questions.size()) {
            Collections.shuffle(questions);
            currentQuestionIndex = 0;
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
            Platform.runLater(() -> bombUtility.startBombAnimation(true)); // endless mode
        }

        currentQuestionIndex++;
        checkMode = true;
    }

}