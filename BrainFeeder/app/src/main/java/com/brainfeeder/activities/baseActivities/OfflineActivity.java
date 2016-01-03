package com.brainfeeder.activities.baseActivities;

import android.content.Intent;
import android.os.Bundle;

import com.brainfeeder.activities.BrainFeederActivity;

/**
 * Created by Benjamin on 03/01/2016.
 */
public class OfflineActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (_session.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), BrainFeederActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (_session.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), BrainFeederActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
