// Simple weather condition at a specific grid location
public class WeatherCondition {
    private final int gridX;
    private final int gridY;
    private float windStrength;  // 0.0 to 1.0
    private float rainfall;      // 0.0 to 1.0
    public WeatherCondition(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.windStrength = 0.5f;
        this.rainfall = 0.5f;
    }
    public int getGridX() { return gridX; }
    public int getGridY() { return gridY; }
    public float getWindStrength() { return windStrength; }
    public float getRainfall() { return rainfall; }
    public void setWindStrength(float windStrength) {
        this.windStrength = Math.max(0f, Math.min(1f, windStrength));
    }
    public void setRainfall(float rainfall) {
        this.rainfall = Math.max(0f, Math.min(1f, rainfall));
    }
    
    // Calculate "harshness" of weather (0 = mild, 1 = extreme)
    public float getHarshness() {
        return (windStrength + rainfall) / 2f;
    }

    // Checks if "rest spot" (mild weather)
    public boolean isRestSpot() {
        return getHarshness() < 0.4f;
    }

    @Override
    public String toString() {
        return String.format("Weather[(%d,%d) wind=%.2f rain=%.2f harsh=%.2f]", gridX, gridY, windStrength, rainfall, getHarshness());
    }
}