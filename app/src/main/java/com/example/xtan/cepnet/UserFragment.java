package com.example.xtan.cepnet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {
    private ListView mUserListView;
    private ProgressBar mProgressView;
    public List< Pair<String, Boolean> > mUserList = new ArrayList< Pair<String, Boolean> >();
    public UserAdapter mUserListAdapter;
    private ParseUser mUser;

    public UserFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        mUserListView = (ListView)rootView.findViewById(R.id.user_list);
        mProgressView = (ProgressBar)rootView.findViewById(R.id.user_list_progress);

        mUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ProfileViewActivity.class);
                intent.putExtra("viewUsername", mUserList.get(position).first);
                startActivity(intent);
            }
        });

        loadUserList();
        return rootView;
    }

    public void loadUserList() {
        showProgress(true);
        mUser = ParseUser.getCurrentUser();
        if (mUser == null) return;

        final List<String> friendList = mUser.getList("friendList");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                showProgress(false);
                if (e == null) {
                    mUserList.clear();
                    for (int i = 0; i < users.size(); i++) {
                        ParseUser user = users.get(i);
                        boolean isFriend = false;
                        if (user.getUsername().equals(mUser.getUsername())) continue;
                        for (int j = 0; j < friendList.size() && !isFriend; j++) {
                            if (friendList.get(j).equals(user.getUsername())) {
                                mUserList.add(new Pair(user.getUsername(), true));
                                isFriend = true;
                            }
                        }

                        if (!isFriend) mUserList.add(new Pair(user.getUsername(), false));
                    }

                    mUserListAdapter = new UserAdapter(getActivity(), R.layout.fragment_user_row, mUserList);
                    mUserListView.setAdapter(mUserListAdapter);
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
            mUserListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
        else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mUserListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
