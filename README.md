# University-Scheduling-Optimisation
Optimising a scheduling problem with meta heuristics (Greedy search/ALNS)
<p>
<strong>
This project was part of a competition in DTU (Denmark Technical University), and ranked first out of 20 teams.
The goal was to optimize an university timetable. Our solution is written in Java, and implements a greedy search (GRASP, LNS and ALNS). All the parameters have been optimized for the given data. Heuristics allow to find in a few minutes a good solution to a problem that could not be solved otherwise. 
</p>
</strong>
<p>
<strong>
This project has been done by Maria Albert Gimeno and Adrien Haegel.
</strong>
</p>

<h4> Problem </h4>
The problem is described in the "Problem Description" folder.
</br>
 <br>
<h4>Presentation and report</h4>
Our report on this project is in the report folder.
All the explanations about this algorithm and analysis of results are there. 

<h4>DATA</h4>
The "DATA" folder contains the raw data, and "DATA - Final test" also contains the solutions

<h4>Code</h4>
All the code is given in "Code_Gimeno_Haegel".

The solver is "java jar\Solver.jar", and can be called using:

java -jar Solver.jar basic.utt courses.utt lecturers.utt rooms.utt curricula.utt relation.utt unavailability.utt 300 > Solution.sol

(All the utt files should be in the same folder as Solver.jar)
A batch file directly calling it is also provided in the same folder.


