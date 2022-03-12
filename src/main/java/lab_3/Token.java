package lab_3;

class Token {
    final TokenType type;
    final Object value;

    Token(TokenType type,  Object value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        if(value != null){
            return type + "(" + value + ")";
        }
        return type.toString();
    }
}
