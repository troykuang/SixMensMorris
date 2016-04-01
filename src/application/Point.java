package application;
import java.util.ArrayList;
/**
 * A class determines the properties of each point on the board.
 */
public class Point {
	public boolean validity;
	public ArrayList<String> Hconnections;
	public ArrayList<String> Vconnections;
	public int discCounter;
	public String color;
	public int x;
	public int y;
	public String locationFlag;
	public final String coor;
	/**
	 * Creates one point on the board. For each point, it has coordinates, 
	 * validity ( indicating its visibility on the board, in other words, 
	 * a valid point or not ), connections (both horizontal connections and 
	 * vertical connections), number of discs on this point and if there 
	 * is a disc on this point, the color of the disc. As well as its location
	 * flag which will be useful to find the connections.
	 * 
	 * @param x the x-coordinate of the point
	 * @param y the y-coordinate of the point
	 */
	
	public Point(int x, int y){
		this.validity = false;
		this.locationFlag = "";
		this.Hconnections = new ArrayList<String>();
		this.Vconnections = new ArrayList<String>();
		this.discCounter = 0;
		this.color = "black";
		this.x = x;
		this.y = y;
	//	this.coor = "("+x+","+y+")";
		this.coor = "" +x+","+y;
		
	}
			
	public String toString(){
		if (validity) {
			return (coor+locationFlag+Hconnections+Vconnections+color+discCounter+"  ");
		}
		else return "  XX  ";
	}
	
}
