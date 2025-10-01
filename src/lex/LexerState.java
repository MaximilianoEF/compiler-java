package lex;

/** Estado mutable del escaneo: lexema, token actual y posición. Mantiene el buffer. */
final class LexerState {
    final StringBuilder lex = new StringBuilder();
    int tokenCode = 0;
    int i=0, line=1, col=1;

    // Limpia el contenido del lexema acumulado para comenzar una nueva captura.
    void clearLex(){ lex.setLength(0); }
    
    // Agrega un carácter al lexema actual preservando el orden de lectura.
    void appendChar(char ch){ lex.append(ch); }
}
