package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class GameElement implements ImageTile, isMovable {
	
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
	
	public void setLayer(int layer) {
	}
	
	
	public void move(Direction direction) {
		return;
	}
	public boolean remove() {
		return false;
	}
	@Override
	public boolean isValid(Direction dir) {
			Point2D newPosition = Point2D.plus(dir.asVector());
			GameEngine instance = GameEngine.getInstance();
			if (newPosition.getX()>=0 && newPosition.getX()<10 && 
				newPosition.getY()>=0 && newPosition.getY()<10){
						if(instance.getLayer(newPosition) == 0){
							return true;
							}
						}
				return false;
	}
}