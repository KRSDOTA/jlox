package org.lox.jloxrunner;

import org.lox.abstractsyntaxtree.Expression;
import org.lox.errorhandler.JLoxErrorHandler;
import org.lox.errorhandler.JLoxLexerErrorHandler;
import org.lox.parser.Parser;
import org.lox.scanning.Scanner;
import org.lox.scanning.Token;
import org.lox.vistor.AstPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Runner implements JLoxRunner {

  private final InputStreamReader input = new InputStreamReader(System.in);
  private final BufferedReader reader = new BufferedReader(input);

  private final JLoxErrorHandler jLoxErrorHandler = new JLoxLexerErrorHandler();

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
    final List<Token> tokens = scanner.scanTokens();
    final Parser parser = new Parser(tokens);
    final Expression expression = parser.parse();

    if (parser.hadError()) {
      return;
    }

    System.out.println(new AstPrinter().printTree(expression));

  }

}
