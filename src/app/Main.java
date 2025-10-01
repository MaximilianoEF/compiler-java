package app;

import lex.Lexer;
import lex.Tokens;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
	// Punto de entrada del programa: prepara la entrada y recorre los tokens generados por el lexer.
  public static void main(String[] args) throws Exception {
    String input;
    if (args.length > 0) {
      input = Files.readString(Path.of(args[0]));
    } else {
      input = """
// Ejemplo 1: Declaración y Asignación de Variables
int a;
""";
      System.out.println("(Leyendo ejemplo embebido; pasá un archivo como argumento para leer desde disco)\n");
    }

    Lexer lx = new Lexer(input);
    while (true){
      Lexer.Tok t = lx.next();
      System.out.printf("%4d | %-26s | %-12s | %d:%d%n", t.code, Tokens.name(t.code), t.lex, t.line, t.col);
      if (t.code==Tokens.EOF || t.code==Tokens.ERROR) break;
    }
  }
}


