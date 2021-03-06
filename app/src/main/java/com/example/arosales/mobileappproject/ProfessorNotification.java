package com.example.arosales.mobileappproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfessorNotification extends AppCompatActivity {

    public static final String COURSENAME = "com.example.arosales.mobileappproject.COURSENAME";
    public static final String TITLE = "com.example.arosales.mobileappproject.TITLE";
    public static final String MESSAGE = "com.example.arosales.mobileappproject.MESSAGE";


    private ParseObject course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_notification);

        Intent intent = getIntent();
        String courseId = (String) intent.getSerializableExtra(CourseAdapter.COURSE);
        ParseQuery<ParseObject> courseQuery = ParseQuery.getQuery("Course");
        try {
            course = courseQuery.get(courseId);
            TextView course_name = (TextView) findViewById(R.id.courseName);
            if (course.get("Name") != null) {
                course_name.setText(course.getString("Name"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


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
            if (typeUser.equals("Student")) {
                Intent intent = new Intent(this, StudentHome.class);
                startActivity(intent);
            } else if (typeUser.equals("Company")) {
                Intent intent = new Intent(this, CompanyHome.class);
                startActivity(intent);
            } else if (typeUser.equals("Teacher")) {
                Intent intent = new Intent(this, ProfessorHome.class);
                startActivity(intent);
            }
            return true;
        } else if (id == R.id.action_profile) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            String typeUser = currentUser.getString("TypeUser");
            if (typeUser.equals("Student")) {
                Intent intent = new Intent(this, ProfileStudent.class);
                startActivity(intent);
            } else if (typeUser.equals("Company")) {
                Intent intent = new Intent(this, ProfileCompany.class);
                startActivity(intent);
            } else if (typeUser.equals("Teacher")) {
                Intent intent = new Intent(this, ProfileProfessor.class);
                startActivity(intent);
            }
            return true;
        } else if (id == R.id.action_logout) {
            Utils.unsubscribeCourses();
            ParseUser.logOut();
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        TextView course_name = (TextView) findViewById(R.id.courseName);
        EditText title = (EditText) findViewById(R.id.textTitle);
        EditText message = (EditText) findViewById(R.id.textMessage);

        outState.putString(COURSENAME, course_name.getText().toString());
        outState.putString(TITLE, title.getText().toString());
        outState.putString(MESSAGE, message.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        TextView course_name = (TextView) findViewById(R.id.courseName);
        EditText title = (EditText) findViewById(R.id.textTitle);
        EditText message = (EditText) findViewById(R.id.textMessage);

        course_name.setText(savedInstanceState.getString(COURSENAME));
        title.setText(savedInstanceState.getString(TITLE));
        message.setText(savedInstanceState.getString(MESSAGE));

    }

    public void goBack(View view) {
        finish();
    }

    public void sendNotification(View view) {

        final EditText title = (EditText) findViewById(R.id.textTitle);
        final EditText message = (EditText) findViewById(R.id.textMessage);
        boolean validationError = false;

        StringBuilder validationErrorMessage = new StringBuilder(getResources().getString(R.string.error_intro)+"\n");
        if (title.getText().toString().equals("")) {
            validationError = true;
            validationErrorMessage.append(getResources().getString(R.string.error_blank_title)+"\n");
        }

        if (message.getText().toString().equals("")) {
            validationError = true;
            validationErrorMessage.append(getResources().getString(R.string.error_blank_message)+"\n");
        }

        if (validationError) {
            Toast.makeText(ProfessorNotification.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dlg = new ProgressDialog(ProfessorNotification.this);
        dlg.setTitle(R.string.sendnotificationtitle);
        dlg.setMessage(getString(R.string.sendnotification));
        dlg.show();

        ParseObject new_notification = new ParseObject("Notification");
        new_notification.put("Title", title.getText().toString());
        new_notification.put("Message", message.getText().toString());
        new_notification.put("CourseId", course);


        new_notification.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                ParsePush push = new ParsePush();
                JSONObject data = new JSONObject();
                try {
                    data.put("title",title.getText().toString());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                push.setChannel(course.getString("Name").replaceAll("\\s", "").replaceAll("-",""));
                push.setData(data);
                push.setMessage(getResources().getString(R.string.new_notification)+" "+course.getString("Name"));
                push.sendInBackground();
                dlg.dismiss();
                finish();
            }
        });



    }
}
