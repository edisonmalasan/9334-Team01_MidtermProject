package Client.Player.utils;
/**
 * Manages the bomb in the game
 */

import exception.ThreadInterruptedException;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import java.util.List;

public class BombUtility {
    private final ImageView bombImage;
    private final ImageView flame;
    private final Line wick;
    private final Label timerLabel;
    private final Runnable explosionCallback;
    private final List<Button> choiceButtons;

    private Timeline wickAnimation;
    private TranslateTransition flameFlicker;
    private Timeline bombTimer;
    private final int totalTime = 120;
    private int remainingTime;
    private boolean hasExploded = false;
    private boolean isRunning = false;
    private boolean isEndlessMode;

    public BombUtility(ImageView bombImage, ImageView flame, Line wick, Label timerLabel,
                       Runnable explosionCallback, List<Button> choiceButtons, boolean isEndlessMode) {
        this.bombImage = bombImage;
        this.flame = flame;
        this.wick = wick;
        this.timerLabel = timerLabel;
        this.explosionCallback = explosionCallback;
        this.choiceButtons = choiceButtons;
        this.isEndlessMode = isEndlessMode;
    }

    public void startBombAnimation(boolean isEndlessMode) {
        if (isRunning) return;
        isRunning = true;

        bombImage.setVisible(true);
        flame.setVisible(true);
        wick.setVisible(true);
        hasExploded = false;

        flameFlicker = new TranslateTransition(Duration.millis(200), flame);
        flameFlicker.setFromX(-2);
        flameFlicker.setToX(2);
        flameFlicker.setAutoReverse(true);
        flameFlicker.setCycleCount(Animation.INDEFINITE);
        flameFlicker.play();

        if (!isEndlessMode) {
            remainingTime = totalTime;
            updateTimerLabel();

            double wickLength = wick.getEndX() - wick.getStartX();
            double shrinkAmountPerSecond = wickLength / totalTime;

            wickAnimation = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                if (wick.getStartX() < wick.getEndX()) {
                    wick.setStartX(wick.getStartX() + shrinkAmountPerSecond);
                    flame.setLayoutX(flame.getLayoutX() + shrinkAmountPerSecond);
                }
            }));

            wickAnimation.setCycleCount(totalTime);
            wickAnimation.play();

            bombTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                if (remainingTime > 0) {
                    remainingTime--;
                    updateTimerLabel();
                }

                if (remainingTime <= 0) {
                    triggerExplosion();
                }
            }));

            bombTimer.setCycleCount(totalTime);
            bombTimer.setOnFinished(e -> triggerExplosion());
            bombTimer.play();
        } else {
            timerLabel.setText("∞");
        }
    }

    public void applyPenalty(int penalty) {
        if (!isRunning || hasExploded) return;

        double wickLength = wick.getEndX() - wick.getStartX();

        if (isEndlessMode) {
            double shrinkAmount = 30;
            if (wickLength > 0) {
                wick.setStartX(wick.getStartX() + shrinkAmount);
                flame.setLayoutX(flame.getLayoutX() + shrinkAmount);
            }

            if (wick.getStartX() >= wick.getEndX()) {
                triggerExplosion();
            }
        } else {
            double shrinkAmount = wickLength / (totalTime / penalty);
            if (wickLength > 0) {
                wick.setStartX(wick.getStartX() + shrinkAmount);
                flame.setLayoutX(flame.getLayoutX() + shrinkAmount);
            }

            remainingTime = Math.max(0, remainingTime - penalty);
            updateTimerLabel();

            if (remainingTime <= 0 || wick.getStartX() >= wick.getEndX()) {
                triggerExplosion();
            }
        }
    }

    private void updateTimerLabel() {
        Platform.runLater(() -> timerLabel.setText(remainingTime + "s"));
    }

    public void stopBombAnimation() {
        if (wickAnimation != null) {
            wickAnimation.stop();
            wickAnimation = null;
        }
        if (flameFlicker != null) {
            flameFlicker.stop();
            flameFlicker = null;
        }
        if (bombTimer != null) {
            bombTimer.stop();
            bombTimer = null;
        }

        flame.setVisible(false);
        wick.setVisible(false);
    }

    private void triggerExplosion() {
        if (hasExploded) return;
        hasExploded = true;
        stopBombAnimation();
        bombImage.setImage(new Image("/images/explosion.png"));
        System.out.println("BOOM! The bomb explodes!");

        for (Button btn : choiceButtons) {
            btn.setDisable(true);
            btn.setOpacity(0.8);
        }

        Platform.runLater(() -> {
            try {
                Thread.sleep(1000); // wait 1 second before switching to scoreview
            } catch (InterruptedException e) {
                throw new ThreadInterruptedException("Thread was interrupted:", e);
            }

            explosionCallback.run();
        });
    }

    public boolean hasExploded() {
        return hasExploded;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
