module HamiltonianReader
  # Allows us to reduce the graph given a list of states.
  def reduce_graph( states )
    @graph.delete_if do |state, neighbours|
      neighbours.delete_if { |neighbour| !states.include?(neighbour) }
      !states.include?(state)
    end
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

end
