import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;

public class Fish extends Item {
  public Fish(Cell inLoc) {
    loc = inLoc;
    color = Color.WHITE;
    display = new ArrayList<Polygon>();
    Polygon body = new Polygon();
    body.addPoint(loc.x + 5, loc.y + 13);
    body.addPoint(loc.x + 20, loc.y + 13);
    body.addPoint(loc.x + 20, loc.y + 22);
    body.addPoint(loc.x + 5, loc.y + 22);
    Polygon tail = new Polygon();
    tail.addPoint(loc.x + 20, loc.y + 18);
    tail.addPoint(loc.x + 30, loc.y + 13);
    tail.addPoint(loc.x + 30, loc.y + 22);
    display.add(body);
    display.add(tail);
  }
}