package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Martelo extends GameElement {

	private Point2D position;
	int layer;
	public Martelo(Point2D position) {
		super(position);
		this.position = position;
		layer = 0;
	}

    @Override
	public String getName() {
		return "Martelo";
	}

	@Override
	public int getLayer() {
		return layer;
	}
	public void setLayer(int value) {
		this.layer = value;
	}
	
	@Override
	public Point2D getPosition() {
		return position;
	}
	@Override
	public boolean remove() {
		GameEngine instance = GameEngine.getInstance();
		instance.setMartelo(true);
		return true;
	}

}
