# -*- encoding: utf-8 -*-
import os.path
import random
import math
import sys
from itertools import izip
from math import sqrt

if sys.version_info.major < 3:
      import Tkinter
else:
      import tkinter as Tkinter

class TSP_WIN(object):
      def __init__(self, aRoot, aLifeCount = 1000, aWidth = 1680, aHeight = 990):
            self.root = aRoot
            self.lifeCount = aLifeCount
            self.width = aWidth
            self.height = aHeight
            self.canvas = Tkinter.Canvas(
                        self.root,
                        width = self.width,
                        height = self.height,
                  )
            self.canvas.pack(expand = Tkinter.YES, fill = Tkinter.BOTH)
            self.initCitys()

            self.title("PediBus")


      def initCitys(self):
            self.nNodes = 0
            self.alpha = 0
            self.costs = []

            self.coordX = []
            self.coordY = []
            self.danger = []
            datFilename = ""

            for arg in sys.argv[1:]:
                filename, file_extension = os.path.splitext(arg)
                basename = os.path.basename(filename)
                if (os.path.isfile(arg) and file_extension == ".dat"):
                    datFilename = basename
                    self.nNodes, self.alpha, self.costs, self.coordX, self.coordY, self.danger = readDatFile(arg)

                else:
                    if (os.path.isfile(arg)):
                        print "Wrong input file type ("+arg+")."
                    else:
                        print "Cannot read input file"+arg+"."
                    print "Usage:\n python TSP_GA_w.py INSTANCE_FILE.dat "
                    sys.exit(1)

            self.citys = []
            for countNode in range(self.nNodes + 1):
                    self.citys.append((self.coordX[countNode],self.coordY[countNode]))

            # 坐标变换
            minX, minY = self.citys[0][0], self.citys[0][1]
            maxX, maxY = minX, minY
            for city in self.citys[1:]:
                  if minX > city[0]:
                        minX = city[0]
                  if minY > city[1]:
                        minY = city[1]
                  if maxX < city[0]:
                        maxX = city[0]
                  if maxY < city[1]:
                        maxY = city[1]

            w = maxX - minX
            h = maxY - minY
            xoffset = 30
            yoffset = 30
            ww = self.width - 2 * xoffset
            hh = self.height - 2 * yoffset
            xx = ww / float(w)
            yy = hh / float(h)
            r = 5

            self.nodes = []
            self.nodes2 = []
            countCity = 0
            for city in self.citys:
                  x = (city[0] - minX ) * xx + xoffset
                  y = hh - (city[1] - minY) * yy + yoffset
                  self.nodes.append((x, y))
                  node = self.canvas.create_oval(x - r, y - r, x + r, y + r, # 画圆点
                        fill = "#ff0000",
                        outline = "#000000",
                        tags = "node",)
                  self.nodes2.append(node)
                  self.canvas.create_text(x-r, y-r,text=countCity,font="Times 10 bold ",tags="text")
                  countCity = countCity + 1
            self.school = self.citys[0]
            x = (self.school[0] - minX) * xx + xoffset
            y = hh - (self.school[1] - minY) * yy + yoffset
            node = self.canvas.create_oval(x - r, y -r, x + r, y + r,
                               fill = "#33ff33",
                               outline = "#000000",
                               tags = "node",)



      def title(self, text):
            self.root.title(text)


      def line(self, order):
            self.canvas.delete("line")
            count = 0
            for i in range(self.nNodes):
                  p1 = self.nodes[count+1]
                  p2 = self.nodes[order[count]]
                  self.canvas.create_line(p1, p2, fill = "#000000", tags = "line")
                  count = count + 1

      def mainloop(self):
            self.root.mainloop()


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

def main():
    tsp = TSP_WIN(Tkinter.Tk())
    tsp.mainloop()


if __name__ == '__main__':
    if len(sys.argv)!=2:
        print "Usage:\n python TSP_GA_w.py <instance.dat>"
        sys.exit(0)
    else:
        main()
