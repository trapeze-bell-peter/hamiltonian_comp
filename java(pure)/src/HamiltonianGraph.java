import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HamiltonianGraph {

    public static void main(String[] args) {
        initialiseStates();
        reduceGraph(easternStates);
        for (int i = 0; i < 30; i++) {
            findHamiltonian("wdc");
        }
        long sum = 0;
        int count = 10;
        String st = args.length == 0 ? "wdc" : args[0];
        System.out.println(findHamiltonian(st));
        for (int i = 0; i < count; ++i) {
            long start = System.nanoTime();
            findHamiltonian(st);
            sum += System.nanoTime() - start;
        }
        System.out.println("average: " + TimeUnit.NANOSECONDS.toMicros(sum / count));
    }

    private static class SimpleLinkedList<T> {
        final int size;
        final T value;
        final SimpleLinkedList next;

        public SimpleLinkedList(T value, SimpleLinkedList next) {
            this.value = value;
            this.next = next;
            this.size = 1 + (next == null ? 0 : next.size);
        }
    }

    static class Node {
        final String state;
        final Collection<Node> links = new LinkedHashSet<>();

        public Node(String state) {
            this.state = state;
        }

        Node retain(Collection<String> col) {
            links.removeIf(n -> !col.contains(n.state));
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return state != null ? state.equals(node.state) : node.state == null;
        }

        @Override
        public int hashCode() {
            return state != null ? state.hashCode() : 0;
        }

        @Override
        public String toString() {
            return state;
        }
    }

    static Map<String, Node> graphNodes = new HashMap<>();

    static Collection<String> easternStates = new HashSet<>();

    static Node getOrCreate(String state) {
        return graphNodes.computeIfAbsent(state, Node::new);
    }

    static void link(String node, String... children) {
        for (String st : children) {
            getOrCreate(node).links.add(getOrCreate(st));
        }
    }

    static Collection<Node> findHamiltonian(String starting) {
        return findHamiltonianRecursive(getOrCreate(starting), null, new HashSet<>(), graphNodes.size());
    }

    static Collection<Node> findHamiltonianRecursive(Node starting, SimpleLinkedList<String> visitedStack, Set<String> visitedSet, int max) {
        SimpleLinkedList<String> sll = new SimpleLinkedList<>(starting.state, visitedStack);
        visitedSet.add(starting.state);
        if (sll.size == max) {
            return collect(sll);
        }
        for (Node child : starting.links) {
            if (!visitedSet.contains(child.state)) {
                Collection<Node> r = findHamiltonianRecursive(child, sll, visitedSet, max);
                if (r != null) {
                    return r;
                }
            }
        }
        visitedSet.remove(starting.state);
        return null;
    }

    private static Collection<Node> collect(SimpleLinkedList<String> sll) {
        List<Node> list = new ArrayList<>();
        while (sll != null) {
            list.add(graphNodes.get(sll.value));
            sll = sll.next;
        }
        return list;
    }

    static void reduceGraph(Collection<String> selected) {
        graphNodes = graphNodes.entrySet().stream().filter(e -> selected.contains(e.getKey())).map(Map.Entry::getValue).map(n -> n.retain(selected)).collect(Collectors.toMap(n -> n.state, n -> n));
    }

    static void initialiseStates() {
        link("wa", "or", "id");
        link("or", "wa", "id", "nv", "ca");
        link("ca", "or", "nv", "az");
        link("id", "wa", "or", "nv", "ut", "wy", "mt");
        link("nv", "or", "ca", "az", "ut", "id");
        link("ut", "id", "nv", "az", "co", "wy");
        link("az", "ca", "nv", "ut", "nm");
        link("mt", "id", "wy", "sd", "nd");
        link("wy", "mt", "id", "ut", "co", "ne", "sd");
        link("co", "wy", "ut", "nm", "ok", "ks", "ne");
        link("nm", "co", "az", "tx", "ok");
        link("nd", "mt", "sd", "mn");
        link("sd", "nd", "mt", "wy", "ne", "ia", "mn");
        link("ne", "sd", "wy", "co", "ks", "mo", "ia");
        link("ks", "ne", "co", "ok", "mo");
        link("ok", "ks", "co", "nm", "tx", "ar", "mo");
        link("tx", "ok", "nm", "la", "ar");
        link("mn", "nd", "sd", "ia", "wi");
        link("ia", "mn", "sd", "ne", "mo", "il", "wi");
        link("mo", "ia", "ne", "ks", "ok", "ar", "tn", "ky", "il");
        link("ar", "mo", "ok", "tx", "la", "ms", "tn");
        link("la", "ar", "tx", "ms");
        link("wi", "mn", "ia", "il");
        link("il", "wi", "ia", "mo", "ky", "in");
        link("tn", "ky", "mo", "ar", "ms", "al", "ga", "nc", "va", "ky");
        link("ms", "tn", "ar", "la", "al", "tn");
        link("mi", "in", "oh");
        link("in", "mi", "il", "ky", "oh");
        link("ky", "oh", "in", "il", "mo", "tn", "va", "wv", "oh");
        link("al", "tn", "ms", "fl", "ga");
        link("ga", "nc", "tn", "al", "fl", "sc");
        link("oh", "mi", "in", "ky", "wv", "pa");
        link("wv", "pa", "oh", "ky", "va", "md");
        link("ny", "pa", "nj", "ct", "ma", "vt");
        link("nj", "ny", "pa", "de");
        link("pa", "ny", "nj", "oh", "wv", "md", "de");
        link("va", "md", "wv", "ky", "tn", "nc", "wdc");
        link("nc", "va", "tn", "ga", "sc");
        link("sc", "nc", "ga");
        link("fl", "ga", "al");
        link("me", "nh");
        link("nh", "me", "vt", "ma");
        link("vt", "nh", "ny", "ma");
        link("ma", "nh", "vt", "ny", "ct", "ri");
        link("ct", "ma", "ny", "ri");
        link("ri", "ma", "ct");
        link("de", "pa", "md", "nj");
        link("md", "pa", "wv", "va", "de", "wdc");
        link("wdc", "md", "va");

        easternStates.addAll(Arrays.asList("mn", "ia", "mo", "ar", "la", "wi", "il", "tn", "ms", "mi", "in", "ky", "al",
                "ga", "oh", "wv", "ny", "nj", "pa", "va", "nc", "sc", "fl", "me", "nh", "vt", "ma", "ct", "ri",
                "de", "md", "wdc"));

    }

}
