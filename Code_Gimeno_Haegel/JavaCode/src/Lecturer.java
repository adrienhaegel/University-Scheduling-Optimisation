import java.util.ArrayList;
import java.util.List;

public class Lecturer {
	String lecturerid;
	int ID;
	
	List<Course> teachinglist;//List of courses taught
	
	public Lecturer(String stringid, int id){//Constructor
		this.lecturerid=stringid;
		this.ID=id;
		teachinglist=new ArrayList<Course>();
		//System.out.println(this);
	}
	
	public void AssignCourse(Course c){ //add a course to the lecturer duty
		teachinglist.add(c);
	}
	
	public String toString(){//tostring
		return "Lecturer : " + this.lecturerid + " ID :" + ID ; 
	}
	
	public Lecturer copy(){
		Lecturer l = new Lecturer(this.lecturerid, this.ID);
		l.teachinglist=new ArrayList<Course>();
		return l;
	}
	
	
}
