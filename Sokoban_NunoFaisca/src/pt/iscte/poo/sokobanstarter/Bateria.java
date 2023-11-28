package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
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

	@Override
	public void move(Direction direction) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'move'");
	}


}
