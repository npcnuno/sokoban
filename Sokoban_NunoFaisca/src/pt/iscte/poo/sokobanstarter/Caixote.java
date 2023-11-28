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
		this.alvo = alvo;

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
		
			GameElement element = instance.getGameElement(newPosition);

			if(element.getLayer() == 0){
				if(alvo == true) {
					alvo = false;
					instance.alvosAtingidos--;
				}
				if(element.getName() == "Alvo") {
					alvo = true;
					instance.alvosAtingidos++;

				}
				instance.addGameElement(newPosition, new Caixote(newPosition, alvo));
				instance.removeGameElement(position);
				
				
		}
	}
	
}
