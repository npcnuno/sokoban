package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

/**
 * Abstract class representing a game element in the Sokoban game.
 * This class provides common functionalities for all game elements,
 * such as their position, mobility status, and interaction capabilities.
 */
public abstract class GameElement implements ImageTile, isMovable {
    
    // Constants representing different types of objects mobility status.
    protected static String STATIC = "StaticObject";
    protected static String FLOOR_LEVEL = "FloorObject";
    protected static String MOVABLE = "MovableObject";
    
    //Constants representing different types of objects
    protected static String TARGET = "Alvo";
    protected static String BATTERY = "Bateria";
    protected static String HOLE = "Buraco";
    protected static String CRATE = "Caixote";
    protected static String FLOOR = "Chao";
    protected static String BOBCAT = "Empilhadora";
    protected static String HAMMER = "Martelo";
    protected static String PALLET = "Palete";
    protected static String WALL = "Parede";
    protected static String BROKEN_WALL = "ParedeRachada";
    protected static String TELEPORT = "Teleporte";



    // Position of the game element on the grid.
    private Point2D Point2D;

    // Mobility status of the game element, indicating whether it's static, movable, or part of the floor.
    protected String MobilityStatus = STATIC;
    
    /**
     * Constructor to initialize the game element with a position.
     * @param Point2D The position of the game element.
     */
    public GameElement(Point2D Point2D){
        this.Point2D = Point2D;
    }
    
    /**
     * Abstract method to get the name of the game element.
     * This must be implemented by subclasses.
     * @return The name of the game element.
     */
    @Override
    public abstract String getName();

    /**
     * Gets the current position of the game element.
     * @return The current position as a Point2D object.
     */
    @Override
    public Point2D getPosition() {
        return Point2D;
    }
    
    /**
     * Gets the mobility status of the game element.
     * @return The mobility status as a String.
     */
    public String MobilityStatus() {
        return MobilityStatus;
    }

    /**
     * Sets the mobility status of the game element.
     * @param value The new mobility status to set.
     */
    public void setObjectMobilityStatus(String value) {
        MobilityStatus = value;
    }
    
    /**
     * Abstract method to get the layer of the game element.
     * This must be implemented by subclasses.
     * @return The layer of the game element.
     */
    @Override
    public abstract int getLayer();
    
    /**
     * Moves the game element in a specified direction.
     * This method is meant to be overridden by subclasses.
     * @param direction The direction to move the game element.
     */
    public void move(Direction direction) {
        // Implementation should be provided in subclasses
    }

    /**
     * Removes the game element from the game.
     * This method is meant to be overridden by subclasses.
     * @return A boolean indicating if the removal was successful.
     */
    public boolean remove() {
        return false;
    }

    /**
     * Checks if the next position in the specified direction is a hole.
     * This method is meant to be overridden by subclasses.
     * @param position The current position of the game element.
     * @param dir The direction to check.
     * @return The new position if it's a hole, otherwise null.
     */
    public Point2D isHole(Point2D position, Direction dir) {
        return null;
    }
    
    /**
     * Checks if moving in a given direction is valid.
     * @param dir The direction to check.
     * @return True if the move is valid, false otherwise.
     */
    @Override
    public boolean isValid(Direction dir) {
        // Calculate the new position based on the current direction
        Point2D newPosition = Point2D.plus(dir.asVector());
        GameEngine instance = GameEngine.getInstance();
        // Retrieve the game element at the new position
        GameElement forwardElement = instance.getGameElement(newPosition);

        // Check if the new position is within the game boundaries
        if (newPosition.getX() >= 0 && newPosition.getX() < 10 &&
            newPosition.getY() >= 0 && newPosition.getY() < 10) {
            
            // Check if the forward element's mobility status allows for valid movement
            if (forwardElement.MobilityStatus().equals(FLOOR_LEVEL)) {
                return true;
            }
        }
        // Return false if the move is not valid
        return false;
    }

}
