import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;
import java.util.Optional;

public abstract class Actor implements Pulse {
  Color baseColor, color;
  Cell loc;
  List<Polygon> display;
  boolean bot;
  int moves;
  int turns;
  MoveStrategy mover;
  
  // Energy system
  float energy;  // 0 to 100
  EnergyStrategy energyStrategy;

  protected Actor(Cell inLoc, Color inColor, boolean isBot, int inMoves, EnergyStrategy strategy) {
    loc = inLoc;
    baseColor = inColor;
    color = inColor;
    bot = isBot;
    moves = inMoves;
    turns = 1;
    energy = 100f;  // Start with full energy
    energyStrategy = strategy;
    
    // Initialize mover based on row
    if(loc.row % 2 == 0) {
      mover = new MoveRandomly();
    } 
    else {
      mover = new MoveLeft();
    }
    
    setPoly();
  }

  public void paint(Graphics g) {
    // Draw actor
    for(Polygon p: display) {
      g.setColor(color);
      g.fillPolygon(p);
      g.setColor(Color.GRAY);
      g.drawPolygon(p);
    }
    
    // Draw energy bar above actor
    drawEnergyBar(g);
  }

  private void drawEnergyBar(Graphics g) {
    int barWidth = 30;
    int barHeight = 4;
    int barX = loc.x + 2;
    int barY = loc.y - 8;
    
    // Background
    g.setColor(Color.DARK_GRAY);
    g.fillRect(barX, barY, barWidth, barHeight);
    
    // Energy level
    int energyWidth = (int)((energy / 100f) * barWidth);
    if (energy > 60) {
      g.setColor(Color.GREEN);
    } 
    else if (energy > 30) {
      g.setColor(Color.YELLOW);
    } 
    else {
      g.setColor(Color.RED);
    }
    g.fillRect(barX, barY, energyWidth, barHeight);
    
    // Border
    g.setColor(Color.BLACK);
    g.drawRect(barX, barY, barWidth, barHeight);
  }

  protected abstract void setPoly();

  public boolean isBot() {
    return bot;
  }

  public void setLocation(Cell inLoc) {
    loc = inLoc;
    if(loc.row % 2 == 0) {
      mover = new MoveRandomly();
    } 
    else {
      mover = new MoveLeft();
    }
    setPoly();
  }


   // Update energy based on current weather
  public void updateEnergy(WeatherTracker tracker) {
    int gridX = (int)(loc.col - 'A');
    int gridY = loc.row;
    
    Optional<WeatherCondition> weather = tracker.getWeatherAt(gridX, gridY);
    
    float change;
    if (weather.isPresent()) {
      // Weather data available, use energy strategy to calculate percentage change
      change = energyStrategy.calculateEnergyChange(weather.get(), energy);
      float harshness = weather.get().getHarshness();
      String weatherType = harshness < 0.4f ? "MILD" : harshness < 0.7f ? "MODERATE" : "HARSH";
      System.out.println(getClass().getSimpleName() + " at (" + loc.col + loc.row + ") in " + weatherType + " weather (harsh=" + String.format("%.2f", harshness) + "): energy change = " + String.format("%.1f", change));
    } 
    else {
      // No weather data, use moderate weather as default
      WeatherCondition defaultWeather = new WeatherCondition(gridX, gridY);
      change = energyStrategy.calculateEnergyChange(defaultWeather, energy);
      System.out.println(getClass().getSimpleName() + " at (" + loc.col + loc.row + ") NO WEATHER DATA: energy change = " + String.format("%.1f", change));
    }
    
    float oldEnergy = energy;
    energy += change;
    energy = Math.max(0f, Math.min(100f, energy)); // Clamp to 0-100
    
    System.out.println("  Energy: " + String.format("%.1f", oldEnergy) + " -> " + String.format("%.1f", energy));
  }

  //Checks if actor can move (needs energy > 0)
  public boolean canMove() {
    return energy > 10f;
  }

  //Checks if need to skip turn for rest
  public boolean needsRest() {
    return energy < 30f;
  }

  public float getEnergy() {
    return energy;
  }

  public EnergyStrategy getEnergyStrategy() {
    return energyStrategy;
  }

  public void pulsate(char phase, int percentage) {
    // Adjust color saturation according to the beat
    float[] hsbValues = new float[3];
    Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), hsbValues);
    hsbValues[1] = ((float) percentage) / 100.0f;
    color = Color.getHSBColor(hsbValues[0], hsbValues[1], hsbValues[2]);
  }
}