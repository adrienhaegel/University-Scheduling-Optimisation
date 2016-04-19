# University-Scheduling-Optimisation
Optimising a scheduling problem with meta heuristics (Greedy search/ALNS)

<strong>
This project was part of a competition in DTU (Denmark Technical University), and ranked first our of 20 teams.
The goal was to optimize an university timetable. Our solution is written in Java, and implements a greedy search. All the parameters have been optimized for the given data. Heuristics allow to find in a few minutes a good solution to a problem that could not be solved otherwise. This project has been done by Maria Albert Gimeno and Adrien Haegel.
</strong>

*** Problem ***
The problem is described in the "Problem Description" folder.

*** Presentation and report ***
Our report on this project is in the report folder.
All the explanations about this algorithm and analysis of results are there. 

*** DATA ***
The "DATA" folder contains the raw data, and "DATA - Final test" also contains the solutions

*** Code ***
All the code is given in "Code_Gimeno_Haegel".

The solver is "java jar\Solver.jar", and can be called using:

java -jar Solver.jar basic.utt courses.utt lecturers.utt rooms.utt curricula.utt relation.utt unavailability.utt 300 > Solution.sol

(All the utt files should be in the same folder as Solver.jar)
A batch file directly calling it is also provided in the same folder.


