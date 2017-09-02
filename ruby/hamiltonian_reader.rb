module HamiltonianReader
  # Allows us to reduce the graph given a list of states.
  def reduce_graph( states )
    @graph.delete_if do |state, neighbours|
      neighbours.delete_if { |neighbour| !states.include?(neighbour) }
      !states.include?(state)
    end
  end
end
