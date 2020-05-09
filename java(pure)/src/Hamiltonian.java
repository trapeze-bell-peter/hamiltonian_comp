import java.util.*;
import java.util.stream.Collectors;

public class Hamiltonian {

    private Map<String, List<String>> usa;
    private List<String> easternStates;

    private List<String> journey = new ArrayList<>();
    private Map<String, List<String>> graph = new HashMap<>();

    public static void main(String[] args) {
        for (int i=0; i<30; ++i) {
            Hamiltonian h = new Hamiltonian();
            long start = Calendar.getInstance().getTimeInMillis();
            h.findHamiltonian("wdc");
            long end = Calendar.getInstance().getTimeInMillis();
            System.out.println("\nDuration: " + (end - start) + "ms");
            }
    }

    public Hamiltonian() {
        initialiseStates();
        this.graph.putAll(usa);
        this.reduceGraph(easternStates);
        this.checkGraph();
    }

    private void reduceGraph(List<String> statesToVisit){
        Map<String, List<String>> reducedGraph = new HashMap<>();
        for(String state : this.graph.keySet()) {
            if (statesToVisit.indexOf(state) >= 0) {
                List<String> neighbours = this.graph.get(state).stream().filter(
                    s -> statesToVisit.indexOf(s) >= 0).collect(Collectors.toList());
                reducedGraph.put(state, neighbours);
            }
        }
    this.graph = reducedGraph;
    }

    private void checkGraph() {
        for (Map.Entry<String, List<String>> entry : this.graph.entrySet()) {
            for (String neighbour : entry.getValue()) {
                if (this.graph.get(neighbour) == null) {
                    System.out.println("No graph entry for " + neighbour);
                } else if (this.graph.get(neighbour).indexOf(entry.getKey()) < 0){
                    System.out.println(entry.getKey() + " -> " + neighbour + " exists, but not " + neighbour + " -> " + entry.getKey());
                }
            }
        }
    }

    private void findHamiltonian(String start){
        if (!this.findHamiltoniamRecursively(start)) {
            System.out.println("No hamiltonian path found.");
        }
    }

    private boolean findHamiltoniamRecursively(String current){
        journey.add(current);

        if(journey.size() == graph.size()){
            for(String state : journey){
                System.out.print(state + "->");
            }
            return true;
        } else {
            for (String neighbour : graph.get(current)) {
                if (journey.indexOf(neighbour) < 0) {
                    if (findHamiltoniamRecursively(neighbour)) {
                        return true;
                    }
                }
            }
        }

        journey.remove(journey.size()-1);
        return false;
    }


    private void initialiseStates() {
        usa = new HashMap<>();
        usa.put("wa", Arrays.asList("or", "id"));
        usa.put("or", Arrays.asList("wa", "id", "nv", "ca"));
        usa.put("ca", Arrays.asList("or", "nv", "az"));
        usa.put("id", Arrays.asList("wa", "or", "nv", "ut", "wy", "mt"));
        usa.put("nv", Arrays.asList("or", "ca", "az", "ut", "id"));
        usa.put("ut", Arrays.asList("id", "nv", "az", "co", "wy"));
        usa.put("az", Arrays.asList("ca", "nv", "ut", "nm"));
        usa.put("mt", Arrays.asList("id", "wy", "sd", "nd"));
        usa.put("wy", Arrays.asList("mt", "id", "ut", "co", "ne", "sd"));
        usa.put("co", Arrays.asList("wy", "ut", "nm", "ok", "ks", "ne"));
        usa.put("nm", Arrays.asList("co", "az", "tx", "ok"));
        usa.put("nd", Arrays.asList("mt", "sd", "mn"));
        usa.put("sd", Arrays.asList("nd", "mt", "wy", "ne", "ia", "mn"));
        usa.put("ne", Arrays.asList("sd", "wy", "co", "ks", "mo", "ia"));
        usa.put("ks", Arrays.asList("ne", "co", "ok", "mo"));
        usa.put("ok", Arrays.asList("ks", "co", "nm", "tx", "ar", "mo"));
        usa.put("tx", Arrays.asList("ok", "nm", "la", "ar"));
        usa.put("mn", Arrays.asList("nd", "sd", "ia", "wi"));
        usa.put("ia", Arrays.asList("mn", "sd", "ne", "mo", "il", "wi"));
        usa.put("mo", Arrays.asList("ia", "ne", "ks", "ok", "ar", "tn", "ky", "il"));
        usa.put("ar", Arrays.asList("mo", "ok", "tx", "la", "ms", "tn"));
        usa.put("la", Arrays.asList("ar", "tx", "ms"));
        usa.put("wi", Arrays.asList("mn", "ia", "il"));
        usa.put("il", Arrays.asList("wi", "ia", "mo", "ky", "in"));
        usa.put("tn", Arrays.asList("ky", "mo", "ar", "ms", "al", "ga", "nc", "va", "ky"));
        usa.put("ms", Arrays.asList("tn", "ar", "la", "al", "tn"));
        usa.put("mi", Arrays.asList("in", "oh"));
        usa.put("in", Arrays.asList("mi", "il", "ky", "oh"));
        usa.put("ky", Arrays.asList("oh", "in", "il", "mo", "tn", "va", "wv", "oh"));
        usa.put("al", Arrays.asList("tn", "ms", "fl", "ga"));
        usa.put("ga", Arrays.asList("nc", "tn", "al", "fl", "sc"));
        usa.put("oh", Arrays.asList("mi", "in", "ky", "wv", "pa"));
        usa.put("wv", Arrays.asList("pa", "oh", "ky", "va", "md"));
        usa.put("ny", Arrays.asList("pa", "nj", "ct", "ma", "vt"));
        usa.put("nj", Arrays.asList("ny", "pa", "de"));
        usa.put("pa", Arrays.asList("ny", "nj", "oh", "wv", "md", "de"));
        usa.put("va", Arrays.asList("md", "wv", "ky", "tn", "nc", "wdc"));
        usa.put("nc", Arrays.asList("va", "tn", "ga", "sc"));
        usa.put("sc", Arrays.asList("nc", "ga"));
        usa.put("fl", Arrays.asList("ga", "al"));
        usa.put("me", Arrays.asList("nh"));
        usa.put("nh", Arrays.asList("me", "vt", "ma"));
        usa.put("vt", Arrays.asList("nh", "ny", "ma"));
        usa.put("ma", Arrays.asList("nh", "vt", "ny", "ct", "ri"));
        usa.put("ct", Arrays.asList("ma", "ny", "ri"));
        usa.put("ri", Arrays.asList("ma", "ct"));
        usa.put("de", Arrays.asList("pa", "md", "nj"));
        usa.put("md", Arrays.asList("pa", "wv", "va", "de", "wdc"));
        usa.put("wdc", Arrays.asList("md", "va"));

        easternStates = new ArrayList<>();
        easternStates.addAll(Arrays.asList("mn", "ia", "mo", "ar", "la", "wi", "il", "tn", "ms", "mi", "in", "ky", "al",
                "ga", "oh", "wv", "ny", "nj", "pa", "va", "nc", "sc", "fl", "me", "nh", "vt", "ma", "ct", "ri",
                "de", "md", "wdc"));
    }

}
