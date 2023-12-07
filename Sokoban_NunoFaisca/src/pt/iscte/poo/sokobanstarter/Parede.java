package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Parede extends GameElement {
	
	//CONSTRUCTOR
	
	public Parede(Point2D position) {
		super(position);
	}

	//METHODS
	
	@Override
	public String getName() {
		return WALL;
	}

	@Override
	public int getLayer() {
		return 2;
	}

}
