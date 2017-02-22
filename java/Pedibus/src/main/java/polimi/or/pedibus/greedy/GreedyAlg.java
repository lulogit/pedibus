package polimi.or.pedibus.greedy;

import polimi.or.pedibus.model.ProblemInstance;
import polimi.or.pedibus.solution.Solution;

// TODO: Auto-generated Javadoc
/**
 * The Class GreedyAlg.
 * it is a generic algorithm
 */
abstract class GreedyAlg {
	
	/**
	 * Solve.
	 *
	 * @param instance the instance
	 * @return the solution
	 */
	public abstract Solution solve(ProblemInstance instance);
	
}
