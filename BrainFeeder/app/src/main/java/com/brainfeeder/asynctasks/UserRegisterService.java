package com.brainfeeder.asynctasks;

import android.content.Context;

public class UserRegisterService extends BasicService {

    public UserRegisterService(Context context, IServiceCallback callback, String requestType, String login, String password){
        super(context, null,null,null, callback);

        this.requestType = requestType;
        this.urlParameters =
                "requestType="+requestType+
                        "&login=" +  login +
                        "&password=" + password +
                        urlParameters;
    }
}
