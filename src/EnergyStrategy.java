import java.util.List;
import java.util.Comparator;

public interface EnergyStrategy {
    

    // Calculate energy change per turn based on weather. Positive = gain energy, Negative = lose energy
    float calculateEnergyChange(WeatherCondition weather, float currentEnergy);
    
    //Choose best cell for energy management using STREAMS & LAMBDAS
    default Cell chooseBestCell(List<Cell> options, WeatherTracker tracker, float currentEnergy) {
        if (options.isEmpty()) return null;
        
        // Find cell with best energy benefit
        return options.stream().max(Comparator.comparingDouble(cell -> tracker.getWeatherAt((int)(cell.col - 'A'), cell.row).map(w -> (double)calculateEnergyChange(w, currentEnergy)).orElse(0.0))).orElse(options.get(0));
    }
}

// Energy strategies for specific actors (Birds, Cats, Dogs), can be tweaked individually if needed
// Birds - standard percentage-based energy changes
class BirdEnergyStrategy implements EnergyStrategy {
    @Override
    public float calculateEnergyChange(WeatherCondition weather, float currentEnergy) {
        float harshness = weather.getHarshness();
        
        if (harshness < 0.4f) {
            // Mild weather: recover 10%
            return currentEnergy * 0.10f;
        } else if (harshness < 0.7f) {
            // Moderate weather: lose 10%
            return currentEnergy * -0.10f;
        } else {
            // Harsh weather: lose 20%
            return currentEnergy * -0.20f;
        }
    }
}

// Cats - standard percentage-based energy changes
class CatEnergyStrategy implements EnergyStrategy {
    @Override
    public float calculateEnergyChange(WeatherCondition weather, float currentEnergy) {
        float harshness = weather.getHarshness();
        
        if (harshness < 0.4f) {
            // Mild weather: recover 10%
            return currentEnergy * 0.10f;
        } else if (harshness < 0.7f) {
            // Moderate weather: lose 10%
            return currentEnergy * -0.10f;
        } else {
            // Harsh weather: lose 20%
            return currentEnergy * -0.20f;
        }
    }
}

// Dogs - standard percentage-based energy changes
class DogEnergyStrategy implements EnergyStrategy {
    @Override
    public float calculateEnergyChange(WeatherCondition weather, float currentEnergy) {
        float harshness = weather.getHarshness();
        
        if (harshness < 0.4f) {
            // Mild weather: recover 10%
            return currentEnergy * 0.10f;
        } else if (harshness < 0.7f) {
            // Moderate weather: lose 10%
            return currentEnergy * -0.10f;
        } else {
            // Harsh weather: lose 20%
            return currentEnergy * -0.20f;
        }
    }
}