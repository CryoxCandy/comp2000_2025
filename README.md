# Assignment 2 - README
46469087
Sean H

## Game Mechanics
The implementation features an energy management system where actors' energy levels decrease proportionally to the severity of weather conditions at their current location. Harsh weather accelerates energy depletion, while mild weather facilitates energy recovery. Bot actors utilize a threshold-based strategy, performing random movements until energy falls below a specified threshold, where they will then actively seek mild weather tiles for regeneration. Actors with depleted energy are rendered immobile until sufficient energy is restored.

## Design Patterns

### 1. Strategy Pattern
**Location**: `EnergyStrategy.java` with implementations `BirdEnergyStrategy`, `CatEnergyStrategy`, `DogEnergyStrategy`

**Purpose**: Encapsulates different energy calculation algorithms for each actor type.

**Implementation**:
```java
public interface EnergyStrategy {
    float calculateEnergyChange(WeatherCondition weather, float currentEnergy);
    Cell chooseBestCell(List<Cell> options, WeatherTracker tracker, float currentEnergy);
}
```

Each actor is constructed with a strategy:
```java
public Bird(Cell inLoc, boolean isBot) {
    super(inLoc, Color.GREEN, isBot, birbMoves, new BirdEnergyStrategy());
}
```

**Benefits**:
- **Flexible**: Easy to add new actor types with different energy behaviors
- **Maintainable**: Energy logic is separated from actor rendering/movement
- **Extensible**: Can swap strategies at runtime if needed
- **Clean**: Avoids large if-else chains for different actor types

### 2. State Pattern
**Location**: `GameState.java` with implementations `ChoosingActor`, `SelectingNewLocation`, `BotMoving`

**Purpose**: Manages different game phases (selecting actors, choosing destinations, bot turns).

**Implementation**:
```java
public interface GameState {
    void mouseClick(int x, int y, Stage s);
    void paint(Graphics g, Stage s);
}
```

The stage maintains current state:
```java
GameState currentState;
currentState.mouseClick(x, y, this);
```

**Benefits**:
- **Clear phases**: Each game state has distinct behavior
- **No conditionals**: Avoids checking "what phase are we in?" everywhere
- **Easy transitions**: States change themselves (e.g., `s.currentState = new BotMoving()`)
- **Testable**: Each state can be tested independently

### 3. Template Method Pattern
**Location**: `Actor.java` (abstract class)

**Purpose**: Defines the overall actor structure while letting subclasses customize specific aspects.

**Implementation**:
```java
public abstract class Actor {
    protected Actor(...) {
        // Common initialization
        setPoly(); // Calls subclass implementation
    }
    
    protected abstract void setPoly(); // Implemented by Bird, Cat, Dog
}
```

**Benefits**:
- **Code reuse**: Common actor behavior (energy, painting) in one place
- **Customization**: Subclasses only implement what's different (shape)
- **Consistency**: All actors follow same initialization pattern

## Stream Operations and Lambdas

### 1. Finding Rest Spots
**Location**: `WeatherTracker.java`

```java
public List<WeatherCondition> findRestSpots() {
    return conditions.values().stream()
            .filter(WeatherCondition::isRestSpot)
            .collect(Collectors.toList());
}
```

**Lambda/Stream Features**:
- **Method reference**: `WeatherCondition::isRestSpot` instead of `w -> w.isRestSpot()`
- **Filter operation**: Selects only cells meeting predicate
- **Collector**: Gathers results into List

**Purpose**: Finds all mild weather locations (green tiles) for display and bot decision-making.

### 2. Finding Harshest Weather
**Location**: `WeatherTracker.java`

```java
public List<WeatherCondition> findHarshestWeather(int limit) {
    return conditions.values().stream()
            .sorted(Comparator.comparingDouble(WeatherCondition::getHarshness).reversed())
            .limit(limit)
            .collect(Collectors.toList());
}
```

**Lambda/Stream Features**:
- **Comparator with method reference**: `Comparator.comparingDouble(WeatherCondition::getHarshness)`
- **Reversed sorting**: `.reversed()` for descending order
- **Limit operation**: Takes only top N results

**Purpose**: Identifies most dangerous locations for analysis.

### 3. Calculating Average Harshness
**Location**: `WeatherTracker.java`

```java
public double getAverageHarshness() {
    return conditions.values().stream()
            .mapToDouble(WeatherCondition::getHarshness)
            .average()
            .orElse(0.5);
}
```

**Lambda/Stream Features**:
- **mapToDouble**: Converts objects to primitive doubles for efficiency
- **Method reference**: Extracts harshness value from each condition
- **Average aggregation**: Built-in statistical operation
- **Optional handling**: `.orElse()` provides default if no data

**Purpose**: Shows overall weather severity in sidebar.

### 4. Weather Severity Grouping
**Location**: `WeatherTracker.java`

```java
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
```

**Lambda/Stream Features**:
- **Complex lambda**: Multi-line classifier function
- **groupingBy collector**: Groups elements by classifier result
- **Nested collector**: `Collectors.counting()` counts each group
- **Map result**: Returns counts by category

**Purpose**: Displays how many cells are mild/moderate/harsh in sidebar.

### 5. Choosing Best Movement Cell
**Location**: `EnergyStrategy.java`

```java
default Cell chooseBestCell(List<Cell> options, WeatherTracker tracker, float currentEnergy) {
    return options.stream().max(Comparator.comparingDouble(cell -> tracker.getWeatherAt((int)(cell.col - 'A'), cell.row).map(w -> (double)calculateEnergyChange(w, currentEnergy)).orElse(0.0))).orElse(options.get(0));
}
```

**Lambda/Stream Features**:
- **max operation**: Finds cell with highest energy benefit
- **Complex lambda**: Multi-step calculation inside comparator
- **Chained Optional**: `.map()` on Optional to transform value
- **Nested lambda**: Lambda calls method reference `calculateEnergyChange`
- **Fallback handling**: `.orElse()` provides default if no max found

**Purpose**: Bots use this to intelligently choose where to move based on weather.

### 6. Processing Weather Stream (if using HTTP)
**Location**: `WeatherTracker.java` (in HTTP version)

```java
reader.lines()
    .filter(line -> !line.trim().isEmpty())
    .map(this::parseLine)
    .filter(Optional::isPresent)
    .map(Optional::get)
    .forEach(this::updateWeather);
```

**Lambda/Stream Features**:
- **Stream pipeline**: Chained operations process data flow
- **Lambda predicate**: `line -> !line.trim().isEmpty()`
- **Method references**: `this::parseLine`, `Optional::isPresent`, `Optional::get`, `this::updateWeather`
- **Filter + map pattern**: Common idiom for processing and transforming
- **forEach terminal**: Applies side effect (updating weather data)

**Purpose**: Processes incoming HTTP stream data line by line.

## Weather Data Interpretation

### Weather Attributes from Server

The server provides four attributes per location:
- **`rain`**: Rainfall amount (0.0 to 1.0)
- **`windx`**: Wind X-component (0.0 to 1.0)
- **`windy`**: Wind Y-component (0.0 to 1.0)
- **`temp`**: Temperature (0.0 to 1.0)

**Current Implementation**: We use **rain** and combine **windx/windy** into a single wind strength value. Temperature is available but not currently used.

### Coordinate System

**Server coordinates**: Origin (0, 0) at grid center, can be negative  
**Grid coordinates**: 20×20 grid with columns A-T (0-19) and rows 0-19

**Conversion**:
```java
int gridX = serverX + 10;  // Server -10 to +9 → Grid 0 to 19
int gridY = serverY + 10;
```

**Example**:
- Server: (-10, -10) → Grid: (0, 0) = Cell A0
- Server: (0, 0) → Grid: (10, 10) = Cell K10
- Server: (9, 9) → Grid: (19, 19) = Cell T19

### Weather Processing

**Step 1: Calculate Wind Strength**
```java
// Combine X and Y components into magnitude
float windMagnitude = Math.sqrt(windX² + windY²)

// But we simplify by averaging the components:
float windStrength = (Math.abs(windX - 0.5) * 2 + Math.abs(windY - 0.5) * 2) / 2
```

**Step 2: Calculate Harshness**
```java
harshness = (windStrength + rainfall) / 2.0
```

This gives us a single "harshness" value from 0.0 (calm/dry) to 1.0 (stormy/wet).

**Step 3: Categorize Weather**
```java
if (harshness < 0.4)  → MILD      (Green tiles, +10% energy)
if (harshness < 0.7)  → MODERATE  (White tiles, -10% energy)
if (harshness >= 0.7) → HARSH     (Red tiles, -20% energy)
```

### Energy Calculation

Actors gain or lose **percentage** of their current energy based on weather:

```java
// In mild weather (green tiles):
energyChange = currentEnergy * 0.10f   // Gain 10%

// In moderate weather (white tiles):
energyChange = currentEnergy * -0.10f  // Lose 10%

// In harsh weather (red tiles):
energyChange = currentEnergy * -0.20f  // Lose 20%
```

**Examples**:
- Actor with 100 energy in harsh weather: loses 20 → becomes 80
- Actor with 50 energy in moderate weather: loses 5 → becomes 45
- Actor with 30 energy in mild weather: gains 3 → becomes 33

### Weather Updates

**Initialization**: All 400 cells get random weather values (0.0 to 1.0) on startup.

**Updates Every 4 Moves**:
```java
// Smooth transition (not completely random)
newValue = currentValue + random(-0.2, +0.2)
newValue = clamp(newValue, 0.0, 1.0)
```

This creates gradual weather changes rather than sudden jumps, making it feel more realistic.

### Visual Interpretation

**On Grid**:
- **Green tint** = harshness < 0.4 (mild, safe)
- **No tint** = harshness 0.4-0.7 (moderate, normal)
- **Red tint** = harshness > 0.7 (harsh, dangerous)

**In Sidebar** (when hovering over cells):
- Displays exact harshness value (e.g., "Harshness: 0.63")
- Shows category text: "Mild weather", "Moderate weather", or "Harsh weather"
- Color-coded text matches severity

**Statistics**:
- **Rest spots**: Count of mild weather cells
- **Harsh zones**: Count of harsh weather cells
- **Avg Harsh**: Average harshness across all 400 cells