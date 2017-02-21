import array
import random

import numpy

from deap import algorithms
from deap import base
from deap import creator
from deap import tools
import time
import multiprocessing as mp


import sys
import os.path
from itertools import islice, izip
from utils import *

def main(numNodes, alpha, coordX, coordY, dangerMatrix):
    '''
        solve the pedibus problem and return the solution as arc iterator
    '''
    # compute distance matrix
    nodes = set(xrange(numNodes+1))
    dist = {
            # dict (i,j) -> euclidean_distance(i,j)
            (i,j): float("{0:.4f}".format(((coordX[i]-coordX[j])**2 + (coordY[i]-coordY[j])**2)**0.5))
            for i in nodes
            for j in nodes}
    constraint = {
            node: alpha * dist[(node,0)]
            for node in nodes}
    danger = { # reshape from matrix to dict for faster lookup
            (i,j): dangerMatrix[i][j]
            for i in nodes
            for j in nodes}
    distanceMatrix, dangerMatrix, constraintDict = dist, danger, constraint

    '''
        use genetic algorithm to solve the pedibus problem
    '''

    creator.create("FitnessMin", base.Fitness, weights=(-1.0,))
    creator.create("Individual", array.array, typecode='i', fitness=creator.FitnessMin)

    toolbox = base.Toolbox()

    pedibusStops = nodes.difference({0}) # do not assign the school
    IND_SIZE = len(pedibusStops)

    # Attribute generator
    toolbox.register("indices", random.sample, range(IND_SIZE), IND_SIZE)

    # Structure initializers
    toolbox.register("individual", tools.initIterate, creator.Individual, toolbox.indices)
    toolbox.register("population", tools.initRepeat, list, toolbox.individual)

    # compute the danger weight
    beta = 0.1
    if IND_SIZE > 10 and IND_SIZE <= 100:
        beta = 0.01
    elif IND_SIZE > 100:
        beta = 0.001

    def evalPedibus(individual):
        totDanger = 0.
        totLeaves = 0
        pathDistance = 0.
        prevNode = 0 # start from school
        for node in individual: # add all nodes to solution following order
            # add to previous node, if not violates alpha constraint
            node += 1
            l = distanceMatrix[(prevNode,node)]
            if pathDistance + l < constraintDict[node]:
                pathDistance += l
                totDanger += dangerMatrix[(node,prevNode)]
                prevNode = node
            else:
                pathDistance = distanceMatrix[(node,0)]
                totDanger += dangerMatrix[(node,0)]
                prevNode = node
                totLeaves += 1
        totLeaves += 1
        return round(totLeaves+(totDanger*beta),4), # oss: is a tuple

    toolbox.register("mate", tools.cxOrdered)
    toolbox.register("mutate", tools.mutShuffleIndexes, indpb=0.05)
    toolbox.register("select", tools.selTournament, tournsize=3)
    toolbox.register("evaluate", evalPedibus)


    # run over multiple CPUs
    #pool = mp.Pool()
    #toolbox.register("map", pool.map)


    random.seed(time.time())

    pop = toolbox.population(n=300)

    # use multiple CPUs
    #toolbox.register("map", futures.map)

    hof = tools.HallOfFame(1)
    stats = tools.Statistics(lambda ind: ind.fitness.values)
    #stats.register("avg", numpy.mean)
    #stats.register("std", numpy.std)
    stats.register("min", numpy.min)
    #stats.register("max", numpy.max)

    algorithms.eaSimple(pop, toolbox, 0.9, 0.05, 2000, stats=stats,
                        halloffame=hof, verbose = True)
    print hof[0]
    # transform best solution to arc sequence
    arcs = []
    pathDistance = 0.
    prevNode = 0 # start from school
    for node in hof[0]: # add all nodes to solution following order
        # add to previous node, if not violates alpha constraint
        node += 1
        l = distanceMatrix[(node,prevNode)]
        print pathDistance, prevNode, node, l, constraintDict[node]
        if pathDistance + l <= constraintDict[node]:
            pathDistance += l
            arcs.append((node,prevNode))
            prevNode = node
        else:
            pathDistance = distanceMatrix[(node,0)]
            arcs.append((node,0))
            prevNode = node
    print arcs
    return arcs


if __name__ == "__main__":
    if len(sys.argv)!=2:
        print "Usage:\n python %s <instance.dat>" % (sys.argv[0])
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
            solution = main(nNodes, alpha, coordX,coordY, danger)
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
            print "Usage:\n python %s <instance.dat>" % (sys.argv[0])
            sys.exit(1)

