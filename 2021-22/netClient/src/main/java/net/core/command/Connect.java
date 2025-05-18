package net.core.command;

public class Connect extends Command {
    @Override
    public String toString() {
        return "Connect{}";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        return obj.getClass() == this.getClass();
    }
}
