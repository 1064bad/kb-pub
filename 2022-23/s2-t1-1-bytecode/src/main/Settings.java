package main;

public class Settings implements SettingsMBean {
    private static final String DEFAULT_NAME = "Dolly";

    private String name = DEFAULT_NAME;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void reset() {
        name = DEFAULT_NAME;
    }
}
