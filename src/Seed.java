import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;

public class Seed extends Item {
  public Seed(Cell inLoc) {
    loc = inLoc;
    color = Color.WHITE;
    display = new ArrayList<Polygon>();
    Polygon seed1 = new Polygon();
    seed1.addPoint(loc.x + 5, loc.y + 10);
    seed1.addPoint(loc.x + 10, loc.y + 10);
    seed1.addPoint(loc.x + 10, loc.y + 15);
    seed1.addPoint(loc.x + 5, loc.y + 15);
    Polygon seed2 = new Polygon();
    seed2.addPoint(loc.x + 25, loc.y + 20);
    seed2.addPoint(loc.x + 30, loc.y + 20);
    seed2.addPoint(loc.x + 30, loc.y + 25);
    seed2.addPoint(loc.x + 25, loc.y + 25);
    Polygon seed3 = new Polygon();
    seed3.addPoint(loc.x + 13, loc.y + 23);
    seed3.addPoint(loc.x + 18, loc.y + 23);
    seed3.addPoint(loc.x + 18, loc.y + 18);
    seed3.addPoint(loc.x + 13, loc.y + 18);
    Polygon seed4 = new Polygon();
    seed4.addPoint(loc.x + 18, loc.y + 13);
    seed4.addPoint(loc.x + 23, loc.y + 13); 
    seed4.addPoint(loc.x + 23, loc.y + 18);
    seed4.addPoint(loc.x + 18, loc.y + 18);
    display.add(seed1);
    display.add(seed2);
    display.add(seed3);
    display.add(seed4);
  }
}