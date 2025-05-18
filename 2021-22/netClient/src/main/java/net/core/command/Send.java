package net.core.command;

import java.util.Objects;

public class Send extends Command {
    private final String message;

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Send{" +
                "message='" + message + '\'' +
                '}';
    }

    public Send(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Send send = (Send) o;
        return Objects.equals(message, send.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
