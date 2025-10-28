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
  WeatherPreference weatherPreference;
  protected Actor(Cell inLoc, Color inColor, boolean isBot, int inMoves, WeatherPreference preference) {
    loc = inLoc;
    baseColor = inColor;
    color = inColor;
    bot = isBot;
    moves = inMoves;
    turns = 1;
    weatherPreference = preference;
    setPoly();
  }

  public void paint(Graphics g) {
    for(Polygon p: display) {
    g.setColor(color);
    g.fillPolygon(p);
    g.setColor(Color.GRAY);
    g.drawPolygon(p);
    }
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

  //Update color based on current weather (visual mood)
  public void updateMoodFromWeather(WeatherSystem weatherSystem) {
    Optional<WeatherData> weather = weatherSystem.getWeatherAt((int)(loc.col - 'A'), loc.row);
    if (weather.isPresent()) {
      double score = weatherPreference.scoreCell(weather.get());
      
      // Happy (high score) = bright color, Unhappy (low score) = dark color
      float[] hsb = Color.RGBtoHSB(
          baseColor.getRed(), 
          baseColor.getGreen(), 
          baseColor.getBlue(), 
          null
      );
      
      // Adjust brightness based on happiness
      hsb[2] = (float)(0.3 + score * 0.7); // Brightness: 0.3 to 1.0
      color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
  }
  public void pulsate(char phase, int percentage) {
    // Adjust color saturation according to the beat
    float[] hsbValues = new float[3];
    Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), hsbValues);
    hsbValues[1] = ((float) percentage) / 100.0f;
    color = Color.getHSBColor(hsbValues[0], hsbValues[1], hsbValues[2]);
  }
  public WeatherPreference getWeatherPreference() {
    return weatherPreference;
  }
}