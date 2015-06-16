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
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ProfessorConsultingHours extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_consulting_hours);

        new RetrieveFromDatabase().execute();
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

    private class RetrieveFromDatabase extends AsyncTask<Void, Void, ArrayList<ConsultingHour>> {

        private ProgressDialog progressDialog = new ProgressDialog(ProfessorConsultingHours.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Loading consulting hours");
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        @Override
        protected ArrayList<ConsultingHour> doInBackground(Void... voids) {


            ArrayList<ConsultingHour> consultinghours = new ArrayList<ConsultingHour>();

            ParseQuery<ParseObject> teacherQuery = ParseQuery.getQuery("Teacher");
            teacherQuery.include("TeacherId");
            teacherQuery.whereEqualTo("TeacherId", ParseUser.getCurrentUser());


            try {
                ParseObject currentTeacher = teacherQuery.getFirst();
                if (currentTeacher != null) {
                    ParseQuery<ParseObject> courseQuery = ParseQuery.getQuery("Course");
                    courseQuery.include("TeacherId");
                    courseQuery.whereEqualTo("TeacherId", currentTeacher);

                    List<ParseObject> results_courses = courseQuery.find();
                    ParseQuery<ParseObject> consultingInfoQuery = ParseQuery.getQuery("ConsultingInformation");
                    consultingInfoQuery.include("CourseId");
                    consultingInfoQuery.include("CourseId.TeacherId");
                    consultingInfoQuery.whereContainedIn("CourseId", results_courses);

                    List<ParseObject> result_hours = consultingInfoQuery.find();
                    for (ParseObject parsedHour : result_hours) {
                        ConsultingHour consultingHour = new ConsultingHour();
                        consultingHour.setId(parsedHour.getObjectId());
                        if (parsedHour.get("Time") != null)
                            consultingHour.setTime(parsedHour.get("Time").toString());
                        if (parsedHour.get("Place") != null)
                            consultingHour.setPlace(parsedHour.get("Place").toString());

                        ParseObject parsedCourse = parsedHour.getParseObject("CourseId");
                        if (parsedCourse != null) {
                            Course course = new Course();
                            course.setId(parsedCourse.getObjectId());
                            if (parsedCourse.get("Name") != null)
                                course.setName(parsedCourse.get("Name").toString());
                            if (parsedCourse.get("StartDate") != null)
                                course.setStartDate(parsedCourse.getDate("StartDate"));
                            if (parsedCourse.get("EndDate") != null)
                                course.setEndDate(parsedCourse.getDate("EndDate"));

                            ParseObject current_teacher = parsedCourse.getParseObject("TeacherId");
                            if (current_teacher != null) {
                                Teacher teacher = new Teacher();
                                teacher.setId(current_teacher.getObjectId());
                                if (current_teacher.get("Name") != null)
                                    teacher.setName(current_teacher.get("Name").toString());
                                if (current_teacher.get("Surname") != null)
                                    teacher.setSurname(current_teacher.get("Surname").toString());
                                if (current_teacher.get("Department") != null)
                                    teacher.setDepartment(current_teacher.get("Department").toString());
                                if (current_teacher.get("Description") != null)
                                    teacher.setDescription(current_teacher.get("Description").toString());
                                course.setTeacher(teacher);
                            }
                            consultingHour.setCourse(course);
                        }
                        consultinghours.add(consultingHour);
                    }


                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            return consultinghours;
        }

        @Override
        protected void onPostExecute(ArrayList<ConsultingHour> consultinghours) {
            super.onPostExecute(consultinghours);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            ConsultingHourAdapter cAdapter = new ConsultingHourAdapter(ProfessorConsultingHours.this, consultinghours);

            ListView list_students = (ListView) findViewById(R.id.listConsultingHours);
            list_students.setAdapter(cAdapter);
            list_students.setEmptyView(findViewById(R.id.emptyView));


        }
    }
}
