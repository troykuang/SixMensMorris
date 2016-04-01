package application;

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
	
	/* THIS PART IS FOR RESET BUTTON AND PROB SHOULD BE MOVED TO CONTROLLER LATER
	 public static void clearBoard(){
		// keep valid points and connections but clear discCount and color
		for (int i =0;i<length;i++){
			for (int j=0;j<length;j++){
				while (Points[i][j].validity){ 
					Points[i][j].discCounter = 0;
					Points[i][j].color = "";
			}
		}
	}		
}
	*/
	
	
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
