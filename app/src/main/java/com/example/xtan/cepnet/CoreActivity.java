package com.example.xtan.cepnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

public class CoreActivity extends ActionBarActivity implements UserAdapter.UpdateInterface {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private UserFragment mUserFragment;
    private ChatFragment mChatFragment;
    private ProfileFragment mProfileFragment;
    private BroadcastReceiver mLoginReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mLoginReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mViewPager.getAdapter().notifyDataSetChanged();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mLoginReceiver, new IntentFilter("login"));

        /*
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
        */

        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(CoreActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLoginReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_core, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.getCurrentUser().logOutInBackground();
            Intent intent = new Intent(CoreActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_user) {
            mViewPager.setCurrentItem(0);
            return true;
        }
        else if (id == R.id.action_chat) {
            mViewPager.setCurrentItem(1);
            return true;
        }
        else if (id == R.id.action_profile) {
            mViewPager.setCurrentItem(2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                UserFragment userTab = new UserFragment();
                mUserFragment = userTab;
                return userTab;
            }
            else if (position == 1) {
                ChatFragment chatTab = new ChatFragment();
                mChatFragment = chatTab;
                return chatTab;
            }
            else {
                ProfileFragment profileTab = new ProfileFragment();
                mProfileFragment = profileTab;
                return profileTab;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public void updateFragmentUserList(int position) {
        Pair<String, Boolean> user1 = mUserFragment.mUserList.get(position);
        mUserFragment.mUserList.set(position, new Pair(user1.first, !user1.second));
        mUserFragment.mUserListAdapter.notifyDataSetChanged();
    }
}