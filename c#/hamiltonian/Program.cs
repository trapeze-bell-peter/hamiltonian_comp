using System;
using System.Diagnostics;

namespace hamiltonian {
    class Program {
        static void Main(string[] args) {
            var hamiltonian = new Hamiltonian();
            
            for (var i = 0; i < 30; i++) {
                var stopwatch = Stopwatch.StartNew();
                hamiltonian.FindHamiltonian("wdc");
                stopwatch.Stop();
                Console.WriteLine($"Execution time:  {stopwatch.Elapsed}");
            }
        }
    }
}
