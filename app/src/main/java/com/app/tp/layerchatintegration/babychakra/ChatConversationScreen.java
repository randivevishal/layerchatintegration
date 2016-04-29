package com.app.tp.layerchatintegration.babychakra;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.app.tp.layerchatintegration.ChatList;
import com.app.tp.layerchatintegration.R;
import com.app.tp.layerchatintegration.utility.Constants;
import com.app.tp.layerchatintegration.utility.Utility;
import com.layer.atlas.AtlasConversationsRecyclerView;
import com.layer.atlas.adapters.AtlasConversationsAdapter;
import com.layer.atlas.provider.Participant;
import com.layer.atlas.provider.ParticipantProvider;
import com.layer.atlas.util.views.SwipeableItem;
import com.layer.sdk.LayerClient;
import com.layer.sdk.messaging.Conversation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.app.tp.layerchatintegration.babychakra.LoginController.getLayerClient;
import static com.app.tp.layerchatintegration.utility.Utility.getPicasso;
/**
 * Created by vishalrandive on 28/04/16.
 */
public class ChatConversationScreen extends AppCompatActivity {

    String TAG = ChatList.class.getName();
     AtlasConversationsRecyclerView conversationsList;
    List<Participant> mParticipants = new ArrayList<Participant>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversation);

        conversationsList = (AtlasConversationsRecyclerView) findViewById(R.id.conversations_list);

        // Atlas methods
        conversationsList.init(getLayerClient(), new ParticipantProvider() {
            @Override
            public Map<String, Participant> getMatchingParticipants(String filter, Map<String, Participant> result) {
                Utility.showLog("user id - " + result.size() + ", " + filter);

                return null;
            }

            @Override
            public Participant getParticipant(String userId) {
                Utility.showLog("user id - " + userId);

//                if (!userId.equals(getLayerClient().getAuthenticatedUserId())) {
//                    Participant participant = getParticipant(userId);
//                    if (participant != null)
//                    mParticipants.add(participant);
//                }

                return null;
            }
        }, getPicasso())
                .setInitialHistoricMessagesToFetch(20)
                .setOnConversationClickListener(new AtlasConversationsAdapter.OnConversationClickListener() {
                    @Override
                    public void onConversationClick(AtlasConversationsAdapter adapter, Conversation conversation) {
                        Intent intent = new Intent(ChatConversationScreen.this, ChatMessagesActivity.class);
                            Utility.showLog("Launching MessagesListActivity with existing conversation ID: " + conversation.getId());
                        intent.putExtra(Constants.LAYER_CONVERSATION_KEY, conversation.getId());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public boolean onConversationLongClick(AtlasConversationsAdapter adapter, Conversation conversation) {
                        return false;
                    }
                })
                .setOnConversationSwipeListener(new SwipeableItem.OnSwipeListener<Conversation>() {
                    @Override
                    public void onSwipe(final Conversation conversation, int direction) {
                        new AlertDialog.Builder(ChatConversationScreen.this)
                                .setMessage(R.string.alert_message_delete_conversation)
                                .setNegativeButton(R.string.alert_button_cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO: simply update this one conversation
                                        conversationsList.getAdapter().notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                })
                                .setNeutralButton(R.string.alert_button_delete_my_devices, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        conversation.delete(LayerClient.DeletionMode.ALL_MY_DEVICES);
                                    }
                                })
                                .setPositiveButton(R.string.alert_button_delete_all_participants, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        conversation.delete(LayerClient.DeletionMode.ALL_PARTICIPANTS);
                                    }
                                })
                                .show();
                    }
                });


        findViewById(R.id.floating_action_button)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        startActivity(new Intent(ChatConversationScreen.this, ChatMessagesActivity.class));
                    }
                });

    }
}
