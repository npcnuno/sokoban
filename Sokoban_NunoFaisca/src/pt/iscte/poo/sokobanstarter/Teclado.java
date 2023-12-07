package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;

import pt.iscte.poo.utils.Direction;

public class Teclado {
    
        public static Direction  Key_Pressed(int key) {

        
        if(key == KeyEvent.VK_LEFT ||  key == KeyEvent.VK_KP_LEFT || key == KeyEvent.VK_A)
            return Direction.LEFT;
        if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_KP_RIGHT ||  key == KeyEvent.VK_D)
            return Direction.RIGHT;
        if(key == KeyEvent.VK_UP || key == KeyEvent.VK_KP_UP  || key == KeyEvent.VK_W)
            return Direction.UP;
        if(key == KeyEvent.VK_DOWN  || key == KeyEvent.VK_KP_DOWN  || key == KeyEvent.VK_S)
            return Direction.DOWN;
        if(key == KeyEvent.VK_R)
        	restart();
        if(key == KeyEvent.VK_N )
        	nextLevel();
        if(key == KeyEvent.VK_Q )
        	quit();
        	
        	
        return null;
    }


public static void restart() {
	GameEngine instance = GameEngine.getInstance();
	instance.restartLevel();
}

public static void nextLevel() {
	GameEngine instance = GameEngine.getInstance();
	if(instance.wonGame)
		instance.restartGame();

	if(instance.PassedLevel && !instance.wonGame)
		instance.nextLevel();
}

public static void quit() {
	GameEngine instance = GameEngine.getInstance();
	
	if(instance.wonGame)
		instance.restartGame();
}

}