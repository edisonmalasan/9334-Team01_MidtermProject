package Client.Player.model;
/**
 * Represents a combo (correct answer streak)
 */
public class ComboModel {
    private int comboCount;
    private int highestCombo;

    public ComboModel() {
        this.comboCount = 0;
        this.highestCombo = 0;
    }

    public void incrementCombo() {
        comboCount++;
        if (comboCount > highestCombo) {
            highestCombo = comboCount;
        }
    }

    public void resetCombo() {
        comboCount = 0;
    }

    public int getComboCount() {
        return comboCount;
    }

    public void setComboCount(int comboCount) {
        this.comboCount = comboCount;
    }

    public int getHighestCombo() {
        return highestCombo;
    }

    public void setHighestCombo(int highestCombo) {
        this.highestCombo = highestCombo;
    }

    @Override
    public String toString() {
        return "ComboModel [comboCount=" + comboCount + ", highestCombo=" + highestCombo + "]";
    }
}
