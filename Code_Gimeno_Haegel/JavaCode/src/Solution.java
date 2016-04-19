import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class Solution {
	DataLoader data;
	Schedule schedule;
	String path;
	String[] args;
	
	public Solution(String path, String[] args){
		this.path=path;
		this.args =args;
		data = new DataLoader(path,args); 
		schedule=new Schedule(data);
	}
	
	public Solution(){
		
	}
	
	public void Reset(){
		this.data.Reset();
		this.schedule = new Schedule(this.data);
	}
	
	public Solution copy(){
		Solution s = new Solution();
		s.path=this.path;
		s.args=this.args;
		s.data=this.data.copy();
		s.schedule=this.schedule.ScheduleCopy(s.data);
		
		return s;
	}
	
	public void Destroy(DESTROY destroy, int[] parameters){

			this.schedule.Destroy(destroy, parameters);
		
		
	}
	
	
	public void GRASP(int windowlength, SORTING sortingtype){
		this.schedule.GRASP(windowlength,sortingtype);
	}
	
	public void FullGRASP(int windowlength){
		this.schedule.FullGRASP(windowlength);
	}
	
	public void easyGRASP(){
		this.schedule.easyGRASP();
	}
	
	public void PrintSolution(){

		System.out.println(data.emptysolutionvalue);
		System.out.println(schedule.currentsolutionvalue);
		
		String st = "";
		
		st += "UNSCHEDULED " + schedule.currentunscheduledvalue + "\n";
		st += "ROOMSTABILITY " + schedule.currentroomstabilityvalue + "\n";
		st += "ROOMCAPACITY " + schedule.currentroomcapacityvalue + "\n";
		st += "MINIMUMWORKINGDAYS " + schedule.currentMWDvalue + "\n";
		st += "CURRICULUMCOMPACTNESS " + schedule.currentcurriculumcompactnessvalue + "\n";
		st +="OBJECTIVE " + schedule.currentsolutionvalue + "\n";
		
		System.out.println(st);
		
		
		String pathfile = path+"/Solution.sol";
		/**
		 * BufferedWriter a besoin d un FileWriter, 
		 * les 2 vont ensemble, on donne comme argument le nom du fichier
		 * true signifie qu on ajoute dans le fichier (append), on ne marque pas par dessus 
		 
		 */
		try{
		FileWriter fw = new FileWriter(pathfile, false);
		
		// le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
		BufferedWriter output = new BufferedWriter(fw);
		
		//on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
		output.write(schedule.PrintSolution());
		//on peut utiliser plusieurs fois methode write
		
		output.flush();
		//ensuite flush envoie dans le fichier, ne pas oublier cette methode pour le BufferedWriter
		
		output.close();
		//et on le ferme
		System.out.println("fichier créé");
		}
		catch(IOException ioe){
			System.out.print("Erreur : ");
			ioe.printStackTrace();
			}
	

}
}
