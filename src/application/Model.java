package application;

import java.util.ArrayList;
import java.util.Random;
/**
 * Contains the logic for the game. Uses the Point and Board classes 
 * to store game variables and states and check logic of game play actions.
 * @author Natasha DeCoste, Rebecca Tran, Chaoyi Kuang
 *
 */
public class Model {		//this will hold the logic of the game
	
	//these are the variables for the player colour
	private boolean p1red = true;		//default will be red
	private boolean p1blue = false;
	
	private boolean p1Play = false;		//will tell us who is playing 
	private Board currentBoard;
	private int redDiscs = 6; 	//to decrement 
	private int blueDiscs = 6;	//to decrement
	
	
	public void assignPlayer(String colourChoice){
		if(colourChoice.equals("Red")){
			//we want to assign the player to the color red
			p1red= true;
			p1blue=false;
			
		}
		else{
			//we want to assign the player to the value blue
			p1blue= true;
			p1red=false;
		}
		//System.out.println("red= " + p1red + "  blue= " + p1blue);
	}
	
	public void getfirstPlayer(){
		Random rand = new Random();
		int test = rand.nextInt(10);
		//System.out.println("random = "+test);
		if(test<5){
			p1Play = true;		//else it stays as false
		}
	}
	
	public String getDiscs(String colour){
		if(colour.equals("Red")){
			return Integer.toString(4);
		}
		else{
			return Integer.toString(blueDiscs);
		}
		
	}
		
	public void createBoard(int N){
		this.currentBoard = new Board(N);

	}
	
	
	//this is for the installation of the reset button 
/*	public void reset(){
		this.currentBoard.clearBoard();
	}
	*/
	public String[] invalidPoints(){ 
		// Check for each point on the board there's only one disc
		ArrayList<String> errorPoints = new ArrayList<String>(); 
		for (int i =0;i<currentBoard.length;i++){
			for (int j=0;j<currentBoard.length;j++){
				if (currentBoard.Points[i][j].validity){ 
					if (currentBoard.Points[i][j].discCounter>1){
	
						errorPoints.add(currentBoard.Points[i][j].coor);
					}
				}
			}
		}
		String[] errors = new String[errorPoints.size()];
		for(int i=0;i<errorPoints.size();i++){
			errors[i] = "Error " + errorPoints.get(i);
		}
		
		return errors;
	}


	
	private void showBoards(){
		for (int i = 0;i<currentBoard.length;i++){
			for (int j = 0;j<currentBoard.length;j++){
				System.out.print(currentBoard.Points[i][j]);

				System.out.print(" ");
				}
			System.out.println();
		}		
	}
	
	public void placeDisc(String color,int x, int y){
		currentBoard.Points[x][y].discCounter ++;			
		currentBoard.Points[x][y].color = color;
		if (color.equals("Red")){
			redDiscs --;
		}
		else{
			blueDiscs --;
		}
	}
	

	
	public String getPlayerColour(){
		if(p1Play){ 	//if it is player 1's turn
			if(p1red){
					//if they are red
				return "Red";
				}
			else{
				return "Blue";
			}
		}
		else{		//if it is player 2's turn
			if(p1red){		//then player 2 is blue
				return "Blue";	//means player 2 is playing and player one is red so p2 is blue
			}
			else{
				return "Red";
			}
		
		}
	}	
	
	
	public void switchPlayer(){
		//for when they are in gameplay mode
		if(p1Play){
			p1Play = false;
		}
		else {p1Play = true;}
	}
	
	public void changePlayerColourBlue(){ //for when they wanna place a different colour disc
			p1red= false;		//default will be red
			p1blue = true;
			
	}
	
	public void changePlayerColourRed(){ //for when they wanna place a different colour disc
			p1red= true;
			p1blue=false;
			
	}


	
	
	
	

}
