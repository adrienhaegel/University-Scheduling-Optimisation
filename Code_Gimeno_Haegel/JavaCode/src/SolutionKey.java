
public class SolutionKey implements Comparable<SolutionKey> {
	TimeSlot ts;
	Room r;
	Course c;
	int courseindex;
	public int[] scoreTS;
	public int[] scoreRoom;
	
	public SolutionKey(TimeSlot ts, Room r, int[] scoreTS, int[] scoreRoom){
		this.ts=ts;
		this.r=r;
		this.scoreTS = scoreTS;
		this.scoreRoom = scoreRoom;
	}
	
	public SolutionKey(TimeSlot ts, Room r, int[] scoreTS, int[] scoreRoom, Course c, int index){
		this.ts=ts;
		this.r=r;
		this.scoreTS = scoreTS;
		this.scoreRoom = scoreRoom;
		this.c=c;
		this.courseindex=index;
	}

	public double Comparison(SolutionKey k){
			return this.scoreTS[0]-k.scoreTS[0]+this.scoreTS[1]-k.scoreTS[1]+this.scoreTS[2]-k.scoreTS[2]+this.scoreRoom[0]-k.scoreRoom[0];
	}
	
	public int compareTo(SolutionKey k) {
		return 1000*(this.scoreTS[0]-k.scoreTS[0]+this.scoreTS[1]-k.scoreTS[1]+this.scoreTS[2]-k.scoreTS[2]+this.scoreRoom[0]-k.scoreRoom[0]+this.scoreRoom[1]-k.scoreRoom[1])+(this.r.capacity-k.r.capacity);
		
	}
	
	public String toString(){
		return "TS: " + ts.toString() + " Room : " + r.toString() + " score : " + scoreTS.toString() + scoreRoom.toString();
	}
	
	public int getScore(){
		return this.scoreTS[0]+this.scoreTS[1]+this.scoreTS[2]+this.scoreRoom[0]+this.scoreRoom[1];
	}
}
