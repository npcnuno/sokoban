package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Teleporte extends GameElement {

	Point2D position;

	public Teleporte(Point2D position) {
		super(position);
		this.position = position;
		setObjectMobilityStatus(FLOOR);


	}

    @Override
	public String getName() {
		return "Teleporte";
	}

	@Override
	public int getLayer() {
		return 0;
	}
	public Point2D getPosition() {
		return position;
	}

	@Override
	public void move(Direction direction) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'move'");
	}
	public Point2D isHole(Point2D position, Direction dir) {
		GameEngine instance = GameEngine.getInstance();
		position = instance.searchTypeOfGameElement(position, "Teleporte");
		if(instance.getGameElement(position).getName().equals("Teleporte")){
			return position;
		}
		return null;
	}
}
