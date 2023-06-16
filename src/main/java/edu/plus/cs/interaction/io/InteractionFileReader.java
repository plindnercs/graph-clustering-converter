package edu.plus.cs.interaction.io;

import edu.plus.cs.interaction.Interaction;
import edu.plus.cs.interaction.InteractionType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class InteractionFileReader {

    private File file;
    private Scanner fileScanner;

    public InteractionFileReader(File file) throws FileNotFoundException {
        this.file = file;
        this.fileScanner = new Scanner(file);

        // skip first line (header)
        if (fileScanner.hasNext()) {
            fileScanner.next();
        }
    }

    public Optional<Interaction> readLineAsInteraction() {
        if (!fileScanner.hasNext()) {
            return Optional.empty();
        }

        String input = fileScanner.next();

        Interaction interaction = readInteractionFromInputString(input);

        return Optional.of(interaction);
    }

    private Interaction readInteractionFromInputString(String input) {
        String[] splitInput = input.split(",");

        if (splitInput.length != 4) {
            throw new InputMismatchException("Invalid number of parameters found");
        }

        try {
            long sourceId = Long.parseLong(splitInput[0]);
            long targetId = Long.parseLong(splitInput[1]);
            InteractionType interactionType = InteractionType.valueOf(splitInput[2].toUpperCase());
            String timestamp = splitInput[3];

            return new Interaction(sourceId, targetId, interactionType, timestamp);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            throw new InputMismatchException("Invalid input format");
        }
    }

    public void close() {
        fileScanner.close();
    }
}
