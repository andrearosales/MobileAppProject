package com.example.arosales.mobileappproject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseUser;

import java.util.HashMap;


public class SearchTimetables extends AppCompatActivity {

    public final static String INFO_COURSE = "com.example.arosales.mobileappproject.COURSE";
    public final static String INFO_TEACHER = "com.example.arosales.mobileappproject.TEACHER";
    public final static String INFO_TYPE = "com.example.arosales.mobileappproject.TYPE";
    public final static String INFO_HASH = "com.example.arosales.mobileappproject.HASH";

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        EditText course_filter = (EditText) findViewById(R.id.courseText);
        EditText teacher_filter = (EditText) findViewById(R.id.teacherText);

        outState.putString(INFO_COURSE, course_filter.getText().toString());
        outState.putString(INFO_TEACHER,teacher_filter.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        EditText course_filter = (EditText) findViewById(R.id.courseText);
        EditText teacher_filter = (EditText) findViewById(R.id.teacherText);

        course_filter.setText(course_filter.getText().toString());
        teacher_filter.setText(teacher_filter.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_timetables);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_timetables, menu);
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

    public void sendSearch(View view) {
        HashMap<String, String> filters = new HashMap<>();


        EditText course_filter = (EditText) findViewById(R.id.courseText);
        EditText teacher_filter = (EditText) findViewById(R.id.teacherText);

        filters.put(INFO_TYPE,"Search");

        if(!course_filter.getText().toString().equals(""))
        filters.put(INFO_COURSE,course_filter.getText().toString());

        if(!teacher_filter.getText().toString().equals(""))
        filters.put(INFO_TEACHER,teacher_filter.getText().toString());

        Intent intent = new Intent(this, StudentSchedule.class);
        intent.putExtra(INFO_HASH,filters);
        startActivity(intent);
    }

    public void goMyCourses(View view) {

        HashMap<String, String> filters = new HashMap<>();
        filters.put(INFO_TYPE,"MyCourses");

        Intent intent = new Intent(this, StudentSchedule.class);
        intent.putExtra(INFO_HASH,filters);
        startActivity(intent);
    }


}
