package com.app.tp.layerchatintegration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.app.tp.layerchatintegration.utility.Utility;
import com.layer.atlas.AtlasAddressBar;
import com.layer.atlas.AtlasConversationsRecyclerView;
import com.layer.atlas.AtlasMessageComposer;
import com.layer.atlas.AtlasMessagesRecyclerView;
import com.layer.atlas.AtlasTypingIndicator;
import com.layer.atlas.adapters.AtlasConversationsAdapter;
import com.layer.atlas.messagetypes.location.LocationCellFactory;
import com.layer.atlas.messagetypes.location.LocationSender;
import com.layer.atlas.messagetypes.text.TextCellFactory;
import com.layer.atlas.messagetypes.text.TextSender;
import com.layer.atlas.messagetypes.threepartimage.CameraSender;
import com.layer.atlas.messagetypes.threepartimage.GallerySender;
import com.layer.atlas.messagetypes.threepartimage.ThreePartImageCellFactory;
import com.layer.atlas.provider.Participant;
import com.layer.atlas.provider.ParticipantProvider;
import com.layer.atlas.typingindicators.BubbleTypingIndicatorFactory;
import com.layer.atlas.util.picasso.requesthandlers.MessagePartRequestHandler;
import com.layer.sdk.messaging.Conversation;
import com.layer.sdk.query.Predicate;
import com.layer.sdk.query.Query;
import com.layer.sdk.query.SortDescriptor;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * Created by vishalrandive on 28/03/16.
 */
public class ChatScreen extends AppCompatActivity {

    AtlasConversationsRecyclerView conversationsList;

    AtlasMessagesRecyclerView messagesList;
    AtlasMessageComposer messageComposer;
    AtlasAddressBar addressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        TextView tvChatMessages = (TextView) findViewById(R.id.tvChatMessages);
        tvChatMessages.setText("HELLO MSGE : \n");




//        addressBar = (AtlasAddressBar) findViewById(R.id.address_bar)
//                .init(HomeScreen.getLayerClient(), objParticipantProvider, Utility.getPicasso())
//                .setOnConversationClickListener(new AtlasConversationsAdapter.OnConversationClickListener() {
//                    @Override
//                    public void onConversationClick(AtlasConversationsAdapter adapter, Conversation conversation) {
//                        setConversation(conversation);
//                    }
//
//                    @Override
//                    public boolean onConversationLongClick(AtlasConversationsAdapter adapter, Conversation conversation) {
//                        return false;
//                    }
//
//                })
//                .setOnParticipantSelectionChangeListener(new AtlasAddressBar.OnParticipantSelectionChangeListener() {
//                    public void onParticipantSelectionChanged(AtlasAddressBar addressBar, List<String> participantIds) {
//                        if (participantIds.isEmpty()) {
//                            setConversation(null);
//                            return;
//                        }
//                        try {
//                            ConversationOptions options = new ConversationOptions().distinct(true);
//                            setConversation(layerClient.newConversation(options, participantIds), false);
//                        } catch (LayerConversationException e) {
//                            setConversation(e.getConversation(), false);
//                        }
//                    }
//                });
//



        conversationsList = ((AtlasConversationsRecyclerView) findViewById(R.id.conversations_list))
                .init(HomeScreen.getLayerClient(), new ParticipantProvider() {
                    @Override
                    public Map<String, Participant> getMatchingParticipants(String filter, Map<String, Participant> result) {
                        Utility.showLog("DSKJLSDJLKDJSLFJLKSD "+ filter + "," + result.size());
                        return null;
                    }

                    @Override
                    public Participant getParticipant(String userId) {
                        Utility.showLog("DSKJLSDJLKDJSLFJLKSD " + userId + "," + userId);
                        return null;
                    }
                }, Utility.getPicasso())
                .setOnConversationClickListener(new AtlasConversationsAdapter.OnConversationClickListener() {
                    public void onConversationClick(AtlasConversationsAdapter adapter, Conversation conversation) {
//                        launchMessagesList(conversation);
                    }

                    public boolean onConversationLongClick(AtlasConversationsAdapter adapter, Conversation conversation) {
                        return false;
                    }
                });

        activeConversation = getConversation();


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


        messagesList = ((AtlasMessagesRecyclerView) findViewById(R.id.messages_list))
                .init(HomeScreen.getLayerClient(), objParticipantProviderMsgSender, Utility.getPicasso())
                .setConversation(activeConversation)
                .addCellFactories(
                        new TextCellFactory(),
                        new ThreePartImageCellFactory(this, HomeScreen.getLayerClient(), Utility.getPicasso()),
                        new LocationCellFactory(this, Utility.getPicasso()));



        AtlasTypingIndicator typingIndicator = new AtlasTypingIndicator(this)
                .init(HomeScreen.getLayerClient())
                .setTypingIndicatorFactory(new BubbleTypingIndicatorFactory())
                .setTypingActivityListener(new AtlasTypingIndicator.TypingActivityListener() {
                    public void onTypingActivityChange(AtlasTypingIndicator typingIndicator, boolean active) {
                        messagesList.setFooterView(active ? typingIndicator : null);
                    }
                });

        typingIndicator.setConversation(activeConversation);





        messageComposer = ((AtlasMessageComposer) findViewById(R.id.message_composer))
                .init(HomeScreen.getLayerClient(), objParticipantProviderMsgSender)

                .setTextSender(new TextSender())
                .addAttachmentSenders(
                        new CameraSender("Camera", R.drawable.ic_photo_camera_white_24dp, this),
                        new GallerySender("Gallery", R.drawable.ic_photo_white_24dp, this),
                        new LocationSender("Location", R.drawable.ic_place_white_24dp, this));
        messageComposer.setEnabled(true);


    }

    //Current conversation
    private Conversation activeConversation;
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

    
}
