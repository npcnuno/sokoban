package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class GameMoves implements ValidMove{
	
	private Point2D position;
	Direction dir;



	@Override
	public boolean isValid(Direction dir, GameElement element) {
		Point2D newPosition = position.plus(dir.asVector());
		GameEngine instance = GameEngine.getInstance();
		GameElement fowardElement = instance.getGameElement(newPosition);
		
		if (newPosition.getX()>=0 && newPosition.getX()<10 && 
			newPosition.getY()>=0 && newPosition.getY()<10){
			
			if (element.getName() == "Empilhadora") {
					if(instance.getLayer(newPosition) <= 1){
						if(fowardElement.getName() == "Caixote" || fowardElement.getName() == "Palete"){
							if(isValid(dir, fowardElement)){
								element.move(dir);
								instance.bateria = instance.bateria - 2;
								return true;
							}

						} else if(fowardElement.getName() == "Chao" || fowardElement.getName() == "Alvo"){
							element.move(dir);
							instance.bateria--;
							return true;

						} else if(fowardElement.getName() == "Buraco"){
							instance.removeGameElement(position);
							return false;

						} else if(fowardElement.getName() == "Bateria"){

								instance.bateria = instance.bateria + 100;

								instance.removeGameElement(newPosition);
								instance.addGameElement(newPosition,new Chao(newPosition));
								element.move(dir);
								return true;
							
						}

						return true;
					}
				return false;
			}
			if (element.getName() == "Caixote"){
				if(instance.getLayer(newPosition) == 0 && (element.getName() == "Chao" || element.getName() == "Alvo")) {
					element.move(dir);
					return true; 
				} else if(fowardElement.getName() == "Buraco"){
					instance.removeGameElement(position);
					return false;
				}
				return false;
			}
			
			if (element.getName() == "Palete"){
				if(instance.getLayer(newPosition) == 0 && (element.getName() == "Chao" || element.getName() == "Alvo")) {
					element.move(dir);
					return true; 
				}
				return false;
			}
		}
		
	return false;
	}
}