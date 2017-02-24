package polimi.or.pedibus.algorithms;

import polimi.or.pedibus.model.ProblemInstance;
import polimi.or.pedibus.solution.Solution;

public abstract class SimulatedAnnealing extends Algorithm {
	
	// Set initial temp
    protected double INIT_TEMP;
    // Cooling rate
    protected double COOLING_RATE;
	
	@Override
	public Solution applyTo(ProblemInstance instance){
		Solution bestSol;
		Solution currentSol;
		Solution neighbourSol;
		double bestEnergy;
		double currentEnergy;
        double neighbourEnergy;
		
		// set start temperature
		double temp = INIT_TEMP;

        // Set start (and of course best) solution with
        bestSol = generateStartingSolution(instance);
        currentSol = bestSol;
        bestEnergy = getEnergy(bestSol);
        
        // Loop until system has cooled
        while (temp > 1) {
        	
        	neighbourSol = generateNeighbour(currentSol);
            
            // Get energy of solutions
            currentEnergy = getEnergy(currentSol);
            neighbourEnergy = getEnergy(neighbourSol);

            // Decide if we should accept the neighbour
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSol = neighbourSol;
                currentEnergy = neighbourEnergy;
            }

            // Keep track of the best solution found
            if (currentEnergy < bestEnergy) {
                bestSol = currentSol;
                bestEnergy = currentEnergy;
            }
            
            // Cool system
            temp *= 1-COOLING_RATE;
        }

        return bestSol;
	}
	
	// evaluate the energy of a solution (the function to minimize)
	public abstract double getEnergy(Solution sol);
	
	// return the start solution for algorithm
	// eg: randomly or using a greedy algorithm
	abstract Solution generateStartingSolution(ProblemInstance instance);
	
	// return a neighbour of the solution
	// eg: randomly or with heuristic
	abstract Solution generateNeighbour(Solution currentSol);

	// Calculate the acceptance probability (standard literature solution)
    public static double acceptanceProbability(double currentEnergy, double neighbourEnergy, double temperature) {
        // If the new solution is better, accept it
        if (neighbourEnergy < currentEnergy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((currentEnergy - neighbourEnergy) / temperature);
    }

}
