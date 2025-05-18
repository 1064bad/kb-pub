package app.client.token;

public class InMemoryTokenHolder implements TokenHolder {
    private String token;

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }
}
