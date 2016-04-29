package com.app.tp.layerchatintegration.fragments;

/**
 * Created by vishalrandive on 27/04/16.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tp.layerchatintegration.ChatList;
import com.app.tp.layerchatintegration.ChatUsers;
import com.app.tp.layerchatintegration.R;
import com.app.tp.layerchatintegration.babychakra.LoginController;
import com.app.tp.layerchatintegration.utility.Utility;
import com.layer.atlas.AtlasMessageComposer;
import com.layer.atlas.AtlasMessagesRecyclerView;
import com.layer.atlas.AtlasTypingIndicator;
import com.layer.atlas.messagetypes.generic.GenericCellFactory;
import com.layer.atlas.messagetypes.location.LocationCellFactory;
import com.layer.atlas.messagetypes.location.LocationSender;
import com.layer.atlas.messagetypes.singlepartimage.SinglePartImageCellFactory;
import com.layer.atlas.messagetypes.text.TextCellFactory;
import com.layer.atlas.messagetypes.text.TextSender;
import com.layer.atlas.messagetypes.threepartimage.CameraSender;
import com.layer.atlas.messagetypes.threepartimage.GallerySender;
import com.layer.atlas.messagetypes.threepartimage.ThreePartImageCellFactory;
import com.layer.atlas.provider.Participant;
import com.layer.atlas.provider.ParticipantProvider;
import com.layer.atlas.typingindicators.BubbleTypingIndicatorFactory;
import com.layer.atlas.util.views.SwipeableItem;
import com.layer.sdk.LayerClient;
import com.layer.sdk.messaging.Conversation;
import com.layer.sdk.messaging.Message;
import com.layer.sdk.query.Predicate;
import com.layer.sdk.query.Query;
import com.layer.sdk.query.SortDescriptor;

import java.util.List;
import java.util.Map;

/**
 * Created by vishalrandive on 27/04/16.
 */
public class ChatMessageFragment extends BaseFragment {

    public static final int REQUEST_CODE_100 = 100;
    public static final int REQUEST_CODE_101 = 101;
    private Context mContext;
    private AppCompatActivity activity;

    //Current conversation
    private static Conversation activeConversation;
    private AtlasMessagesRecyclerView objAtlasMessagesRecyclerView;
    private AtlasMessageComposer objAtlasMessageComposer;
    String TAG = ChatList.class.getName();


    public static ChatMessageFragment getChatMessageFragment(Conversation activeConversation){
        ChatMessageFragment.activeConversation = activeConversation;

        return new ChatMessageFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_chat_message, container, false);


        ParticipantProvider objParticipantProviderMsgSender = new ParticipantProvider() {
            @Override
            public Map<String, Participant> getMatchingParticipants(String filter, Map<String, Participant> result) {
                Utility.showLog("objParticipantProviderMsgSender getMatchingParticipants "+ filter +"," + result.size());
                return null;
            }

            @Override
            public Participant getParticipant(String userId) {

                Utility.showLog("objParticipantProviderMsgSender getParticipant "+ userId +"," + userId);
                return null;
            }
        };

        if(activeConversation == null)
            activeConversation = getConversation();

//        objAtlasMessagesRecyclerView = ((AtlasMessagesRecyclerView) view.findViewById(R.id.messages_list))
//                .init(LoginController.getLayerClient(), objParticipantProviderMsgSender, Utility.getPicasso())
//                .setConversation(activeConversation)
//                .addCellFactories(
//                        new TextCellFactory(),
//                        new ThreePartImageCellFactory(activity, LoginController.getLayerClient(), Utility.getPicasso()),
//                        new LocationCellFactory(activity, Utility.getPicasso()));


        objAtlasMessagesRecyclerView = ((AtlasMessagesRecyclerView) view.findViewById(R.id.messages_list))
                .init(LoginController.getLayerClient(), objParticipantProviderMsgSender, Utility.getPicasso())
                .setConversation(activeConversation)
                .addCellFactories(
                        new TextCellFactory(),
                        new ThreePartImageCellFactory(activity, LoginController.getLayerClient(), Utility.getPicasso()),
                        new LocationCellFactory(activity, Utility.getPicasso()),
                        new SinglePartImageCellFactory(activity, LoginController.getLayerClient(), Utility.getPicasso()),
                        new GenericCellFactory())
                .setOnMessageSwipeListener(new SwipeableItem.OnSwipeListener<Message>() {
                    @Override
                    public void onSwipe(final Message message, int direction) {
                        new AlertDialog.Builder(activity)
                                .setMessage("alert_message_delete_message")
                                .setNegativeButton("alert_button_cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO: simply update this one message
                                        objAtlasMessagesRecyclerView.getAdapter().notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                })
                                .setNeutralButton("alert_button_delete_my_devices", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        message.delete(LayerClient.DeletionMode.ALL_MY_DEVICES);
                                    }
                                })
                                .setPositiveButton(".alert_button_delete_all_participants", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        message.delete(LayerClient.DeletionMode.ALL_PARTICIPANTS);
                                    }
                                })
                                .show();
                    }
                });



        objAtlasMessageComposer = ((AtlasMessageComposer) view.findViewById(R.id.message_composer))
                .init(LoginController.getLayerClient(), objParticipantProviderMsgSender)
                .setTextSender(new TextSender())

                .addAttachmentSenders(
                        new CameraSender("Camera", R.drawable.ic_photo_camera_white_24dp, activity),
                        new GallerySender("Gallery", R.drawable.ic_photo_white_24dp, activity),
                        new LocationSender("Location", R.drawable.ic_place_white_24dp, activity));


            objAtlasMessageComposer.setConversation(activeConversation);

//        objAtlasMessageComposer = ((AtlasMessageComposer) view.findViewById(R.id.message_composer))
//                .init(LoginController.getLayerClient(), objParticipantProviderMsgSender)
//                .setTextSender(new TextSender())
//                .addAttachmentSenders(
//                        new CameraSender("Camera", R.drawable.ic_photo_camera_white_24dp, activity),
//                        new GallerySender("Gallery", R.drawable.ic_photo_white_24dp, activity),
//                        new LocationSender("Location", R.drawable.ic_place_white_24dp, activity))
//                .setOnMessageEditTextFocusChangeListener(new View.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChange(View v, boolean hasFocus) {
////                        if (hasFocus) {
////                            setUiState(UiState.CONVERSATION_COMPOSER);
////                            setTitle(true);
////                        }
//                    }
//                });
//            objAtlasMessageComposer.setConversation(activeConversation);

        AtlasTypingIndicator typingIndicator = new AtlasTypingIndicator(activity)
                .init(LoginController.getLayerClient())
                .setTypingIndicatorFactory(new BubbleTypingIndicatorFactory())
                .setTypingActivityListener(new AtlasTypingIndicator.TypingActivityListener() {
                    public void onTypingActivityChange(AtlasTypingIndicator typingIndicator, boolean active) {
                        objAtlasMessagesRecyclerView.setFooterView(active ? typingIndicator : null);
                    }
                });

        typingIndicator.setConversation(activeConversation);

        return view;
    }

//    private void replaceFragment(Fragment fragment, CharSequence title, boolean addToBackstack) {
//        Bundle bundle = new Bundle();
//        bundle.putString(HomeScreen.KEY_TITLE, title.toString());
//        fragment.setArguments(bundle);
//        Utility.replaceFragment(activity, fragment, R.id.container, addToBackstack);
//
//    }

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


    //Checks to see if there is already a conversation between the device and emulator
    private Conversation getConversation() {

        if (activeConversation == null) {

            Query query = Query.builder(Conversation.class)
                    .predicate(new Predicate(Conversation.Property.PARTICIPANTS, Predicate
                            .Operator.EQUAL_TO, ChatUsers.getAllParticipants()))
                    .sortDescriptor(new SortDescriptor(Conversation.Property.CREATED_AT,
                            SortDescriptor.Order.DESCENDING)).build();

            List<Conversation> results = LoginController.getLayerClient().executeQuery(query, Query.ResultType.OBJECTS);
            if (results != null && results.size() > 0) {
                return results.get(0);
            }
        }

        //Returns the active conversation (which is null by default)
        return activeConversation;
    }

}

