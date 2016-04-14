package application;

import java.util.ArrayList;

public class Board {
	
	public static Point[][] Points;
	private int N; // N Men's Morris 
	public static int length; // Length of array of the board (cells)
	private int layer;
	private int matrixSize;

	
	public Board(int N){ //Create a Board for N Men's Morris
		this.N = N;
		this.length = (2*N)/3 +1;
		this.layer = N/3;
		this.matrixSize = (2 * N/3);
		
		this.Points = new Point [length][length];
		for (int i =0;i<length;i++){
			for (int j =0;j<length;j++){
				Points[i][j] = new Point(i,j);
			}
		}
		placeValidPoints(); // Find all valid points on the board.
		makeConnections(); // Make connections for each points.
	}

	private void placeValidPoints(){
		//The board is made up of a 4x4 grid, for Six Men’s Morris, and can be represented as a 5x5 array, 
		//where all valid points are placed along two central axis and two diagonals except for the central middle point.
		// For each valid point, its validity is set to be true and the for rest of the points, 
		// including the central point, their validities remain unchanged, by default that is false

		for (int i =0;i<this.length;i++){
			for (int j =0;j<this.length;j++){
				if (i==j){ // first diagonal
					Points[i][j].validity = true;
					if (i<N/3){
						Points[i][j].locationFlag = "TopLeft";
					}
					else if (i>N/3){
						Points[i][j].locationFlag = "BtmRight";
					}
				}
				if (i+j == this.matrixSize){ // second diagonal
					Points[i][j].validity = true;
					if (i>N/3){
						Points[i][j].locationFlag = "BtmLeft";
					}
					else if (i<N/3){
						Points[i][j].locationFlag = "TopRight";
				}
				}
				// middle axes
				if ((i == this.matrixSize/2) || (j == this.matrixSize/2)){
					Points[i][j].validity = true;
					if ((i==this.matrixSize/2) && (j < matrixSize/2)){
						Points[i][j].locationFlag = "MdlLeft";
					}
					if ((i==this.matrixSize/2) && (j > matrixSize/2)){
						Points[i][j].locationFlag = "MdlRight";
					}
					if ((i<this.matrixSize/2) && (j == matrixSize/2)){
						Points[i][j].locationFlag = "MdlTop";
					}
					if ((i>this.matrixSize/2) && (j == matrixSize/2)){
						Points[i][j].locationFlag = "MdlBtm";
					}
				}
			}
		}
		Points[this.matrixSize/2][this.matrixSize/2].validity= false;
		// diagonals and axis - central point
		
		
	}

	private void makeConnections() { //Determines the horizontal and vertical connections of the points.
		//For each layer, “TopLeft” is connected to “MdlTop” and "MdlLeft", 
		// "TopRight" is connected to “MdlTop” and "MdlRight", 
		// “BtmLeft” is connected to  "MdlLeft" and "MdlBtm" 
		// while "BtmRight" is connected to "MdlLeft" and "MdlBtm". 
		// Correspondingly, all the midpoints are connected to corner points. 
		// When a midpoint is not in the innermost or outermost layer, 
		// it is always horizontally or vertically connected to another two midpoints. 
		// For any other midpoints that are in the innermost and outermost layers, only another one midpoint is connected.
		for (int i = 0; i < this.length; i++) {
			for (int j = 0; j < this.length; j++) {
				if (Points[i][j].validity) {
					int distanceX,distanceY;
					distanceX = Math.abs(j - N / 3);
					distanceY = Math.abs(i - N / 3);
					

					int layerN = Math.max(distanceX,distanceY); // Find out which layer is
														// this current point on
														// (distance to the
														// axis)

					if (Points[i][j].locationFlag.equals("TopLeft")) { //
						Points[i][j].Hconnections.add(Points[i][j + layerN].coor);
						Points[i][j].Vconnections.add(Points[i + layerN][j].coor);

					} else if (Points[i][j].locationFlag.equals("TopRight")) {
						Points[i][j].Hconnections.add(Points[i][j - layerN].coor);
						Points[i][j].Vconnections.add(Points[i + layerN][j].coor);
					} else if (Points[i][j].locationFlag.equals("BtmLeft")) {
						Points[i][j].Hconnections.add(Points[i][j + layerN].coor);
						Points[i][j].Vconnections.add(Points[i - layerN][j].coor);

					} else if (Points[i][j].locationFlag.equals("BtmRight")) {
						Points[i][j].Hconnections.add(Points[i][j - layerN].coor);
						Points[i][j].Vconnections.add(Points[i - layerN][j].coor);

					} 
					else if (Points[i][j].locationFlag.equals("MdlLeft")) {
						Points[i][j].Vconnections.add(Points[i + layerN][j].coor);
						Points[i][j].Vconnections.add(Points[i - layerN][j].coor);
						if (layerN == 1){ // The inner-most layer
							Points[i][j].Hconnections.add(Points[i][j-1].coor);
						}
						else if (layerN == N/3){ // The outer-most layer
							Points[i][j].Hconnections.add(Points[i][j+1].coor);
						}
						else{
							Points[i][j].Hconnections.add(Points[i][j+1].coor);
							Points[i][j].Hconnections.add(Points[i][j-1].coor);
						}					
					} 
					else if (Points[i][j].locationFlag.equals("MdlRight")) {
						Points[i][j].Vconnections.add(Points[i + layerN][j].coor);
						Points[i][j].Vconnections.add(Points[i - layerN][j].coor);
						if (layerN == 1){ // The inner-most layer
							Points[i][j].Hconnections.add(Points[i][j+1].coor);
						}
						else if (layerN == N/3){ // The outer-most layer
							Points[i][j].Hconnections.add(Points[i][j-1].coor);
						}
						else{
							Points[i][j].Hconnections.add(Points[i][j+1].coor);
							Points[i][j].Hconnections.add(Points[i][j-1].coor);
						}					
					} 
					else if (Points[i][j].locationFlag.equals("MdlTop")) {
						Points[i][j].Hconnections.add(Points[i][j - layerN].coor);
						Points[i][j].Hconnections.add(Points[i][j + layerN].coor);
						if (layerN == 1){ // The inner-most layer
							Points[i][j].Vconnections.add(Points[i-1][j].coor);
						}
						else if (layerN == N/3){ // The outer-most layer
							Points[i][j].Vconnections.add(Points[i+1][j].coor);
						}
						else{
							Points[i][j].Vconnections.add(Points[i-1][j].coor);
							Points[i][j].Vconnections.add(Points[i+1][j].coor);
						}
					} 
					else if (Points[i][j].locationFlag.equals("MdlBtm")) {
						Points[i][j].Hconnections.add(Points[i][j - layerN].coor);
						Points[i][j].Hconnections.add(Points[i][j + layerN].coor);
						if (layerN == 1){ // The inner-most layer
							Points[i][j].Vconnections.add(Points[i+1][j].coor);
						}
						else if (layerN == N/3){ // The outer-most layer
							Points[i][j].Vconnections.add(Points[i-1][j].coor);
						}
						else{
							Points[i][j].Vconnections.add(Points[i-1][j].coor);
							Points[i][j].Vconnections.add(Points[i+1][j].coor);
						}
						
					}
				}
			}
		}
		
		
	}
	
	public void showBoards(){  // Print the whole Board
		for (int i =0;i<length;i++){
			for (int j =0;j<length;j++){
				System.out.print(Points[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
		
		
	}
	// THIS IS SPECIFICALLY DESIGNED FOR SIX MEN'S MORRIS ONLY
	public ArrayList<Point[]> allMills(){
	
		ArrayList<Point[]> allMills = new ArrayList<Point[]>();
		//allMills[0]
		Point[] h0 = new Point[3];
		h0[0] = Points[0][0];
		h0[1] = Points[0][2];
		h0[2] = Points[0][4];
		allMills.add(h0);
		//allMills[1]
		Point[] h1 = new Point[3];
		h1[0] = Points[1][1];
		h1[1] = Points[1][2];
		h1[2] = Points[1][3];
		allMills.add(h1);
		//allMills[2]
		Point[] h2 = new Point[3];
		h2[0] = Points[3][1];
		h2[1] = Points[3][2];
		h2[2] = Points[3][3];
		allMills.add(h2);
		//allMills[3]
		Point[] h3 = new Point[3];
		h3[0] = Points[4][0];
		h3[1] = Points[4][2];
		h3[2] = Points[4][4];
		allMills.add(h3);
		//allMills[4]
		Point[] v4 = new Point[3];
		v4[0] = Points[0][0];
		v4[1] = Points[2][0];
		v4[2] = Points[4][0];
		allMills.add(v4);
		//allMills[5]
		Point[] v5 = new Point[3];
		v5[0] = Points[1][1];
		v5[1] = Points[2][1];
		v5[2] = Points[3][1];
		allMills.add(v5);
		//allMills[6]
		Point[] v6 = new Point[3];
		v6[0] = Points[1][3];
		v6[1] = Points[2][3];
		v6[2] = Points[3][3];
		allMills.add(v6);
		//allMills[7]
		Point[] v7 = new Point[3];
		v7[0] = Points[0][4];
		v7[1] = Points[2][4];
		v7[2] = Points[4][4];
		allMills.add(v7);
		return allMills;
	}
	
	public ArrayList<String> allXcolorDiscs(String color){
		ArrayList<String> allAIpoints = new ArrayList<String>();
		for (int i = 0; i < this.length; i++) {
			for (int j = 0; j < this.length; j++) {
				if ((Points[i][j].validity) && Points[i][j].color.equals(color)) {
					allAIpoints.add((Points[i][j].coor));
				}
			}
		}
				
		return allAIpoints;
	}
	

	 public static void clearBoard(){
		// keep valid points and connections but clear discCount and color
		 
		for (int i =0;i<length;i++){
			for (int j=0;j<length;j++){
				if (Points[i][j].validity){ 
					Points[i][j].discCounter = 0;
					Points[i][j].color = "";
			}
		}
	}		
}
	
	
	
	// Testing
/*	public static void main(String[] args) {
		Board A = new Board(9);
		for (int i = 0;i<A.length;i++){
			for (int j = 0;j<A.length;j++){
				System.out.print(A.Points[i][j]);

				System.out.print(" ");
				}
			System.out.println();
		}		
	}
	*/

}
