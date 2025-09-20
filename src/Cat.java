import java.awt.Color;
import java.awt.Polygon;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cat extends Actor {
  private boolean visible = true;
  private boolean fishCollected = false;
  public Cat(Cell inLoc) {
    loc = inLoc;
    color = Color.MAGENTA;
    display = new ArrayList<Polygon>();
    Polygon ear1 = new Polygon();
    ear1.addPoint(loc.x + 11, loc.y + 5);
    ear1.addPoint(loc.x + 15, loc.y + 15);
    ear1.addPoint(loc.x + 7, loc.y + 15);
    Polygon ear2 = new Polygon();
    ear2.addPoint(loc.x + 22, loc.y + 5);
    ear2.addPoint(loc.x + 26, loc.y + 15);
    ear2.addPoint(loc.x + 18, loc.y + 15);
    Polygon face = new Polygon();
    face.addPoint(loc.x + 5, loc.y + 15);
    face.addPoint(loc.x + 29, loc.y + 15);
    face.addPoint(loc.x + 17, loc.y + 30);
    display.add(face);
    display.add(ear1);
    display.add(ear2);
  }

  @Override
  public void moveRandomly(Grid grid) {
    List<Cell> neighbors = grid.getNeighbors(loc);
    neighbors.removeIf(cell -> cell instanceof WaterCell); // Remove WaterCell from possible moves

    if (!neighbors.isEmpty()) {
      Random random = new Random();
      Cell newLoc = neighbors.get(random.nextInt(neighbors.size()));
      int deltaX = newLoc.x - loc.x;
      int deltaY = newLoc.y - loc.y;
      loc = newLoc;
      updateDisplay(deltaX, deltaY);
    }
  }

  @Override
  public void paint(Graphics g) {
    if (!visible) {
      return; // Do not draw if not visible
    }
    super.paint(g);
  }
  public void checkFishStatus(Item fish) {
    if (fish.isRemoved()) {
      fishCollected = true; // Hide the cat if the fish is removed
    }
  }

  public void checkProximityToPlayer(Player player) {
    int distanceX = Math.abs(loc.x - player.getLocation().x);
    int distanceY = Math.abs(loc.y - player.getLocation().y);

    if (distanceX <= Cell.size && distanceY <= Cell.size && fishCollected) {
      visible = false; // Hide the cat if near the player
    }
  }
}
