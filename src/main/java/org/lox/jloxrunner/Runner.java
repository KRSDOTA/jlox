package org.lox.jloxrunner;

import org.lox.abstractsyntaxtree.expression.AssignmentExpression;
import org.lox.abstractsyntaxtree.statement.ExpressionStatement;
import org.lox.abstractsyntaxtree.statement.Statement;
import org.lox.errorhandler.JLoxErrorHandler;
import org.lox.errorhandler.JLoxLexerErrorHandler;
import org.lox.parser.Parser;
import org.lox.scanning.Scanner;
import org.lox.scanning.Token;
import org.lox.vistor.Interpreter;
import org.lox.vistor.SingleExpressionInterpreter;

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
  private final Interpreter interpreter = new Interpreter();
  private final SingleExpressionInterpreter singleExpressionInterpreter = new SingleExpressionInterpreter();
  private final boolean replEnabled;

    public Runner(boolean replEnabled) {
        this.replEnabled = replEnabled;
    }

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

    final List<Statement> statements = parser.parse();

    if (parser.hadError()) {
      System.exit(65);
    }

    if(containsSingleExpression(statements) && replEnabled) {
      singleExpressionInterpreter.interpret(((ExpressionStatement) statements.get(0)).getStatement());
    } else {
      interpreter.interpret(statements);
    }
  }

  private boolean containsSingleExpression(List<Statement> statements){
    if(statements.isEmpty()) {
      return false;
    }
    final Statement statement = statements.get(0);

    if(!(statement instanceof ExpressionStatement)) {
     return false;
    }

    return !(((ExpressionStatement) statement).getStatement() instanceof AssignmentExpression);
  }
}
