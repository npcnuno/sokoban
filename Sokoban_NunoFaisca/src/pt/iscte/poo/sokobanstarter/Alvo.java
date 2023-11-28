package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Alvo extends GameElement {
	


	public Alvo(Point2D position) {
		super(position);

	}

	@Override
	public String getName() {
		return "Alvo";
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
