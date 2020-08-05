import "dart:core";
import "dart:collection";

class Hamiltonian {

  var _usa;
  var _easternStates;

  HashSet<String> _visited = new HashSet<String>();
  Queue<String> _journey = new Queue<String>();

  Hamiltonian() {
    InitialiseStates();
    ReduceGraph();
  }


  void FindHamiltonian(String start)
  {
    _journey.clear();
    FindHamiltonianRecursively(start);
  }

  bool FindHamiltonianRecursively(String current) {
    _journey.addLast(current);
    _visited.add(current);
    if (_journey.length == _usa.length) {
      _visited.clear();
      return(true);
    }
    else
    {
      if (_usa[current]!=null) {
        for(String neighbour in _usa[current]) {
          if (_visited.contains(neighbour)) continue;
          if (FindHamiltonianRecursively(neighbour)) return true;
        }
      }
    }
    _journey.removeLast();
    _visited.remove(current);
    return false;
  }

  void ReduceGraph() {
    Map<String,List<String>> _reduced = {};
    _easternStates.forEach( (s) => _reduced[s] = _usa[s]);
    _usa = _reduced;
  }

  void InitialiseStates() {
      _usa = {
        'wa' : ['or','id'],
        'or' : ['wa', 'id', 'nv', 'ca'],
        'ca' : ['or', 'nv', 'az'],
        'id' : ['wa', 'or', 'nv', 'ut', 'wy', 'mt'],
        'nv' : ['or', 'ca', 'az', 'ut', 'id'],
        'ut' : ['id', 'nv', 'az', 'co', 'wy'],
        'az' : ['ca', 'nv', 'ut', 'nm'],
        'mt' : ['id', 'wy', 'sd', 'nd'],
        'wy' : ['mt', 'id', 'ut', 'co', 'ne', 'sd'],
        'co' : ['wy', 'ut', 'nm', 'ok', 'ks', 'ne'],
        'nm' : ['co', 'az', 'tx', 'ok'],
        'nd' : ['mt', 'sd', 'mn'],
        'sd' : ['nd', 'mt', 'wy', 'ne', 'ia', 'mn'],
        'ne' : ['sd', 'wy', 'co', 'ks', 'mo', 'ia'],
        'ks' : ['ne', 'co', 'ok', 'mo'],
        'ok' : ['ks', 'co', 'nm', 'tx', 'ar', 'mo'],
        'tx' : ['ok', 'nm', 'la', 'ar'],
        'mn' : ['nd', 'sd', 'ia', 'wi'],
        'ia' : ['mn', 'sd', 'ne', 'mo', 'il', 'wi'],
        'mo' : ['ia', 'ne', 'ks', 'ok', 'ar', 'tn', 'ky', 'il'],
        'ar' : ['mo', 'ok', 'tx', 'la', 'ms', 'tn'],
        'la' : ['ar', 'tx', 'ms'],
        'wi' : ['mn', 'ia', 'il'],
        'il' : ['wi', 'ia', 'mo', 'ky', 'in'],
        'tn' : ['ky', 'mo', 'ar', 'ms', 'al', 'ga', 'nc', 'va', 'ky'],
        'ms' : ['tn', 'ar', 'la'],
        'mi' : ['in', 'oh'],
        'in' : ['mi', 'il', 'ky', 'oh'],
        'ky' : ['oh', 'in', 'il', 'mo', 'tn', 'va', 'wv'],
        'al' : ['tn', 'ms', 'fl', 'ga'],
        'ga' : ['nc', 'tn', 'al', 'fl', 'sc'],
        'oh' : ['mi', 'in', 'ky', 'wv', 'pa'],
        'wv' : ['pa', 'oh', 'ky', 'va', 'md'],
        'ny' : ['pa', 'nj', 'ct', 'ma', 'vt'],
        'nj' : ['ny', 'pa', 'de'],
        'pa' : ['ny', 'nj', 'oh', 'wv', 'md', 'de'],
        'va' : ['md', 'wv', 'ky', 'tn', 'nc', 'wdc'],
        'nc' : ['va', 'tn', 'ga', 'sc'],
        'sc' : ['nc', 'ga'],
        'fl' : ['ga', 'al'],
        'me' : ['nh'],
        'nh' : ['me', 'vt', 'ma'],
        'vt' : ['nh', 'ny', 'ma'],
        'ma' : ['nh', 'vt', 'ny', 'ct', 'ri'],
        'ct' : ['ma', 'ny', 'ri'],
        'ri' : ['ma', 'ct'],
        'de' : ['pa', 'md', 'nj'],
        'md' : ['pa', 'wv', 'va', 'de', 'wdc'],
        'wdc' : ['md', 'va']
      };

      _easternStates = ['mn', 'ia', 'mo', 'ar', 'la', 'wi', 'il', 'tn', 'ms', 'mi', 'in', 'ky', 'al','ga', 'oh', 'wv', 'ny', 'nj', 'pa', 'va', 'nc', 'sc', 'fl', 'me', 'nh', 'vt', 'ma', 'ct', 'ri','de', 'md', 'wdc'];

  }

  String result() {
    return _journey.join('->');
  }
}

void main() {
  var hamiltonian = new Hamiltonian();
  for(int i=0; i<30; i++) {
    var stopwatch = Stopwatch();
    stopwatch.start();
    hamiltonian.FindHamiltonian('wdc');
    stopwatch.stop();
    Duration duration = stopwatch.elapsed;
    print("Duration: " + duration.toString() + "     " + hamiltonian.result());
  }
}