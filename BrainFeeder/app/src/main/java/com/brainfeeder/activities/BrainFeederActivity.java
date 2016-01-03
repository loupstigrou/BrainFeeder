package com.brainfeeder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.brainfeeder.R;
import com.brainfeeder.activities.baseActivities.InGameActivity;
import com.brainfeeder.database.MySQLiteHelper;

/**
 * Activité principale après connexion
 */
public class BrainFeederActivity extends InGameActivity {

    Intent locationServiceIntent;

    private TextView connectedText;
    private MySQLiteHelper mySQLiteHelper;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_feeder);

        this.connectedText = (TextView) findViewById(R.id.main_activity_description);

        String msgConnected = String.format(getResources().getString(R.string.main_activity_description), _user.getLogin());
        connectedText.setText(msgConnected);

        // Service pour s'abonner aux notifications push venant du serveur
        /*if (checkPlayServices()) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
            if (!sentToken) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }*/
    }

    @Override
    protected void onDestroy() {
        this.stopService(locationServiceIntent);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this._session.checkLogin();
    }

    public void goToInformationActivity(View v) {
       /* Intent intent = new Intent(BrainFeederActivity.this, XXXXXX.class);
        startActivity(intent);*/
    }


    /*private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("ScanMonstersActivity", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }*/
}
