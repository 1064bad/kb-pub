package animals;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        var rabbit = new Rabbit();
        List.of(new Cow(), new Dog(), new Bear()).forEach(rabbit::beat);
    }
}
