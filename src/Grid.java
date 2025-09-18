import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Grid {
  Cell[][] cells = new Cell[20][20];
  
  private Cell createRandomCell(int i, int j) {
    Random random = new Random();
    int cellType = random.nextInt(3);
    char colLabel = colToLabel(i);
    int x = 10 + Cell.size * i;
    int y = 10 + Cell.size * j;

    switch (cellType) {
      case 0:
        return new GrassCell(colLabel, j, x, y);
      case 1:
        return new WaterCell(colLabel, j, x, y);
      case 2:
        return new SandCell(colLabel, j, x, y);
      default:
        return new GrassCell(colLabel, j, x, y);
    }
  }

  public Grid() {
    for (int i = 0; i < cells.length; i++) {
      for (int j = 0; j < cells[i].length; j++) {
        cells[i][j] = createRandomCell(i, j);
      }
    }
  }

  private char colToLabel(int col) {
    return (char) (col + Character.valueOf('A'));
  }

  private int labelToCol(char col) {
    return (int) (col - Character.valueOf('A'));
  }

  public void paint(Graphics g, Point mousePos) {
    for(int i=0; i<cells.length; i++) {
      for(int j=0; j<cells[i].length; j++) {
        cells[i][j].paint(g, mousePos);
      }
    }
  }

  public Optional<Cell> cellAtColRow(int c, int r) {
    if(c >= 0 && c < cells.length && r >=0 && r < cells[c].length) {
      return Optional.of(cells[c][r]);
    } else {
      return Optional.empty();
    }
  }

  public Optional<Cell> cellAtColRow(char c, int r) {
    return cellAtColRow(labelToCol(c), r);
  }

  public Optional<Cell> cellAtPoint(Point p) {
    for(int i=0; i < cells.length; i++) {
      for(int j=0; j < cells[i].length; j++) {
        if(cells[i][j].contains(p)) {
          return Optional.of(cells[i][j]);
        }
      }
    }
    return Optional.empty();
  }

  public Optional<String> cellInfoAtPoint(Point p) {
    Optional<Cell> cell = cellAtPoint(p);
    if (cell.isPresent()) {
      return Optional.of(((Cell) cell.get()).getInfo());
    }
    return Optional.empty();
  }

  public List<Cell> getNeighbors(Cell cell) {
    List<Cell> neighbors = new ArrayList<>();
    int col = labelToCol(cell.col);
    int row = cell.row;

    int[] dCol = {-1, 0, 1, 0};
    int[] dRow = {0, -1, 0, 1};

    for (int i = 0; i < 4; i++) {
      int newCol = col + dCol[i];
      int newRow = row + dRow[i];
      cellAtColRow(newCol, newRow).ifPresent(neighbors::add);
    }

    return neighbors;
  }
}
