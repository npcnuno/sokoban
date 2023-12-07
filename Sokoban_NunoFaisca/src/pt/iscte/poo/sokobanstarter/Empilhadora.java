package pt.iscte.poo.sokobanstarter;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;

public class Empilhadora extends GameElement {
	private static String FORKLIFT_DOWN = "Empilhadora_D";
	private static String FORKLIFT_UP = "Empilhadora_U";
	private static String FORKLIFT_LEFT = "Empilhadora_L";
	private static String FORKLIFT_RIGHT = "Empilhadora_R";

	private Point2D position;
	private String imageName;
	
	
	public Empilhadora(Point2D initialPosition){
		super(initialPosition);
		position = initialPosition;
		imageName = FORKLIFT_DOWN;
	}
	
	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}
	@Override
	public int getLayer() {
		return 2;
	}
	
	public void move(Direction dir) {
		switch (dir) {
		case DOWN:
		imageName = FORKLIFT_DOWN;
		break;
		case UP:
		imageName = FORKLIFT_UP;
		break;
		case LEFT:
		imageName = FORKLIFT_LEFT;
		break;
		case RIGHT:
		imageName = FORKLIFT_RIGHT;
		break;
		default:
		break;
	}
		Point2D newPosition = position.plus(dir.asVector());
		GameEngine instance = GameEngine.getInstance();
		
		instance.setBattery(-1);
		GameElement fowardElement = instance.getGameElement(newPosition);
		
		if(isValid(dir)){

			
			
			if(fowardElement.MobilityStatus().equals(MOVABLE)) {
				if(fowardElement.isValid(dir)) {
					
					
					
					fowardElement.move(dir);

					
					//CHECK IF THE OBJECT MOVED IF NOT RETURN
					fowardElement = instance.getGameElement(newPosition);
					System.out.print(fowardElement.getName());

					
					
				}
				if(fowardElement.MobilityStatus().equals(MOVABLE))
					 return;
			}


			if(fowardElement.remove()) {
					instance.removeGameElement(newPosition);
					position = newPosition;
					instance.addGameElement(newPosition,new Chao(newPosition));
					return;
				}
			if(fowardElement.isHole(newPosition, dir) != null) {
					position = fowardElement.isHole(newPosition, dir);
					return;
				}
			position = newPosition;		

		}
	}
	
	@Override
	public boolean isValid(Direction dir) {
		Point2D newPosition = position.plus(dir.asVector());
		GameEngine instance = GameEngine.getInstance();
		GameElement fowardElement = instance.getGameElement(newPosition);

		if (newPosition.getX()>=0 && newPosition.getX()<10 && 
			newPosition.getY()>=0 && newPosition.getY()<10){
					if(fowardElement.MobilityStatus().equals(MOVABLE) || fowardElement.MobilityStatus().equals(FLOOR)){
						return true;
					} else if( fowardElement.MobilityStatus().equals(STATIC) && fowardElement.getName().equals("ParedeRachada") && instance.temMartelo) {
						return true;
					}
					
				}
		return false;
}

	




	
}