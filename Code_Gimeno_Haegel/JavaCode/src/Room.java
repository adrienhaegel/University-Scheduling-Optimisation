
public class Room { //Room Class

	int capacity;
	String stringid;
	int ID;
	
	
	public Room(String stringid, int ID, int capacity){//constructor
		this.stringid=stringid;
		this.ID=ID;
		this.capacity=capacity;
	}
	
	
	public String toString(){
		return "Room : " + this.stringid + " Capacity = " + this.capacity;
	}
	
	public Room copy(){
		return new Room(this.stringid,this.ID,this.capacity);
	}
	
	public boolean equals(Room r){
		return r.ID==this.ID;
	}
	
}
