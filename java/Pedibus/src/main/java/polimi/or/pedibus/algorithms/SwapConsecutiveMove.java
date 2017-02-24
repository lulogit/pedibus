package polimi.or.pedibus.algorithms;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import polimi.or.pedibus.graph.Path;
import polimi.or.pedibus.solution.Solution;

public class SwapConsecutiveMove extends MoveSA {
	private int n1;
	private int n2;
	
	public SwapConsecutiveMove(int n1, int n2) {
		this.n1 = n1;
		this.n2 = n2;
	}
	
	@Override
	public Solution applyTo(Solution sol) {
		return sol.swapInPath(n1, n2);
	}
	
	public static SwapConsecutiveMove randomSwap(Solution sol){
		List<Path> candidatePaths = sol.getPaths().stream()
			.filter(p -> p.size()>2)
			.collect(Collectors.toList());
		Random rand = new Random();
		Path p = candidatePaths.get(rand.nextInt(candidatePaths.size()));
		int i1 = rand.nextInt(p.size()-2)+2;
		int i2 = i1 -1;
		return new SwapConsecutiveMove(p.get(i1), p.get(i2));
	}
	
}
