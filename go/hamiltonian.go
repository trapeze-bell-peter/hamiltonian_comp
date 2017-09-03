package main

import "fmt"
import "sort"
import "time"

func main() {
	sort.Strings(easternHalf)
	reduceGraph()

	for i := 0;  i<5; i++ {
		unvisited := make(map[string]bool)
		for state := range USA {
			unvisited[state] = true
		}

		start := time.Now()
		find_hamiltonian_recursively("wdc", make([]string, 0), unvisited)
		fmt.Printf("Time to search for hamiltonian %s", time.Since(start))
  }
}

// Allows us to reduce the graph given a list of states.
func reduceGraph() {
	for state, neighbours := range USA {
		if !isWantedState(state) {
			delete(USA, state)
		} else {
			USA[state] = removeUnwantedNeighbours(neighbours)
		}
	}
}

// search through the set of
func isWantedState(state string) bool {
	pos := sort.SearchStrings(easternHalf, state)
	return pos<len(easternHalf) && easternHalf[pos]==state
}

func removeUnwantedNeighbours(neighbours []string) []string {
	var new_neighbours []string

	for _, neighbour := range neighbours {
		if isWantedState(neighbour) {
			new_neighbours = append(new_neighbours, neighbour)
		}
	}

	return new_neighbours
}

// Find a hamiltonian cycle recursively.  Function returns true if a cycle has
// been found
func find_hamiltonian_recursively(current string, journey []string, unvisited map[string]bool) bool {
	// add the current node to the journey
	journey = append(journey, current)

	// remove the current node from the list of unvisited nodes.
	delete(unvisited, current)

	if len(unvisited)==0 {
		// All states have been visisted.  Therefore, the journey must
		// contain a valid Hamiltonian path.  Let's output it and return.
		fmt.Println(journey)
		return true
	} else {
		// Go through each of the neighbours, and check to see if they are in the unvisited
		// list
		for _, neighbour := range USA[current] {
			if _, unvisited_state := unvisited[neighbour]; unvisited_state {
				// possible path via this neighbour
				if find_hamiltonian_recursively(neighbour, journey, unvisited) {
					return true
				}
			}
		}
	}

	unvisited[current] = true
	journey = journey[:len(journey)-1]

	return false
}


func printGraph() {
	for state, neighbours := range USA {
		fmt.Println("Key:", state, "Value:", neighbours)
	}
}
// USA graph showing for each state what its immediate neighbours are.
var USA = map[string][]string{
	"wa":  {"or", "id"},
	"or":  {"wa", "id", "nv", "ca"},
	"ca":  {"or", "nv", "az"},
	"id":  {"wa", "or", "nv", "ut", "wy", "mt"},
	"nv":  {"or", "ca", "az", "ut", "id"},
	"ut":  {"id", "nv", "az", "co", "wy"},
	"az":  {"ca", "nv", "ut", "nm"},
	"mt":  {"id", "wy", "sd", "nd"},
	"wy":  {"mt", "id", "ut", "co", "ne", "sd"},
	"co":  {"wy", "ut", "nm", "ok", "ks", "ne"},
	"nm":  {"co", "az", "tx", "ok"},
	"nd":  {"mt", "sd", "mn"},
	"sd":  {"nd", "mt", "wy", "ne", "ia", "mn"},
	"ne":  {"sd", "wy", "co", "ks", "mo", "ia"},
	"ks":  {"ne", "co", "ok", "mo"},
	"ok":  {"ks", "co", "nm", "tx", "ar", "mo"},
	"tx":  {"ok", "nm", "la", "ar"},
	"mn":  {"nd", "sd", "ia", "wi"},
	"ia":  {"mn", "sd", "ne", "mo", "il", "wi"},
	"mo":  {"ia", "ne", "ks", "ok", "ar", "tn", "ky", "il"},
	"ar":  {"mo", "ok", "tx", "la", "ms", "tn"},
	"la":  {"ar", "tx", "ms"},
	"wi":  {"mn", "ia", "il"},
	"il":  {"wi", "ia", "mo", "ky", "in"},
	"tn":  {"ky", "mo", "ar", "ms", "al", "ga", "nc", "va", "ky"},
	"ms":  {"tn", "ar", "la", "al", "tn"},
	"mi":  {"in", "oh"},
	"in":  {"mi", "il", "ky", "oh"},
	"ky":  {"oh", "in", "il", "mo", "tn", "va", "wv", "oh"},
	"al":  {"tn", "ms", "fl", "ga"},
	"ga":  {"nc", "tn", "al", "fl", "sc"},
	"oh":  {"mi", "in", "ky", "wv", "pa"},
	"wv":  {"pa", "oh", "ky", "va", "md"},
	"ny":  {"pa", "nj", "ct", "ma", "vt"},
	"nj":  {"ny", "pa", "de"},
	"pa":  {"ny", "nj", "oh", "wv", "md", "de"},
	"va":  {"md", "wv", "ky", "tn", "nc", "wdc"},
	"nc":  {"va", "tn", "ga", "sc"},
	"sc":  {"nc", "ga"},
	"fl":  {"ga", "al"},
	"me":  {"nh"},
	"nh":  {"me", "vt", "ma"},
	"vt":  {"nh", "ny", "ma"},
	"ma":  {"nh", "vt", "ny", "ct", "ri"},
	"ct":  {"ma", "ny", "ri"},
	"ri":  {"ma", "ct"},
	"de":  {"pa", "md", "nj"},
	"md":  {"pa", "wv", "va", "de", "wdc"},
	"wdc": {"md", "va"},
}

// eastern_half the states in the eastern half of continental US to visit.
var easternHalf = []string{
	"mn", "ia", "mo", "ar", "la", "wi", "il", "tn", "ms", "mi", "in", "ky", "al", "ga", "oh", "wv", "ny",
	"nj", "pa", "va", "nc", "sc", "fl", "me", "nh", "vt", "ma", "ct", "ri", "de", "md", "wdc",
}
