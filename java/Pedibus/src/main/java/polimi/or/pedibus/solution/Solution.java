package polimi.or.pedibus.solution;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import polimi.or.pedibus.graph.Path;
import polimi.or.pedibus.model.ProblemInstance;

// TODO: Auto-generated Javadoc
/**
 * The Class Solution.
 */
public class Solution {
	
	/** The next. */
	private final Map<Integer,Integer> next;
	
	/** The data. */
	private final ProblemInstance data;
	
	/**
	 * Instantiates a new solution.
	 *
	 * @param instance the instance
	 */
	public Solution(ProblemInstance instance){
		next = new HashMap<>();
		for (Integer i: instance.getNodeIndices()){
			next.put(i, null);
		}
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
		this.data = solutionToModify.data;
	}
	
	private Solution(Solution soulutionToModify, Map<Integer,Integer> newNextMap){
		this.data = soulutionToModify.data;
		this.next = newNextMap;
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
	
	public void writeToFile(String filename) throws IOException{
		List<String> lines = new ArrayList<>();
		for (Entry<Integer,Integer> e: next.entrySet()){
			// write arc as node indices, separated by a space
			lines.add(e.getKey()+" "+e.getValue());
		}
		java.nio.file.Path file = Paths.get(filename);
		Files.write(file, lines, Charset.forName("UTF-8"));
	}
	
	public List<Integer> getLeaves(){
		List<Integer> leaves = new ArrayList<>(next.keySet());
		leaves.removeAll(next.values());
		return leaves;
	}

	public double getScore() {
		DecimalFormat df = new DecimalFormat( "0.0000 ");
		double dangerValue = 0;
		
		int leaves = getLeaves().size();
		
		for (Entry<Integer,Integer> arc: next.entrySet()){
			dangerValue += data.getDanger(arc.getKey(),arc.getValue());			
		}
		
		int nNodes = next.size();
		
		double beta = 0.1;
        if(nNodes>10 && nNodes <= 100){
                    beta = 0.01;
        } else if(nNodes>100 && nNodes <= 1000){
                    beta = 0.001;
        } else if(nNodes > 1000) {
                    beta = 0.0001;
        }
		double score = leaves + (dangerValue*beta);
		return Double.parseDouble(df.format(score));
	}
	
	/**
	 * Swap 2 nodes in a path of a solution: 
	 * eg: ... -> n1 -> n2 -> ... becames
	 * 	   ... -> n2 -> n1 -> ...
	 * @param n1 a node in path whose next is n2
	 * @param n2 the follower node of n1
	 * @return the neighbour solution, in which the swap is performed
	 */
	public Solution swapInPath(int n1, int n2){
		Map<Integer, Integer> swappedNextMap = new HashMap<>(this.next);
		swappedNextMap.put(n1, next.get(n2));
		swappedNextMap.put(n2,n1);
		for (Entry<Integer,Integer> e: next.entrySet()){
			if (e.getValue()==n1){
				swappedNextMap.put(e.getKey(), n2);
				break;
			}
		}
		return new Solution(this,swappedNextMap);
	}

	public boolean isFeasible() {
		for (Path p: getPaths()){
			float length = 0.f;
			for (int i=0;i<p.size()-1;i++) {
				length += data.getDistance(p.get(i), p.get(i+1));
				if (length>data.getAlphaConstraintFor(p.get(i))){
					return false;
				}
			}
		}
		return true;
	}

	public List<Path> getPaths() {
		List<Path> paths = new ArrayList<>();
		for (int l: getLeaves()){
			Path p = new Path();
			int node = l;
			while (node!=ProblemInstance.SCHOOL) {
				p.add(0,node);
				node = next.get(node);
			}
			paths.add(p);
		}
		return paths;
	}

}
