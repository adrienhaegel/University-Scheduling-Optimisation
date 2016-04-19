import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


public class Solver { //Main function

	public static int niter;

	//THINGS TO TEST:
	//windowlength from 1 to 20
	//Sorting : see SORTING in Schedule
	//add positive solutions, only select negative solutions in Bestsolutions

	public static void main(String[] args) {
		int nbtrials = 5;
		String stats = "";
		String stvalues="";
		String stiter = "";
		String path;
		Solution InitSolution;




		double[] lambdavalues = new double[]{0.0025};
		double[] scalevalues = new double[]{500};
		double[] selectvalues = new double[]{1.025};

					for(int i=1;i<14;i++){

						path ="DATA/Test0"+i+"/"; //DATA path
						InitSolution = new Solution(path,args);
						stats+="Test 0"+i+"  " + "InitialValue : " + InitSolution.schedule.currentsolutionvalue + "\n";
						stvalues = "Values ";
						stiter = "Niter ";
						System.out.println("Test n " + i);	
						for(int j=0;j<nbtrials;j++){
							//Solution BestSolution=GRASPtest(InitSolution,1*Integer.parseInt(args[7]),1,SORTING.BYNBSTUDENTSANDRANDOM); 
							//Solution BestSolution=FullGRASPtest(InitSolution,100*Integer.parseInt(args[7]),1); 
							//Solution BestSolution=LNS(InitSolution,200*Integer.parseInt(args[7]),fullparam,4,randomparam); 

							Solution BestSolution = ALNSSelect(InitSolution,1000*Integer.parseInt(args[7]),lambdavalues[0],100,scalevalues[0],selectvalues[0]);

							stvalues +=BestSolution.schedule.currentsolutionvalue + " ";
							stiter+= niter + " ";
							BestSolution.PrintSolution();
						}
						stats += stvalues + "\n" + stiter + "\n"+"\n"+"\n";

					}
					

		
	}


	public static Solution GRASPtest(Solution InitSolution, int timelength, int windowlength, SORTING sortingtype){

		niter=0;
		long starttime =System.currentTimeMillis();
		int min = 10000;
		Solution BestSolution=InitSolution;
		while(System.currentTimeMillis()-starttime < timelength){
			Solution s = InitSolution.copy();
			s.GRASP(windowlength,sortingtype);
			niter+=1;
			if(s.schedule.currentsolutionvalue<min){
				min=s.schedule.currentsolutionvalue;
				BestSolution=s.copy();

			}
		}

		return BestSolution;
	}

	public static Solution FullGRASPtest(Solution InitSolution, int timelength, int windowlength){

		niter=0;
		long starttime =System.currentTimeMillis();
		int min = 10000;
		Solution BestSolution=InitSolution;
		while(System.currentTimeMillis()-starttime < timelength){
			Solution s = InitSolution.copy();
			s.FullGRASP(windowlength);
			niter+=1;
			if(s.schedule.currentsolutionvalue<min){
				min=s.schedule.currentsolutionvalue;
				BestSolution=s.copy();

			}
		}

		return BestSolution;
	}


	public static Solution LNS(Solution InitSolution, int timelength, int windowlength, int typedestroy, int param){
		niter=0;
		long starttime =System.currentTimeMillis();
		int min = InitSolution.schedule.currentsolutionvalue;
		Solution BestSolution=InitSolution;
		Solution s = InitSolution.copy();
		s.GRASP(windowlength,SORTING.BYNBSTUDENTSANDRANDOM);
		while(System.currentTimeMillis()-starttime < timelength){
			niter+=1;
			int[] parameters = new int[1];
			parameters[0]=param;

			s.Destroy(DESTROY.values()[typedestroy], parameters );
			//s.GRASP(windowlength,SORTING.BYNBSTUDENTSANDRANDOM);

			s.FullGRASP(windowlength);
			if(s.schedule.currentsolutionvalue<min){
				min=s.schedule.currentsolutionvalue;
				BestSolution=s.copy();
				//System.out.println(BestSolution.schedule.currentsolutionvalue);

			}
		}

		return BestSolution;
	}


	public static Solution easyGRASPtest(Solution InitSolution, int timelength){

		niter=0;
		long starttime =System.currentTimeMillis();
		int min = 10000;
		Solution BestSolution=InitSolution;
		while(System.currentTimeMillis()-starttime < timelength){
			Solution s = InitSolution.copy();
			s.easyGRASP();
			niter+=1;
			if(s.schedule.currentsolutionvalue<min){
				min=s.schedule.currentsolutionvalue;
				BestSolution=s.copy();

			}
		}

		return BestSolution;
	}

	public static Solution ALNSFull(Solution InitSolution, int timelength){
		niter=0;
		long starttime =System.currentTimeMillis();
		int min = 100000;
		Solution BestSolution=InitSolution;
		Solution s = InitSolution.copy();

		s.GRASP(1,SORTING.BYNBSTUDENTSANDRANDOM);

		double[] rewardvalues = new double[]{100000.0,5000.0,1000.0,0};
		int reward;

		double[] repairvalues = new double[6];
		double[] repairproba ;
		double[] destroyvalues = new double[12];
		double[] destroyproba ;

		for(int i=0;i<repairvalues.length;i++){
			repairvalues[i]=1;
		}

		for(int i=0;i<destroyvalues.length;i++){
			destroyvalues[i]=1;
		}

		Random rand = new Random();

		while(System.currentTimeMillis()-starttime < timelength){
			niter+=1;

			Solution oldsolution = s.copy();

			repairproba = CreateProbaArray(repairvalues);
			destroyproba = CreateProbaArray(destroyvalues);

			int destroymethod = GetALNSMethod(destroyproba, rand.nextFloat());

			switch(destroymethod){

			case 0:
				s.Destroy(DESTROY.DESTROYCOURSE, new int[]{1} );
				break;
			case 1:
				s.Destroy(DESTROY.DESTROYCOURSE, new int[]{2} );
				break;
			case 2:
				s.Destroy(DESTROY.DESTROYCOURSE, new int[]{3} );
				break;
			case 3:
				s.Destroy(DESTROY.DESTROYCOURSE, new int[]{4} );
				break;
			case 4:
				s.Destroy(DESTROY.DESTROYROOM, new int[]{1} );
				break;
			case 5:
				s.Destroy(DESTROY.DESTROYROOM, new int[]{2} );
				break;
			case 6:
				s.Destroy(DESTROY.DESTROYROOM, new int[]{3} );
				break;
			case 7:
				s.Destroy(DESTROY.DESTROYROOM, new int[]{4} );
				break;
			case 8:
				s.Destroy(DESTROY.DESTROYTS, new int[]{1} );
				break;
			case 9:
				s.Destroy(DESTROY.DESTROYTS, new int[]{2} );
				break;			
			case 10:
				s.Destroy(DESTROY.DESTROYTS, new int[]{3} );
				break;
			case 11:
				s.Destroy(DESTROY.DESTROYTS, new int[]{4} );
				break;
			}

			int repairmethod = GetALNSMethod(repairproba, rand.nextFloat());



			switch(repairmethod){

			case 0:
				s.GRASP(1,SORTING.BYNBSTUDENTSANDRANDOM);
				break;
			case 1:
				s.GRASP(2,SORTING.BYNBSTUDENTSANDRANDOM);
				break;
			case 2:
				s.GRASP(3,SORTING.BYNBSTUDENTSANDRANDOM);
				break;
			case 3:
				s.FullGRASP(1);
				break;
			case 4:
				s.FullGRASP(2);
				break;
			case 5:
				s.FullGRASP(3);
				break;
			}

			if(s.schedule.currentsolutionvalue< Math.max(oldsolution.schedule.currentsolutionvalue*1.02, 5)){
				if(s.schedule.currentsolutionvalue< oldsolution.schedule.currentsolutionvalue){
					if(s.schedule.currentsolutionvalue<min){
						min=s.schedule.currentsolutionvalue;
						BestSolution=s.copy();
						//System.out.println(BestSolution.schedule.currentsolutionvalue);
						reward=0;
					}
					else{
						reward=1;
					}
				}
				else{
					reward=2;
				}
			}
			else{
				reward =3;
				s=oldsolution.copy();
			}

			repairvalues[repairmethod] = 0.999* repairvalues[repairmethod]+ 0.001 * rewardvalues[reward] ;
			destroyvalues[destroymethod] = 0.999* destroyvalues[destroymethod]+ 0.001 * rewardvalues[reward] ;


		}
		return BestSolution;
	}

	public static Solution ALNSSelect(Solution InitSolution, int timelength, double lambda, double minreward, double scalefactor, double acceptvalue){
		niter=0;
		long starttime =System.currentTimeMillis();
		int min = InitSolution.schedule.currentsolutionvalue;
		Solution BestSolution=InitSolution;
		Solution s = InitSolution.copy();

		s.GRASP(1,SORTING.BYNBSTUDENTSANDRANDOM);

		double[] rewardvalues = new double[]{scalefactor*scalefactor*minreward,scalefactor*minreward,minreward,0};
		int reward;

		double[] repairvalues = new double[5];
		double[] repairproba ;
		double[] destroyvalues = new double[5];
		double[] destroyproba ;

		for(int i=0;i<repairvalues.length;i++){
			repairvalues[i]=1;
		}

		for(int i=0;i<destroyvalues.length;i++){
			destroyvalues[i]=1;
		}

		Random rand = new Random();

		while(System.currentTimeMillis()-starttime < timelength){
			niter+=1;

			Solution oldsolution = s.copy();

			repairproba = CreateProbaArray(repairvalues);
			destroyproba = CreateProbaArray(destroyvalues);

			int destroymethod = GetALNSMethod(destroyproba, rand.nextFloat());

			switch(destroymethod){

			case 0:
				s.Destroy(DESTROY.DESTROYCOURSE, new int[]{3} );
				break;
			case 1:
				s.Destroy(DESTROY.DESTROYROOM, new int[]{2} );
				break;
			case 2:
				s.Destroy(DESTROY.DESTROYTS, new int[]{1} );
				break;
			case 3:
				s.Destroy(DESTROY.DESTROYTS, new int[]{2} );
				break;			
			case 4:
				s.Destroy(DESTROY.DESTROYTS, new int[]{3} );
				break;
			}

			int repairmethod = GetALNSMethod(repairproba, rand.nextFloat());



			switch(repairmethod){

			case 0:
				s.GRASP(1,SORTING.BYNBSTUDENTSANDRANDOM);
				break;
			case 1:
				s.GRASP(2,SORTING.BYNBSTUDENTSANDRANDOM);
				break;
			case 2:
				s.FullGRASP(1);
				break;
			case 3:
				s.FullGRASP(2);
				break;
			case 4:
				s.FullGRASP(3);
				break;
			}

			if(s.schedule.currentsolutionvalue<(oldsolution.schedule.currentsolutionvalue*acceptvalue)){
				if(s.schedule.currentsolutionvalue< oldsolution.schedule.currentsolutionvalue){
					if(s.schedule.currentsolutionvalue<min){
						min=s.schedule.currentsolutionvalue;
						BestSolution=s.copy();
						//System.out.println(BestSolution.schedule.currentsolutionvalue);
						reward=0;
					}
					else{
						reward=1;
					}
				}
				else{
					reward=2;
				}
			}
			else{
				reward =3;
				s=oldsolution.copy();
			}

			repairvalues[repairmethod] = (1-lambda)* repairvalues[repairmethod]+ lambda * rewardvalues[reward] ;
			destroyvalues[destroymethod] = (1-lambda)* destroyvalues[destroymethod]+ lambda * rewardvalues[reward] ;



		}


		String desstring = "";
		String repstring = "";
		for(int i=0;i<destroyvalues.length;i++){
			desstring+=destroyvalues[i] + " , ";
		}
		for(int i=0;i<repairvalues.length;i++){
			repstring+=repairvalues[i]+ " , ";
		}
		desstring += "\n";
		repstring += "\n";
		System.out.println(desstring);
		System.out.println(repstring);

		return BestSolution;
	}


	public static Solution ALNS(Solution InitSolution, int timelength){
		niter=0;
		long starttime =System.currentTimeMillis();
		int min = InitSolution.schedule.currentsolutionvalue;
		Solution BestSolution=InitSolution;
		Solution s = InitSolution.copy();

		s.GRASP(1,SORTING.BYNBSTUDENTSANDRANDOM);

		double[] rewardvalues = new double[]{100000.0,5000.0,1000.0,0};
		int reward;

		double[] repairvalues = new double[5];
		double[] repairproba ;
		double[] destroyvalues = new double[5];
		double[] destroyproba ;

		for(int i=0;i<repairvalues.length;i++){
			repairvalues[i]=1;
		}

		for(int i=0;i<destroyvalues.length;i++){
			destroyvalues[i]=1;
		}

		Random rand = new Random();

		while(System.currentTimeMillis()-starttime < timelength){
			niter+=1;

			Solution oldsolution = s.copy();

			destroyproba = CreateProbaArray(destroyvalues);

			int destroymethod = GetALNSMethod(destroyproba, rand.nextFloat());

			switch(destroymethod){

			case 0:
				s.Destroy(DESTROY.DESTROYCOURSE, new int[]{3} );
				s.FullGRASP(1);
				break;
			case 1:
				s.Destroy(DESTROY.DESTROYROOM, new int[]{2} );
				s.FullGRASP(1);
				break;
			case 2:
				s.Destroy(DESTROY.DESTROYTS, new int[]{3} );
				s.FullGRASP(1);
				break;
			case 3:
				s.Destroy(DESTROY.DESTROYTS, new int[]{1} );
				s.GRASP(1,SORTING.BYNBSTUDENTSANDRANDOM);
				break;			
			case 4:
				s.Destroy(DESTROY.DESTROYTS, new int[]{3} );
				s.FullGRASP(2);
				break;
			}



			if(s.schedule.currentsolutionvalue< Math.max(oldsolution.schedule.currentsolutionvalue*1.02, 5)){
				if(s.schedule.currentsolutionvalue< oldsolution.schedule.currentsolutionvalue){
					if(s.schedule.currentsolutionvalue<min){
						min=s.schedule.currentsolutionvalue;
						BestSolution=s.copy();
						//System.out.println(BestSolution.schedule.currentsolutionvalue);
						reward=0;
					}
					else{
						reward=1;
					}
				}
				else{
					reward=2;
				}
			}
			else{
				reward =3;
				s=oldsolution.copy();
			}


			destroyvalues[destroymethod] = 0.999* destroyvalues[destroymethod]+ 0.001 * rewardvalues[reward] ;



		}


		String desstring = "";
		String repstring = "";
		for(int i=0;i<destroyvalues.length;i++){
			desstring+=destroyvalues[i] + " , ";
		}
		for(int i=0;i<repairvalues.length;i++){
			repstring+=repairvalues[i]+ " , ";
		}
		desstring += "\n";
		repstring += "\n";
		System.out.println(desstring);
		System.out.println(repstring);

		return BestSolution;
	}

	public static double[] CreateProbaArray(double[] values){
		double[] proba = new double[values.length];

		for(int i=0;i<values.length;i++){
			proba[i]=0;
		}
		double sum =0;

		for(int i=0;i<values.length;i++){
			for(int j=0;j<=i;j++){
				proba[i]+=values[j];
			}
			sum+=values[i];
		}
		for(int i=0;i<values.length;i++){
			proba[i]=proba[i]/sum;
		}

		return proba;
	}

	public static int GetALNSMethod(double[] proba, float f){
		for(int j=0;j<proba.length;j++){
			if(f<proba[j]){
				return j;
			}
		}
		return -1;

	}

	public static void PrintStats(String s, String name){
		String pathfile = "DATA/Stats_"+name+".txt";
		/**
		 * BufferedWriter a besoin d un FileWriter, 
		 * les 2 vont ensemble, on donne comme argument le nom du fichier
		 * true signifie qu on ajoute dans le fichier (append), on ne marque pas par dessus 

		 */
		try{
			FileWriter fw = new FileWriter(pathfile, true);

			// le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
			BufferedWriter output = new BufferedWriter(fw);

			//on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
			output.write(s);
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



	public static void PrintSolution(Schedule s, String path){

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
			output.write(s.PrintSolution());
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
