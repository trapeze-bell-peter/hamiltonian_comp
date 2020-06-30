import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HamiltonianFast {

    private static Map<String, List<String>> usa;
    private static List<String> easternStates;
    private Map<String, Integer> stateToIndex;
    private String[] indexToState;

    static {
        initialiseStates();
    }

    private final int journeySize;
    private long[] graph;

    private static class SimpleLinkedList {
        int size;
        int value;
        SimpleLinkedList next;

        public SimpleLinkedList(int value, SimpleLinkedList next) {
            this.value = value;
            this.next = next;
            this.size = 1 + (next == null ? 0 : next.size);
        }

    }

    public static void main(String[] args) {
        HamiltonianFast h = new HamiltonianFast(true);
        for (int i = 0; i < 30; ++i) {
            h.findHamiltonian("wdc");
        }
        long sum = 0;
        int count = 10;
        String st = args.length == 0 ? "wdc" : args[0];
        System.out.println(h.findHamiltonian(st));
        for (int i = 0; i < count; ++i) {
            long start = System.nanoTime();
            h.findHamiltonian(st);
            long end = System.nanoTime();
            sum += end - start;
            System.gc();
        }
        System.out.println("average: " + TimeUnit.NANOSECONDS.toMicros(sum / count) + "us");
    }

    public HamiltonianFast(boolean reduce) {
        Map<String, List<String>> copy = reduceGraph(usa, reduce ? easternStates : usa.keySet());
        graph = buildFrom(copy);
        this.journeySize = copy.size();
    }

    private long[] buildFrom(Map<String, List<String>> copy) {
        stateToIndex = new HashMap<>();
        indexToState = new String[copy.size()];
        for (String state : copy.keySet()) {
            int index = stateToIndex.size();
            stateToIndex.put(state, index);
            indexToState[index] = state;
        }
        long[] m = new long[copy.size()];
        for (Entry<String, List<String>> entry : copy.entrySet()) {
            int[] a = entry.getValue().stream().mapToInt(stateToIndex::get).toArray();
            long key = 0;
            for (int state : a) {
                key |= 1L << state;
            }
            m[stateToIndex.get(entry.getKey())] = key;
        }
        return m;
    }

    private Map<String, List<String>> reduceGraph(Map<String, List<String>> copy, Collection<String> statesToVisit) {
        Map<String, List<String>> reducedGraph = new HashMap<>();
        for (String state : copy.keySet()) {
            if (statesToVisit.contains(state)) {
                List<String> neighbours = copy.get(state).stream().filter(
                        s -> statesToVisit.contains(s)).collect(Collectors.toList());
                if (!neighbours.isEmpty()) {
                    reducedGraph.put(state, neighbours);
                }
            }
        }
        return reducedGraph;

    }

    private String[] findHamiltonian(String start) {
        return this.findHamiltoniamRecursively(stateToIndex.get(start), null, 0);
    }

    private String[] findHamiltoniamRecursively(int index, SimpleLinkedList list, long visited) {
        SimpleLinkedList ll = new SimpleLinkedList(index, list);
        long v2 = visited | (1L << index);
        if (ll.size == journeySize) {
            String[] r = new String[journeySize];
            int i = 0;
            while (ll != null) {
                r[i++] = indexToState[ll.value];
                ll = ll.next;
            }
            return r;
        } else {
            long toVisit = graph[index] & ~v2;
            while (toVisit != 0) {
                int next = Long.numberOfTrailingZeros(toVisit);
                String[] r = findHamiltoniamRecursively(next, ll, v2);
                if (r != null) {
                    return r;
                }
                toVisit = toVisit & ~(1L << next);

            }
        }
        return null;
    }

    private static void initialiseStates() {
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
