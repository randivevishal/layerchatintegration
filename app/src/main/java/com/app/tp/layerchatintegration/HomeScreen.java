package com.app.tp.layerchatintegration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.app.tp.layerchatintegration.fragments.ChatFragment;
import com.app.tp.layerchatintegration.fragments.ChatUsersFragment;
import com.app.tp.layerchatintegration.layer.AuthListener;
import com.app.tp.layerchatintegration.layer.ConnectionListener;
import com.app.tp.layerchatintegration.utility.Config;
import com.app.tp.layerchatintegration.utility.Utility;
import com.layer.sdk.LayerClient;

/**
 * Created by vishalrandive on 28/03/16.
 */
public class HomeScreen extends AppCompatActivity
{


    /**
     *   Global variables used to manage the Layer Client and the conversations in this app
     *   Layer connection and authentication callback listeners
     */
    private static LayerClient layerClient;

    private ConnectionListener objConnectionListener;
    private AuthListener objAuthenticationListener;
    private FrameLayout flHomeContainer;
    private Button btnAuthenticate;

    public static final String KEY_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        flHomeContainer = (FrameLayout) findViewById(R.id.container);

        btnAuthenticate = (Button) findViewById(R.id.btnAuthenticate);
        btnAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                initLayerClient();
            }
        });

        //Create the callback listeners

        if(objConnectionListener == null)
            objConnectionListener = new ConnectionListener(this);

        if(objAuthenticationListener == null)
            objAuthenticationListener = new AuthListener(this);


    }

    public static LayerClient getLayerClient(){
        if(layerClient!=null)
            return layerClient;
        return null;
    }


    private void initDefaultFragmentView() {
        Fragment fragment = new ChatUsersFragment();
        replaceFragment(fragment, "CHAT USERS", true);
    }


    private void replaceFragment(Fragment fragment, CharSequence title, boolean addToBackstack) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title.toString());
        fragment.setArguments(bundle);
        Utility.replaceFragment(this, fragment, R.id.container, addToBackstack);
        setTitle(title.toString());
    }

    protected void onResume(){
        super.onResume();

        /**
         * Connect to Layer and Authenticate a user
         * */

        initLayerClient();

        //Every time the app is brought to the foreground, register the typing indicator
//        if(layerClient != null && conversationView != null)
//            layerClient.registerTypingIndicator(conversationView);
    }

    //onPause is called when the app is sent to the background
    protected void onPause(){
        super.onPause();

        //When the app is moved to the background, unregister the typing indicator
//        if(layerClient != null && conversationView != null)
//            layerClient.unregisterTypingIndicator(conversationView);
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

            layerClient = LayerClient.newInstance(this, Config.LAYER_APP_ID, options);

            /**
             * Register the connection and authentication listeners
             * */

            layerClient.registerConnectionListener(objConnectionListener);
            layerClient.registerAuthenticationListener(objAuthenticationListener);
        }


        /**
         * Check the current state of the SDK. The client must be CONNECTED and the user mustbe
         * AUTHENTICATED in order to send and receive messages. Note: it is possible to be
         * authenticated, but not connected, and vice versa, so it is a best practice to check
         * both states when your app launches or comes to the foreground.
         */

        if (!layerClient.isConnected()) {

            Utility.showToast("not connected", getApplicationContext());
            //If Layer is not connected, make sure we connect in order to send/receive messages.
            // ConnectionListener.java handles the callbacks associated with Connection, and
            // will start the Authentication process once the connection is established
            layerClient.connect();

        } else if (!layerClient.isAuthenticated()) {

            //If the client is already connected, try to authenticate a user on this device.
            // AuthListener.java handles the callbacks associated with Authentication
            // and will start the Conversation View once the user is authenticated
            Utility.showToast("not Authenticated", getApplicationContext());
            layerClient.authenticate();

        } else {

            // If the client is to Layer and the user is authenticated, start the Conversation
            // View. This will be called when the app moves from the background to the foreground,
            // for example.
            Utility.showToast("GOOD TO GO >>>>>>>>>> ", getApplicationContext());
            onUserAuthenticated();
        }

    }

    public void onUserAuthenticated()
    {





        if(Utility.getFragmentCounts(this) >0)
        {
            Fragment currFragment = Utility.getCurrentVisibleFragment(this);
            if (currFragment instanceof ChatFragment)
            {
                Utility.showLog(" no need to replace the fragment with ChatUserFragment");
            }
        }
        else {
            btnAuthenticate.setVisibility(View.GONE);
            initDefaultFragmentView();

        }

        //startActivity(new Intent(HomeScreen.this, ChatUsers.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Utility.getFragmentCounts(this) >0)
        {
            Fragment currFragment = Utility.getCurrentVisibleFragment(this);
            if (currFragment instanceof ChatUsersFragment)
                finish();
        }
        finish();
    }
}


// Create a LayerClient ready to receive push notifications through GCM
//        layerClient = LayerClient.newInstance(HomeScreen.this, LAYER_APP_ID,
//                new LayerClient.Options().googleCloudMessagingSenderId(GCM_PROJECT_NUMBER));
//        layerClient.registerConnectionListener(this);
//        layerClient.registerAuthenticationListener(this);