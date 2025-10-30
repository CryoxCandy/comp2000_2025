import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// Tracks weather - updates every 4 moves (tweakable)
public class WeatherTracker {
    private Map<String, WeatherCondition> conditions;
    private Random random;
    private int moveCount; // Track number of moves
    private static final int MOVES_PER_WEATHER_UPDATE = 4; // Update every 4 moves (tweakable)

    public WeatherTracker() {
        this.conditions = new ConcurrentHashMap<>();
        this.random = new Random();
        this.moveCount = 0;
    }

    public void start() {
        // Initialize all grid cells with random weather
        initializeAllCells();
    }

    private void initializeAllCells() {
        // Initialize all 20x20 grid cells with random weather
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                String key = x + "," + y;
                WeatherCondition condition = new WeatherCondition(x, y);
                condition.setWindStrength(random.nextFloat());
                condition.setRainfall(random.nextFloat());
                conditions.put(key, condition);
            }
        }
        System.out.println("Initialized weather for all 400 grid cells");
        System.out.println("Weather will update every " + MOVES_PER_WEATHER_UPDATE + " moves");
    }

    // Calls after each move
    public void recordMove() {
        moveCount++;
        System.out.println("Move recorded: " + moveCount + " / " + MOVES_PER_WEATHER_UPDATE);
        
        if (moveCount >= MOVES_PER_WEATHER_UPDATE) {
            updateAllWeather();
            moveCount = 0;
        }
    }

    // Update all cells with new random weather
    private void updateAllWeather() {
        int updated = 0;
        for (WeatherCondition condition : conditions.values()) {
            // Smoothly transition weather (70% same, 30% change)
            float currentWind = condition.getWindStrength();
            float currentRain = condition.getRainfall();
            
            // Adds some randomness
            float newWind = currentWind + (random.nextFloat() - 0.5f) * 0.4f;
            float newRain = currentRain + (random.nextFloat() - 0.5f) * 0.4f;
            
            condition.setWindStrength(newWind);
            condition.setRainfall(newRain);
            updated++;
        }
    }

    public Optional<WeatherCondition> getWeatherAt(int x, int y) {
        return Optional.ofNullable(conditions.get(x + "," + y));
    }

    // Find all rest spots (mild weather)
    public List<WeatherCondition> findRestSpots() {
        return conditions.values().stream()
                .filter(WeatherCondition::isRestSpot)
                .collect(Collectors.toList());
    }

    // Find harshest weather locations
    public List<WeatherCondition> findHarshestWeather(int limit) {
        return conditions.values().stream()
                .sorted(Comparator.comparingDouble(WeatherCondition::getHarshness).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Get average harshness across grid
    public double getAverageHarshness() {
        return conditions.values().stream()
                .mapToDouble(WeatherCondition::getHarshness)
                .average()
                .orElse(0.5);
    }

    // Count cells with different severity levels
    public Map<String, Long> getWeatherSeverityStats() {
        return conditions.values().stream()
                .collect(Collectors.groupingBy(
                    w -> {
                        float h = w.getHarshness();
                        if (h < 0.3f) return "Mild";
                        if (h < 0.6f) return "Moderate";
                        return "Harsh";
                    },
                    Collectors.counting()
                ));
    }

    public void stop() {
        // Nothing to stop - no background threads
    }

    public boolean isRunning() {
        return true; // Always "running" since not connecting to external server
    }

    public int getTrackedLocationCount() {
        return conditions.size();
    }

    public int getMoveCount() {
        return moveCount;
    }
}