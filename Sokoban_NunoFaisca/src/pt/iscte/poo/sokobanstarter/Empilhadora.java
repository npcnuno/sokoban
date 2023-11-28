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
	


	
	public void rotateImage(Direction dir) {
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
	}
	//TODO implementar o movimento da empelhadora
	public void move(Direction dir) {
		
		// Gera uma direcao aleatoria para o movimento
		/*Direction[] possibleDirections = Direction.values();
		Random randomizer = new Random();
		int randomNumber = randomizer.nextInt(possibleDirections.length);
		Direction randomDirection = possibleDirections[randomNumber];
		*/
		// Move segundo a direcao gerada, mas so' se estiver dentro dos limites
		
		Point2D newPosition = position.plus(dir.asVector());
		position = newPosition;
	}
}