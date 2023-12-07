package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class GameElement implements ImageTile, isMovable {
	
	protected static String STATIC = "StaticObject";
	protected static String FLOOR = "FloorObject";
	public static String MOVABLE = "MovableObject";



	
	private Point2D Point2D;
	protected String MobilityStatus = STATIC;
	
	public GameElement(Point2D Point2D){
		this.Point2D = Point2D;
	}
	
	@Override
	public abstract String getName();

	@Override
	public Point2D getPosition() {
		return Point2D;
	}
	
	public String MobilityStatus() {
		return MobilityStatus;
	}
	public void setObjectMobilityStatus(String value) {
		
		MobilityStatus = value;
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
	public Point2D isHole(Point2D position, Direction dir) {
		return null;
	}
	
	@Override
	public boolean isValid(Direction dir) {
			Point2D newPosition = Point2D.plus(dir.asVector());
			GameEngine instance = GameEngine.getInstance();
			GameElement fowardElement = instance.getGameElement(newPosition);
			if (newPosition.getX()>=0 && newPosition.getX()<10 && 
				newPosition.getY()>=0 && newPosition.getY()<10){
						if(fowardElement.MobilityStatus().equals(FLOOR)){
							return true;
							}
						}
				return false;
	}
}