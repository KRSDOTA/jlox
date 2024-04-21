package org.lox.jloxrunner;

import org.lox.abstractsyntaxtree.BinaryExpression;
import org.lox.abstractsyntaxtree.LiteralExpression;
import org.lox.errorhandler.JLoxErrorHandler;
import org.lox.errorhandler.JLoxInterpreterErrorHandler;
import org.lox.scanning.Scanner;
import org.lox.scanning.Token;
import org.lox.scanning.TokenType;
import org.lox.vistor.ExpressionVisitor;
import org.lox.vistor.ReversePolishNotationVisitor;

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

  final ExpressionVisitor<String> reversePolishNotationVisitor = new ReversePolishNotationVisitor();

  public void runInterpreterPrompt() throws IOException {
    final Token multiply = new Token(TokenType.STAR, "*", null, 10);
    final Token literal1 = new Token(TokenType.NUMBER, "10", 10, 9);
    final Token literal2 = new Token(TokenType.NUMBER, "20", 20, 11);
    final LiteralExpression literalExpression1 = new LiteralExpression(10);
    final LiteralExpression literalExpression2 = new LiteralExpression(20);
    final BinaryExpression binaryExpression = new BinaryExpression(literalExpression1, multiply, literalExpression2);
    System.out.println(reversePolishNotationVisitor.visitBinaryExpr(binaryExpression));
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
