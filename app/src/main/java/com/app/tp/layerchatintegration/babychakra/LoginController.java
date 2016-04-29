package com.app.tp.layerchatintegration.babychakra;

import android.app.Activity;
import android.content.Intent;

import com.app.tp.layerchatintegration.babychakra.ChatConversationScreen;
import com.app.tp.layerchatintegration.babychakra.MyAuthListener;
import com.app.tp.layerchatintegration.babychakra.MyConnectionListener;
import com.app.tp.layerchatintegration.utility.Config;
import com.app.tp.layerchatintegration.utility.Constants;
import com.app.tp.layerchatintegration.utility.GenericListener;
import com.app.tp.layerchatintegration.utility.Utility;
import com.layer.sdk.LayerClient;

/**
 * Created by vishalrandive on 27/04/16.
 */
public class LoginController
{

    /**
     *   Variables used to manage the Layer Client and the conversations in this app
     *   Layer connection and authentication callback listeners
     */

    private static LayerClient layerClient;

    private GenericListener<Object> objGenericListener;
    private MyConnectionListener objMyLayerConnectionListener;
    private MyAuthListener objMyAuthenticationListener;
    private Activity activity;


    public LoginController(GenericListener<Object> objGenericListener, Activity activity)
    {
        this.objGenericListener = objGenericListener;
        this.activity = activity;

        if(objMyLayerConnectionListener == null)
            objMyLayerConnectionListener = new MyConnectionListener(new GenericListener<Object>(){

                @Override
                public void onResponse(int callerID, Object messages)
                {
                    switch (callerID)
                    {
                        case Constants.DEFAULT_ID:
                            onUserAuthenticated();
                            break;
                    }
                }

            });

        if(objMyAuthenticationListener == null)
            objMyAuthenticationListener = new MyAuthListener(new GenericListener<Object>()
            {
                    @Override
                    public void onResponse(int callerID, Object messages)
                    {
                        switch (callerID)
                        {
                            case Constants.DEFAULT_ID:
                                onUserAuthenticated();
                                break;
                        }
                    }

            });

        /**
         * Connect to Layer and Authenticate a user
         * */

        initLayerClient();

    }

    void initLayerClient()
    {


        if(layerClient == null)
        {

            /**
             *  Initializes a LayerClient object with the Google Project Number
             * */
            LayerClient.Options options = new LayerClient.Options();
            options.googleCloudMessagingSenderId(Config.GCM_PROJECT_NUMBER);

            /**
             * By default, only unread messages are synced after a user is authenticated, but you
             * can change that behavior to all messages or just the last message in a conversation
             * */

            options.historicSyncPolicy(LayerClient.Options.HistoricSyncPolicy.ALL_MESSAGES);

            layerClient = LayerClient.newInstance(activity, Config.LAYER_APP_ID, options);

            /**
             * Register the connection and authentication listeners
             * */

            layerClient.registerConnectionListener(objMyLayerConnectionListener);
            layerClient.registerAuthenticationListener(objMyAuthenticationListener);
        }


        /**
         * Check the current state of the SDK. The client must be CONNECTED and the user mustbe
         * AUTHENTICATED in order to send and receive messages. Note: it is possible to be
         * authenticated, but not connected, and vice versa, so it is a best practice to check
         * both states when your app launches or comes to the foreground.
         */

        if (!layerClient.isConnected()) {

            Utility.showToast("connecting....", activity);
            //If Layer is not connected, make sure we connect in order to send/receive messages.
            // ConnectionListener.java handles the callbacks associated with Connection, and
            // will start the Authentication process once the connection is established
            layerClient.connect();

        } else if (!layerClient.isAuthenticated()) {

            //If the client is already connected, try to authenticate a user on this device.
            // AuthListener.java handles the callbacks associated with Authentication
            // and will start the Conversation View once the user is authenticated
            Utility.showToast("not Authenticated", activity);
            layerClient.authenticate();

        } else {

            // If the client is to Layer and the user is authenticated, start the Conversation
            // View. This will be called when the app moves from the background to the foreground,
            // for example.
            Utility.showToast("GOOD TO GO >>>>>>>>>> ", activity);
            onUserAuthenticated();
        }

    }

    public static boolean HAS_LOGGEDIN = false;

    public void onUserAuthenticated()
    {

        if(!HAS_LOGGEDIN)
        {
            Utility.showToast("Authenticated", activity);
            activity.startActivity(new Intent(activity, ChatConversationScreen.class));
            activity.finish();
        }

        HAS_LOGGEDIN = true;

    }

    public static LayerClient getLayerClient(){
        if(layerClient!=null)
            return layerClient;
        return null;
    }


}
