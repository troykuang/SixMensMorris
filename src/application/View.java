package application;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.effect.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class View extends Application {
	private Stage leStage = new Stage();
	private Circle circle [][];
	private Circle buttons[][];
	private Scene scene;
	private Pane gameboard = new Pane();
	private Label redDiscs;
	private Label blueDiscs;
	private Label playing = new Label();
	private Label status = new Label();



	@FXML
	private int cells;

	@Override
	public void start(Stage primaryStage) {// Stage primaryStage is the javaFX 
		try {
			Parent root = FXMLLoader.load(getClass().getResource("TitleScreen.fxml"));		//to load the FXML file
			Scene scene = new Scene(root, 500, 500);										//want the menu screen to be smaller
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("6 Men's Morris Game");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();															//display the stage


		} catch (Exception e) {													//any exceptions with loading the FXML
			e.printStackTrace();
		}
	}

	private Pane drawBoard(){			//dynamically drawing the game board  and returning it on a Pane
		
		
		gameboard.setPrefSize(500, 500);		//going to always be 500 by 500
		
		/*creating the board*/
		Rectangle backboard = new Rectangle(500,500);
		backboard.setFill(Color.rgb(145,107,99));
		gameboard.getChildren().add(backboard);		//adding all the lines to the gameboard (to be returned in the end)
		
		
		/* creating the lines of the board game */
		double increment = (500.0 /(cells - 1));
		int p = (cells - 1) / 2;
		double min = 0.0;
		double max = 500.0;
		ArrayList<Line> lines = new ArrayList<Line>();
		
		
		while (p != 0) {
		
		
			// top line
			Line line = new Line();
			line.setStroke(Color.SIENNA);
			line.setStrokeWidth(3.0);
			line.setStartX(min);
			line.setStartY(min);
			line.setEndX(max);
			line.setEndY(min);
			lines.add(line);


			// bottom line
			line = new Line();
			line.setStroke(Color.SIENNA);
			line.setStrokeWidth(3.0);
			line.setStartX(min);
			line.setStartY(max);
			line.setEndX(max);
			line.setEndY(max);
			lines.add(line);


			// right line
			line = new Line();
			line.setStrokeWidth(3.0);
			line.setStroke(Color.SIENNA);
			line.setStartX(max);
			line.setStartY(min);
			line.setEndX(max);
			line.setEndY(max);
			lines.add(line);


			// left line
			line = new Line();
			line.setStrokeWidth(3.0);
			line.setStroke(Color.SIENNA);
			line.setStartX(min);
			line.setStartY(min);
			line.setEndX(min);
			line.setEndY(max);
			lines.add(line);


			if (p - 1 != 0) {
				// top vertical line
				line = new Line();
				line.setStrokeWidth(3.0);
				line.setStroke(Color.SIENNA);
				line.setStartX((min + max) / 2);
				line.setStartY(min);
				line.setEndX((min + max) / 2);
				line.setEndY(min + increment);
				lines.add(line);

				// bottom vertical line
				line = new Line();
				line.setStrokeWidth(3.0);
				line.setStroke(Color.SIENNA);
				line.setStartX((min + max)/2);
				line.setStartY(max);
				line.setEndX((min + max)/2);
				line.setEndY(max - increment);
				lines.add(line);

				// left horizontal line
				line = new Line();
				line.setStrokeWidth(3.0);
				line.setStroke(Color.SIENNA);
				line.setStartX(min);
				line.setStartY((min + max) / 2);
				line.setEndX(min + increment);
				line.setEndY((min + max) / 2);
				lines.add(line);

				
				// right horizontal line
				line = new Line();
				line.setStrokeWidth(3.0);
				line.setStroke(Color.SIENNA);
				line.setStartX(max - increment);
				line.setStartY((min + max) / 2);
				line.setEndX(max);
				line.setEndY((min + max) / 2);
				lines.add(line);


			}

			p -= 1;
			min += increment;
			max -= increment;

		}
		

		gameboard.getChildren().addAll(lines);		//adds all the lines to the gameboard
		
		
		circle = new Circle[cells][cells];			//this will be the drawn nodes creating the board
		buttons = new Circle[cells][cells];			//these will be the clickable circles that turn into the game pieces (they are initially transparent)
		int i, j;
		p = (cells - 1) / 2; // Reinitializing it
		int lowerBound = 0;
		int upperBound = cells;
		while (p != 0) {
			i = lowerBound;
			j = lowerBound;
			// going to add buttons to the applicable board spots
			while (i < upperBound) {
				while (j < upperBound) {
					if ((i == ((cells - 1) / 2)) && (j == ((cells - 1) / 2))) {
						System.out.println(i);
						j += p;
						// nothing, don't make a button for the middle square
					} else {
						circle[i][j] = new Circle((i*increment),(j*increment),10.0);
						buttons[i][j] = new Circle(i*increment,j*increment, 15.0);
						//listener
						buttons[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {		//so they can be clicked
							public void handle (MouseEvent mouseEvent) {
								// Handle here.
								int x = 0, y = 0;
								for(int i = 0;i<cells;i++){
									for(int j =0;j<cells;j++){
									if(mouseEvent.getSource()==buttons[i][j]){	//to get the x,y of the pressed button, so we know which circle has the game piece
										x=i;
										y=j;
									}
									
									}
								}
								Controller.inputClick(x,y); 		//the controller will then communicate with the model and decide what colour to draw/if its valid
							}
						}); //end of lambda expression
						buttons[i][j].setFill(Color.TRANSPARENT);	//initially there are no pieces placed, so they are transparent
						gameboard.getChildren().addAll(circle[i][j],buttons[i][j]);	//adding them both
						
						
						j += p;
					}
				}
				i += p;
				j = lowerBound;
			}
			lowerBound += 1;
			upperBound -= 1;
			p -= 1;
		}
		 return gameboard;		//return the completely drawn board
		
		
	}
	
	
	public void newGame(int n) {		//the new game stage/screen
		cells = n; // #((mensMorris/3)*2)+1 IN THIS CASE 5
		try {
			AnchorPane total = new AnchorPane(); // going to be parent to the
													// scene builder items and
													// the ones we create
													// dynamically
			Parent root2 = FXMLLoader.load(getClass().getResource("GameScreen.fxml"));		//the gameScreen fxml file
			Pane gameboard = drawBoard();
			total.setTopAnchor(gameboard, 75.0);
			total.setLeftAnchor(gameboard, 20.0);
			
			
			redDiscs = new Label("6");		//the labels so the player knows how many they have left
			blueDiscs = new Label("6");		//blue disc labels
			redDiscs.setTranslateX(785);
			redDiscs.setTranslateY(392);
			blueDiscs.setTranslateX(705);
			blueDiscs.setTranslateY(392);
			
			playing.setText(Controller.currentPlayer());	//to tell the person who is playing
			playing.setTranslateX(730);
			playing.setTranslateY(20);
			playing.setTextFill(Color.WHITE);
			playing.setFont(new Font("Arial", 30));
			
			status = new Label();		//will tell if the game is in progress, red wins, or blue wins
			status.setTranslateX(720);
			status.setTranslateY(200);
			status.setTextFill(Color.WHITE);
			Controller.checkWins();		//check wins will update this label after checking the state
			
			
			
			total.getChildren().addAll(root2, gameboard, redDiscs, blueDiscs, playing, status); 	//adding it all to the AnchorPane
			Scene scene = new Scene(total, 900, 600);	//set the scene size and add the AnchorPane

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			leStage.setScene(scene);
			leStage.setResizable(false);	//don't want it to be resizable
			leStage.show();
			leStage.setTitle("6 Men's Morris Game");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void changeState(int i){		//to change the Label status
		System.out.println(i);			//check the state
		switch (i){
		case 1:
			System.out.println("I'M IN CHANGESTATE 1");
			status.setText("In PROGRESS");	//tell the player the game is in progress
			break;
		case 2:
			System.out.println("I'M IN CHANGESTATE 2");
			status.setText("Red Wins!");		//when red wins
			break;
		case 3:
			System.out.println("I'M IN CHANGESTATE 3");
			status.setText("Blue Wins!");		//when blue wins
			break;
		}
		
	}
	
	public void changePlaying(){			//to change the label to the player
		playing.setText(Controller.currentPlayer());
	}
	
	
	public void loadGame(int n, String colourOption){	//the loadgame stage, either manually load or from file

		cells = n; // #((mensMorris/3)*2)+1 IN THIS CASE 5
		try {
			AnchorPane total = new AnchorPane(); // going to be parent to the
													// scene builder items and
													// the ones we create
													// dynamically
			Parent root2 = FXMLLoader.load(getClass().getResource("LoadGameBoard.fxml"));

			Pane gameboard = drawBoard();
			total.setTopAnchor(gameboard, 75.0);
			total.setLeftAnchor(gameboard, 20.0);

			redDiscs = new Label("6");		//will set up the discs to show each having 6
			blueDiscs = new Label("6");
			redDiscs.setTranslateX(785);
			redDiscs.setTranslateY(392);
			blueDiscs.setTranslateX(705);
			blueDiscs.setTranslateY(392);


			total.getChildren().addAll(root2, gameboard, redDiscs, blueDiscs);
			Scene scene = new Scene(total, 900, 600);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			leStage.setScene(scene);
			leStage.setResizable(false);		//not resizeable
			leStage.show();
			leStage.setTitle("6 Men's Morris Game");

		} catch (Exception e) {
			e.printStackTrace();
		}


		
	}
	
	public void noGame(){			//just to tell the user when they are trying to load a game and there is no saved game
		Stage dialogStage = new Stage();		//opening a pop up dialog box
		VBox p = new VBox();
		p.setAlignment(Pos.CENTER);
		p.setPadding(new Insets(20));
		Text heading = new Text("Sorry, no saved game to load. Place pieces manually.");	//error message
		p.getChildren().add(heading);
		
		Button b = new Button("Okay");		//button to close the dialog box
		b.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle (MouseEvent mouseEvent) {
				Stage st = (Stage) (b.getScene().getWindow());
				st.close();
				
				
			}
		});
		p.getChildren().add(b); 		
		Scene s = new Scene(p);
		dialogStage.setScene(s);
		dialogStage.show();
	}
	
	public void invalid(ArrayList<String> x){	//to show the user what the mistakes were when they placed the discs manually
			//System.out.println("VIEW CLASS");
			Stage dialogStage = new Stage();
			
			
			VBox p = new VBox();
			p.setAlignment(Pos.CENTER);
			p.setPadding(new Insets(20));
			Text heading = new Text("Sorry friend, you've placed some things incorrectly!");
			p.getChildren().add(heading);
			
			for(String i: x ){			// showing all of the errors on the dialog 
			Text n = new Text(i);
			p.getChildren().add(n);
			}
			
			Button b = new Button("Fix it, Okay?");			//button to close the dialog box
			b.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle (MouseEvent mouseEvent) {
					Stage st = (Stage) (b.getScene().getWindow());
					st.close();
					
					
				}
			});
			p.getChildren().add(b); 		
			Scene s = new Scene(p);
			dialogStage.setScene(s);
			dialogStage.show();		//show the dialog box
		}

	public void resetLoad(){				//when the person wants to reset the manual load board
		for(int i=0; i< buttons.length; i++){
			for (int j=0;j<buttons.length;j++){
				if(buttons[i][j] != null){
					buttons[i][j].setFill(Color.TRANSPARENT);		//changing all the game pieces back to transparents
				}
				
				//also we want to clear the board in model (done in the controller)
			}
		}
		redDiscs.setText("6");		//resetting the amount of discs
		blueDiscs.setText("6");
		

	}
	
	public void playerChange(){		//keeps the label updated telling the user who's turn it is
		playing.setText(Controller.currentPlayer());
	}

		
	public void drawDashed(int i,int j){		//to let people know where they can slide the game pieces (after all pieces have been played)
		double x = buttons[i][j].getCenterX();	//getting the x,y for the available points
		double y = buttons[i][j].getCenterY();
		Circle current = new Circle(x,y,20.0);
		current.setFill(null);;
		current.setStroke(Color.WHITE);
		current.getStrokeDashArray().addAll(5d, 5d);
		gameboard.getChildren().add(current);	//adding it on top of the gameboard
		
	}
	
	public void removeDashed(){		//we want to remove the dashed indicators as soon as the person has played
		gameboard.getChildren().remove(gameboard.getChildren().size()-1);
	}
	
	public void draw(String color, int i, int j){		//this function takes care of drawing on the pieces as they are played
		//System.out.println("drawing colour " + color);
		//this is going to draw on the circle that was placed on the board
		Paint player = Color.web(color);	//the colour piece to draw
		System.out.println(player.toString());
		Light.Distant light = new Light.Distant(45.0,80.0,Color.WHITE);
		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(2.0);
		buttons[i][j].setFill(player);	//i,j tells us the spot in which needs a disc
		buttons[i][j].setEffect(lighting);
		if(color.equals("Red")){		//taking care of the disc counters for red and blue
			if(Integer.parseInt(redDiscs.getText()) == 0){
				//null
			}
			else{
				redDiscs.setText(Integer.toString(Integer.parseInt(redDiscs.getText())-1));	
			}
		}
		else {							//blue counter decrementing 
			if(Integer.parseInt(blueDiscs.getText()) == 0){
				//null
			}
			else{
				blueDiscs.setText(Integer.toString(Integer.parseInt(blueDiscs.getText())-1));	
			}
		}
	}

	public void undrawDisc(int i,int j){			//getting rid of disc
		buttons[i][j].setFill(Color.TRANSPARENT);	//just reFill it transparent
	}
	
	public void exit(){						//to close the whole stage if they want to exit
		Stage st = (Stage) (leStage.getScene().getWindow());
		resetLoad();						//we also need to reset it, in case they open another window, we dont want the game pieces to be there still
		st.close();

	}
	
	public static void main(String[] args) {		//javaFX enforced launch of the stages
		launch(args);
	}

}
