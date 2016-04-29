package com.app.tp.layerchatintegration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.app.tp.layerchatintegration.utility.Constants;
import com.app.tp.layerchatintegration.utility.GenericListener;
import com.app.tp.layerchatintegration.babychakra.LoginController;

import static com.app.tp.layerchatintegration.utility.Constants.USERS_HASHMAP;
import static com.app.tp.layerchatintegration.utility.Constants.USER_ID;
import static com.app.tp.layerchatintegration.utility.Constants.USER_NAME;


/**
 * Created by vishalrandive on 23/03/16.
 */
public class Login extends AppCompatActivity implements View.OnClickListener{



//    Google App Project number  : 564208962528
//    GCM Key 'Server Key 1' : AIzaSyCcMwwu4re1Mho4jqADcaCUAXSf5aVeOMs
//
//    User Ids:
//    Mohit: 41955
//    Bharat: 46120
//    Vishal: 26082
//
//    all above IDs have email xyz@babychakra.com
//
//    API: http://vapi.babychakra-dev.net/layerIdToken?user_id=$user_id&nonce=$nonce
//    where $user_id = one those three
//    and $nonce = nonce given to android app by layer


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn1 = (Button)findViewById(R.id.btnUser1);btn1.setOnClickListener(this);
        Button btn2 = (Button)findViewById(R.id.btnUser2);btn2.setOnClickListener(this);
        Button btn3 = (Button)findViewById(R.id.btnUser3);btn3.setOnClickListener(this);

        btn1.setText("MOHIT");
        btn2.setText("BHARAT");
        btn3.setText("VISHAL");

        Button btn4 = (Button)findViewById(R.id.btnUser4);btn4.setOnClickListener(this);
        btn4.setText("VISHAL Randive");

    }

    @Override
    public void onClick(View v) {

        USERS_HASHMAP.clear();

        switch (v.getId())
        {

            case R.id.btnUser1: //MOHIT 41955
                USER_ID ="41955";
                USER_NAME = "Mohit";

                USERS_HASHMAP.put(46120,"Bharat");
                USERS_HASHMAP.put(26082,"Vishal");

                break;

            case R.id.btnUser2:
                USER_ID ="46120";
                USER_NAME = "Bharat";

                USERS_HASHMAP.put(41955,"Mohit");
                USERS_HASHMAP.put(26082,"Vishal");
                break;

            case R.id.btnUser3:
                USER_ID ="26082";
                USER_NAME = "Vishal";

                USERS_HASHMAP.put(41955, "Mohit");
                USERS_HASHMAP.put(46120,"Bharat");
                break;


            case R.id.btnUser4:
                USER_ID = "26083.vishalrandive";
                USER_NAME = "VishalRandive";

                USERS_HASHMAP.put(41955, "Mohit");
                USERS_HASHMAP.put(46120,"Bharat");
                break;

        }
//        startActivity(new Intent(Login.this, HomeScreen.class));

        new LoginController(new GenericListener<Object>(){
            @Override
            public void onResponse(int callerID, Object messages) {
                switch (callerID){
                    case Constants.DEFAULT_ID:

                        break;
                }
            }
        }, this);


    }
}
