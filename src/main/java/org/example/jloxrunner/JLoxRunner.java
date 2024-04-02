package org.example.jloxrunner;

import java.io.IOException;

public interface JLoxRunner {

    void runInterpreterPrompt() throws IOException;

    void runFile(String filePath) throws IOException;

}
