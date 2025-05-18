package animals;

abstract public class Animal {
    private final String name;


    public String getName() {
        return name;
    }

    public Animal(String name) {
        this.name = name;
    }

    public void beat(Animal other) {
        System.out.println(getName() + " omnoms " + other.getName());
    }
}
