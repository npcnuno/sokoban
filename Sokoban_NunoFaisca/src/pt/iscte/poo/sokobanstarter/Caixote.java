package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Caixote extends GameElement {

	private Point2D position;
	private String imageName;
	private boolean noAlvo;
	private int layer;
	
	public Caixote(Point2D initialposition) {
		super(initialposition);
		position = initialposition;
		imageName = "Caixote";
		noAlvo = false;
		layer = 1;

	}

	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public int getLayer() {
		return layer;
	}
	public void setLayer(int value) {
		layer = value;
	}


	@Override
	public Point2D getPosition(){
		return position;
	}
	
	public boolean isAlvo() {
		return noAlvo;
	}

	public void setAlvo(boolean alvo) {
		this.noAlvo = alvo;
	}
	
	@Override
	public void move(Direction dir) {
		Point2D newPosition = position.plus(dir.asVector());
		GameEngine instance = GameEngine.getInstance();
		GameElement fowardElement = instance.getGameElement(newPosition);
		System.out.print(fowardElement.getLayer() + "\n");

		instance.setBattery(-1);
		if(isValid(dir)){
			 if(fowardElement.getName() == "Alvo") {
					instance.alvosAtingidos++;
					setAlvo(true);
				 }else if(fowardElement.getName() == "Chao")
					 if(isAlvo() == true) {
						 setAlvo(false);
						 instance.alvosAtingidos--;
		}
			 if(fowardElement.getName().equals("Teleporte")) {
					
					Point2D oldPosition = position;
					
					position = instance.searchTypeOfGameElement(newPosition, "Teleporte");
					newPosition = position.plus(dir.asVector());
					
					if(instance.getLayer(newPosition) <= 0) {
						move(dir);
					}else {
						position = oldPosition;
						
						return;
					}
				}
		if(fowardElement.getName().equals("Buraco")) {
			instance.GameOver = true;
		}
			position = newPosition;
		}
	}
	
	
	@Override
	public boolean isValid(Direction dir) {
			Point2D newPosition = position.plus(dir.asVector());
			GameEngine instance = GameEngine.getInstance();
			if (newPosition.getX()>=0 && newPosition.getX()<10 && 
				newPosition.getY()>=0 && newPosition.getY()<10){
						if(instance.getLayer(newPosition) == 0){
							return true;
							}
						}
				return false;
	
	}
	
}
	

