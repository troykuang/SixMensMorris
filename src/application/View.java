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
import javafx.scene.text.Text;


public class View extends Application {
	private Stage leStage = new Stage();
	private Circle circle [][];
	private Scene scene;



	@FXML
	private int cells;

	
	@Override
	public void start(Stage primaryStage) {// Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("TitleScreen.fxml"));
			Scene scene = new Scene(root, 500, 500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("6 Men's Morris Game");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	
	
	
	public void newGame(int n) {
		cells = n; // #((mensMorris/3)*2)+1 IN THIS CASE 5
		try {
			AnchorPane total = new AnchorPane(); // going to be parent to the
													// scene builder items and
													// the ones we create
													// dynamically
			Parent root2 = FXMLLoader.load(getClass().getResource("GameScreen.fxml"));
			Pane gameboard = drawBoard();
			total.setTopAnchor(gameboard, 105.0);
			total.setLeftAnchor(gameboard, 20.0);

			total.getChildren().addAll(root2, gameboard);
			scene = new Scene(total, 900, 900);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			leStage.setScene(scene);
			leStage.show();
			leStage.setTitle("6 Men's Morris Game");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void loadGame(int n){//, String colourOption){

		cells = n; // #((mensMorris/3)*2)+1 IN THIS CASE 5
		try {
			AnchorPane total = new AnchorPane(); // going to be parent to the
													// scene builder items and
													// the ones we create
													// dynamically
			Parent root2 = FXMLLoader.load(getClass().getResource("LoadGameBoard.fxml"));

			Pane gameboard = drawBoard();
			total.setTopAnchor(gameboard, 105.0);
			total.setLeftAnchor(gameboard, 20.0);

			total.getChildren().addAll(root2, gameboard);
			Scene scene = new Scene(total, 900, 900);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			leStage.setScene(scene);
			leStage.show();
			leStage.setTitle("6 Men's Morris Game");

		} catch (Exception e) {
			e.printStackTrace();
		}


		
	}
	

	public void invalid(String [] x){
			//System.out.println("VIEW CLASS");
			Stage dialogStage = new Stage();
			//dialogStage.initModality(Modality.WINDOW_MODAL);
			
			VBox p = new VBox();
			p.setAlignment(Pos.CENTER);
			p.setPadding(new Insets(20));
			Text heading = new Text("Sorry friend, you've placed some things incorrectly!");
			heading.setStyle("-fx-text-fill: red" + "-fx-font-size: 16px");
			p.getChildren().add(heading);
			for(String i: x ){
			Text n = new Text(i);
			p.getChildren().add(n);
			}
			Button b = new Button("Fix it, Okay?");
			b.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle (MouseEvent mouseEvent) {
					Stage st = (Stage) (b.getScene().getWindow());
					st.close();
					//((Button) mouseEvent.getSource()).getScene().close();
					
				}
			});
			p.getChildren().add(b); 		//((Node)(event.getSource())).getScene().getWindow().hide(););
			Scene s = new Scene(p);
			dialogStage.setScene(s);
			dialogStage.show();
		}

	
	//these will be for the reset button that is to be implemented at a later time
	/*public void resetLoad(){
		try {
			Parent root = FXMLLoader.load(getClass().getResource("LoadGameBoard.fxml"));
			AnchorPane total = new AnchorPane();
			Pane gameboard = drawBoard();
			total.getChildren().addAll(root,gameboard);
			Scene scene = new Scene(total, 900, 900);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			leStage.setTitle("6 Men's Morris Game");
			leStage.setScene(scene);
			leStage.show();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void resetGame(){
		try {
			Parent root = FXMLLoader.load(getClass().getResource("GameScreen.fxml"));
			AnchorPane total = new AnchorPane();
			Pane gameboard = drawBoard();
			total.getChildren().addAll(root,gameboard);
			Scene scene = new Scene(total, 900, 900);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			leStage.setTitle("6 Men's Morris Game");
			leStage.setScene(scene);
			leStage.show();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
		

	public void draw(String color, int i, int j){
		//this is going to draw on the circle that was placed on the board
		Paint player = Color.web(color);
		//System.out.println(player.toString());
		Light.Distant light = new Light.Distant(45.0,80.0,Color.WHITE);
		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(2.0);


		
		circle[i][j].setFill(player);
		circle[i][j].setEffect(lighting);

	}

	
	private Pane drawBoard(){
		
		Pane gameboard = new Pane();
		gameboard.setPrefSize(660, 660);
		
		/*creating the board*/
		Rectangle backboard = new Rectangle(660,660);
		backboard.setFill(Color.rgb(145,107,99));
		gameboard.getChildren().add(backboard);
		
		
		/* creating the lines of the board game */
		double increment = (660.0 /(cells - 1));
		int p = (cells - 1) / 2;
		double min = 0.0;
		double max = 660.0;
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
		gameboard.getChildren().addAll(lines);
		
		//creating all the nodes for the game board 
		circle = new Circle[cells][cells];
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
						//listener
						circle[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
							public void handle (MouseEvent mouseEvent) {
								// Handle here.
								int x = 0, y = 0;
								for(int i = 0;i<cells;i++){
									for(int j =0;j<cells;j++){
									if(mouseEvent.getSource()==circle[i][j]){
										x=i;
										y=j;
										}
									}
								}
								Controller.inputClick(x,y);
							}
						}); //end of lambda expression
						
						gameboard.getChildren().add(circle[i][j]);

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
		 return gameboard;

	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
