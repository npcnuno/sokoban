package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Buraco extends GameElement {

	
	public Buraco(Point2D position) {
		super(position);

	}

    @Override
	public String getName() {
		return "Buraco";
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public void move(Direction direction) {
		throw new UnsupportedOperationException("Unimplemented method 'move'");
	}


}