package common.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class PlayerModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String role;
    private int classicScore;
    private int endlessScore;
    private boolean hasPlayedClassic;
    private boolean hasPlayedEndless;

    public PlayerModel(String username, String password, String role, int classicScore, int endlessScore,
                       boolean hasPlayedClassic, boolean hasPlayedEndless) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.classicScore = classicScore;
        this.endlessScore = endlessScore;
        this.hasPlayedClassic = hasPlayedClassic;
        this.hasPlayedEndless = hasPlayedEndless;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getClassicScore() {
        return classicScore;
    }

    public void setClassicScore(int classicScore) {
        this.classicScore = classicScore;
    }

    public int getEndlessScore() {
        return endlessScore;
    }
    public void setEndlessScore(int endlessScore) {
        this.endlessScore = endlessScore;
    }
    public void setHasPlayedClassic(boolean hasPlayedClassic) {
        this.hasPlayedClassic = hasPlayedClassic;
    }
    public boolean getHasPlayedClassic() {
        return hasPlayedClassic;
    }
    public void setHasPlayedEndless(boolean hasPlayedEndless) {
        this.hasPlayedEndless = hasPlayedEndless;
    }
    public boolean getHasPlayedEndless() {
        return hasPlayedEndless;
    }

    @Override
    public String toString() {
        return "PlayerModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", classicScore=" + classicScore +
                ", endlessScore=" + endlessScore +
                ", hasPlayedClassic=" + hasPlayedClassic +
                ", hasPlayedClassic=" + hasPlayedClassic +
                '}';
    }

    public boolean equals(PlayerModel other) {
        return Objects.equals(this.username, other.username) && Objects.equals(this.password, other.password)
                && Objects.equals(this.role, other.role) && this.classicScore == other.classicScore
                && this.endlessScore == other.endlessScore && this.hasPlayedClassic == other.hasPlayedClassic
                && this.hasPlayedEndless == other.hasPlayedEndless;
    }
}
