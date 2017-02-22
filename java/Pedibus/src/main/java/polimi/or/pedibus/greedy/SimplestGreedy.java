package polimi.or.pedibus.greedy;

import polimi.or.pedibus.model.ProblemInstance;
import polimi.or.pedibus.solution.Solution;

// TODO: Auto-generated Javadoc
/**
 * The Class SimplestGreedy.
 */
public class SimplestGreedy extends GreedyAlg {

	/* (non-Javadoc)
	 * @see polimi.or.pedibus.greedy.GreedyAlg#solve(polimi.or.pedibus.model.ProblemInstance)
	 */
	@Override
	public Solution solve(ProblemInstance instance) {
		Solution currentSolution = new Solution(instance);
		for (Integer i: instance.getNodeIndices()){
			currentSolution = currentSolution.setNext(i, ProblemInstance.SCHOOL);
		}
		return currentSolution;
	}

}
