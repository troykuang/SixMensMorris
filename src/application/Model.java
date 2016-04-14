package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Model {		//this will hold the logic of the game
	
	//these are the variables for the player color
	private boolean p1red = true;		//default will be red
	private boolean p1blue = false;
	private boolean p1Play = false;
	public static Board currentBoard;
	public int redDiscs = 6; 	// The number of red discs that are remained to be placed on the board.
	public int blueDiscs = 6;	// The number of blue discs that are remained to be placed on the board.
	public int totalRed = 6; // The total number of red discs remained on the board
	public int totalBlue = 6; // The total number of blue discs remained on the board
	private ArrayList<String> mills = new ArrayList<String>(); // An ArrayList that holds all the coordinates in the mills 
	public String AIcolor;
	
	
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
	
	public void remove(String color){ // Decrease a red/blue from back-end
		if (color == "Red"){
			totalRed --;
		}
		else totalBlue --;
	}
	
	public void switchColor(){ // Switch players
		if (p1red){
			p1red = false;
			p1blue = true;
		}
		else{
			p1red = true;
			p1blue = false;
		}
	}
	
	public void getfirstPlayer(){ // Get the player after randomly decide the order of play
		Random rand = new Random();
		if(rand.nextInt(2)==0){
			p1Play = true;
			System.out.println("PLAYER 1 WILL PLAY");
			System.out.println(getPlayerColour());
		}
		else {
			System.out.println("Computer plays");
			if(p1red){
				changePlayerColourBlue();
			}
			else{
				changePlayerColourRed();
				
			}
		}
	}
	
	public void createBoard(int N){ // Create a board for N Men's Morris
		this.currentBoard = new Board(N);
	}
	
	public void placeDisc(String color,int x, int y){ // Place a disc in the back-end
		currentBoard.Points[x][y].discCounter ++;			
		currentBoard.Points[x][y].color = color;
		if (color.equals("Red")){ // Decrease the number of discs that r remained to be placed on the board
			redDiscs --;
		}
		else{
			blueDiscs --;
		}
//		checkH(x,y);
//		checkV(x,y);
	}
	
	public String getPlayerColour(){ // Get current payer color
		if(p1red){return "Red";}
		else{return "Blue";}
		
	}
	
	public String getOppositeColour(){ // Get opponent's color
		if(p1red){return "Blue";}
		else{return "Red";}
		
	}
	
	public void changePlayerColourBlue(){ //for when they wanna place a different colour disc
			p1red= false;		//default will be red
			p1blue = true;
	}
	
	public void changePlayerColourRed(){ //for when they wanna place a different colour disc
			p1red= true;
			p1blue=false;
	}

	public ArrayList<String> checkInvalidPoints(){ 
		// In the load mode, when the player wants to manually placed the discs all at once
		// There should be only 6 or less than 6 discs on the board after the placement
		// If any color of discs are greater than 6 an error message is passed to View to display.
		ArrayList<String> errorMsg = new ArrayList<String>(); 
		int red = 0;
		int blue = 0;
		for (int i =0;i<currentBoard.length;i++){
			for (int j=0;j<currentBoard.length;j++){
				if (currentBoard.Points[i][j].validity){ 
					if (currentBoard.Points[i][j].color.equals("Red")){
						red ++;
					}
					else if (currentBoard.Points[i][j].color.equals("Blue")){
						blue ++;
					}
				}
			}
		}
		if ((red > 6)||(blue > 6)){
			errorMsg.add("Please place only 6 for each color of discs.");
		}
		if(errorMsg.size()==0){
			return null;
		}
		else{
			return errorMsg;
		}
	}
	
	public ArrayList<String> showValidMoves(int x, int y){ // Show all the valid next moves for given point (x, y)
		ArrayList<String> validMoves = new ArrayList<String>();
		ArrayList<String> allConnections = new ArrayList<String>();
		allConnections.addAll(currentBoard.Points[x][y].Hconnections);
		allConnections.addAll(currentBoard.Points[x][y].Vconnections);		
		// First, gather all the connections of this point 
		// And go through the list, if the current point is available to place a disc on,
		// then add if to validMoves.
		for (String currentPoint :allConnections){
			String[] xy = currentPoint.split(",");
			int currentX = Integer.parseInt(xy[0]);
			int currentY = Integer.parseInt(xy[1]);
			if (currentBoard.Points[currentX][currentY].discCounter == 0)
				validMoves.add(currentPoint);
		}
		return validMoves;
	}
	
	public void resetA(int x, int y){ //Remove a disc from point A in back-end
		currentBoard.Points[x][y].discCounter = 0;
		currentBoard.Points[x][y].color = "black";	
	}
	
	public boolean inMills(int x, int y){ // To check a point is in mills or not, this is used to remove an opponent disc
		String coor = ""+x+","+y;
		return mills.contains(coor);
	}
	
	public boolean checkMills(int x, int y){ // To check a mill is formed or not.
		return (checkH(x,y) || checkV(x,y));
	}
	
	private ArrayList<String> currentHconnections(int x, int y){ // All horizontal connections of point (x, y)
		return currentBoard.Points[x][y].Hconnections;
	}
	
	private ArrayList<String> currentVconnections(int x, int y){ // All vertical connections of point (x, y)
		return currentBoard.Points[x][y].Vconnections;
	}
	
	private boolean checkH(int x, int y){ // Given a point (x, y), check if it's in a horizontal mill.
		// Find all the points that could be in this horizontal mill and the color of the current point
		// And use checkMill to check if any mill is formed
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
	
	private boolean checkV(int x, int y){ // Given a point (x, y), check if it's in a vertical mill.
		// Find all the points that could be in this vertical mill and the color of the current point
		// And use checkMill to check if any mill is formed
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
	
	private boolean checkMill(String color,ArrayList<String> allConnctions){ // Given a color and a Point array to check if all the Points have the same color
		
		int mill = 0;
		for (String currentCoor : allConnctions){
			String[] a = currentCoor.split(",");
			int currentX = Integer.parseInt(a[0]);
			int currentY = Integer.parseInt(a[1]);
			if (currentBoard.Points[currentX][currentY].color == color) // If the disc on current point has the same color as the input color
				mill ++; // int mill increases 
		}
		if (mill == 3){ // If mill== 3 that means a mill is formed
			System.out.print("A MILL IS FORMED");
			mills.addAll(allConnctions);
			printMill();
			return true ;
		}
		else return false;
		
	}
	
	private void printMill(){ // Print all the point in a mill, this is designed for testing purpose.
		System.out.println();
		System.out.print("Points in Mills");
		for (String points: mills){
			System.out.print(points + " ");
		}
		System.out.println();
	}
	
	public void removeMill(int x, int y){ // Moving a point out of a mill and take all the point associated to this mill from array mills
		System.out.println("MOVING A POINT IN THE MILL");
		mills.remove(""+x+","+y);
		printMill();
		for (String a:currentHconnections(x,y) ){
			if (mills.contains(a))
				mills.remove(a);
		}
		for (String a:currentVconnections(x,y) ){
			if (mills.contains(a))
				mills.remove(a);
			
		}
  		printMill();
		
	}
	
	public void saveGame(int currentState){
		// Save the currentBoard to a txt file
		// By saving all the info of: points, currentStates and current player
		try{
		File save = new File("Saved.txt");
		FileWriter fw = new FileWriter(save);
		String add = getPlayerColour();
		
		fw.write(add+"\n");
		add = Integer.toString(currentState);
		fw.write(add +"\n");
		for(int i = 0; i < currentBoard.length; i++){
			for(int j = 0; j < currentBoard.length; j++){
				if(currentBoard.Points[i][j].validity){
				add = currentBoard.Points[i][j].color + "," + i + "," + j;
				if(i == 4 && j == 4){
					fw.write(add);
					break;
				}
				fw.write(add +"\n"); 
				}
			}
		}

		fw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> loadGame(){
		// Load a board from a txt file
		try{
		ArrayList<String> pieces = new ArrayList<String>();
		
		String[] Split;
		BufferedReader br = new BufferedReader(new FileReader("Saved.txt"));
		String line = br.readLine();
		while(line!= null){
			pieces.add(line);

			line = br.readLine();
			
		}
		String player = pieces.get(0); 
		assignPlayer(player); // first line will be the player		
		// iterate through the array list of string, split at the commas and place pieces based on
		// the colour, and location of the piece.
		for(int i = 2; i < pieces.size(); i++){
			Split = pieces.get(i).split(",");
			String colour = Split[0];
			int row = Integer.parseInt(Split[1]);
			int column = Integer.parseInt(Split[2]);
			if(!colour.equals("black")){
				placeDisc(colour, row, column);	 // replace all the disc from input file
			}
			}
		return pieces;
		}
		catch (Exception e){
			e.printStackTrace();
			System.out.println("Error Loading");
			return null;
		}
		
	}
	
	public ArrayList<String> AInextMoves(){
		ArrayList<String> nextMoves = new ArrayList<String>();
		ArrayList<Point[]> allMills = currentBoard.allMills();
		ArrayList<ArrayList<String>> LineInfo = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < 8; i++) {
			int red = 0;
			int blue = 0;
			ArrayList<String> currentLineInfo = new ArrayList<String>();
			ArrayList<String> availableSpot = new ArrayList<String>();
			for (Point current : allMills.get(i)){
				if (current.color.equals("Red")){
					red ++;
					//System.out.println("red piece at" + current.coor);
					}
				if (current.color.equals("Blue")){
					blue ++;
					//System.out.println("blue piece at" + current.coor);
					}
				if (current.color.equals("black")){
					availableSpot.add(current.coor);
					//System.out.println("The AI can place at " + current.coor);
					}
			}
			currentLineInfo.add(""+red);
			currentLineInfo.add(""+blue);
			currentLineInfo.addAll(availableSpot);
			LineInfo.add(currentLineInfo);
		}
		//A
		//System.out.println(LineInfo.toString());
		for (int i = 0;i < 8;i++){
			ArrayList<String> current = LineInfo.get(i);
			if ((current.get(0).equals("2") ||current.get(1).equals("2")) && (current.size()>2)){
				if (!nextMoves.contains(current.get(2)))
					nextMoves.add(current.get(2));
					//System.out.println("AI will create / break mill");
			}
		}
		if (nextMoves.isEmpty()){
			//B
			for (int i = 0;i < 8;i++){
				ArrayList<String> current = LineInfo.get(i);
				if (AIcolor.equals("Red")){
					if ((current.get(0).equals("1")) && ((current.get(1).equals("0")))){
						if (!nextMoves.contains(current.get(2))){
							nextMoves.add(current.get(2));
							//System.out.println("AI is Red, row/column has a red piece and location 2 is empty");
						}
						if (!nextMoves.contains(current.get(3))){
							nextMoves.add(current.get(3));
							//System.out.println("AI is Red, row/column has a red piece and location 3 is empty");
						}
					}
				}
				else if (AIcolor.equals("Blue")){
					if ((current.get(0).equals("0")) && ((current.get(1).equals("1")))){
						if (!nextMoves.contains(current.get(2))){
							nextMoves.add(current.get(2));
							//System.out.println("AI is Blue, row/column has a blue piece and location 2 is empty");
						}
						if (!nextMoves.contains(current.get(3))){
							nextMoves.add(current.get(3));
							//System.out.println("AI is Blue, row/column has a blue piece and location 3 is empty");
						}
					}
				}
			}
			if (nextMoves.isEmpty()){
				//C
				for (int i = 0;i < 8;i++){
					ArrayList<String> current = LineInfo.get(i);
					if ((current.get(0).equals("0") && current.get(1).equals("0"))){
						if (!nextMoves.contains(current.get(2))){
							nextMoves.add(current.get(2));
							//System.out.println("There are no pieces in the row / column, and location 1 isn't yet stored");
						}
						if (!nextMoves.contains(current.get(3))){
							nextMoves.add(current.get(3));
							//System.out.println("There are no pieces in the row / column, and location 2 isn't yet stored");
						}
						if (!nextMoves.contains(current.get(4))){
							nextMoves.add(current.get(4));
							//System.out.println("There are no pieces in the row / column, and location 3 isn't yet stored");
						}
					}
				}
				if (nextMoves.isEmpty()){
					for (int i = 0;i < 8;i++){
						ArrayList<String> current = LineInfo.get(i);
						if ((current.get(0).equals("1") && current.get(1).equals("1"))){
							if (!nextMoves.contains(current.get(2))){
								nextMoves.add(current.get(2));
								//System.out.println("There are one of each colour in this row and the location isn't yet stored");
							}
						}
					}
					return nextMoves;				
				}
				else return nextMoves;
			}
			else return nextMoves;
		}
		else return nextMoves;
	}
}
