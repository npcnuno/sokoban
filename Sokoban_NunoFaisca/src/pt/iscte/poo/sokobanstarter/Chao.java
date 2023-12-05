package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Chao extends GameElement {

	private Point2D position;
	private String imageName;


		
	public Chao(Point2D initialPosition) {
		super(initialPosition);
		this.position = initialPosition;
		imageName = "Chao";
	}

	@Override
	public String getName() {
		return imageName;
	}
	
	@Override
	public int getLayer() {
		return 0;
	}
	@Override
	public Point2D getPosition(){
		return position;
	}
}
