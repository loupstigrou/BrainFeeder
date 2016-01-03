package com.brainfeeder.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brainfeeder.R;
import com.brainfeeder.activities.BrainFeederActivity;
import com.brainfeeder.activities.baseActivities.OfflineActivity;
import com.brainfeeder.asynctasks.IServiceCallback;
import com.brainfeeder.asynctasks.UserRegisterService;
import com.brainfeeder.user.User;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * Activité de Connexion
 * (login + mdp)
 */
public class ConnectionActivity extends OfflineActivity implements IServiceCallback {

    private EditText loginView;
    private EditText passwordView;
    private Button loginButton;
    private View progressView;
    private View loginFormView;

    private User _tmpUser;

    private UserRegisterService _userLoginTask = null;

    // Les expressions régulières que doivent matcher les différents champs en entrées
    private static final Pattern loginPattern = Pattern.compile("^[a-z0-9_-]{3,15}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern passwordPattern = Pattern.compile("[a-z0-9]{2,19}", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        this.loginView = (EditText) findViewById(R.id.connection_login);
        this.passwordView = (EditText) findViewById(R.id.connection_password);

        this.loginButton = (Button) findViewById(R.id.signup_button);
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedLogin();
            }
        });

        // Appui sur le bouton Se connecter sur le clavier
        this.passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.connection || actionId == EditorInfo.IME_NULL) {
                    proceedLogin();
                    return true;
                }
                return false;
            }
        });

        this.progressView = findViewById(R.id.connection_login_progress);
        this.loginFormView = findViewById(R.id.connection_login_form);
    }

    /**
     * Méthode qui va effectuer toute les vérifications
     * (Champs rempli, longueur et regex correct)
     * Et tenter la connexion si c'est bon
     */
    private void proceedLogin() {
        if (_userLoginTask != null)
            return;

        this.loginView.setError(null);
        this.passwordView.setError(null);

        String login = this.loginView.getText().toString();
        String password = this.passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Vérification de la validité
        if (TextUtils.isEmpty(login)) {
            loginView.setError(getString(R.string.error_field_required));
            focusView = loginView;
            cancel = true;
        } else if (!isLoginValid(login)) {
            loginView.setError(getString(R.string.error_invalid_login));
            focusView = loginView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        try {
            login = URLEncoder.encode(login, "UTF-8");
            password = URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            loginView.setError(getString(R.string.error_invalid_login));
            passwordView.setError(getString(R.string.error_invalid_password));
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true, progressView, loginFormView);
            _tmpUser = new User(login, password);
            _userLoginTask = new UserRegisterService(this, this, "connexion", login, password);
            _userLoginTask.execute((Void) null);
        }
    }

    private boolean isLoginValid(CharSequence email) {
        return this.loginPattern.matcher(email).matches();
    }

    private boolean isPasswordValid(CharSequence password) {
        return this.passwordPattern.matcher(password).matches() && password.length() < 20;
    }



    @Override
    public void onReceiveData(boolean success, String data) {
        _userLoginTask = null;
        showProgress(false, progressView, loginFormView);
        if (success) {
            _session.createLoginSession(_tmpUser);
            Intent intent = new Intent(this, BrainFeederActivity.class);
            startActivity(intent);
            finish();
        } else {
            loginView.setError(getString(R.string.error_wrong_login_or_password));
            passwordView.setText(null);
            loginView.requestFocus();
        }
    }
}
