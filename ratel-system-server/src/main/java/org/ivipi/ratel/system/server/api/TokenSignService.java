package org.ivipi.ratel.system.server.api;

import org.springframework.stereotype.Service;

@Service
public class TokenSignService {

    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
