package edu.plus.cs.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class UserIdMappingFileWriter {

    private BufferedWriter bufferedWriter;

    private File file;

    public UserIdMappingFileWriter(File file) throws IOException {
        this.file = file;
        bufferedWriter = new BufferedWriter(new FileWriter(file));

        // write header
        bufferedWriter.write("original_user_id,metis_user_id\n");
    }

    public void writeUserIdMapping(long metisUserId, long originalUserId) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(metisUserId).append(",").append(originalUserId).append("\n");
        bufferedWriter.write(stringBuilder.toString());
    }

    public void close() throws IOException {
        bufferedWriter.close();
    }
}
