package com.example.arosales.mobileappproject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SearchClassrooms extends AppCompatActivity {

    public static final String CAMPUS = "com.example.arosales.mobileappproject.CAMPUS";
    public static final String CLASSROOM = "com.example.arosales.mobileappproject.CLASSROOM";
    private ArrayAdapter<String> adapterCampus;
    private ArrayAdapter<String> adapterClassroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_classrooms);

        Spinner campus = (Spinner) findViewById(R.id.spinnerCampus);
        Spinner classroom = (Spinner) findViewById(R.id.spinnerClassroom);


        ParseQuery campusQuery = new ParseQuery("Campus");
        List<String> displayCampus = new ArrayList<String>();
        displayCampus.add("-");
        try {
            List<ParseObject> list_campus = campusQuery.find();
            for (ParseObject c : list_campus) {
                displayCampus.add(c.getString("Name"));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        adapterCampus = new ArrayAdapter<String>(SearchClassrooms.this, android.R.layout.simple_spinner_item, displayCampus);
        adapterCampus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campus.setAdapter(adapterCampus);


        classroom.setFocusable(false);
        classroom.setFocusableInTouchMode(false);
        classroom.setClickable(false);
        classroom.setEnabled(false);

        List<String> displayClassrooms = new ArrayList<String>();
        displayClassrooms.add("-");
        adapterClassroom = new ArrayAdapter<String>(SearchClassrooms.this, android.R.layout.simple_spinner_item, displayClassrooms);
        adapterClassroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classroom.setAdapter(adapterClassroom);

        campus.setOnItemSelectedListener(new FillClassroom(displayClassrooms, adapterClassroom));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Spinner campus = (Spinner) findViewById(R.id.spinnerCampus);
        Spinner classroom = (Spinner) findViewById(R.id.spinnerClassroom);

        if (!campus.getSelectedItem().toString().equals("-"))
            outState.putString(CAMPUS, campus.getSelectedItem().toString());

        if (!classroom.getSelectedItem().toString().equals("-"))
            outState.putString(CLASSROOM, classroom.getSelectedItem().toString());


        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Spinner campus = (Spinner) findViewById(R.id.spinnerCampus);
        Spinner classroom = (Spinner) findViewById(R.id.spinnerClassroom);

        if (savedInstanceState.containsKey(CAMPUS)) {
            campus.setSelection(adapterCampus.getPosition(savedInstanceState.getString(CAMPUS)));
            classroom.setFocusable(true);
            classroom.setFocusableInTouchMode(true);
            classroom.setClickable(true);
            classroom.setEnabled(true);

            if(savedInstanceState.containsKey(CLASSROOM))
            classroom.setSelection(adapterClassroom.getPosition(savedInstanceState.getString(CLASSROOM)));
        } else {
            classroom.setFocusable(false);
            classroom.setFocusableInTouchMode(false);
            classroom.setClickable(false);
            classroom.setEnabled(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student, menu);
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
        } else if (id == R.id.action_notifications) {
            Intent intent = new Intent(this, ViewNotifications.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_logout) {
            Utils.unsubscribeCourses();
            ParseUser.logOut();
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Search(View view) {

        Spinner campus = (Spinner) findViewById(R.id.spinnerCampus);
        Spinner classroom = (Spinner) findViewById(R.id.spinnerClassroom);

        if (!classroom.getSelectedItem().toString().equals("-")) {
            Intent intent = new Intent(this, MapClassrooms.class);
            intent.putExtra(CAMPUS, campus.getSelectedItem().toString());
            intent.putExtra(CLASSROOM, classroom.getSelectedItem().toString());
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.error_blank_classroom), Toast.LENGTH_LONG).show();
        }
    }

    public class FillClassroom implements AdapterView.OnItemSelectedListener {

        private ArrayAdapter<String> classroomAdapter;
        private List<String> classToShow;

        public FillClassroom(List<String> classToShow, ArrayAdapter<String> adapter) {
            this.classroomAdapter = adapter;
            this.classToShow = classToShow;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            classToShow.clear();
            classToShow.add("-");
            adapterClassroom.notifyDataSetChanged();

            Spinner classroom = (Spinner) findViewById(R.id.spinnerClassroom);
            String selectedCampus = (String) adapterView.getItemAtPosition(position);

            if(!selectedCampus.equals("-")) {

                ParseQuery classroomQuery = new ParseQuery("Classroom");
                classroomQuery.include("CampusId");

                ParseQuery campusQuery = new ParseQuery("Campus");
                campusQuery.whereEqualTo("Name", selectedCampus);

                try {
                    ParseObject parsedCampus = campusQuery.getFirst();
                    classroomQuery.whereEqualTo("CampusId", parsedCampus);
                    List<ParseObject> results_classrooms = classroomQuery.find();

                    for (ParseObject c : results_classrooms) {
                        classToShow.add(c.getString("Name"));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                adapterClassroom.notifyDataSetChanged();
                classroom.setFocusable(true);
                classroom.setFocusableInTouchMode(true);
                classroom.setClickable(true);
                classroom.setEnabled(true);
            }else{
                classroom.setFocusable(false);
                classroom.setFocusableInTouchMode(false);
                classroom.setClickable(false);
                classroom.setEnabled(false);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
