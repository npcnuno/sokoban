package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Caixote extends GameElement {

	private Point2D position;
	private String imageName;
	
	public Caixote(Point2D initialposition) {
		super(initialposition);
		position = initialposition;
		imageName = "Caixote";

	}

	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public Point2D getPosition(){
		return position;
	}
}
