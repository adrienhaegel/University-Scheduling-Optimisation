
public class Test {

	public static void Test01(Course c){ //To verify that a course is correctly loaded
		//System.out.println(c.toString());
	}
	

	
	
	
	//!!!//Things to TEST://!!!//
	
	// ! OK ! Course Data correctly loaded
	// Curriculum Data correctly loaded
	// Lecturer Data correctly loaded
	// Room Data correctly loaded
	
	// ! OK ! Availability in Course correctly loaded
	

	//Test Assigncourse: Is everything correctly updated? Are the unavaibilities OK?
	//Assign courses that are unavailable
	// Check the WD score
	//Check the curriculum compactness score
	// Check the room stability score
	
	//AC for method AssignCourse in Course class
	/*
	public static void TestACCurriculumAvailability(Course c, TimeSlot t, Room r)
	{
		c.AssignCourse(t, r);
		//is timeslot booked for all the curriculums where c belongs?
		for(Curriculum cur:c.curriculumlist)
			for(Course course:cur.courselist)
			{
				//Status status = course.Availability[t.currentday][t.currentperiod];
				//if (status.Isavailable()) System.out.println("Error in booking time slots \n Day "+ t.nbdays+ "Period "+ t.nbperiod);
			}
		System.out.println("Curriculum Availability test passed satisfactory");
	}
	*/
	public static void TestACRoomAvailability(Course c, TimeSlot t, Room r, RoomList availablerooms){
		c.AssignCourse(t, r);
		//roomlist: is room r now available?
		if(availablerooms.roomlist.contains(r)) System.out.println("Error in booking room. Room " +r+ "should be booked");
	}
	
	public static void TestACForceUnavailability(Course c1, Course c2, TimeSlot t, Room r){
		c1.AssignCourse(t, r);
		c2.AssignCourse(t, r);
	}
	
	public static void TestACRoomScore(Course c, int currentSolutionValue){
		/*Random r;
		c.AssignCourse(t, r);
		int RoomStabilityScore;*/
		
	
	}
	
	
	
}