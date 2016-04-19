import java.util.ArrayList;


public class RoomList { //This is just an Array of rooms (used to avoid Java problem with generic arrays)

	Room[] roomtab; //static list of rooms. defined at the beginning with the data
	public ArrayList<Room> roomlist; //List of current rooms
	
	public RoomList(Room[] rtab){ //constructor
		this.roomtab=rtab;
		roomlist = new ArrayList<Room>();
		for(Room r:rtab){
			roomlist.add(r);
		}
		OrderRooms();
	}
	
	public RoomList(){
		roomlist = new ArrayList<Room>();
	}
	
	public void setroomtab(Room[] rt){ //define the static room tabular
		this.roomtab=rt;
	}
	
	public void AddRoom(Room r){//Add a room to the List
		//TODO TEST: Check if the room is missing
		roomlist.add(r);
	}
	
	public void DeleteRoom(Room r){//Add a room to the List
		//TODO TEST: Check if the room is there
		roomlist.remove(r);
	}
	
	public void OrderRooms(){//Order the rooms by size //WARNING may be costly: implement in DataLoader?
		
	}
	
	public RoomList copy(Room[] newroomtab){
		RoomList rl = new RoomList();
		rl.setroomtab(newroomtab);
		for(Room r:this.roomlist){
			rl.roomlist.add(newroomtab[r.ID]);
		}
		return rl;
	}
	
	//future improvement: order the rooms
}
