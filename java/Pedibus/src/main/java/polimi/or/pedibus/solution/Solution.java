package polimi.or.pedibus.solution;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import polimi.or.pedibus.model.Node;
import polimi.or.pedibus.model.ProblemInstance;

// TODO: Auto-generated Javadoc
/**
 * The Class Solution.
 */
public class Solution {
	
	/** The next. */
	private final Map<Integer,Integer> next;
	
	/** The tot danger. */
	private final float totDanger;
	
	/** The num leaves. */
	private final int numLeaves;
	
	/** The data. */
	private final ProblemInstance data;
	
	/**
	 * Instantiates a new solution.
	 *
	 * @param instance the instance
	 */
	public Solution(ProblemInstance instance){
		next = new HashMap<>();
		for (Entry<Integer,Node> e: instance.getNodes()){
			next.put(e.getKey(), null);
		}
		totDanger = 0.f;
		numLeaves = 0;
		this.data = instance;
	}
	
	/**
	 * Instantiates a new solution.
	 *
	 * @param solutionToModify the solution to modify
	 * @param nodeToAssign the node to assign
	 * @param nextValueForAssignment the next value for assignment
	 */
	private Solution(Solution solutionToModify, Integer nodeToAssign, Integer nextValueForAssignment){
		this.next = new HashMap<>(solutionToModify.next);
		this.next.put(nodeToAssign, nextValueForAssignment);
		// TODO: recompute totDanger and numLeaves
		totDanger = solutionToModify.totDanger;
		numLeaves = solutionToModify.numLeaves;
		this.data = solutionToModify.data;
	}
	
	/**
	 * Sets the next.
	 *
	 * @param node the node
	 * @param nextNode the next node
	 * @return the solution
	 */
	public Solution setNext(Integer node, Integer nextNode){
		return new Solution(this, node, nextNode);
	}

}
