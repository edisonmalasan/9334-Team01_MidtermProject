package Client.User.model;
/**
 * Represents a player of the game
 */
import java.io.Serializable;

public class PlayerModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int score;

    public PlayerModel(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "PlayerModel{name='" + name + "', score=" + score + "}";
    }
}
