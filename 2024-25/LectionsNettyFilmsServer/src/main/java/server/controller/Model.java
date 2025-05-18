package server.controller;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private String template;
    private final Map<String, Object> variables = new HashMap<>();

    public Model(String template) {
        this.template = template;
    }

    public Model() {
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void set(String name, Object value) {
        getVariables().put(name, value);
    }
}
