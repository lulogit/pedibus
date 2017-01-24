from ordered_set import OrderedSet

class PediGraph:
    'A directed graph with weightened edges'

    def __init__(self, numNodes,xCoords,yCoords,dangerMatrix, alpha):
        '''
            create a pedibus graph (fully connected D.A.G.
            with a distance and a risk for each arc)
        '''
        self.n = numNodes
        self.alpha = alpha
        self._x = xCoords
        self._y = yCoords
        self.school = 0
        self._arcs = set()
        self._neighbours = dict()
        self._distances = dict()
        self._danger = dangerMatrix
        self.generateFeasibleArcs()
        self._arcs.union()

    def getNeighbours(self, node):
        'return the neighbours of one node'
        if node not in self._neighbours:
            self._neighbours[node] = OrderedSet(sorted(
                    (j for i,j in self.iterarcs() if i is node),
                    key=lambda x: self.distance(node,x) ))
        return self._neighbours[node]


    def sortedByDistance(self):
        'sort graph nodes by distance from school'
        return sorted(self.iternodes(),key=lambda node: self.distance[(node,0)])

    def sortedByDistance(self,nodes):
        'sort given nodes by distance from school'
        return sorted(nodes,key=lambda node: self.distance[(node,0)])

    def iternodes(self):
        'iterate over the set of nodes'
        for node in xrange(0,self.n):
            yield node

    def generateFeasibleArcs(self):
        'Add all feasible arcs to the graph'
        for i in xrange(1,self.n+1):
            self.distance(i,0)
            self._arcs.add((i,0))
        for i in xrange(1,self.n+1):
            for j in xrange(i+1,self.n+1):
                if self.canPassThrough(i,j) and self.distance(i,self.school) >= self.distance(j,self.school):
                    self._arcs.add( (i,j) )
                if self.canPassThrough(j,i) and self.distance(j,self.school) >= self.distance(i,self.school):
                    self._arcs.add( (j,i) )

    def canPassThrough(self, i,j):
        'return whether a pupil at stop i can pass through stop j'
        return self.distance(i,j)+self.distance(j,self.school) <= self.alpha*self.distance(i,0)

    def distance(self, i,j):
        'lazy evaluate the distance between node <i> and node <j>'
        if (i,j) not in self._distances:
            self._distances[(i,j)] = float(
                "{0:.4f}".format(
                    ((self._x[i]-self._x[j])**2 + (self._y[i]-self._y[j])**2)**0.5))
        return self._distances[(i,j)]

    def iterarcs(self):
        'iterate over the arcs'
        for arc in self._arcs:
            yield arc

    def numArcs(self):
        return len(self._arcs)
