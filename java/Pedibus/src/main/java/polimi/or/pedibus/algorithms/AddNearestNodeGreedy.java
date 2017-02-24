package polimi.or.pedibus.algorithms;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import polimi.or.pedibus.graph.PathEnd;
import polimi.or.pedibus.model.ProblemInstance;
import polimi.or.pedibus.solution.Solution;

public class AddNearestNodeGreedy extends Algorithm {

	@Override
	public Solution applyTo(ProblemInstance instance) {
		Solution sol = new Solution(instance);
		
		SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> g = 
				instance.getDistanceGraph();
		
		// keep track of the current explored path
        /*PathEnd currPath = new PathEnd(0, 0.f);
        Set<Integer> visited = new HashSet<>();
        DijkstraShortestPath<V, E>
        
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
        }*/
		
		return sol;
	}

}
