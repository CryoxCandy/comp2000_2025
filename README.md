# COMP2000 Assignment 1
## Session 2, 2025
Student ID: 46469087

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