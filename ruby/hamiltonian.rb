require './hamiltonian_reader'

class Hamiltonian
  include HamiltonianReader

  # Constructor
  def initialize( graph_of_states, states_to_visit )
    # The undirected graph of neighbouring states is represented as a hash of arrays.  The key
    # is used to identify a specific state.  The returned array provides the list of neighbouring
    # states.
    @graph = graph_of_states.clone
    @attempt = 0

    # Make the problem more manageable
    self.reduce_graph( states_to_visit )
    self.check_graph
  end

  # Little test method to ensure the graph is undirected: if :a is a neighbour of :b, then
  # :b must be a neighbour of :a
  def check_graph
    @graph.each_pair do | state, neighbours |
      neighbours.each do | neighbour |
        if @graph[ neighbour ].nil?
          puts "no graph entry for #{neighbour.to_s}"
        elsif !@graph[ neighbour ].include?( state )
          puts " #{state.to_s} -> #{neighbour.to_s} exists, but not #{neighbour.to_s} -> #{state.to_s}"
        end
      end
    end
  end

  # set things up for the recursive search.
  def find_hamiltonian( start )
    # Define a list of unvisited states.
    @unvisited = @graph.keys

    # We will store the journey taken in the array journey.
    @journey = []

    # start the recursive hunt
    unless find_hamiltonian_recursively(start)
      puts 'Hamiltonian does not exist'
    end
  end

  private

  # Find a hamiltonian cycle recursively.  Function returns true if a cycle has been found
  def find_hamiltonian_recursively( current )
    # add the current node to the journey
    @journey << current

    # remove the current node from the list of unvisited nodes.
    @unvisited.delete( current )

    if @unvisited.empty?
      # All states have been visisted.  Therefore, the @journey must
      # contain a valid Hamiltonian path.  Let's output it and return.
      puts "[#{@journey.map{|state| ":#{state.to_s}"}.join(', ')}]"
      return true
    else
      # Go through each of the neighbours, and check to see if they are in the unvisited
      # list
      @graph[ current ].each do |neighbour|
        if @unvisited.include?( neighbour )
          # possible path via this neighbour
          if find_hamiltonian_recursively( neighbour )
            return true
          end
        end
      end
    end

    # Return current to the unvisited list and remove from the journey, and return to allow
    # another route to be explored.
    @unvisited.push( current )
    @journey.pop

    return false
  end
end
