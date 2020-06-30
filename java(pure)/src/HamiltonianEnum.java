import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HamiltonianEnum {

    public static void main(String[] args) {
        HamiltonianEnum h = new HamiltonianEnum();
        for (int i = 0; i < 300; ++i) {
            h.findHamiltonian("wdc");
        }
        long sum = 0;
        int count = 10;
        String st = args.length == 0 ? "wdc" : args[0];
        System.out.println(h.findHamiltonian(st));
        for (int i = 0; i < count; ++i) {
            long start = System.nanoTime();
            h.findHamiltonian(st);
            sum += System.nanoTime() - start;
        }
        System.out.println("average: " + TimeUnit.NANOSECONDS.toMicros(sum / count) + "us");
    }

    private static Map<State, Set<State>> usa;
    private static Set<State> easternStates;

    static {
        initialiseStates();
    }

    private final int journeySize;

    private Map<State, Set<State>> graph;

    private static class SimpleLinkedList {
        int size;
        State value;
        SimpleLinkedList next;

        public SimpleLinkedList(State value, SimpleLinkedList next) {
            this.value = value;
            this.next = next;
            this.size = 1 + (next == null ? 0 : next.size);
        }
    }


    public HamiltonianEnum() {
        this.graph = reduceGraph(usa, easternStates);
        this.journeySize = graph.size();
    }

    private Map<State, Set<State>> reduceGraph(Map<State, Set<State>> copy, Set<State> statesToVisit) {
        Map<State, Set<State>> reducedGraph = new EnumMap<State, Set<State>>(State.class);
        for (State state : copy.keySet()) {
            if (statesToVisit.contains(state)) {
                Set<State> neighbours = copy.get(state).stream().filter(
                        s -> statesToVisit.contains(s)).collect(Collectors.toCollection(() -> EnumSet.noneOf(State.class)));
                if (!neighbours.isEmpty()) {
                    reducedGraph.put(state, neighbours);
                }
            }
        }
        return reducedGraph;
    }

    private Collection<String> findHamiltonian(String start) {
        return this.findHamiltoniamRecursively(State.valueOf(start), null, EnumSet.noneOf(State.class));
    }

    private Collection<String> findHamiltoniamRecursively(State current, SimpleLinkedList list, Set<State> visited) {
        SimpleLinkedList ll = new SimpleLinkedList(current, list);
        visited.add(current);
        if (ll.size == journeySize) {
            return collect(ll);
        } else {
            Set<State> toMaybeVisit = ((EnumSet<State>) graph.get(current)).clone();
            toMaybeVisit.removeAll(visited);
            for (State toVisit : toMaybeVisit) {
                Collection<String> r = findHamiltoniamRecursively(toVisit, ll, visited);
                if (r != null) {
                    return r;
                }
            }
            visited.remove(current);
            return null;
        }
    }

    private Collection<String> collect(SimpleLinkedList ll) {
        List<String> list = new ArrayList<>();
        while (ll != null) {
            list.add(ll.value.name());
            ll = ll.next;
        }
        return list;
    }

    private static Set<State> fromNames(String... names) {
        Set<State> set = EnumSet.noneOf(State.class);
        for (String n : names) {
            set.add(State.valueOf(n));
        }
        return set;
    }

    private static void initialiseStates() {
        usa = new EnumMap<>(State.class);
        usa.put(State.wa, fromNames("or", "id"));
        usa.put(State.or, fromNames("wa", "id", "nv", "ca"));
        usa.put(State.ca, fromNames("or", "nv", "az"));
        usa.put(State.id, fromNames("wa", "or", "nv", "ut", "wy", "mt"));
        usa.put(State.nv, fromNames("or", "ca", "az", "ut", "id"));
        usa.put(State.ut, fromNames("id", "nv", "az", "co", "wy"));
        usa.put(State.az, fromNames("ca", "nv", "ut", "nm"));
        usa.put(State.mt, fromNames("id", "wy", "sd", "nd"));
        usa.put(State.wy, fromNames("mt", "id", "ut", "co", "ne", "sd"));
        usa.put(State.co, fromNames("wy", "ut", "nm", "ok", "ks", "ne"));
        usa.put(State.nm, fromNames("co", "az", "tx", "ok"));
        usa.put(State.nd, fromNames("mt", "sd", "mn"));
        usa.put(State.sd, fromNames("nd", "mt", "wy", "ne", "ia", "mn"));
        usa.put(State.ne, fromNames("sd", "wy", "co", "ks", "mo", "ia"));
        usa.put(State.ks, fromNames("ne", "co", "ok", "mo"));
        usa.put(State.ok, fromNames("ks", "co", "nm", "tx", "ar", "mo"));
        usa.put(State.tx, fromNames("ok", "nm", "la", "ar"));
        usa.put(State.mn, fromNames("nd", "sd", "ia", "wi"));
        usa.put(State.ia, fromNames("mn", "sd", "ne", "mo", "il", "wi"));
        usa.put(State.mo, fromNames("ia", "ne", "ks", "ok", "ar", "tn", "ky", "il"));
        usa.put(State.ar, fromNames("mo", "ok", "tx", "la", "ms", "tn"));
        usa.put(State.la, fromNames("ar", "tx", "ms"));
        usa.put(State.wi, fromNames("mn", "ia", "il"));
        usa.put(State.il, fromNames("wi", "ia", "mo", "ky", "in"));
        usa.put(State.tn, fromNames("ky", "mo", "ar", "ms", "al", "ga", "nc", "va", "ky"));
        usa.put(State.ms, fromNames("tn", "ar", "la", "al", "tn"));
        usa.put(State.mi, fromNames("in", "oh"));
        usa.put(State.in, fromNames("mi", "il", "ky", "oh"));
        usa.put(State.ky, fromNames("oh", "in", "il", "mo", "tn", "va", "wv", "oh"));
        usa.put(State.al, fromNames("tn", "ms", "fl", "ga"));
        usa.put(State.ga, fromNames("nc", "tn", "al", "fl", "sc"));
        usa.put(State.oh, fromNames("mi", "in", "ky", "wv", "pa"));
        usa.put(State.wv, fromNames("pa", "oh", "ky", "va", "md"));
        usa.put(State.ny, fromNames("pa", "nj", "ct", "ma", "vt"));
        usa.put(State.nj, fromNames("ny", "pa", "de"));
        usa.put(State.pa, fromNames("ny", "nj", "oh", "wv", "md", "de"));
        usa.put(State.va, fromNames("md", "wv", "ky", "tn", "nc", "wdc"));
        usa.put(State.nc, fromNames("va", "tn", "ga", "sc"));
        usa.put(State.sc, fromNames("nc", "ga"));
        usa.put(State.fl, fromNames("ga", "al"));
        usa.put(State.me, fromNames("nh"));
        usa.put(State.nh, fromNames("me", "vt", "ma"));
        usa.put(State.vt, fromNames("nh", "ny", "ma"));
        usa.put(State.ma, fromNames("nh", "vt", "ny", "ct", "ri"));
        usa.put(State.ct, fromNames("ma", "ny", "ri"));
        usa.put(State.ri, fromNames("ma", "ct"));
        usa.put(State.de, fromNames("pa", "md", "nj"));
        usa.put(State.md, fromNames("pa", "wv", "va", "de", "wdc"));
        usa.put(State.wdc, fromNames("md", "va"));

        easternStates = fromNames("mn", "ia", "mo", "ar", "la", "wi", "il", "tn", "ms", "mi", "in", "ky", "al",
                "ga", "oh", "wv", "ny", "nj", "pa", "va", "nc", "sc", "fl", "me", "nh", "vt", "ma", "ct", "ri",
                "de", "md", "wdc");
    }

    enum State {
        wa,
        or,
        ca,
        id,
        nv,
        ut,
        az,
        mt,
        wy,
        co,
        nm,
        nd,
        sd,
        ne,
        ks,
        ok,
        tx,
        mn,
        ia,
        mo,
        ar,
        la,
        wi,
        il,
        tn,
        ms,
        mi,
        in,
        ky,
        al,
        ga,
        oh,
        wv,
        ny,
        nj,
        pa,
        va,
        nc,
        sc,
        fl,
        me,
        nh,
        vt,
        ma,
        ct,
        ri,
        de,
        md,
        wdc;
    }

}
