package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Alvo extends GameElement {
	

	//CONSTRUCTORS	
	
	public Alvo(Point2D position) {
		super(position);
		setObjectMobilityStatus(FLOOR_LEVEL);
	}

	//METHODS
	@Override
	public String getName() {
		return TARGET;
	}

	@Override
	public int getLayer() {
		return 0;
	}



}
