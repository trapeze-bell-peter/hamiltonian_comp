class Hamiltonian
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

  # Allows us to reduce the graph given a list of states.
  def reduce_graph
    @graph.delete_if do |state, neighbours|
      neighbours.reject! { |neighbour| !@states_to_visit.includes?(neighbour) }
      !@states_to_visit.includes?(state)
    end
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

usa = {
  :wa => [ :or, :id],
  :or => [ :wa, :id, :nv, :ca],
  :ca => [ :or, :nv, :az ],
  :id => [ :wa, :or, :nv, :ut, :wy, :mt ],
  :nv => [ :or, :ca, :az, :ut, :id],
  :ut => [ :id, :nv, :az, :co, :wy ],
  :az => [ :ca, :nv, :ut, :nm ],
  :mt => [ :id, :wy, :sd, :nd ],
  :wy => [ :mt, :id, :ut, :co, :ne, :sd ],
  :co => [ :wy, :ut, :nm, :ok, :ks, :ne ],
  :nm => [ :co, :az, :tx, :ok ],
  :nd => [ :mt, :sd, :mn ],
  :sd => [ :nd, :mt, :wy, :ne, :ia, :mn],
  :ne => [ :sd, :wy, :co, :ks, :mo, :ia],
  :ks => [ :ne, :co, :ok, :mo ],
  :ok => [ :ks, :co, :nm, :tx, :ar, :mo ],
  :tx => [ :ok, :nm, :la, :ar ],
  :mn => [ :nd, :sd, :ia, :wi ],
  :ia => [ :mn, :sd, :ne, :mo, :il, :wi ],
  :mo => [ :ia, :ne, :ks, :ok, :ar, :tn, :ky, :il ],
  :ar => [ :mo, :ok, :tx, :la, :ms, :tn ],
  :la => [ :ar, :tx, :ms ],
  :wi => [ :mn, :ia, :il ],
  :il => [ :wi, :ia, :mo, :ky, :in ],
  :tn => [ :ky, :mo, :ar, :ms, :al, :ga, :nc, :va, :ky ],
  :ms => [ :tn, :ar, :la, :al, :tn ],
  :mi => [ :in, :oh ],
  :in => [ :mi, :il, :ky, :oh ],
  :ky => [ :oh, :in, :il, :mo, :tn, :va, :wv, :oh ],
  :al => [ :tn, :ms, :fl, :ga ],
  :ga => [ :nc, :tn, :al, :fl, :sc ],
  :oh => [ :mi, :in, :ky, :wv, :pa ],
  :wv => [ :pa, :oh, :ky, :va, :md ],
  :ny => [ :pa, :nj, :ct, :ma, :vt ],
  :nj => [ :ny, :pa, :de ],
  :pa => [ :ny, :nj, :oh, :wv, :md, :de ],
  :va => [ :md, :wv, :ky, :tn, :nc, :wdc ],
  :nc => [ :va, :tn, :ga, :sc ],
  :sc => [ :nc, :ga ],
  :fl => [ :ga, :al ],
  :me => [ :nh ],
  :nh => [ :me, :vt, :ma ],
  :vt => [ :nh, :ny, :ma ],
  :ma => [ :nh, :vt, :ny, :ct, :ri ],
  :ct => [ :ma, :ny, :ri ],
  :ri => [ :ma, :ct ],
  :de => [ :pa, :md, :nj ],
  :md => [ :pa, :wv, :va, :de, :wdc ],
  :wdc => [ :md, :va ]
}

eastern_half = [ :mn, :ia, :mo, :ar, :la, :wi, :il, :tn, :ms, :mi, :in, :ky, :al, :ga, :oh, :wv, :ny, :nj, :pa, :va,
               :nc, :sc, :fl, :me, :nh, :vt, :ma, :ct, :ri, :de, :md, :wdc ]

trip1 = Hamiltonian.new( usa, eastern_half )
trip1.find_hamiltonian( :wdc )
