package com.example.arosales.mobileappproject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class SendMessage extends AppCompatActivity {

    private static final String NAME = "com.example.arosales.mobileappproject.COMPANYNAME";
    private static final String SUBJECT = "com.example.arosales.mobileappproject.SUBJECT";
    private static final String MESSAGE = "com.example.arosales.mobileappproject.MESSAGE";
    public static final String RECEIVER = "com.example.arosales.mobileappproject.RECEIVER";
    public static final String RECEIVERTYPE = "com.example.arosales.mobileappproject.RECEIVERTYPE";

    private String receiverId;
    private String receiverType;
    private String receiverIdtoDB;
    private List<String> selected;

    public void sendMessage(View view) {
        String senderId = ParseUser.getCurrentUser().getObjectId();
        //
        /*String senderId =null;
        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", "y7djJxGraH");
            ParseUser user = query.getFirst();
            senderId = user.getObjectId();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        //
        ParseObject message = new ParseObject("Message");
        message.put("SenderId", senderId);
        message.put("ReceiverId", receiverIdtoDB);
        EditText subjectText = (EditText) findViewById(R.id.textSubject);
        String subject = subjectText.getText().toString();
        message.put("Subject", subject);
        EditText messageText = (EditText) findViewById(R.id.textMessage);
        String mess = messageText.getText().toString();
        message.put("Message", mess);
        message.saveInBackground();

        SendMessage.this.finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Intent intent = getIntent();
        //receiverId = intent.getStringExtra(RECEIVER);
        receiverType = intent.getStringExtra(RECEIVERTYPE);
        if(receiverType.equals("Company")) {
            receiverId = intent.getStringExtra(RECEIVER);
            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Company");
                query.include("CompanyId");
                query.whereEqualTo("objectId",receiverId);
                ParseObject object = query.getFirst();
                ParseUser user = object.getParseUser("CompanyId");
                receiverIdtoDB = user.getObjectId();
                TextView name = (TextView) findViewById(R.id.receiverText);
                name.setText(object.getString("Name").toUpperCase());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            if(receiverType.equals("Student")) {
                receiverId = intent.getStringExtra(RECEIVER);
                try {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Student");
                    query.whereEqualTo("objectId", receiverId);
                    query.include("StudentId");
                    ParseObject object = query.getFirst();
                    ParseUser user = object.getParseUser("StudentId");
                    receiverIdtoDB = user.getObjectId();
                    TextView name = (TextView) findViewById(R.id.receiverText);
                    name.setText(object.getString("Name").toUpperCase() + " " + object.getString("Surname").toUpperCase());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        TextView name = (TextView) findViewById(R.id.companyName);
        EditText subjectText = (EditText) findViewById(R.id.textSubject);
        EditText messageText = (EditText) findViewById(R.id.textMessage);

        savedInstanceState.putString(NAME, name.toString());
        savedInstanceState.putString(SUBJECT, subjectText.getText().toString());
        savedInstanceState.putString(MESSAGE, messageText.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        TextView name = (TextView) findViewById(R.id.companyName);
        EditText subjectText = (EditText) findViewById(R.id.textSubject);
        EditText messageText = (EditText) findViewById(R.id.textMessage);

        name.setText(savedInstanceState.getString(NAME));
        subjectText.setText(savedInstanceState.getString(SUBJECT));
        messageText.setText(savedInstanceState.getString(MESSAGE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_message, menu);
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

    public void selectReceivers(View view) {
        ArrayList<String> receivers = new ArrayList<>();

        ParseQuery queryStudents = new ParseQuery("Student");
        queryStudents.include("StudentId");
        queryStudents.whereNotEqualTo("StudentId", ParseUser.getCurrentUser());

        try {
            List<ParseObject> results=queryStudents.find();
            for(ParseObject p:results){
                receivers.add(p.getString("Name").substring(0, 1).toUpperCase() + p.getString("Name").substring(1).toLowerCase()+
                        " "+p.getString("Surname").substring(0,1).toUpperCase()+p.getString("Surname").substring(1).toLowerCase());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LayoutInflater inflater = (LayoutInflater) SendMessage.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_receivers, (ViewGroup) findViewById(R.id.popup));

        final ReceiverAdapter receiverAdapter= new ReceiverAdapter(SendMessage.this,receivers);
        ListView list_messages= (ListView) layout.findViewById(R.id.listStudents);
        EditText filter = new EditText(SendMessage.this);
        filter.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        filter.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        filter.setHint("Filter by Name");
        filter.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size));

        list_messages.setAdapter(receiverAdapter);

        final PopupWindow popupWindow = new PopupWindow(layout, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        /*popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    popupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });*/
        Button ok_button = (Button) layout.findViewById(R.id.ok_button);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView receiverText = (TextView) findViewById(R.id.receiverText);
                selected = receiverAdapter.selected;
                if(selected.size()>0){
                    receiverText.setText("");
                    Boolean first = true;
                    for(int i=0;i<selected.size();i++){
                        if(first) {
                            receiverText.setText(selected.get(i));
                            first = false;
                        }
                        else
                            receiverText.setText(receiverText.getText()+","+selected.get(i));
                    }
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}
