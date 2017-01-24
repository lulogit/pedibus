from priority_queue import PriorityQueue
from graph import Wgraph, ArcWeight

def dijkstra_1( graph, source_vertex, destination=None ):
    '''
        Compute distances of every vertex in Graph from the SourceVertex,
        if EndVertex is provided, it stops once reached it
    '''
    distance = { source_vertex: ArcWeight(0.,0.) }
    previous = {}
    Q = PriorityQueue()
    Q.add_task( source_vertex, 0 )
    while Q.is_not_empty():
        curr_vert = Q.pop_task()
        if curr_vert is destination:
            break
        curr_dist = distance[curr_vert]
        for vert,dist in graph.neighbours( curr_vert ):
            new_dist = curr_dist + dist
            if vert in distance:
                if distance[vert]>new_dist:
                    distance[vert] = new_dist
                    previous[vert] = curr_vert
                    Q.add_task( vert, new_dist )
            else:
                distance[vert] = new_dist
                previous[vert] = curr_vert
                Q.add_task( vert, new_dist )
    return (distance,previous)

def a_min_path( graph, source, destination ):
    distances,previous = dijkstra_1( graph, source, destination )
    path = []
    curr_vert = destination
    while curr_vert is not source:
        path.insert(0,curr_vert)
        curr_vert = previous[curr_vert]
    path.insert(0,curr_vert)
    return (distances[destination],path)

if __name__ == "__main__":
    graph = Wgraph()
    graph.add_edge('a','d',ArcWeight(2,0))
    graph.add_edge('a','b',ArcWeight(4,0))
    graph.add_edge('a','e',ArcWeight(7,0))
    graph.add_edge('b','e',ArcWeight(2,0))
    graph.add_edge('d','g',ArcWeight(1,0))
    graph.add_edge('d','h',ArcWeight(4,0))
    graph.add_edge('g','h',ArcWeight(2,0))
    graph.add_edge('h','e',ArcWeight(5,0))
    graph.add_edge('h','f',ArcWeight(1,0))
    graph.add_edge('e','f',ArcWeight(2,0))
    graph.add_edge('f','c',ArcWeight(1,0))
    graph.add_edge('c','e',ArcWeight(4,0))
    print a_min_path( graph, 'a','f' )
