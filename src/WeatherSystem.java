import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WeatherSystem {
    private static final String WEATHER_URL = "http://13.238.167.130/weather";
    private Map<String, WeatherData> weatherMap; // "x,y" -> WeatherData
    private Thread streamThread;
    private volatile boolean running;
    
    public WeatherSystem() {
        this.weatherMap = new ConcurrentHashMap<>();
        this.running = false;
    }

    public void start() {
        if (running) return;
        running = true;
        streamThread = new Thread(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(WEATHER_URL)).header("Accept", "text/event-stream").build();
                client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()).thenApply(HttpResponse::body).thenAccept(inputStream -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        // LAMBDA & STREAM USAGE: Process weather stream
                        reader.lines().filter(line -> !line.trim().isEmpty()).forEach(this::processLine);
                    } catch (Exception e) {
                        if (running) System.err.println("Stream error: " + e.getMessage());
                    }
                }).join();
            }
            catch (Exception e) {
                System.err.println("Connection error: " + e.getMessage());
            }
        });
        streamThread.setDaemon(true);
        streamThread.start();
    }

    private void processLine(String line) {
        try {
        // Convert server coords (center at 0,0) to grid coords (0-19)
        int serverX = WeatherData.parseX(line);
        int serverY = WeatherData.parseY(line);
        int gridX = serverX + 10;
        int gridY = serverY + 10;
        // Ignore out of bounds
            if (gridX < 0 || gridX >= 20 || gridY < 0 || gridY >= 20) return;
            
            String key = gridX + "," + gridY;
            String attribute = WeatherData.parseAttribute(line);
            float value = WeatherData.parseValue(line);
            
            weatherMap.putIfAbsent(key, new WeatherData(gridX, gridY));
            WeatherData data = weatherMap.get(key);
            
            if (attribute.equals("rain")) {
                data.setRainfall(value);
            } 
            else if (attribute.equals("temp")) {
                data.setTemperature(value);
            }
        } catch (Exception e) {
            // Ignore parse errors
        }
    }

    public Optional<WeatherData> getWeatherAt(int x, int y) {
        return Optional.ofNullable(weatherMap.get(x + "," + y));
    }

    //Find cells matching weather condition
    public List<WeatherData> findCellsWhere(java.util.function.Predicate<WeatherData> condition) {
        return weatherMap.values().stream().filter(condition).collect(Collectors.toList());
    }
    //Get average temperature
    public double getAverageTemperature() {
        return weatherMap.values().stream().mapToDouble(WeatherData::getTemperature).average().orElse(0.5);
    }
    //Get average rainfall
    public double getAverageRainfall() {
        return weatherMap.values().stream().mapToDouble(WeatherData::getRainfall).average().orElse(0.5);
    }
    public void stop() {
        running = false;
        if (streamThread != null) streamThread.interrupt();
    }
    public boolean isRunning() {
        return running;
    }
}