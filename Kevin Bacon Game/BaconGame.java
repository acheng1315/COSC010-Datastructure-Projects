import java.io.IOException;
import java.io.BufferedReader;
import java.util.*;
import java.io.FileReader;


/**
 * Kevin Bacon Game with following functionality:
 * c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation
 * d <low> <high>: list actors sorted by degree, with degree between low and high
 * i: list actors with infinite separation from the current center
 * p <name>: find path from <name> to current center of the universe
 * s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high
 * u <name>: make <name> the center of the universe
 * @author Andrew Cheng and Samantha Wang
 *
 */
public class BaconGame {

    private Graph<String, Set<String>> pathTree;
    private Graph<String, Set<String>> baconGraph;
    private Scanner scanner;
    private String root;

    /**
     * Constructor for the Bacon game
     * @param scanner scanner
     * @param root first name
     * @param actorsPath file path for actors
     * @param moviesPath file path for movies
     * @param moviesActorsPath file path for movie actor relationships
     */
    public BaconGame(Scanner scanner, String root, String actorsPath, String moviesPath, String moviesActorsPath) {
        this.scanner = scanner;
        this.root = root;
        this.baconGraph = new AdjacencyMapGraph<>();
        try {
            startGraph(actorsPath, moviesPath, moviesActorsPath);
            createPathTree();
        }

        catch (IOException e) {
            System.err.println("Error in reading file!");
        }
    }

    /**
     * sets the root to a new center vertex
     * @param newCenter new center of the universe
     */
    public void setNewUniverseCenter(String newCenter) {
        if (baconGraph.hasVertex(newCenter)) {
            root = newCenter;
            pathTree = GraphLib.bfs(baconGraph, newCenter);
            displayUniverseCenterMessage();
        }
        else {
            System.err.println("invalid input!");
        }
    }

    /**
     * given a root returns the average separation
     * @param root root
     * @return average sep
     */
    public double getAverageSeparationForRoot(String root) {
        Graph<String, Set<String>> tempPathTree = GraphLib.bfs(baconGraph, root);
        return GraphLib.averageSeparation(tempPathTree, root);
    }

    /**
     * displays the vertices from low to high.
     * @param low low range
     * @param high high range
     */
    public void displaySortedBySeparation(int low, int high) {
        // comparator
        class SeparationComparator implements Comparator<String> {
            public int compare(String actor1, String actor2) {
                return GraphLib.findPath(pathTree, actor1).size() - GraphLib.findPath(pathTree, actor2).size();
            }
        }

        SeparationComparator separationComparator = new SeparationComparator();
        PriorityQueue<String> sortedSeparation = new PriorityQueue<>(separationComparator);
        for (String v : pathTree.vertices()){
            // checks bounds of low and high
            if (GraphLib.findPath(pathTree, v).size() > low && GraphLib.findPath(pathTree, v).size() <= high) {
                sortedSeparation.add(v);
            }
        }

        List<String> sortedBySeparationList = new ArrayList<>();
        while(!sortedSeparation.isEmpty()) {
            sortedBySeparationList.add(sortedSeparation.poll());
        }

        System.out.println(sortedBySeparationList);
    }

    /**
     * shows a list of all the vertices not connected to the current center
     */
    public void displayInfinitelySeparated() {
        System.out.println(GraphLib.missingVertices(baconGraph, pathTree));
    }

    /**
     * displays the sorted center of the universe using comparator class
     * @param index index
     */
    public void displaySortedUniverseCenters(int index){
        // comparator class
        class centerOfUniverseComparator implements Comparator<String> {
            // overrides original compare method
            @Override
            public int compare(String actor1, String actor2) {
                if (index > 0) {
                    return (int)(Math.signum(getAverageSeparationForRoot(actor1) - getAverageSeparationForRoot(actor2)));

                }else {
                    return (int)(Math.signum(getAverageSeparationForRoot(actor2) - getAverageSeparationForRoot(actor1)));
                }
            }
        }
        // calculates the abs value of the index
        int indexMagnitude = Math.abs(index);
        centerOfUniverseComparator centerComparator = new centerOfUniverseComparator();
        PriorityQueue<String> sortedCenters = new PriorityQueue<>(centerComparator);
        // for each string add it to the priority queue
        for (String v: baconGraph.vertices()) {
            sortedCenters.add(v);
        }

        List<String> sortedList = new ArrayList<>();
        // sort and decrement magnitude
        while (!sortedCenters.isEmpty() && indexMagnitude > 0) {
            sortedList.add(sortedCenters.poll());
            indexMagnitude--;
        }
        // print sorted
        System.out.println(sortedList);
    }

    /**
     * shows a list sorted by lowest to the highest range of connected degrees
     * @param low low
     * @param high high
     */
    public void displayActorsSortedByDegree(int low, int high) {
        class VertexComparator implements Comparator<String> {
            public int compare(String actor1, String actor2) {
                return (baconGraph.outDegree(actor1)) - (baconGraph.outDegree(actor2));
            }
        }

        VertexComparator comparator = new VertexComparator();
        PriorityQueue<String> sortedByDegree = new PriorityQueue<>(comparator);
        for (String v : baconGraph.vertices()) {
            if (baconGraph.outDegree(v)  >= low && baconGraph.outDegree(v) <= high) {
                sortedByDegree.add(v);
            }
        }

        List<String> sortedList = new ArrayList<>();
        while (!sortedByDegree.isEmpty()) {
            sortedList.add(sortedByDegree.poll());
        }

        System.out.println(sortedList);
    }

    /**
     * prints the info about the actorName and how far they were from the center and which movies they starred in.
     * @param actorName actor name
     */
    public void displayActorInfo(String actorName) {
        if (baconGraph.hasVertex(actorName) && pathTree.hasVertex(actorName)) {
            List<String> shortestPath = GraphLib.findPath(pathTree, actorName);

            System.out.println(actorName + "'s number is " + (shortestPath.size()-1));
            String currActor = actorName;
            for (String actorName2 : shortestPath) {
                if (!Objects.equals(actorName, actorName2)) {
                    System.out.println(currActor + " appeared in " + baconGraph.getLabel(currActor, actorName2) + " with " + actorName2);
                    currActor = actorName2;
                }
            }
        }
        else {
            System.err.println(actorName + "'s number is infinity!");
        }
    }

    /**
     * shows who is currently the center of the universe
     */
    public void displayUniverseCenterMessage(){
        System.out.println(root + " is now the center of the acting universe, connected to " +
                pathTree.numVertices() + "/" + baconGraph.numVertices() +
                " actors with average separation " + GraphLib.averageSeparation(pathTree, root));
    }

    /**
     * creates the path tree using GraphLib methods
     */
    public void createPathTree(){
        pathTree = GraphLib.bfs(baconGraph, root);
    }

    /**
     * creates the graph using file inputs
     * @param actorsPath actor file path
     * @param moviesPath movie file path
     * @param moviesActorsPath actor movie file path
     * @throws IOException exception
     */
    public void startGraph(String actorsPath, String moviesPath, String moviesActorsPath) throws IOException {
        Map<Integer, String> actorMap = new HashMap<>();
        Map<Integer, String> movieMap = new HashMap<>();
        Map<Integer, List<Integer>> movieActorMap = new HashMap<>();

        BufferedReader actorsInput = new BufferedReader(new FileReader(actorsPath));
        BufferedReader moviesInput = new BufferedReader(new FileReader(moviesPath));
        BufferedReader actorsMoviesInput = new BufferedReader(new FileReader(moviesActorsPath));

        // reads actor file
        String actorLine = actorsInput.readLine();
        while (actorLine != null){
            String[] twoLine = actorLine.split("\\|");
            if (twoLine.length > 0){
                actorMap.put(Integer.parseInt(twoLine[0]), twoLine[1]);
            }
            actorLine = actorsInput.readLine();
        }
        actorsInput.close();

        // reads movie file
        String movieLine = moviesInput.readLine();
        while (movieLine != null){
            String[] twoLine = movieLine.split("\\|");
            if (twoLine.length > 0){
                movieMap.put(Integer.parseInt(twoLine[0]), twoLine[1]);
            }
            movieLine = moviesInput.readLine();
        }
        moviesInput.close();

        // reads actor movie file
        String actorMovieLine = actorsMoviesInput.readLine();
        while (actorMovieLine != null){
            String[] twoLine = actorMovieLine.split("\\|");
            if (twoLine.length > 0){
                if(!movieActorMap.containsKey(Integer.valueOf(twoLine[0]))){
                    movieActorMap.put(Integer.valueOf(twoLine[0]), new ArrayList<>());
                }
                movieActorMap.get(Integer.valueOf(twoLine[0])).add(Integer.valueOf(twoLine[1]));
            }
            actorMovieLine = actorsMoviesInput.readLine();
        }
        actorsMoviesInput.close();

        // adds vertices
        for (Integer actorKey : actorMap.keySet()){
            if (actorMap.get(actorKey) != null){
                baconGraph.insertVertex(actorMap.get(actorKey));
            }
        }
        // adds edges
        for (Integer movieKey: movieActorMap.keySet()){
            String movie = movieMap.get(movieKey);

            for (Integer act1 : movieActorMap.get(movieKey)){

                for (Integer act2 : movieActorMap.get(movieKey)){
                    String actor1 = actorMap.get(act1);
                    String actor2 = actorMap.get(act2);

                    if (!Objects.equals(act1, act2) && !baconGraph.hasEdge(actor1, actor2)){
                        // adds all edges which are movie sets
                        Set<String> edgeSet = new HashSet<>();
                        edgeSet.add(movie);
                        baconGraph.insertUndirected(actor1, actor2, edgeSet);

                    }
                    else if(baconGraph.hasEdge(actor1, actor2)){
                        baconGraph.getLabel(actor1, actor2).add(movie);
                    }
                }
            }
        }
    }


    /**
     * scans user input and returns string
     * @param in input
     * @return next line
     */
    public String getInput(String in){

        System.out.println(in);
        return scanner.nextLine();
    }

    /**
     * Uses game rules to call other functions in class depending on user's input
     * @param input input
     * @return true false
     */
    public boolean handleKeyPress(String input) {
        // edge case to check for empty input
        if (input == null || input.length() < 1){
            System.out.println("invalid input!");
            return true;
        }
        // edge case to check for short input
        String[] splitString = input.split(" ");
        if (splitString.length < 1) {
            System.out.println("invalid input!");
            return true;
        }

        if (splitString[0].equalsIgnoreCase("q")) {
            return false;
        }
        else if (splitString[0].equalsIgnoreCase("i")) {
            displayInfinitelySeparated();
            return true;
        }
        // invalid input
        else if (splitString.length < 2){
            System.out.println("invalid input!");
            return true;
        }

        String[] inputArray = Arrays.copyOfRange(splitString, 1, splitString.length);
        String configuredInput = String.join(" ", inputArray);
        if (splitString[0].equalsIgnoreCase("u")) {
            if (baconGraph.hasVertex(configuredInput)) {
                setNewUniverseCenter(configuredInput);
            }
            else{
                System.out.println("invalid input!");
            }
        }
        else if (splitString[0].equalsIgnoreCase("c")) {
            try{
                int intInput = Integer.parseInt(splitString[1]);
                displaySortedUniverseCenters(intInput);
            }
            catch (Exception e) {
                System.out.println("invalid input!");
            }
        }
        else if (splitString[0].equalsIgnoreCase("p")) {
            if (baconGraph.hasVertex(configuredInput)){
                displayActorInfo(configuredInput);
            }
            else{
                System.out.println("invalid input!");
            }
        }

        else if (splitString[0].equalsIgnoreCase("s")) {
            if (splitString.length < 3) {
                System.out.println("invalid input!");
                return true;
            }
            try {
                int low = Integer.parseInt(splitString[1]);
                int high = Integer.parseInt(splitString[2]);
                displaySortedBySeparation(low, high);
            }
            catch(Exception e) {
                System.out.println("invalid input!");
            }
        }
        else if (splitString[0].equalsIgnoreCase("d")) {
            if (splitString.length < 3){
                System.out.println("invalid input!");
                return true;
            }
            try {
                int low = Integer.parseInt(splitString[1]);
                int high = Integer.parseInt(splitString[2]);
                displayActorsSortedByDegree(low ,high);
            }
            catch(Exception e) {
                System.out.println("invalid input!");
            }
        }

        return true;
    }

    /**
     * Starts the game and prints all the possible commands
     */
    public void startGame() {
        System.out.println("Commands:");
        System.out.println("c <#>: list top (positive number) or bottom (negative) <#> centers of the universe, sorted by average separation");
        System.out.println("d <low> <high>: list actors sorted by degree, with degree between low and high");
        System.out.println("i: list actors with infinite separation from the current center");
        System.out.println("p <name>: find path from <name> to current center of the universe");
        System.out.println("s <low> <high>: list actors sorted by non-infinite separation from the current center, with separation between low and high");
        System.out.println("u <name>: make <name> the center of the universe");
        System.out.println("q: quit game");
        System.out.println();
        String input = getInput("Kevin Bacon game >");
        boolean checkInput = handleKeyPress(input);
        while (checkInput) {
            input = getInput("Kevin Bacon game >");
            checkInput = handleKeyPress(input);
        }
    }

    /**
     * Main method - runs the code
     */
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        // full files
        String actorPath = "ps4/bacon/actors.txt";
        String moviesPath = "ps4/bacon/movies.txt";
        String movieActorPath = "ps4/bacon/movie-actors.txt";
        BaconGame game = new BaconGame(scanner, "Kevin Bacon", actorPath, moviesPath, movieActorPath);

//         test files
//        String actorPathTest = "ps4/bacon/actorsTest.txt";
//        String moviesPathTest = "ps4/bacon/moviesTest.txt";
//        String movieActorPathTest = "ps4/bacon/movie-actorsTest.txt";
//        BaconGame game = new BaconGame(scanner, "Kevin Bacon", actorPathTest, moviesPathTest, movieActorPathTest);

        game.startGame();

    }

}