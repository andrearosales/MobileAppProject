package com.example.arosales.mobileappproject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class InboxDescription extends AppCompatActivity {

    public final static String INFOLOCAL_MESSAGE = "com.example.arosales.mobileappproject.MESSAGE";
    public final static String INFOLOCAL_SUBJECT = "com.example.arosales.mobileappproject.SUBJECT";
    public static final String RECEIVER = "com.example.arosales.mobileappproject.RECEIVER";
    public static final String RECEIVERTYPE = "com.example.arosales.mobileappproject.RECEIVERTYPE";
    public static final String NAMERECEIVER = "com.example.arosales.mobileappproject.NAMERECEIVER";

    private String fromId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_description);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String subject = (String) b.get(Inbox.INFO_SUBJECT);
        String message = (String) b.get(Inbox.INFO_MESSAGE);
        String fromType = (String) b.get(Inbox.INFO_FROM_TYPE);
        fromId = (String) b.get(Inbox.INFO_FROM);

        TextView txt_subject= (TextView) findViewById(R.id.textSubject);
        TextView txt_message= (TextView) findViewById(R.id.messageText);
        TextView txt_from= (TextView) findViewById(R.id.textFrom);

        txt_subject.setText(subject);
        txt_message.setText(message);

        if(fromType.equals("Company")){
            try {
                ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
                ParseUser company = queryUser.get(fromId);
                txt_from.setText(company.getUsername());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
                ParseUser student = queryUser.get(fromId);
                ParseQuery<ParseObject> queryStudent = ParseQuery.getQuery("Student");
                queryStudent.include("StudentId");
                queryStudent.whereEqualTo("StudentId", student);
                ParseObject result = queryStudent.getFirst();
                txt_from.setText(getResources().getString(R.string.from)+": "+result.getString("Name").substring(0, 1).toUpperCase() + result.getString("Name").substring(1).toLowerCase()+
                        " "+result.getString("Surname").substring(0,1).toUpperCase()+result.getString("Surname").substring(1).toLowerCase());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        TextView txt_subject= (TextView) findViewById(R.id.textSubject);
        TextView txt_message= (TextView) findViewById(R.id.messageText);

        outState.putString(INFOLOCAL_MESSAGE, txt_message.getText().toString());
        outState.putString(INFOLOCAL_SUBJECT,txt_subject.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        TextView txt_subject= (TextView) findViewById(R.id.textSubject);
        TextView txt_message= (TextView) findViewById(R.id.messageText);

        txt_subject.setText(savedInstanceState.getString(INFOLOCAL_SUBJECT));
        txt_message.setText(savedInstanceState.getString(INFOLOCAL_MESSAGE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ParseUser currentUser = ParseUser.getCurrentUser();
        String typeUser = currentUser.getString("TypeUser");
        if (typeUser.equals("Student")) {
            getMenuInflater().inflate(R.menu.menu_student, menu);
        } else if(typeUser.equals("Company")){
            getMenuInflater().inflate(R.menu.menu_global, menu);
        }
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
        } else if (id == R.id.action_notifications) {
            Intent intent = new Intent(this, ViewNotifications.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_logout) {
            Utils.unsubscribeCourses();
            ParseUser.logOut();
            Intent intent= new Intent(this,LogIn.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void backToResults(View view) {
        Intent intent= new Intent(this,Inbox.class);
        startActivity(intent);
    }

    public void reply(View view) {
        TextView txt_from= (TextView) findViewById(R.id.textFrom);
        Intent intent = new Intent(this, SendMessage.class);
        intent.putExtra(RECEIVER, fromId);
        intent.putExtra(RECEIVERTYPE, "Reply");
        intent.putExtra(NAMERECEIVER, txt_from.getText().toString());
        startActivity(intent);
    }
}
