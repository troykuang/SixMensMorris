package application;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.fxml.*;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.*;
import javafx.scene.effect.*;
import javafx.scene.control.Button;

public class Controller {
	public static int N; // N Men's Morris
	public static int dashedCircles = 0;	//counter for the dashed circles indicating possible disc movements
	public static int currentState;			//current state 
	public static ArrayList<String> nextMoves;	//to show next moves, the dashed circles will highlight
	public static int nowX;	
	public static int nowY;
	
	static Model laModel = new Model();	//instantiate the model in order to store the placements 
	static View leView = new View();		//view object in order to change the view
	
	private int gameTile;
	@FXML 
	private String colourChoice;			//when the person picks what colour they want to be (for new game mode)
	@FXML 
	private Label titleScreenLabel = new Label("Red");	//displaying on the titlescreen what colour the player wants to be


	public void getPlayerColour(ActionEvent event){
		colourChoice = ((Button)event.getSource()).getText();	//will give us either "Red" or "Blue" BASED ON THE HOMESCREEN BUTTONS ON THE MAIN MENU
		titleScreenLabel.setText(((Button)event.getSource()).getText());
		laModel.assignPlayer(colourChoice);									//THIS IS SO THE MODEL KNOWS WITH BOOLEANS WHICH PLAYER IS WHICH COLOUR
		
	}
	
	public void changeDiscColourBlue(){				//changes the disc colour being placed to blue and the title screen label to be corresponding 
			laModel.changePlayerColourBlue();	
			//System.out.println("change to blue");
			titleScreenLabel.setText("Blue");
	}
	
	public void changeDiscColourRed(){		//changes the disc colour being placed to RED and the title screen label to be corresponding 
			laModel.changePlayerColourRed();
			//System.out.println("change to red");
			titleScreenLabel.setText("Red");
	}
	
	public static String currentPlayer(){		//to return the current player from the Model (so it can be sent to the view)
		return laModel.getPlayerColour();
	}

	public void startNewGame(ActionEvent event){	//if the person clicks new game on the titleScreen
		int N = 6;
		gameTile = ((N/3)*2)+1;
		laModel.getfirstPlayer();	//to ranomize the first player
		leView.newGame(gameTile); //# of mens morris
		laModel.createBoard(N);		//creating a fresh board with no placed discs
		//laModel.currentBoard.showBoards();	
		currentState =1;			//setting the state (alternates players)

	}
	
	public void play(){			//for when the board has been manually loaded or if we are loading from file
		int N = 6;
		gameTile = ((N/3)*2)+1;
		leView.newGame(gameTile);		//we start a new game, empty board VISUALLY but the model doesnt have an empty board
		//currentState = 1;
		loadSaved();					//populating the board with the pieces from the Model
		leView.changePlaying();			//we want to get the player instead of randomizing the first player
		
	}
	
	public void load(ActionEvent event){		//if they choose to load the Game from the titleScreen
		int N = 6;
		gameTile = ((6/3)*2)+1;
		leView.loadGame(gameTile,laModel.getPlayerColour());	
		laModel.createBoard(N);					//empty board to manually populate
		//laModel.currentBoard.showBoards();
		laModel.getfirstPlayer();
		currentState = 5;					//state that allows for the placements of discs as user decides 
	}
	
	public void loadSaved(){				//to load from file
		if(laModel.loadGame() == null){		//if it is null, that means there is no text file with a saved game
			leView.noGame();				//dialog box to tell the user there is no file
		}
		else{
			loadFile(laModel.loadGame());	//if one exists, we want to load it 
			
		}
	}

	public void saveGame(){		//to save the game and the current state of it to a save file
		System.out.println("saveGame");
		laModel.saveGame(currentState);		//the save is all done in the Model class
		
	}
	
	public void resetLoad(){		//resetting the logic in the model as well as the board graphically
		changeDiscColourRed();		//going back to red (which is the default)
		laModel.createBoard(N);		//resetting the model logic, no discs placed
		
		leView.resetLoad();			//resetting the graphical board as well
	}

	public static void inputClick(int x, int y){ //when a circle mouse click listener is clicked it is redirected here
		//System.out.println("mouse clicked at circle: "+y+" "+x);
		//System.out.println("currentState is "+ currentState);
		//laModel.currentBoard.showBoards();
		switch (currentState){
		case 1: // Two users put their discs in turn 	
			// If a mill is formed during this procedure, currentState changes to 4
			// If both player have already placed 6 discs, currentState changes to 2
			// Otherwise, players alternate and stay in currentState 1.
			if (laModel.currentBoard.Points[y][x].discCounter ==0){
				//System.out.println("redDics"+ laModel.redDiscs + "blueDics"+laModel.blueDiscs);
				leView.draw(laModel.getPlayerColour(),x,y);			//drawing it on the board
				laModel.placeDisc(laModel.getPlayerColour(),y, x);	//logically placing it in the model as well
				if (laModel.checkMills(y,x)){						//check if there is a mill
					currentState = 4;					//change the state if there is 
					checkWins();						//to update the progress graphically and in the logic
					break;
				}
			}
			else{ // click on a point where is invalid to place the disc
				currentState =1;				
				checkWins();					//this function keeps checking the game progression
				break;
			}
			if ((laModel.redDiscs == 0) && (laModel.blueDiscs == 0)){ // If both player have already placed 6 discs, currentState changes to 2
				laModel.switchColor();	
				leView.playerChange();//this is changing colours
				currentState = 2;
				checkWins();
				break;
			}
			else{ // Otherwise, players alternate and stay in currentState 1.
				laModel.switchColor();
				leView.playerChange();
				currentState = 1;
				checkWins();
				break;
			}
		case 2: // Show valid next moves
			// If the player clicks on his own discs, the program shows valid next moves using dashed circles.
			// 		And it saves all valid next moves to ArrayList nextMoves for later use.
			//		Also, in currentState 2, current x,y is saved to nowX, nowY. So in currentState 3 we'll know which disc to move
			// Otherwise, currentStates remains in 2.
			while (dashedCircles!=0){ // Clean up all the dashed circles from the last click.
				leView.removeDashed();
				dashedCircles --;
			}
			String color = laModel.getPlayerColour();
			if (laModel.currentBoard.Points[y][x].color == color){
				// If the player clicks on his own discs, the program show valid next moves using dashed circles.
				ArrayList<String> nextMoves = laModel.showValidMoves(y, x);
				System.out.println("Valid next moves are:");
				for (String a : nextMoves){
					System.out.print("("+a+")");
				}
				System.out.println();
				drawValidNextMoves(y,x);
				currentState = 3;
				checkWins();
				// Also, in currentState 2, current x,y is saved to nowX, nowY. So in currentState 3 we'll know which disc to move
				nowX = y;
				nowY = x;
				break;
			}
			else{
				currentState = 2;
				checkWins();
				break;
			}
		case 3: // Move a disc from point A to point B
			// Check point B is a valid point for the disc on A to move to. If it is valid, move A to B.
			// And check if a mill is formed due to this move, if so, currentState changes to 4. If not, currentState changes to 2.
			// If B is not in valid next moves, currentState changes back to 2, where user can find valid next moves again.
			while (dashedCircles!=0){ // Clean up all the dashed circles from the last click.
				leView.removeDashed();
				dashedCircles --;
			}

			String click = ""+y+","+x;
			if (nextMoves.contains(click)){ 
				System.out.println("moving"+nowX+","+nowY+"to "+y+","+x);
				moveA2B(nowX,nowY,y,x); 
				if (laModel.checkMills(y,x)){ 	// And check if a mill is formed due to this move, if so, currentState changes to 4.
					currentState = 4; 
					checkWins();
					break;
				} //  If not, currentState changes to 2.
				currentState = 2;
				checkWins();
				laModel.switchColor();
				leView.playerChange();
				break;
			}
			else{ // If B is not in valid next moves, currentState changes back to 2, where user can find valid next moves again.
				currentState =2 ;
				checkWins();
				break;
			}
		case 4: // A mill is formed, remove an opponent's disc.
			// If the disc the user clicks on is in opposite color and it's not in a mill, then that disc is removed. 
			// If the players haven't finished placing their discs, currentState goes back to 1, otherwise it changes to 2 where players move their discs in turn
			// If the player click on his own disc/ a point with disc, currenState remains in 4.
			String colorO = laModel.getOppositeColour();
			if ((laModel.currentBoard.Points[y][x].color == colorO) && (!laModel.inMills(y,x))){
				//// If the disc the user clicks on is in opposite color and it's not in a mill, then that disc is removed. 
				laModel.resetA(y,x);
				leView.undrawDisc(x,y);
				laModel.remove(colorO);
				laModel.switchColor();
				leView.playerChange();//this is changed
				if ((laModel.redDiscs > 0) || (laModel.blueDiscs  >0)){
					// If the players haven't finished placing their discs, currentState goes back to 1,
					currentState = 1;
					checkWins();
					break;
				}
				else{ // otherwise it changes to 2 where players move their discs in turn
					currentState = 2;
					checkWins();
					break;			
				}	
			}
			else{ // If the player click on his own disc/ a point with disc, currenState remains in 4.
				currentState = 4;
				checkWins();
				break;
			}
		case 5: //the load game where pieces can be loaded out of turn
			if (laModel.currentBoard.Points[y][x].discCounter ==0){
				System.out.println("redDics"+ laModel.redDiscs + "blueDics"+laModel.blueDiscs);
				leView.draw(laModel.getPlayerColour(),x,y);
				laModel.placeDisc(laModel.getPlayerColour(),y, x);
			}
		}
	}
	
	public void loadFile(ArrayList<String> x){	//the arraylist is of all the points that need to be put on to the board graphically
		leView.resetLoad();						//first reset it
		currentState = Integer.parseInt(x.get(1));	//the first thing in the arrayList is the state
		//System.out.println("New currentState assigned: "+ currentState);
		for(int i = 2; i< x.size(); i++){			//to go through the points and draw everything
			String [] y = x.get(i).split(",");
			if(y[0].equals("black")){				//dont draw the black ones because there isnt a disc there
				laModel.currentBoard.Points[Integer.parseInt(y[1])][Integer.parseInt(y[2])].discCounter = 0;	//reset the counter
			}
			else{
				leView.draw(y[0],Integer.parseInt(y[2]),Integer.parseInt(y[1]));	//we need to draw it so the user can see the discs
		//		laModel.placeDisc(y[0], Integer.parseInt(y[2]), Integer.parseInt(y[1]));
			}
			
		}
		
	}
	
	public static void drawValidNextMoves(int x,int y){	//to highlight the valid moves with dashed circles
		nextMoves = laModel.showValidMoves(x, y);		
		for (String current : nextMoves){	//go through the arrayList drawing all the dashed Circles
			String[] a = current.split(",");
			int currentX = Integer.parseInt(a[1]);		
			int currentY = Integer.parseInt(a[0]);
			leView.drawDashed(currentX, currentY);		//the view drawing the dashed circles
			dashedCircles ++;		//counting how many dashed circles on the board, this is designed for removing the dashed circles.
			
		}
		
		
	}
	
	public static void moveA2B (int x1, int y1, int x2, int y2){ // Move disc from point A to point B and reset point A
		ArrayList<String> validMoves = laModel.showValidMoves(x1,y1); // All the valid next moves for point A are saved in ArrayList validMoves.
		String coorB = ""+x2+","+y2;
		System.out.println("moving"+x1+","+y1+"to "+x2+","+y2);
		if (validMoves.contains(coorB)){ // If B is a valid point for disc on A to move to.
			laModel.resetA(x1,y1); // Clean all the info for point A (discCounter set back to 0 and color set to "black")
			leView.undrawDisc(y1,x1); // Remove disc from view
			leView.draw(laModel.getPlayerColour(),y2,x2); // Place disc in the front-end
			laModel.placeDisc(laModel.getPlayerColour(),x2, y2); // Place disc in the back-end
			
			if (laModel.inMills(x1, y1)){ // If the disc is in a mill at point A, remove the whole mill from ArrayList mill.
				laModel.removeMill(x1,y1);
			}
		}
	}
	
	public void exitGame(ActionEvent event){		//exits and terminates the open windows
		System.exit(0);
	}
	
	public void exitToMain(){		//exits to the titleScreen and resets
		leView.resetLoad();
		laModel.createBoard(N);
		leView.exit();
		
	}
	
	public void valid(){
		System.out.println("controller CLASS");
		//call the model class, to see if it is valid
		//if it isnt valid then return a string array of the errors so i can dialog box them as a pop up to the user
		//if it IS valid then just return null
		if(laModel.checkInvalidPoints() != null){
			//this means there are invalid points
			//we want to reset load now
			System.out.println("THERE ARE INVALID PTS");
			leView.invalid(laModel.checkInvalidPoints());
			leView.resetLoad();
		}
		else {
			//there arent any invalid points
			saveGame();
			play();	
		}
	}
	
	public static void checkWins(){
		if (laModel.totalBlue == 2){
			//System.out.println("RED WINS");
			// view to display "red wins"
			leView.changeState(2);		//6 = red wins
		}
		else if (laModel.totalRed == 2){
			//System.out.println("BLUE WINS");
			//  view to display "BLUE wins"
			leView.changeState(3);		//6 = red wins
		}
		else{
			//System.out.println("GAME IN PROGRESS");
			//  view to display "GAME IN PROGRESS"
			leView.changeState(1);		//6 = red wins
		}
	}
	

}
