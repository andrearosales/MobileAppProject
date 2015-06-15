package com.example.arosales.mobileappproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class Inbox extends AppCompatActivity {

    public final static String INFO_MESSAGE = "com.example.arosales.mobileappproject.MESSAGE";
    public final static String INFO_SUBJECT = "com.example.arosales.mobileappproject.SUBJECT";
    public final static String INFO_FROM = "com.example.arosales.mobileappproject.FROM";
    public final static String INFO_FROM_TYPE = "com.example.arosales.mobileappproject.FROMTYPE";
    public final static String LIST_MESSAGES = "com.example.arosales.mobileappproject.LIST";
    public static final String RECEIVERTYPE = "com.example.arosales.mobileappproject.RECEIVERTYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        ParseUser currentUser= ParseUser.getCurrentUser();
        new RetrieveFromDatabase().execute(currentUser.getObjectId());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            String typeUser = currentUser.getString("TypeUser");
            if(typeUser.equals("Student")){
                Intent intent = new Intent(this, StudentHome.class);
                startActivity(intent);
            }
            else if (typeUser.equals("Company")){
                Intent intent = new Intent(this, CompanyHome.class);
                startActivity(intent);
            }
            else if (typeUser.equals("Teacher")){
                Intent intent = new Intent(this, ProfessorHome.class);
                startActivity(intent);
            }
            return true;
        }
        else if (id == R.id.action_profile) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            String typeUser = currentUser.getString("TypeUser");
            if(typeUser.equals("Student")){
                Intent intent = new Intent(this, ProfileStudent.class);
                startActivity(intent);
            }
            else if (typeUser.equals("Company")){
                Intent intent = new Intent(this, ProfileCompany.class);
                startActivity(intent);
            }else if (typeUser.equals("Teacher")){
                Intent intent = new Intent(this, ProfileProfessor.class);
                startActivity(intent);
            }
            return true;
        }
        else if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent= new Intent(this,LogIn.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void newMessage(View view) {
        Intent intent= new Intent(this,SendMessage.class);
        intent.putExtra(RECEIVERTYPE, "StudentSocial");
        startActivity(intent);
    }

    private class RetrieveFromDatabase extends AsyncTask<String,Void,ArrayList<Message>>{

        private ProgressDialog progressDialog= new ProgressDialog(Inbox.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Loading messages");
            if(!progressDialog.isShowing()){
                progressDialog.show();
            }
        }

        @Override
        protected ArrayList<Message> doInBackground(String... params) {
            ArrayList<Message> result_messages=new ArrayList<Message>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
            query.include("SenderId");
            query.whereEqualTo("ReceiverIds",params[0]);

            try {
                List<ParseObject> results=query.find();
                for(ParseObject p:results){
                    Message msg= new Message();

                    msg.setFrom(p.getParseUser("SenderId"));

                    if(!p.get("Subject").equals(""))
                        msg.setSubject((String) p.get("Subject"));

                    if(!p.get("Message").equals(""))
                        msg.setMessage((String) p.get("Message"));

                    result_messages.add(msg);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return result_messages;
        }

        @Override
        protected void onPostExecute(ArrayList<Message> messages) {
            super.onPostExecute(messages);
            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            MessageAdapter mAdapter= new MessageAdapter(Inbox.this,messages);

            ListView list_messages= (ListView) findViewById(R.id.listMessages);

            if(!ParseUser.getCurrentUser().getString("TypeUser").equals("Company")) {
                final Button sendMessage = new Button(Inbox.this);
                Drawable background = getResources().getDrawable(R.drawable.background_color);

                if (android.os.Build.VERSION.SDK_INT >= 16)
                    sendMessage.setBackground(background);
                else
                    sendMessage.setBackgroundDrawable(background);

                sendMessage.setHeight(getResources().getDimensionPixelSize(R.dimen.button_height));
                sendMessage.setWidth(getResources().getDimensionPixelSize(R.dimen.width_buttons));
                sendMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size));
                sendMessage.setTextColor(Color.WHITE);
                sendMessage.setTypeface(null, Typeface.BOLD);
                sendMessage.setText(getResources().getString(R.string.send_button));

                sendMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newMessage(sendMessage);
                    }
                });

                list_messages.addFooterView(sendMessage);
            }

            list_messages.setAdapter(mAdapter);
            list_messages.setEmptyView(findViewById(R.id.emptyView));
            if(ParseUser.getCurrentUser().getString("TypeUser").equals("Company")) {
                Button newEmptyButton = (Button) findViewById(R.id.sendNewMessage);
                newEmptyButton.setVisibility(View.GONE);
            }

        }
    }

}
