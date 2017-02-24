package polimi.or.pedibus.algorithms;

import polimi.or.pedibus.model.ProblemInstance;
import polimi.or.pedibus.solution.Solution;

// TODO: Auto-generated Javadoc
/**
 * The Class GreedyAlg.
 */
abstract class Algorithm {
	
	/**
	 * Solve.
	 *
	 * @param instance the instance
	 * @return the solution
	 */
	public abstract Solution applyTo(ProblemInstance instance);
	
}
