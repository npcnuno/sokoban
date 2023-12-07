package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class ParedeRachada extends GameElement {


	//CONSTRUCTORS
	
	public ParedeRachada(Point2D position) {
		super(position);
	}
	
	//METHODS

    @Override
	public String getName() {
		return BROKEN_WALL;
	}

	@Override
	public int getLayer() {
		return 2;
	}

	public boolean remove() {
		GameEngine instance = GameEngine.getInstance();
		instance.setBattery(-1);
		instance.setHammer(false);
		return true;
	}

	
}
