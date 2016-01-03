package com.brainfeeder.activities.baseActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.brainfeeder.R;
import com.brainfeeder.activities.BrainFeederActivity;

/**
 * Activité avec menu de base
 */
public class InGameActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!_session.checkLogin()) {
            finish();
        }
        _user = _session.getUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!_session.checkLogin()) {
            finish();
        }
    }

    /**
     * Menu with a logout option if the _user is logged in
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (this._session.isLoggedIn())
            getMenuInflater().inflate(R.menu.menu_logged_in, menu);

        return true;
    }

    /**
     * Logout functionnality
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_logout) {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getText(R.string.warning).toString());
            alertDialog.setMessage(getText(R.string.confirm_logout).toString());
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getText(R.string.no).toString(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getText(R.string.yes).toString(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    _session.logoutUser();
                }
            });
            alertDialog.show();
            return super.onOptionsItemSelected(item);

        } else if (id == R.id.menu_infos) {
            /*Intent intent = new Intent(getApplicationContext(), XXXXXXXX.class);
            startActivity(intent);*/
        }
        if(!(this instanceof BrainFeederActivity))
            finish();

        return super.onOptionsItemSelected(item);
    }
}
