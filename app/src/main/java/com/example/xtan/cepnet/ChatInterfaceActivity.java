package com.example.xtan.cepnet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.ParseUser;


public class ChatInterfaceActivity extends ActionBarActivity {
    private ParseUser mUser;
    private ParseUser mChatUser;
    private String mChatUsername;
    private ListView mChatLogView;
    private ProgressBar mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_interface);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mChatLogView = (ListView)findViewById(R.id.chat_log);
        mProgressView = (ProgressBar)findViewById(R.id.chat_log_progress);
        mUser = ParseUser.getCurrentUser();

        Intent intent = getIntent();
        mChatUsername = intent.getStringExtra("chatUser");
        getSupportActionBar().setTitle(mChatUsername);
        loadChatLog();
    }

    public void loadChatLog() {

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
