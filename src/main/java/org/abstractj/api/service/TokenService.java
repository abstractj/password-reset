package org.abstractj.api.service;

public interface TokenService {
    void destroy(String id);
    boolean isValid(String id);
    void send(String email);
}
