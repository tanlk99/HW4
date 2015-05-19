package com.example.xtan.cepnet;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SignupActivity extends ActionBarActivity {
    private EditText mUsernameText;
    private EditText mPasswordText;
    private EditText mEmailText;
    private Button mSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mUsernameText = (EditText)findViewById(R.id.signup_username);
        mPasswordText = (EditText)findViewById(R.id.signup_password);
        mEmailText = (EditText)findViewById(R.id.signup_email);
        mSignupButton = (Button)findViewById(R.id.signup_button);

        mPasswordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused) ((EditText)view).setTransformationMethod(null);
                else ((EditText)view).setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
