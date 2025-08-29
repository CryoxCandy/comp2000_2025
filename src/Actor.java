import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public abstract class Actor {
  List<Polygon> shapes;
  Cell loc;
  Color color;

  public Actor() {
    shapes = new ArrayList<>();
  }

  public void paint(Graphics g) {
    for (Polygon shape : shapes) {
      g.setColor(color);
      g.fillPolygon(shape);
      g.setColor(Color.PINK);
      g.drawPolygon(shape);
    }
  }
}
