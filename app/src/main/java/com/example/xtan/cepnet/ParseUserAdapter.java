package com.example.xtan.cepnet;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.parse.ParseUser;

import java.util.List;

/**
 * Created by xtan on 5/25/2015.
 */
public class ParseUserAdapter extends ArrayAdapter<ParseUser> {
    private Context mContext;
    private List<ParseUser> mParseUserList;
    private int mLayoutResourceId;

    public ParseUserAdapter(Context context, int layoutResourceId, List<ParseUser> parseUserList) {
        super(context, android.R.layout.simple_list_item_1, parseUserList);
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mParseUserList = parseUserList;
    }
}
