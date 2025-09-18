import java.awt.Color;

public class SandCell extends Cell {
  private String temperature;

  public SandCell(char inCol, int inRow, int x, int y) {
    super(inCol, inRow, x, y);
    this.temperature = "35°C"; // Default value
  }

  @Override
  public Color getColor() {
    return Color.YELLOW;
  }

  public String getInfo() {
    return "Temperature: " + temperature;
  }
}
