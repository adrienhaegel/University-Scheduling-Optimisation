import java.util.Comparator;


public class CourseComparator{


	
	public  Comparator<Course> GetComparatorCurriculum(){
		
		 return new Comparator<Course>() {
	            public int compare(Course c1, Course c2) {
	                int s1 = 0;
	                int s2 = 0;
	            	for(Curriculum cur1 : c1.curriculumlist ){
	            		for(Course course1 : cur1.courselist){
	            			s1+= course1.nboflectures;
	            		}
	            	}
	            	for(Curriculum cur2 : c2.curriculumlist ){
	            		for(Course course2 : cur2.courselist){
	            			s2+= course2.nboflectures;
	            		}
	            	}
	            	return -s1+s2;
	            }
	        };
	}
	
	
	public  Comparator<Course> GetComparatornbstudents(){
		
		 return new Comparator<Course>() {
	            public int compare(Course c1, Course c2) {
	                return -c1.nbstudents + c2.nbstudents;
	            }
	        };
	}
	
	public  Comparator<Course> GetComparatornblectures(){
		
		 return new Comparator<Course>() {
	            public int compare(Course c1, Course c2) {
	                return -c1.nboflectures + c2.nboflectures;
	            }
	        };
	}

	
	

}
