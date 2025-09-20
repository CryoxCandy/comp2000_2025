import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;
import java.util.Random;

public abstract class Actor {
  Color color;
  Cell loc;
  List<Polygon> display;

  public void paint(Graphics g) {
    for(Polygon p: display) {
      g.setColor(color);
      g.fillPolygon(p);
      g.setColor(Color.GRAY);
      g.drawPolygon(p);
    }
  }

  public void moveRandomly(Grid grid) {
    List<Cell> neighbors = grid.getNeighbors(loc);
    if (!neighbors.isEmpty()) {
      Random random = new Random();
      Cell newLoc = neighbors.get(random.nextInt(neighbors.size()));
      int deltaX = newLoc.x - loc.x;
      int deltaY = newLoc.y - loc.y;
      loc = newLoc;
      updateDisplay(deltaX, deltaY); // Translate polygons based on movement delta
    }
  }

  public void updateDisplay() {
    display.clear();
    Polygon body = new Polygon();
    body.addPoint(loc.x + 10, loc.y + 10);
    body.addPoint(loc.x + 25, loc.y + 10);
    body.addPoint(loc.x + 25, loc.y + 25);
    body.addPoint(loc.x + 10, loc.y + 25);
    display.add(body);
  }

  public void updateDisplay(int deltaX, int deltaY) {
    for (Polygon p : display) {
      p.translate(deltaX, deltaY);
    }
  }
  
}
