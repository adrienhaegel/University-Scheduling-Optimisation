
public class TimeSlot {//This class represent every time slot in the schedule

	static int nbdays; //static number of days. Defined during DataLoading
	static int nbperiod; //static number of periods. Defined during DataLoading

	int currentday; //Current day for this TimeSlot Object
	int currentperiod;//Current period for this TimeSlot Object

	public TimeSlot(int day, int period){ //Constructor
		this.currentday=day;
		this.currentperiod=period;
	}

	public TimeSlot(){//override for Monday morning
		this.currentday=0;
		this.currentperiod=-1;
	}

	public TimeSlot copy(){//return a copy of this TimeSlot
		TimeSlot t = new TimeSlot(this.currentday,this.currentperiod);
		return t;
	}
	
	public static void setperiods(int days, int period){ //for static definition
		nbdays = days;
		nbperiod = period;
	}

	public int TSIndex(){//returns the index integer of this TS (Monday morning =0, Monday afternoon = 1, ..)
		return currentday*nbperiod+(currentperiod+1)-1;

	}

	public boolean TSUpdate(){//increase the TimeSlot for the next time. Returns false friday afternoon.
		if (currentperiod+1 < nbperiod){
			currentperiod+=1;
			return true;
		}
		else if (currentday+1 <nbdays) {
			currentday+=1;
			currentperiod=0;
			return true;
		} 
		else{
			return false;
		}


	}

	public String toString(){//toString
		return "Day : " + currentday + " Slot : " + currentperiod;	
	}

	public boolean equals(Object t){
		if(!(t instanceof TimeSlot)) return false;
		TimeSlot ts = (TimeSlot) t;
		if(ts.currentday==this.currentday && ts.currentperiod == this.currentperiod){
			return true;
		}
		else{
			return false;
		}
	}
	
	
   
}
