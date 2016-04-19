import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;



public class Course {// Course class

	String courseid;
	int ID;
	int nboflectures;
	int MWD; //minimum working days (data)
	int nbstudents;
	static int nbdays;
	static int nbofperiodsperday;
	Lecturer lecturer;
	//*****
	ArrayList<Curriculum> curriculumlist;

	Status Availability[][];
	int WD[]; //number of courses per day
	int WDaddingscore[]; //cost of adding a course a given day (used to compute the delta evaluation of the working days constraint)
	int WDsubstractingscore[];//cost of substracting a course a given day (used to compute the delta evaluation of the working days constraint)
	
	int lecturecounter;

	ArrayList<TimeSlot> lecturetimeslots; //the time slots of the different lectures
	HashMap<Room,Integer> roomstability; //The room for the different lectures and the corresponding number of lectures
	

	public Course(String stringid, int ID,Lecturer lecturer, int nboflectures,int MWD, int nbstudents){//constructor
		this.courseid=stringid;
		this.ID=ID;
		this.lecturer=lecturer;//Assign the lecturer to this course
		this.nboflectures=nboflectures;
		this.MWD=MWD;
		this.nbstudents = nbstudents;
		this.curriculumlist=new ArrayList<Curriculum>();
		this.lecturecounter=0;
		lecturer.AssignCourse(this);//Assign this course to the lecturer

		lecturetimeslots = new ArrayList<TimeSlot>();
		roomstability = new HashMap<Room,Integer>();
		
		Availability = new Status[nbdays][nbofperiodsperday];
		WD = new int[nbdays];
		WDaddingscore = new int[nbdays];
		WDsubstractingscore = new int[nbdays];
		for(int i=0;i<nbdays;i++){
			for(int j=0; j<nbofperiodsperday;j++){
				Availability[i][j]=new Status();
			}
			WD[i]=0;
			WDaddingscore[i]=-5;
			WDsubstractingscore[i]=0;
		}
	}

	public Course(){
		
	}
	
	public Course copy(){
		Course c = new Course();
		c.courseid=this.courseid;
		c.ID=this.ID;
		c.nboflectures=this.nboflectures;
		c.MWD=this.MWD;
		c.nbstudents = this.nbstudents;
		c.curriculumlist=new ArrayList<Curriculum>();
		c.lecturecounter=0;
		
		c.lecturetimeslots = new ArrayList<TimeSlot>();
		c.roomstability = new HashMap<Room,Integer>();
		
		c.Availability = new Status[nbdays][nbofperiodsperday];
		c.WD = new int[nbdays];
		c.WDaddingscore = new int[nbdays];
		c.WDsubstractingscore = new int[nbdays];
		for(int i=0;i<nbdays;i++){
			for(int j=0; j<nbofperiodsperday;j++){
				c.Availability[i][j]=new Status();
			}
			c.WD[i]=0;
			c.WDaddingscore[i]=-5;
			c.WDsubstractingscore[i]=0;
		}
		
		
		for(int i=0; i<nbdays;i++){
			for(int j=0; j<nbofperiodsperday;j++){
				c.Availability[i][j]=this.Availability[i][j].copy();
			}
		}
		c.WD=this.WD.clone();
		c.WDaddingscore = this.WDaddingscore.clone();
		c.WDsubstractingscore = this.WDsubstractingscore.clone();
		
		c.lecturecounter=this.lecturecounter;
		
		for(TimeSlot t:this.lecturetimeslots){
			c.lecturetimeslots.add(t.copy());
		}
		
		
		return c;
	}

	
	
	static void Assignnbdays(int nbofdays, int nbperiods){ //static : assign nbofdays and nbofperiods
		nbdays=nbofdays;
		nbofperiodsperday=nbperiods;
	}

	public void AssignCurriculum(Curriculum curriculum){//Assign the curriculum to this course
		this.curriculumlist.add(curriculum);
	}


	public boolean Isavailable(TimeSlot t){//Check if the curriculum is available
		return Availability[t.currentday][t.currentperiod].Isavailable();
	}


	public void AssignCourse(TimeSlot t,Room r){//Assign a course to a timeslot and a room
		if(!this.Isavailable(t)){
			System.out.println("Error!! : the course "+ this.toString() + "is being planned on timeslot " + t.toString());
		}
		
		AssignRoom(r);
		this.lecturetimeslots.add(t);
		for(Curriculum cur:this.curriculumlist){
			for(Course course:cur.courselist){
				course.BookCurriculum(cur, t);
			}
		}
		for(Course course:this.lecturer.teachinglist){
			course.BookTeacher(t);
		}
		this.lecturecounter+=1;
		this.WD[t.currentday]+=1;
		updateWDaddingscore();
		updateWDsubstractingscore();
		for(Curriculum c:this.curriculumlist){
			c.ScheduleCourseCurriculum(t);
		}
		
		//MAG: should we remove room r from available roomlist?
		
		
		//System.out.println(WD[0]);
		//System.out.println(WDaddingscore[0]);
	}

	public void UnAssignCourse(TimeSlot t,Room r){// Unassign an assigned course
		//System.out.println(this.lecturetimeslots.toString());
		UnAssignRoom(r);
		this.lecturetimeslots.remove(this.lecturetimeslots.indexOf(t));
		for(Curriculum cur:this.curriculumlist){
			for(Course course:cur.courselist){
				course.FreeCurriculum(cur, t);
			}
		}
		for(Course course:this.lecturer.teachinglist){
			course.FreeTeacher(t);
		}
		this.lecturecounter-=1;
		this.WD[t.currentday]-=1;
		updateWDaddingscore();
		updateWDsubstractingscore();
		for(Curriculum c:this.curriculumlist){
			c.UnScheduleCourseCurriculum(t);
		}
	}

	private void AssignRoom(Room r){
		if(this.roomstability.get(r)!=null){
			int n = this.roomstability.get(r);
			this.roomstability.put(r, n+1);
		}
		else{
			this.roomstability.put(r, 1);
		}
	}
	
	private void UnAssignRoom(Room r){

		//System.out.println(this.roomstability.keySet());
		if(this.roomstability.get(r)>1){
			int n = this.roomstability.get(r);
			this.roomstability.put(r, n-1);
		}
		else{
			this.roomstability.remove(r);
		}
	}
	
	public int AddingRoomStabilityCost(Room r){
		if(this.roomstability.containsKey(r)){
			return 0;
		}
		else if(this.roomstability.isEmpty())
		{
			return 0;
		}
		else{
			return 1;
		}
	}
	
	public int SubtractingRoomStabilityCost(Room r){
		if(this.roomstability.get(r)==1 & this.roomstability.size()>1){
			return -1;
		}
		else{
			return 0;
		}
	}
	
	
	private void updateWDaddingscore(){//update the WD score
		int workingdays=0;
		for(int i=0; i<nbdays;i++){
			if(this.WD[i]>0){
				workingdays+=1;
			}
		}
		if(workingdays >= this.MWD){
			for(int i=0; i<nbdays;i++){
				this.WDaddingscore[i]=0;
			}
		}
		else{
			for(int i=0; i<nbdays;i++){
				if(this.WD[i]==0){
					this.WDaddingscore[i]=-5;
				}
				else{
					this.WDaddingscore[i]=0;
				}
			}
		}
		//System.out.println(Arrays.toString(this.WDaddingscore));
		//System.out.println(Arrays.toString(this.WD));
		//System.out.println("MWD: " + this.MWD + "  Working days : "+ workingdays);
		//System.out.println("\n" + "\n");
	}
	private void updateWDsubstractingscore(){ //update the WD score
		int workingdays=0;
		for(int i=0; i<nbdays;i++){
			if(this.WD[i]>0){
				workingdays+=1;
			}
		}
		if(workingdays > this.MWD){
			for(int i=0; i<nbdays;i++){
				this.WDsubstractingscore[i]=0;
			}
		}
		else{
			for(int i=0; i<nbdays;i++){
				if(this.WD[i]==1){
					this.WDsubstractingscore[i]=5;
				}
				else{
					this.WDsubstractingscore[i]=0;
				}
			}
		}
	}
	
	public int GetAddingMWDCost(TimeSlot t){ //Cost of adding a course to a given timeslot ( minimum working days)
		int cost =0;
		cost+=this.WDaddingscore[t.currentday];
		//System.out.println("WD: " + cost);
		return cost;
	}
	public int GetAddingCurriculumCost(TimeSlot t){ //Cost of adding a course to a given timeslot (Curriculumcompactness)
		int cost =0;
		for(Curriculum c:this.curriculumlist){
			cost+=c.Getsecludedaddingcost(t);
			//System.out.println("Cur: " + cost);
		}
		//System.out.println("WD: " + cost);
		return cost;
	}
	
	
	public int GetSubtractingMWDCost(TimeSlot t){//Cost of withdrawing a course from a given timeslot ( minimum working days)
		int cost =0;
		
		cost+=this.WDsubstractingscore[t.currentday];
		return cost;
	}
	public int GetSubtractingCurriculumCost(TimeSlot t){//Cost of withdrawing a course from a given timeslot (Curriculumcompactness)
		int cost =0;
		for(Curriculum c:this.curriculumlist){
			//System.out.println(c.toString());
			cost+=c.Getsecludedsubtractingcost(t);
		}
		return cost;
	}

	private void BookCurriculum(Curriculum c, TimeSlot t){
		this.Availability[t.currentday][t.currentperiod].TakeCurriculum(c);
	}

	private void FreeCurriculum(Curriculum c, TimeSlot t){
		this.Availability[t.currentday][t.currentperiod].FreeCurriculum(c);
	}

	private void BookTeacher(TimeSlot t){
		this.Availability[t.currentday][t.currentperiod].TakeTeacher();
	}

	private void FreeTeacher(TimeSlot t){
		this.Availability[t.currentday][t.currentperiod].FreeTeacher();
	}

	public void DefineUnavailability(int day, int period){ //define the unavailabilities obtained in unavalaibility.utt
		Availability[day][period].SetUnavailable();
	}

	
	
	
	//common GET methods
	public int getnboflectures(){
		return this.nboflectures;
	}

	public String toString(){//toString
		String s="";
		s+="Course name : " + courseid; //Course id
		//s+="Course name : " + courseid + " CourseID : " + ID + " nblectures " + nboflectures ; //extensive course description
		//s+= " Curricula : " + this.curriculumlist.toString() + "\n" ; //Curricula description
		s+=  Arrays.deepToString(this.Availability); //availability array
		return s;
	}
	
	public void ResetCourses(){

		//this.curriculumlist=new ArrayList<Curriculum>();
		this.lecturecounter=0;
		

		lecturetimeslots = new ArrayList<TimeSlot>();
		roomstability = new HashMap<Room,Integer>();

		WD = new int[nbdays];
		WDaddingscore = new int[nbdays];
		WDsubstractingscore = new int[nbdays];
		for(int i=0;i<nbdays;i++){
			for(int j=0; j<nbofperiodsperday;j++){
				Availability[i][j].Reset();
				
			}
			WD[i]=0;
			WDaddingscore[i]=-5;
			WDsubstractingscore[i]=0;
		}
	}


}
