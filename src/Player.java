import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

  public void move(Direction direction, Grid grid, List<Actor> actors, List<Item> items, Stage stage) {
    System.out.println("Move method called with direction: " + direction); // Debug output to confirm method call

    final int targetCol;
    final int targetRow;

    switch (direction) {
      case UP:
        targetCol = loc.col;
        targetRow = loc.row - 1;
        break;
      case DOWN:
        targetCol = loc.col;
        targetRow = loc.row + 1;
        break;
      case LEFT:
        targetCol = loc.col - 1;
        targetRow = loc.row;
        break;
      case RIGHT:
        targetCol = loc.col + 1;
        targetRow = loc.row;
        break;
      default:
        return; // Invalid direction
    }

    System.out.println("Attempting to move to: " + targetCol + ", " + targetRow); // Debug output

    Optional<Cell> newLoc = grid.getNeighbors(loc).stream()
      .filter(cell -> cell.col == targetCol && cell.row == targetRow)
      .findFirst();

    System.out.println("Target cell present: " + newLoc.isPresent()); // Debug output to check if cell exists

    newLoc.ifPresent(cell -> {
      int deltaX = cell.x - loc.x;
      int deltaY = cell.y - loc.y;
      loc = cell;
      updateDisplay(deltaX, deltaY);
      System.out.println("Player moved to: " + cell.col + cell.row); // Debug output after move

      // Check for item pickup
      items.removeIf(item -> {
        if (item.getLocation().equals(cell)) {
          System.out.println("Picked up item at: " + cell.col + cell.row); // Debug output for item pickup
          item.setRemoved(true); // Mark the item as removed
          stage.updateActorVisibility(); // Update actor visibility
          return true; // Remove the item
        }
        return false;
      });

      // Check proximity to other actors
      stage.checkActorProximityToPlayer(this);

      // Move other actors
      for (Actor actor : actors) {
        if (!(actor instanceof Player)) {
          actor.moveRandomly(grid);
        }
      }
    });
  }

  public Cell getLocation() {
    return loc;
  }
}