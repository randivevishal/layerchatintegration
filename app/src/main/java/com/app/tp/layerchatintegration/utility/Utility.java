package com.app.tp.layerchatintegration.utility;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.app.tp.layerchatintegration.MyApp;
import com.app.tp.layerchatintegration.babychakra.LoginController;
import com.layer.atlas.util.picasso.requesthandlers.MessagePartRequestHandler;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by vishalrandive on 28/03/16.
 */
public class Utility {

    public static void showLog(String message)
    {
        Log.e("layerchatintegration ", message);
    }

    public static void showToast(String msg, Context context)
    {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    private static Picasso mPicasso;

    public static Picasso getPicasso() {
        if (mPicasso == null) {
            // Picasso with custom RequestHandler for loading from Layer MessageParts.
            mPicasso = new Picasso.Builder(MyApp.getInstance())
                    .addRequestHandler(new MessagePartRequestHandler(LoginController.getLayerClient()))
                    .build();
        }
        return mPicasso;
    }

    public static void replaceFragment(AppCompatActivity activity, Fragment fragment, int containerId, boolean addToBackStack) {
        String tag = fragment.getClass().getName();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment, tag);
        if(addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    public static Fragment getCurrentVisibleFragment(AppCompatActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        String fragmentTag =
                fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = activity.getSupportFragmentManager().findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    public static int getFragmentCounts(AppCompatActivity activity){

        int count =0;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        count = fragmentManager.getBackStackEntryCount();
        return count;
    }

    public static String streamToString(InputStream stream) throws IOException {
        int n = 0;
        char[] buffer = new char[1024 * 4];
        InputStreamReader reader = new InputStreamReader(stream, "UTF8");
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
        return writer.toString();
    }
}
