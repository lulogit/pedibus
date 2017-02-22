package polimi.or.pedibus.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

// TODO: Auto-generated Javadoc
/**
 * The Class ProblemInstance.
 * this class represents a particular instance
 * of the pedibus problem
 */
public class ProblemInstance {
	
	/** The Constant SCHOOL. */
	public static final int SCHOOL = 0;
	
	/** The nodes. */
	private final Map<Integer,Node> nodes;
	
	/** The alpha. */
	public final float alpha;
	
	/** The danger. */
	private final CostMatrix danger;
	
	/** The distances. */
	private final CostMatrix distances;
	
	/** The num nodes. */
	public final int numNodes;
	
	private final List<Integer> indices;
	 
	/**
	 * Instantiates a new problem instance.
	 * 
	 * @param nodes a map of id->node
	 * @param alpha the alpha value for the problem.
	 * @param danger the danger for each path of pair of nodes.
	 */
	private ProblemInstance(Map<Integer,Node> nodes, float alpha, CostMatrix danger) {
		this.alpha = alpha;
		this.danger = danger;
		this.nodes = nodes;
		this.distances = CostMatrix.euclideanDistances(nodes);
		numNodes = nodes.size()-1;
		indices = Collections.unmodifiableList(
				nodes.keySet().stream()
				.filter(i -> i>SCHOOL)
				.collect(Collectors.toList()));
	}
	
	
	/**
	 * Read a problem instance from a .dat file.
	 *
	 * @param filename the .dat filename (including path)
	 * @return the problem instance
	 * @throws FileNotFoundException the file not found exception
	 */
	public static ProblemInstance readFromFile(String filename) throws FileNotFoundException{
		// some parameters
		int nNodes = 0;
		float alpha = 0.f; 
		Map<Integer,Node> nodes = new HashMap<>();
		CostMatrix danger = null;
		boolean readX = false;
		boolean readY = false;
		boolean readD = false;
		BufferedReader br = new BufferedReader(
				new FileReader(filename));
		String line;
		String[] s;
		int i;
		try {
			while ((line = br.readLine()) != null) {
		        line = line // strip whitespaces
		        		.replaceAll("^[ \t]*","")
		        		.replaceAll("[ \t\n\r]*$","");
		         s = line.split("[\t ]+");
		         if (s.length>1 && "param".equals(s[0]) && "n".equals(s[1])){
	                 readX =false;;
	                 readY =false;;
	                 readD =false;;
	                 // read number of nodes
	                 nNodes = Integer.parseInt(s[s.length-1]);
	                 // generate empty nodes
	                 for (i=0;i<nNodes+1;i++){ 
	                	 nodes.put(i,new Node());
	                 }
	                 danger = new CostMatrix(nNodes+1);
		         } else if (s.length>1 && "param".equals(s[0]) && "alpha".equals(s[1])) {
		        	 readX = false;
	                 readY = false;
	                 readD = false;
	                 alpha = Float.parseFloat(s[s.length-1]);
		         } else if (s.length>1 && "param".equals(s[0]) && "coordX".equals(s[1])) {
		        	 readX = true;
	                 readY =false;
	                 readD =false;
		         } else if (s.length>1 && "param".equals(s[0]) && "coordY".equals(s[1])) {
		        	 readX = false;
	                 readY = true;
	                 readD = false;
		         } else if (s.length>1 && "param".equals(s[0]) && "d".equals(s[1])) {
		        	 readX =false;
	                 readY =false;
	                 readD = true;
		         } else if (s.length>0 && ";".equals(s[0]) || s.length<=0 ) {
		        	 readX = false;
	                 readY = false;
	                 readD = false;
		         } else {
		        	 if (readX){
		        		 i = Integer.parseInt(s[0]);
		        		 nodes.get(i).setX(Integer.parseInt(s[1]));
		        	 }
		        	 if (readY){
		        		 i = Integer.parseInt(s[0]);
		        		 nodes.get(i).setY(Integer.parseInt(s[1]));
		        	 }
		        	 if (readD && !(":=".equals(s[s.length-1]))){
		        		 for (i=0;i<nNodes+1;i++){
		        			 danger.setSetAtColumn(i,Float.parseFloat(s[i+1]));
		        		 }
		        		 danger.moveToNextRow();
		        	 }
		         }
		    }
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ProblemInstance(nodes,alpha,danger);
	}
	
	/**
	 * Gets the school 
	 * Return the school node.
	 *
	 * @return the school
	 */
	public Node getSchool(){
		return this.nodes.get(SCHOOL);
	}
	
	/**
	 * Gets the nodes except the school.
	 *
	 * @return the nodes 
	 */
	public List<Entry<Integer,Node>> getNodes(){
		return nodes.entrySet().stream()
				.filter(e -> e.getKey()>SCHOOL)
				.collect(Collectors.toList());
	}
	
	public List<Integer> getNodeIndices(){
		return indices;
	}
	
	/**
	 * Gets the distance between nodes.
	 *
	 * @param n1 a random node 
	 * @param n2 another random node
	 * @return the distance between those two nodes
	 */
	public float getDistance(int n1, int n2){
		return this.distances.at(n1, n2);
	}
	
	/**
	 * Gets the danger between nodes.
	 *
	 * @param n1 the n 1
	 * @param n2 the n 2
	 * @return the danger between those two nodes.
	 */
	public float getDanger(int n1, int n2){
		return this.danger.at(n1, n2);
	}

}
