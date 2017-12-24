class Hamiltonian {
	graph: Map<String, String[]> = new Map();
	journey: String[] = [];
  attempt = 0;

  constructor(graph_of_states: any, states_to_visit: String[] ) {
    // convert the JSON object into a true Map.
    for(var state of Object.getOwnPropertyNames(graph_of_states)) {
      this.graph.set(state, graph_of_states[state]);
    }
		
		this.reduce_graph(states_to_visit);
		this.check_graph();
  }

  reduce_graph(states_to_visit: String[]): void {
    // remove unwanted states.
    for(var state of this.graph.keys()) {
      if (states_to_visit.indexOf(state)<0) {
        this.graph.delete(state);
      } else {
        let neighbours = this.graph.get(state).filter(state => states_to_visit.indexOf(state) >= 0);
        this.graph.set(state, neighbours);
      }
    }
	}
	
	// Little test method to ensure the graph is undirected: if 'a' is a neighbour of 'b', then
  // 'b' must be a neighbour of 'a'
	check_graph(): void {
		for(var [state, neighbours] of this.graph) {
			for(var neighbour of neighbours) {
				if (this.graph.get(neighbour) == null) {
					console.log(`no graph entry for ${neighbour}`);
				} else if (this.graph.get(neighbour).indexOf(state)<0) {
					console.log(`${state} -> ${neighbour} exists, but not ${neighbour} -> ${state}`); 
				}
			}
		}
	}

	// set things up for the recursive search.
	find_hamiltonian(start: String): void {
		if (!this.find_hamiltonian_recursively(start)) {
			console.log('No hamiltonian path found.');
		}
	}

	find_hamiltonian_recursively(current: String): Boolean {
		this.journey.push(current);

		if (this.journey.length==this.graph.size) {
			// All states have been visisted.  Therefore, the @journey must
      // contain a valid Hamiltonian path.  Let's output it and return.
			console.log(this.journey.join('-> '));
			return true;
		} else {
			for(var neighbour of this.graph.get(current)) {
				// Go through each of the neighbours, and check to see if they are still to be visited
				if(this.journey.indexOf(neighbour)<0) {
					if (this.find_hamiltonian_recursively(neighbour)) {
						return true;
					}
				}
			}
		}

		// Not been able to find a hamiltonian given current journey from this node.  Pop and recurse back.
		this.journey.pop();
		return false;
	}
}

let usa = {
  "wa": ["or", "id"],
	"or": ["wa", "id", "nv", "ca"],
	"ca": ["or", "nv", "az"],
	"id": ["wa", "or", "nv", "ut", "wy", "mt"],
	"nv": ["or", "ca", "az", "ut", "id"],
	"ut": ["id", "nv", "az", "co", "wy"],
	"az": ["ca", "nv", "ut", "nm"],
	"mt": ["id", "wy", "sd", "nd"],
	"wy": ["mt", "id", "ut", "co", "ne", "sd"],
	"co": ["wy", "ut", "nm", "ok", "ks", "ne"],
	"nm": ["co", "az", "tx", "ok"],
	"nd": ["mt", "sd", "mn"],
	"sd": ["nd", "mt", "wy", "ne", "ia", "mn"],
	"ne": ["sd", "wy", "co", "ks", "mo", "ia"],
	"ks": ["ne", "co", "ok", "mo"],
	"ok": ["ks", "co", "nm", "tx", "ar", "mo"],
	"tx": ["ok", "nm", "la", "ar"],
	"mn": ["nd", "sd", "ia", "wi"],
	"ia": ["mn", "sd", "ne", "mo", "il", "wi"],
	"mo": ["ia", "ne", "ks", "ok", "ar", "tn", "ky", "il"],
	"ar": ["mo", "ok", "tx", "la", "ms", "tn"],
	"la": ["ar", "tx", "ms"],
	"wi": ["mn", "ia", "il"],
	"il": ["wi", "ia", "mo", "ky", "in"],
	"tn": ["ky", "mo", "ar", "ms", "al", "ga", "nc", "va", "ky"],
	"ms": ["tn", "ar", "la", "al", "tn"],
	"mi": ["in", "oh"],
	"in": ["mi", "il", "ky", "oh"],
	"ky": ["oh", "in", "il", "mo", "tn", "va", "wv", "oh"],
	"al": ["tn", "ms", "fl", "ga"],
	"ga": ["nc", "tn", "al", "fl", "sc"],
	"oh": ["mi", "in", "ky", "wv", "pa"],
	"wv": ["pa", "oh", "ky", "va", "md"],
	"ny": ["pa", "nj", "ct", "ma", "vt"],
	"nj": ["ny", "pa", "de"],
	"pa": ["ny", "nj", "oh", "wv", "md", "de"],
	"va": ["md", "wv", "ky", "tn", "nc", "wdc"],
	"nc": ["va", "tn", "ga", "sc"],
	"sc": ["nc", "ga"],
	"fl": ["ga", "al"],
	"me": ["nh"],
	"nh": ["me", "vt", "ma"],
	"vt": ["nh", "ny", "ma"],
	"ma": ["nh", "vt", "ny", "ct", "ri"],
	"ct": ["ma", "ny", "ri"],
	"ri": ["ma", "ct"],
	"de": ["pa", "md", "nj"],
	"md": ["pa", "wv", "va", "de", "wdc"],
	"wdc": ["md", "va"]
}

let eastern_states = [ "mn", "ia", "mo", "ar", "la", "wi", "il", "tn", "ms", "mi", "in", "ky", "al",
  "ga", "oh", "wv", "ny", "nj", "pa", "va", "nc", "sc", "fl", "me", "nh", "vt", "ma", "ct", "ri",
  "de", "md", "wdc" ];

for(var i=0; i<5; i++){
	var h = new Hamiltonian(usa, eastern_states);
	console.time('hamiltonian');
	h.find_hamiltonian('wdc');
	console.timeEnd('hamiltonian');
}
