package org.example;

import org.example.jloxrunner.Runner;
import org.example.jloxrunner.JLoxRunner;

import java.io.IOException;

public class Lox {

  private static final JLoxRunner jLoxRunner = new Runner();

  public static void main(String[] args) throws IOException {
    if (args.length > 1) {
      System.exit(64);
    } else if (args.length == 1) {
      jLoxRunner.runFile(args[0]);
    } else {
      jLoxRunner.runInterpreterPrompt();
    }

  }
}