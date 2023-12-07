package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Bateria extends GameElement {
	
	private int nivelbateria;
	private Point2D position;

	
	public Bateria(Point2D position, int nivelBateria) {
		super(position);
		this.position = position;
		nivelbateria = nivelBateria;
		setObjectMobilityStatus(FLOOR);
	}

	@Override
	public String getName() {
		return "Bateria";
	}

	@Override
	public int getLayer() {
		return 0;
	}
	
	public int addBatteryToBobcat() {
		return nivelbateria;
	}
	
	@Override
	public Point2D getPosition() {
		return position;
	}
	
	@Override
	public boolean remove() {
		GameEngine instance = GameEngine.getInstance();
		instance.setBattery(nivelbateria); //TODO: Mudei o valor da bateria de 50 para 100
		return true;
	}
	

	

}
