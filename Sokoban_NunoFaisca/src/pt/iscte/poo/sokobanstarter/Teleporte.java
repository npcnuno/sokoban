package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Teleporte extends GameElement {

	//DATA FIELDS
	
	Point2D position;

	//CONSTRUCTORS
	
	public Teleporte(Point2D position) {
		super(position);
		this.position = position;
		setObjectMobilityStatus(FLOOR_LEVEL);
	}
	
	//METHODS
	
    @Override
	public String getName() {
		return TELEPORT;
	}

	@Override
	public int getLayer() {
		return 0;
	}
	public Point2D getPosition() {
		return position;
	}

	public Point2D isHole(Point2D position, Direction dir) {
		GameEngine instance = GameEngine.getInstance();
		position = instance.searchTypeOfGameElement(position, TELEPORT);
		
		if(instance.getGameElement(position).getName().equals(TELEPORT)){
			return position;
		}
		
		return null;
	}
}
