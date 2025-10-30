import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Stage {
  Grid grid;
  List<Actor> listOfPlayers;
  List<Cell> cellOverlay;
  Optional<Actor> playerInAction;
  GameState currentState;
  Beat beat;
  WeatherTracker weatherTracker;

  public Stage() {
    grid = new Grid();
    listOfPlayers = new ArrayList<Actor>();
    cellOverlay = new ArrayList<Cell>();
    playerInAction = Optional.empty();
    currentState = new ChoosingActor();
    beat = new AnimationBeat();
    weatherTracker = new WeatherTracker();
    weatherTracker.start();
  }

  public void addPlayer(Actor player) {
    listOfPlayers.add(player);
    if(player.isBot()) {
      beat.punchIn(player);
    }
  }

  public void paint(Graphics g, Point mouseLoc) {
    // checks for bot moves needed to be made
    currentState.paint(g, this);
    grid.paint(g, mouseLoc);

    // Highlight harsh weather (red) and rest spots (green)
    highlightWeatherConditions(g);
    
    // Blue cell selection overlay with 50% transparency
    grid.paintOverlay(g, cellOverlay, new Color(0f, 0f, 1f, 0.5f));

    beat.ticktock();
    
    // Don't update energy in paint loop - only paint actors
    for(Actor player: listOfPlayers) {
      player.paint(g);
    }
    
    draw_sidepanel(g, mouseLoc);
  }

  private void highlightWeatherConditions(Graphics g) {
    // Get all weather conditions
    for (int x = 0; x < 20; x++) {
      for (int y = 0; y < 20; y++) {
        Optional<WeatherCondition> weather = weatherTracker.getWeatherAt(x, y);
        if (weather.isPresent()) {
          float harshness = weather.get().getHarshness();
          Optional<Cell> cell = grid.cellAtColRow(x, y);
          
          if (cell.isPresent()) {
            Cell c = cell.get();
            
            // Red tint for harsh weather (>0.7)
            if (harshness > 0.7f) {
              g.setColor(new Color(255, 0, 0, 40)); // Red with transparency
              g.fillRect(c.x + 2, c.y + 2, c.width - 4, c.height - 4);
            }
            // Green tint for mild weather (<0.4) - rest spots
            else if (harshness < 0.4f) {
              g.setColor(new Color(0, 255, 0, 30)); // Green with transparency
              g.fillRect(c.x + 2, c.y + 2, c.width - 4, c.height - 4);
            }
          }
        }
      }
    }
  }

  private void draw_sidepanel(Graphics g, Point mouseLoc) {
    final int hTab = 10;
    final int blockVT = 35;
    final int margin = 21*blockVT;
    int yLoc = 20;

    // state display
    g.setColor(Color.DARK_GRAY);
    g.drawString(currentState.toString(), margin, yLoc);
    yLoc = yLoc + blockVT;
    
    Optional<Cell> underMouse = grid.cellAtPoint(mouseLoc);
    if(underMouse.isPresent()) {
      Cell hoverCell = underMouse.get();
      g.setColor(Color.DARK_GRAY);
      String coord = String.valueOf(hoverCell.col) + String.valueOf(hoverCell.row);
      g.drawString(coord, margin, yLoc);
      
      // Show weather harshness at this cell
      Optional<WeatherCondition> weather = weatherTracker.getWeatherAt(
          (int)(hoverCell.col - 'A'), 
          hoverCell.row
      );
      if (weather.isPresent()) {
        yLoc += 15;
        float harshness = weather.get().getHarshness();
        if (harshness < 0.4f) {
          g.setColor(Color.GREEN);
          g.drawString("Mild weather", margin, yLoc);
        } else if (harshness < 0.7f) {
          g.setColor(Color.ORANGE);
          g.drawString("Moderate weather", margin, yLoc);
        } else {
          g.setColor(Color.RED);
          g.drawString("Harsh weather", margin, yLoc);
        }
        yLoc += 15;
        g.setColor(Color.DARK_GRAY);
        g.drawString(String.format("Harshness: %.2f", harshness), margin, yLoc);
      }
    }

    // agent display
    final int vTab = 15;
    final int labelIndent = margin + hTab;
    final int valueIndent = margin + 3*blockVT;
    yLoc = yLoc + 2*blockVT;
    for(int i = 0; i < listOfPlayers.size(); i++){
      Actor a = listOfPlayers.get(i);
      yLoc = yLoc + 2*blockVT;
      g.drawString(a.getClass().getName(), margin, yLoc);
      g.drawString("location:", labelIndent, yLoc+vTab);
      g.drawString(Character.toString(a.loc.col) + Integer.toString(a.loc.row), valueIndent, yLoc+vTab);
      g.drawString("player type:", labelIndent, yLoc+2*vTab);
      g.drawString(a.isBot() ? "Bot" : "Human", valueIndent, yLoc+2*vTab);
      
      // Show energy level
      g.drawString("energy:", labelIndent, yLoc+3*vTab);
      float energy = a.getEnergy();
      if (energy > 60) g.setColor(Color.GREEN);
      else if (energy > 30) g.setColor(Color.ORANGE);
      else g.setColor(Color.RED);
      g.drawString(String.format("%.0f%%", energy), valueIndent, yLoc+3*vTab);
      g.setColor(Color.DARK_GRAY);
      
      if(a.isBot() && a.mover != null) {
        g.drawString("mover:", labelIndent, yLoc+4*vTab);
        g.drawString(a.mover.getClass().getName(), valueIndent, yLoc+4*vTab);
      }
    }
    
    // Weather statistics
    yLoc += 3*blockVT;
    g.setColor(Color.DARK_GRAY);
    g.drawString("Weather System:", margin, yLoc);
    yLoc += vTab;
    g.drawString(weatherTracker.isRunning() ? "Connected" : "Disconnected", margin + hTab, yLoc);
    yLoc += vTab;
    
    // Show move counter
    int movesUntilChange = 4 - weatherTracker.getMoveCount();
    g.setColor(movesUntilChange <= 1 ? Color.ORANGE : Color.DARK_GRAY);
    g.drawString(String.format("Weather changes in: %d moves", movesUntilChange), margin + hTab, yLoc);
    yLoc += vTab;
    g.setColor(Color.DARK_GRAY);
    
    g.drawString(String.format("Tracked: %d cells", weatherTracker.getTrackedLocationCount()), margin + hTab, yLoc);
    yLoc += vTab;
    g.drawString(String.format("Avg Harsh: %.2f", weatherTracker.getAverageHarshness()), margin + hTab, yLoc);
    yLoc += vTab;
    
    // Show both rest spots and harsh weather counts
    int restSpots = weatherTracker.findRestSpots().size();
    long harshSpotsLong = weatherTracker.getWeatherSeverityStats().getOrDefault("Harsh", 0L);
    int harshSpots = (int) harshSpotsLong;
    g.setColor(Color.GREEN);
    g.drawString(String.format("Rest spots: %d", restSpots), margin + hTab, yLoc);
    yLoc += vTab;
    g.setColor(Color.RED);
    g.drawString(String.format("Harsh zones: %d", harshSpots), margin + hTab, yLoc);
  }

  public List<Cell> getClearRadius(Cell from, int size) {
    List<Cell> init = grid.getRadius(from, size);
    for(Actor player: listOfPlayers) {
      init.remove(player.loc);
    }
    return init;
  }

  public void mouseClicked(int x, int y) {
    currentState.mouseClick(x, y, this);
  }

  public WeatherTracker getWeatherTracker() {
    return weatherTracker;
  }
}