package lab_3;

enum TokenType {

    LEFT_SQUARE_BRACKET('['),
    RIGHT_SQUARE_BRACKET(']'),
    LEFT_PARENTHESIS('('),
    RIGHT_PARENTHESIS(')'),
    LEFT_BRACE('{'),
    RIGHT_BRACE('}'),
    COMMA(','),
    DOT('.'),
    MINUS('-'),
    PLUS('+'),
    SEMICOLON(';'),
    SLASH('/'),
    STAR('*'),

    NOT('!'){
        @Override
        public TokenType getDoubleSymbolOperatorIfPossible(Lexer lexer) {
            if(TokenType.checkNextCharEqual(lexer)){
                return NOT_EQUAL;
            }
            return this;
        }
    },
    NOT_EQUAL,
    EQUAL('='){
        @Override
        public TokenType getDoubleSymbolOperatorIfPossible(Lexer lexer) {
            if(TokenType.checkNextCharEqual(lexer)){
                return EQUAL_EQUAL;
            }
            return this;
        }
    },
    EQUAL_EQUAL,
    GREATER('>'){
        @Override
        public TokenType getDoubleSymbolOperatorIfPossible(Lexer lexer) {
            if(TokenType.checkNextCharEqual(lexer)){
                return GREATER_EQUAL;
            }
            return this;
        }
    },
    GREATER_EQUAL,
    LESS('<'){
        @Override
        public TokenType getDoubleSymbolOperatorIfPossible(Lexer lexer) {
            if(TokenType.checkNextCharEqual(lexer)){
                return LESS_EQUAL;
            }
            return this;
        }
    },
    LESS_EQUAL,
    IDENTIFIER,
    STRING,
    NUMBER,
    ELSE,
    FUN,
    FOR,
    IF,
    NIL,
    PRINT,
    RETURN,
    VAR,
    EOF;

    private char val;

    TokenType(){}

    TokenType(char val){
        this.val = val;
    }

    public static TokenType getTokenTypeByChar(char c, Lexer lexer){
        for(TokenType tokenType : TokenType.values()){
            if(tokenType.val == c) return tokenType.getDoubleSymbolOperatorIfPossible(lexer);
        }
        return null;
    }

    public TokenType getDoubleSymbolOperatorIfPossible(Lexer lexer){
        return this;
    }

    private static boolean checkNextCharEqual(Lexer lexer){
        return checkNextChar('=',lexer);
    }

    private static boolean checkNextChar(char expected, Lexer lexer) {

        if (lexer.EOF()) return false;
        if (lexer.getContent().charAt(lexer.getCurrent()) != expected) return false;
        lexer.incrementCurrent();
        return true;
    }
}
