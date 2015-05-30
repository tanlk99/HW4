package com.example.xtan.cepnet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xtan on 5/28/2015.
 */
public class ChatAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> mChatUserList;
    private int mLayoutResourceId;

    public ChatAdapter(Context context, int layoutResourceId, List<String> chatUserList) {
        super(context, layoutResourceId, chatUserList);
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mChatUserList = chatUserList;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        ChatHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new ChatHolder();
            holder.mNameView = (TextView)row.findViewById(R.id.chat_list_name);
            row.setTag(holder);
        }
        else {
            holder = (ChatHolder)row.getTag();
        }

        String currentUser = mChatUserList.get(position);
        holder.mNameView.setText(currentUser);
        return row;
    }

    static class ChatHolder {
        private TextView mNameView;
    }
}
