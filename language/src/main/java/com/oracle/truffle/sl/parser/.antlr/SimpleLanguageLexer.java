// Generated from /Volumes/projects/futz/truffle/graawk/language/src/main/java/com/oracle/truffle/sl/parser/SimpleLanguage.g4 by ANTLR 4.8

// DO NOT MODIFY - generated from SimpleLanguage.g4 using "mx create-sl-parser"

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SimpleLanguageLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, WS=35, COMMENT=36, LINE_COMMENT=37, IDENTIFIER=38, 
		STRING_LITERAL=39, NUMERIC_LITERAL=40, REGEX_LITERAL=41;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", 
			"T__25", "T__26", "T__27", "T__28", "T__29", "T__30", "T__31", "T__32", 
			"T__33", "WS", "COMMENT", "LINE_COMMENT", "LETTER", "NON_ZERO_DIGIT", 
			"DIGIT", "HEX_DIGIT", "OCT_DIGIT", "BINARY_DIGIT", "TAB", "STRING_CHAR", 
			"REGEX_BODY", "IDENTIFIER", "STRING_LITERAL", "NUMERIC_LITERAL", "REGEX_LITERAL"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'function'", "'('", "','", "')'", "'{'", "'}'", "'break'", "';'", 
			"'continue'", "'debugger'", "'while'", "'if'", "'else'", "'return'", 
			"'||'", "'&&'", "'<'", "'<='", "'>'", "'>='", "'=='", "'!='", "'+'", 
			"'-'", "'*'", "'/'", "'%'", "'~'", "'!~'", "'$'", "'='", "'.'", "'['", 
			"']'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, "WS", 
			"COMMENT", "LINE_COMMENT", "IDENTIFIER", "STRING_LITERAL", "NUMERIC_LITERAL", 
			"REGEX_LITERAL"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public SimpleLanguageLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SimpleLanguage.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2+\u0134\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3"+
		"\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3"+
		"\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20"+
		"\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\25\3\25"+
		"\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33"+
		"\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\""+
		"\3#\3#\3$\6$\u00da\n$\r$\16$\u00db\3$\3$\3%\3%\3%\3%\7%\u00e4\n%\f%\16"+
		"%\u00e7\13%\3%\3%\3%\3%\3%\3&\3&\3&\3&\7&\u00f2\n&\f&\16&\u00f5\13&\3"+
		"&\3&\3\'\5\'\u00fa\n\'\3(\3(\3)\3)\3*\5*\u0101\n*\3+\3+\3,\3,\3-\3-\3"+
		".\3.\3/\3/\3/\3/\3/\3/\7/\u0111\n/\f/\16/\u0114\13/\3\60\3\60\3\60\7\60"+
		"\u0119\n\60\f\60\16\60\u011c\13\60\3\61\3\61\7\61\u0120\n\61\f\61\16\61"+
		"\u0123\13\61\3\61\3\61\3\62\3\62\3\62\7\62\u012a\n\62\f\62\16\62\u012d"+
		"\13\62\5\62\u012f\n\62\3\63\3\63\3\63\3\63\3\u00e5\2\64\3\3\5\4\7\5\t"+
		"\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23"+
		"%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G"+
		"%I&K\'M\2O\2Q\2S\2U\2W\2Y\2[\2]\2_(a)c*e+\3\2\13\5\2\13\f\16\17\"\"\4"+
		"\2\f\f\17\17\5\2C\\aac|\3\2\63;\3\2\62;\5\2\62;CHch\3\2\629\5\2\f\f\17"+
		"\17$$\5\2\f\f\17\17\61\61\2\u0134\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2"+
		"\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3"+
		"\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2"+
		"\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2"+
		"\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2"+
		"\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2"+
		"\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2_\3\2\2\2\2"+
		"a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\3g\3\2\2\2\5p\3\2\2\2\7r\3\2\2\2\tt\3"+
		"\2\2\2\13v\3\2\2\2\rx\3\2\2\2\17z\3\2\2\2\21\u0080\3\2\2\2\23\u0082\3"+
		"\2\2\2\25\u008b\3\2\2\2\27\u0094\3\2\2\2\31\u009a\3\2\2\2\33\u009d\3\2"+
		"\2\2\35\u00a2\3\2\2\2\37\u00a9\3\2\2\2!\u00ac\3\2\2\2#\u00af\3\2\2\2%"+
		"\u00b1\3\2\2\2\'\u00b4\3\2\2\2)\u00b6\3\2\2\2+\u00b9\3\2\2\2-\u00bc\3"+
		"\2\2\2/\u00bf\3\2\2\2\61\u00c1\3\2\2\2\63\u00c3\3\2\2\2\65\u00c5\3\2\2"+
		"\2\67\u00c7\3\2\2\29\u00c9\3\2\2\2;\u00cb\3\2\2\2=\u00ce\3\2\2\2?\u00d0"+
		"\3\2\2\2A\u00d2\3\2\2\2C\u00d4\3\2\2\2E\u00d6\3\2\2\2G\u00d9\3\2\2\2I"+
		"\u00df\3\2\2\2K\u00ed\3\2\2\2M\u00f9\3\2\2\2O\u00fb\3\2\2\2Q\u00fd\3\2"+
		"\2\2S\u0100\3\2\2\2U\u0102\3\2\2\2W\u0104\3\2\2\2Y\u0106\3\2\2\2[\u0108"+
		"\3\2\2\2]\u0112\3\2\2\2_\u0115\3\2\2\2a\u011d\3\2\2\2c\u012e\3\2\2\2e"+
		"\u0130\3\2\2\2gh\7h\2\2hi\7w\2\2ij\7p\2\2jk\7e\2\2kl\7v\2\2lm\7k\2\2m"+
		"n\7q\2\2no\7p\2\2o\4\3\2\2\2pq\7*\2\2q\6\3\2\2\2rs\7.\2\2s\b\3\2\2\2t"+
		"u\7+\2\2u\n\3\2\2\2vw\7}\2\2w\f\3\2\2\2xy\7\177\2\2y\16\3\2\2\2z{\7d\2"+
		"\2{|\7t\2\2|}\7g\2\2}~\7c\2\2~\177\7m\2\2\177\20\3\2\2\2\u0080\u0081\7"+
		"=\2\2\u0081\22\3\2\2\2\u0082\u0083\7e\2\2\u0083\u0084\7q\2\2\u0084\u0085"+
		"\7p\2\2\u0085\u0086\7v\2\2\u0086\u0087\7k\2\2\u0087\u0088\7p\2\2\u0088"+
		"\u0089\7w\2\2\u0089\u008a\7g\2\2\u008a\24\3\2\2\2\u008b\u008c\7f\2\2\u008c"+
		"\u008d\7g\2\2\u008d\u008e\7d\2\2\u008e\u008f\7w\2\2\u008f\u0090\7i\2\2"+
		"\u0090\u0091\7i\2\2\u0091\u0092\7g\2\2\u0092\u0093\7t\2\2\u0093\26\3\2"+
		"\2\2\u0094\u0095\7y\2\2\u0095\u0096\7j\2\2\u0096\u0097\7k\2\2\u0097\u0098"+
		"\7n\2\2\u0098\u0099\7g\2\2\u0099\30\3\2\2\2\u009a\u009b\7k\2\2\u009b\u009c"+
		"\7h\2\2\u009c\32\3\2\2\2\u009d\u009e\7g\2\2\u009e\u009f\7n\2\2\u009f\u00a0"+
		"\7u\2\2\u00a0\u00a1\7g\2\2\u00a1\34\3\2\2\2\u00a2\u00a3\7t\2\2\u00a3\u00a4"+
		"\7g\2\2\u00a4\u00a5\7v\2\2\u00a5\u00a6\7w\2\2\u00a6\u00a7\7t\2\2\u00a7"+
		"\u00a8\7p\2\2\u00a8\36\3\2\2\2\u00a9\u00aa\7~\2\2\u00aa\u00ab\7~\2\2\u00ab"+
		" \3\2\2\2\u00ac\u00ad\7(\2\2\u00ad\u00ae\7(\2\2\u00ae\"\3\2\2\2\u00af"+
		"\u00b0\7>\2\2\u00b0$\3\2\2\2\u00b1\u00b2\7>\2\2\u00b2\u00b3\7?\2\2\u00b3"+
		"&\3\2\2\2\u00b4\u00b5\7@\2\2\u00b5(\3\2\2\2\u00b6\u00b7\7@\2\2\u00b7\u00b8"+
		"\7?\2\2\u00b8*\3\2\2\2\u00b9\u00ba\7?\2\2\u00ba\u00bb\7?\2\2\u00bb,\3"+
		"\2\2\2\u00bc\u00bd\7#\2\2\u00bd\u00be\7?\2\2\u00be.\3\2\2\2\u00bf\u00c0"+
		"\7-\2\2\u00c0\60\3\2\2\2\u00c1\u00c2\7/\2\2\u00c2\62\3\2\2\2\u00c3\u00c4"+
		"\7,\2\2\u00c4\64\3\2\2\2\u00c5\u00c6\7\61\2\2\u00c6\66\3\2\2\2\u00c7\u00c8"+
		"\7\'\2\2\u00c88\3\2\2\2\u00c9\u00ca\7\u0080\2\2\u00ca:\3\2\2\2\u00cb\u00cc"+
		"\7#\2\2\u00cc\u00cd\7\u0080\2\2\u00cd<\3\2\2\2\u00ce\u00cf\7&\2\2\u00cf"+
		">\3\2\2\2\u00d0\u00d1\7?\2\2\u00d1@\3\2\2\2\u00d2\u00d3\7\60\2\2\u00d3"+
		"B\3\2\2\2\u00d4\u00d5\7]\2\2\u00d5D\3\2\2\2\u00d6\u00d7\7_\2\2\u00d7F"+
		"\3\2\2\2\u00d8\u00da\t\2\2\2\u00d9\u00d8\3\2\2\2\u00da\u00db\3\2\2\2\u00db"+
		"\u00d9\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00de\b$"+
		"\2\2\u00deH\3\2\2\2\u00df\u00e0\7\61\2\2\u00e0\u00e1\7,\2\2\u00e1\u00e5"+
		"\3\2\2\2\u00e2\u00e4\13\2\2\2\u00e3\u00e2\3\2\2\2\u00e4\u00e7\3\2\2\2"+
		"\u00e5\u00e6\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e6\u00e8\3\2\2\2\u00e7\u00e5"+
		"\3\2\2\2\u00e8\u00e9\7,\2\2\u00e9\u00ea\7\61\2\2\u00ea\u00eb\3\2\2\2\u00eb"+
		"\u00ec\b%\2\2\u00ecJ\3\2\2\2\u00ed\u00ee\7\61\2\2\u00ee\u00ef\7\61\2\2"+
		"\u00ef\u00f3\3\2\2\2\u00f0\u00f2\n\3\2\2\u00f1\u00f0\3\2\2\2\u00f2\u00f5"+
		"\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\u00f6\3\2\2\2\u00f5"+
		"\u00f3\3\2\2\2\u00f6\u00f7\b&\2\2\u00f7L\3\2\2\2\u00f8\u00fa\t\4\2\2\u00f9"+
		"\u00f8\3\2\2\2\u00faN\3\2\2\2\u00fb\u00fc\t\5\2\2\u00fcP\3\2\2\2\u00fd"+
		"\u00fe\t\6\2\2\u00feR\3\2\2\2\u00ff\u0101\t\7\2\2\u0100\u00ff\3\2\2\2"+
		"\u0101T\3\2\2\2\u0102\u0103\t\b\2\2\u0103V\3\2\2\2\u0104\u0105\4\62\63"+
		"\2\u0105X\3\2\2\2\u0106\u0107\7\13\2\2\u0107Z\3\2\2\2\u0108\u0109\n\t"+
		"\2\2\u0109\\\3\2\2\2\u010a\u010b\7^\2\2\u010b\u0111\7^\2\2\u010c\u010d"+
		"\7^\2\2\u010d\u010e\7\61\2\2\u010e\u010f\3\2\2\2\u010f\u0111\n\n\2\2\u0110"+
		"\u010a\3\2\2\2\u0110\u010c\3\2\2\2\u0111\u0114\3\2\2\2\u0112\u0110\3\2"+
		"\2\2\u0112\u0113\3\2\2\2\u0113^\3\2\2\2\u0114\u0112\3\2\2\2\u0115\u011a"+
		"\5M\'\2\u0116\u0119\5M\'\2\u0117\u0119\5Q)\2\u0118\u0116\3\2\2\2\u0118"+
		"\u0117\3\2\2\2\u0119\u011c\3\2\2\2\u011a\u0118\3\2\2\2\u011a\u011b\3\2"+
		"\2\2\u011b`\3\2\2\2\u011c\u011a\3\2\2\2\u011d\u0121\7$\2\2\u011e\u0120"+
		"\5[.\2\u011f\u011e\3\2\2\2\u0120\u0123\3\2\2\2\u0121\u011f\3\2\2\2\u0121"+
		"\u0122\3\2\2\2\u0122\u0124\3\2\2\2\u0123\u0121\3\2\2\2\u0124\u0125\7$"+
		"\2\2\u0125b\3\2\2\2\u0126\u012f\7\62\2\2\u0127\u012b\5O(\2\u0128\u012a"+
		"\5Q)\2\u0129\u0128\3\2\2\2\u012a\u012d\3\2\2\2\u012b\u0129\3\2\2\2\u012b"+
		"\u012c\3\2\2\2\u012c\u012f\3\2\2\2\u012d\u012b\3\2\2\2\u012e\u0126\3\2"+
		"\2\2\u012e\u0127\3\2\2\2\u012fd\3\2\2\2\u0130\u0131\7\61\2\2\u0131\u0132"+
		"\5]/\2\u0132\u0133\7\61\2\2\u0133f\3\2\2\2\17\2\u00db\u00e5\u00f3\u00f9"+
		"\u0100\u0110\u0112\u0118\u011a\u0121\u012b\u012e\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}