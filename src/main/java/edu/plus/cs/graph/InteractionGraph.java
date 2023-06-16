package edu.plus.cs.graph;

import edu.plus.cs.interaction.Interaction;
import edu.plus.cs.util.Pair;

import java.util.*;

public class InteractionGraph {
    private LinkedHashMap<Long, Pair<UserVertex, Map<UserVertex, Integer>>> adjacencyLists;

    private LinkedHashMap<Long, UserVertex> userVertices;

    private HashMap<Long, Long> userVertexIdMapping;
    long nextFreeUserVertexId = 1;
    int numberOfEdges;

    public InteractionGraph() {
        this.adjacencyLists = new LinkedHashMap<>();
        this.userVertices = new LinkedHashMap<>();
        this.userVertexIdMapping = new HashMap<>();
    }

    public void insertInteractionEdge(Interaction interaction) {
        if (!adjacencyLists.containsKey(interaction.getSourceId())) {
            // create user vertex
            UserVertex userVertex = new UserVertex(interaction.getSourceId(), null);
            userVertices.put(userVertex.getKey(), userVertex);

            // omit gender for now
            adjacencyLists.put(interaction.getSourceId(), new Pair<>(userVertex, new HashMap<>()));

            userVertexIdMapping.put(interaction.getSourceId(), nextFreeUserVertexId);
            nextFreeUserVertexId++;
        }

        if (!adjacencyLists.containsKey(interaction.getTargetId())) {
            // create user vertex
            UserVertex userVertex = new UserVertex(interaction.getTargetId(), null);
            userVertices.put(userVertex.getKey(), userVertex);

            // omit gender for now
            adjacencyLists.put(interaction.getTargetId(), new Pair<>(userVertex, new HashMap<>()));

            userVertexIdMapping.put(interaction.getTargetId(), nextFreeUserVertexId);
            nextFreeUserVertexId++;
        }

        Pair<UserVertex, Map<UserVertex, Integer>> userVertexPairFrom = adjacencyLists.get(interaction.getSourceId());
        Pair<UserVertex, Map<UserVertex, Integer>> userVertexPairTo = adjacencyLists.get(interaction.getTargetId());

        // add neighbour to set if not already there
        if (!userVertexPairFrom.getValue().containsKey(userVertexPairTo.getKey())) {
            userVertexPairFrom.getValue().put(userVertexPairTo.getKey(), 1);

            // do not count undirected edges twice
            if (!userVertexPairTo.getValue().containsKey(userVertexPairFrom.getKey())) {
                numberOfEdges++;
            }
        } else {
            // increment cardinality counter for verification afterwards
            int cardinalityCounter = userVertexPairFrom.getValue().get(userVertexPairTo.getKey());
            userVertexPairFrom.getValue().put(userVertexPairTo.getKey(), ++cardinalityCounter);
        }

        if (!userVertexPairTo.getValue().containsKey(userVertexPairFrom.getKey())) {
            userVertexPairTo.getValue().put(userVertexPairFrom.getKey(), 0);

            // do not count undirected edges twice
            if (!userVertexPairFrom.getValue().containsKey(userVertexPairTo.getKey())) {
                numberOfEdges++;
            }
        }
    }

    public int getNumberOfVertices() {
        return this.userVertices.size();
    }

    public int getNumberOfEdges() {
        return this.numberOfEdges;
    }

    public LinkedHashMap<Long, Pair<UserVertex, Map<UserVertex, Integer>>> getAdjacencyLists() {
        return adjacencyLists;
    }

    public HashMap<Long, Long> getUserVertexIdMapping() {
        return userVertexIdMapping;
    }

    public UserVertex getUserVertex(Long userId) {
        return userVertices.get(userId);
    }

    public Map<Long, UserVertex> getUserVertices() {
        return userVertices;
    }
}
