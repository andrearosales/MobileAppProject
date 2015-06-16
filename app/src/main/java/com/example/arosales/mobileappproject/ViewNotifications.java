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


public class ViewNotifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications);

        new RetrieveFromDatabase().execute();
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


    private class RetrieveFromDatabase extends AsyncTask<Void, Void, ArrayList<Notification>> {

        private ProgressDialog progressDialog = new ProgressDialog(ViewNotifications.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Loading notifications");
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        @Override
        protected ArrayList<Notification> doInBackground(Void... voids) {


            ArrayList<Notification> notifications = new ArrayList<Notification>();

            ParseQuery<ParseObject> studentQuery = ParseQuery.getQuery("Student");
            studentQuery.include("StudentId");
            studentQuery.include("CurrentStudyCourse");
            studentQuery.whereEqualTo("StudentId", ParseUser.getCurrentUser());


            try {
                ParseObject currentStudent = studentQuery.getFirst();
                if (currentStudent != null) {
                    ParseObject currentStudyCourse = currentStudent.getParseObject("CurrentStudyCourse");
                    if (currentStudyCourse != null) {
                        if (currentStudyCourse.get("Courses") != null) {
                            ParseQuery<ParseObject> courseQuery = ParseQuery.getQuery("Course");
                            ArrayList<String> courses = (ArrayList<String>) currentStudyCourse.get("Courses");
                            if (courses.size() > 0) {
                                courseQuery.whereContainedIn("objectId", courses);
                                List<ParseObject> results_courses = courseQuery.find();

                                ParseQuery<ParseObject> notificationQuery = ParseQuery.getQuery("Notification");
                                notificationQuery.include("CourseId");
                                notificationQuery.include("CourseId.TeacherId");
                                notificationQuery.whereContainedIn("CourseId", results_courses);

                                notificationQuery.orderByDescending("createdAt");
                                List<ParseObject> results_notifications = notificationQuery.find();
                                for (ParseObject n : results_notifications) {
                                    Notification notification = new Notification();
                                    notification.setId(n.getObjectId());
                                    if (n.get("Title") != null)
                                        notification.setTitle(n.getString("Title"));
                                    if (n.get("Message") != null)
                                        notification.setMessage(n.getString("Message"));

                                    ParseObject parseCourse = n.getParseObject("CourseId");
                                    if (parseCourse != null) {
                                        Course course = new Course();
                                        course.setId(parseCourse.getObjectId());
                                        if (parseCourse.get("Name") != null)
                                            course.setName(parseCourse.get("Name").toString());
                                        if (parseCourse.get("StartDate") != null)
                                            course.setStartDate(parseCourse.getDate("StartDate"));
                                        if (parseCourse.get("EndDate") != null)
                                            course.setEndDate(parseCourse.getDate("EndDate"));

                                        ParseObject current_teacher = parseCourse.getParseObject("TeacherId");
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
                                        notification.setCourse(course);
                                    }
                                    notifications.add(notification);
                                }
                            }
                        }

                    }


                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            return notifications;
        }

        @Override
        protected void onPostExecute(ArrayList<Notification> notifications) {
            super.onPostExecute(notifications);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            NotificationAdapter nAdapter = new NotificationAdapter(ViewNotifications.this, notifications);
            ListView list_notifications = (ListView) findViewById(R.id.listNotifications);
            list_notifications.setAdapter(nAdapter);
            list_notifications.setEmptyView(findViewById(R.id.emptyView));
        }
    }
}
