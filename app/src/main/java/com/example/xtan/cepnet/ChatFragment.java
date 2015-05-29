package com.example.xtan.cepnet;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class ChatFragment extends Fragment {
    private ListView mChatListView;
    private ProgressBar mProgressView;
    private List<ParseUser> mChatList = new ArrayList<ParseUser>();
    private ChatAdapter mChatListAdapter;
    private ParseUser mUser;

    public ChatFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        mChatListView = (ListView)rootView.findViewById(R.id.chat_list);
        mProgressView = (ProgressBar)rootView.findViewById(R.id.chat_list_progress);
        loadChatList();
        return rootView;
    }

    public void loadChatList() {
        showProgress(true);
        mUser = ParseUser.getCurrentUser();
        final List<ParseUser> friendList = mUser.getList("friendList");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                showProgress(false);
                if (e == null) {
                    mChatList.clear();
                    for (int i = 0; i < users.size(); i++) {
                        ParseUser user = users.get(i);
                        boolean isFriend = false;
                        if (user.getUsername() == mUser.getUsername()) continue;
                        for (int j = 0; j < friendList.size() && !isFriend; j++) {
                            if (friendList.get(j).getUsername() == user.getUsername()) isFriend = true;
                        }
                        if (!isFriend) continue;

                        boolean isFriendFriend = false;
                        List<ParseUser> friendFriendList = user.getList("friendList");
                        for (int j = 0; j < friendFriendList.size() && !isFriendFriend; j++) {
                            if (friendFriendList.get(j).getUsername() == mUser.getUsername()) isFriendFriend = true;
                        }
                        if (!isFriendFriend) continue;
                        mChatList.add(user);
                    }

                    Toast.makeText(getActivity(), Integer.toString(mChatList.size()), Toast.LENGTH_LONG).show();
                    mChatListAdapter = new ChatAdapter(getActivity(), R.layout.fragment_chat_row, mChatList);
                    mChatListView.setAdapter(mChatListAdapter);
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
            mChatListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
        else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mChatListView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
