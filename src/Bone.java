import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;

public class Bone extends Item {
  public Bone(Cell inLoc) {
    loc = inLoc;
    color = Color.WHITE;
    display = new ArrayList<Polygon>();
    Polygon leftBone = new Polygon();
    leftBone.addPoint(loc.x + 5, loc.y + 10);
    leftBone.addPoint(loc.x + 10, loc.y + 10);
    leftBone.addPoint(loc.x + 10, loc.y + 25);
    leftBone.addPoint(loc.x + 5, loc.y + 25);
    Polygon rightBone = new Polygon();
    rightBone.addPoint(loc.x + 25, loc.y + 10);
    rightBone.addPoint(loc.x + 30, loc.y + 10);
    rightBone.addPoint(loc.x + 30, loc.y + 25);
    rightBone.addPoint(loc.x + 25, loc.y + 25);
    Polygon body = new Polygon();
    body.addPoint(loc.x + 10, loc.y + 20);
    body.addPoint(loc.x + 25, loc.y + 20);
    body.addPoint(loc.x + 25, loc.y + 15);
    body.addPoint(loc.x + 10, loc.y + 15);
    display.add(body);
    display.add(leftBone);
    display.add(rightBone);
  }
}