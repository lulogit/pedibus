package polimi.or.pedibus;

import java.io.FileNotFoundException;

import polimi.or.pedibus.model.ProblemInstance;

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
    	if (args.length<1){
    		System.out.println("Usage: java -jar pedibus.jar <pathTo/instanceFile.dat>");
    		System.exit(-1);
    	}
        try {
			ProblemInstance p = ProblemInstance.readFromFile(args[0]);
			System.out.println("Alpha: "+p.alpha);
			System.out.println("Node number: "+p.getNodes().size());
		} catch (FileNotFoundException e) {
			System.out.println("Impossible to read input .dat file ["+args[0]+"]");
			e.printStackTrace();
		}
    }
}