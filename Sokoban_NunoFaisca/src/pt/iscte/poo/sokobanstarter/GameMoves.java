package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class GameMoves implements ValidMove{
	
	@Override
	public boolean isValid(Direction dir, GameElement element) {
		Point2D position = element.getPosition();
		Point2D newPosition = position.plus(dir.asVector());
		GameEngine instance = GameEngine.getInstance();
		GameElement fowardElement = instance.getGameElement(newPosition);
		if (newPosition.getX()>=0 && newPosition.getX()<10 && 
			newPosition.getY()>=0 && newPosition.getY()<10){

			if (element.getName() == "Empilhadora_D" || element.getName() == "Empilhadora_U" || element.getName() == "Empilhadora_L" || element.getName() == "Empilhadora_R") {
					if(instance.getLayer(newPosition) <= 1){
						if(fowardElement.getName() == "Caixote" || fowardElement.getName() == "Palete"){
							if(isValid(dir, fowardElement)){
								element.move(dir);
								instance.setBattery(-2);

								return true;
							}

						} else if(fowardElement.getName() == "Chao" || fowardElement.getName() == "Alvo"){
							System.out.print(fowardElement.getName());
							instance.setBattery(-1);

							element.move(dir);
							return true;

						} else if(fowardElement.getName() == "Buraco"){
							instance.removeGameElement(position);
							instance.setBattery(-1000);

							return false;

						} else if(fowardElement.getName() == "Bateria"){

								instance.setBattery(100);

								instance.removeGameElement(newPosition);
								instance.addGameElement(newPosition,new Chao(newPosition));
								element.move(dir);
								return true;
							
						}
						return true;
					}

				return false;
			}
			
			//
			if (element.getName() == "Caixote"){

				if(instance.getLayer(newPosition) == 0) {
					Caixote caixote = (Caixote) element;

				 if(fowardElement.getName() == "Buraco"){
					instance.removeGameElement(position);
					return false;
				} else if(fowardElement.getName() == "Alvo") {
						instance.alvosAtingidos++;
						caixote.setAlvo(true);
				}else if(fowardElement.getName() == "Chao")
				 if(caixote.isAlvo() == true)
						instance.alvosAtingidos--;
					
					element.move(dir);
					System.out.print(instance.alvosAtingidos);
					return true; 
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