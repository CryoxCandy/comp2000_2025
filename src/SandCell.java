import java.awt.Color;
import java.util.Random;

public class SandCell extends Cell {
  private String temperature;

  public SandCell(char inCol, int inRow, int x, int y) {
    super(inCol, inRow, x, y);
    Random random = new Random();
    this.temperature = random.nextInt(31) + 10 + "°C"; // Random temperature between 10°C and 40°C
  }

  @Override
  public Color getColor() {
    return Color.YELLOW;
  }

  @Override
  public String getInfo() {
    return "Temperature: " + temperature;
  }
}
