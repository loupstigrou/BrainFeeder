package com.brainfeeder.user;

import java.io.Serializable;

/**
 * Created by Major on 14/10/2015.
 */

/**
 * Classe représentant un utilisateur
 * Login + Password
 */
public class User implements Serializable {

    private String login;
    private String password;
    private String token;

    public User(String login, String password) {
        this(login, password, null);
    }
    public User(String login, String password, String token) {
        this.login = login;
        this.password = password;

        this.token = token;
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
