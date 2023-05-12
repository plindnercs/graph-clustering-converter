package edu.plus.cs.graph;

import edu.plus.cs.interaction.Interaction;
import edu.plus.cs.util.Pair;

import java.util.*;

public class InteractionGraph {
    private LinkedHashMap<Long, Pair<UserVertex, Set<UserVertex>>> adjacencyLists;

    private HashMap<Long, Long> userVertexIdMapping;
    long nextFreeUserVertexId = 1;
    int numberOfEdges;

    public InteractionGraph() {
        this.adjacencyLists = new LinkedHashMap<>();
        this.userVertexIdMapping = new HashMap<>();
    }

    public void insertInteractionEdge(Interaction interaction) {
        if (!adjacencyLists.containsKey(interaction.getSourceId())) {
            // omit gender for now
            adjacencyLists.put(interaction.getSourceId(),
                    new Pair<>(new UserVertex(interaction.getSourceId(), null), new HashSet<>()));

            userVertexIdMapping.put(interaction.getSourceId(), nextFreeUserVertexId);
            nextFreeUserVertexId++;
        }

        if (!adjacencyLists.containsKey(interaction.getTargetId())) {
            // omit gender for now
            adjacencyLists.put(interaction.getTargetId(),
                    new Pair<>(new UserVertex(interaction.getTargetId(), null), new HashSet<>()));

            userVertexIdMapping.put(interaction.getTargetId(), nextFreeUserVertexId);
            nextFreeUserVertexId++;
        }

        Pair<UserVertex, Set<UserVertex>> userVertexPairFrom = adjacencyLists.get(interaction.getSourceId());
        Pair<UserVertex, Set<UserVertex>> userVertexPairTo = adjacencyLists.get(interaction.getTargetId());

        // add neighbour to set if not already there
        if (!userVertexPairFrom.getValue().contains(userVertexPairTo.getKey())) {
            userVertexPairFrom.getValue().add(userVertexPairTo.getKey());
            numberOfEdges++;
        }

        if (!userVertexPairTo.getValue().contains(userVertexPairFrom.getKey())) {
            userVertexPairTo.getValue().add(userVertexPairFrom.getKey());
            numberOfEdges++;
        }
    }

    public int getNumberOfVertices() {
        return this.adjacencyLists.keySet().size();
    }

    public int getNumberOfEdges() {
        return this.numberOfEdges;
    }

    public LinkedHashMap<Long, Pair<UserVertex, Set<UserVertex>>> getAdjacencyLists() {
        return adjacencyLists;
    }

    public HashMap<Long, Long> getUserVertexIdMapping() {
        return userVertexIdMapping;
    }
}
