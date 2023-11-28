package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;

public interface ValidMove {
	
	boolean isValid(Direction direction, GameElement element);

}
