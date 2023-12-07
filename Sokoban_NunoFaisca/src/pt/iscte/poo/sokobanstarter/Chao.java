package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Chao extends GameElement {

	//CONSTRUCTORS
	public Chao(Point2D initialPosition) {
		super(initialPosition);
		setObjectMobilityStatus(FLOOR_LEVEL);
	}

	//METHODS
	
	@Override
	public String getName() {
		return FLOOR;
	}
	
	@Override
	public int getLayer() {
		return 0;
	}
}
