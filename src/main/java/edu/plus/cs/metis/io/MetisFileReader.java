package edu.plus.cs.metis.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class MetisFileReader {

    private File file;

    Stream<String> lines;

    public MetisFileReader(File file) throws IOException {
        this.file = file;
        this.lines = Files.lines(file.toPath());
    }

    public String getVertexById(long vertexId) throws IOException {
        lines = Files.lines(file.toPath());
        return lines.skip(vertexId).findFirst().get();
    }

    public void close() {
        lines.close();
    }
}
