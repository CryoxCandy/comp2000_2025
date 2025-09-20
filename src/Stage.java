import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Stage {
  Grid grid;
  List<Actor> actors;
  List<Item> items;

  public Stage() {
    grid = new Grid();
    actors = new ArrayList<Actor>();
    actors.add(new Cat(grid.cellAtColRow(0, 0).get()));
    actors.add(new Dog(grid.cellAtColRow(0, 15).get()));
    actors.add(new Bird(grid.cellAtColRow(12, 9).get()));    
    actors.add(new Player(grid.cellAtColRow(10, 12).get()));
    items = new ArrayList<Item>();
    items.add(new Bone(grid.cellAtColRow(5, 5).get()));
    items.add(new Fish(grid.cellAtColRow(15, 15).get()));
    items.add(new Seed(grid.cellAtColRow(3, 14).get()));
  }

  public void paint(Graphics g, Point mouseLoc) {
    grid.paint(g, mouseLoc);
    for (Actor a : actors) {
      a.paint(g);
    }
    for (Item i : items) {
      i.paint(g);
    }
    Optional<Cell> underMouse = grid.cellAtPoint(mouseLoc);
    if (underMouse.isPresent()) {
      Cell hoverCell = underMouse.get();
      g.setColor(Color.DARK_GRAY);
      g.drawString(String.valueOf(hoverCell.col) + String.valueOf(hoverCell.row), 740, 30);
      Optional<String> cellInfo = grid.cellInfoAtPoint(mouseLoc);
      cellInfo.ifPresent(info -> g.drawString(info, 740, 50));
    }
  }

  public void moveActors() {
    for (Actor actor : actors) {
      if (!(actor instanceof Player)) {
        actor.moveRandomly(grid);
      }
    }
  }

  public void updateActorVisibility() {
    for (Actor actor : actors) {
      if (actor instanceof Dog) {
        for (Item item : items) {
          if (item instanceof Bone) {
            ((Dog) actor).checkBoneStatus(item);
          }
        }
      }
      if (actor instanceof Cat) {
        for (Item item : items) {
          if (item instanceof Fish) {
            ((Cat) actor).checkFishStatus(item);
          }
        }
      }
      if (actor instanceof Bird) {
        for (Item item : items) {
          if (item instanceof Seed) {
            ((Bird) actor).checkSeedStatus(item);
          }
        }
      }
    }
  }

  public void checkActorProximityToPlayer(Player player) {
    for (Actor actor : actors) {
      if (actor instanceof Dog) {
        ((Dog) actor).checkProximityToPlayer(player);
      }
      if (actor instanceof Bird) {
        ((Bird) actor).checkProximityToPlayer(player);
      }
      if (actor instanceof Cat) {
        ((Cat) actor).checkProximityToPlayer(player);
      }
    }
  }
}
