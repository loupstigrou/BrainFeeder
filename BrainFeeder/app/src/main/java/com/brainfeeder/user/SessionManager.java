package com.brainfeeder.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.brainfeeder.activities.MainActivity;

/**
 * Classe qui va gérer les sesssions utilisateurs, afin de pouvoir récupérer les informations
 * de l'utilisateur au sein de toutes les activités
 */
public class SessionManager {

    // Shared preferences
    SharedPreferences pref;

    // Editor to edit Shared Preferences
    Editor editor;

    // Context
    Context context;

    // Shared preferences mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "ScanMonsterPref";

    /**
     * All Shared preferences keys
     */
    // Key for boolean
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_REMOTE_ID = "remoteId";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TOKEN = "token";

    /**
     * Constructor
     * @param context
     */
    public SessionManager(Context context) {
        this.context = context;
        this.pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor = pref.edit();
    }

    /**
     * Create Log In _session
     * @param user
     */
    public void createLoginSession(User user) {
        this.editor.putBoolean(IS_LOGIN, true);
        this.editor.putString(KEY_REMOTE_ID, user.getRemoteId());
        this.editor.putString(KEY_LOGIN, user.getLogin());
        this.editor.putString(KEY_PASSWORD, user.getPassword());
        this.editor.putString(KEY_TOKEN, user.getToken());
        this.editor.commit();
    }

    /**
     * Check _user Log In status
     * If _user is not logged in, he will return to the Main Activity
     */
    public boolean checkLogin() {
        if (!isLoggedIn()) {
            Intent intent = new Intent(this.context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(intent);
            return false;
        }
        return true;
    }

    /**
     * Check if _user is Logged In
     */
    public boolean isLoggedIn() {
        return this.pref.getBoolean(IS_LOGIN, false);
    }

    /**
     * Return stored _session data
     */
    public User getUser() {
        return new User(pref.getString(KEY_REMOTE_ID, null),pref.getString(KEY_LOGIN, null), pref.getString(KEY_PASSWORD, null), pref.getString(KEY_TOKEN, null));
    }

    /**
     * Clear _session details and return _user to Main Activity
     */
    public void logoutUser() {
        this.editor.clear();
        this.editor.commit();

        Intent intent = new Intent(this.context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(intent);
    }

}
