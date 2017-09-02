module HamiltonianReader
  # Allows us to reduce the graph given a list of states.
  def reduce_graph
    @graph.delete_if do |state, neighbours|
      neighbours.reject! { |neighbour| !@states_to_visit.includes?(neighbour) }
      !@states_to_visit.includes?(state)
    end
  end
end
