package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;

import pt.iscte.poo.utils.Direction;

public class Teclado{

    public static Direction Key_Pressed(int key) {
		if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_KP_LEFT || key == KeyEvent.VK_A)
			return Direction.LEFT;
		if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_KP_RIGHT  || key == KeyEvent.VK_D)
			return Direction.RIGHT;
		if(key == KeyEvent.VK_UP || key == KeyEvent.VK_KP_UP  || key == KeyEvent.VK_W)
			return Direction.UP;
		if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_KP_DOWN  || key == KeyEvent.VK_S)
			return Direction.DOWN;
		return null;
	}
}

