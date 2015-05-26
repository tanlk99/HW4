package com.example.xtan.cepnet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {
    private ListView mListView;
    private ProgressBar mProgressView;
    private List< Pair<ParseUser, Boolean> > mUserList = new ArrayList< Pair<ParseUser, Boolean> >();
    private ParseUser mUser;

    public UserFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        mListView = (ListView)rootView.findViewById(R.id.user_list);
        mProgressView = (ProgressBar)rootView.findViewById(R.id.user_list_progress);
        mUser = ParseUser.getCurrentUser();
        showProgress(true);

        final List<ParseUser> friendList = mUser.getList("friendList");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                showProgress(false);
                if (e == null) {
                    if (friendList != null) {
                        for (int i = 0; i < users.size(); i++) {
                            ParseUser user = users.get(i);
                            if (user.getUsername() == mUser.getUsername()) continue;
                            for (int j = 0; j < friendList.size(); j++) {
                                if (friendList.get(j).getUsername() == mUser.getUsername()) mUserList.add(new Pair(friendList.get(j), true));
                                else mUserList.add(new Pair(friendList.get(j), false));
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < users.size(); i++) {
                            ParseUser user = users.get(i);
                            mUserList.add(new Pair(user, false));
                        }
                    }

                    Toast.makeText(getActivity(), Integer.toString(mUserList.size()), Toast.LENGTH_LONG).show();
                    UserAdapter mAdapter = new UserAdapter(getActivity(), R.layout.fragment_user_row, mUserList);
                    mListView.setAdapter(mAdapter);
                    mListView.setClickable(true);
                }
            }
        });

        return rootView;
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
        }
        else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
