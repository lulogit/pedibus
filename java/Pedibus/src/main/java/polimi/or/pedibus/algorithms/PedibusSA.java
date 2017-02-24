package polimi.or.pedibus.algorithms;

import polimi.or.pedibus.model.ProblemInstance;
import polimi.or.pedibus.solution.Solution;

public class PedibusSA extends SimulatedAnnealing {
	
	public PedibusSA(){
		INIT_TEMP = 10000;
		COOLING_RATE = 0.003;
	}
	
	@Override
	Solution generateStartingSolution(ProblemInstance instance) {
		return (new AddNodeByDistanceGreedy()).applyTo(instance);
	}
	
	@Override
	public double getEnergy(Solution sol) {
		return sol.getScore();
	}
	
	@Override
	Solution generateNeighbour(Solution sol) {
		return sol;
	}

}
