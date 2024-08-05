package edu.plus.cs;

import edu.plus.cs.graph.InteractionGraph;
import edu.plus.cs.interaction.Interaction;
import edu.plus.cs.interaction.io.InteractionFileReader;
import edu.plus.cs.metis.io.MetisFileWriter;
import edu.plus.cs.util.ConversionVerifier;
import edu.plus.cs.util.UserIdMappingFileWriter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 5) {
            System.err.println("Invalid number of arguments! " +
                    "Usage: java edu.plus.cs.Main <input-file> <output-file> <mapping-file> " +
                    "[mode: 0 - convert and verify | 1 - convert only | 2 - verify only] [verify-nth-edge]");
            return;
        }

        int mode = Integer.parseInt(args[3]);
        if (mode < 0 || mode > 2) {
            System.err.println("Invalid processing mode provided! " +
                    "Usage: [mode: 0 - convert and verify | 1 - convert only | 2 - verify only]");
        }

        int verifyNthEdge = 1;
        if (mode == 0 || mode == 2) {
            verifyNthEdge = Integer.parseInt(args[4]);
            System.out.println("Verification of conversion enabled, will verify every " + verifyNthEdge + "-th edge.");
        }

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);

        InteractionFileReader interactionFileReader = new InteractionFileReader(inputFile);
        InteractionGraph interactionGraph = new InteractionGraph();

        // create graph from input file and save the id mapping
        Optional<Interaction> optionalInteraction = interactionFileReader.readLineAsInteraction();
        while (optionalInteraction.isPresent()) {
            interactionGraph.insertInteractionEdge(optionalInteraction.get());

            optionalInteraction = interactionFileReader.readLineAsInteraction();
        }

        interactionFileReader.close();

        if (mode < 2) {
            HashMap<Long, Long> userIdMapping = interactionGraph.getUserVertexIdMapping();

            MetisFileWriter metisFileWriter = new MetisFileWriter(outputFile, interactionGraph);
            UserIdMappingFileWriter userIdMappingFileWriter =
                    new UserIdMappingFileWriter(new File(args[2]));

            // convert data to metis format and write the metis + mapping files
            for (long userId : interactionGraph.getAdjacencyLists().keySet()) {
                metisFileWriter.writeUserVertexPair(interactionGraph.getAdjacencyLists().get(userId), userIdMapping);
                userIdMappingFileWriter.writeUserIdMapping(userId, userIdMapping.get(userId));
            }

            metisFileWriter.close();
            userIdMappingFileWriter.close();
        }

        boolean isCorrect = true;
        if (mode == 0 || mode == 2) {
            // verify conversion
            isCorrect = ConversionVerifier.verifyConversion(interactionGraph, inputFile, outputFile, verifyNthEdge);
        }

        if (isCorrect) {
            System.out.println("Verification successful, graph got converted correctly");
        } else {
            System.err.println("Verificated failed, see previous log output for more information");
        }
    }
}