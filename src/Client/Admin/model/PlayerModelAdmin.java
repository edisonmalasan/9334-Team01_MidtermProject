package Client.Admin.model;

import javafx.beans.property.*;

// paayos lol

public class PlayerModelAdmin {
    private final StringProperty username;
    private final IntegerProperty classicRank;
    private final IntegerProperty classicScore;
    private final IntegerProperty endlessRank;

    public PlayerModelAdmin(String username, int classicRank, int classicScore, int endlessRank) {
        this.username = new SimpleStringProperty(username);
        this.classicRank = new SimpleIntegerProperty(classicRank);
        this.classicScore = new SimpleIntegerProperty(classicScore);
        this.endlessRank = new SimpleIntegerProperty(endlessRank);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public int getClassicRank() {
        return classicRank.get();
    }

    public void setClassicRank(int classicRank) {
        this.classicRank.set(classicRank);
    }

    public IntegerProperty classicRankProperty() {
        return classicRank;
    }

    public int getClassicScore() {
        return classicScore.get();
    }

    public void setClassicScore(int classicScore) {
        this.classicScore.set(classicScore);
    }

    public IntegerProperty classicScoreProperty() {
        return classicScore;
    }

    public int getEndlessRank() {
        return endlessRank.get();
    }

    public void setEndlessRank(int endlessRank) {
        this.endlessRank.set(endlessRank);
    }

    public IntegerProperty endlessRankProperty() {
        return endlessRank;
    }
}
