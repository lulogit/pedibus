package polimi.or.pedibus.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.VertexFactory;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

// TODO: Auto-generated Javadoc
/**
 * The Class ProblemInstance.
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
	 * @param nodes the nodes
	 * @param alpha the alpha
	 * @param danger the danger
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
		        		 for (int j=0;j<s.length;j+=2){
			        		 i = Integer.parseInt(s[j+0]);
			        		 nodes.get(i).setX(Integer.parseInt(s[j+1]));
		        		 }
		        	 }
		        	 if (readY){
		        		 for (int j=0;j<s.length;j+=2){
			        		 i = Integer.parseInt(s[j+0]);
			        		 nodes.get(i).setY(Integer.parseInt(s[j+1]));
		        		 }
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
	 * Gets the school.
	 *
	 * @return the school
	 */
	public Node getSchool(){
		return this.nodes.get(SCHOOL);
	}
	
	/**
	 * Gets the nodes.
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
	 * Gets the distance.
	 *
	 * @param n1 the n 1
	 * @param n2 the n 2
	 * @return the distance
	 */
	public float getDistance(int n1, int n2){
		return this.distances.at(n1, n2);
	}
	
	/**
	 * Gets the danger.
	 *
	 * @param n1 the n 1
	 * @param n2 the n 2
	 * @return the danger
	 */
	public float getDanger(int n1, int n2){
		return this.danger.at(n1, n2);
	}
	
	public SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> getDistanceGraph(){
		
		// undirected weighted graph
		SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> g = 
				new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		CompleteGraphGenerator<Integer, DefaultWeightedEdge> completeGenerator =
	            new CompleteGraphGenerator<>(numNodes+1);
		
		// vertex are numbers 0..numNodes
        VertexFactory<Integer> vFactory = new VertexFactory<Integer>()
        {
            private int id = 0;

            @Override
            public Integer createVertex()
            {
                return id++;
            }
        };

        // Use the CompleteGraphGenerator object to make completeGraph a
        // complete graph with [numNodes+1] number of vertices
        completeGenerator.generateGraph(g, vFactory, null);

        // remove edges exiting from SCHOOL,
        // otherwise set edge weight to the distance
        Set<Integer> vertices = g.vertexSet();
        DefaultWeightedEdge e;
        for (int i: vertices){
        	for (int j: vertices){
        		if (i!=j){
        			e = g.getEdge(i, j);
        			if (i==SCHOOL){
	        			//g.removeEdge(e);
        				g.setEdgeWeight(e, getDistance(i, j));
	        		} else {
	        			g.setEdgeWeight(e, getDistance(i, j));
	        		}
        		}
        	}
        }
        
		return g;
	}
	
	public float getAlphaConstraintFor(int node){
		return alpha * getDistance(node, SCHOOL);
	}
	
	public List<Integer> getIndicesSortedBySchoolDistance(){
		return this.indices.stream()
				.sorted(Comparator.comparingDouble(i -> getDistance(i, SCHOOL)))
				.collect(Collectors.toList());
	}
	
	public List<Integer> getIndicesSortedByNodeDistance(int node){
		return this.indices.stream()
				.sorted(Comparator.comparingDouble(i -> getDistance(i, node)))
				.collect(Collectors.toList());
	}
	

}
