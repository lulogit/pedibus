from itertools import izip
from math import sqrt

def pairwise(iterable):
    '''
        iterate over an iterable and return an iterator yielding
        (i,i) for each element i in the iterable.
    '''
    a = iter(iterable)
    return izip(a, a)

def readDatFile(filename):
    '''
        read a .dat AMPL file into the correct variables, and compute arc weights for pedibus problem
    '''
    nNodes = 0
    alpha = 0
    coordX = []
    coordY = []
    costs = []
    danger = []
    readX = False
    readY = False
    readD = False
    with open(filename, 'r') as f:
        for line in f:
            line = line.strip()
            s = line.split()
            if len(s)>1 and s[0] == "param" and s[1] == "n":
                readX = False
                readY = False
                readD = False
                nNodes = int(s[-1])
                coordX = [0] * (nNodes + 1)
                coordY = [0] * (nNodes + 1)
                costs = [costs[:] for costs in [[0] * (nNodes + 1)] * (nNodes + 1)]
            elif len(s)>1 and s[0] == "param" and s[1] == "alpha":
                readX = False
                readY = False
                readD = False
                alpha = float(s[-1])
            elif len(s)>1 and s[0] == "param" and s[1] == "coordX":
                readX = True
                readY = False
                readD = False
            elif len(s)>1 and s[0] == "param" and s[1] == "coordY":
                readX = False
                readY = True
                readD = False
            elif len(s) > 1 and s[0] == "param" and s[1] == "d":
                readX = False
                readY = False
                readD = True
            elif (len(s)>0 and s[0] == ";") or len(s) <= 0:
                readX = False
                readY = False
                readD = False
            else:
                if readX:
                    for i, j in pairwise(s):
                        coordX[int(i)] = int(j)
                elif readY:
                    for i, j in pairwise(s):
                        coordY[int(i)] = int(j)
                elif readD:
                    if s[-1] != ':=':
                        row = []
                        for col in s[1:]:
                            row.append(float(col))
                        danger.append(row)
    return nNodes, alpha, coordX, coordY, danger

def writeSolution(solution, outFileName):
    '''
        write a <solution> (iterable of int pairs, representing arcs in solution)
        to a <filename> file
    '''
    with open(outFileName,"w") as out:
        first = True
        for i,j in solution:
            if first is True:
                out.write("%d %d" % (i,j))
                first = False
            else:
                out.write("\n%d %d" % (i,j))
