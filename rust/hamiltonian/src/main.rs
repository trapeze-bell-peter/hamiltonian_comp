use std::env;
use std::collections::HashMap;
use std::collections::VecDeque;
use std::time::Instant;

#[derive(Debug)]
struct Node {
	state: String,
	links: Vec<String>
}

impl Node {
	fn add(&mut self, other: String) {
		self.links.push(other);
	}
	fn mk(state: String) -> Node { Node {state: state, links: Vec::new()}  }
	fn retain(&mut self, selected: &Vec<String>) {
		self.links.retain(|x| selected.contains(x));
	}
}

#[derive(Debug)]
struct Graph {
	nodes: HashMap<String, Node>
}
impl Graph {
	fn link(&mut self, root: &String, links:Vec<String>) {
		let root_node = self.nodes.entry(String::from(root)).or_insert(Node::mk(String::from(root)));
		for l in links {
			root_node.add(String::from(l));
		}
	}
	fn link_str(&mut self, root: &str, links:Vec<&str>) {
		self.link(&root.to_string(), links.iter().map(|x| x.to_string()).collect())
	}
	fn reduce(&mut self, selected: Vec<String>) {
		self.nodes.retain(|k, _| selected.contains(k) );
		for node in self.nodes.values_mut() {
			node.retain(&selected);
		}
	}
	fn find_hamiltonian(&self, starting_node: &str) -> Option<Vec<String>> {
		self.find_hamiltonian_r(starting_node, &mut VecDeque::new())
	}
	fn find_hamiltonian_r<'a>(&'a self, starting_node: &'a str, visited: &mut VecDeque<String>) -> Option<Vec<String>> { 
		visited.push_back(starting_node.to_string());
		if visited.len() == self.nodes.len() {
			return Some(visited.into_iter().map(|x| x.to_string()).collect());
		} else {
			for node in self.nodes.get(starting_node).iter().flat_map(|x| x.links.iter() ) {
				if !visited.contains(node) {
					let r = self.find_hamiltonian_r(node, visited);
					if r.is_some() {
						return r;
					}
				}
			}
		}
		visited.pop_back();
		None
	}

	fn mk() -> Graph {Graph { nodes: HashMap::new()}}
}
fn main() {
	let mut graph = Graph::mk();
	graph.link_str("wa", vec!["or", "id"]);
	graph.link_str("or", vec!["wa", "id", "nv", "ca"]);
	graph.link_str("ca", vec!["or", "nv", "az"]);
	graph.link_str("id", vec!["wa", "or", "nv", "ut", "wy", "mt"]);
	graph.link_str("nv", vec!["or", "ca", "az", "ut", "id"]);
	graph.link_str("ut", vec!["id", "nv", "az", "co", "wy"]);
	graph.link_str("az", vec!["ca", "nv", "ut", "nm"]);
	graph.link_str("mt", vec!["id", "wy", "sd", "nd"]);
	graph.link_str("wy", vec!["mt", "id", "ut", "co", "ne", "sd"]);
	graph.link_str("co", vec!["wy", "ut", "nm", "ok", "ks", "ne"]);
	graph.link_str("nm", vec!["co", "az", "tx", "ok"]);
	graph.link_str("nd", vec!["mt", "sd", "mn"]);
	graph.link_str("sd", vec!["nd", "mt", "wy", "ne", "ia", "mn"]);
	graph.link_str("ne", vec!["sd", "wy", "co", "ks", "mo", "ia"]);
	graph.link_str("ks", vec!["ne", "co", "ok", "mo"]);
	graph.link_str("ok", vec!["ks", "co", "nm", "tx", "ar", "mo"]);
	graph.link_str("tx", vec!["ok", "nm", "la", "ar"]);
	graph.link_str("mn", vec!["nd", "sd", "ia", "wi"]);
	graph.link_str("ia", vec!["mn", "sd", "ne", "mo", "il", "wi"]);
	graph.link_str("mo", vec!["ia", "ne", "ks", "ok", "ar", "tn", "ky", "il"]);
	graph.link_str("ar", vec!["mo", "ok", "tx", "la", "ms", "tn"]);
	graph.link_str("la", vec!["ar", "tx", "ms"]);
	graph.link_str("wi", vec!["mn", "ia", "il"]);
	graph.link_str("il", vec!["wi", "ia", "mo", "ky", "in"]);
	graph.link_str("tn", vec!["ky", "mo", "ar", "ms", "al", "ga", "nc", "va"]);
	graph.link_str("ms", vec!["tn", "ar", "la", "al"]);
	graph.link_str("mi", vec!["in", "oh"]);
	graph.link_str("in", vec!["mi", "il", "ky", "oh"]);
	graph.link_str("ky", vec!["oh", "in", "il", "mo", "tn", "va", "wv"]);
	graph.link_str("al", vec!["tn", "ms", "fl", "ga"]);
	graph.link_str("ga", vec!["nc", "tn", "al", "fl", "sc"]);
	graph.link_str("oh", vec!["mi", "in", "ky", "wv", "pa"]);
	graph.link_str("wv", vec!["pa", "oh", "ky", "va", "md"]);
	graph.link_str("ny", vec!["pa", "nj", "ct", "ma", "vt"]);
	graph.link_str("nj", vec!["ny", "pa", "de"]);
	graph.link_str("pa", vec!["ny", "nj", "oh", "wv", "md", "de"]);
	graph.link_str("va", vec!["md", "wv", "ky", "tn", "nc", "wdc"]);
	graph.link_str("nc", vec!["va", "tn", "ga", "sc"]);
	graph.link_str("sc", vec!["nc", "ga"]);
	graph.link_str("fl", vec!["ga", "al"]);
	graph.link_str("me", vec!["nh"]);
	graph.link_str("nh", vec!["me", "vt", "ma"]);
	graph.link_str("vt", vec!["nh", "ny", "ma"]);
	graph.link_str("ma", vec!["nh", "vt", "ny", "ct", "ri"]);
	graph.link_str("ct", vec!["ma", "ny", "ri"]);
	graph.link_str("ri", vec!["ma", "ct"]);
	graph.link_str("de", vec!["pa", "md", "nj"]);
	graph.link_str("md", vec!["pa", "wv", "va", "de", "wdc"]);
	graph.link_str("wdc", vec!["md", "va"]);
	let easter = vec!["mn", "ia", "mo", "ar", "la", "wi", "il", "tn", "ms", "mi", "in", "ky", "al",
                "ga", "oh", "wv", "ny", "nj", "pa", "va", "nc", "sc", "fl", "me", "nh", "vt", "ma", "ct", "ri",
                "de", "md", "wdc"];
    graph.reduce(easter.iter().map(|x| x.to_string()).collect());
    let args: Vec<String> = env::args().collect();
    let start = if args.is_empty() {  "wdc" }  else { &args[0] };
    let now = Instant::now();
    let path = graph.find_hamiltonian("ri");
    println!("found path: {:?}", path);
    println!("{}us", now.elapsed().as_micros());
}