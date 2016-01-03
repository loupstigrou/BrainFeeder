package com.brainfeeder.asynctasks;

/**
 * Created by Benjamin on 14/12/2015.
 */
public interface IServiceCallback {
    void onReceiveData(boolean success, String data);
}
