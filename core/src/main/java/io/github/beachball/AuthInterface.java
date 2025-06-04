package io.github.beachball;

public interface AuthInterface {
    void register(String email, String password);
    void login(String email, String password);
}
