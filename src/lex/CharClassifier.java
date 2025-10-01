package lex;

/** Convierte un carácter a la “clase de carácter” (columna de la matriz δ). */
final class CharClassifier {

	private CharClassifier() {}
	
	public static int cls(int c){
	  if (c==-1) return Automaton.C_EOF;
	  char ch=(char)c;
	  if (ch==' '||ch=='\t'||ch=='\r'||ch=='\n') return Automaton.C_WS;
	  if (Character.isLetter(ch)||ch=='_') return Automaton.C_LET;
	  if (Character.isDigit(ch))      return Automaton.C_DIG;
	  return switch (ch) {
	    case '{'->Automaton.C_LB; case '}'->Automaton.C_RB;
	    case '('->Automaton.C_LP; case ')'->Automaton.C_RP;
	    case ';'->Automaton.C_SEMI;
	    case '-'->Automaton.C_MINUS; case '+'->Automaton.C_PLUS;
	    case '/'->Automaton.C_SLASH; case '*'->Automaton.C_STAR;
	    case '<'->Automaton.C_LT; case '>'->Automaton.C_GT;
	    case '!'->Automaton.C_NOT; case '='->Automaton.C_EQ;
	    default -> -1; 
	  };
	}
	
}
