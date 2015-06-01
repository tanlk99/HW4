package com.example.xtan.cepnet;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xtan on 6/1/2015.
 */
public class MessageAdapter extends ArrayAdapter<Pair<String, Boolean>> {
    private Context mContext;
    private int mLayoutResourceId;
    private List< Pair<String, Boolean> > mMessageList;

    public MessageAdapter(Context context, int layoutResourceId, List< Pair<String, Boolean> > messageList) {
        super(context, layoutResourceId, messageList);
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mMessageList = messageList;
    }

    @Override
    public View getView(final int position, View row, ViewGroup parent) {
        MessageHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new MessageHolder();
            holder.mContent = (TextView)row.findViewById(R.id.message_content);
            row.setTag(holder);
        }
        else {
            holder = (MessageHolder)row.getTag();
        }

        Pair<String, Boolean> message = mMessageList.get(position);
        holder.mContent.setText(message.first);
        if (!message.second) {
            holder.mContent.setGravity(Gravity.RIGHT);
        }
        return row;
    }

    static class MessageHolder {
        private TextView mContent;
    }
}
