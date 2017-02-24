package polimi.or.pedibus.algorithms;

import polimi.or.pedibus.model.ProblemInstance;
import polimi.or.pedibus.solution.Solution;

public class PedibusSA extends SimulatedAnnealing {
	
	public PedibusSA(){
		INIT_TEMP = 10000;
		COOLING_RATE = 0.003; // 0.003
	}
	
	@Override
	Solution generateStartingSolution(ProblemInstance instance) {
		return (new AddNearestNodeGreedy()).applyTo(instance);
	}
	
	@Override
	public double getEnergy(Solution sol) {
		return sol.getScore();
	}
	
	@Override
	Solution generateNeighbour(Solution sol) {
		Solution neighbour;
		// loop 'till feasibility:
		int maxTries = 3;
		do {
			// select move type
			MoveSA m = SwapConsecutiveMove.randomSwap(sol);
			// select move parameter
			
			// apply move
			neighbour = m.applyTo(sol);
		} while (maxTries-->0 && !neighbour.isFeasible());
		if (maxTries>0){
			return neighbour;
		} else {
			return sol;
		}
	}
	
	

}
