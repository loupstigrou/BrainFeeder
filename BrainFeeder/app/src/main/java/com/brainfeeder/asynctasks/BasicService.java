package com.brainfeeder.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.brainfeeder.activities.baseActivities.BaseActivity;
import com.brainfeeder.user.SessionManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class BasicService extends AsyncTask<Void, Void, Boolean> {

    protected Context context;
    protected String serverResponse;
    protected SessionManager session;
    protected String requestType;
    protected String urlParameters;
    protected IServiceCallback _callback;
    protected boolean _noNetwork;
//"&password=" + URLEncoder.encode(session.getUser().getPassword(), "UTF-8")

    public BasicService(Context context, SessionManager session, String requestType, String urlParameters) {
        this(context, session, requestType, urlParameters, null);
    }

    public BasicService(Context context, SessionManager session, String requestType, String urlParameters, IServiceCallback callback) {
        this.context = context;
        this.serverResponse = "";
        this.session = session;
        this.requestType = requestType;
        this.urlParameters = (urlParameters == null)? "" : urlParameters;

        _callback = callback;

        try {
            if(this.session != null && requestType != null)
            {
                this.urlParameters =
                        "requestType="+requestType+
                                "&login=" + URLEncoder.encode(session.getUser().getLogin(), "UTF-8") +
                                "&password=" + URLEncoder.encode(session.getUser().getPassword(), "UTF-8")+
                                urlParameters;
            }
        } catch (Exception e) {
           // e.printStackTrace();
            this.urlParameters = "";
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(BaseActivity.webserviceURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            connection.setFixedLengthStreamingMode(urlParameters.getBytes().length);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(urlParameters);
            out.close();

            Scanner inStream = new Scanner(connection.getInputStream());

            while (inStream.hasNextLine())
                this.serverResponse += (inStream.nextLine());

            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            _noNetwork = true;
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            _noNetwork = true;
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            _noNetwork = true;
            return false;
        }

        return !this.serverResponse.equals("NOK");
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(_callback != null)
        {
            _callback.onReceiveData(success, serverResponse);
        }

        if (success) {


        } else {
            if (_noNetwork)
                Toast.makeText(context, "ERREUR : Pas de réseau !", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "Erreur: " + serverResponse, Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public boolean finished() {
        return getStatus() == Status.FINISHED;
    }

    public Boolean hasNoNetwork() {
        return _noNetwork;
    }
}
