package com.example.xtan.cepnet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.ParseUser;

public class ChatFragment extends Fragment {
    private ListView mChatListView;
    private ProgressBar mProgressView;
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
        return rootView;
    }
}
