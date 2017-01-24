import sys
import os.path
from utils import readDatFile, writeSolution
from graph import PediGraph
from solution import Solution
from dominance import forcedArcs
from paths import allPaths
from itertools import islice, izip
from kmeansExample import main

# PROBLEM CONSTANTS


def solve(numNodes, alpha, coordX, coordY, danger):
    '''
        solve the pedibus problem and return the solution as arc iterator
    '''
    bestSolution = None
    # create a graph instance from the variables read from .dat file
    graph = PediGraph(numNodes, coordX,coordY, danger, alpha)
    points = [[x,y] for x,y in izip(coordX,coordY)]
    main(points,numNodes / 5)
    #for n in islice(graph.iternodes(), 2):
    #    for p in allPaths(graph, n, graph.school, graph.distance(n,graph.school)*1.001):
    #        print p
    # start building a solution
    currentSolution = Solution()
    currentSolution = Solution(currentSolution,forcedArcs(graph))
    print "Graph has |arcs|=",graph.numArcs()
    print "Solution has |arcs|=",currentSolution.numArcs()
    return currentSolution.iterarcs()

if __name__ == "__main__":
    if len(sys.argv)!=2:
        print "Usage:\n python solver.py <instance.dat>"
        sys.exit(0)
    else:
        # LOAD PROBLEM INSTANCE DATA
        dataFile = sys.argv[1]
        filename, file_extension = os.path.splitext(dataFile)
        dirName = os.path.dirname(filename)
        filename = os.path.basename(filename)
        if (os.path.isfile(dataFile) and file_extension == ".dat"):
            nNodes, alpha, coordX,coordY, danger = readDatFile(dataFile)
            # SOLVE PROBLEM
            solution = solve(nNodes, alpha, coordX,coordY, danger)
            # WRITE SOLUTION TO FILE
            writeSolution(solution, os.path.join(dirName,filename+".sol"))
            # PRINT SOLUTION
            #with open(os.path.join(dirName,filename+".sol"),'r') as sol:
            #    print sol.read()
        else:
            if (os.path.isfile(dataFile)):
                print "Wrong input file type ("+dataFile+")."
            else:
                print "Cannot read input file"+dataFile+"."
            print "Usage:\n python solver.py <instance.dat>"
            sys.exit(1)
