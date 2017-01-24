
class Solution(object):
    '''
        represent a possible solution to the pedibus problem
    '''

    def __init__(self, baseSolution=None, arcsToAdd=None):
        'create this solution starting from a base solution'
        if baseSolution:
            self._arcs = set(baseSolution._arcs)
            self._numArcs = baseSolution._numArcs + len(arcsToAdd)
        else:
            self._arcs = set()
            self._numArcs = 0
        if arcsToAdd:
            self._arcs.union(arcsToAdd)

    def numArcs(self):
        'return the number of arcs in solution'
        return self._numArcs

    def iterarcs(self):
        'iterate over solution arcs'
        for arc in self._arcs:
            yield arc

    def iterArcsToCover(self, graph):
        'returns a set of arcs that can be added to the solution'
        return set()
