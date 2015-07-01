package com.example.xtan.cepnet;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private ListView mChatListView;
    private ProgressBar mProgressView;
    private List<String> mChatList = new ArrayList<String>();
    private ChatAdapter mChatListAdapter;
    private ParseUser mUser;
    private Button mRefreshButton;

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
        mRefreshButton = (Button)rootView.findViewById(R.id.chat_list_refresh);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadChatList();
            }
        });

        mChatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatInterfaceActivity.class);
                intent.putExtra("chatUser", mChatList.get(position));
                startActivity(intent);
            }
        });

        loadChatList();
        return rootView;
    }

    public void loadChatList() {
        if (mProgressView.getVisibility() == View.VISIBLE) return;
        showProgress(true);
        mUser = ParseUser.getCurrentUser();
        if (mUser == null) return;

        final List<String> friendList = mUser.getList("friendList");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                showProgress(false);
                if (e == null) {
                    mChatList.clear();
                    for (int i = 0; i < users.size(); i++) {
                        ParseUser user = users.get(i);
                        boolean isFriend = false;
                        if (user.getUsername().equals(mUser.getUsername())) continue;
                        for (int j = 0; j < friendList.size() && !isFriend; j++) {
                            if (friendList.get(j).equals(user.getUsername())) isFriend = true;
                        }
                        if (!isFriend) continue;

                        boolean isFriendFriend = false;
                        List<String> friendFriendList = user.getList("friendList");
                        for (int j = 0; j < friendFriendList.size() && !isFriendFriend; j++) {
                            if (friendFriendList.get(j).equals(mUser.getUsername())) isFriendFriend = true;
                        }
                        if (!isFriendFriend) continue;
                        mChatList.add(user.getUsername());
                    }

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
