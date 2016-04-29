package com.app.tp.layerchatintegration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.app.tp.layerchatintegration.utility.Constants.OPPONENT_ID;
import static com.app.tp.layerchatintegration.utility.Constants.OPPONENT_NAME;
import static com.app.tp.layerchatintegration.utility.Constants.USERS_HASHMAP;

/**
 * Created by vishalrandive on 23/03/16.
 */
public class ChatUsers extends AppCompatActivity {

    Button btnUsers [] = new Button[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_users);

        Button btnUser1 = (Button) findViewById(R.id.btnChatUser1);
        Button btnUser2 = (Button) findViewById(R.id.btnChatUser2);

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
                    startActivity(new Intent(ChatUsers.this, ChatScreen.class));
//                    Toast.makeText(getApplicationContext(),"ss NAMe " +OPPONENT_NAME +", ID"+ OPPONENT_ID, Toast.LENGTH_SHORT).show();
                }
            });
            i++;
            //btnUsers[1].setText(map.getValue());
        }

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

        Toast.makeText(getApplicationContext()," NAMe " +OPPONENT_NAME +", ID"+ OPPONENT_ID, Toast.LENGTH_SHORT).show();

    }

    //By default, create a conversationView between these 3 participants
    public static List<String> getAllParticipants(){
//        return Arrays.asList("Device", "Simulator", "Dashboard");
        return Arrays.asList(OPPONENT_NAME);
    }

}
