using System;
using System.Diagnostics;

namespace hamiltonian {
    class Program {
        static void Main(string[] args) {
            var hamiltonian = new Hamiltonian();
            var starting = "wdc";
            if(args.Length > 0) {
                starting = args[0];
            }
            for (var i = 0; i < 30; i++) {
                var stopwatch = Stopwatch.StartNew();
                hamiltonian.FindHamiltonian(starting);
                stopwatch.Stop();
                Console.WriteLine($"Execution time:  {stopwatch.Elapsed}");
            }
        }
    }
}
