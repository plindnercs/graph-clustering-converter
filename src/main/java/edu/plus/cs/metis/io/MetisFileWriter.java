package edu.plus.cs.metis.io;

import edu.plus.cs.graph.InteractionGraph;
import edu.plus.cs.graph.UserVertex;
import edu.plus.cs.util.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MetisFileWriter {

    private BufferedWriter writer;

    private File file;

    public MetisFileWriter(File file, InteractionGraph interactionGraph) throws IOException {
        this.file = file;

        writer = new BufferedWriter(new FileWriter(this.file));

        // write header line
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(interactionGraph.getNumberOfVertices())
                .append(" ")
                .append(interactionGraph.getNumberOfEdges())
                .append("\n");
        writer.write(stringBuilder.toString());
    }


    public void writeUserVertexPair(Pair<UserVertex, Map<UserVertex, Integer>> userVertexPair,
                                        HashMap<Long, Long> userIdMapping) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<UserVertex, Integer> neighbourVertex : userVertexPair.getValue().entrySet()) {
            stringBuilder.append(userIdMapping.get(neighbourVertex.getKey().getKey()));
            stringBuilder.append(" ");
        }

        stringBuilder.append("\n");

        writer.write(stringBuilder.toString());
    }

    public void close() throws IOException {
        writer.close();
    }
}
