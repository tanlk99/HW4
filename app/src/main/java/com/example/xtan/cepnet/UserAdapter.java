package com.example.xtan.cepnet;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by xtan on 5/25/2015.
 */
public class UserAdapter extends ArrayAdapter< Pair<ParseUser, Boolean> > {
    private Context mContext;
    private List< Pair<ParseUser, Boolean> > mParseUserList;
    private int mLayoutResourceId;
    private UpdateInterface mListener;

    public UserAdapter(Context context, int layoutResourceId, List< Pair<ParseUser, Boolean> > parseUserList) {
        super(context, android.R.layout.simple_list_item_1, parseUserList);
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mParseUserList = parseUserList;
        mListener = (UpdateInterface)context;
    }

    @Override
    public View getView(final int position, View row, ViewGroup parent) {
        UserHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new UserHolder();
            holder.mNameView = (TextView)row.findViewById(R.id.user_list_name);
            holder.mToggleFriend = (ImageView)row.findViewById(R.id.toggle_friend);
            row.setTag(holder);
        }
        else {
            holder = (UserHolder)row.getTag();
        }

        final Pair<ParseUser, Boolean> currentUser = mParseUserList.get(position);
        holder.mNameView.setText(currentUser.first.getUsername());
        holder.mToggleFriend.setClickable(true);

        if (currentUser.second) holder.mToggleFriend.setImageResource(R.drawable.delete_friend_icon);
        else holder.mToggleFriend.setImageResource(R.drawable.add_friend_icon);

        holder.mToggleFriend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                if (!currentUser.second) user.add("friendList", currentUser.first);
                else user.removeAll("friendList", (Collection)Arrays.asList(currentUser.first));
                user.saveInBackground();
                mListener.updateFragmentUserList(position);
            }
        });

        return row;
    }

    static class UserHolder {
        private TextView mNameView;
        private ImageView mToggleFriend;
    }

    public static interface UpdateInterface {
        public void updateFragmentUserList(int position);
    }
}
