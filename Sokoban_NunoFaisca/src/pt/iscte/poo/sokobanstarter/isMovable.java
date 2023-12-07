package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;

/**
 * The isMovable interface defines a contract for game elements in the Sokoban game
 * that are capable of moving or being moved. 
 * Implementing this interface implies that the game element has some mobility
 * and can be checked for valid movement in a specific direction.
 */
public interface isMovable {

    /**
     * Determines whether it's valid for the game element to move in a given direction.
     * This method typically checks if the movement is feasible within the game's rules
     * and the current state of the game board.
     * 
     * @param dir The direction in which the movement is intended.
     * @return True if the movement is valid, false otherwise.
     */
    boolean isValid(Direction dir);
}
