package app.server.session;

import java.util.Objects;
import java.util.UUID;

public class Token {
    private final String text;

    public Token() {
        this.text = UUID.randomUUID().toString();
    }

    private Token(String text) {
        this.text = text;
    }

    public static Token fromText(String text) {
        return new Token(text);
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(text, token.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public String toString() {
        return text;
    }
}
