package com.app.tp.layerchatintegration.utility;


/**
 * Created by vishalrandive on 27/04/16.
 */
public interface OnResponseListener<T> {
    public void onResponse(int callerID, T messages);
}

