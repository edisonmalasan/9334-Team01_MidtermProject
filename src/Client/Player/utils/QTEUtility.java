package Client.Player.utils;
/**
 * Manages Quick Time Events in the game
 */

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class QTEUtility {
    private final Consumer<Integer> applyPenalty;
    private final Pane QTEPane;
    private final Random random = new Random();
    private final Set<Integer> qteTriggers;
    private final Timer timer = new Timer();

    public QTEUtility(int totalQuestions, Consumer<Integer> applyPenalty, Pane QTEPane) {
        this.applyPenalty = applyPenalty;
        this.QTEPane = QTEPane;
        this.qteTriggers = generateQTETriggers(totalQuestions);
    }

    /**
     * Generates random points in the quiz where Quick Time Events (QTEs) will trigger.
     */
    private Set<Integer> generateQTETriggers(int totalQuestions) {
        Set<Integer> qteTriggers = new HashSet<>();
        int qteCount = Math.min(3, totalQuestions); // Max 3 QTEs per game

        while (qteTriggers.size() < qteCount) {
            qteTriggers.add(random.nextInt(totalQuestions));
        }
        return qteTriggers;
    }

    /**
     * Triggers a Quick Time Event (QTE) by displaying a "Defuse" button at a random position.
     */
    public void triggerQuickTimeEvent(int currentQuestionIndex) {
        if (!qteTriggers.contains(currentQuestionIndex)) return;

        Platform.runLater(() -> {
            Button defuseButton = new Button();
            defuseButton.setStyle("-fx-background-image: url('/images/defuse_button.png'); " +
                    "-fx-background-size: contain; " +
                    "-fx-background-repeat: no-repeat; " +
                    "-fx-background-color: transparent; " +
                    "-fx-pref-width: 200px; -fx-pref-height: 80px;");

            double maxX = QTEPane.getWidth() - 200;
            double maxY = QTEPane.getHeight() - 80;
            double randomX = random.nextDouble() * maxX;
            double randomY = random.nextDouble() * maxY;

            defuseButton.setLayoutX(randomX);
            defuseButton.setLayoutY(randomY);
            QTEPane.getChildren().add(defuseButton);
            defuseButton.toFront();

            TimerTask failTask = new TimerTask() {
                @Override
                public void run() {
                    if (QTEPane.getChildren().contains(defuseButton)) {
                        Platform.runLater(() -> {
                            QTEPane.getChildren().remove(defuseButton);
                            int penalty = determineQTEPenalty();
                            applyPenalty.accept(penalty);
                        });
                    }
                }
            };
            timer.schedule(failTask, 3000); // 3 seconds to react

            defuseButton.setOnAction(e -> {
                defuseButton.setStyle("-fx-background-image: url('/images/defuse_button_clicked.png'); " +
                        "-fx-background-size: contain; " +
                        "-fx-background-repeat: no-repeat; " +
                        "-fx-background-color: transparent; " +
                        "-fx-pref-width: 200px; -fx-pref-height: 80px;");

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> QTEPane.getChildren().remove(defuseButton));
                    }
                }, 300);
                failTask.cancel();
            });
        });
    }

    /**
     * Determines how much time to deduct when a QTE is failed (relative to the 30-second timer).
     */
    private int determineQTEPenalty() {
        return 10;
    }
}
