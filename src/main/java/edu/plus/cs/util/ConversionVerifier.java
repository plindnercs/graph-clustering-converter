package edu.plus.cs.util;

import edu.plus.cs.graph.InteractionGraph;
import edu.plus.cs.graph.UserVertex;
import edu.plus.cs.interaction.Interaction;
import edu.plus.cs.interaction.io.InteractionFileReader;
import edu.plus.cs.metis.io.MetisFileReader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class ConversionVerifier {

    public static boolean verifyConversion(InteractionGraph interactionGraph, File inputFile,
                                           File outputFile, int verifyNthEdge) throws IOException {
        Map<Long, Long> userIdMapping = interactionGraph.getUserVertexIdMapping();

        // read line of input file
        InteractionFileReader interactionFileReader = new InteractionFileReader(inputFile);
        // read output metis file
        MetisFileReader metisFileReader = new MetisFileReader(outputFile);

        long edgeCounter = 0;
        Optional<Interaction> optionalInteraction = interactionFileReader.readLineAsInteraction();
        while (optionalInteraction.isPresent()) {
            edgeCounter++;

            Interaction interaction = optionalInteraction.get();

            // search entry for target user vertex in adjacencyList of source user vertex
            UserVertex targetUserVertex = interactionGraph.getUserVertex(interaction.getTargetId());

            // decrement cardinalityCounter
            int cardinalityCounter = interactionGraph.getAdjacencyLists().get(interaction.getSourceId()).getValue()
                    .get(targetUserVertex);

            interactionGraph.getAdjacencyLists().get(interaction.getSourceId()).getValue().put(targetUserVertex,
                    --cardinalityCounter);

            // check if adjacency is also listed in the output metis file
            // do this only for certain edges, since the verification would take too long otherwise
            if (edgeCounter % verifyNthEdge == 0) {
                long metisFromId = userIdMapping.get(interaction.getSourceId());
                long metisToId = userIdMapping.get(interaction.getTargetId());

                if (!Arrays.stream(metisFileReader.getVertexById(metisFromId).split(" "))
                        .anyMatch(id -> (metisToId == Long.parseLong(id)))) {
                    System.err.println("Found edge from UserVertex " + interaction.getSourceId() + " to UserVertex " +
                            interaction.getTargetId() + " in input file, but outgoing edge is not present in metis file");
                    return false;
                }

                if (!Arrays.stream(metisFileReader.getVertexById(metisToId).split(" "))
                        .anyMatch(id -> (metisFromId == Long.parseLong(id)))) {
                    System.err.println("Found edge from UserVertex " + interaction.getSourceId() + " to UserVertex " +
                            interaction.getTargetId() + " in input file, but incoming edge is not present in metis file");
                    return false;
                }

                System.out.println("Successfully verified edge " + interaction.getSourceId()
                        + " to " + interaction.getTargetId());
            }

            optionalInteraction = interactionFileReader.readLineAsInteraction();
        }

        interactionFileReader.close();

        edgeCounter = 0;
        for (Long userVertexId : interactionGraph.getUserVertices().keySet()) {
            edgeCounter++;
            // check if all adjacencyLists only have entries with cardinalityCounter = 0
            Map<UserVertex, Integer> adjacencyMap = interactionGraph.getAdjacencyLists().get(userVertexId).getValue();

            if (adjacencyMap.values().stream().mapToInt(Integer::intValue).sum() != 0) {
                System.err.println("Number of read incoming and outgoing edges is not represented correctly " +
                        "inside the graph, failed at UserVertex: " + userVertexId);
                return false;
            }

            if (edgeCounter % verifyNthEdge == 0) {
                // check if cardinality of the adjacencyList matches the number of connected nodes in the metis file
                String metisVertex = metisFileReader.getVertexById(userIdMapping.get(userVertexId));
                if (metisVertex.trim().split(" ").length != adjacencyMap.size()) {
                    System.err.println("Number of entries in adjacencyList do not match with the number of " +
                            "connected nodes in the metis file, failed at UserVertex: " + userVertexId);
                    return false;
                }

                System.out.println("User vertex " + userVertexId + " has correct size of adjacency list in metis file");
            }
        }

        metisFileReader.close();

        return true;
    }
}
