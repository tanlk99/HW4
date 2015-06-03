package com.example.xtan.cepnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

/**
 * Created by xtan on 6/3/2015.
 */
public class ProfileFragment extends Fragment {
    private ParseUser mUser;
    private EditText mProfileNameView;
    private EditText mProfileEmailView;
    private Button mSaveButton;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mProfileNameView = (EditText)rootView.findViewById(R.id.profile_name);
        mProfileEmailView = (EditText)rootView.findViewById(R.id.profile_email);
        mSaveButton = (Button)rootView.findViewById(R.id.save_button);
        mUser = ParseUser.getCurrentUser();

        if (mUser.getString("fullName") != null) mProfileNameView.setText(mUser.getString("fullName"));
        if (mUser.getString("email") != null) mProfileEmailView.setText(mUser.getEmail());

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.put("fullName", mProfileNameView.getText().toString());

                String email = mProfileEmailView.getText().toString();
                if (!email.isEmpty() && !email.contains("@")) {
                    Toast.makeText(getActivity(), "Email is Invalid", Toast.LENGTH_LONG).show();
                    mProfileEmailView.setText(mUser.getEmail());
                    return;
                }
                mUser.put("email", email);
                mUser.saveInBackground();
            }
        });

        return rootView;
    }
}
