import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public abstract class Actor {
  List<Polygon> shapes;
  Cell loc;

  public Actor() {
    shapes = new ArrayList<>();
  }

  public void paint(Graphics g) {
    for (Polygon shape : shapes) {
      g.fillPolygon(shape);
      g.setColor(Color.GRAY);
      g.drawPolygon(shape);
    }
  }
}
