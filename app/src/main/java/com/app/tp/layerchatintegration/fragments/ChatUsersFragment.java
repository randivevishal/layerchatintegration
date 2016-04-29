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
import android.widget.Toast;

import com.app.tp.layerchatintegration.ChatList;
import com.app.tp.layerchatintegration.ChatScreen;
import com.app.tp.layerchatintegration.HomeScreen;
import com.app.tp.layerchatintegration.R;
import com.app.tp.layerchatintegration.utility.Utility;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.app.tp.layerchatintegration.utility.Constants.OPPONENT_ID;
import static com.app.tp.layerchatintegration.utility.Constants.OPPONENT_NAME;
import static com.app.tp.layerchatintegration.utility.Constants.USERS_HASHMAP;

/**
 * Created by vishalrandive on 28/03/16.
 */
public class ChatUsersFragment extends BaseFragment {

    public static final int REQUEST_CODE_100 = 100;
    public static final int REQUEST_CODE_101 = 101;
    private Context mContext;
    private AppCompatActivity activity;
    Button btnUsers [] = new Button[2];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat_users, container, false);

        Button btnUser1 = (Button) view.findViewById(R.id.btnChatUser1);
        Button btnUser2 = (Button) view.findViewById(R.id.btnChatUser2);

        btnUsers[0] = btnUser1;
        btnUsers[1] = btnUser2;


        int i =0;

        for(Map.Entry<Integer, String> map : USERS_HASHMAP.entrySet())
        {
            btnUsers[i].setText(map.getValue());
            btnUsers[i].setId(i);

            btnUsers[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOpponentValues(v.getId());
//                    replaceFragment(ChatFragment.getChatFragment(),"Chat", true);
                    startActivity(new Intent(mContext, ChatList.class));
//                    Toast.makeText(getApplicationContext(),"ss NAMe " +OPPONENT_NAME +", ID"+ OPPONENT_ID, Toast.LENGTH_SHORT).show();
                }
            });
            i++;
            //btnUsers[1].setText(map.getValue());
        }

        return view;
    }

    private void replaceFragment(Fragment fragment, CharSequence title, boolean addToBackstack) {
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

    public void setOpponentValues(int index){

        int i=0;
        for(Map.Entry<Integer, String> map : USERS_HASHMAP.entrySet())
        {
            if(index==i)
            {
                OPPONENT_NAME = map.getValue();
                OPPONENT_ID =map.getKey();
            }
            i++;
        }

        Toast.makeText(mContext, " NAMe " + OPPONENT_NAME + ", ID" + OPPONENT_ID, Toast.LENGTH_SHORT).show();

    }

    //By default, create a conversationView between these 3 participants
    public static List<String> getAllParticipants(){
//        return Arrays.asList("Device", "Simulator", "Dashboard");
        return Arrays.asList(""+OPPONENT_ID);
    }
}