package com.prototype_1.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Boolean loginModeActive=false;
    TextView loginTextView;
    Button signUpButton;

    public  void redirect() {
    Intent intent=new Intent(this,UserListActivity.class);
    startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText=findViewById(R.id.usernameEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        loginTextView=findViewById(R.id.loginTextView);
        signUpButton=findViewById(R.id.signupButton);

        ParseInstallation.getCurrentInstallation().saveInBackground();
        if(ParseUser.getCurrentUser()!=null){
            redirect();
        }
    }

    public void singuplogin(View view) {
        if (loginModeActive) {
            ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(e==null){
                        Log.i("Info","user logged in");
                        redirect();
                    }else
                    {
                        Toast.makeText(MainActivity.this, e.getMessage().substring(e.getMessage().indexOf("")), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            ParseUser user = new ParseUser();
            user.setUsername(usernameEditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(MainActivity.this, "Successfully Signed up!", Toast.LENGTH_SHORT).show();
                        redirect();
                    } else {
                        Toast.makeText(MainActivity.this, e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void toggleLogin(View view) {
        if(loginModeActive){
            loginModeActive=false;
            loginTextView.setText(", login");
            signUpButton.setText("SignUp");

        }else{
            loginModeActive=true;
            loginTextView.setText(", Signup");
            signUpButton.setText("Login");

        }
    }
}