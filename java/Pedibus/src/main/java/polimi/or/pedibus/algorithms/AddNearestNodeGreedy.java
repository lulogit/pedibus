package polimi.or.pedibus.algorithms;

import java.util.HashMap;

import polimi.or.pedibus.graph.PathEnd;
import polimi.or.pedibus.model.ProblemInstance;
import polimi.or.pedibus.solution.Solution;

public class AddNearestNodeGreedy extends Algorithm {

	@Override
	public Solution applyTo(ProblemInstance instance) {
		// init solution and path tree
		Solution sol = new Solution(instance);
		HashMap<Integer,PathEnd> tree = new HashMap<>();;
		for (Integer i: instance.getNodeIndices()){
			tree.put(i, null);
		}
		
		for (Integer node: instance.getIndicesSortedBySchoolDistance()){
			if (tree.get(node) == null){
				tree.put(node, new PathEnd(0,instance.getDistance(node, ProblemInstance.SCHOOL)));
				tree = DFSpro(node, tree, instance);
			}
		}
		
		for (Integer i: instance.getNodeIndices()){
			sol = sol.setNext(i, tree.get(i).endVertex);
		}
		return sol;
	}
	
	public HashMap<Integer, PathEnd> DFSpro(int root, HashMap<Integer,PathEnd> tree, 
			ProblemInstance instance){
		
		int newRoot = root;
		for (Integer node: instance.getIndicesSortedByNodeDistance(root)){
			if (tree.get(node) == null){
				float newDistance = 
						tree.get(root).pathWeight + instance.getDistance(root,node);
				if ( newDistance < instance.getAlphaConstraintFor(node) ){
					tree.put(node,new PathEnd(root,newDistance));
					newRoot = node;
					break;
				}
			}
		}
		if ( newRoot != root){
			return DFSpro(newRoot, tree, instance);
		} else {
			return tree;
		}
	}

}
