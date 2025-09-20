import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dog extends Actor {
  private boolean visible = true;
  private boolean boneCollected = false;

  public Dog(Cell inLoc) {
    loc = inLoc;
    color = Color.ORANGE;
    display = new ArrayList<Polygon>();
    Polygon ear1 = new Polygon();
    ear1.addPoint(loc.x + 5, loc.y + 5);
    ear1.addPoint(loc.x + 15, loc.y + 5);
    ear1.addPoint(loc.x + 5, loc.y + 15);
    Polygon ear2 = new Polygon();
    ear2.addPoint(loc.x + 20, loc.y + 5);
    ear2.addPoint(loc.x + 30, loc.y + 5);
    ear2.addPoint(loc.x + 30, loc.y + 15);
    Polygon face = new Polygon();
    face.addPoint(loc.x + 8, loc.y + 7);
    face.addPoint(loc.x + 27, loc.y + 7);
    face.addPoint(loc.x + 27, loc.y + 25);
    face.addPoint(loc.x + 8, loc.y + 25);
    display.add(face);
    display.add(ear1);
    display.add(ear2);
  }
 @Override
  public void moveRandomly(Grid grid) {
    List<Cell> neighbors = grid.getNeighbors(loc);
    neighbors.removeIf(cell -> cell instanceof GrassCell); // Remove GrassCell from possible moves

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

  public void checkBoneStatus(Item bone) {
    if (bone.isRemoved()) {
      boneCollected = true; // Hide the dog if the bone is removed
    }
  }

  public void checkProximityToPlayer(Player player) {
    int distanceX = Math.abs(loc.x - player.getLocation().x);
    int distanceY = Math.abs(loc.y - player.getLocation().y);

    if (distanceX <= Cell.size && distanceY <= Cell.size && boneCollected) {
      visible = false; // Hide the dog if near the player
    }
  }
}
