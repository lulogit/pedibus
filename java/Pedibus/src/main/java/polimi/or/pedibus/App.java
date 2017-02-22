package polimi.or.pedibus;

import java.io.FileNotFoundException;
import java.io.IOException;

import polimi.or.pedibus.greedy.SimplestGreedy;
import polimi.or.pedibus.model.ProblemInstance;
import polimi.or.pedibus.solution.Solution;

// TODO: Auto-generated Javadoc
/**
 * App to run for the challenge.
 */
public class App 
{
    
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main( String[] args )
    {
    	ProblemInstance p = null;
    	if (args.length<1){
    		System.out.println("Usage: java -jar pedibus.jar <pathTo/instanceFile.dat>");
    		
    	}
        try {
			p = ProblemInstance.readFromFile(args[0]);
		} catch (FileNotFoundException e) {
			System.out.println("Impossible to read input .dat file ["+args[0]+"]");
			e.printStackTrace();
			System.exit(-1);
		}
        System.out.println("Alpha: "+p.alpha);
		System.out.println("Node number: "+p.getNodes().size());
		Solution sol = (new SimplestGreedy()).solve(p);
		String outFile = args[0].replaceAll("[.]dat$", ".sol");
		try {
			sol.writeToFile(outFile);
		} catch (IOException e) {
			System.out.println("Impossible to write output .sol file ["+outFile+"]");
			e.printStackTrace();
			System.exit(-1);
		}
    }
}