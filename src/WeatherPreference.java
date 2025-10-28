import java.util.List;
import java.util.Comparator;
//import java.util.stream.Collectors;

public interface WeatherPreference {
    //Score a cell based on weather (higher = better)
    double scoreCell(WeatherData weather);

    //Choose best cell from options using STREAMS & LAMBDAS
    default Cell chooseBestCell(List<Cell> options, WeatherSystem weatherSystem) {
        if (options.isEmpty()) return null;
        // LAMBDA & STREAM USAGE: Find cell with best weather score
        return options.stream().max(Comparator.comparingDouble(cell -> weatherSystem.getWeatherAt((int)(cell.col - 'A'), cell.row).map(this::scoreCell).orElse(0.0))).orElse(options.get(0));
    }
}

//Birds like dry weather (low rain)
class LikesDryWeather implements WeatherPreference {
    @Override
    public double scoreCell(WeatherData weather) {
        return 1.0 - weather.getRainfall(); // Low rain = high score
    }
}

//Cats like warm weather (high temp)
class LikesWarmWeather implements WeatherPreference {
    @Override
    public double scoreCell(WeatherData weather) {
        return weather.getTemperature(); // High temp = high score
    }
}

//Dogs like rainy weather (high rain, moderate temp)
class LikesRainyWeather implements WeatherPreference {
    @Override
    public double scoreCell(WeatherData weather) {
        // Prefer rain but not too hot or cold
        double rainScore = weather.getRainfall();
        double tempScore = 1.0 - Math.abs(weather.getTemperature() - 0.5) * 2;
        return (rainScore * 0.7 + tempScore * 0.3);
    }
}