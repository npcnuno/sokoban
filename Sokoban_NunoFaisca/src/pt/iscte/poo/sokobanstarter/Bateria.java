package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Bateria extends GameElement {
	
	private int Nivelbateria;
	
	public Bateria(Point2D position) {
		super(position);
		Nivelbateria = 100;
	}

	@Override
	public String getName() {
		return "Bateria";
	}

	@Override
	public int getLayer() {
		return 0;
	}
	
	public int addBatteryToBobcat() {
		return Nivelbateria;
	}


}
