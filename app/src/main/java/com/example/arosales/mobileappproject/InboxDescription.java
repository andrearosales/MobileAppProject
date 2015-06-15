package com.example.arosales.mobileappproject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;


public class InboxDescription extends AppCompatActivity {

    public final static String INFOLOCAL_MESSAGE = "com.example.ricardogarcia.politojobs.MESSAGE";
    public final static String INFOLOCAL_SUBJECT = "com.example.ricardogarcia.politojobs.SUBJECT";
    public static final String RECEIVER = "com.example.arosales.mobileappproject.RECEIVER";
    public static final String RECEIVERTYPE = "com.example.arosales.mobileappproject.RECEIVERTYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_description);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String subject = (String) b.get(Inbox.INFO_SUBJECT);
        String message = (String) b.get(Inbox.INFO_MESSAGE);

        TextView txt_subject= (TextView) findViewById(R.id.textSubject);
        TextView txt_message= (TextView) findViewById(R.id.messageText);

        txt_subject.setText(subject);
        txt_message.setText(message);
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
        getMenuInflater().inflate(R.menu.menu_inbox_description, menu);
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

    public void backToResults(View view) {
        Intent intent= new Intent(this,Inbox.class);
        startActivity(intent);
    }

    public void reply(View view) {
        Intent intent = new Intent(this, SendMessage.class);
        //intent.putExtra(RECEIVER, student.getId());
        //intent.putExtra(RECEIVER, "?");
        if(ParseUser.getCurrentUser().getString("TypeUser").equals("Student"))
            intent.putExtra(RECEIVERTYPE, "StudentSocial");
        else if (ParseUser.getCurrentUser().getString("TypeUser").equals("Company"))
            intent.putExtra(RECEIVERTYPE, "Student");
        startActivity(intent);
    }
}
