# -*- encoding: utf-8 -*-
from graphics import *
import os.path
import random
import math
import sys
from itertools import izip
from math import sqrt
import time

def main():
    WIDTH = 1920
    HEIGHT = 1080
    win = GraphWin('PediBuZ', WIDTH, HEIGHT) # give title and dimensions
    win.setBackground('white')

    nNodes, alpha, costs, coordX, coordY, danger = readData()

    points = getScreenPoints(coordX,coordY,WIDTH,HEIGHT)

    # draw cities
    cities = getCities(points, radius=4)
    for c in cities:
        c.draw(win)


    nodes = range(nNodes+1)

    def handleCityClick(clickPoint):
        # find city nearest
        node,_ = min(
            enumerate(points),
            key=lambda (_,pp): pp.squaredDistanceTo(clickPoint) )
        # reset colors
        for i,c in enumerate(cities):
            if i == 0:
                c.setFill("red")
            else:
                c.setFill("yellow")
        # change color of neighbors that can be take on
        cities[node].setFill("red")
        for n in nodes:
            if n!=0 and n!=node and costs[node][n]+costs[n][0] <= alpha*costs[node][0]:
                cities[n].setFill("blue")
        # redraw
        win.redraw()
    win.setMouseHandler(handleCityClick)

    '''
    nodes = range(nNodes+1)
    arcs = getArcs(nodes)
    # take only 3 best arcs
    #arcs = takeBest(nodes,arcs,costs,3)
    # draw roads
    # getRoads(arcs,points,danger)
    print len(getRoads(takeBest(nodes,arcs,costs,num=5),points,danger))
    for i in xrange(1,5):
        for r in getRoads(takeBest(nodes,arcs,costs,i-1,i),points,danger):
            r.draw(win)
        time.sleep(1)
    '''

    message = Text(Point(win.getWidth()/2, 20), 'Press a KEY to quit.')
    message.setTextColor('black')
    message.setStyle('italic')
    message.setSize(10)
    message.draw(win)

    win.getKey()
    win.close()

def takeBest(nodes,arcs,costs,start=0,num=3):
    results = []
    for n in nodes:
        neighbors = sorted( (j for i,j in arcs if i==n), key=lambda x: costs[n][x] )
        results += [(n,j) for j in neighbors[start:num]]
    return results

def getRoads(arcs, points, danger):
    maxD = max((max(row) for row in danger))
    minD = min((min(row) for row in danger))
    dCol = 255.  / (maxD-minD)
    result = []
    for i,j in arcs:
        road = Line(points[i],points[j])
        road.setFill(color_rgb( (danger[i][j]-minD) * dCol, 0, 0 ))
        road.setWidth(2)
        result.append(road)
    return result

def getArcs(nodes):
    return [(i,j) for i in nodes for j in nodes if i!=j and i!=0]

def getCities(points,col1="red",col2="yellow",radius=5):
    result = []
    for i,p in enumerate(points):
        circle = Circle(p, radius) # set center and radius
        if i == 0:
            circle.setFill(col1)
        else:
            circle.setFill(col2)
        result.append(circle)
    return result


def readData():
    # read dat file or error
    arg = sys.argv[1]
    filename, file_extension = os.path.splitext(arg)
    basename = os.path.basename(filename)
    if (os.path.isfile(arg) and file_extension == ".dat"):
        datFilename = basename
        return readDatFile(arg)
    else:
        if (os.path.isfile(arg)):
            print "Wrong input file type ("+arg+")."
        else:
            print "Cannot read input file"+arg+"."
        print "Usage:\n python %s INSTANCE_FILE.dat " % sys.argv[0]
        sys.exit(1)

def getScreenPoints(cx,cy,width,height):
    minX,maxX,minY,maxY = min(cx),max(cx),min(cy),max(cy)
    W = float(maxX - minX)
    H = float(maxY - minY)
    xoffset = 30
    yoffset = 60
    ww = width - 2 * xoffset
    hh = height - 2 * yoffset
    xx = ww / W
    yy = hh / W
    points = [
        Point( (x - minX ) * xx + xoffset, hh - (y - minY) * yy + yoffset )
        for x,y in izip(cx,cy)]
    return points


def pairwise(iterable):
    a = iter(iterable)
    return izip(a, a)

def readDatFile(filename):
    nNodes = 0
    alpha = 0
    coordX = []
    coordY = []
    costs = []
    danger = []
    readX = False
    readY = False
    readD = False
    f = open(filename, 'r')
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
    f.close()
    for i in range(0, (nNodes + 1)):
        for j in range(0, (nNodes + 1)):
            costs[i][j] = float("{0:.4f}".format(sqrt((coordX[i]-coordX[j])**2 + (coordY[i]-coordY[j])**2)))

    return nNodes, alpha, costs, coordX, coordY, danger

if __name__ == "__main__":
    if len(sys.argv)!=2:
        print "Usage:\n python %s <instance.dat>" % sys.argv[0]
        sys.exit(0)
    else:
        main()
