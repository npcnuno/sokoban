package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Palete extends GameElement {

	
	public Palete(Point2D position) {
		super(position);

	}

    @Override
	public String getName() {
		return "Palete";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public void move(Direction direction) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'move'");
	}


}
