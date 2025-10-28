public class WeatherData {
    private final int gridX;
    private final int gridY;
    private float rainfall;
    private float temperature;
    public WeatherData(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.rainfall = 0.5f;
        this.temperature = 0.5f;
    }

    public int getGridX() { return gridX; }
    public int getGridY() { return gridY; }
    public float getRainfall() { return rainfall; }
    public float getTemperature() { return temperature; }

    public void setRainfall(float rainfall) {
        this.rainfall = rainfall;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    // Parse from server format: "timestamp attribute x y value"
    public static String parseAttribute(String line) {
        return line.split(" ")[1];
    }

    public static int parseX(String line) {
        return Integer.parseInt(line.split(" ")[2]);
    }

    public static int parseY(String line) {
        return Integer.parseInt(line.split(" ")[3]);
    }

    public static float parseValue(String line) {
        return Float.parseFloat(line.split(" ")[4]);
    }
}