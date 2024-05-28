package org.lox;

import org.lox.jloxrunner.Runner;
import org.lox.jloxrunner.JLoxRunner;

import java.io.IOException;

public class Lox {

  private static JLoxRunner jLoxRunner;

  public static void main(String[] args) throws IOException {
    if (args.length > 1) {
      System.exit(64);
    } else if (args.length == 1) {
      jLoxRunner = new Runner(false);
      jLoxRunner.runFile(args[0]);
    } else {
      jLoxRunner = new Runner(true);
      jLoxRunner.runInterpreterPrompt();
    }
  }
}