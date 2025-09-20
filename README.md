# COMP2000 Assignment 1
## Session 2, 2025
Student ID: 46469087

## Project Description

A grid exists that is navigatable by the Player (White Box with smaller box) to navigate with arrow keys. Player and other actors are restricted by the bounds of the grid.
The grid features cells of types grass, sand, and water which features different traits of wind strength, temperature, and depth respectively. The values of these traits
are randomly assigned on start-up of a specified range or strings. Animal actors move when a player actor moves, and the actors can only move across specific cell types. Dogs 
cannot move across grass cells, birds cannot move across sand cells, and cats cannot move across water cells. Items exist in the scene - bone, fish, and seed, which can be picked
up by the player actor when moving onto the same cell. In doing so, the player actor can now move into a nearby cell of an animal actor (1 surrounding cell) and feed the animal to
collect it and remove it from the grid. The dog actor requires a bone, the cat actor requires a fish, and the bird actor requires seeds. The game ends when all animal actors are
collected (removed). No code has been written to detect the removal of all animal actors and create a finish screen or quit application.

## Project File Explanation

### Main
Initialises the window, stage, and player object. Handles inputs from keyboard, renders canvas, updating the scene constantly, integrating all components.

### Stage
Initialises and manages the grid, ensuring the grid is populated with cells and cell types. Manages all actors and updating them. Manages all items and handles their interactions.

### Grid
Creates a 2D array of cell objects. Populates with different cell types. Manages cells, including access to cells and rendering. Neighbour detection method is part of grid.
Required for game logic involving spacial structure needed for actor movement, item placement, and interactions.

### Actor
Base class for actors, containing common properties shared among actors. Handles the random movement of actors. 

### Bird, Cat, Dog
Extensions of the actor class, with unique properties to handle their unique behaviour.

### Player
Overrides the base movement from actor with a user input system. Adds interaction between the player actor and items as well as other actors.

### Item
Base class for items containing common properties. Provides location management for items.

### Seed, Fish, Bone
Extension of the item class, with unique properties.

### Cell
Base class for cells, encapsulating shared properties and behaviours. Includes mouse interaction with hovering over cells and providing cell information.

### GrassCell, WaterCell, SandCell
Extension of the cell class, with unique properties.

### Direction
Simply there for standardised direction representation, aka making it easier to read and write.

## Project Development and Decision Making
### Cell Types

Created a variety of cell types - water, grass, and sand with unique properties from each other.

1. Inheritance

Main class contains all common behaviour and traits of cells. From this, created subclasses GrassCell, WaterCell, and SandCell.

Each subclass adds its own properties, like wind strength, depth, or temperature, and gives its own version of getInfo.

This avoids repeating code and makes it easy to add new types of cells later.

2. Interfaces

GetInfo method in the Cell class forces every subclass to explain its unique information.

This means other parts of the program, like the Grid or Stage, can work with any kind of Cell without needing to know the details.

4. Overall Design

Encapsulation: Each cell keeps track of its own data and behavior.

Polymorphism: Methods like paint and getInfo behave differently depending on the cell type.

Extensibility: It’s easy to add new cell types, like a MountainCell, without changing existing code. Which I just might do.

### Movement and Actors

Updated actors and actor class. Added movement.

1. Inheitance

Main actor class has shared properties which are inherited by Player, Dog, Cat, and Bird. Actor has moveRandomly method used by the actors, excluding Player who overrides with their own move method.

2. Overall Design

Encapsulation: Each actor class manages its own data and behavior internally, hiding details from other classes.

Polymorphism: Shared methods like move behave differently depending on whether the object is a Player, Dog, Cat, or Bird.

Extensibility: New actor types can be added easily by extending the Actor class without changing existing code.

### Items and Actor Removal

Added item class and item objects. Added conditions for "winning."
1. Inheritance

All items extend a common Item class. Core properties and methods such as loc (location), color, and paint are defined once. Subclasses have overrides for unique interactions.

2. Generics

Proximity checking method can work with both Actor and Item objects using generic types, allowing for reusable logic.