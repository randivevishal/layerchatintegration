package com.app.tp.layerchatintegration.layer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.tp.layerchatintegration.ChatList;
import com.app.tp.layerchatintegration.R;
import com.app.tp.layerchatintegration.utility.Constants;
import com.app.tp.layerchatintegration.babychakra.LoginController;
import com.app.tp.layerchatintegration.utility.Utility;
import com.layer.atlas.AtlasConversationsRecyclerView;
import com.layer.atlas.adapters.AtlasConversationsAdapter;
import com.layer.atlas.provider.Participant;
import com.layer.atlas.provider.ParticipantProvider;
import com.layer.atlas.util.Log;
import com.layer.atlas.util.views.SwipeableItem;
import com.layer.sdk.LayerClient;
import com.layer.sdk.messaging.Conversation;

import java.util.Map;

/**
 * Created by vishalrandive on 28/04/16.
 */
public class ChatListScreen extends AppCompatActivity {



    AtlasConversationsRecyclerView objAtlasConversationsRecyclerView;
    String TAG = ChatList.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        objAtlasConversationsRecyclerView  = (AtlasConversationsRecyclerView) findViewById(R.id.conversations_list);

        // Atlas methods
        objAtlasConversationsRecyclerView.init(LoginController.getLayerClient(), new ParticipantProvider() {
            @Override
            public Map<String, Participant> getMatchingParticipants(String filter, Map<String, Participant> result) {
                return null;
            }

            @Override
            public Participant getParticipant(String userId) {
                return null;
            }
        }, Utility.getPicasso())
                .setInitialHistoricMessagesToFetch(20)
                .setOnConversationClickListener(new AtlasConversationsAdapter.OnConversationClickListener() {
                    @Override
                    public void onConversationClick(AtlasConversationsAdapter adapter, Conversation conversation) {
                        Intent intent = new Intent(ChatListScreen.this, ChatMessageScreen.class);
                        if (Log.isLoggable(Log.VERBOSE)) {
                            Log.v("Launching MessagesListActivity with existing conversation ID: " + conversation.getId());
                        }
                        intent.putExtra(Constants.LAYER_CONVERSATION_KEY, conversation.getId());
                        startActivity(intent);
                    }

                    @Override
                    public boolean onConversationLongClick(AtlasConversationsAdapter adapter, Conversation conversation) {
                        return false;
                    }
                })
                .setOnConversationSwipeListener(new SwipeableItem.OnSwipeListener<Conversation>() {
                    @Override
                    public void onSwipe(final Conversation conversation, int direction) {
                        new AlertDialog.Builder(ChatListScreen.this)
                                .setMessage(R.string.alert_message_delete_conversation)
                                .setNegativeButton(R.string.alert_button_cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO: simply update this one conversation
                                        objAtlasConversationsRecyclerView.getAdapter().notifyDataSetChanged();
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

    }
}
