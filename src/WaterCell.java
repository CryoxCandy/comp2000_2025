import java.awt.Color;

public class WaterCell extends Cell {
  private String depth;

  public WaterCell(char inCol, int inRow, int x, int y) {
    super(inCol, inRow, x, y);
    this.depth = "5 meters"; // Default value
  }

  @Override
  public Color getColor() {
    return Color.BLUE;
  }

  public String getInfo() {
    return "Depth: " + depth;
  }
}
