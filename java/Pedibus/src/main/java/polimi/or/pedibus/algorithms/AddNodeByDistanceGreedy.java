package polimi.or.pedibus.algorithms;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import polimi.or.pedibus.graph.PathEnd;
import polimi.or.pedibus.model.ProblemInstance;
import polimi.or.pedibus.solution.Solution;

public class AddNodeByDistanceGreedy extends Algorithm {

	@Override
	public Solution applyTo(ProblemInstance instance) {
		Solution sol = new Solution(instance);
		// SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> g = instance.getDistanceGraph();
		
		// add nodes to solution
        Set<PathEnd> paths = new HashSet<>();
        		
        /* Iterator<Integer> iter = new ClosestFirstIterator<>(g, ProblemInstance.SCHOOL);
        iter.next(); // discard first visited node (SCHOOL)
        while (iter.hasNext()) {
            int node = iter.next(); */
        for (Integer node: instance.getIndicesSortedBySchoolDistance()){
            // addToSoution
            Optional<PathEnd> selectedPath = 
            	paths.stream()
            	.filter(p -> 
					p.pathWeight+instance.getDistance(node,p.endVertex) < instance.getAlphaConstraintFor(node))
				.sorted(Comparator.comparingDouble(p -> instance.getDistance(node,p.endVertex)))
				.findFirst();
            if (selectedPath.isPresent()){
            	PathEnd p = selectedPath.get();
            	sol = sol.setNext(node, p.endVertex);
            	p.pathWeight += instance.getDistance(node, p.endVertex);
            	p.endVertex = node;
            } else {
            	sol = sol.setNext(node, ProblemInstance.SCHOOL);
            	paths.add(
            			new PathEnd(node, instance.getDistance(node, ProblemInstance.SCHOOL)));
            }
        }
		
		return sol;
	}
	
	
}
