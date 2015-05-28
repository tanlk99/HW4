package com.example.xtan.cepnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.Locale;


public class CoreActivity extends ActionBarActivity implements UserAdapter.UpdateInterface {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    UserFragment mUserFragment;
    ChatFragment mChatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

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
        else Toast.makeText(getApplicationContext(), "Welcome " + user.getUsername(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_core, menu);
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
            else return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 1:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    //TODO: Replace class with actual fragments
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_core, container, false);
            TextView text = (TextView)rootView.findViewById(R.id.section_label);
            ViewPager pager = (ViewPager)getActivity().findViewById(R.id.pager);
            text.setText(pager.getAdapter().getPageTitle(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public void loadFragmentUserList() {
        mUserFragment.loadUserList();
    }
}