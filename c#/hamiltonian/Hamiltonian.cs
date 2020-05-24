using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using static System.Array;

namespace hamiltonian
{
    public class Hamiltonian
    {
        private Dictionary<string, List<string>> _usa;
        private Array _easternStates;
        private Stack<string> _journey;
        
        public Hamiltonian()
        {
            InitialiseStates();
            ReduceGraph();
        }

        private void ReduceGraph() {
            foreach (var (state, neighbours) in _usa) {
                if (((IList) _easternStates).Contains(state)) {
                    neighbours.RemoveAll(neighbour => !((IList) _easternStates).Contains(neighbour));
                } else {
                    _usa.Remove(state);
                }
            }
        }

        public void PrintGraph() {
            foreach (var (state, neighbours) in _usa) {
                Console.WriteLine($"{state}: {String.Join(", ", neighbours)}");
            }
        }

        public void FindHamiltonian(string start) {
            _journey = new Stack<string>(26);
            
            if (!FindHamiltonianRecursively(start)) {
                Console.WriteLine("No hamiltonian path found.");
            }
        }
        private bool FindHamiltonianRecursively(string current) {
            _journey.Push(current);

            if (_journey.Count == _usa.Count) {
                Console.WriteLine(String.Join("->", _journey.Reverse()));
                return true;
            } else {
                foreach (var neighbour in _usa[current]) {
                    if (_journey.Contains(neighbour)) continue;
                    if (FindHamiltonianRecursively((neighbour))) return true;
                }
            }

            _journey.Pop();
            return false;
        }
        
        private void InitialiseStates() {
            _usa = new Dictionary<string, List<string>> {
                {"wa", new List<string> {"or", "id"}},
                {"or", new List<string> {"wa", "id", "nv", "ca"}},
                {"ca", new List<string> {"or", "nv", "az"}},
                {"id", new List<string> {"wa", "or", "nv", "ut", "wy", "mt"}},
                {"nv", new List<string> {"or", "ca", "az", "ut", "id"}},
                {"ut", new List<string> {"id", "nv", "az", "co", "wy"}},
                {"az", new List<string> {"ca", "nv", "ut", "nm"}},
                {"mt", new List<string> {"id", "wy", "sd", "nd"}},
                {"wy", new List<string> {"mt", "id", "ut", "co", "ne", "sd"}},
                {"co", new List<string> {"wy", "ut", "nm", "ok", "ks", "ne"}},
                {"nm", new List<string> {"co", "az", "tx", "ok"}},
                {"nd", new List<string> {"mt", "sd", "mn"}},
                {"sd", new List<string> {"nd", "mt", "wy", "ne", "ia", "mn"}},
                {"ne", new List<string> {"sd", "wy", "co", "ks", "mo", "ia"}},
                {"ks", new List<string> {"ne", "co", "ok", "mo"}},
                {"ok", new List<string> {"ks", "co", "nm", "tx", "ar", "mo"}},
                {"tx", new List<string> {"ok", "nm", "la", "ar"}},
                {"mn", new List<string> {"nd", "sd", "ia", "wi"}},
                {"ia", new List<string> {"mn", "sd", "ne", "mo", "il", "wi"}},
                {"mo", new List<string> {"ia", "ne", "ks", "ok", "ar", "tn", "ky", "il"}},
                {"ar", new List<string> {"mo", "ok", "tx", "la", "ms", "tn"}},
                {"la", new List<string> {"ar", "tx", "ms"}},
                {"wi", new List<string> {"mn", "ia", "il"}},
                {"il", new List<string> {"wi", "ia", "mo", "ky", "in"}},
                {"tn", new List<string> {"ky", "mo", "ar", "ms", "al", "ga", "nc", "va", "ky"}},
                {"ms", new List<string> {"tn", "ar", "la", "al", "tn"}},
                {"mi", new List<string> {"in", "oh"}},
                {"in", new List<string> {"mi", "il", "ky", "oh"}},
                {"ky", new List<string> {"oh", "in", "il", "mo", "tn", "va", "wv", "oh"}},
                {"al", new List<string> {"tn", "ms", "fl", "ga"}},
                {"ga", new List<string> {"nc", "tn", "al", "fl", "sc"}},
                {"oh", new List<string> {"mi", "in", "ky", "wv", "pa"}},
                {"wv", new List<string> {"pa", "oh", "ky", "va", "md"}},
                {"ny", new List<string> {"pa", "nj", "ct", "ma", "vt"}},
                {"nj", new List<string> {"ny", "pa", "de"}},
                {"pa", new List<string> {"ny", "nj", "oh", "wv", "md", "de"}},
                {"va", new List<string> {"md", "wv", "ky", "tn", "nc", "wdc"}},
                {"nc", new List<string> {"va", "tn", "ga", "sc"}},
                {"sc", new List<string> {"nc", "ga"}},
                {"fl", new List<string> {"ga", "al"}},
                {"me", new List<string> {"nh"}},
                {"nh", new List<string> {"me", "vt", "ma"}},
                {"vt", new List<string> {"nh", "ny", "ma"}},
                {"ma", new List<string> {"nh", "vt", "ny", "ct", "ri"}},
                {"ct", new List<string> {"ma", "ny", "ri"}},
                {"ri", new List<string> {"ma", "ct"}},
                {"de", new List<string> {"pa", "md", "nj"}},
                {"md", new List<string> {"pa", "wv", "va", "de", "wdc"}},
                {"wdc", new List<string> {"md", "va"}}
            };
            
            _easternStates = new[] {
                "mn", "ia", "mo", "ar", "la", "wi", "il", "tn", "ms", "mi", "in", "ky", "al",
                "ga", "oh", "wv", "ny", "nj", "pa", "va", "nc", "sc", "fl", "me", "nh", "vt", "ma", "ct", "ri",
                "de", "md", "wdc"
            };

        }
    }
}