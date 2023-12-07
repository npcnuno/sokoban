package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Caixote extends GameElement {

	//DATA FIELDS
	
	private Point2D position;
	private boolean onTarget = false;

	//CONSTRUCTORS
	
	public Caixote(Point2D initialposition) {
		super(initialposition);
		position = initialposition;
		setObjectMobilityStatus(MOVABLE);

		
	}
	
	//METHODS

	@Override
	public String getName() {
		return CRATE;
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public Point2D getPosition(){
		return position;
	}
	
	public boolean onTarget() {
		return onTarget;
	}

	public void setTargetBoolean(boolean alvo) {
		this.onTarget = alvo;
	}
	
	@Override
	public void move(Direction dir) {
		Point2D newPosition = position.plus(dir.asVector());
		GameEngine instance = GameEngine.getInstance();
		GameElement forwardElement = instance.getGameElement(newPosition);

		// Decrease battery with each move
		instance.setBattery(-1);

		// Check if the move is valid
		if (isValid(dir)) {

			// Handling interaction with the targe
			if (TARGET.equals(forwardElement.getName())) {
				instance.targetsHit++;
				setTargetBoolean(true);
			} else if (FLOOR.equals(forwardElement.getName()) && onTarget) {
				setTargetBoolean(false);
				instance.targetsHit--;
			}

			// Handling hole interaction
			if (forwardElement.isHole(newPosition, dir) != null) {
				position = forwardElement.isHole(newPosition, dir);
				return;
			}

			// Update the position
			position = newPosition;
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

