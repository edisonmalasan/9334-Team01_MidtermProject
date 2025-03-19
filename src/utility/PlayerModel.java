package utility;

public class PlayerModel {
    private String username;
    private String password;
    private String role;
    private int classicScore;
    private int endlessScore;

    public PlayerModel(String username, String password, String role, int classicScore, int endlessScore) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.classicScore = classicScore;
        this.endlessScore = endlessScore;
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

    @Override
    public String toString() {
        return "PlayerModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", classicScore=" + classicScore +
                ", endlessScore=" + endlessScore +
                '}';
    }
}
