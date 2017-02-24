package polimi.or.pedibus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import polimi.or.pedibus.algorithms.AddNodeByDistanceGreedy;
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
    	if (args.length!=1){
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
		
		//Solution sol = (new SimplestGreedy()).solve(p);
		Solution sol = (new AddNodeByDistanceGreedy()).applyTo(p);
		
		// write out solution
		Path fp = Paths.get(args[0]);
		String outFile = fp.getFileName().toString().replaceAll("[.]dat$", ".sol");
		try {
			sol.writeToFile(outFile);
		} catch (IOException e) {
			System.out.println("Impossible to write output .sol file ["+outFile+"]");
			e.printStackTrace();
			System.exit(-1);
		}
    }
}