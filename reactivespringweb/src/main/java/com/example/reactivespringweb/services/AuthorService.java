package com.example.reactivespringweb.services;

import com.example.reactivespringweb.models.Author;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class AuthorService {
    private final static Logger logger = Logger.getLogger(AuthorService.class.getName());
    private final ClassPathResource AUTHORS_FILE_PATH = new ClassPathResource("AuthorsFile.txt");

    @SneakyThrows
    private List<Author> readIO() {
        InputStream input = AUTHORS_FILE_PATH.getInputStream();
        return readFromStream(input);
    }

    @SneakyThrows
    private List<Author> readFromStream(InputStream input) {
        List<Author> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String headerLine = reader.readLine();
        logger.info("File header name: " + headerLine);

        String line = reader.readLine();
        while (line != null){
            String[] items = line.split(" ");
            result.add(
                    Author.builder()
                            .name(items[1])
                            .age(Long.parseLong(items[2]))
                            .build()
            );
            line = reader.readLine();
        }
        return result;
    }

    @SneakyThrows
    private List<Author> readNIO() {
        long bytes = java.nio.file.Files.size(java.nio.file.Paths.get(AUTHORS_FILE_PATH.getFile().getAbsolutePath()));
        ByteBuffer buffer = ByteBuffer.allocate((int)bytes);
        ReadableByteChannel inChannel = AUTHORS_FILE_PATH.readableChannel();
        int bytesRead = inChannel.read(buffer);
        while(bytesRead != bytes) {
            logger.info("Iteration occured!");
            bytesRead = inChannel.read(buffer);
        }

        InputStream input = new ByteArrayInputStream(buffer.array());
        return readFromStream(input);
    }

    public List<Author> readAuthors() {
        return readNIO();
    }
}
