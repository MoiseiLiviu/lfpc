package lab_3;


import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lab_3.TokenType.*;

@Getter
public class Lexer {
    private static Map<String, TokenType> keywords;

    private final String content;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Lexer(String content) {
        this.content = content;
        this.initKeywords();
    }

    private void initKeywords(){

        keywords = new HashMap<>();
        keywords.put("else", ELSE);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("var", VAR);
    }

    public List<Token> readTokens() {

        while (!EOF()) {
            start = current;
            readToken();
        }
        tokens.add(new Token(EOF, ""));
        return tokens;
    }

    private void readToken(){

        char c = getNextChar();
        if(!isEmptySpace(c)){
            if(isDigit(c)){
                addNumberToken();
            } else if(isLetter(c)){
                addIdentifierToken();
            } else if(c == '\n'){
                line++;
            } else if(c == '"'){
                addStringToken();
            } else if(c == '/'){
                if (checkNextChar('/')) {
                    while (flashCurrentChar() != '\n' && !EOF()) getNextChar();
                } else {
                    addToken(SLASH);
                }
            } else {
                TokenType tokenType = TokenType.getTokenTypeByChar(c,this);
                if(tokenType == null) throw new RuntimeException("Unexpected character.");
                addToken(tokenType);
            }
        }
    }

    private boolean isEmptySpace(char c){
        return c == ' ' || c == '\r' ||  c == '\t';
    }

    private void addIdentifierToken() {

        while (isDigitOrLetter(flashCurrentChar())) getNextChar();
        String text = content.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }

    private void addNumberToken() {

        while (isDigit(flashCurrentChar())) getNextChar();
        if (flashCurrentChar() == '.' && isDigit(peekNextChar())) {
            getNextChar();

            while (isDigit(flashCurrentChar())) getNextChar();
        }
        addToken(NUMBER, Double.parseDouble(content.substring(start, current)));
    }

    private void addStringToken() {

        while (flashCurrentChar() != '"' && !EOF()) {
            if (flashCurrentChar() == '\n') line++;
            getNextChar();
        }
        if (EOF()) {
            return;
        }
        getNextChar();
        String value = content.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    public boolean checkNextChar(char expected) {

        if (EOF()) return false;
        if (content.charAt(current) != expected) return false;
        current++;
        return true;
    }

    private char flashCurrentChar() {

        if (EOF()) return '\0';
        return content.charAt(current);
    }

    private char peekNextChar() {

        if (current + 1 >= content.length()) return '\0';
        return content.charAt(current + 1);
    }

    private boolean isLetter(char c) {

        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isDigitOrLetter(char c) {
        return isLetter(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    boolean EOF() {
        return current >= content.length();
    }

    private char getNextChar() {
        return content.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        tokens.add(new Token(type, literal));
    }

    public void incrementCurrent(){
        this.current++;
    }
}
