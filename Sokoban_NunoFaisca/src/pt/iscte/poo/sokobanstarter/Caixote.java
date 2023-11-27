package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
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
	public boolean move(Direction dir) {
		
		// Gera uma direcao aleatoria para o movimento
		/*Direction[] possibleDirections = Direction.values();
		Random randomizer = new Random();
		int randomNumber = randomizer.nextInt(possibleDirections.length);
		Direction randomDirection = possibleDirections[randomNumber];
		*/
		// Move segundo a direcao gerada, mas so' se estiver dentro dos limites
		Point2D newPosition = position.plus(dir.asVector());
		GameEngine instance = GameEngine.getInstance();
		if (newPosition.getX()>=0 && newPosition.getX()<10 && 
			newPosition.getY()>=0 && newPosition.getY()<10 ){
			GameElement element = instance.getGameElement(newPosition);

			if(instance.getLayer(newPosition) == 0){
				if(element.getName() == "Chao") {
					
					Chao chao = (Chao) element;
					
					instance.removeGameElement(newPosition, new Caixote(newPosition));
					instance.removeGameElement(position, new Chao(position));
				
					
					
				return true;
				} else {
					
					
				}
			return true;}
		}
		return false;
	}
	
}
