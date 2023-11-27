package pt.iscte.poo.sokobanstarter;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends GameElement{
	private static String FORKLIFT_DOWN = "Empilhadora_D";
	private static String FORKLIFT_UP = "Empilhadora_U";
	private static String FORKLIFT_LEFT = "Empilhadora_L";
	private static String FORKLIFT_RIGHT = "Empilhadora_R";

	private Point2D position;
	private String imageName;
	private int bateria;
	
	
	public Empilhadora(Point2D initialPosition){
		super(initialPosition);
		position = initialPosition;
		imageName = FORKLIFT_DOWN;
		bateria  = 100;
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
	
	public int getBateria() { return this.bateria; }

	
	
	//TODO implementar o movimento da empelhadora
	public void move(Direction dir) {
		
		// Gera uma direcao aleatoria para o movimento
		/*Direction[] possibleDirections = Direction.values();
		Random randomizer = new Random();
		int randomNumber = randomizer.nextInt(possibleDirections.length);
		Direction randomDirection = possibleDirections[randomNumber];
		*/
		// Move segundo a direcao gerada, mas so' se estiver dentro dos limites
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
		Point2D cnewPosition = newPosition.plus(dir.asVector());

		GameEngine instance = GameEngine.getInstance();
		this.bateria = this.bateria - 1;
		if (newPosition.getX()>=0 && newPosition.getX()<10 && 
			newPosition.getY()>=0 && newPosition.getY()<10 ){
			GameElement objeto = instance.getGameElement(newPosition);

			if(instance.getLayer(newPosition) == 0) {
					
				if(objeto.getName() == "Bateria"){
					Bateria bateria = (Bateria) objeto;
					
					
					this.bateria = this.bateria + bateria.addBatteryToBobcat();
					instance.removeGameElement(newPosition, new Chao(newPosition));
				}
				position = newPosition;
				//Add Battery to bobcat
				}
			
			else if(instance.getLayer(newPosition) == 1){
				if(objeto.getName() == "Caixote"){
					Caixote caixote = (Caixote) objeto;
					if(caixote.move(dir))
						position = newPosition;
				}
			}
		
		}
	}
}