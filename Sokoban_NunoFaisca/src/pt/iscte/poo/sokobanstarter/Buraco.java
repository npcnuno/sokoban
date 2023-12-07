package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Buraco extends GameElement {

	private Point2D position;
	private boolean blocked;

	
	public Buraco(Point2D position) {
		super(position);
		this.position = position;
		this.blocked = false;
		setObjectMobilityStatus(FLOOR);
	}

    @Override
	public String getName() {
		return "Buraco";
	}

	@Override
	public int getLayer() {
		return 0;
	}
	
	public Point2D isHole(Point2D position, Direction dir) {
		GameEngine instance = GameEngine.getInstance();
		GameElement element = instance.getGameElement(position);
		if(!blocked) {
			this.blocked = true;
			element.setObjectMobilityStatus(FLOOR);
			isValid(element);
			return this.position;
		} else {
			return this.position;
		}
	}
	public void isValid(GameElement element) {
		GameEngine instance = GameEngine.getInstance();

		if(!element.getName().equals("Palete")){
			instance.GameOver = true;
		}
	}
	

	
	
	




}