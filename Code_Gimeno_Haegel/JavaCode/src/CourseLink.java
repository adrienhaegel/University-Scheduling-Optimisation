import java.util.ArrayList;


public class CourseLink {

	
	public ArrayList<TimeSlot> tslist;
	public ArrayList<Room> roomlist;
	
	public CourseLink(TimeSlot ts, Room r) {//This class is only a key containing a TimeSlot and a Room
		tslist = new ArrayList<TimeSlot>();
		roomlist = new ArrayList<Room>();
		this.tslist.add(ts);
		this.roomlist.add(r);
	}
	
	public CourseLink() {//This class is only a key containing a TimeSlot and a Room
		tslist = new ArrayList<TimeSlot>();
		roomlist = new ArrayList<Room>();
	}
	
	public int GetSize(){
		return this.tslist.size();
	}

	public TimeSlot GetTS(int index){
		return tslist.get(index);
	}
	
	public Room GetRoom(int index){
		return roomlist.get(index);
	}
	
	public void AddLink(TimeSlot ts, Room r){
		tslist.add(ts);
		roomlist.add(r);
	}
	
	public void Delete(int index){
		tslist.remove(index);
		roomlist.remove(index);
	}
	
	public ArrayList<Integer> Find(TimeSlot t){
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(int i=0; i<tslist.size();i++){
			if(tslist.get(i).equals(t)){
				res.add(i);
			}
		}
		return res;
	}
	
	public ArrayList<Integer> Find(Room r){
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(int i=0; i<roomlist.size();i++){
			if(roomlist.get(i).equals(r)){
				res.add(i);
			}
		}
		return res;
	}
	
	public int Find(TimeSlot t, Room r){
		for(int i=0; i<roomlist.size();i++){
			if(roomlist.get(i).equals(r) & tslist.get(i).equals(t)){
				return i;
			}
			
		}
		return -1;

	}
	

	
	
}
