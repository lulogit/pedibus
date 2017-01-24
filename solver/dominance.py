
def forcedArcs(graph):
    '''
        return a set of arcs that must be added to solution cause are the only
        ones covering a pedibus stop
    '''
    arcsToAdd = set()
    onlySchool = {graph.school}
    for node in graph.iternodes():
        neighbours = graph.getNeighbours(node)
        if neighbours == onlySchool:
            arcsToAdd.add(node)
    return arcsToAdd
