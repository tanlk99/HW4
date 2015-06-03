package com.example.xtan.cepnet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class ProfileViewActivity extends ActionBarActivity {
    private String mViewUsername;
    private ParseUser mViewUser;
    private TextView mProfileNameView;
    private TextView mProfileEmailView;
    private TableLayout mProfileTable;
    private ProgressBar mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mProfileNameView = (TextView)findViewById(R.id.profile_view_name);
        mProfileEmailView = (TextView)findViewById(R.id.profile_view_email);
        mProfileTable = (TableLayout)findViewById(R.id.profile_view_table);
        mProgressView = (ProgressBar)findViewById(R.id.profile_view_progress);

        Intent intent = getIntent();
        mViewUsername = intent.getStringExtra("viewUsername");
        getSupportActionBar().setTitle(mViewUsername + "'s Profile");
        loadTable();
    }

    public void loadTable() {
        showProgress(true);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", mViewUsername);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                showProgress(false);
                if (e == null) {
                    mViewUser = user;
                    if (mViewUser.getString("fullName") != null) mProfileNameView.setText(mViewUser.getString("fullName"));
                    if (mViewUser.getString("email") != null) mProfileEmailView.setText(mViewUser.getEmail());
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
            mProfileTable.setVisibility(show ? View.GONE : View.VISIBLE);
        }
        else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProfileTable.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
