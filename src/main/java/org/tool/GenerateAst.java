package org.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("Usage: generate_ast <output_directory>");
      System.exit(64);
    }
    String outputDir = args[0];
    defineAst(outputDir, "Expr", Arrays.asList(
      "Binary   : Expr left, Token operator, Expr right",
      "Grouping : Expr expression",
      "Literal  : Object value",
      "Unary    : Token operator, Expr right"
    ));
  }

  private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
    final String path = outputDir + "/" + baseName + ".java";
    final PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

    writer.println("package com.craftinginterpreters.lox;");
    writer.println();
    writer.println("import java.util.List;");
    writer.println();
    writer.println("abstract class " + baseName + " {");

    defineVistor(writer, baseName, types);

    for (String type : types) {
      final String className = type.split(":")[0].trim();
      final String fields = type.split(":")[1].trim();
      defineType(writer, baseName, className, fields);
    }

    writer.println("}");
    writer.close();
  }

  private static void defineVistor(PrintWriter writer, String baseName, List<String> types) {
    writer.println("  interface Visitor<T> {");

    for (String type : types) {
      String typeName = type.split(":")[0].trim();
      writer.println("    T visit" + typeName + baseName + "(" +
        typeName + " " + baseName.toLowerCase() + ");");
    }

    writer.println();
    writer.println("  abstract <T> T accept(Visitor<T> visitor);");

    writer.println("  }");

    // Visitor pattern.
    writer.println();
    writer.println("    @Override");
    writer.println("    <T> T accept(Visitor<T> visitor) {");
    writer.println("      return visitor.visit" + baseName + "(this);");
    writer.println("    }");
  }

  private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
    writer.println(" static class " + className + " extends " + baseName + " {" );

    writer.println("  " + className + "(" + fieldList + ") {" );

    final String[] fields = fieldList.split(", ");
    for (String field : fields) {
      String name = field.split(" ")[1];
      writer.println("      this." + name + " = " + name + ";");
    }

    writer.println("    }");

    writer.println();
    for (String field : fields) {
      writer.println("    final " + field + ";");
    }

    writer.println("  }");

  }

}
