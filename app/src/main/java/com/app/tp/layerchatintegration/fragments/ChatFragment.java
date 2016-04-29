package com.app.tp.layerchatintegration.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.tp.layerchatintegration.ChatUsers;
import com.app.tp.layerchatintegration.HomeScreen;
import com.app.tp.layerchatintegration.MessageView;
import com.app.tp.layerchatintegration.R;
import com.app.tp.layerchatintegration.utility.Constants;
import com.app.tp.layerchatintegration.utility.Utility;
import com.layer.atlas.AtlasHistoricMessagesFetchLayout;
import com.layer.sdk.changes.LayerChangeEvent;
import com.layer.sdk.listeners.LayerChangeEventListener;
import com.layer.sdk.messaging.Conversation;
import com.layer.sdk.messaging.Message;
import com.layer.sdk.messaging.MessageOptions;
import com.layer.sdk.messaging.MessagePart;
import com.layer.sdk.query.Predicate;
import com.layer.sdk.query.Query;
import com.layer.sdk.query.SortDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by vishalrandive on 28/03/16.
 */
public class ChatFragment extends BaseFragment{

    public static final int REQUEST_CODE_100 = 100;
    public static final int REQUEST_CODE_101 = 101;
    private Context mContext;
    private AppCompatActivity activity;
    Button btnUsers [] = new Button[2];
    TextView tvChatMessages;

    //List of all users currently typing
    private ArrayList<String> typingUsers;

    public static ChatFragment getChatFragment(){
        return new ChatFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_chat, container, false);
        tvChatMessages = (TextView) view.findViewById(R.id.tvChatMessages);
        //List of users that are typing which is used with LayerTypingIndicatorListener
        typingUsers = new ArrayList<>();
        initChat();

        return view;
    }

    private void replaceFragment(Fragment fragment, CharSequence title, boolean addToBackstack)
    {
        Bundle bundle = new Bundle();
        bundle.putString(HomeScreen.KEY_TITLE, title.toString());
        fragment.setArguments(bundle);
        Utility.replaceFragment(activity, fragment, R.id.container, addToBackstack);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_100 && resultCode == Activity.RESULT_OK) {
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        if(context instanceof  AppCompatActivity)
            activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    private void initChat()
    {
        HomeScreen.getLayerClient().registerEventListener(new LayerChangeEventListener() {
            @Override
            public void onChangeEvent(LayerChangeEvent layerChangeEvent)
            {
                if(layerChangeEvent!=null && layerChangeEvent.getChanges()!=null && layerChangeEvent.getChanges().size()>0)
                    Utility.showLog(layerChangeEvent.getChanges().get(0).getAttributeName());
            }
        });

        //If there is an active conversation between the Device, Simulator, and Dashboard (web
        // client), cache it
        activeConversation = getConversation();

        //If there is an active conversation, draw it
//        drawConversation();

    }


    //Redraws the conversation window in the GUI
//    private void drawConversation() {
//
//        //Only proceed if there is a valid conversation
//        if (activeConversation != null) {
//
//            //Clear the GUI first and empty the list of stored messages
//            conversationView.removeAllViews();
//            allMessages = new Hashtable<String, MessageView>();
//
//            //Grab all the messages from the conversation and add them to the GUI
//            List<Message> allMsgs = layerClient.getMessages(activeConversation);
//            for (int i = 0; i < allMsgs.size(); i++) {
//                addMessageToView(allMsgs.get(i));
//            }
//
//            //After redrawing, force the scroll view to the bottom (most recent message)
//            conversationScroll.post(new Runnable() {
//                @Override
//                public void run() {
//                    conversationScroll.fullScroll(View.FOCUS_DOWN);
//                }
//            });
//        }
//    }

    //Create a new message and send it
//    private void sendButtonClicked() {
//
//        //Check to see if there is an active conversation between the pre-defined participants
//        if (activeConversation == null) {
//            activeConversation = getConversation();
//
//            //If there isn't, create a new conversation with those participants
//            if (activeConversation == null) {
//                activeConversation = homeScreen.getLayerClient().newConversation(ChatUsersFragment.getAllParticipants());
//            }
//        }
//
//        sendMessage(userInput.getText().toString());
//
//        //Clears the text input field
//        userInput.setText("");
//    }

    //Checks to see if there is already a conversation between the device and emulator
    private Conversation getConversation() {

        if (activeConversation == null) {

            Query query = Query.builder(Conversation.class)
                    .predicate(new Predicate(Conversation.Property.PARTICIPANTS, Predicate
                            .Operator.EQUAL_TO, ChatUsers.getAllParticipants()))
                    .sortDescriptor(new SortDescriptor(Conversation.Property.CREATED_AT,
                            SortDescriptor.Order.DESCENDING)).build();

            List<Conversation> results = HomeScreen.getLayerClient().executeQuery(query, Query.ResultType.OBJECTS);
            if (results != null && results.size() > 0) {
                return results.get(0);
            }
        }

        //Returns the active conversation (which is null by default)
        return activeConversation;
    }


    private void sendMessage(String text) {

        //Put the user's text into a message part, which has a MIME type of "text/plain" by default
        MessagePart messagePart = HomeScreen.getLayerClient().newMessagePart(text);

        //Formats the push notification that the other participants will receive
        MessageOptions options = new MessageOptions();
        options.pushNotificationMessage(Constants.USER_ID + ": " + text);

        //Creates and returns a new message object with the given conversation and array of
        // message parts
        Message message = HomeScreen.getLayerClient().newMessage(options, Arrays.asList(messagePart));

        //Sends the message
        if (activeConversation != null)
            activeConversation.send(message);
    }
    //Current conversation
    private Conversation activeConversation;

    //All messages
    private Hashtable<String, MessageView> allMessages;
}