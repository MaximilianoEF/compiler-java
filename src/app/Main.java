package app;

import lex.Lexer;
import lex.Tokens;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
  public static void main(String[] args) throws Exception {
    String input;
    if (args.length > 0) {
      input = Files.readString(Path.of(args[0]));
    } else {
      // fallback: tus sentencias
      input = """
// Ejemplo 1: Declaración y Asignación de Variables
int a;
int b;
int c;
int x;
int y;
int counter;

a = 10;
b = 20;

// Ejemplo 2: Uso de `visualizar` para Mostrar Valores
visualizar(a);

// Ejemplo 3: Estructura `if`
if (a > 5) {
    visualizar(a);
}

// Ejemplo 4: Estructura `if-else`
a = 3;
if (a > 5) {
    visualizar(a);
} else {
    visualizar(5);
}

// Ejemplo 5: Bucle `while`
counter = 0;
while (counter < 5) {
    visualizar(counter);
    counter = counter + 1;
}

// Ejemplo 6: Programa Completo con Condicional y Bucle
x = 10;
y = 20;
if (x < y) {
    while (x < y) {
        visualizar(x);
        x = x + 1;
    }
} else {
    visualizar(y);
}

// Ejemplo 7: Operaciones Aritméticas Compuestas
a = 5;
b = 3;
c = (a + b) * (a - b) / 2;
visualizar(c);

// Ejemplo 8: Condiciones Compuestas con `and` y `or`
a = 10;
b = 20;
if ((a > 5 and b < 25) or (a == b)) {
    visualizar(a);
} else {
    visualizar(b);
}
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


