package pt.iscte.poo.sokobanstarter;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;

public class Empilhadora extends GameElement {
	
	//ENUMS
	private static String FORKLIFT_DOWN = "Empilhadora_D";
	private static String FORKLIFT_UP = "Empilhadora_U";
	private static String FORKLIFT_LEFT = "Empilhadora_L";
	private static String FORKLIFT_RIGHT = "Empilhadora_R";

	
	//DATA FIELDS
	
	private Point2D position;
	private String imageName;
	
	
	//CONSTRUCTOR
	
	public Empilhadora(Point2D initialPosition){
		super(initialPosition);
		position = initialPosition;
		imageName = FORKLIFT_DOWN;
	}
	
	//METHODS
	
	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	@Override
	public Point2D getPosition(){
		return position;
	}

	@Override
	public void move(Direction dir) {
		// Update the image based on the direction of movement
		switch (dir) {
			case DOWN:
				imageName = FORKLIFT_DOWN;
				break;
			case UP:
				imageName = FORKLIFT_UP;
				break;
			case LEFT:
				imageName = FORKLIFT_LEFT;
				break;
			case RIGHT:
				imageName = FORKLIFT_RIGHT;
				break;
			default:
				break;
		}

		Point2D newPosition = position.plus(dir.asVector());
		GameEngine instance = GameEngine.getInstance();

		// Decreasing the battery level with each move
		instance.setBattery(-1);

		// Getting the game element in the new position
		GameElement forwardElement = instance.getGameElement(newPosition);

		// Check if the move is valid
		if (isValid(dir)) {

			// Handle the case where the forward element is movable
			if (forwardElement.MobilityStatus().equals(MOVABLE)) {
				if (forwardElement.isValid(dir)) {
					forwardElement.move(dir);

					// Check if the object moved, if not return
					forwardElement = instance.getGameElement(newPosition);
				}

				if (forwardElement.MobilityStatus().equals(MOVABLE))
					return;
			}

			// Handling interactions with removable objects
			if (forwardElement.remove()) {
				instance.removeGameElement(newPosition);
				position = newPosition;
				instance.addGameElement(newPosition, new Chao(newPosition));
				return;
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
		Point2D newPosition = position.plus(dir.asVector());
		GameEngine instance = GameEngine.getInstance();
		GameElement forwardElement = instance.getGameElement(newPosition);

		// Check the boundaries and the type of the forward element
		if (newPosition.getX() >= 0 && newPosition.getX() < 10 &&
				newPosition.getY() >= 0 && newPosition.getY() < 10) {

			// Check if the forward element is movable or floor, allowing movement
			if (forwardElement.MobilityStatus().equals(MOVABLE)
					|| forwardElement.MobilityStatus().equals(FLOOR_LEVEL)) {
				return true;
			}
			// Special case for a breakable wall when the player has a hammer
			else if (forwardElement.MobilityStatus().equals(STATIC) && forwardElement.getName().equals(BROKEN_WALL)
					&& instance.hasHammer) {
				return true;
			}
		}
		return false;
	}

}