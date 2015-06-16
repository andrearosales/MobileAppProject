package com.example.arosales.mobileappproject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.parse.Parse;
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
    public static final String NAMERECEIVER = "com.example.arosales.mobileappproject.NAMERECEIVER";

    private String receiverId;
    private String receiverType;
    private String receiverIdtoDB;
    private List<Student> selected;
    private ArrayList<Student> receivers;
    private TextView receiverText;

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
        //message.put("SenderId", senderId);
        message.put("SenderId", ParseUser.getCurrentUser());
        ArrayList<String> receiversIds = new ArrayList<>();
        if(receiverType.equals("Company") || receiverType.equals("Student") || receiverType.equals("Reply")) {
            //message.put("ReceiverIds", receiverIdtoDB);
            receiversIds.add(receiverIdtoDB);
        }
        else{
            for(int i=0;i<selected.size();i++){
                receiversIds.add(selected.get(i).getId());
            }
        }
        message.put("ReceiverIds", receiversIds);
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
                name.setClickable(false);
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
                    name.setClickable(false);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(receiverType.equals("Reply")){
                receiverIdtoDB = intent.getStringExtra(RECEIVER);
                TextView name = (TextView) findViewById(R.id.receiverText);
                name.setText(intent.getStringExtra(NAMERECEIVER));
                name.setClickable(false);
            }
        }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        TextView name = (TextView) findViewById(R.id.receiverText);
        EditText subjectText = (EditText) findViewById(R.id.textSubject);
        EditText messageText = (EditText) findViewById(R.id.textMessage);

        savedInstanceState.putString(NAME, name.getText().toString());
        savedInstanceState.putString(SUBJECT, subjectText.getText().toString());
        savedInstanceState.putString(MESSAGE, messageText.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        TextView name = (TextView) findViewById(R.id.receiverText);
        EditText subjectText = (EditText) findViewById(R.id.textSubject);
        EditText messageText = (EditText) findViewById(R.id.textMessage);

        name.setText(savedInstanceState.getString(NAME));
        subjectText.setText(savedInstanceState.getString(SUBJECT));
        messageText.setText(savedInstanceState.getString(MESSAGE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        ParseUser currentUser = ParseUser.getCurrentUser();
        String typeUser = currentUser.getString("TypeUser");
        if(typeUser.equals("Student")) {
            getMenuInflater().inflate(R.menu.menu_student, menu);
        }else if(typeUser.equals("Company")){
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
        }else if (id == R.id.action_notifications) {
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

    public void selectReceivers(View view) {

        receivers = new ArrayList<>();

        ParseQuery queryStudent = new ParseQuery("Student");
        queryStudent.include("StudentId");
        queryStudent.whereNotEqualTo("StudentId", ParseUser.getCurrentUser());

        try {
            List<ParseObject> results=queryStudent.find();
            for(ParseObject p:results){
                Student student = new Student();
                student.setName(p.getString("Name").substring(0, 1).toUpperCase() + p.getString("Name").substring(1).toLowerCase());
                student.setSurname(p.getString("Surname").substring(0, 1).toUpperCase() + p.getString("Surname").substring(1).toLowerCase());
                student.setId(p.getParseObject("StudentId").getObjectId());
                //receivers.add(p.getString("Name").substring(0, 1).toUpperCase() + p.getString("Name").substring(1).toLowerCase()+
                  //      " "+p.getString("Surname").substring(0,1).toUpperCase()+p.getString("Surname").substring(1).toLowerCase());
                receivers.add(student);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LayoutInflater inflater = (LayoutInflater) SendMessage.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_receivers, (ViewGroup) findViewById(R.id.popup));

        final ReceiverAdapter receiverAdapter= new ReceiverAdapter(SendMessage.this,receivers);
        ListView list_messages= (ListView) layout.findViewById(R.id.listStudents);
        /*EditText filter = new EditText(SendMessage.this);
        filter.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        filter.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        filter.setHint("Filter by Name");
        filter.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size));
        filter.setTextColor(Color.BLACK);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                receivers = searchStudents(s);
                receiverAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        list_messages.addHeaderView(filter);*/

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
                receiverText = (TextView) findViewById(R.id.receiverText);
                selected = receiverAdapter.selected;
                if(selected.size()>0){
                    receiverText.setText("");
                    Boolean first = true;
                    for(int i=0;i<selected.size();i++){
                        if(first) {
                            receiverText.setText(selected.get(i).getName().substring(0, 1).toUpperCase() + selected.get(i).getName().substring(1).toLowerCase()+
                                    " "+selected.get(i).getSurname().substring(0,1).toUpperCase()+selected.get(i).getSurname().substring(1).toLowerCase());
                            first = false;
                        }
                        else
                            receiverText.setText(receiverText.getText()+", "+selected.get(i).getName().substring(0, 1).toUpperCase() + selected.get(i).getName().substring(1).toLowerCase()+
                                    " "+selected.get(i).getSurname().substring(0,1).toUpperCase()+selected.get(i).getSurname().substring(1).toLowerCase());
                    }
                }
                else {
                    receiverText.setText(getResources().getString(R.string.select_receiver));
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public ArrayList<String> searchStudents(CharSequence filter){
        ArrayList<String> receivers = new ArrayList<>();

        ParseQuery<ParseObject> queryStudentName = new ParseQuery("Student");
        //queryStudentName.include("StudentId");
        //queryStudentName.whereNotEqualTo("StudentId", ParseUser.getCurrentUser());
        queryStudentName.whereContains("Name", filter.toString());

        ParseQuery<ParseObject> queryStudentSurname = new ParseQuery("Student");
        //queryStudentSurname.include("StudentId");
        //queryStudentSurname.whereNotEqualTo("StudentId", ParseUser.getCurrentUser());
        queryStudentSurname.whereContains("Surname", filter.toString());

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(queryStudentName);
        queries.add(queryStudentSurname);

        ParseQuery<ParseObject> superQuery = new ParseQuery("Student");
        superQuery.include("StudentId");
        superQuery.whereNotEqualTo("StudentId", ParseUser.getCurrentUser());
        ParseQuery.or(queries);

        try {
            List<ParseObject> results=superQuery.find();
            for(ParseObject p:results){
                receivers.add(p.getString("Name").substring(0, 1).toUpperCase() + p.getString("Name").substring(1).toLowerCase()+
                        " "+p.getString("Surname").substring(0,1).toUpperCase()+p.getString("Surname").substring(1).toLowerCase());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return receivers;
    }
}
