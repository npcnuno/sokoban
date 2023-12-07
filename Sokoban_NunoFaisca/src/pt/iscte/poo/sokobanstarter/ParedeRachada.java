package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class ParedeRachada extends GameElement {


	
	public ParedeRachada(Point2D position) {
		super(position);

	}

    @Override
	public String getName() {
		return "ParedeRachada";
	}

	@Override
	public int getLayer() {
		return 2;
	}

	@Override
	public void move(Direction dirtion) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'move'");
	}
	public boolean remove() {
		GameEngine instance = GameEngine.getInstance();
		instance.setBattery(-1);
		instance.setMartelo(false);
		return true;
	}

	
}
