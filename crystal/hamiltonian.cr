require "./hamiltonian_reader"

class Hamiltonian
  include HamiltonianReader

  # The undirected graph of neighbouring states is represented as a hash of arrays.  The key
  # is used to identify a specific state.  The returned array provides the list of neighbouring
  # states.
  @graph : Hash(Symbol, Array(Symbol))

  # Keep track of what we have not visited yet.
  @unvisited : Array(Symbol)

  # Constructor
  def initialize( @graph : Hash(Symbol, Array(Symbol)), @states_to_visit : Array(Symbol) )
    # Make the problem more manageable
    reduce_graph

    # Define a list of unvisited states.
    @unvisited = @graph.keys

    # We will store the journey taken in the array journey.
    @journey = [] of Symbol
  end

  # set things up for the recursive search.
  def find_hamiltonian( start )
    # start the recursive hunt
    unless find_hamiltonian_recursively(start)
      puts "Hamiltonian does not exist"
    end
  end

  # Find a hamiltonian cycle recursively.  Function returns true
  # if a cycle has been found
  private def find_hamiltonian_recursively( current )
    # add the current node to the journey
    @journey << current

    # remove the current node from the list of unvisited nodes.
    @unvisited.delete( current )

    if @unvisited.empty?
      # All states have been visisted.  Therefore, the @journey must
      # contain a valid Hamiltonian path.  Let's output it and return.
      puts "complete journey = #{@journey.join(" -> ") }"
      return true
    else
      # Go through each of the neighbours, and check to see if they are in the unvisited
      # list
      @graph[ current ].each do |neighbour|
        if @unvisited.includes?( neighbour )
          # possible path via this neighbour
          if find_hamiltonian_recursively( neighbour )
            return true
          end
        end
      end
    end

    @unvisited.push( current )
    @journey.pop

    return false
  end
end
