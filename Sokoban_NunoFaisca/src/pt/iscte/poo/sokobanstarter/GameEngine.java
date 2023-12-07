package pt.iscte.poo.sokobanstarter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class GameEngine implements Observer  {
	
	
	// Enum-like constants for different types of objects in the game
    private static String STATIC = "StaticObject";
    private static String FLOOR_LEVEL = "FloorObject";
    private static String MOVABLE = "MovableObject";

    // Game state variables
    private int level = 0;
    private int lastLevel = getLastLevelNumber(level);
    public boolean hasHammer = false; 
    public boolean GameOver = false;
    public boolean PassedLevel = false;
    public boolean wonGame = false;
    private String username;
    public int battery;
    public int targetsHit = 0; // Targets hit
    private int numberOfTargetsOfLevel = 0; // Number of targets in the level

	// Dimensoes da grelha de jogo
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;
	
	private char[][] floorScheme = new char[GRID_WIDTH][GRID_HEIGHT];

	
	private static GameEngine INSTANCE; // Referencia para o unico objeto GameEngine (singleton)
	private ImageMatrixGUI gui;  		// Referencia para ImageMatrixGUI (janela de interface com o utilizador) 
	private List<GameElement> tileList;	// Lista de imagens estáticas (no nosso exemplo, apenas o chao)
	private Empilhadora bobcat;	        // Referencia para a empilhadora
	

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
		gui = ImageMatrixGUI.getInstance();    // 1. obter instancia ativa de ImageMatrixGUI	
		gui.setSize(GRID_HEIGHT, GRID_WIDTH);  // 2. configurar as dimensoes 
		gui.registerObserver(this);            // 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go();                              // 4. lancar a GUI
		// Criar o cenario de jogo
		gui.setMessage("The controls of the game are WASD or any arrow keys \n"
				+ "Press Q to quit and to restart the level");
		username = gui.askUser("Enter your username: \n "
				+ "After press any arrow Key or any WASD key");
		getFloorSchema();
		createWarehouse();      // criar o armazem
		tileList.sort(Comparator.comparing(GameElement::getName).reversed());
		tileList.sort(Comparator.comparing(GameElement::getLayer));
		sendImagesToGUI();      // enviar as imagens para a GUI
		battery = 100;
		gui.setStatusMessage(getBestLevelScores()+" || Level " + level + 
				" || Bateria: " + battery + " || Martelo: " + hasHammer);
	}

	/**
	 * The update method is automatically invoked whenever the user presses a key.
	 * It processes the key press, updates the game state, and refreshes the GUI
	 * accordingly.
	 * 
	 * @param source The observed object triggering the update, typically the GUI.
	 */
	@Override
	public void update(Observed source) {
		int key = gui.keyPressed();
		Direction dir = Teclado.Key_Pressed(key);

		// Perform actions only if the game is not over, level not passed, and game not
		// won
		if (dir != null && !GameOver && !PassedLevel && !wonGame) {
			bobcat.move(dir);
			gui.update();
			gui.setStatusMessage(getBestLevelScores() + " || Level " +
					level + " || Bateria: " + battery + " || Martelo: " + hasHammer);

			// Handle game over scenario
			if (battery <= 0) {
				gui.setMessage("GAME OVER");
				restartGame();
			}

			// Check if the level is passed
			if (targetsHit == numberOfTargetsOfLevel) {
				PassedLevel = true;
				if (PassedLevel && this.level >= lastLevel) {
					wonGame = true;
				}
			}

			// Update GUI messages based on game state (Game Over, Passed Level, Won Game)
			if (GameOver) {
				gui.setStatusMessage("Game Over || press R to restart level");
				gui.setMessage(getUsersBestScores());
			}
			if (PassedLevel) {
				gui.setStatusMessage("Passed Level || press N to continue");
				gui.setMessage(getAllBestLevelScores());
			}
			if (wonGame) {
				gui.setStatusMessage("You Won || press N to continue");
				gui.setMessage(getUsersBestScores());
			}
		}
	}

	/**
	 * Restarts the current level.
	 * This method resets the game state and recreates the level layout.
	 */
	public void restartLevel(){
			gui.clearImages();
			tileList.clear();
			targetsHit = 0;
			numberOfTargetsOfLevel = 0;
			hasHammer = false;
			GameOver = false;
			PassedLevel = false;
			wonGame = false;
			tileList.sort(Comparator.comparing(GameElement::getName).reversed());
			tileList.sort(Comparator.comparing(GameElement::getLayer));
			createWarehouse();     
			sendImagesToGUI();      
			setBattery(100);;
			gui.update();
			gui.setStatusMessage(getBestLevelScores()+" || Level " + 
			level + " || Bateria: " + getBattery() + " || Martelo: " + getHammer());
			
	}
	 
	 /**
	  * Advances the game to the next level.
	  * This method registers the levels, increments the level number, and restarts the level with the new layout.
	  */
		public void nextLevel(){
			registerLevels();
			level++;
			getFloorSchema();
			restartLevel();
		}
		
		
		/**
		 * Restarts the entire game.
		 * This method resets the level to the beginning and restarts the level.
		 */
		public void restartGame() {
			level = 0;
			getFloorSchema();
			restartLevel();	
		}
		
		
		/**
		 * Quits the game.
		 * This method disposes of the GUI, closing the game window.
		 */
		public void quit() {
			gui.dispose();
		}
	
		
		/**
		 * Retrieves the top-most GameElement at a specified point on the game grid.
		 * The method considers the layer of each element, giving priority to elements on higher layers.
		 * @param point The Point2D representing the position on the grid.
		 * @return The top-most GameElement at the specified point, or null if no element is found.
		 */
		public GameElement getGameElement(Point2D point) {
			List<GameElement> elementList = new ArrayList<>();
			for (GameElement element : tileList) {
				if (point.equals(element.getPosition())) {
					elementList.add(element);
				}
			}
			elementList.sort(Comparator.comparing(GameElement::getLayer).reversed());

			if (elementList.size() == 1)
				return elementList.get(0);

			for (GameElement element : elementList) {
				if (element.MobilityStatus().equals(MOVABLE))
					return element;
			}
			for (GameElement element : elementList) {
				if (element.MobilityStatus().equals(MOVABLE) && !element.getName().equals("Buraco"))
					return element;
			}
			for (GameElement element : elementList) {
				if (element.MobilityStatus().equals(STATIC) || element.MobilityStatus().equals(FLOOR_LEVEL))
					return element;
			}
			return null;
		}
		
		
		/**
		 * Searches for a GameElement of a specific type, excluding the specified point.
		 * @param point The Point2D position to exclude from the search.
		 * @param name The name of the GameElement type to search for.
		 * @return The position of the found GameElement, or null if not found.
		 */
		public Point2D searchTypeOfGameElement(Point2D point, String name) {
			for (GameElement tile : tileList) {
				if (!point.equals(tile.getPosition()) && name.equals(tile.getName())) {
					return tile.getPosition();
				}
			}
			return null;
		}
		
		/**
		 * Adds a new GameElement to the game at a specified point.
		 * The method places the new element in the tile list and updates the GUI.
		 * @param point The Point2D position where the element is to be added.
		 * @param element2 The GameElement to be added to the game.
		 */
		public void addGameElement(Point2D point, GameElement element2) {
			int i = 0;
			for (GameElement tile : tileList) {
				if (point.equals(tile.getPosition())) {
					tileList.add(i, element2);
					gui.addImage(element2);
					break;
				}
				i++;
			}
		}

		/**
		 * Removes a GameElement from the game at a specified point.
		 * The method updates both the tile list and the GUI.
		 * 
		 * @param point The Point2D position from which the element is to be removed.
		 */
		public void removeGameElement(Point2D point) {
			GameElement element = getGameElement(point);
			gui.removeImage((ImageTile) element);
			int i = 0;
			for (GameElement tile : tileList) {
				if (point.equals(tile.getPosition())) {
					tileList.remove(i);
					break;
				}
				i++;
			}
		}

		/**
		 * This method is used to increase or decrease the battery level of the player.
		 * 
		 * @param value The amount to be added to the current battery level. 
		 *              A negative value will decrease the battery level.
		 */		
		public void setBattery(int value) {
			this.battery = this.battery + value;
		}

		public boolean getHammer() {
			return hasHammer;
		}

		public int getBattery() {
			return battery;
		}
		
		
		/**
		 * This method is used to increase or decrease the battery level of the player.
		 * 
		 * @param value The amount to be added to the current battery level. 
		 *              A negative value will decrease the battery level.
		 */
		public void setHammer(boolean value) {
			this.hasHammer = value;
		}
		
		
		/**
		 * Retrieves the layer of an element at a specific point in the game.
		 * This method is used to determine the rendering layer of the element 
		 * at the specified position, which can be important for visual 
		 * representation and game mechanics.
		 * 
		 * @param point The Point2D representing the position on the grid.
		 * @return The layer number of the element at the specified position.
		 *         Returns 0 if no element is found at that position.
		 */
		public int getLayer(Point2D point){
			for (ImageTile tile : tileList) {
				if(point.equals(tile.getPosition())){
					return tile.getLayer();
				}
			}
			return 0;
		}


	/**
	 * Creates the warehouse layout for the current level.
	 * This method iterates through the grid defined by GRID_HEIGHT and GRID_WIDTH,
	 * and places various game elements on the grid based on the character representation
	 * in the floorScheme array. Each character corresponds to a different type of game element.
	 */
	private void createWarehouse() {
		for (int y = 0; y < GRID_HEIGHT; y++) {
			for (int x = 0; x < GRID_HEIGHT; x++) {
				switch (floorScheme[y][x]) {
					case ' ':
						// Add floor tile
						tileList.add(new Chao(new Point2D(x, y)));
						break;
					case 'X':
						// Add target tile and increment target count
						tileList.add(new Alvo(new Point2D(x, y)));
						numberOfTargetsOfLevel++;
						break;
					case 'C':
						// Add crate and underlying floor tile
						tileList.add(new Caixote(new Point2D(x, y)));
						tileList.add(new Chao(new Point2D(x, y)));
						break;
					case '#':
						// Add wall tile
						tileList.add(new Parede(new Point2D(x, y)));
						break;
					case 'B':
						// Add battery tile
						tileList.add(new Bateria(new Point2D(x, y)));
						break;
					case 'O':
						// Add hole tile
						tileList.add(new Buraco(new Point2D(x, y)));
						break;
					case 'P':
						// Add pallet and underlying floor tile
						tileList.add(new Palete(new Point2D(x, y)));
						tileList.add(new Chao(new Point2D(x, y)));
						break;
					case 'M':
						// Add hammer tile
						tileList.add(new Martelo(new Point2D(x, y)));
						break;
					case '%':
						// Add cracked wall tile
						tileList.add(new ParedeRachada(new Point2D(x, y)));
						break;
					case 'T':
						// Add teleport tile
						tileList.add(new Teleporte(new Point2D(x, y)));
						break;
					case 'E':
						// Add floor tile and place the forklift (bobcat)
						tileList.add(new Chao(new Point2D(x, y)));
						bobcat = new Empilhadora(new Point2D(x, y));
						tileList.add(bobcat);
						break;
				}
			}
		}
	}

				
	/**
	 * Registers the level scores to a file.
	 * This method checks if the current score is better than the best score
	 * recorded and updates the scores file accordingly.
	 */
	public void registerLevels() {
		int bestScore = Integer.parseInt(getBestLevelScores().split(":", 2)[1]);
		if (bestScore < getBattery()) {
			File file = new File("pontuacao.txt");
			try {
				file.createNewFile();

				try (BufferedWriter output = new BufferedWriter(new FileWriter(file, true))) {
					output.write("Username:" + username + "| Level:" + this.level + "| Score:" + getBattery());
					output.newLine(); // Adds a new line for each entry
				}
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
	}

		
	/**
	 * Reads a file and returns its content as a list of string arrays.
	 * Each line in the file is split into parts and added to the list.
	 * 
	 * @param filePath The path of the file to read.
	 * @return A list of string arrays, where each array represents a line in the
	 *         file.
	 * @throws FileNotFoundException if the file is not found.
	 */
	private List<String[]> readFile(String filePath) throws FileNotFoundException {
		List<String[]> lines = new ArrayList<>();
		try (Scanner fileScanner = new Scanner(new File(filePath))) {
			while (fileScanner.hasNextLine()) {
				String[] parts = fileScanner.nextLine().split("\\|", 3);
				if (parts.length == 3) {
					lines.add(parts);
				}
			}
		}
		return lines;
	}

	/**
	 * Retrieves the best score for the current level.
	 * This method scans through the scores file and finds the best score for the
	 * user at the current level.
	 * 
	 * @return A string representing the best score of the user at the current
	 *         level.
	 */
	public String getBestLevelScores() {
		int bestScore = 0;
		try {
			for (String[] parts : readFile("pontuacao.txt")) {
				String username = parts[0].split(":", 2)[1].trim();
				if (username.equals(this.username)) {
					int currentLevel = Integer.parseInt(parts[1].split(":", 2)[1].trim());
					int currentScore = Integer.parseInt(parts[2].split(":", 2)[1].trim());
					if (currentLevel == level && currentScore > bestScore) {
						bestScore = currentScore;
					}
				}
			}
		} catch (FileNotFoundException e) {
			return "Error: Scores file not found.";
		}
		return this.username + ":" + bestScore;
	}

	/**
	 * Retrieves the best scores of all users for the current level.
	 * This method scans through the scores file and compiles a list of the best
	 * scores at the current level.
	 * 
	 * @return A formatted string representing the best scores of all users at the
	 *         current level.
	 */
	public String getAllBestLevelScores() {
		StringBuilder usersBestScore = new StringBuilder("Best Scores of level " + level + "\n");
		try {
			for (String[] parts : readFile("pontuacao.txt")) {
				int currentLevel = Integer.parseInt(parts[1].split(":", 2)[1].trim());
				if (currentLevel == level) {
					String username = parts[0].split(":", 2)[1].trim();
					int currentScore = Integer.parseInt(parts[2].split(":", 2)[1].trim());
					usersBestScore.append("User(").append(username).append("): ").append(currentScore).append("\n");
				}
			}
		} catch (FileNotFoundException e) {
			return "Error: Scores file not found.";
		}
		return usersBestScore.append("\nPress N to continue").toString();
	}

	/**
	 * Retrieves the best scores for the current user for all levels.
	 * This method scans through the scores file and compiles a list of the best
	 * scores of the current user across all levels.
	 * 
	 * @return A formatted string representing the best scores of the current user
	 *         for each level.
	 */
	public String getUsersBestScores() {
		List<String> userScores = new ArrayList<>();
		try {
			for (String[] parts : readFile("pontuacao.txt")) {
				String username = parts[0].split(":", 2)[1].trim();
				int currentLevel = Integer.parseInt(parts[1].split(":", 2)[1].trim());
				if (currentLevel == level) {
					int currentScore = Integer.parseInt(parts[2].split(":", 2)[1].trim());
					userScores.add(username + ": " + currentScore);
				}
			}
		} catch (FileNotFoundException e) {
			return "Error: Scores file not found.";
		}

		List<String> userBestScores = new ArrayList<>();
		for (String scoreInfo : userScores) {
			String[] parts = scoreInfo.split(": ");
			String username = parts[0];
			int score = Integer.parseInt(parts[1]);

			boolean found = false;
			for (String bestScoreInfo : userBestScores) {
				if (bestScoreInfo.startsWith(username + ":")) {
					int existingScore = Integer.parseInt(bestScoreInfo.split(": ")[1]);
					if (score > existingScore) {
						userBestScores.set(userBestScores.indexOf(bestScoreInfo), scoreInfo);
					}
					found = true;
					break;
				}
			}

			if (!found) {
				userBestScores.add(scoreInfo);
			}
		}

		StringBuilder result = new StringBuilder("Sokoban Game Best Scores:\n");
		for (String bestScore : userBestScores) {
			result.append(bestScore).append("\n");
		}
		return result.append("\nPress N to continue").toString();
	}
		
	/**
	 * Sends the current game elements to the graphical user interface (GUI) for
	 * display.
	 * This method collects all the game elements from the tile list and adds them
	 * to the GUI.
	 * It is typically used to refresh or update the game visuals after changes in
	 * the game state.
	 */
	private void sendImagesToGUI() {
		List<ImageTile> imageTileList = new ArrayList<>();

		for (GameElement element : tileList) {
			imageTileList.add((ImageTile) element);
		}

		gui.addImages(imageTileList);
	}

	
	/**
	 * Recursively determines the number of the last level available in the game.
	 * This method checks for the existence of level files in a sequence,
	 * incrementing the level number until a file is not found.
	 * It's used to identify how many levels are present in the game.
	 *
	 * @param level The starting level number from which to begin the search.
	 *              Usually starts with 1 or the lowest level number.
	 * @return The number of the last level available. If no file is found for the
	 *         current level, it returns one less than the current level.
	 */
	private int getLastLevelNumber(int level) {
		File file = new File("./levels/level" + level + ".txt");

		if (file.exists()) {
			return getLastLevelNumber(level + 1);
		} else {
			return level - 1;
		}
	}

	/**
	 * Reads the floor scheme for the current level from a file and populates the
	 * floorScheme array.
	 * This method opens a file corresponding to the current level and reads its
	 * contents line by line.
	 * Each line represents a row in the level's floor scheme, and each character in
	 * a line represents a different element or tile.
	 */
	private void getFloorSchema() {
		File file = new File("./levels/level" + level + ".txt");
		try {
			Scanner sc = new Scanner(file);
			int row = 0;
			while (sc.hasNextLine() && row != 10) {
				floorScheme[row] = sc.nextLine().toCharArray();
				row++;
			}
			sc.close();

		} catch (FileNotFoundException e) {
			// Handle the case where the level file is not found
			e.printStackTrace();
		}
	}
	
}
