package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Buraco extends GameElement {

	private Point2D position;
	
	public Buraco(Point2D position) {
		super(position);
		this.position = position;		
	}

    @Override
	public String getName() {
		return "Buraco";
	}

	@Override
	public int getLayer() {
		return 0;
	}
	

	
	
	




}