package org.example.jloxrunner;

import org.example.errorhandler.JLoxErrorHandler;
import org.example.errorhandler.JLoxInterpreterErrorHandler;
import org.example.scanning.Scanner;
import org.example.scanning.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Runner implements JLoxRunner {

  private final InputStreamReader input = new InputStreamReader(System.in);
  private final BufferedReader reader = new BufferedReader(input);

  private final JLoxErrorHandler jLoxErrorHandler = new JLoxInterpreterErrorHandler();

  public void runInterpreterPrompt() throws IOException {
    for (; ; ) {
      System.out.println("> ");
      final String line = reader.readLine();
      if (line == null) break;
      run(line);
    }
  }

  public void runFile(String path) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    run(new String(bytes, Charset.defaultCharset()));
  }

  public void run(String source) {
    final Scanner scanner = new Scanner(source, jLoxErrorHandler);
    final Stream<Token> tokens = scanner.scanTokens().stream();
    tokens.forEach(System.out::println);
  }

}
