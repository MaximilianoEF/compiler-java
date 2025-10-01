package lex;

/**
 * Tabla de TOKENS con los mismos códigos numéricos que utilizamos.
 * Sirve para construir/imprimir la “matriz de tokens” que produce el lexer.
 */
public final class Tokens {
  private Tokens(){}

  // Identificadores y literales
  public static final int IDENT          = 256; // <identificador> — máx 32 chars
  public static final int NUMBER         = 257; // <numero> — debe caber en int64 (signed)

  // Palabra reservada de tipo entero ("int")
  public static final int TIPO_ENTERO    = 258; // "int" (semánticamente = int64)

  // Signos/operadores básicos
  public static final int ASIGNACION     = 259; // "="
  public static final int FIN_DE_LINEA   = 260; // ";"

  // Palabras reservadas de control e I/O
  public static final int IF             = 261; // "if"
  public static final int ELSE           = 262; // "else"
  public static final int WHILE          = 263; // "while"
  public static final int LLAVE_ABRE     = 264; // "{"
  public static final int LLAVE_CIERRA   = 265; // "}"
  public static final int PAR_ABRE       = 266; // "("
  public static final int PAR_CIERRA     = 267; // ")"
  public static final int VISUALIZAR     = 268; // "visualizar"

  // Aritméticos
  public static final int OP_SUMA        = 269; // "+"
  public static final int OP_RESTA       = 270; // "-"
  public static final int OP_MUL         = 271; // "*"
  public static final int OP_DIV         = 272; // "/"

  // Comparadores lógicos
  public static final int CMP_IGUAL      = 273; // "=="
  public static final int CMP_DISTINTO   = 274; // "!="
  public static final int CMP_MENOR      = 275; // "<"
  public static final int CMP_MAYOR      = 276; // ">"
  public static final int CMP_MENOR_IG   = 277; // "<="
  public static final int CMP_MAYOR_IG   = 278; // ">="

  // Lógicos (palabras)
  public static final int AND            = 279; // "and"
  public static final int OR             = 280; // "or"
  public static final int NOT            = 281; // "!" 

  public static final int EOF            = 900; // fin de linea
  public static final int ERROR          = 999; // error

  /** Nombre legible del token. */
  public static String name(int code){
    return switch (code) {
      case IDENT -> "<identificador>";
      case NUMBER -> "<numero>";
      case TIPO_ENTERO -> "<tipo_entero>";
      case ASIGNACION -> "<asignacion>";
      case FIN_DE_LINEA -> "<fin_de_linea>";
      case IF -> "<if>";
      case ELSE -> "<else>";
      case WHILE -> "<while>";
      case LLAVE_ABRE -> "<llave_abre>";
      case LLAVE_CIERRA -> "<llave_cierra>";
      case PAR_ABRE -> "<par_parentesis_abre>";
      case PAR_CIERRA -> "<par_parentesis_cierra>";
      case VISUALIZAR -> "<visualizar>";
      case OP_SUMA -> "<operador_suma>";
      case OP_RESTA -> "<operador_resta>";
      case OP_MUL -> "<operador_multiplicacion>";
      case OP_DIV -> "<operador_division>";
      case CMP_IGUAL -> "<comparador_igual>";
      case CMP_DISTINTO -> "<comparador_distinto>";
      case CMP_MENOR -> "<comparador_menor>";
      case CMP_MAYOR -> "<comparador_mayor>";
      case CMP_MENOR_IG -> "<comparador_menor_igual>";
      case CMP_MAYOR_IG -> "<comparador_mayor_igual>";
      case AND -> "<operador_and>";
      case OR  -> "<operador_or>";
      case NOT -> "<operador_not>";
      case EOF -> "<EOF>";
      case ERROR -> "<ERROR>";
      default -> "<DESCONOCIDO " + code + ">";
    };
  }
}
