package application;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.fxml.*;
import javafx.event.*;
import javafx.scene.effect.*;
import javafx.scene.control.Button;

public class Controller {
	public static int N; // N Men's Morris
	private static boolean gameplay; //state of whether the game is in play or not
	private int gameTile;
	static Model laModel = new Model();
	static View leView = new View();
	private int redDiscsLeft = N;
	private int blueDiscsLeft = N;

	
	

	@FXML 
	private String colourChoice;
	@FXML 
	private Label titleScreenLabel = new Label("Red");
	@FXML
	private Label redDiscs = new Label();
	@FXML
	private Label blueDiscs = new Label();


		
	/**
	 * Gets the player color depending from the button pressed on the Menu Screen.
	 * 
	 * @param event Mouse Clicked
	 * 
	 */
	public void getPlayerColour(ActionEvent event){
		colourChoice = ((Button)event.getSource()).getText();	//will give us either "Red" or "Blue" BASED ON THE HOMESCREEN BUTTONS ON THE MAIN MENU
		titleScreenLabel.setText(((Button)event.getSource()).getText());
		laModel.assignPlayer(colourChoice);									//THIS IS SO THE MODEL KNOWS WITH BOOLEANS WHICH PLAYER IS WHICH COLOUR
		
	}
	
	/**
	 * Changes the Player 1 color to Blue.
	 */
	public void changeDiscColourBlue(){
			laModel.changePlayerColourBlue();	
			//System.out.println("change to blue");
			titleScreenLabel.setText("Blue");
	}
	
	/**
	 * Changes the Player 1 color to Red.
	 */
	public void changeDiscColourRed(){
			laModel.changePlayerColourRed();
			//System.out.println("change to red");
			titleScreenLabel.setText("Red");
	}

	/**
	 * Starts a new game
	 * 
	 * @param event Mouse clicked on New Game button.
	 */
	public boolean startNewGame(ActionEvent event){
		//if there is a state in model, call that
		gameplay = true;
		int N = 6;		//so that we can pass the # of mens morris into the VIew
		redDiscs.setText(Integer.toString(redDiscsLeft));		//this will show the players how many discs they have left to place
		blueDiscs.setText(Integer.toString(blueDiscsLeft));
		gameTile = ((N/3)*2)+1;	//array size needed to draw the gameboard for example 6 mens morris requires an array of 5x5
		leView.newGame(gameTile); //array size # of mens morris passed into the view
		laModel.createBoard(N);
		laModel.currentBoard.showBoards();


		laModel.getfirstPlayer();
		if (laModel.getPlayerColour().equals("Red")){
			changeDiscColourRed();
		}
		else {
			changeDiscColourBlue();
		}
		return true;

	}
	
	
	public void load(ActionEvent event){
		gameplay = false;
		changeDiscColourRed();
		gameTile = ((6/3)*2)+1;
		laModel.createBoard(N);
		laModel.currentBoard.showBoards();
		leView.loadGame(gameTile);
	}
	
	public static void removeGamePiece(int x, int y){
		laModel.resetA(x, y);
		System.out.println("removed piece at "+x +" and "+ y);
	}
	
	//for when we implement the reset buttons

	public void resetLoad(){
		changeDiscColourRed();
		laModel.createBoard(N);
		leView.resetLoad();
	}
/*	
	public void resetSceneGame(){
		changeDiscColourRed();
		laModel.reset();
		leView.resetGame();
	}*/

	public static void inputClick(int x, int y){
		System.out.println("mouse clicked at circle: "+y+" "+x);		//	JUST A TEST, BUT THE X AND Y WILL LET YOU CHECK THE MODEL'S ARRAY COORDINATES
		leView.draw(laModel.getPlayerColour(),x,y);
		laModel.placeDisc(laModel.getPlayerColour(),x, y);
		if(gameplay){
			//if we are playing a game instead of on load screen
			//laModel.switchPlayer();
			//need to also check validity 
		}
		//laModel.showBoards();
		
	}
	
	
	public void valid(){
		//call the model class, to see if it is valid
		//if it isnt valid then return a string array of the errors so i can dialog box them as a pop up to the user
		//if it IS valid then just return null
		String [] errors = laModel.checkInvalidPoints();
		if (errors.length == 0){
			//start game
			System.out.println("here");
			
		}
		else{ 
			leView.invalid(errors);

		}
	}

	public void exitGame(ActionEvent event){
		System.exit(0);
	}
	

}
