package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
	
	private int level = 1;


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
	//private GameMoves gameMoves; // Add this line to declare the gameMoves instance

	// Construtor - neste exemplo apenas inicializa uma lista de ImageTiles
	private GameEngine() {
		tileList = new ArrayList<>();
		//gameMoves = new GameMoves(); // Instantiate the gameMoves instance
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
		deleteFile();

		// Criar o cenario de jogo
		
		getFloorScheme();
		createWarehouse();      // criar o armazem
		//createMoreStuff();// criar mais algun objetos (empilhadora, caixotes,...)
		sendImagesToGUI();      // enviar as imagens para a GUI
		bateria = 100;
		
		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage("Sokoban Level " + level + " || bateria: " + bateria);
	}

	// O metodo update() e' invocado automaticamente sempre que o utilizador carrega numa tecla
	// no argumento do metodo e' passada uma referencia para o objeto observado (neste caso a GUI)
	@Override
	public void update(Observed source) {

		int key = gui.keyPressed();    // obtem o codigo da tecla pressionada
		/*if (key == KeyEvent.VK_ENTER)// se a tecla for ENTER, manda a empilhadora mover*/
		Direction dir = Teclado.Key_Pressed(key);		
		if(dir != null){
			GameMoves gameMoves = new GameMoves();
			gameMoves.isValid(dir, bobcat);
			bobcat.rotateImage(dir);
		}
		gui.update();
		gui.setStatusMessage("Sokoban Level " + level + " || bateria: " + bateria);
		if( bateria <=  0) {
				restart();
		}
		if(alvosAtingidos == NumAlvosNivel) {
			nextLevel();
		}
			
			

		
		// redesenha a lista de ImageTiles na GUI, 
		// tendo em conta as novas posicoes dos objetos
	}
	public GameElement getGameElement(Point2D point){

		for (GameElement tile : tileList) {
				if(point.equals(tile.getPosition())){
					return tile;
				}
			}
		return null;
	}
	
	public void setBattery(int value) {
		this.bateria = this.bateria + value;
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
	

//	public void addGameElement(Point2D point, GameElement element2){
//		GameElement element = getGameElement(point);
//		
//		int i = 0;
//		for (GameElement tile : tileList) {
//			if(point.equals(tile.getPosition())){
//				tileList.add(i, element2);
//				gui.addImage(element2);
//
//				break;
//			}
//			i++;
//		}
//
//	}
	// Reinicia o jogo
 void restart(){
		deleteFile();
		gui.clearImages();
		tileList.clear();
		level = 1;
		alvosAtingidos = 0;
		NumAlvosNivel = 0;
		getFloorScheme();
		createWarehouse();      // criar o armazem
		//createMoreStuff();// criar mais algun objetos (empilhadora, caixotes,...)
		sendImagesToGUI();      // enviar as imagens para a GUI
		bateria = bobcat.getBateria();
		gui.update();

		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage("Sokoban Level " + level + " || bateria: " + bateria);
		
	}
	private void nextLevel(){
		registerLevels();
		gui.clearImages();
		tileList.clear();
		level++;
		bateria = 100;
		alvosAtingidos = 0;
		NumAlvosNivel = 0;
		getFloorScheme();
		createWarehouse();      // criar o armazem
		//createMoreStuff();// criar mais algun objetos (empilhadora, caixotes,...)
		sendImagesToGUI();      // enviar as imagens para a GUI
		gui.update();
		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage("Sokoban Level " + level + " || bateria: " + bateria);
		
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
					tileList.add(new Caixote(new Point2D(x,y), false));
					tileList.add(new Chao(new Point2D(x,y)));

					break;				
				case '#':	
				tileList.add(new Parede(new Point2D(x,y)));break;
				case 'B':
					//tileList.add(new Chao(new Point2D(x,y)));
					tileList.add(new Bateria(new Point2D(x,y)));break;	
				case 'O':
					tileList.add(new Buraco(new Point2D(x,y)));break;	
				case 'P':
					tileList.add(new Palete(new Point2D(x,y)));break;	
				case 'M':
					tileList.add(new Martelo(new Point2D(x,y)));break;
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
		
		public void deleteFile() {
		      File file = new File("pontuacao.txt");
		      file.delete();
		}
		
		public void registerLevels() {
		    File file = new File("pontuacao.txt");
		    try {
		        if (file.createNewFile()) {
		            System.out.println("File created: " + file.getName());
		        } else {
		            System.out.println("File already exists.");
		        }

		        try (BufferedWriter output = new BufferedWriter(new FileWriter(file, true))) {
		            output.write("Level:" + this.level + "::pontuacao:" + this.bateria);
		            output.newLine();  // Adds a new line for each entry
		        }
		    } catch (IOException e) {
		        System.out.println("An error occurred.");
		        e.printStackTrace();
		    }
		}

	// Criacao de mais objetos - neste exemplo e' uma empilhadora e dois caixotes
	/*private void createMoreStuff() {
		//bobcat = new Empilhadora( new Point2D(5,5));
		//tileList.add(bobcat);

		tileList.add(new Caixote(new Point2D(3,3)));
		tileList.add(new Caixote(new Point2D(3,2)));
	}*/

	// Envio das mensagens para a GUI - note que isto so' precisa de ser feito no inicio
	// Nao e' suposto re-enviar os objetos se a unica coisa que muda sao as posicoes  
	private void sendImagesToGUI() {
		List<ImageTile> imageTileList = new ArrayList<>();
		for (GameElement element : tileList) {
				imageTileList.add((ImageTile) element);
		}
		gui.addImages(imageTileList);
	}
	
	//extrair esquema
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

		    // Debug: Print each element of floorScheme
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
