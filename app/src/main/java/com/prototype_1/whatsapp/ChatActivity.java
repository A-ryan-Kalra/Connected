package com.prototype_1.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    String activeUser="";
    ListView chatListView;
    ArrayAdapter arrayAdapter;
    ArrayList<String>messages=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent=getIntent();
        activeUser=intent.getStringExtra("username");
        setTitle("Chat with "+activeUser);
        Log.i("username",activeUser);

        chatListView=findViewById(R.id.chatListView);
        arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,messages);
        chatListView.setAdapter(arrayAdapter);

        ParseQuery<ParseObject>query1=new ParseQuery<ParseObject>("Message");
        query1.whereEqualTo("sender",ParseUser.getCurrentUser().getUsername());
        query1.whereEqualTo("recipient",activeUser);
        ParseQuery<ParseObject>query2=new ParseQuery<ParseObject>("Message");
        query2.whereEqualTo("sender",activeUser);
        query2.whereEqualTo("recipient",ParseUser.getCurrentUser().getUsername());

        List<ParseQuery<ParseObject>>queries=new ArrayList<>();
        queries.add(query1);
        queries.add(query2);
        ParseQuery<ParseObject>query=ParseQuery.or(queries);
        query.orderByAscending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    for(ParseObject object:objects){
                        String messageContent=object.getString("message");
                        if(!object.getString("sender").equals(ParseUser.getCurrentUser().getUsername())){
                            messageContent="> "+messageContent;
                        }
                        messages.add(messageContent);
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void sendChat(View view) {
        EditText chatEditText=findViewById(R.id.chatEditText);
        ParseObject message=new ParseObject("Message");

        String messageContent=chatEditText.getText().toString();

        message.put("sender", ParseUser
        .getCurrentUser().getUsername());
        message.put("recipient",activeUser);
        message.put("message",messageContent);
        chatEditText.setText("");
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    messages.add(messageContent);
                    arrayAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(ChatActivity.this, "Error,chats can not be sent! ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}