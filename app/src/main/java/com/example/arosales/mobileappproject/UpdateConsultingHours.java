package com.example.arosales.mobileappproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class UpdateConsultingHours extends AppCompatActivity {

    public static final String COURSENAME = "com.example.arosales.mobileappproject.COURSENAME";
    public static final String TIME = "com.example.arosales.mobileappproject.TIME";
    public static final String PLACE = "com.example.arosales.mobileappproject.PLACE";

    private ParseObject consultingHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_consulting_hours);

        EditText textCourse = (EditText) findViewById(R.id.textCourse);
        EditText textTime = (EditText) findViewById(R.id.textTime);
        EditText textPlace = (EditText) findViewById(R.id.placeText);

        textCourse.setFocusable(false);
        textCourse.setFocusableInTouchMode(false);
        textCourse.setClickable(false);

        Intent intent = getIntent();
        String consultingHourId = (String) intent.getSerializableExtra(ConsultingHourAdapter.CONSULTING_HOUR);

        ParseQuery<ParseObject> consultingHourQuery = ParseQuery.getQuery("ConsultingInformation");
        consultingHourQuery.include("CourseId");
        try {
            consultingHour = consultingHourQuery.get(consultingHourId);
            if (consultingHour.get("Time") != null)
                textTime.setText(consultingHour.getString("Time"));
            if (consultingHour.get("Place") != null)
                textPlace.setText(consultingHour.getString("Place"));
            if (consultingHour.get("CourseId") != null) {
                ParseObject parsedCourse = consultingHour.getParseObject("CourseId");
                if (parsedCourse != null) {
                    textCourse.setText(parsedCourse.getString("Name"));
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        EditText textCourse = (EditText) findViewById(R.id.textCourse);
        EditText textTime = (EditText) findViewById(R.id.textTime);
        EditText textPlace = (EditText) findViewById(R.id.placeText);

        outState.putString(COURSENAME, textCourse.getText().toString());
        outState.putString(TIME, textTime.getText().toString());
        outState.putString(PLACE, textPlace.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        EditText textCourse = (EditText) findViewById(R.id.textCourse);
        EditText textTime = (EditText) findViewById(R.id.textTime);
        EditText textPlace = (EditText) findViewById(R.id.placeText);

        if (savedInstanceState.containsKey(COURSENAME)) {
            textCourse.setText(savedInstanceState.getString(COURSENAME));
        } else
            textCourse.setText("");

        if (savedInstanceState.containsKey(TIME))
            textTime.setText(savedInstanceState.getString(TIME));
        else
            textTime.setText("");

        if (savedInstanceState.containsKey(PLACE))
            textPlace.setText(savedInstanceState.getString(PLACE));
        else
            textPlace.setText("");

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

    public void updateConsultingHour(View view) {

        EditText textTime = (EditText) findViewById(R.id.textTime);
        EditText textPlace = (EditText) findViewById(R.id.placeText);

        if (!textTime.getText().toString().equals(""))
            consultingHour.put("Time", textTime.getText().toString());
        else
            consultingHour.remove("Time");

        if (!textPlace.getText().toString().equals(""))
            consultingHour.put("Place", textPlace.getText().toString());
        else
            consultingHour.remove("Place");

        consultingHour.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                String message = "The consulting hour was updated successfully";

                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateConsultingHours.this);
                builder.setTitle(R.string.updateconsultinghourstitle);
                builder.setMessage(message);
                builder.setCancelable(true);
                builder.setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //dialog.cancel();
                                Intent intent= new Intent(UpdateConsultingHours.this,ProfessorConsultingHours.class);
                                startActivity(intent);
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }

    public void goBack(View view) {
        Intent intent = new Intent(UpdateConsultingHours.this, ProfessorConsultingHours.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
