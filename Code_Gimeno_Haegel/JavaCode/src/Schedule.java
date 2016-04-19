import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

enum SORTING {DONOTHING,RANDOMIZE,BYCURRICULUM,BYNBSTUDENTS,BYNBLECTURES, BYNBSTUDENTSANDRANDOM};
enum DESTROY {DESTROYCOURSE,DESTROYCURRICULUM,DESTROYROOM,DESTROYTS,RANDOM,DESTROYLOWESTVALUE,DESTROYTOFREESCHEDULE};


public class Schedule { //This is where the algorithms apply

	DataLoader data; //the data
	Course[][] currentsolution; //The current schedule currentsolution[Timeslot][Room] = course
	CourseLink[] currentsolutionbycourse;
	RoomList availablerooms[]; //an Array of roomlists. roomlist[timeslot]=Available remaining rooms for that timeslot
	Course[] coursetab;
	Room roomtab[];
	ArrayList<Course> coursestoplan; //Courses that still have to be scheduled. a same course appears here several time: for each lecture
	ArrayList<Course> unscheduledcourses; // Courses that have not managed to be put on the schedule


	int currentsolutionvalue; //The current value of the solution
	int currentunscheduledvalue;
	int currentMWDvalue;
	int currentroomstabilityvalue;
	int currentcurriculumcompactnessvalue;
	int currentroomcapacityvalue;



	public Schedule(DataLoader Data){//Constructor

		this.data=Data;
		this.coursetab=Data.coursetab;
		this.roomtab=Data.roomtab;
		currentsolution=new Course[data.getnumberofslots()][data.getnumberofrooms()]; //MAG: why there are two dimensions?
		currentsolutionbycourse = new CourseLink[this.data.nbcourses];
		for(int i=0;i<data.nbcourses;i++){
			currentsolutionbycourse[i]= new CourseLink();
		}
		availablerooms = new RoomList[data.getnumberofslots()];
		for(int i=0; i<data.getnumberofslots();i++){
			availablerooms[i]=new RoomList(Data.roomtab);
		}
		coursestoplan = new ArrayList<Course>(data.courselist);
		unscheduledcourses = new ArrayList<Course>();
		this.currentsolutionvalue=Data.emptysolutionvalue;
		this.currentMWDvalue=Data.emptyMWDvalue;
		this.currentunscheduledvalue=Data.emptyunscheduledvalue;
	}

	public Schedule ScheduleCopy(DataLoader newdata){
		Schedule s = new Schedule();
		s.data=newdata;
		s.coursetab=newdata.coursetab;
		s.roomtab=newdata.roomtab;

		s.currentsolutionvalue=this.currentsolutionvalue;
		s.currentunscheduledvalue=this.currentunscheduledvalue;
		s.currentcurriculumcompactnessvalue=this.currentcurriculumcompactnessvalue;
		s.currentMWDvalue=this.currentMWDvalue;
		s.currentroomcapacityvalue=this.currentroomcapacityvalue;
		s.currentroomstabilityvalue=this.currentroomstabilityvalue;
		s.currentsolutionbycourse = new CourseLink[this.data.nbcourses];
		s.currentsolution=new Course[data.getnumberofslots()][data.getnumberofrooms()];
		TimeSlot ts = new TimeSlot();
		while(ts.TSUpdate()){
			for(Room r:this.roomtab){
				if(this.currentsolution[ts.TSIndex()][r.ID]!=null)
					s.currentsolution[ts.TSIndex()][r.ID]=s.coursetab[this.currentsolution[ts.TSIndex()][r.ID].ID];
			}
		}

		for(int i=0; i<data.nbcourses;i++){
			CourseLink cl = this.currentsolutionbycourse[i];
			s.currentsolutionbycourse[i]= new CourseLink();
			for(int j=0;j<cl.GetSize();j++){

				s.currentsolutionbycourse[i].AddLink(cl.GetTS(j).copy(), s.roomtab[cl.GetRoom(j).ID]);
			}

		}

		s.availablerooms = new RoomList[data.getnumberofslots()];
		for(int i=0;i<data.getnumberofslots();i++){
			s.availablerooms[i]=this.availablerooms[i].copy(s.roomtab);
		}

		s.coursestoplan = new ArrayList<Course>();
		for(Course c:this.coursestoplan){
			s.coursestoplan.add(s.coursetab[c.ID]);
		}

		s.unscheduledcourses = new ArrayList<Course>();
		for(Course c:this.unscheduledcourses){
			s.unscheduledcourses.add(s.coursetab[c.ID]);
		}

		return s;
	}

	public Schedule(){

	}

	public void TestSc01(){
		Course c0 = coursetab[8];
		System.out.println(c0.toString());
		TimeSlot ts0 = new TimeSlot(2,0);
		Room r0=roomtab[2];
		System.out.println(r0);
		//int c01=CalculateTSDeltaddingCost(c0, ts0);
		System.out.println(ts0.toString());
		//int c02= CalculateRoomDeltaddingCost(c0,r0);
		//System.out.println("TSCost : " + c01 + "  Roomcost :"+ c02);
		c0.AssignCourse(ts0,roomtab[0]);
		System.out.println(c0.toString());
		for(Curriculum cur: c0.curriculumlist){
			System.out.println(cur.deeptoString()+ "\n");

		}
		System.out.println(Arrays.deepToString(c0.curriculumlist.get(0).secludedaddingcost));
		System.out.println(Arrays.deepToString(c0.curriculumlist.get(0).secludedsubtractingcost));
		Room r1=roomtab[3];
		c0.curriculumlist.get(0).courselist.get(2).AssignCourse(new TimeSlot(2,2), r1);
		System.out.println(Arrays.deepToString(c0.curriculumlist.get(0).secludedaddingcost));
		System.out.println(Arrays.deepToString(c0.curriculumlist.get(0).secludedsubtractingcost));
		ts0.TSUpdate();
		//int c03=CalculateTSDeltaddingCost(c0, ts0);
		//int c04= CalculateRoomDeltaddingCost(c0,r1);
		//System.out.println("TSCost : " + c03 + "  Roomcost :"+ c04);
		c0.AssignCourse(ts0,roomtab[3]);
		c0.UnAssignCourse( ts0, roomtab[3]);
		System.out.println(c0.lecturer.teachinglist.toString());

		System.out.println(Arrays.deepToString(c0.curriculumlist.get(0).secludedaddingcost));
		System.out.println(Arrays.deepToString(c0.curriculumlist.get(0).secludedsubtractingcost));



		System.out.println(Arrays.deepToString(c0.curriculumlist.get(0).secludedaddingcost));
		System.out.println(Arrays.deepToString(c0.curriculumlist.get(0).secludedsubtractingcost));
		System.out.println(c0.toString());


	}

	public int[] CalculateTSDeltaddingCost(Course c, TimeSlot ts){//WARNING: Should only be used on an EMPTY room !
		int[] deltacost = new int[3]; // deltacost[0] = Unscheduled, deltacost[1] = MWD, deltacost[2]= curriculumcompactness
		deltacost[0]=-10;
		deltacost[1]=c.GetAddingMWDCost(ts);
		deltacost[2]= c.GetAddingCurriculumCost(ts);
		return deltacost;
	}

	public int[] CalculateTSDeltasubtractingCost(Course c,TimeSlot ts){//WARNING: Should only be used on a FULL room !
		int[] deltacost = new int[3]; // deltacost[0] = Unscheduled, deltacost[1] = MWD, deltacost[2]= curriculumcompactness
		deltacost[0]=10;
		deltacost[1]=c.GetSubtractingMWDCost(ts);
		deltacost[2]= c.GetSubtractingCurriculumCost(ts);
		return deltacost;
	}

	public int[] CalculateRoomDeltaddingCost(Course c, Room r){//WARNING: Should only be used on an EMPTY room !
		int[] deltacost = new int[2]; // deltacost[0] = RoomStability, deltacost[1] = RoomCapacity
		deltacost[0]=c.AddingRoomStabilityCost(r);
		deltacost[1]=Math.max(0, c.nbstudents-r.capacity);
		return deltacost;
	}

	public int[] CalculateRoomDeltasubtractingCost(Course c,Room r){//WARNING: Should only be used on a FULL room !
		int[] deltacost = new int[2]; // deltacost[0] = RoomStability, deltacost[1] = RoomCapacity
		deltacost[0]=c.SubtractingRoomStabilityCost(r);
		deltacost[1]=-Math.max(0, c.nbstudents-r.capacity);
		return deltacost;
	}

	public void addcoursetoschedule(Course c, TimeSlot ts, Room r){//adds a course to the solution
		c.AssignCourse(ts, r);
		currentsolution[ts.TSIndex()][r.ID]=c;
		currentsolutionbycourse[c.ID].AddLink(ts, r);
		availablerooms[ts.TSIndex()].DeleteRoom(r);
	}

	public void removecoursefromschedule(Course c, TimeSlot ts, Room r){//remove course from the solution
		c.UnAssignCourse(ts, r);
		currentsolution[ts.TSIndex()][r.ID]=null;
		CourseLink cl = currentsolutionbycourse[c.ID];
		cl.Delete(cl.Find(ts, r));
		availablerooms[ts.TSIndex()].AddRoom(r);
		this.unscheduledcourses.add(c);
	}

	public void easyGRASP(){
		for(Iterator<Course> iter = this.coursestoplan.iterator(); iter.hasNext();){
			Course c = iter.next();
			TimeSlot t = new TimeSlot();

			int mindelta=1000;
			TimeSlot minTS=new TimeSlot();
			Room minRoom = roomtab[0];
			boolean solutionfound=false;
			while(t.TSUpdate()){
				if(c.Isavailable(t)){
					int tscost[] = this.CalculateTSDeltaddingCost(c, t);

					for(Room r : this.availablerooms[t.TSIndex()].roomlist){
						int roomcost[] = this.CalculateRoomDeltaddingCost(c, r);
						if(tscost[0]+tscost[1]+tscost[2]+roomcost[0]+roomcost[1]<mindelta){
							mindelta=tscost[0]+tscost[1]+tscost[2]+roomcost[0]+roomcost[1];
							minTS=new TimeSlot(t.currentday,t.currentperiod);
							minRoom=r;
							solutionfound=true;
						}

					}
				}


			}

			if(solutionfound && mindelta<0){
				this.addcoursetoschedule(c, minTS, minRoom);
				this.currentsolutionvalue+=mindelta;
				//System.out.println("Course : " + c.ID + " TS : " + minTS.toString() + " Room : " + minRoom.toString() + " Solution value : " + this.currentsolutionvalue);
				iter.remove();
			}
			else{
				iter.remove();
			}






		}


	}

	public int GRASP(int windowlength, SORTING sortingtype){//GRASP Algorithm
		while(!this.unscheduledcourses.isEmpty()){
			Course c = this.unscheduledcourses.get(0);
			this.unscheduledcourses.remove(0);
			this.coursestoplan.add(c);
		}
		Random random = new Random(); //random number generator
		this.SortCoursesToPlan(sortingtype);
		for(Iterator<Course> iter = this.coursestoplan.iterator(); iter.hasNext();){ //Iteration on coursestoplan
			Course c = iter.next();
			TimeSlot t = new TimeSlot();

			ArrayList<SolutionKey> BestSolutions = new ArrayList<SolutionKey>(); // Array of the Bestsolutions

			boolean solutionfound=false; //If at least one solution is found
			while(t.TSUpdate()){//Iterations on the timeslots
				if(c.Isavailable(t)){//If this timeslot is available
					int tscost[] = this.CalculateTSDeltaddingCost(c, t);//timeslot costs
					for(Room r : this.availablerooms[t.TSIndex()].roomlist){//iteration on the available rooms

						int roomcost[] = this.CalculateRoomDeltaddingCost(c, r); //Costs for the room
						SolutionKey SK = new SolutionKey(new TimeSlot(t.currentday,t.currentperiod),r,tscost,roomcost); //SolutionKey contains all the information and costs of a potential solution
						if(SK.getScore()<0){
							if(BestSolutions.size()<windowlength){ //If Bestsolutions is not full: always add
								BestSolutions.add(SK);
							}
							else{ //The solutions are sorted and then the highest solution is replaced if the new one is better
								Collections.sort(BestSolutions);
								if (SK.getScore() <BestSolutions.get(windowlength-1).getScore()){
									BestSolutions.set(windowlength-1, SK);
								}
							}
							solutionfound=true; //there is an available room for an available timeslot
						}
					}
				}
			}

			if(solutionfound){//if a solution is found, a solution is chosen randomly amongst it
				int ran = random.nextInt(BestSolutions.size());
				SolutionKey chosensolution = BestSolutions.get(ran);
				this.addcoursetoschedule(c, chosensolution.ts, chosensolution.r); //the course is added to the schedule
				ChangeValue(chosensolution);
			}
			else{
				this.unscheduledcourses.add(c);
			}
			iter.remove();//the course is removed from the list
		}
		return this.currentsolutionvalue;
	}

	public int FullGRASP(int windowlength){//GRASP Algorithm
		while(!this.unscheduledcourses.isEmpty()){
			Course c = this.unscheduledcourses.get(0);
			this.unscheduledcourses.remove(0);
			this.coursestoplan.add(c);
		}
		Random random = new Random(); //random number generator
		boolean endloop = false;
		while(!this.coursestoplan.isEmpty() & !endloop){

			ArrayList<SolutionKey> BestSolutions = new ArrayList<SolutionKey>(); // Array of the Bestsolutions
			boolean solutionfound=false; //If at least one solution is found


			for(int courseindex=0; courseindex < this.coursestoplan.size();courseindex++){ //Iteration on coursestoplan
				Course c= this.coursestoplan.get(courseindex);
				TimeSlot t = new TimeSlot();
				while(t.TSUpdate()){//Iterations on the timeslots
					if(c.Isavailable(t)){//If this timeslot is available
						int tscost[] = this.CalculateTSDeltaddingCost(c, t);//timeslot costs
						for(Room r : this.availablerooms[t.TSIndex()].roomlist){//iteration on the available rooms

							int roomcost[] = this.CalculateRoomDeltaddingCost(c, r); //Costs for the room
							SolutionKey SK = new SolutionKey(new TimeSlot(t.currentday,t.currentperiod),r,tscost,roomcost,c,courseindex); //SolutionKey contains all the information and costs of a potential solution
							if(SK.getScore()<0){
								if(BestSolutions.size()<windowlength){ //If Bestsolutions is not full: always add
									BestSolutions.add(SK);
								}
								else{ //The solutions are sorted and then the highest solution is replaced if the new one is better
									Collections.sort(BestSolutions);
									if (SK.getScore() <BestSolutions.get(windowlength-1).getScore()){
										BestSolutions.set(windowlength-1, SK);
									}
								}
								solutionfound=true; //there is an available room for an available timeslot
							}
						}
					}
				}
			}
			if(solutionfound){//if a solution is found, a solution is chosen randomly amongst it
				int ran = random.nextInt(BestSolutions.size());
				SolutionKey chosensolution = BestSolutions.get(ran);
				this.addcoursetoschedule(chosensolution.c, chosensolution.ts, chosensolution.r); //the course is added to the schedule
				ChangeValue(chosensolution);
				this.coursestoplan.remove(chosensolution.courseindex);
			}
			else{
				endloop = true;
			}


		}
		return this.currentsolutionvalue;
	}

	public void Destroy(DESTROY destroy, int[] parameters){
		Random rand = new Random();
		int nbtodestroy;
		switch(destroy)
		{
		case DESTROYCOURSE:
			if(parameters[0]<2){
				nbtodestroy=1;
			}
			else{
				nbtodestroy = rand.nextInt(parameters[0]-1)+1;
			}
			for(int i=0; i<nbtodestroy;i++){
				Course c = this.data.coursetab[rand.nextInt(this.data.nbcourses)];
				int size = this.currentsolutionbycourse[c.ID].GetSize();
				for(int j=0;j<size;j++){
					TimeSlot t = this.currentsolutionbycourse[c.ID].GetTS(0);
					Room r = this.currentsolutionbycourse[c.ID].GetRoom(0);
					int[] s1 = this.CalculateRoomDeltasubtractingCost(c, r);
					int[] s2 = this.CalculateTSDeltasubtractingCost(c, t);
					this.removecoursefromschedule(c, t, r);
					SolutionKey sk= new SolutionKey(t,r,s2,s1);
					this.ChangeValue(sk);

				}
			}
			break;
		case DESTROYROOM:
			if(parameters[0]<2){
				nbtodestroy=1;
			}
			else{
				nbtodestroy = rand.nextInt(parameters[0]-1)+1;
			}
			for(int i=0; i<nbtodestroy;i++){
				Room r = this.data.roomtab[rand.nextInt(this.data.nbrooms)];
				TimeSlot ts = new TimeSlot();
				while(ts.TSUpdate()){
					if(this.currentsolution[ts.TSIndex()][r.ID]!=null){
						Course c = this.currentsolution[ts.TSIndex()][r.ID];
						int[] s1 = this.CalculateRoomDeltasubtractingCost(c, r);
						int[] s2 = this.CalculateTSDeltasubtractingCost(c, ts.copy());
						this.removecoursefromschedule(c, ts.copy(), r);
						SolutionKey sk= new SolutionKey(ts,r,s2,s1);
						this.ChangeValue(sk);
					}
				}
			}
			break;
		case DESTROYCURRICULUM:
			if(parameters[0]<2){
				nbtodestroy=1;
			}
			else{
				nbtodestroy = rand.nextInt(parameters[0]-1)+1;
			}

			for(int i=0; i<nbtodestroy;i++){
				Curriculum cur = this.data.curriculumtab[rand.nextInt(this.data.nbcurriculum)];
				for(Course cc: cur.courselist){
					int size = this.currentsolutionbycourse[cc.ID].GetSize();
					for(int j=0;j<size;j++){
						TimeSlot t = this.currentsolutionbycourse[cc.ID].GetTS(0);
						Room r = this.currentsolutionbycourse[cc.ID].GetRoom(0);
						int[] s1 = this.CalculateRoomDeltasubtractingCost(cc, r);
						int[] s2 = this.CalculateTSDeltasubtractingCost(cc, t);
						this.removecoursefromschedule(cc, t, r);
						SolutionKey sk= new SolutionKey(t,r,s2,s1);
						this.ChangeValue(sk);
					}
				}

			}
			break;
		case DESTROYTS:
			if(parameters[0]<2){
				nbtodestroy=1;
			}
			else{
				nbtodestroy = rand.nextInt(parameters[0]-1)+1;
			}
			for(int i=0; i<nbtodestroy;i++){
				TimeSlot ts = new TimeSlot(rand.nextInt(this.data.nbdays),rand.nextInt(this.data.nbperiodsperday));
				for(int roomind = 0 ; roomind< this.data.nbrooms;roomind++){
					Room rr = this.data.roomtab[roomind];
					if(this.currentsolution[ts.TSIndex()][rr.ID]!=null){
						Course ccc = this.currentsolution[ts.TSIndex()][rr.ID];
						int[] s1 = this.CalculateRoomDeltasubtractingCost(ccc, rr);
						int[] s2 = this.CalculateTSDeltasubtractingCost(ccc, ts.copy());
						this.removecoursefromschedule(ccc, ts.copy(), rr);
						SolutionKey sk= new SolutionKey(ts,rr,s2,s1);
						this.ChangeValue(sk);
					}
				}
			}
			break;
		case RANDOM:
			rand = new Random();
			int i = rand.nextInt(4);
			if(i==0){
				parameters[0]= 5;
				this.Destroy(DESTROY.DESTROYCOURSE, parameters);
			}
			else if(i==3){
				parameters[0]= 1;
				this.Destroy(DESTROY.DESTROYCURRICULUM, parameters);
			}
			else if(i==2){
				parameters[0]= 1;
				this.Destroy(DESTROY.DESTROYROOM, parameters);
			}
			else if(i==1){
				parameters[0]= 3;
				this.Destroy(DESTROY.DESTROYTS, parameters);
			}
			break;

		case DESTROYLOWESTVALUE:
			for(int k=0; k<this.data.nbcourses;k++){
				Course c = this.data.coursetab[k];
				int size = this.currentsolutionbycourse[c.ID].GetSize();
				for(int j=0;j<size;j++){
					TimeSlot t = this.currentsolutionbycourse[c.ID].GetTS(0);
					Room r = this.currentsolutionbycourse[c.ID].GetRoom(0);
					int[] s1 = this.CalculateRoomDeltasubtractingCost(c, r);
					int[] s2 = this.CalculateTSDeltasubtractingCost(c, t);
				
					SolutionKey sk= new SolutionKey(t,r,s2,s1);
					
					if(sk.getScore()<6){
					//System.out.println(sk.getScore());
					
					this.removecoursefromschedule(c, t, r);
					this.ChangeValue(sk);
					}
				}
			}
			break;
		}

	}

	public void ChangeValue(SolutionKey sk){
		this.currentsolutionvalue+=sk.getScore(); //the current solution costs are updated
		this.currentunscheduledvalue+=sk.scoreTS[0];
		this.currentMWDvalue+=sk.scoreTS[1];				
		this.currentcurriculumcompactnessvalue+=sk.scoreTS[2];
		this.currentroomcapacityvalue+=sk.scoreRoom[1];
		this.currentroomstabilityvalue+=sk.scoreRoom[0];
	}


	public void GRASPSelection(){
		//TODO	
	}

	public void RandomStart(){
		//select a random course 
		//select a random timeslot
		//select a random available room, if not, dont push the course
		//update
	}

	public void GRASPDestruction(){

	}

	public void GRASPReconstruction(){


	}

	public int getsolutionvalue(){
		return this.currentsolutionvalue;
	}


	public String PrintSolution(){
		String s = "";

		s += "UNSCHEDULED " + this.currentunscheduledvalue/10 + "\n";
		s += "ROOMSTABILITY " + this.currentroomstabilityvalue + "\n";
		s += "ROOMCAPACITY " + this.currentroomcapacityvalue + "\n";
		s += "MINIMUMWORKINGDAYS " + this.currentMWDvalue/5 + "\n";
		s += "CURRICULUMCOMPACTNESS " + this.currentcurriculumcompactnessvalue/2 + "\n";
		s +="OBJECTIVE " + this.currentsolutionvalue + "\n";

		for(Room r : roomtab){
			TimeSlot t = new TimeSlot();
			while(t.TSUpdate()){
				if(this.currentsolution[t.TSIndex()][r.ID]!=null){
					s+= this.currentsolution[t.TSIndex()][r.ID].courseid + " " + t.currentday + " " + t.currentperiod + " "+ r.stringid + "\n";
				}
			}
		}

		return s;
	}


	public void SortCoursesToPlan(SORTING sorting){
		//enum SORTING {DONOTHING,RANDOMIZE,BYCURRICULUM,BYNBSTUDENTS,BYNBLECTURES,BYNBSTUDENTSANDRANDOM};
		CourseComparator coursecomp = new CourseComparator();
		switch(sorting)
		{
		case DONOTHING:

			break;
		case RANDOMIZE:
			Random rand = new Random();
			ArrayList<Course> templist = new ArrayList<Course>();
			while(!this.coursestoplan.isEmpty()){
				int index  = rand.nextInt(this.coursestoplan.size());
				templist.add(this.coursestoplan.get(index));
				this.coursestoplan.remove(index);
			}
			this.coursestoplan=templist;

			break;
		case BYCURRICULUM:
			Collections.sort(this.coursestoplan, coursecomp.GetComparatorCurriculum());
			break;

		case BYNBSTUDENTS:
			Collections.sort(this.coursestoplan, coursecomp.GetComparatornbstudents());
			break;

		case BYNBLECTURES:
			Collections.sort(this.coursestoplan, coursecomp.GetComparatornblectures());
			break;

		case BYNBSTUDENTSANDRANDOM:
			Collections.sort(this.coursestoplan, coursecomp.GetComparatornbstudents());
			Random rand2 = new Random();
			if(this.coursestoplan.size()>0){
				int nbmodif = rand2.nextInt(this.coursestoplan.size());
				for(int i = 0 ; i<nbmodif ; i++){
					int ind1 = rand2.nextInt(this.coursestoplan.size());
					int ind2 = rand2.nextInt(this.coursestoplan.size());
					Course temp1 = this.coursestoplan.get(ind1);
					Course temp2 = this.coursestoplan.get(ind2);
					this.coursestoplan.remove(ind1);
					this.coursestoplan.add(ind1, temp2);
					this.coursestoplan.remove(ind2);
					this.coursestoplan.add(ind2, temp1);
				}
			}
			break;
		}
	}

}
