package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
public class Palete extends GameElement {

	private Point2D position;
	private String imageName;
	private int layer;
	protected String MobilityStatus = MOVABLE;

	
	public Palete(Point2D initialposition) {
		super(initialposition);
		position = initialposition;
		imageName = "Palete";
		layer = 1;
		setObjectMobilityStatus(MOVABLE);


	}

	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public int getLayer() {
		return layer;
	}
	public void setLayer(int value) {
		layer = value;
	}


	@Override
	public Point2D getPosition(){
		return position;
	}
	
	@Override
	public void move(Direction dir) {
		Point2D newPosition = position.plus(dir.asVector());
		GameEngine instance = GameEngine.getInstance();
		GameElement fowardElement = instance.getGameElement(newPosition);
		instance.setBattery(-1);
		System.out.print("\n Elemento: " + fowardElement.getName());
		
		if(isValid(dir)){
			 
			position = newPosition;
			
			if(fowardElement.isHole(newPosition, dir) != null) {
				position = fowardElement.isHole(newPosition, dir);
				return;
			}
		}
	}
	
	@Override
	public boolean isValid(Direction dir) {
			Point2D newPosition = position.plus(dir.asVector());
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
	