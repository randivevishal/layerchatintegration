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

import com.app.tp.layerchatintegration.ChatList;
import com.app.tp.layerchatintegration.HomeScreen;
import com.app.tp.layerchatintegration.R;
import com.app.tp.layerchatintegration.babychakra.LoginController;
import com.app.tp.layerchatintegration.utility.Utility;
import com.layer.atlas.AtlasConversationsRecyclerView;
import com.layer.atlas.adapters.AtlasConversationsAdapter;
import com.layer.atlas.provider.Participant;
import com.layer.atlas.provider.ParticipantProvider;
import com.layer.sdk.messaging.Conversation;

import java.util.Map;

/**
 * Created by vishalrandive on 27/04/16.
 */
public class ChatListFragment extends BaseFragment {

        public static final int REQUEST_CODE_100 = 100;
        public static final int REQUEST_CODE_101 = 101;
        private Context mContext;
        private AppCompatActivity activity;

    AtlasConversationsRecyclerView objAtlasConversationsRecyclerView;
    String TAG = ChatList.class.getName();

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View view = inflater.inflate(R.layout.layout_chat_users, container, false);

            objAtlasConversationsRecyclerView = ((AtlasConversationsRecyclerView) view.findViewById(R.id.conversations_list))
                    .init(LoginController.getLayerClient(), new ParticipantProvider() {
                        @Override
                        public Map<String, Participant> getMatchingParticipants(String filter, Map<String, Participant> result) {
                            Utility.showLog(TAG + filter + "," + result.size());
                            return null;
                        }
                        @Override
                        public Participant getParticipant(String userId) {
                            Utility.showLog(TAG + userId + "," + userId);
                            return null;
                        }
                    }, Utility.getPicasso())

                    .setOnConversationClickListener(new AtlasConversationsAdapter.OnConversationClickListener() {
                        public void onConversationClick(AtlasConversationsAdapter adapter, Conversation conversation)
                        {
//                        launchMessagesList(conversation);
                            Fragment fragment = ChatMessageFragment.getChatMessageFragment(conversation);
                            replaceFragment(fragment, true);
                        }

                        public boolean onConversationLongClick(AtlasConversationsAdapter adapter, Conversation conversation) {
                            return false;
                        }
                    });

            return view;
        }

        private void replaceFragment(Fragment fragment, boolean addToBackstack) {
            Bundle bundle = new Bundle();
            bundle.putString(HomeScreen.KEY_TITLE, "CHAT_SCREEN");
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

    }
