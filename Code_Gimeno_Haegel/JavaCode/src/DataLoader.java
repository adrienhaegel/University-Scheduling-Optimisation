import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataLoader { //Loading of all the parameters

	int nbcourses;
	int nbrooms;
	int nbdays;
	int nbperiodsperday;
	int nbcurriculum;
	int nbconstraints;
	int nblecturers;

	public Lecturer lecturertab[]; //array of all lecturers
	public Curriculum curriculumtab[]; //array of all curriculum
	public Course coursetab[]; //array of all courses
	public ArrayList<Course> courselist; //list of all courses (used to generate a courselist in schedule)
	public Room roomtab[];//array of all rooms
	
	int emptysolutionvalue; //value of the objective function when the solution is empty
	int emptyMWDvalue;
	int emptyunscheduledvalue;
	
	
	public void Reset(){
		for (int i=0; i<nbcourses;i++){
			coursetab[i].ResetCourses();
		}
		for (int i=0; i<nbcurriculum;i++){
			curriculumtab[i].Reset();
		}
		this.emptysolutionvalue=0;
		this.emptyunscheduledvalue=0;
		this.emptyMWDvalue=0;
		
		for(int i=0; i<this.nbcourses;i++){
			Course c = coursetab[i];
			this.emptysolutionvalue+=10*c.nboflectures;
			this.emptysolutionvalue+=5*c.MWD;
			this.emptyMWDvalue+=5*c.MWD;
			this.emptyunscheduledvalue+=10*c.nboflectures;
		}
	}
	
	public DataLoader copy(){
		DataLoader dl = new DataLoader();
		dl.nbcourses=this.nbcourses;
		dl.nbrooms=this.nbrooms;
		dl.nbdays=this.nbdays;
		dl.nbperiodsperday=this.nbperiodsperday;
		dl.nbcurriculum=this.nbcurriculum;
		dl.nbconstraints=this.nbconstraints;
		dl.nblecturers=this.nblecturers;
		
		dl.emptysolutionvalue=this.emptysolutionvalue;
		dl.emptyMWDvalue=this.emptyMWDvalue;
		dl.emptyunscheduledvalue=this.emptyunscheduledvalue;
		
		dl.lecturertab = new Lecturer[this.nblecturers];
		for(int i=0; i<this.nblecturers;i++){
			dl.lecturertab[i]=this.lecturertab[i].copy();
		}
		
		dl.roomtab = new Room[nbrooms];
		for(int i=0;i<this.nbrooms;i++){
			dl.roomtab[i]=this.roomtab[i].copy();
		}
		
		
		dl.coursetab=new Course[nbcourses];
		for(int i=0;i<this.nbcourses;i++){
			dl.coursetab[i]=this.coursetab[i].copy();
			dl.coursetab[i].lecturer=dl.lecturertab[this.coursetab[i].lecturer.ID];
			for(Room r:this.coursetab[i].roomstability.keySet()){
				dl.coursetab[i].roomstability.put(dl.roomtab[r.ID], this.coursetab[i].roomstability.get(r));
			}
		}
		
		
		dl.curriculumtab = new Curriculum[nbcurriculum];
		for(int i=0;i<this.nbcurriculum;i++){
			dl.curriculumtab[i]=this.curriculumtab[i].copy();
			for(Course c:this.curriculumtab[i].courselist){
				dl.curriculumtab[i].courselist.add(dl.coursetab[c.ID]);
			}
		}
		
		for(int i=0;i<this.nbcourses;i++){
			for(Curriculum c: this.coursetab[i].curriculumlist){
			dl.coursetab[i].curriculumlist.add(dl.curriculumtab[c.ID]);
			}
		}

		dl.courselist =new ArrayList<Course>(); 
		for(int i=0; i<this.nbcourses;i++){
			Course c = dl.coursetab[i];
			for(int j=0;j<c.getnboflectures();j++){
				dl.courselist.add(c);
			}
		}
		
		for(int i=0; i<this.nblecturers;i++){
			for(Course c:this.lecturertab[i].teachinglist){
				dl.lecturertab[i].teachinglist.add(dl.coursetab[c.ID]);
			}
				
		}
		
		for(int i=0;i<this.nbcourses;i++){
			Course c = dl.coursetab[i];
			for(int day=0; day<this.nbdays;day++){
				for(int period=0;period<this.nbperiodsperday;period++){
					for(Curriculum cur : this.coursetab[i].Availability[day][period].curriculumlist)
					c.Availability[day][period].curriculumlist.add(dl.curriculumtab[cur.ID]);
				}
				
			}
		}
		
		
		return dl;
		
	}
	
	public DataLoader(){
		
	}
	
	public DataLoader(String path, String[] args){//file reader

		Loadbasic(path,args[0]); //Load basic.utt
		
		//Creation of the data objects
		lecturertab= new Lecturer[nblecturers];
		curriculumtab= new Curriculum[nbcurriculum];
		coursetab=new Course[nbcourses];
		roomtab=new Room[nbrooms];
		courselist = new ArrayList<Course>();
		Course.Assignnbdays(this.nbdays, this.nbperiodsperday);
		Curriculum.Assignnbdays(this.nbdays, this.nbperiodsperday);
		this.emptysolutionvalue=0;
		this.emptyunscheduledvalue=0;
		this.emptyMWDvalue=0;
		
		Loadlecturers(path,args[2]); //Load lecturers.utt
		
		Loadcurricula(path,args[4]); //Load curricula.utt
		
		Loadcourses(path,args[1]); //Load courses.utt

		Loadrelation(path,args[5]); //Load relation.utt
		
		Loadrooms(path,args[3]); //Load rooms.utt
		
		Loadunavailability(path,args[6]); //Load unavailability.utt
		
		//Defining the static properties
		TimeSlot.setperiods(this.nbdays, this.nbperiodsperday);
	
		
		//creation of the courselist
		for(int i=0; i<this.nbcourses;i++){
			Course c = coursetab[i];
			for(int j=0;j<c.getnboflectures();j++){
				courselist.add(c);
			}
		}
		//Calculation of the empty solution cost
		for(int i=0; i<this.nbcourses;i++){
			Course c = coursetab[i];
			this.emptysolutionvalue+=10*c.nboflectures;
			this.emptysolutionvalue+=5*c.MWD;
			this.emptyMWDvalue+=5*c.MWD;
			this.emptyunscheduledvalue+=10*c.nboflectures;
		}

	}

	
// Common GET methods
	public int getnbdays(){
		return this.nbdays;
	}
	
	public int getperiodsperday(){
		return this.nbperiodsperday;
	}
	
	public int getnumberofslots(){
		return this.nbperiodsperday*this.nbdays;
	}
	
	public int getnumberofrooms(){
		return this.nbrooms;
	}

	public int getnumberofcourses(){
		return this.nbcourses;
	}
	
	public int getnumberofcurriculum(){
		return this.nbcurriculum;
	}
	public Course getcourse(int i){
		return this.coursetab[i];
	}

	public Lecturer[] GetLecturerTab(){
		Lecturer[] l = new Lecturer[this.nblecturers];
		
		for(int i = 0; i<this.nblecturers;i++){
			l[i]= this.lecturertab[i].copy();
		}
		return l;
	}
	
	public Course[] GetCourseTab(){
		Course[] c = new Course[this.nbcourses];
		
		for(int i = 0; i<this.nbcourses;i++){
			c[i]= this.coursetab[i].copy();
		}
		return c;
	}
	
	
	///////   FILE READERS   //////
	
	public void Loadbasic(String path, String file){ //Load basic.utt
		File f = new File (path+file);

		try{
			FileReader fr = new FileReader (f);
			BufferedReader br = new BufferedReader (fr);
			br.readLine();
			String line = br.readLine();
			while (line != null)
			{
				String[] values=line.split(" ");
				this.nbcourses=Integer.parseInt(values[0]);
				this.nbrooms=Integer.parseInt(values[1]);
				this.nbdays=Integer.parseInt(values[2]);
				this.nbperiodsperday=Integer.parseInt(values[3]);
				this.nbcurriculum=Integer.parseInt(values[4]);
				this.nbconstraints=Integer.parseInt(values[5]);
				this.nblecturers=Integer.parseInt(values[6]);
				line = br.readLine();
			}
			br.close();
			fr.close();
		}
		catch(IOException exception)
		{
			System.out.println ("Error when opening DataLoader: " + exception.getMessage());
		}
	}

	public void Loadcourses(String path, String file){//Load courses.utt
		File f = new File (path+file);

		try{
			FileReader fr = new FileReader (f);
			BufferedReader br = new BufferedReader (fr);
			br.readLine();
			String line = br.readLine();

			while (line != null)
			{
				String[] values=line.split(" ");
				//System.out.println(Arrays.toString(values));
				int courseid = Integer.parseInt(values[0].substring(1));
				int lecturerid = Integer.parseInt(values[1].substring(1));
				
				Course course = new Course(values[0],courseid,this.lecturertab[lecturerid],Integer.parseInt(values[2]),Integer.parseInt(values[3]),Integer.parseInt(values[4]));
				this.coursetab[courseid]=course;
				line = br.readLine();
			}
			//System.out.println (Arrays.toString(coursetab));
			br.close();
			fr.close();
		}
		catch(IOException exception)
		{
			System.out.println ("Error when opening DataLoader: " + exception.getMessage());
		}
		
	}

	public void Loadcurricula(String path, String file){//Load curricula.utt
		File f = new File (path+file);

		try{
			FileReader fr = new FileReader (f);
			BufferedReader br = new BufferedReader (fr);
			br.readLine();
			String line = br.readLine();

			while (line != null)
			{
				String[] values=line.split(" ");
				int stringid = Integer.parseInt(values[0].substring(1));
				Curriculum cur = new Curriculum(values[0],stringid,Integer.parseInt(values[1]));
				this.curriculumtab[stringid]=cur;
				
				line = br.readLine();
			}
			//System.out.println ();
			br.close();
			fr.close();
		}
		catch(IOException exception)
		{
			System.out.println ("Error when opening DataLoader: " + exception.getMessage());
		}
	}

	public void Loadlecturers(String path, String file){//Load lecturers.utt
		File f = new File (path+file);

		try{
			FileReader fr = new FileReader (f);
			BufferedReader br = new BufferedReader (fr);
			br.readLine();
			String line = br.readLine();

			while (line != null)
			{
				
				int stringid = Integer.parseInt(line.substring(1));
				Lecturer lec = new Lecturer(line,stringid);
				this.lecturertab[stringid]=lec;
				
				line = br.readLine();
			}
			//System.out.println (Arrays.toString(lecturertab));
			br.close();
			fr.close();
		}
		catch(IOException exception)
		{
			System.out.println ("Error when opening DataLoader: " + exception.getMessage());
		}
	}

	public void Loadrelation(String path, String file){//Load relation.utt
		File f = new File (path+file);

		try{
			FileReader fr = new FileReader (f);
			BufferedReader br = new BufferedReader (fr);
			br.readLine();
			String line = br.readLine();

			while (line != null)
			{
				String[] values=line.split(" ");
				int curriculumid = Integer.parseInt(values[0].substring(1));
				int courseid = Integer.parseInt(values[1].substring(1));
				curriculumtab[curriculumid].AssignCourse(coursetab[courseid]);
				coursetab[courseid].AssignCurriculum(curriculumtab[curriculumid]);
				line = br.readLine();
			}

			br.close();
			fr.close();
		}
		catch(IOException exception)
		{
			System.out.println ("Error when opening DataLoader: " + exception.getMessage());
		}
	}

	public void Loadrooms(String path, String file){//Load rooms.utt
		File f = new File (path+file);

		try{
			FileReader fr = new FileReader (f);
			BufferedReader br = new BufferedReader (fr);
			br.readLine();
			String line = br.readLine();

			while (line != null)
			{
				String[] values=line.split(" ");
				int roomid = Integer.parseInt(values[0].substring(1));
				Room room = new Room(values[0],roomid,Integer.parseInt(values[1]));
				this.roomtab[roomid]=room;
				line = br.readLine();
			}

			br.close();
			fr.close();
		}
		catch(IOException exception)
		{
			System.out.println ("Error when opening DataLoader: " + exception.getMessage());
		}
	}

	public void Loadunavailability(String path, String file){//Load unavailability.utt
		File f = new File (path+file);

		try{
			FileReader fr = new FileReader (f);
			BufferedReader br = new BufferedReader (fr);
			br.readLine();
			String line = br.readLine();

			while (line != null)
			{
				String[] values=line.split(" ");
				int courseid = Integer.parseInt(values[0].substring(1));
				this.coursetab[courseid].DefineUnavailability(Integer.parseInt(values[1]), Integer.parseInt(values[2]));
				line = br.readLine();
			}

			br.close();
			fr.close();
		}
		catch(IOException exception)
		{
			System.out.println ("Error when opening DataLoader: " + exception.getMessage());
		}
	}
}
