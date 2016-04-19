import java.util.ArrayList;

enum AS { AVAILABLE, TAKENBYTEACHER, TAKENBYCURRICULUM, TAKENBYBOTH , DATAUNAVAILABLE};
public class Status { 
	AS availability;
	ArrayList<Curriculum> curriculumlist;
	
	public Status(){
		this.availability=AS.AVAILABLE;
		curriculumlist=new ArrayList<Curriculum>();
	}

	public Status copy(){
		Status s = new Status();
		switch(this.availability)
		{
		case AVAILABLE:
			s.availability=AS.AVAILABLE;
			break;
		case TAKENBYTEACHER:
			s.availability=AS.TAKENBYTEACHER;
			break;
		case TAKENBYCURRICULUM:
			s.availability=AS.TAKENBYCURRICULUM;
			break;
		case TAKENBYBOTH:
			s.availability=AS.TAKENBYBOTH;
			break;
		case DATAUNAVAILABLE:
			s.availability=AS.DATAUNAVAILABLE;
			break;
		}
		
		
		return s;
	}
	
	public void setStatus(AS a){
		this.availability = a;
	}
	
	public AS getStatus(){
		return availability;
	}
	
	public boolean Isavailable(){
		if(this.availability==AS.AVAILABLE){
			return true;
		}
		return false;
	}
	
	public void SetUnavailable(){
		this.availability=AS.DATAUNAVAILABLE;
	}
	
	public void Reset(){
		switch(availability)
		{
		case AVAILABLE:
			
			break;
		case TAKENBYTEACHER:
			this.availability=AS.AVAILABLE;
			break;
		case TAKENBYCURRICULUM:
			this.availability=AS.AVAILABLE;
			break;
		case TAKENBYBOTH:
			this.availability=AS.AVAILABLE;
			break;
		case DATAUNAVAILABLE:
	
			break;
		}
		curriculumlist=new ArrayList<Curriculum>();
		
	}


	public void TakeTeacher(){
		switch(availability)
		{
		case AVAILABLE:
			this.availability=AS.TAKENBYTEACHER;
			break;
		case TAKENBYTEACHER:
			System.out.println("ERROR in STATUS 1!");
			break;
		case TAKENBYCURRICULUM:
			this.availability=AS.TAKENBYBOTH;
			break;
		case TAKENBYBOTH:
			System.out.println("ERROR in STATUS 2!");
			break;
		case DATAUNAVAILABLE:
	
			break;
		}
	}
	
	public void FreeTeacher(){
		switch(availability)
		{
		case AVAILABLE:
			System.out.println("ERROR in STATUS 3!");
			break;
		case TAKENBYTEACHER:
			this.availability=AS.AVAILABLE;
			break;
		case TAKENBYCURRICULUM:
			System.out.println("ERROR in STATUS 4!");
			break;
		case TAKENBYBOTH:
			this.availability=AS.TAKENBYCURRICULUM;
			break;
		case DATAUNAVAILABLE:
			
			break;
		}
	}

	public void TakeCurriculum(Curriculum c){
		switch(availability)
		{
		case AVAILABLE:
			this.availability=AS.TAKENBYCURRICULUM;
			this.curriculumlist.add(c);
			break;
		case TAKENBYTEACHER:
			this.availability=AS.TAKENBYBOTH;
			this.curriculumlist.add(c);
			break;
		case TAKENBYCURRICULUM:
			this.curriculumlist.add(c);
			break;
		case TAKENBYBOTH:
			this.curriculumlist.add(c); 
			break;
		case DATAUNAVAILABLE:
			
			break;
		}
	}
	
	public void FreeCurriculum(Curriculum c){
		switch(availability)
		{
		case AVAILABLE:
			System.out.println("ERROR in STATUS 5!");
			break;
		case TAKENBYTEACHER:
			System.out.println("ERROR in STATUS 6!");
			break;
		case TAKENBYCURRICULUM:
			int index = FindIndex(c);
			this.curriculumlist.remove(index);
			if(this.curriculumlist.isEmpty()){
				this.availability=AS.AVAILABLE;
			}
			break;
		case TAKENBYBOTH:
			int index2 = FindIndex(c);
			this.curriculumlist.remove(index2);
			if(this.curriculumlist.isEmpty()){
				this.availability=AS.TAKENBYTEACHER;
			}
			break;
		case DATAUNAVAILABLE:
			
			break;
		}
	}


	
	public String toString(){
		return this.availability.toString();
	}
	
	public int FindIndex(Curriculum c){
		for(int i=0;i<this.curriculumlist.size();i++){
			if(c.equals(this.curriculumlist.get(i))){
				return i;
			}
		}
		return -1;
	}


}
