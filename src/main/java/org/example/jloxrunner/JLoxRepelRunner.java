package org.example.jloxrunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JLoxRepelRunner implements JLoxRunner {

    private final InputStreamReader input = new InputStreamReader(System.in);
    private final BufferedReader reader = new BufferedReader(input);

    public JLoxRepelRunner() {

    }

    public void runPrompt() throws IOException {
        for (;;) {
            System.out.println("> ");
            final String line = reader.readLine();
            if(line == null) break;
            run(line);
        }
    }

    public void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    public void run(String s) {

    }

}
