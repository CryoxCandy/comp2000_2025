import java.awt.Color;
import java.util.Random;

public class GrassCell extends Cell {
  private String windStrength;

  public GrassCell(char inCol, int inRow, int x, int y) {
    super(inCol, inRow, x, y);
    String[] strengths = {"None", "Weak", "Moderate", "Strong"};
    Random random = new Random();
    this.windStrength = strengths[random.nextInt(strengths.length)]; // Random wind strength
  }

  @Override
  public Color getColor() {
    return Color.GREEN;
  }

  @Override
  public String getInfo() {
    return "Wind Strength: " + windStrength;
  }
}
