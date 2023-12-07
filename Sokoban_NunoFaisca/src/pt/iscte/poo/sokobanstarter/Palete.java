package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
public class Palete extends GameElement {

	//DATA FIELDS
	
	private Point2D position;

	
	//CONSTRUCTOR
	
	public Palete(Point2D initialposition) {
		super(initialposition);
		position = initialposition;
		setObjectMobilityStatus(MOVABLE);


	}

	//METHODS
	
	@Override
	public String getName() {
		return PALLET;
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public Point2D getPosition(){
		return position;
	}
	
	@Override
	public void move(Direction dir) {
		// Calculate the new position based on the current direction
		Point2D newPosition = position.plus(dir.asVector());
		GameEngine instance = GameEngine.getInstance();
		// Retrieve the game element at the new position
		GameElement forwardElement = instance.getGameElement(newPosition);

		// Decrease the battery level by 1 on every move
		instance.setBattery(-1);

		// Check if the move is valid based on the game rules
		if (isValid(dir)) {
			// Update the position to the new position
			position = newPosition;

			// Check if the new position is a hole and handle accordingly
			if (forwardElement.isHole(newPosition, dir) != null) {
				position = forwardElement.isHole(newPosition, dir);
				// Early return if the new position is a hole
				return;
			}
		}
	}

	@Override
	public boolean isValid(Direction dir) {
		// Calculate the new position based on the current direction
		Point2D newPosition = position.plus(dir.asVector());
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
