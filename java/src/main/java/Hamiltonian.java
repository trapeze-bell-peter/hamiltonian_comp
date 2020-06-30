import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Hamiltonian Path for a given graph
 */
public class Hamiltonian {
    // Journey found
    private final List<String> journey;

    // Unvisited nodes in the graph
    private final List<String> unvisited;

    // Graph to traverse
    private final Graph graph;

    /**
     * Constructor
     *
     * @param graph Graph to find Hamiltonian path within
     */
    private Hamiltonian(Graph graph) {
        this.graph = graph;
        this.unvisited = new ArrayList<>();
        this.unvisited.addAll(graph.getRootNodes());
        this.journey = new ArrayList<>();
    }

    /**
     * Recursive method to find a Hamiltonian path in the current graph
     *
     * @param current Current node we are traversing from
     * @return true if a path is found, false otherwise
     */
    private boolean findHamiltonian(String current) {
        // Add the current node to our journey
        this.journey.add(current);

        // Remove the current node from the list of unvisited nodes
        this.unvisited.remove(current);

        // Have we visited all nodes ?
        if (this.unvisited.isEmpty()) {
            // We have found a path, so let's log it out and exit
            System.out.println("Found Path");
            System.out.println(this.journey);
            return true;
        }

        // Else, we still have some nodes yet to visit
        else {
            // Loop through all of the adjacent nodes to the current one
            // Are we yet to visit this node ?
            // Recurse to see if we can complete the path via this node
            for (String adjNode : graph.getAdjacentNodes(current))
                if (this.unvisited.contains(adjNode))
                    if (this.findHamiltonian(adjNode))
                        return true;
        }

        // Failed to find a path, to re-add this node to the list of unvisited and remove it from the journey so far
        this.unvisited.add(current);
        if (!this.journey.isEmpty())
            this.journey.remove(this.journey.size() - 1);

        return false;
    }

    // #################################################################################################################
    //  Application Entry Point
    // #################################################################################################################

    public static void main(String[] args) {
        System.out.println("Java Hamiltonian Path");

        JSONParser parser = new JSONParser();

        try {
            // Parse the USA graph from JSON file
            JSONObject usaJsonGraph = (JSONObject) parser.parse(
                    new BufferedReader(
                            new InputStreamReader(
                                    Hamiltonian.class.getResourceAsStream("/usa.json"))));

            // Parse the East Coast node list from JSON file
            JSONArray eastCoastJson = (JSONArray) parser.parse(
                    new BufferedReader(
                            new InputStreamReader(
                                    Hamiltonian.class.getResourceAsStream("/eastCoast.json"))));

            Graph graph = new Graph(usaJsonGraph);
            graph.reduceGraph(eastCoastJson);
            if (graph.checkGraph()) {
                for (int runIndex = 0; runIndex < 30; ++runIndex) {
                    Hamiltonian hamiltonian = new Hamiltonian(graph);
                    Calendar startTime = Calendar.getInstance();
                    hamiltonian.findHamiltonian("wdc");
                    Calendar endTime = Calendar.getInstance();
                    System.out.println("Run " + (runIndex + 1) + " : Duration: " +
                            (endTime.getTimeInMillis() - startTime.getTimeInMillis()) + "ms");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
