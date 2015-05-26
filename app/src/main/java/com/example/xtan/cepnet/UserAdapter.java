package com.example.xtan.cepnet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.List;

/**
 * Created by xtan on 5/25/2015.
 */
public class UserAdapter extends ArrayAdapter<ParseUser> {
    private Context mContext;
    private List<ParseUser> mParseUserList;
    private int mLayoutResourceId;

    public UserAdapter(Context context, int layoutResourceId, List<ParseUser> parseUserList) {
        super(context, android.R.layout.simple_list_item_1, parseUserList);
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mParseUserList = parseUserList;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        UserHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new UserHolder();
            holder.mNameView = (TextView)row.findViewById(R.id.user_list_name);
            holder.mToggleButton = (ImageButton)row.findViewById(R.id.toggle_friend);
            row.setTag(holder);
        }
        else {
            holder = (UserHolder)row.getTag();
        }

        ParseUser currentUser = mParseUserList.get(position);
        holder.mNameView.setText(currentUser.getUsername());
        return row;
    }

    static class UserHolder {
        private TextView mNameView;
        private ImageButton mToggleButton;
    }
}
