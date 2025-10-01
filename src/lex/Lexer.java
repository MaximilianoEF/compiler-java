package lex;

/**
 * Lexer “table-driven” simplificado:
 * - Usa el DFA (tablas) para IDENT y NUMBER (lo que más conviene).
 * - Maneja comentarios y operadores de 1/2 char (==, !=, <=, >=, +, -, *) por código directo (lookahead).
 * - En NUMBER valida int64; en IDENT limita a 32 chars; "int" es <tipo_entero>.
 *
 * Resultado: código corto, claro y sin ambigüedades en +, -, *, <, >, =, !.
 */

public class Lexer {
  private final String src;
  private final LexerState S = new LexerState();

  public Lexer(String source){ this.src = source; }

  private int peek(){ return (S.i >= src.length()) ? -1 : src.charAt(S.i); }
  private int adv(){
    int c = peek(); if (c==-1) return -1;
    S.i++; if (c=='\n'){ S.line++; S.col=1; } else S.col++;
    return c;
  }
  private static boolean isWS(int c){ return c==' '||c=='\t'||c=='\r'||c=='\n'; }

  /** Salta blancos y descarta comentarios y antes del DFA. */
  private boolean skipSpacesAndComments(){
    boolean skipped=false;
    while (true){
      while (isWS(peek())) { adv(); skipped=true; }
      if (peek()=='/' && S.i+1 < src.length() && src.charAt(S.i+1)=='/'){
        while (peek()!=-1 && peek()!='\n') adv(); skipped=true; continue;
      }
      if (peek()=='/' && S.i+1 < src.length() && src.charAt(S.i+1)=='*'){
        int sl=S.line, sc=S.col; adv(); adv(); int prev=-1;
        while (peek()!=-1){ int d=adv(); if (prev=='*' && d=='/') break; prev=d; }
        if (peek()==-1 && prev!='*') { // EOF en comentario
          lastTok = new Tok(Tokens.ERROR, "EOF en comentario", sl, sc);
          return false;
        }
        skipped=true; continue;
      }
      break;
    }
    return skipped;
  }

  /** Token resultante (código, lexema y posición). */
  public static class Tok {
    public final int code, line, col; public final String lex;
    Tok(int code, String lex, int line, int col){ this.code=code; this.lex=lex; this.line=line; this.col=col; }
    @Override public String toString(){ return Tokens.name(code)+"('"+lex+"') @"+line+":"+col+" ["+code+"]"; }
  }

  private Tok lastTok = null;

  /** Devuelve el siguiente token. */
  public Tok next(){
    lastTok = null;
    skipSpacesAndComments();
    if (lastTok != null) return lastTok; // error de comentario

    // si ya no hay nada más, devolver EOF acá y listo
    if (peek() == -1) return new Tok(Tokens.EOF, "<EOF>", S.line, S.col);
    
    int startI = S.i, sl=S.line, sc=S.col;
    int q=0, lastAcc=0, lastLen=0; S.lex.setLength(0); S.tokenCode=0;

    while (true){
      int c = peek();
      int k = CharClassifier.cls(c);
      if (k == -1){
        if (lastAcc!=0) break;
        char bad=(char)adv();
        return new Tok(Tokens.ERROR, String.valueOf(bad), sl, sc);
      }

      // Ejecutar acción y consumir char si corresponde
      LexActions.run(Automaton.ACTION[q][k], S, c);
      int to = Automaton.DELTA[q][k];

      if (to == Automaton.DEAD){
        if (lastAcc!=0) break;
        if (c==-1) return new Tok(Tokens.EOF, "<EOF>", sl, sc);
        char bad=(char)adv();
        return new Tok(Tokens.ERROR, String.valueOf(bad), sl, sc);
      } else {
        if (c!=-1) adv();
        q = to;

        // Estados aceptores 1-char (según tu tabla)
        switch (q){
          case 1 -> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.IDENT; }
          case 2 -> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.NUMBER; }
          case 3 -> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.ASIGNACION; }
          case 4 -> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.CMP_IGUAL; }
          case 5 -> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.NOT; }
          case 6 -> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.CMP_DISTINTO; }
          case 7 -> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.CMP_MENOR; }
          case 8 -> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.CMP_MENOR_IG; }
          case 9 -> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.CMP_MAYOR; }
          case 10-> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.CMP_MAYOR_IG; }
          case 11-> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.PAR_ABRE; }
          case 12-> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.PAR_CIERRA; }
          case 13-> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.LLAVE_ABRE; }
          case 14-> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.LLAVE_CIERRA; }
          case 15-> { lastAcc=q; lastLen=S.i-startI; S.tokenCode=Tokens.FIN_DE_LINEA; }
        }
      }
    }

    // Volver a último aceptado y ajustar línea/col
    S.i = startI + lastLen; S.line=sl; S.col=sc;
    for (int k=0; k<lastLen; k++){ char ch = src.charAt(startI+k); if (ch=='\n'){S.line++; S.col=1;} else S.col++; }

    String lx = src.substring(startI, startI+lastLen);
    int code = LexActions.reclassIfKeyword(S.tokenCode, lx);

    if (code==Tokens.IDENT && lx.length()>32)   return new Tok(Tokens.ERROR, "Identificador > 32", sl, sc);
    if (code==Tokens.NUMBER && !LexActions.fitsInt64(lx)) return new Tok(Tokens.ERROR, "Entero fuera de int64", sl, sc);

    return new Tok(code, lx, sl, sc);
  }
}

