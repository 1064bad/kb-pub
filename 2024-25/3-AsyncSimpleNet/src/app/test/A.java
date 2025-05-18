package app.test;


abstract public class A {
    static int x = sf();

    private static int sf() {
        return -1;
    }
}

class B extends A {

}