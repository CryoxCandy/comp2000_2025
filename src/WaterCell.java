import java.awt.Color;
import java.util.Random;

public class WaterCell extends Cell {
  private String depth;

  public WaterCell(char inCol, int inRow, int x, int y) {
    super(inCol, inRow, x, y);
    Random random = new Random();
    this.depth = (random.nextInt(49) + 2) + " meters"; // Random depth between 2 and 50 meters
  }

  @Override
  public Color getColor() {
    return Color.BLUE;
  }

  @Override
  public String getInfo() {
    return "Depth: " + depth;
  }
}
