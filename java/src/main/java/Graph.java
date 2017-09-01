import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Representation of an undirected graph
 */
public class Graph
    {
    // JSON field names
    private static final String ROOT_NODE = "graph";
    private static final String NODE_NAME = "node_name";
    private static final String ADJ_NODES = "adjacent_nodes";

    // Graph is represented as a map of root nodes -> adjacent nodes
    private Map<String, List<String>> graph = new HashMap<>();

    /**
     * Constructor
     *
     * @param jsonGraph JSON object containing the graph
     */
    Graph(JSONObject jsonGraph)
        {
        this.parseGraph(jsonGraph);
        }

    /**
     * Get the root nodes for this graph
     *
     * @return Set of root nodes
     */
    Set<String> getRootNodes()
        {
        return this.graph.keySet();
        }

    /**
     * Get the adjacent nodes for a given root node
     *
     * @param key Root node to obtain adjacent nodes for
     *
     * @return List of adjacent nodes
     */
    List<String> getAdjacentNodes(String key)
        {
        return this.graph.get(key);
        }

    /**
     * Parse the JSON object into a Graph object
     *
     * @param jsonGraph JSON object containing the graph data
     */
    private void parseGraph(JSONObject jsonGraph)
        {
        // Loop through each root node
        for (JSONObject node : (Iterable<JSONObject>) (jsonGraph.get(ROOT_NODE)))
            {
            List<String> adjacentNodes = new ArrayList<>();
            adjacentNodes.addAll((JSONArray) node.get(ADJ_NODES));
            graph.put(node.get(NODE_NAME).toString(), adjacentNodes);
            }
        }

    /**
     * Reduce the graph to only those nodes supplied
     *
     * @param requiredNodes Nodes to remain in graph
     */
    void reduceGraph(JSONArray requiredNodes)
        {
        Map<String, List<String>> reducedGraph = new HashMap<>();
        for (Map.Entry<String, List<String>> node : graph.entrySet())
            {
            String nodeName = node.getKey();
            if (requiredNodes.contains(nodeName))
                {
                List<String> adjNodes = new ArrayList<>();
                for(String adjNode : node.getValue())
                    if (requiredNodes.contains(adjNode))
                        adjNodes.add(adjNode);
                reducedGraph.put(nodeName, adjNodes);
                }
            }
        graph = reducedGraph;
        }

    /**
     * Sanity check that a graph is undirected : if a is a neighbour of b, then b must be a neighbour of a
     *
     * @return true if graph is undirected, false otherwise
     */
    boolean checkGraph()
        {
        boolean undirected = true;
        for (Map.Entry<String, List<String>> node : graph.entrySet())
            for (String adjNodeName : node.getValue())
                {
                if (!graph.containsKey(adjNodeName))
                    {
                    System.err.println("No graph entry for " + adjNodeName);
                    undirected = false;
                    }
                else if (!graph.get(adjNodeName).contains(node.getKey()))
                    {
                    System.err.println(node.getKey() + " => " + adjNodeName + " exists, but " + adjNodeName +
                            " => " + node.getKey() + " doesn't");
                    undirected = false;
                    }
                }
        return undirected;
        }

    /**
     * @return String representation of the graph
     */
    @Override
    public String toString()
        {
        StringBuilder stringRepresentation = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : graph.entrySet())
            stringRepresentation.append(entry.getKey()).append(" => ").append(entry.getValue()).append("\r\n");
        return stringRepresentation.toString();
        }
    }
