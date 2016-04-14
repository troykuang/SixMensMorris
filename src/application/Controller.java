package application;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.fxml.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

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
	private String AIcolor;
	
	static Model laModel = new Model();	//instantiate the model in order to store the placements 
	static View leView = new View();		//view object in order to change the view
	
	private static int gameTile;
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
	
	public void getGameplay(ActionEvent event){
		leView.getGameplay();
	}

	public static void startNewGame(boolean b){	//if the person clicks new game on the titleScreen
		int N = 6;
		gameTile = ((N/3)*2)+1;
		laModel.getfirstPlayer();	//to randomize the first player
		System.out.println("boolean: " + b);
		laModel.createBoard(N);		//creating a fresh board with no placed discs
		leView.newGame(gameTile); //# of mens morris
		if(b){
			//ai play
			
			int a = randomInteger(0,100);
			System.out.println("THE RANDOM NUMBER IS: "+a);
			if(a <= 50){
				currentState = 71;			//setting the state (alternates players)
				laModel.AIcolor = laModel.getPlayerColour();
				inputClick(0,0);	
			}
			else{
				laModel.AIcolor = laModel.getOppositeColour();
				currentState = 7; 
			}
		}
		else {
			currentState = 1;	
		}
		System.out.println("STARTING STATE IS: "+ currentState);
		
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
		
		
		boolean leaveSwitch = false;
		while (leaveSwitch == false){
			
			System.out.println("I'M IN STATE:"+currentState);
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
						leaveSwitch = true;
						break;
					}
				}
				else{ // click on a point where is invalid to place the disc
					currentState =1;				
					checkWins();					//this function keeps checking the game progression
					leaveSwitch = true;
					break;
				}
				if ((laModel.redDiscs == 0) && (laModel.blueDiscs == 0)){ // If both player have already placed 6 discs, currentState changes to 2
					laModel.switchColor();	
					leView.playerChange();//this is changing colours
					currentState = 2;
					checkWins();
					leaveSwitch = true;
					break;
				}
				else{ // Otherwise, players alternate and stay in currentState 1.
					laModel.switchColor();
					leView.playerChange();
					currentState = 1;
					checkWins();
					leaveSwitch = true;
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
					leaveSwitch = true;
					break;
				}
				else{
					currentState = 2;
					checkWins();
					leaveSwitch = true;
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
						leaveSwitch = true;
						break;
					} //  If not, currentState changes to 2.
					currentState = 2;
					checkWins();
					laModel.switchColor();
					leView.playerChange();
					leaveSwitch = true;
					break;
				}
				else{ // If B is not in valid next moves, currentState changes back to 2, where user can find valid next moves again.
					currentState =2 ;
					checkWins();
					leaveSwitch = true;
					break;
				}
			case 4: // A mill is formed, remove an opponent's disc.
				// If the disc the user clicks on is in opposite color and it's not in a mill, then that disc is removed. 
				// If the players haven't finished placing their discs, currentState goes back to 1, otherwise it changes to 2 where players move their discs in turn
				// If the player click on his own disc/ a point with disc, currenState remains in 4.
				String colorO = laModel.getOppositeColour();
				int oppoDiscs = 0;
				if (colorO == "Red"){
					oppoDiscs = laModel.totalRed;
				}
				else if (colorO == "Blue"){
					oppoDiscs = laModel.totalBlue;
				}
				if ((laModel.currentBoard.Points[y][x].color == colorO) && (oppoDiscs == 3)){ //VERY LAST 3 PIECES
					laModel.resetA(y,x);
					leView.undrawDisc(x,y);
					laModel.remove(colorO);
					checkWins();
					leaveSwitch = true;
					break;			
				}
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
						leaveSwitch = true;
						break;
					}
					else{ // otherwise it changes to 2 where players move their discs in turn
						currentState = 2;
						checkWins();
						leaveSwitch = true;
						break;			
					}	
				}
				else{ // If the player click on his own disc/ a point without disc, currenState remains in 4.
					currentState = 4;
					checkWins();
					leaveSwitch = true;
					break;
				}
			case 5: //the load game where pieces can be loaded out of turn
				if (laModel.currentBoard.Points[y][x].discCounter ==0){
					System.out.println("redDics"+ laModel.redDiscs + "blueDics"+laModel.blueDiscs);
					leView.draw(laModel.getPlayerColour(),x,y);
					laModel.placeDisc(laModel.getPlayerColour(),y, x);
				}
			case 6: // A trapping state. The end of the game no more click actions
				currentState = 6;
				leaveSwitch = true;
				break;
			 
			case 7: // User places discs
				// If a mill is formed during this procedure, currentState changes to 4
				// If both player have already placed 6 discs, currentState changes to 2
				// Otherwise, players alternate and stay in currentState 1.
				if (laModel.currentBoard.Points[y][x].discCounter ==0){
					leView.draw(laModel.getPlayerColour(),x,y);			//drawing it on the board
					laModel.placeDisc(laModel.getPlayerColour(),y, x);	//logically placing it in the model as well
					if (laModel.checkMills(y,x)){						//check if there is a mill
						currentState = 10;					// TODO: change the state if there is 
						checkWins();						//to update the progress graphically and in the logic
						leaveSwitch = true;
						laModel.currentBoard.showBoards();
						break;
					}
				}
				else{ // click on a point where is invalid to place the disc, so it gets back
					currentState = 7;	
					leaveSwitch = true;
					laModel.currentBoard.showBoards();
					break;
				}
				if ((laModel.redDiscs <= 0) && (laModel.blueDiscs <= 0)){ // If both player have already placed 6 discs, currentState changes to 2
					laModel.switchColor();	
					leView.playerChange();//this is changing colours
					currentState = 91; // AI moves a disc
					checkWins();
					leaveSwitch = false;
					laModel.currentBoard.showBoards();
					break;
				}
				else{ // Otherwise, players alternate and stay in currentState 1.
					laModel.switchColor();
					leView.playerChange();
					currentState = 71;
					checkWins();
					leaveSwitch = false;
					laModel.currentBoard.showBoards();
					break;
				}
			case 71: // AI places discs
				ArrayList<String> AInextMoves = laModel.AInextMoves();
				int length = AInextMoves.size();
				int chosen = randomInteger(0,length-1);
				String[] a = AInextMoves.get(chosen).split(",");
				int coorX = Integer.parseInt(a[0]);
				int coorY = Integer.parseInt(a[1]);
				leView.draw(laModel.getPlayerColour(),coorY,coorX);			//drawing it on the board
				laModel.placeDisc(laModel.getPlayerColour(),coorX, coorY);	//logically placing it in the model as well
				if (laModel.checkMills(coorX,coorY)){						//check if there is a mill
					currentState = 101;					
					checkWins();						//to update the progress graphically and in the logic
					leaveSwitch = false;
					laModel.currentBoard.showBoards();
					break;
				}
				
				if ((laModel.redDiscs <= 0) && (laModel.blueDiscs <= 0)){ // If both player have already placed 6 discs, currentState changes to 8
					laModel.switchColor();	
					leView.playerChange();//this is changing colours
					currentState = 8; // Player places a disc
					checkWins();
					leaveSwitch = true;
					laModel.currentBoard.showBoards();
					break;
				}
				else{ // Otherwise, players alternate and stay in currentState 7 (player's turn to place).
					laModel.switchColor();
					leView.playerChange();
					currentState = 7;
					checkWins();
					leaveSwitch = true;
					laModel.currentBoard.showBoards();
					break;
			}
			case 8: // Show valid next moves
				// If the player clicks on his own discs, the program shows valid next moves using dashed circles.
				// 		And it saves all valid next moves to ArrayList nextMoves for later use.
				//		Also, in currentState 2, current x,y is saved to nowX, nowY. So in currentState 3 we'll know which disc to move
				// Otherwise, currentStates remains in 2.
				while (dashedCircles!=0){ // Clean up all the dashed circles from the last click.
					leView.removeDashed();
					dashedCircles --;
				}
				String colorN = laModel.getPlayerColour();
				if (laModel.currentBoard.Points[y][x].color == colorN){
					// If the player clicks on his own discs, the program show valid next moves using dashed circles.
					ArrayList<String> nextMoves = laModel.showValidMoves(y, x);
					System.out.println("Valid next moves are:");
					for (String b : nextMoves){
						System.out.print("("+b+")");
					}
					System.out.println();
					drawValidNextMoves(y,x);
					currentState = 9; // PLAYER MOVE HIS OWN DISCS
					checkWins();
					// Also, in currentState 2, current x,y is saved to nowX, nowY. So in currentState 3 we'll know which disc to move
					nowX = y;
					nowY = x;
					leaveSwitch = true;
					laModel.currentBoard.showBoards();
					break;
				}
				else{
					currentState = 8;
					checkWins();
					leaveSwitch = true;
					laModel.currentBoard.showBoards();
					break;
				}
			case 9: // Move a disc from point A to point B
				// Check point B is a valid point for the disc on A to move to. If it is valid, move A to B.
				// And check if a mill is formed due to this move, if so, currentState changes to 4. If not, currentState changes to 2.
				// If B is not in valid next moves, currentState changes back to 2, where user can find valid next moves again.
				while (dashedCircles!=0){ // Clean up all the dashed circles from the last click.
					leView.removeDashed();
					dashedCircles --;
				}

				String clickN = ""+y+","+x;
				if (nextMoves.contains(clickN)){ 
					System.out.println("moving"+nowX+","+nowY+"to "+y+","+x);
					moveA2B(nowX,nowY,y,x); 
					if (laModel.checkMills(y,x)){ 	// And check if a mill is formed due to this move, if so, currentState changes to 10.
						currentState = 10; 
						checkWins();
						leaveSwitch = true;
						laModel.currentBoard.showBoards();
						break;
					} //  If not, currentState --> AI MOVES DISCS
					currentState = 91;
					checkWins();
					laModel.switchColor();
					leView.playerChange();
					leaveSwitch = false;
					laModel.currentBoard.showBoards();
					break;
				}
				else{ // If B is not in valid next moves, currentState changes back to 2, where user can find valid next moves again.
					currentState = 8 ;
					checkWins();
					leaveSwitch = true;
					laModel.currentBoard.showBoards();
					break;
				}
			case 91:
				System.out.println("AI MOVES HIS OWN PIECES");
				ArrayList<String> allAIdiscs = laModel.currentBoard.allXcolorDiscs(laModel.AIcolor); 
				ArrayList<String> currentValidNextMoves = new ArrayList<String>();
				int chosenPoint, currentX = 0, currentY=0;
				int nextMoves = 0;
				
				ArrayList<String> allPossibleNextMoves = new ArrayList<String>();
				for (String currentCoor : allAIdiscs){
					String[] c = currentCoor.split(",");
					currentX = Integer.parseInt(c[0]);
					currentY = Integer.parseInt(c[1]);
					allPossibleNextMoves.addAll(laModel.showValidMoves(currentX, currentY));
				}
				if (allPossibleNextMoves.isEmpty()){
					currentState = 11;
					leaveSwitch = false;
					checkWins("a");
					break;			
				}
				else{
					while (nextMoves == 0){ // HAVE TO FIND A WAY TO FIND ALL THE POSSIBLE MOVES AND IF THAT LIST IS EMPTY THEN GAME DRAWN

						chosenPoint = randomInteger(0,allAIdiscs.size()-1);
						String[] c = allAIdiscs.get(chosenPoint).split(",");
						currentX = Integer.parseInt(c[0]);
						currentY = Integer.parseInt(c[1]);
						currentValidNextMoves = laModel.showValidMoves(currentX, currentY);
						nextMoves = currentValidNextMoves.size();
					}
					// Chosen point currentX, currentY
					int desIndex = randomInteger(0,currentValidNextMoves.size()-1);
					String desCoor = currentValidNextMoves.get(desIndex);
					String[] d = desCoor.split(",");
					int nextX = Integer.parseInt(d[0]);
					int nextY = Integer.parseInt(d[1]);
					System.out.println("AI IS moving"+currentX+","+currentY+"to "+nextX+","+nextY);
					moveA2B(currentX,currentY,nextX,nextY); 
					if (laModel.checkMills(nextX,nextY)){ 	// And check if a mill is formed due to this move, if so, currentState changes to 10.
						currentState = 101; 
						checkWins();
						leaveSwitch = false;
						laModel.currentBoard.showBoards();
						break;
					} //  If not, currentState --> AI MOVES DISCS
					currentState = 8;
					checkWins();
					laModel.switchColor();
					leView.playerChange();
					leaveSwitch = true;
					laModel.currentBoard.showBoards();
					break;				
				}
			case 10: // A mill is formed, remove an opponent's disc.
				// If the disc the user clicks on is in opposite color and it's not in a mill, then that disc is removed. 
				// If the players haven't finished placing their discs, currentState goes back to 1, otherwise it changes to 2 where players move their discs in turn
				// If the player click on his own disc/ a point with disc, currenState remains in 4.
				String colorOAI = laModel.getOppositeColour();
				int oppoDiscsAI = 0;
				if (colorOAI == "Red"){
					oppoDiscsAI = laModel.totalRed;
				}
				else if (colorOAI == "Blue"){
					oppoDiscsAI = laModel.totalBlue;
				}
				if ((laModel.currentBoard.Points[y][x].color == colorOAI) && (oppoDiscsAI == 3)){
					laModel.resetA(y,x);
					leView.undrawDisc(x,y);
					laModel.remove(colorOAI);
					checkWins();
					leaveSwitch = true;
					laModel.currentBoard.showBoards();
					break;			
				}
				if ((laModel.currentBoard.Points[y][x].color == colorOAI) && (!laModel.inMills(y,x))){
					//// If the disc the user clicks on is in opposite color and it's not in a mill, then that disc is removed. 
					laModel.resetA(y,x);
					leView.undrawDisc(x,y);
					laModel.remove(colorOAI);
					laModel.switchColor();
					leView.playerChange();//this is changed
					if ((laModel.redDiscs > 0) || (laModel.blueDiscs  >0)){
						// If the players haven't finished placing their discs, currentState goes back to 1,
						currentState = 71;
						checkWins();
						leaveSwitch = false;
						laModel.currentBoard.showBoards();
						break;
					}
					else{ // otherwise it changes to 2 where players move their discs in turn
						currentState = 91;
						checkWins();
						leaveSwitch = false;
						laModel.currentBoard.showBoards();
						break;
					}	
				}
				else{ // If the player click on his own disc/ a point with disc, currenState remains in 4.
					currentState = 10;
					checkWins();
					leaveSwitch = true;
					laModel.currentBoard.showBoards();
					break;
				}
			case 101:
				String playerColor = laModel.getOppositeColour();
				ArrayList<String> allPlayerDiscs = laModel.currentBoard.allXcolorDiscs(playerColor);
				ArrayList<String> canBeRemoved = new ArrayList<String>();
				if (allPlayerDiscs.size() == 3){
					String chosenDisc =  allPlayerDiscs.get(randomInteger(0, 2));
					String[] chosenDiscL = chosenDisc.split(",");
					int chosenDiscX = Integer.parseInt(chosenDiscL[0]);
					int chosenDiscY = Integer.parseInt(chosenDiscL[1]);
					laModel.resetA(chosenDiscX,chosenDiscY);
					leView.undrawDisc(chosenDiscY,chosenDiscX);
					laModel.remove(playerColor);
					checkWins();
					leaveSwitch = true;
					laModel.currentBoard.showBoards();
					break;			
				}
				else{
					for (String currentPlayerCoor : allPlayerDiscs){
						String[] e = currentPlayerCoor.split(",");
						int currentPlayerCoorX = Integer.parseInt(e[0]);
						int currentPlayerCoorY = Integer.parseInt(e[1]);
						if (!laModel.inMills(currentPlayerCoorX, currentPlayerCoorY)){
							canBeRemoved.add(currentPlayerCoor);
						}
					}
					String chosenDisc =  allPlayerDiscs.get(randomInteger(0,canBeRemoved.size()-1));
					String[] chosenDiscL = chosenDisc.split(",");
					int chosenDiscX = Integer.parseInt(chosenDiscL[0]);
					int chosenDiscY = Integer.parseInt(chosenDiscL[1]);
					laModel.resetA(chosenDiscX,chosenDiscY);
					leView.undrawDisc(chosenDiscY,chosenDiscX);
					laModel.remove(playerColor);
					laModel.switchColor();
					leView.playerChange();//this is changed
					if ((laModel.redDiscs > 0) || (laModel.blueDiscs  >0)){
						// If the players haven't finished placing their discs, currentState goes back to 1,
						currentState = 7;
						checkWins();
						leaveSwitch = true;
						laModel.currentBoard.showBoards();
						break;
					}
					else{ // otherwise it changes to 2 where players move their discs in turn
						currentState = 9;
						checkWins();
						leaveSwitch = true;
						laModel.currentBoard.showBoards();
						break;
					}
			
				}			
			case 11:
				System.out.println("GAME DRAWN");
				currentState = 11;
				leaveSwitch = true;
				break;
			}	
		
			
		}
		
			
	}
	
	public static int randomInteger(int min, int max) {
	    Random rand = new Random();
	    // nextInt excludes the top value so we have to add 1 to include the top value
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
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
		laModel.currentBoard.clearBoard();
	//	laModel.createBoard(N);
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
	
	public static void checkWins(String a){
		//System.out.println("Game Drawn");
		leView.changeState(4);
		// leView
	}
	
	public static void checkWins(){
		if (laModel.totalBlue == 2){
			//System.out.println("RED WINS");
			// view to display "red wins"
			leView.changeState(2);		//6 = red wins
			currentState = 6;
		}
		else if (laModel.totalRed == 2){
			//System.out.println("BLUE WINS");
			//  view to display "BLUE wins"
			leView.changeState(3);		//6 = red wins
			currentState = 6;
			
		}
		else{
			//System.out.println("GAME IN PROGRESS");
			//  view to display "GAME IN PROGRESS"
			leView.changeState(1);		//6 = red wins
		}
	}
	
	

}
