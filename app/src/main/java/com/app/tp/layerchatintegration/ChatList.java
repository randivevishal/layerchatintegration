package com.app.tp.layerchatintegration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.app.tp.layerchatintegration.fragments.ChatListFragment;
import com.app.tp.layerchatintegration.utility.Utility;

/**
 * Created by vishalrandive on 27/04/16.
 */
public class ChatList extends AppCompatActivity {


    FrameLayout flHomeContainer;
    public static final String KEY_TITLE = "title";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        flHomeContainer = (FrameLayout) findViewById(R.id.container);

        initDefaultFragmentView();

    }

    private void initDefaultFragmentView()
    {
        Fragment fragment = new ChatListFragment();
        replaceFragment(fragment, "CHAT_USERS", true);
    }


    private void replaceFragment(Fragment fragment, CharSequence title, boolean addToBackstack)
    {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title.toString());
        fragment.setArguments(bundle);
        Utility.replaceFragment(this, fragment, R.id.container, addToBackstack);
        setTitle(title.toString());
    }


    @Override
    public void onBackPressed() {
        if(Utility.getFragmentCounts(this) >0)
        {
            finish();
        }
        else
            super.onBackPressed();
    }
}
