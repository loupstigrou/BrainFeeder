package com.brainfeeder.user;

import java.io.Serializable;

/**
 * Classe représentant un utilisateur
 * Login + Password
 */
public class User implements Serializable {

    private String remoteId;
    private String login;
    private String password;
    private String token;

    public User(String login, String password) {
        this(null, login, password, null);
    }
    public User(String remoteId, String login, String password, String token) {
        this.remoteId = remoteId;
        this.login = login;
        this.password = password;
        this.token = token;
    }

    public String getRemoteId() {
        return this.remoteId;
    }
    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getToken() {
        return this.token;
    }

}
