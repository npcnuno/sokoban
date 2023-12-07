package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Bateria extends GameElement {
	
	
	//CONSTRUCTORS
	
	public Bateria(Point2D position) {
		super(position);		
		//sets the mobility Status
		setObjectMobilityStatus(FLOOR_LEVEL);
	}

	//METHODS
	
	@Override
	public String getName() {
		return BATTERY;
	}

	@Override
	public int getLayer() {
		return 0;
	}
		
	@Override
	public boolean remove() {
		GameEngine instance = GameEngine.getInstance();
		instance.setBattery(100);
		return true;
	}

}
