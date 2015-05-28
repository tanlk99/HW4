package com.example.xtan.cepnet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.ParseUser;

import java.util.List;

/**
 * Created by xtan on 5/28/2015.
 */
public class ChatAdapter extends ArrayAdapter<ParseUser> {
    private Context mContext;
    private List<ParseUser> mChatUserList;
    private int mLayoutResourceId;

    public ChatAdapter(Context context, int layoutResourceId, List<ParseUser> chatUserList) {
        super(context, layoutResourceId, chatUserList);
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mChatUserList = chatUserList;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        return row;
    }
}
