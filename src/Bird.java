import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Graphics;

public class Bird extends Actor {
  private boolean visible = true;
  private boolean seedCollected = false;
  
  public Bird(Cell inLoc) {
    loc = inLoc;
    color = Color.RED;
    display = new ArrayList<Polygon>();
    Polygon wing1 = new Polygon();
    wing1.addPoint(loc.x + 5, loc.y + 5);
    wing1.addPoint(loc.x + 15, loc.y + 17);
    wing1.addPoint(loc.x + 5, loc.y + 17);
    Polygon wing2 = new Polygon();
    wing2.addPoint(loc.x + 30, loc.y + 5);
    wing2.addPoint(loc.x + 20, loc.y + 17);
    wing2.addPoint(loc.x + 30, loc.y + 17);
    Polygon body = new Polygon();
    body.addPoint(loc.x + 15, loc.y + 10);
    body.addPoint(loc.x + 20, loc.y + 10);
    body.addPoint(loc.x + 20, loc.y + 25);
    body.addPoint(loc.x + 15, loc.y + 25);
    display.add(body);
    display.add(wing1);
    display.add(wing2);
  }

  @Override
  public void moveRandomly(Grid grid) {
    List<Cell> neighbors = grid.getNeighbors(loc);
    neighbors.removeIf(cell -> cell instanceof SandCell); // Remove SandCell from possible moves

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

  public void checkSeedStatus(Item seed) {
    if (seed.isRemoved()) {
      seedCollected = true; // Hide the bird if the seed is removed
    }
  }

  public void checkProximityToPlayer(Player player) {
    int distanceX = Math.abs(loc.x - player.getLocation().x);
    int distanceY = Math.abs(loc.y - player.getLocation().y);

    if (distanceX <= Cell.size && distanceY <= Cell.size && seedCollected) {
      visible = false; // Hide the bird if near the player
    }
  }
}
