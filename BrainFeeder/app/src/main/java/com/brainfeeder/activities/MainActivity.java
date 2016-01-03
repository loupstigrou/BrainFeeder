package com.brainfeeder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.brainfeeder.R;
import com.brainfeeder.activities.baseActivities.OfflineActivity;
import com.brainfeeder.authentication.ConnectionActivity;
import com.brainfeeder.authentication.InscriptionActivity;

/**
 * Activité principale
 * Connexion + Inscription
 */
public class MainActivity extends OfflineActivity implements View.OnClickListener {

    private Button inscriptionButton;
    private Button connectionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.inscriptionButton = (Button) findViewById(R.id.inscription_button);
        this.connectionButton = (Button) findViewById(R.id.connection_button);

        this.inscriptionButton.setOnClickListener(this);
        this.connectionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inscription_button:
                this.goToInscriptionActivity();
                break;

            case R.id.connection_button:
                this.goToConnectionActivity();
                break;

            default:
                break;
        }
    }

    private void goToInscriptionActivity() {
        Intent intent = new Intent(MainActivity.this, InscriptionActivity.class);
        startActivity(intent);
    }

    private void goToConnectionActivity() {
        Intent intent = new Intent(MainActivity.this, ConnectionActivity.class);
        startActivity(intent);
    }
}
