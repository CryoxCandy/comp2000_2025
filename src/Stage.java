import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Stage {
  Grid grid;
  List<Actor> actors;

  public Stage() {
    grid = new Grid();
    actors = new ArrayList<>();
    grid.cellAtColRow(0, 0).ifPresent(cell -> actors.add(new Cat(cell)));
    grid.cellAtColRow(0, 15).ifPresent(cell -> actors.add(new Dog(cell)));
    grid.cellAtColRow(12, 9).ifPresent(cell -> actors.add(new Bird(cell)));
  }

  public void paint(Graphics g, Point mouseLoc) {
    grid.paint(g, mouseLoc);
    for (Actor actor : actors) {
      actor.paint(g);
    }

    if (mouseLoc != null) {
      grid.cellAtPoint(mouseLoc).ifPresent(cell -> {
        g.drawString("Cell Info: " + cell.toString(), 740, 20);
      });
    }
  }
}
