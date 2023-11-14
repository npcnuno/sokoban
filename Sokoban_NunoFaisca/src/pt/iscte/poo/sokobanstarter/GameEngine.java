package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
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


public class GameEngine implements Observer {
	
	private int level = 1;


	// Dimensoes da grelha de jogo
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;
	
	private char[][] floorScheme = new char[GRID_WIDTH][GRID_HEIGHT];

	
	private static GameEngine INSTANCE; // Referencia para o unico objeto GameEngine (singleton)
	private ImageMatrixGUI gui;  		// Referencia para ImageMatrixGUI (janela de interface com o utilizador) 
	private List<ImageTile> tileList;	// Lista de imagens
	private Empilhadora bobcat;	        // Referencia para a empilhadora


	// Construtor - neste exemplo apenas inicializa uma lista de ImageTiles
	private GameEngine() {
		tileList = new ArrayList<>();   
	}

	// Implementacao do singleton para o GameEngine
	public static GameEngine getInstance() {
		if (INSTANCE==null)
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
		
		getFloorScheme();
		createWarehouse();      // criar o armazem
		//createMoreStuff();      // criar mais algun objetos (empilhadora, caixotes,...)
		sendImagesToGUI();      //a enviar as imagens para a GUI

		
		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage("Sokoban Starter - demo");
	}

	// O metodo update() e' invocado automaticamente sempre que o utilizador carrega numa tecla
	// no argumento do metodo e' passada uma referencia para o objeto observado (neste caso a GUI)
	
	//separar, por à parte, adicionalmente já havia uma função para isto na diretion.- professor 
	@Override
	public void update(Observed source) {
		int key = gui.keyPressed();   
		Direction dir = Teclado.Key_Pressed(key);
        if(dir != null)
            bobcat.move(dir);
		gui.update();                  
		// redesenha a lista de ImageTiles na GUI, 
		// tendo em conta as novas posicoes dos objetos
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
					break;	
				case 'C':
					tileList.add(new Caixote(new Point2D(x,y)));	
					break;				
				case '#':	
					tileList.add(new Parede(new Point2D(x,y)));
					break;
				case 'B':
					tileList.add(new Bateria(new Point2D(x,y)));
					break;	
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

	// Criacao de mais objetos - neste exemplo e' uma empilhadora e dois caixotes
	private void createMoreStuff() {
		//bobcat = new Empilhadora( new Point2D(5,5));
		//tileList.add(bobcat);

		tileList.add(new Caixote(new Point2D(3,3)));
		tileList.add(new Caixote(new Point2D(3,2)));
	}

	// Envio das mensagens para a GUI - note que isto so' precisa de ser feito no inicio
	// Nao e' suposto re-enviar os objetos se a unica coisa que muda sao as posicoes  
	private void sendImagesToGUI() {
		gui.addImages(tileList);
	}
	
	//extrair esquema
	private void getFloorScheme() {
		File file = new File("./levels/level"+level+".txt");
		
		
		try {
		    Scanner sc = new Scanner(file);
		    int row = 0;
		    while (sc.hasNextLine() && row!= 10) {
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
