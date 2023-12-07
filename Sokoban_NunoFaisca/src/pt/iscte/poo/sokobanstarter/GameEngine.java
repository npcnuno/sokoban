package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

// Note que esta classe e' um exemplo - nao pretende ser o inicio do projeto, 
// embora tambem possa ser usada para isso.
//
// No seu projeto e' suposto haver metodos diferentes.
// 
// As coisas que comuns com o projeto, e que se pretendem ilustrar aqui, sao:
// - GameEngine implementa Observer - para  ter o metodo update(...)  
// - Configurar a janela do interface grafico (GUI):
//        + definir as dimensoes
//        + registar o objeto GameEngine ativo como observador da GUI
//        + lancar a GUI
// - O metodo update(...) e' invocado automaticamente sempre que se carrega numa tecla
//
// Tudo o mais podera' ser diferente!


public class GameEngine implements Observer  {
	
	protected static String STATIC = "StaticObject";
	protected static String FLOOR = "FloorObject";
	public static String MOVABLE = "MovableObject";

	
	
	private int level = 0;
	private int lastLevel = 0;
	public boolean temMartelo = false;
	public boolean GameOver = false;
	public boolean PassedLevel = false;
	public boolean wonGame = false;
	private String username;

	// Dimensoes da grelha de jogo
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;
	
	private char[][] floorScheme = new char[GRID_WIDTH][GRID_HEIGHT];

	
	private static GameEngine INSTANCE; // Referencia para o unico objeto GameEngine (singleton)
	private ImageMatrixGUI gui;  		// Referencia para ImageMatrixGUI (janela de interface com o utilizador) 
	private List<GameElement> tileList;	// Lista de imagens estáticas (no nosso exemplo, apenas o chao)
	private Empilhadora bobcat;	        // Referencia para a empilhadora
	public int bateria;
	public int alvosAtingidos = 0;
	private int NumAlvosNivel = 0;

	// Construtor - neste exemplo apenas inicializa uma lista de ImageTiles
	private GameEngine() {
		tileList = new ArrayList<>();
	}

	// Implementacao do singleton para o GameEngine
	public static GameEngine getInstance() {
		if (INSTANCE == null)
			return INSTANCE = new GameEngine();
		return INSTANCE;
	}
	// Inicio
	public void start() {

		// Setup inicial da janela que faz a interface com o utilizador
		// algumas coisas poderiam ser feitas no main, mas estes passos tem sempre que ser feitos!
		this.lastLevel = getLastLevelNumber(lastLevel);
		gui = ImageMatrixGUI.getInstance();    // 1. obter instancia ativa de ImageMatrixGUI	
		gui.setSize(GRID_HEIGHT, GRID_WIDTH);  // 2. configurar as dimensoes 
		gui.registerObserver(this);            // 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go();                              // 4. lancar a GUI
		System.out.print(lastLevel);
		// Criar o cenario de jogo
		gui.setMessage("The controls of the game are WASD or any arrow keys");
		username = gui.askUser("Enter your username: \n After press any arrow Key or any WASD key");
		getFloorScheme();
		createWarehouse();      // criar o armazem
		tileList.sort(Comparator.comparing(GameElement::getName).reversed());
		tileList.sort(Comparator.comparing(GameElement::getLayer));
		sendImagesToGUI();      // enviar as imagens para a GUI
		bateria = 100;

		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage(getBestLevelScores()+" || Level " + level + " || Bateria: " + bateria + " || Martelo: " + temMartelo);
	}

	// O metodo update() e' invocado automaticamente sempre que o utilizador carrega numa tecla
	// no argumento do metodo e' passada uma referencia para o objeto observado (neste caso a GUI)
	@Override
	public void update(Observed source) {

		int key = gui.keyPressed();    // obtem o codigo da tecla pressionada
		Direction dir = Teclado.Key_Pressed(key);	
		
		
		if(dir != null && !GameOver && !PassedLevel && !wonGame){
			bobcat.move(dir);
			gui.update();
			gui.setStatusMessage(getBestLevelScores()+" || Level " + level + " || Bateria: " + bateria + " || Martelo: " + temMartelo);
			if( bateria <=  0) {
					restartLevel();
			}
			if(alvosAtingidos == NumAlvosNivel) {
				PassedLevel = true;
			if(PassedLevel && this.level >= lastLevel) {
				wonGame = true;
				System.out.print(wonGame);
			}

		}
		if(GameOver) {
			gui.setStatusMessage("Game Over || press R to restart level");
			gui.setMessage(getUsersBestScores());
		}
		if(PassedLevel) {
			gui.setStatusMessage("Passed Level || press N to continue");
			gui.setMessage(getAllBestLevelScores());
		}
		if(wonGame) {
			gui.setStatusMessage("You Won || press N to continue");
			gui.setMessage(getUsersBestScores());

		}

}
	}
	public GameElement getGameElement(Point2D point) {
		List<GameElement> elementList = new ArrayList<>();
	    for (GameElement element : tileList) {
	       if(point.equals(element.getPosition())) {	
	    	   elementList.add(element);
	    	 }
	    }
		elementList.sort(Comparator.comparing(GameElement::getLayer).reversed());

	    if(elementList.size() ==1)
	    	return elementList.get(0);
	    
	    for(GameElement element : elementList) {
	    	if(element.MobilityStatus().equals(MOVABLE))
	    		return element;
	    }
	    for(GameElement element : elementList) {
	    	if(element.MobilityStatus().equals(MOVABLE) && !element.getName().equals("Buraco"))
	    		return element;
	    }
	    for(GameElement element : elementList) {
	    	if(element.MobilityStatus().equals(STATIC) || element.MobilityStatus().equals(FLOOR))
	    		return element;
	    }
	    
	    return null;
	    
	} 
	
	public void setBattery(int value) {
		this.bateria = this.bateria + value;
	}
	public void setMartelo(boolean value) {
		this.temMartelo = value;
	}
	
	public Point2D searchTypeOfGameElement(Point2D point, String name){
		
		for (GameElement tile : tileList) {
			if(!point.equals(tile.getPosition()) && name.equals(tile.getName()) ){
				return tile.getPosition();
			}
		}
		return null;

	}
	public void addGameElement(Point2D point, GameElement element2){
		
		int i = 0;
		for (GameElement tile : tileList) {
			if(point.equals(tile.getPosition())){
				tileList.add(i, element2);
				gui.addImage(element2);

				break;
			}
			i++;
		}

	}
	public void removeGameElement(Point2D point){
		GameElement element = getGameElement(point);
		gui.removeImage((ImageTile) element);
		int i = 0;
		for (GameElement tile : tileList) {
			if(point.equals(tile.getPosition())){
				tileList.remove(i);
				break;
			}
			i++;
		}
	}
	// Reinicia o nível
 public void restartLevel(){
		gui.clearImages();
		tileList.clear();
		alvosAtingidos = 0;
		NumAlvosNivel = 0;
		getFloorScheme();
		createWarehouse();      // criar o armazem
		//createMoreStuff();// criar mais algun objetos (empilhadora, caixotes,...)
		sendImagesToGUI();      // enviar as imagens para a GUI
		bateria = 100;
		gui.update();

		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage(getBestLevelScores()+" || Level " + level + " || Bateria: " + bateria + " || Martelo: " + temMartelo);
		
}
	public void nextLevel(){
		registerLevels();
		gui.clearImages();
		tileList.clear();
		level++;
		bateria = 100;
		alvosAtingidos = 0;
		NumAlvosNivel = 0;
		getFloorScheme();
		createWarehouse();      // criar o armazem
		sendImagesToGUI();      // enviar as imagens para a GUI
		gui.update();
		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage(getBestLevelScores()+" || Level " + level + " || bateria: " + bateria + " || Martelo: " + temMartelo);
		
	}
	public void restartGame() {
		level = 1;
		gui.clearImages();
		tileList.clear();
		alvosAtingidos = 0;
		NumAlvosNivel = 0;
		getFloorScheme();
		createWarehouse();      // criar o armazem
		//createMoreStuff();// criar mais algun objetos (empilhadora, caixotes,...)
		sendImagesToGUI();      // enviar as imagens para a GUI
		bateria = 100;
		gui.update();

		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage(getBestLevelScores()+" || Level " + level + " || Bateria: " + bateria + " || Martelo: " + temMartelo);
		
	}


	// Criacao da planta do armazem - so' chao neste exemplo 
	private void createWarehouse() {
		for (int y=0; y<GRID_HEIGHT; y++) {
			for (int x=0; x<GRID_HEIGHT; x++) {
				switch(floorScheme[y][x]) {
					
				case ' ':
					tileList.add(new Chao(new Point2D(x,y)));
					break;
				case 'X':
					tileList.add(new Alvo(new Point2D(x,y)));
					NumAlvosNivel++;
					break;	
					
				case 'C':
					tileList.add(new Caixote(new Point2D(x,y)));
					tileList.add(new Chao(new Point2D(x,y)));

					break;				
				case '#':	
					tileList.add(new Parede(new Point2D(x,y)));break;
				case 'B':
					tileList.add(new Bateria(new Point2D(x,y), 100));break;	
				case 'O':
					tileList.add(new Buraco(new Point2D(x,y)));break;	
				case 'P':
					tileList.add(new Palete(new Point2D(x,y)));
					tileList.add(new Chao(new Point2D(x,y)));

					break;
					
				case 'M':

					tileList.add(new Martelo(new Point2D(x,y)));

					break;
				case '%':
					tileList.add(new ParedeRachada(new Point2D(x,y)));break;
				case 'T':
					tileList.add(new Teleporte(new Point2D(x,y)));break;			
				case 'E':
					tileList.add(new Chao(new Point2D(x,y)));	
					bobcat = new Empilhadora(new Point2D(x,y));
					tileList.add(bobcat);	
					break;
				
				}

			}
			}
		}
			
	
		public int getLayer(Point2D point){
			for (ImageTile tile : tileList) {
				if(point.equals(tile.getPosition())){
					return tile.getLayer();
				}
			}
			return 0;
		}
				
		public void registerLevels() {
			int bestScore = Integer.parseInt(getBestLevelScores().split(":", 2)[1].trim());
			if(bestScore < bateria) {
			    File file = new File("pontuacao.txt");
			    try {
			        if (file.createNewFile()) {
			            System.out.println("File created: " + file.getName());
			        } else {
			            System.out.println("File already exists.");
			        }
	
			        try (BufferedWriter output = new BufferedWriter(new FileWriter(file, true))) {
			            output.write("Username:" + username + "| Level:" + this.level + "| Score:" + this.bateria);
			            output.newLine();  // Adds a new line for each entry
			        }
			    } catch (IOException e) {
			        System.out.println("An error occurred.");
			        e.printStackTrace();
			    }
			}
		}

		public String getBestLevelScores() {
		    int bestScore = 0;

		    try (Scanner fileScanner = new Scanner(new File("pontuacao.txt"))) {
		        while (fileScanner.hasNextLine()) {
		            String[] parts = fileScanner.nextLine().split("\\|", 3); // usage of \\ for special caracter

		            if (parts.length == 3) {
		                String username = parts[0].split(":", 2)[1].trim();
		                if(username.equals(this.username)) {
			                int currentLevel = Integer.parseInt(parts[1].split(":", 2)[1].trim());
			                int currentScore = Integer.parseInt(parts[2].split(":", 2)[1].trim());
	
			                if (currentLevel == level && currentScore > bestScore) {
			                    bestScore = currentScore;
			                }
			    		    return username + ":" + bestScore;

		                }
		            }
		        }
		    } catch (FileNotFoundException e) {
		        System.out.println("An error occurred trying to retrieve the ScoresFile.");
		        e.printStackTrace();
		    }

		    return"Username: " + username;
		}
		public String getAllBestLevelScores() {
			List<String> usersBestScore = new ArrayList<>();
		    try (Scanner fileScanner = new Scanner(new File("pontuacao.txt"))) {
		        while (fileScanner.hasNextLine()) {
		            String[] parts = fileScanner.nextLine().split("\\|", 3); // usage of \\ for special caracter

		            if (parts.length == 3) {
		                String username = parts[0].split(":", 2)[1].trim();
			                int currentLevel = Integer.parseInt(parts[1].split(":", 2)[1].trim());
			                int currentScore = Integer.parseInt(parts[2].split(":", 2)[1].trim());
			                if(currentLevel == level)
			                	usersBestScore.add("User(" + username + "): "+currentScore);

		            }
		        }
		    } catch (FileNotFoundException e) {
		        System.out.println("An error occurred trying to retrieve the ScoresFile.");
		        e.printStackTrace();
		    }
		    String usersBestScoreString = "Best Scores of level " + level +"\n";
		    
		    for(String BestScore : usersBestScore) {
		    	usersBestScoreString =  usersBestScoreString + BestScore + " \n " ;
		    }
		    return usersBestScoreString + "\n" + "Press N to continue";
		    
		}
		public String getUsersBestScores() {
			List<String> userScores = new ArrayList<>();
		    try (Scanner fileScanner = new Scanner(new File("pontuacao.txt"))) {
		        while (fileScanner.hasNextLine()) {
		            String[] parts = fileScanner.nextLine().split("\\|", 3); // usage of \\ for special caracter

		            if (parts.length == 3) {
		                String username = parts[0].split(":", 2)[1].trim();
			                int currentLevel = Integer.parseInt(parts[1].split(":", 2)[1].trim());
			                int currentScore = Integer.parseInt(parts[2].split(":", 2)[1].trim());
			                if(currentLevel == level)
			                	userScores.add(username +": " + currentScore);
			                

		            }
		        }
		    } catch (FileNotFoundException e) {
		        System.out.println("An error occurred trying to retrieve the ScoresFile.");
		        e.printStackTrace();
		    }
		    List<String> userBestScores = new ArrayList<>();
		    for (String scoreInfo : userScores) {
		        String[] parts = scoreInfo.split(": ");
		        String username = parts[0];
		        int score = Integer.parseInt(parts[1]);

		        boolean added = false;
		        for (int i = 0; i < userBestScores.size(); i++) {
		            String userScore = userBestScores.get(i);
		            String existingUsername = userScore.split(": ")[0];
		            int existingScore = Integer.parseInt(userScore.split(": ")[1]);

		            if (username.equals(existingUsername)) {
		                if (score > existingScore) {
		                    userBestScores.set(i,username + ": " + score);
		                }
		                added = true;
		                break;
		            }
		        }
		        
		        if (!added) {
		            userBestScores.add(scoreInfo);
		        }
		    }
		    String usersBestScoreString = " Sokoban Game Best Scores: \n";
		    
		    for(String BestScore : userBestScores) {
		    	usersBestScoreString = usersBestScoreString + BestScore + " \n ";
		    }


		    return usersBestScoreString + " \n " + "Press N to continue";
		    
		}
		
		


	// Envio das mensagens para a GUI - note que isto so' precisa de ser feito no inicio
	// Nao e' suposto re-enviar os objetos se a unica coisa que muda sao as posicoes  
	private void sendImagesToGUI() {
		List<ImageTile> imageTileList = new ArrayList<>();
		for (GameElement element : tileList) {
				imageTileList.add((ImageTile) element);
		}
		gui.addImages(imageTileList);
	}
	private int getLastLevelNumber(int level) {
	    File file = new File("./levels/level" + level + ".txt");
  
	    if(file.exists()) 
	        return getLastLevelNumber(level + 1);
	     else 
	        return level -1;
	   }
	
	private void getFloorScheme() {
		File file = new File("./levels/level"+level+".txt");
		
		
		try {
		    Scanner sc = new Scanner(file);
		    int row = 0;
		    while (sc.hasNextLine() && row != 10) {
				floorScheme[row] = sc.nextLine().toCharArray();

		        row++;

		    }
		    sc.close();

		    for (int r = 0; r < floorScheme.length; r++) {
		        for (int c = 0; c < floorScheme[r].length; c++) {
		            System.out.print(floorScheme[r][c] + " ");
		        }
		        System.out.println();
		    }
		} 
		catch (FileNotFoundException e) {
		    e.printStackTrace();
		}

	}
	
}
