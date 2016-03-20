package application;

import java.util.ArrayList;
import java.util.Random;

public class Model {		//this will hold the logic of the game
	
	//these are the variables for the player colour
	private boolean p1red = true;		//default will be red
	private boolean p1blue = false;
	private boolean p1Play;
	public Board currentBoard;
	private int redDiscs = 6; 	//to decrement 
	private int blueDiscs = 6;	//to decrement
	private ArrayList<String> mills = new ArrayList<String>();
	
	
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
		System.out.println("red= " + p1red + "  blue= " + p1blue);
	}
	
	public void getfirstPlayer(){
		Random rand = new Random();
		if(rand.nextInt(2)==0){
			p1Play = true;
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

	
	
	public void placeDisc(String color,int x, int y){
		currentBoard.Points[x][y].discCounter ++;			
		currentBoard.Points[x][y].color = color;
		if (color.equals("Red")){
			redDiscs --;
		}
		else{
			blueDiscs --;
		}
		checkH(x,y);
		checkV(x,y);
	}
	
	public String getPlayerColour(){
		if(p1red){return "Red";}
		else{return "Blue";}
		
	}
	
	public void changePlayerColourBlue(){ //for when they wanna place a different colour disc
			p1red= false;		//default will be red
			p1blue = true;
	}
	
	public void changePlayerColourRed(){ //for when they wanna place a different colour disc
			p1red= true;
			p1blue=false;
	}

	public String[] checkInvalidPoints(){ 
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
		
		//return errors;
		String[] arr = new String[4];
		for(int i = 0;i<4; i++){
			arr[i] = Integer.toString(i);
		}
		
		return arr;
	}
	
	public ArrayList<String> showValidMoves(int x, int y){
		ArrayList<String> validMoves = new ArrayList<String>();
		ArrayList<String> allConnections = new ArrayList<String>();
		allConnections.addAll(currentBoard.Points[x][y].Hconnections);
		allConnections.addAll(currentBoard.Points[x][y].Vconnections);		
		for (String currentPoint :allConnections){
			String[] xy = currentPoint.split(",");
			int currentX = Integer.parseInt(xy[0]);
			int currentY = Integer.parseInt(xy[1]);
			if (currentBoard.Points[currentX][currentY].discCounter == 0)
				validMoves.add(currentPoint);
		}
		return validMoves;
	}
	
	public void resetA(int x, int y){ //Remove a disc from point A
		currentBoard.Points[x][y].discCounter = 0;
		currentBoard.Points[x][y].color = "black";	
	}
	
	public void checkMills(int x, int y){
		while (checkH(x,y) || checkV(x,y)){
			System.out.println("A MILL IS FORMED. REMOVE AN OPPENET'S DISC."); //TODO: CALL VIEW TO DISPLAY INFO. User click on a disc
			// CHECK THAT DISC IS MOVEABLE OR NOT (MUST BE OPPOSITE COLOR + NOT IN A MILL)
			//IF IT CAN BE REMOVED: RESET THE POINT AND DISC NUMBER --;
			//CHECKWIN() ?
			
		}
		
	}
	
	private ArrayList<String> currentHconnections(int x, int y){
		return currentBoard.Points[x][y].Hconnections;
	}
	
	private ArrayList<String> currentVconnections(int x, int y){
		return currentBoard.Points[x][y].Vconnections;
	}
	
	private boolean checkH(int x, int y){
		String color = currentBoard.Points[x][y].color;
		ArrayList<String> allHConnctions = new ArrayList<String>();
		allHConnctions.add(""+x+","+y);
		for (String currentCoor: currentBoard.Points[x][y].Hconnections){
			if (!allHConnctions.contains(currentCoor))
				allHConnctions.add(currentCoor);
			String[] a = currentCoor.split(",");
			int currentX = Integer.parseInt(a[0]);
			int currentY = Integer.parseInt(a[1]);
			for (String coor: currentHconnections(currentX,currentY)){
				if (!allHConnctions.contains(coor))
					allHConnctions.add(coor);
			}
			
		}
		System.out.println();
		System.out.print("Horizontal connections are :" );
		for (String currentCoor: allHConnctions){
			System.out.print("("+currentCoor + ")");
		}
		System.out.println();
		return checkMill(color,allHConnctions);
	}
	
	private boolean checkMill(String color,ArrayList<String> allConnctions){
		int mill = 0;
		for (String currentCoor : allConnctions){
			String[] a = currentCoor.split(",");
			int currentX = Integer.parseInt(a[0]);
			int currentY = Integer.parseInt(a[1]);
			if (currentBoard.Points[currentX][currentY].color == color)
				mill ++;
		}
		if (mill == 3){
			System.out.print("A MILL IS FORMED");
			mills.addAll(allConnctions);
			printMill();
			return true ;
		}
		else return false;
		
	}
	
	private void printMill(){
		System.out.println();
		System.out.print("Points in Mills");
		for (String points: mills){
			System.out.print(points + " ");
		}
		System.out.println();
	}
	
	private boolean checkV(int x, int y){
		String color = currentBoard.Points[x][y].color;
		ArrayList<String> allVConnctions = new ArrayList<String>();
		allVConnctions.add(""+x+","+y);
		for (String currentCoor: currentBoard.Points[x][y].Vconnections){
			if (!allVConnctions.contains(currentCoor))
				allVConnctions.add(currentCoor);
			String[] a = currentCoor.split(",");
			int currentX = Integer.parseInt(a[0]);
			int currentY = Integer.parseInt(a[1]);
			for (String coor: currentVconnections(currentX,currentY)){
				if (!allVConnctions.contains(coor))
					allVConnctions.add(coor);
			}
			
		}
		System.out.println();
		System.out.print("Verical connections are :" );
		for (String currentCoor: allVConnctions){
			System.out.print("("+currentCoor + ")");
		}
		System.out.println();
		return checkMill(color,allVConnctions);
	}
		
	
	
	
	

}
