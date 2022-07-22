import java.util.*;

/**
 * Library for graph analysis
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2016
 * @author Andrew Cheng and Samantha Wang
 */
public class GraphLib {

    /**
     * implements bfs in order to intake a graph and a start vertex to find the shortest path
     * @param g graph
     * @param start start vertex
     * @return graph
     */
    public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V start){
        Graph<V, E> bfsTree = new AdjacencyMapGraph<>();
        bfsTree.insertVertex(start);

        Set<V> visited = new HashSet<>();
        Queue<V> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);
        while(!queue.isEmpty()){
            V u = queue.remove();
            for (V v: g.outNeighbors(u)){
                if (!visited.contains(v)) {
                    visited.add(v);
                    queue.add(v);
                    bfsTree.insertVertex(v);
                    bfsTree.insertDirected(v, u, g.getLabel(u, v));
                }
            }
        }

        return bfsTree;
    }

    /**
     * finds the shortest path from a point in the tree.
     * @param tree graph that is represented as a tree
     * @param v curr vertex
     * @return a list of the vertices that creates the shortest path
     */
    public static <V,E> List<V> findPath(Graph<V,E> tree, V v){
        ArrayList<V> shortestPath = new ArrayList<>();
        V currVertex = v;
        shortestPath.add(currVertex);

        while (tree.outDegree(currVertex) > 0){
            for (V u: tree.outNeighbors(currVertex)){
                shortestPath.add(u);
                currVertex = u;
            }
        }

        return shortestPath;
    }

    /**
     * finds vertices that are not connected to root
     * @param graph graph
     * @param subgraph subgraph
     * @return set of unconnected vertices
     */
    public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph){
        Set<V> missing = new HashSet<>();

        for (V v: graph.vertices()){
            if (!subgraph.hasVertex(v)){
                missing.add(v);
            }
        }

        return missing;
    }

    /**
     * recursively calculates the average separation of each vertex in the graph tree
     * @param tree graph tree
     * @param root root of tree
     * @return the value of average separation
     */
    public static <V,E> double averageSeparation(Graph<V,E> tree, V root){

        int[] sum = {0};

        separationHelper(tree, root, 0, sum);
        return ((double)(sum[0]))/((double)tree.numVertices());

    }

    /**
     * helper that sums the distance to help calculate avg separation.
     * @param tree graph tree
     * @param currVertex curr vertex
     * @param pathDistance distance of the path
     * @param sum sum of the distances
     */
    public static <V,E> void separationHelper(Graph<V,E> tree, V currVertex, int pathDistance, int[] sum){

        sum[0] += pathDistance;

        if (tree.inDegree(currVertex) > 0){
            for (V u: tree.inNeighbors(currVertex)){
                separationHelper(tree, u, pathDistance+1, sum);
            }
        }
    }

    /**
     * test method
     */
    public static void testMethods(){
        Graph<String, Set<String>> testGraph = new AdjacencyMapGraph<>();

        // vertices
        String alice = "Alice";
        String bob = "Bob";
        String charlie = "Charlie";
        String dartmouth = "Dartmouth";
        String kevin = "Kevin Bacon";
        String nobody = "Nobody";
        String nobodyf = "Nobody's Friend";
        // kevin alice movies
        Set<String> kevinalice = new HashSet<>();
        kevinalice.add("A Movie");
        kevinalice.add("E Movie");
        // kevin bob movies
        Set<String> kevinbob = new HashSet<>();
        kevinbob.add("A Movie");
        // alice bob movies
        Set<String> alicebob = new HashSet<>();
        alicebob.add("A Movie");
        // alice charlie movies
        Set<String> alicecharlie = new HashSet<>();
        alicecharlie.add("D Movie");
        // bob charlie movies
        Set<String> bobcharlie = new HashSet<>();
        bobcharlie.add("C Movie");
        // charlie dartmouth movies
        Set<String> charliedartmouth = new HashSet<>();
        charliedartmouth.add("B Movie");
        // nobody nobodyf movies
        Set<String> nobodynobodyf = new HashSet<>();
        nobodynobodyf.add("F Movie");

        // inserts vertices
        testGraph.insertVertex(alice);
        testGraph.insertVertex(bob);
        testGraph.insertVertex(charlie);
        testGraph.insertVertex(dartmouth);
        testGraph.insertVertex(kevin);
        testGraph.insertVertex(nobody);
        testGraph.insertVertex(nobodyf);

        // inserts edges
        testGraph.insertUndirected(kevin, alice, kevinalice);
        testGraph.insertUndirected(kevin, bob, kevinbob);
        testGraph.insertUndirected(alice, bob, alicebob);
        testGraph.insertUndirected(alice, charlie, alicecharlie);
        testGraph.insertUndirected(bob, charlie, bobcharlie);
        testGraph.insertUndirected(charlie, dartmouth, charliedartmouth);
        testGraph.insertUndirected(nobody, nobodyf, nobodynobodyf);


        // prints info
        System.out.println((testGraph));
        System.out.println(bfs(testGraph, kevin));
        System.out.println(missingVertices(testGraph, bfs(testGraph, kevin)));
        System.out.println(averageSeparation(bfs(testGraph, kevin), kevin));
        System.out.println(findPath(bfs(testGraph, kevin), charlie));


    }

    public static void main(String[] args){
        testMethods();
    }

}