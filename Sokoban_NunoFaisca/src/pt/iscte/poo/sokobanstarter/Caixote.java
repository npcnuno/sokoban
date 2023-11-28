package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Caixote extends GameElement {

	private Point2D position;
	private String imageName;
	private boolean alvo;
	
	public Caixote(Point2D initialposition, boolean alvo) {
		super(initialposition);
		position = initialposition;
		imageName = "Caixote";
		this.setAlvo(alvo);

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

	@Override
	public void move(Direction dir) {
		Point2D newPosition = position.plus(dir.asVector());
		GameEngine instance = GameEngine.getInstance();
		instance.addGameElement(newPosition, new Caixote(newPosition, this.alvo));
		instance.removeGameElement(position);
	}

	public boolean isAlvo() {
		return alvo;
	}

	public void setAlvo(boolean alvo) {
		this.alvo = alvo;
	}
}
	

