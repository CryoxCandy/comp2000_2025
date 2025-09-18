import java.awt.Color;

public class GrassCell extends Cell {
  private String windStrength;

  public GrassCell(char inCol, int inRow, int x, int y) {
    super(inCol, inRow, x, y);
    this.windStrength = "Moderate"; // Default value
  }

  @Override
  public Color getColor() {
    return Color.GREEN;
  }

  public String getInfo() {
    return "Wind Strength: " + windStrength;
  }
}
