package Parser;

import org.junit.jupiter.api.Test;
import org.lox.abstractsyntaxtree.expression.Expression;
import org.lox.parser.Parser;
import org.lox.scanning.Token;
import org.lox.scanning.TokenType;

import java.util.ArrayList;
import java.util.List;

public class ParserTest {



  @Test
  void testDoesConstructAstCorrectlyFromTokenStream() {
    Token plus = new Token(TokenType.PLUS, "+", null, 1);
    Token multiply = new Token(TokenType.STAR, "*", null, 1);
    Token five = new Token(TokenType.NUMBER, "5", 5, 1);
    Token ten = new Token(TokenType.NUMBER, "10", 10, 1);
    Token three = new Token(TokenType.NUMBER, "3", 3, 1);

    List<Token> tokens = new ArrayList<>();
    tokens.add(five);
    tokens.add(plus);
    tokens.add(three);
    tokens.add(multiply);
    tokens.add(ten);

    final Parser parser = new Parser(tokens);
    Expression expression = parser.expression();
  }



}
