package com.app.tp.layerchatintegration.fragments;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by vishalrandive on 28/03/16.
 */
public class Loading extends ProgressDialog {

    public Loading(Context context) {
        super(context);

        this.setMessage("Loading.. please wait.");
    }
}
