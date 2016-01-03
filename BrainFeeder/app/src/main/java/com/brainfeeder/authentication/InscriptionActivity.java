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
import com.brainfeeder.user.SessionManager;
import com.brainfeeder.user.User;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * Activité d'inscription (Login + Mdp)
 */
public class InscriptionActivity extends OfflineActivity implements IServiceCallback {

    private SessionManager session;

    private EditText loginView;
    private EditText passwordView;
    private Button inscriptionButton;
    private View progressView;
    private View loginFormView;

    private User _tmpUser;

    private UserRegisterService _userRegisterService = null;

    // Les expressions régulières que doivent matcher les différents champs en entrées
    private static final Pattern loginPattern = Pattern.compile("^[a-z0-9_-]{3,15}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern passwordPattern = Pattern.compile("[a-z0-9]{2,19}", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        this.session = new SessionManager(getApplicationContext());
        if (this.session.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), BrainFeederActivity.class);
            startActivity(intent);
            finish();
        }

        this.loginView = (EditText) findViewById(R.id.inscription_login);
        this.passwordView = (EditText) findViewById(R.id.inscription_password);

        this.inscriptionButton = (Button) findViewById(R.id.signin_button);
        this.inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedInscription();
            }
        });

        // Appui sur le bouton S'inscrire sur le clavier
        this.passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
                    proceedInscription();
                    return true;
                }
                return false;
            }
        });

        this.progressView = findViewById(R.id.login_progress);
        this.loginFormView = findViewById(R.id.loginForm);
    }

    /**
     * Méthode qui va tenter une inscription
     * après avoir effectuer les vérifications nécessaires
     * (Champs remplis, longueur correct, regex ok)
     */
    private void proceedInscription() {
        if (_userRegisterService != null)
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
            _userRegisterService = new UserRegisterService(this, this, "inscription",  login, password);
            _userRegisterService.execute((Void) null);
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
        _userRegisterService = null;
        showProgress(false, progressView, loginFormView);
        if (success) {
            String[] userData = data.split("=");
            _session.createLoginSession(new User(userData[0], userData[1], _tmpUser.getPassword(), userData[3]));
            Intent intent = new Intent(this, BrainFeederActivity.class);
            startActivity(intent);
            finish();
        } else {
            loginView.setError(getString(R.string.error_login_taken));
            loginView.requestFocus();
        }
    }
}

