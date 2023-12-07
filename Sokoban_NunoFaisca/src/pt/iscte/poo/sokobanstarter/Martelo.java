package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Martelo extends GameElement {

	//CONSTRUCTOR
	public Martelo(Point2D position) {
		super(position);
		setObjectMobilityStatus(FLOOR_LEVEL);

	}

	//METHODS
	
    @Override
	public String getName() {
		return HAMMER;
	}

	@Override
	public int getLayer() {
		return 0;
	}
	
	@Override
	public boolean remove() {
		GameEngine instance = GameEngine.getInstance();
		instance.setHammer(true);
		return true;
	}

}
