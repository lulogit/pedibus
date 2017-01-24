
def allPaths(graph, start, dest, distLeft, path=[], visited=set()):
    '''
        find the list of all possible paths in graph
        starting from <start>, ending in <dest>,
        bounding the path's lenght to <maxDist>
    '''
    if distLeft <= 0:
        return
    path = path + [start]
    if start == dest:
        yield path
        return 
    if len(path) > 1:
        distLeft -= graph.distance(path[-2],path[-1])
    for node in graph.getNeighbours(start):
        if node not in visited:
            for p in allPaths(graph, node, dest, distLeft, path, visited | {node}):
                yield p
