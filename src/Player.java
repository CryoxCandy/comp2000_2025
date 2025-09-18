import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;

public class Player extends Actor {
  public Player(Cell inLoc) {
    loc = inLoc;
    color = Color.WHITE;
    display = new ArrayList<Polygon>();

    Polygon body = new Polygon();
    body.addPoint(loc.x + 10, loc.y + 10);
    body.addPoint(loc.x + 25, loc.y + 10);
    body.addPoint(loc.x + 25, loc.y + 25);
    body.addPoint(loc.x + 10, loc.y + 25);

    Polygon head = new Polygon();
    head.addPoint(loc.x + 15, loc.y + 5);
    head.addPoint(loc.x + 20, loc.y + 5);
    head.addPoint(loc.x + 20, loc.y + 10);
    head.addPoint(loc.x + 15, loc.y + 10);

    display.add(body);
    display.add(head);
  }
}