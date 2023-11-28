package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class GameElement implements ImageTile {
	
	private Point2D Point2D;
	
	public GameElement(Point2D Point2D){
		this.Point2D = Point2D;
	}
	
	public GameElement(String elementName, Point2D Point2D, int layer){
		this.Point2D = Point2D;
	}
	
	@Override
	public abstract String getName();

	@Override
	public Point2D getPosition() {
		return Point2D;
	}

	@Override
	public abstract int getLayer() ;

	public abstract void move(Direction direction);

}