package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import gameoflife.common.CellState;
import gameoflife.controller.GameController;
import gameoflife.controller.GameControllerImpl;
import gameoflife.model.Cell;
import gameoflife.model.Point;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Main extends Application {
    private static GameController gameController;
    private static Color selectedColour = Color.BLACK;
    private Group root = new Group();
    private AnimationTimer timer;
    private double sceneWidth = 500;
    private double sceneHeight = 500;

    private int n = 50;
    private double gridWidth = sceneWidth / n;
    private int m = 50;
    private double gridHeight = sceneHeight / m;

    private static MediaPlayer mediaPlayer;
    MyNode[][] playfield = new MyNode[n][m];

    private static final String musicFileTime1 = "01. Time - Inception.mp3";
    private static final String musicFileNoTimeFor2 = "02. Hans Zimmer - No Time For Caution.mp3";
    private static final String musicFileLionKing3 = "03. This Land (From 'The Lion King').mp3";
    private static final String musicFileCrimsonTide4 = "04. Crimson Tide.mp3";
    private static final String musicFileBatman5 = "05. The Dark Knight Rise.mp3";
    private static final String musicFileDocking6 = "06. The Docking Scene (From 'Interstellar').mp3";
    private static final String musicFileFlight7 = "07. Flight (From 'Man Of Steel').mp3";


    /**
     * Beolvas fájlból egy Object-et.
     *
     * @param name
     * @return a beolvasott fájl tartalma
     * @throws IOException
     * @throws ClassNotFoundException
     */
    static Object load(String name) throws IOException, ClassNotFoundException {
		FileInputStream f = new FileInputStream(name);
		ObjectInputStream in = new ObjectInputStream(f);
		Object res =  in.readObject();
		in.close();
		return res;
	}

    /**
     * Kiír fájlba egy Objectet.
     *
     * @param object
     * @param name
     * @throws IOException
     * @throws ClassNotFoundException
     */
    static void write(Object object, String name) throws IOException, ClassNotFoundException {

		FileOutputStream f = new FileOutputStream(name);
		ObjectOutputStream out = new ObjectOutputStream(f);
		out.writeObject(object);
		out.close();
	}

    /**
	 * Inicializáló függvény.
	 */
	@Override
	public void init() {
        Map<Point, Cell> cellMap = new HashMap<>();
        gameController = new GameControllerImpl(n, m, cellMap);

        playMusic(musicFileDocking6);
        // inicializálja a playfieldet
        for( int i=0; i < n; i++) {
            for( int j=0; j < m; j++) {

                // létrehoz egy node-t
                MyNode node = new MyNode( "Item " + i + "/" + j, i * gridWidth, j * gridHeight, gridWidth, gridHeight);

                // hozzáad egy node-ot a csoporthoz
                root.getChildren().add(node);

                // add to playfield for further reference using an array
                playfield[i][j] = node;
            }
        }
	}

    /**
     * Létrehozza a Menüt.
     * Található benne Játék indítás, Beállítások, Kilépés illetve segítségek.
     *
     * @param primaryStage
     * @return MenuBar
     */
	private MenuBar createMenuBar(Stage primaryStage) {
		MenuBar result = new MenuBar();
		Menu game = new Menu("Játék");
		Menu help = new Menu("Segítség");
		MenuItem rules = new MenuItem("Szabályok");
		MenuItem knownPatterns = new MenuItem("Ismert minták");
		MenuItem settings = new MenuItem("Beállítások");
		MenuItem exit = new MenuItem("Kilépés");
		MenuItem lastGame = new MenuItem("Elõzõ játék újraindítása");
		MenuItem startGame = new MenuItem("Játék indítása");
		MenuItem exitGame = new MenuItem("Játék befejezése");
		MenuItem clearMap = new MenuItem("Pálya törlése");
		MenuItem helpBrowser = new MenuItem("Súgó");

		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});

		/**
		 *A játék szabályainak leírása található meg benne.
		 */
		rules.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("Egy kattintás a cella megjelölése.\nMég egy kattintás a cella megjelölésének törlése\n"
                		+ "A játékot a Játék indítása paranccsal lehet elindítani\n "
                		+ "Leállítani a Játék befejezése paranccsal lehet.\n"
                		+ "A beállítások menüben érhetõek el a játék beállításai.\n"
                		+ "Elõzõ játékmenet visszatöltése az Elõzõ játék újraindítása paranccsal."));

                Scene dialogScene = new Scene(dialogVbox, 500, 200);
                dialog.setScene(dialogScene);
                dialog.show();
			}
		});

		clearMap.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				for (Iterator<Map.Entry<Point, Cell>> iterator = gameController.getStepCellMap().entrySet().iterator(); iterator.hasNext();) {
					Map.Entry<Point, Cell> entry = iterator.next();
					if(!entry.getValue().getCellState().equals(CellState.DEAD)) {
						iterator.remove();
					}
				}
				drawMap();
			}
		});

		/**
		 * Beállítások menüpont, be lehet állítani, hogy melyik zene szóljon, annak a hangerejét, illetve a sejt színét.
		 */
		settings.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage stage = new Stage();
				stage.setTitle("Beállítások");
				stage.initModality(Modality.WINDOW_MODAL);
				stage.setHeight(400);
				stage.setWidth(400);
				stage.initOwner(primaryStage);
				VBox stageVbox = new VBox(20);
				Scene stageScene = new Scene(stageVbox, 400, 400);

				final ColorPicker colorPicker = new ColorPicker();
		        colorPicker.setValue(Color.BLACK);

		        colorPicker.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						selectedColour = colorPicker.getValue();
					}
		        });

				ChoiceBox<String> musicPicker = new ChoiceBox<String>(FXCollections.observableArrayList(
					    musicFileTime1, musicFileNoTimeFor2, musicFileLionKing3, musicFileCrimsonTide4, musicFileBatman5,
						    musicFileDocking6, musicFileFlight7)
					);

					Slider musicVolume = new Slider();

					musicVolume.setMin(0);
					musicVolume.setMax(1);
					musicVolume.setValue(0.3);
					musicVolume.setShowTickLabels(true);
					musicVolume.setShowTickMarks(true);
					musicVolume.setMajorTickUnit(0.5);
					musicVolume.setBlockIncrement(0.1);

				Button applyButton = new Button("Változtatások mentése");

				stageVbox.getChildren().addAll(new Text("Zene kiválasztása\n"), musicPicker, new Text("Hangerõ beállítása\n"), musicVolume,
						new Text("Sejt színének kiválasztása"), colorPicker, applyButton);

				applyButton.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						if(musicPicker.getSelectionModel().getSelectedItem() != null) {
							mediaPlayer.stop();
							playMusic(musicPicker.getSelectionModel().getSelectedItem());
							mediaPlayer.setVolume(musicVolume.getValue());
						}
						drawMap();
					}

				});

				stage.setResizable(false);
				stage.setScene(stageScene);
				stage.show();
			}

		});
		/**
		 * Létrehoz egy új ablakot, ahol az ismert minták találhatóak.
		 * Adott mintára kattintva az megjelenik a pályán.
		 */
		knownPatterns.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                Button beacon = new Button("Jeladó");
                Button block = new Button("Blokk");
                Button boat = new Button("Hajó");
                Button blinker = new Button("Vészvillogó");
            	Button toad	= new Button("Varangyosbéka");
                Button glider = new Button("Sikló");
                Button gliderGun = new Button("Siklóágyú");

                beacon.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						loadPatternFromFile("beacon.dat");
					}

				});

                block.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						loadPatternFromFile("block.dat");
					}

				});

                boat.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						loadPatternFromFile("boat.dat");
					}

				});

                blinker.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						loadPatternFromFile("blinker.dat");
					}

				});

                toad.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						loadPatternFromFile("toad.dat");
					}

				});

                glider.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						loadPatternFromFile("glider.dat");
					}

				});

                gliderGun.setOnAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						loadPatternFromFile("glidergun.dat");
					}
				});

                dialogVbox.getChildren().addAll(beacon, block, boat, blinker, toad, glider, gliderGun);
                Scene dialogScene = new Scene(dialogVbox, 200, 350);
                dialog.setScene(dialogScene);
                dialog.show();

			}

		});

		/**
		 * Játék elindítása parancs.
		 */
		startGame.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				writePattern("lastCellmap.dat");

	            timer = new AnimationTimer() {

	                private long lastUpdate = System.currentTimeMillis();

	                @Override
	                public void handle(long now) {
	                    now = System.currentTimeMillis();
	                    if (now - lastUpdate >= 500) {
	                        gameController.step();
	                        drawMap();
	                        lastUpdate = now;
	                    }
	                }
	            };
	            timer.start();
			}
		});

		/**
		 * Játék befejezése parancs.
		 */
		exitGame.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				timer.stop();
			}
		});

		/**
		 * Elõzõ játék újratöltése parancs.
		 */
		lastGame.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					if(gameController.getStepCellMap() != null) {
						@SuppressWarnings("unchecked")
						HashMap<Point, Cell> load = (HashMap<Point, Cell>) load("lastCellMap.dat");
						gameController.setCellMap(load);
					}
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				drawMap();
			}
		});

		helpBrowser.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
                if (Desktop.isDesktopSupported()) {
                    try {
						Desktop.getDesktop().browse(new URI("https://hu.wikipedia.org/wiki/%C3%89letj%C3%A1t%C3%A9k"));
					} catch (IOException | URISyntaxException e) {
						e.printStackTrace();
					}
                }
			}
		});

		game.getItems().addAll(startGame, exitGame, lastGame, clearMap, new SeparatorMenuItem(), settings, new SeparatorMenuItem(), exit);

		help.getItems().addAll(rules, knownPatterns, new SeparatorMenuItem(), helpBrowser);
		result.getMenus().addAll(game, help);
		return result;
	}

	/**
	 * A megjelenítési felület indítása. Automatikusan indul.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			VBox vBox = new VBox();
			Scene scene = new Scene(vBox, sceneWidth + 50, sceneHeight + 50);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			vBox.getChildren().addAll(this.createMenuBar(primaryStage));

			drawMap();

	        root.setTranslateX(25);
	        root.setTranslateY(10);
	        vBox.getChildren().add(root);
			primaryStage.setTitle("Életjáték");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.centerOnScreen();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *Saját node osztály. Itt történik például a node-ok beszinezése.
	 *
	 * @author Sombali
	 */
	public static class MyNode extends StackPane {
		Rectangle rectangle = null;

        public MyNode( String name, double x, double y, double width, double height) {
            // téglalap létrehozása
            rectangle = new Rectangle(width, height);
            rectangle.setStroke(Color.BLACK);
            rectangle.setFill(Color.WHITE);

            // pozíció beállítása
            setTranslateX(x);
            setTranslateY(y);

            rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
	            	if(!selectedColour.equals(rectangle.getFill())) {
	            		rectangle.setFill(selectedColour);
	            		Point point = new Point((int)x/10, (int)y/10);
	            		gameController.addCell(new Cell(), point);
	            	} else {
	            		rectangle.setFill(Color.WHITE);
	            		Point point = new Point((int)x/10, (int)y/10);
	            		gameController.removeCell(point);
	            	}
				}
            });

            getChildren().add(rectangle);

            }

        /**
         * Kifehéríti a téglalapot.
         */
        public void killCell() {
			rectangle.setFill(Color.WHITE);
		}

        /**
         * Beszínezi a téglalapot.
         */
        public void activeCell() {
			rectangle.setFill(selectedColour);
		}

    }

	/**
	 * A pálya rajzolása.
	 */
	public void drawMap() {
		for( int i=0; i < n; i++) {
            for( int j=0; j < m; j++) {
            	Point point = new Point(i,j);
            	if(gameController.getStepCellMap().containsKey(point) && gameController.getStepCellMap().get(point).getCellState().equals(CellState.ALIVE)) {
           			playfield[i][j].activeCell();
            	}
            	else {
        			playfield[i][j].killCell();
            	}
        	}
		}
	}

	/**
	 * Beolvas egy adott mintát egy fájlból.
	 * @param filename
	 */
	@SuppressWarnings("unchecked")
	public void loadPatternFromFile(String filename) {
		try {
			gameController.setCellMap((HashMap<Point, Cell>) load(filename));
			drawMap();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Kiír egy adott mintát fájlba.
	 * @param filename
	 */
	public void writePattern(String filename) {
		try {
			write(gameController.getStepCellMap(), filename);
		} catch (ClassNotFoundException | IOException e) {
			System.err.println("Nem tudtuk elmenteni a map-et!!");
		}
	}

	/**
	 * Zene elindítása.
	 * @param musicFile
	 */
	public void playMusic(String musicFile) {
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
	}

	public static void main(String[] args) {
		Main.launch();
	}
}





