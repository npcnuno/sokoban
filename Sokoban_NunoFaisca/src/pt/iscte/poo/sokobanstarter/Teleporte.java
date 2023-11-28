package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Teleporte extends GameElement {

	
	public Teleporte(Point2D position) {
		super(position);

	}

    @Override
	public String getName() {
		return "Teleporte";
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public void move(Direction direction) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'move'");
	}


}
