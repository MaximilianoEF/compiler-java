package lex;

import java.util.Map;

/** Implementa f1..f7 y utilidades (palabras clave, chequeos de longitud y de int64). */
final class LexActions {
  private LexActions(){}

  /* ==================== Palabras reservadas ==================== */
  static final Map<String,Integer> KW = Map.of(
    "int", Tokens.TIPO_ENTERO, "if", Tokens.IF, "else", Tokens.ELSE, "while", Tokens.WHILE,
    "visualizar", Tokens.VISUALIZAR, "and", Tokens.AND, "or", Tokens.OR
  );

  // f1: iniciar IDENT
  static void f1_create_var(LexerState st){ st.clearLex(); st.tokenCode = Tokens.IDENT; }
  // f2: agregar char a IDENT (máx 32)
  static void f2_add_char(LexerState st, int c){ if (st.lex.length()<32) st.appendChar((char)c); }
  // f3: iniciar NUMBER
  static void f3_create_int(LexerState st){ st.clearLex(); st.tokenCode = Tokens.NUMBER; }
  // f4: agregar dígito a NUMBER
  static void f4_add_num(LexerState st, int c){ st.appendChar((char)c); }
  // f5/f6/f7 no hacen nada adicional (se valida al emitir)
  static void f5_finish_var(LexerState st) {}
  static void f6_finish_int(LexerState st) {}
  static void f7_nop(LexerState st) {}

  static void run(int fn, LexerState st, int c){
    switch (fn){
      case Automaton.F_CREATE_VAR -> f1_create_var(st);
      case Automaton.F_ADD_CHAR   -> f2_add_char(st, c);
      case Automaton.F_CREATE_INT -> f3_create_int(st);
      case Automaton.F_ADD_NUM    -> f4_add_num(st, c);
      case Automaton.F_FINISH_VAR -> f5_finish_var(st);
      case Automaton.F_FINISH_INT -> f6_finish_int(st);
      case Automaton.F_NOTHING, 0 -> f7_nop(st);
      default -> f7_nop(st);
    }
  }

  // Reclasifica IDENT a palabra reservada si corresponde
  static int reclassIfKeyword(int code, CharSequence lex){
    if (code != Tokens.IDENT) return code;
    Integer kw = KW.get(lex.toString());
    return (kw!=null) ? kw : code;
  }

  // NUMBER debe caber en int64 (signed, sin signo en el literal)
  static boolean fitsInt64(String digits){
    int p=0; while (p<digits.length()-1 && digits.charAt(p)=='0') p++;
    String d = digits.substring(p);
    final String MAX = "9223372036854775807";
    if (d.length() < MAX.length()) return true;
    if (d.length() > MAX.length()) return false;
    return d.compareTo(MAX) <= 0;
  }
}

