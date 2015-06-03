package com.example.xtan.cepnet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class ChatInterfaceActivity extends ActionBarActivity {
    private ParseUser mUser;
    private ParseUser mChatUser;
    private String mChatUsername;
    private List< Pair<String, Pair<Boolean, Date> > > mTempChatLog = new ArrayList< Pair<String, Pair<Boolean, Date> > >();
    private List< Pair<String, Boolean> > mChatLog = new ArrayList< Pair<String, Boolean> >();
    private MessageAdapter mChatLogAdapter;
    private ListView mChatLogView;
    private ProgressBar mProgressView;

    private EditText mMessageView;
    private Button mChatSend;
    private Boolean mUpdating;

    static class MessageComparator implements Comparator< Pair<String, Pair<Boolean, Date> > > {
        public int compare(Pair<String, Pair<Boolean, Date> > a,  Pair<String, Pair<Boolean, Date> > b) {
            Date a1 = a.second.second;
            Date a2 = b.second.second;
            return a1.compareTo(a2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_interface);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mMessageView = (EditText)findViewById(R.id.chat_text);
        mChatSend = (Button)findViewById(R.id.chat_send);
        mChatLogView = (ListView)findViewById(R.id.chat_log);
        mProgressView = (ProgressBar)findViewById(R.id.chat_log_progress);
        mUser = ParseUser.getCurrentUser();

        mChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = mMessageView.getText().toString();
                mMessageView.setText("");
                if (messageText.isEmpty()) return;

                InputMethodManager inputManager = (InputMethodManager)ChatInterfaceActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(mMessageView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                ParseObject message = new ParseObject("ChatMessage");
                message.put("content", messageText);
                message.put("userFrom", mUser.getUsername());
                message.put("userTo", mChatUsername);
                message.saveInBackground();
            }
        });

        Intent intent = getIntent();
        mChatUsername = intent.getStringExtra("chatUser");
        getSupportActionBar().setTitle(mChatUsername);
        loadChatUser(true);

        Thread updateThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!mUpdating) loadChatUser(false);
                            }
                        });
                    }
                }
                catch (InterruptedException e) {}
            }
        };
        updateThread.start();
    }

    public void loadChatUser(final boolean ifShow) {
        mUpdating = true;
        if (ifShow) showProgress(true);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", mChatUsername);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    mChatUser = user;
                    loadChatLogP1(ifShow);
                }
            }
        });
    }

    public void loadChatLogP1(final boolean ifShow) {
        ParseQuery query = ParseQuery.getQuery("ChatMessage");
        query.whereEqualTo("userFrom", mUser.getUsername());
        query.whereEqualTo("userTo", mChatUser.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> chatLog1, ParseException e) {
                if (e == null) {
                    mTempChatLog.clear();
                    for (int i = 0; i < chatLog1.size(); i++) {
                        ParseObject message = chatLog1.get(i);
                        mTempChatLog.add(new Pair(message.get("content"), new Pair(true, message.getCreatedAt())));
                    }
                    loadChatLogP2(ifShow);
                }
            }
        });
    }

    public void loadChatLogP2(final boolean ifShow) {
        ParseQuery query = ParseQuery.getQuery("ChatMessage");
        query.whereEqualTo("userFrom", mChatUser.getUsername());
        query.whereEqualTo("userTo", mUser.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> chatLog2, ParseException e) {
                if (ifShow) showProgress(false);
                if (e == null) {
                    for (int i = 0; i < chatLog2.size(); i++) {
                        ParseObject message = chatLog2.get(i);
                        mTempChatLog.add(new Pair(message.get("content"), new Pair(false, message.getCreatedAt())));
                    }
                    Collections.sort(mTempChatLog, new MessageComparator());

                    mChatLog.clear();
                    for (int i = 0; i < mTempChatLog.size(); i++) {
                        Pair<String, Pair<Boolean, Date> > item = mTempChatLog.get(i);
                        mChatLog.add(new Pair(item.first, item.second.first));
                    }
                    if (mChatLogAdapter == null) {
                        mChatLogAdapter = new MessageAdapter(ChatInterfaceActivity.this, R.layout.activity_chat_interface_row, mChatLog);
                        mChatLogView.setAdapter(mChatLogAdapter);
                    }
                    else mChatLogAdapter.notifyDataSetChanged();
                    mUpdating = false;
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            mChatLogView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
        else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mChatLogView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
