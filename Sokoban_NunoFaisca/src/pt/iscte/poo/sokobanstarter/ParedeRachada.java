package pt.iscte.poo.sokobanstarter;

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
		return 1;
	}

	
}