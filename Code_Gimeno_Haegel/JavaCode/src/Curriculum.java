import java.util.ArrayList;


public class Curriculum { //Curriculum class

	String curriculumid;
	int ID;
	int nbcourse;
	//*****
	ArrayList<Course> courselist; //List of courses related to this curriculum
	static int nbdays;
	static int nbofperiodsperday;
	boolean plannedcourses[][];//list of planned courses : true = course planned on that timeslot
	int nbadjacentcourse[][];//number of adjacent planned courses : 0 = no neighbours, 1 = 1 neighbour, 2 = 2 neighbours 
	int secludedaddingcost[][]; //cost of adding a course -> curriculum compactness
	int secludedsubtractingcost[][];//cost of withdrawing a course -> curriculum compactness


	public Curriculum(String s, int id, int nbcourse){//constructor
		this.curriculumid=s;
		this.ID=id;
		this.nbcourse = nbcourse;
		courselist=new ArrayList<Course>();

		plannedcourses = new boolean[nbdays][nbofperiodsperday];
		nbadjacentcourse=new int[nbdays][nbofperiodsperday];
		secludedaddingcost = new int[nbdays][nbofperiodsperday];
		secludedsubtractingcost = new int[nbdays][nbofperiodsperday];
		for(int i=0;i<nbdays;i++){
			for(int j=0; j<nbofperiodsperday;j++){
				nbadjacentcourse[i][j]=0;
				plannedcourses[i][j]=false;
				secludedaddingcost [i][j]=2;
				secludedsubtractingcost [i][j]=0;
			}
		}

	}

	public void Reset(){
		courselist=new ArrayList<Course>();

		plannedcourses = new boolean[nbdays][nbofperiodsperday];
		nbadjacentcourse=new int[nbdays][nbofperiodsperday];
		secludedaddingcost = new int[nbdays][nbofperiodsperday];
		secludedsubtractingcost = new int[nbdays][nbofperiodsperday];
		for(int i=0;i<nbdays;i++){
			for(int j=0; j<nbofperiodsperday;j++){
				nbadjacentcourse[i][j]=0;
				plannedcourses[i][j]=false;
				secludedaddingcost [i][j]=2;
				secludedsubtractingcost [i][j]=0;
			}
		}
	}

	public Curriculum copy(){
		Curriculum c = new Curriculum(this.curriculumid, this.ID, this.nbcourse);
		for(int i=0;i<nbdays;i++){
			for(int j=0; j<nbofperiodsperday;j++){
				c.plannedcourses[i][j]=this.plannedcourses[i][j];
				c.nbadjacentcourse[i][j]=this.nbadjacentcourse[i][j];
				c.secludedaddingcost[i][j]=this.secludedaddingcost[i][j];
				c.secludedsubtractingcost[i][j]=this.secludedsubtractingcost[i][j];
			}
		}



		return c;
	}

	static void Assignnbdays(int nbofdays, int nbperiods){
		nbdays=nbofdays;
		nbofperiodsperday=nbperiods;
	}



	public void AssignCourse(Course course){//Assign a course to the curriculum
		courselist.add(course);
	}

	public void ScheduleCourseCurriculum(TimeSlot t){
		plannedcourses[t.currentday][t.currentperiod]=true;
		if(t.currentperiod==0)
		{
			this.nbadjacentcourse[t.currentday][t.currentperiod+1]+=1;
		}
		else if(t.currentperiod==nbofperiodsperday-1)
		{
			this.nbadjacentcourse[t.currentday][t.currentperiod-1]+=1;
		}
		else{
			this.nbadjacentcourse[t.currentday][t.currentperiod-1]+=1;
			this.nbadjacentcourse[t.currentday][t.currentperiod+1]+=1;
		}

		UpdateSecludeAddingCost(t);
		UpdateSecludeSubtractingCost(t);

	}

	public void UnScheduleCourseCurriculum(TimeSlot t){
		plannedcourses[t.currentday][t.currentperiod]=false;
		if(t.currentperiod==0)
		{
			this.nbadjacentcourse[t.currentday][t.currentperiod+1]-=1;
		}
		else if(t.currentperiod==t.nbperiod-1)
		{
			this.nbadjacentcourse[t.currentday][t.currentperiod-1]-=1;
		}
		else{
			this.nbadjacentcourse[t.currentday][t.currentperiod-1]-=1;
			this.nbadjacentcourse[t.currentday][t.currentperiod+1]-=1;
		}
		UpdateSecludeAddingCost(t);
		UpdateSecludeSubtractingCost(t);
	}


	public void UpdateSecludeAddingCost(TimeSlot t){

		////Here we update the array secludeaddingcost for the day corresponding to t.

		//for period=0
		if(!this.plannedcourses[t.currentday][0]){ //we only update the time slots without planned courses
			if(!this.plannedcourses[t.currentday][1]){ //if the next period has no planned course
				this.secludedaddingcost[t.currentday][0]=2;
			}
			else{//else update according to the number of neighbors
				if(this.nbadjacentcourse[t.currentday][1]==0){
					this.secludedaddingcost[t.currentday][0]=-2;
				}
				if(this.nbadjacentcourse[t.currentday][1]==1){
					this.secludedaddingcost[t.currentday][0]=0;
				}
			}
		}
		//else{
		//	this.secludedaddingcost[t.currentday][0]=10000;
		//}

		//period = 1 .. nbperiods-1
		for(int period = 1; period<nbofperiodsperday-1; period++){
			if(!this.plannedcourses[t.currentday][period]){//we only update the time slots without planned courses
				if(!this.plannedcourses[t.currentday][period-1] && !this.plannedcourses[t.currentday][period+1] ){//if there is no adjacent courses planned
					this.secludedaddingcost[t.currentday][period]=2;
				}
				else{ //the value is -2 for each of the adjacent courses that have no neighbors
					int value=0;
					if(this.plannedcourses[t.currentday][period-1] && this.nbadjacentcourse[t.currentday][period-1]==0){
						value-=2;
					}
					if(this.plannedcourses[t.currentday][period+1] && this.nbadjacentcourse[t.currentday][period+1]==0){
						value-=2;
					}
					this.secludedaddingcost[t.currentday][period]=value;
				}

			}
			//else{
			//	this.secludedaddingcost[t.currentday][period]=10000;
			//}
		}

		//for period = nbperiod-1
		if(!this.plannedcourses[t.currentday][nbofperiodsperday-1]){//we only update the time slots without planned courses
			if(!this.plannedcourses[t.currentday][nbofperiodsperday-2]){//if the previous period has no planned course
				this.secludedaddingcost[t.currentday][nbofperiodsperday-1]=2;
			}
			else{ //else update according to the number of neighbours
				if(this.nbadjacentcourse[t.currentday][nbofperiodsperday-2]==0){
					this.secludedaddingcost[t.currentday][nbofperiodsperday-1]=-2;
				}
				if(this.nbadjacentcourse[t.currentday][nbofperiodsperday-2]==1){
					this.secludedaddingcost[t.currentday][nbofperiodsperday-1]=0;
				}
			}
		}
		//else{
		//this.secludedaddingcost[t.currentday][nbofperiodsperday-1]=10000;
		//}
	}

	public void UpdateSecludeSubtractingCost(TimeSlot t){
		////Here we update the array secludesubtractingcost for the day corresponding to t.

		//for period=0
		if(this.plannedcourses[t.currentday][0]){ //we only update the time slots with planned courses
			if(!this.plannedcourses[t.currentday][1]){ //if the next period has no planned course
				this.secludedsubtractingcost[t.currentday][0]=-2;
			}
			else{//else update according to the number of neighbors
				if(this.nbadjacentcourse[t.currentday][1]==1){
					this.secludedsubtractingcost[t.currentday][0]=2;
				}
				if(this.nbadjacentcourse[t.currentday][1]==2){
					this.secludedsubtractingcost[t.currentday][0]=0;
				}
			}
		}
		//else{
		//	this.secludedsubtractingcost[t.currentday][0]=10000;
		//}

		//period = 1 .. nbperiods-1
		for(int period = 1; period<nbofperiodsperday-1; period++){
			if(this.plannedcourses[t.currentday][period]){//we only update the time slots with planned courses
				if(!this.plannedcourses[t.currentday][period-1] && !this.plannedcourses[t.currentday][period+1] ){//if there is no adjacent courses planned
					this.secludedsubtractingcost[t.currentday][period]=-2;
				}
				else{ //the value is -2 for each of the adjacent courses that have no neighbors
					int value=0;
					if(this.plannedcourses[t.currentday][period-1] && this.nbadjacentcourse[t.currentday][period-1]==1){
						value+=2;
					}
					if(this.plannedcourses[t.currentday][period+1] && this.nbadjacentcourse[t.currentday][period+1]==1){
						value+=2;
					}
					this.secludedsubtractingcost[t.currentday][period]=value;
				}

			}
			//else{
			//	this.secludedsubtractingcost[t.currentday][period]=10000;
			//}
		}

		//for period = nbperiod-1
		if(this.plannedcourses[t.currentday][nbofperiodsperday-1]){//we only update the time slots with planned courses
			if(!this.plannedcourses[t.currentday][nbofperiodsperday-2]){//if the previous period has no planned course
				this.secludedsubtractingcost[t.currentday][nbofperiodsperday-1]=-2;
			}
			else{ //else update according to the number of neighbours
				if(this.nbadjacentcourse[t.currentday][nbofperiodsperday-2]==1){
					this.secludedsubtractingcost[t.currentday][nbofperiodsperday-1]=2;
				}
				if(this.nbadjacentcourse[t.currentday][nbofperiodsperday-2]==2){
					this.secludedsubtractingcost[t.currentday][nbofperiodsperday-1]=0;
				}
			}
		}
		//else{
		//this.secludedsubtractingcost[t.currentday][nbofperiodsperday-1]=10000;
		//}
	}




	public void UpdateCoursesecluded(){

	}

	public int Getsecludedaddingcost(TimeSlot t){

		return this.secludedaddingcost[t.currentday][t.currentperiod];
	}


	public int Getsecludedsubtractingcost(TimeSlot t){

		return this.secludedsubtractingcost[t.currentday][t.currentperiod];
	}

	public String toString(){

		String clstring = "";
		for(Course c: this.courselist){
			clstring += c.courseid + " ";
		}


		return "Curriculum : "+ this.curriculumid + "  nbcourse: " + this.nbcourse + "  Courselist : " + clstring  ;
	}

	public String deeptoString(){
		String s = "";
		for(Course c: this.courselist){
			s+=c.toString() + " ";

		}
		return s;
	}
	
	public boolean equals(Curriculum c){
		return this.ID==c.ID;
	}

}
