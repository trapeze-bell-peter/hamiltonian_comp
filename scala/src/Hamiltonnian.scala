import java.util.concurrent.TimeUnit

import scala.io.Source

object Hamiltonnian {
  def main(args: Array[String]): Unit = {
    val m = usa.reduce(easternStates)

    0.until(30).foreach(_ => m.findHamiltonian("wdc"))
    if(args.isEmpty) {
      0.until(5).foreach( _ => checkAndTime(m, "wdc"))
    } else {
      0.until(5).foreach( _ => args.foreach(s => checkAndTime(m, s)))
    }
  }

  def checkAndTime(graph: Graph, state: String) = {
    println(s"starting state $state")
    val t0 = System.nanoTime()
    val l = graph.findHamiltonian(state)
    val time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t0)
    println(s"for state: $state result was $l and took: $time millis")
  }

  case class Node(val state: String, var links: List[Node] = List()) {
    def add(node: Node) = if(links.find(_.state == node.state).isEmpty) links = node :: links
    def findHamiltonianRecurse(partialResult: List[String], visited: Set[String], max: Int): Option[List[String]] = {
      if(visited.size == max) Some(partialResult)
      else links.to(LazyList).filter(n => !visited.contains(n.state)).map(c => c.findHamiltonianRecurse(c.state :: partialResult, visited.+(c.state), max)).filter(_.isDefined).headOption.getOrElse(None)
    }
    def reduce(selected: Seq[String]) = { links = links.filter(n => selected.contains(n.state)); this }
  }

  class Graph (var nodes: scala.collection.mutable.Map[String, Node]) {
    def link(root: String)(links: Seq[String]) = links.map(l => nodes.getOrElseUpdate(l, new Node(l))).foreach(nodes.getOrElseUpdate(root, new Node(root)).add)
    def findHamiltonian(starting: String) = nodes.get(starting).flatMap(n => n.findHamiltonianRecurse(List(starting), Set(starting), nodes.size))
    def reduce(selected: Seq[String]) = {nodes = nodes.filter(kv => selected.contains(kv._1)); nodes.values.foreach(_.reduce(selected)); this}
  }

  val easternStates = Seq("mn", "ia", "mo", "ar", "la", "wi", "il", "tn", "ms", "mi", "in", "ky", "al",
    "ga", "oh", "wv", "ny", "nj", "pa", "va", "nc", "sc", "fl", "me", "nh", "vt", "ma", "ct", "ri",
    "de", "md", "wdc")

  val usa = initUsa()

  def initUsa() = {
    val usa = new Graph(scala.collection.mutable.Map())
    usa.link("wa")(Seq("or", "id"))
    usa.link("or")(Seq("wa", "id", "nv", "ca"))
    usa.link("ca")(Seq("or", "nv", "az"))
    usa.link("id")(Seq("wa", "or", "nv", "ut", "wy", "mt"))
    usa.link("nv")(Seq("or", "ca", "az", "ut", "id"))
    usa.link("ut")(Seq("id", "nv", "az", "co", "wy"))
    usa.link("az")(Seq("ca", "nv", "ut", "nm"))
    usa.link("mt")(Seq("id", "wy", "sd", "nd"))
    usa.link("wy")(Seq("mt", "id", "ut", "co", "ne", "sd"))
    usa.link("co")(Seq("wy", "ut", "nm", "ok", "ks", "ne"))
    usa.link("nm")(Seq("co", "az", "tx", "ok"))
    usa.link("nd")(Seq("mt", "sd", "mn"))
    usa.link("sd")(Seq("nd", "mt", "wy", "ne", "ia", "mn"))
    usa.link("ne")(Seq("sd", "wy", "co", "ks", "mo", "ia"))
    usa.link("ks")(Seq("ne", "co", "ok", "mo"))
    usa.link("ok")(Seq("ks", "co", "nm", "tx", "ar", "mo"))
    usa.link("tx")(Seq("ok", "nm", "la", "ar"))
    usa.link("mn")(Seq("nd", "sd", "ia", "wi"))
    usa.link("ia")(Seq("mn", "sd", "ne", "mo", "il", "wi"))
    usa.link("mo")(Seq("ia", "ne", "ks", "ok", "ar", "tn", "ky", "il"))
    usa.link("ar")(Seq("mo", "ok", "tx", "la", "ms", "tn"))
    usa.link("la")(Seq("ar", "tx", "ms"))
    usa.link("wi")(Seq("mn", "ia", "il"))
    usa.link("il")(Seq("wi", "ia", "mo", "ky", "in"))
    usa.link("tn")(Seq("ky", "mo", "ar", "ms", "al", "ga", "nc", "va"))
    usa.link("ms")(Seq("tn", "ar", "la", "al"))
    usa.link("mi")(Seq("in", "oh"))
    usa.link("in")(Seq("mi", "il", "ky", "oh"))
    usa.link("ky")(Seq("oh", "in", "il", "mo", "tn", "va", "wv"))
    usa.link("al")(Seq("tn", "ms", "fl", "ga"))
    usa.link("ga")(Seq("nc", "tn", "al", "fl", "sc"))
    usa.link("oh")(Seq("mi", "in", "ky", "wv", "pa"))
    usa.link("wv")(Seq("pa", "oh", "ky", "va", "md"))
    usa.link("ny")(Seq("pa", "nj", "ct", "ma", "vt"))
    usa.link("nj")(Seq("ny", "pa", "de"))
    usa.link("pa")(Seq("ny", "nj", "oh", "wv", "md", "de"))
    usa.link("va")(Seq("md", "wv", "ky", "tn", "nc", "wdc"))
    usa.link("nc")(Seq("va", "tn", "ga", "sc"))
    usa.link("sc")(Seq("nc", "ga"))
    usa.link("fl")(Seq("ga", "al"))
    usa.link("me")(Seq("nh"))
    usa.link("nh")(Seq("me", "vt", "ma"))
    usa.link("vt")(Seq("nh", "ny", "ma"))
    usa.link("ma")(Seq("nh", "vt", "ny", "ct", "ri"))
    usa.link("ct")(Seq("ma", "ny", "ri"))
    usa.link("ri")(Seq("ma", "ct"))
    usa.link("de")(Seq("pa", "md", "nj"))
    usa.link("md")(Seq("pa", "wv", "va", "de", "wdc"))
    usa.link("wdc")(Seq("md", "va"))
    usa
  }

}