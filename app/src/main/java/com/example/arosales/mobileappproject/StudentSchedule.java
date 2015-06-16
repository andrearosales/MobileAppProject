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
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


public class StudentSchedule extends AppCompatActivity {

    private String searchType;
    private HashSet<Integer> daysHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_schedule);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        new RetrieveFromDatabase().execute((HashMap<String, String>) b.getSerializable(SearchTimetables.INFO_HASH));
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

    private class RetrieveFromDatabase extends AsyncTask<HashMap<String, String>, Void, ArrayList<ExtendedLesson>> {

        private ProgressDialog progressDialog = new ProgressDialog(StudentSchedule.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Loading courses");
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        @Override
        protected ArrayList<ExtendedLesson> doInBackground(HashMap<String, String>... params) {


            ArrayList<ExtendedLesson> coursesToShow = new ArrayList<ExtendedLesson>();
            ArrayList<Lesson> lessons = new ArrayList<Lesson>();

            HashMap<String, String> search_data = params[0];
            searchType = search_data.get(SearchTimetables.INFO_TYPE);
            daysHash = new HashSet<Integer>();

            ParseQuery<ParseObject> courseQuery = ParseQuery.getQuery("Course");

            if (search_data.get(SearchTimetables.INFO_TYPE).equals("Search")) {
                courseQuery.include("TeacherId");

                if (search_data.containsKey(SearchTimetables.INFO_COURSE)) {
                    courseQuery.whereContains("Name", search_data.get(SearchTimetables.INFO_COURSE));
                }

                if (search_data.containsKey(SearchTimetables.INFO_TEACHER)) {
                    ParseQuery<ParseObject> teacherQuery = ParseQuery.getQuery("Teacher");
                    teacherQuery.whereContains("Name", search_data.get(SearchTimetables.INFO_TEACHER));
                    try {
                        List<ParseObject> results_teachers = teacherQuery.find();
                        if (results_teachers.size() > 0)
                            courseQuery.whereContainedIn("TeacherId", results_teachers);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //My Courses
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
                                ArrayList<String> courses = (ArrayList<String>) currentStudyCourse.get("Courses");
                                if (courses.size() > 0) {
                                    courseQuery.whereContainedIn("objectId", courses);
                                }
                            }

                        }


                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }

            try {
                List<ParseObject> results_courses = courseQuery.find();

                ParseQuery<ParseObject> lessonQuery = ParseQuery.getQuery("Lesson");
                lessonQuery.include("CourseId");
                lessonQuery.include("CourseId.TeacherId");

                if (results_courses.size() > 0)
                    lessonQuery.whereContainedIn("CourseId", results_courses);

                List<ParseObject> results_lessons = lessonQuery.find();

                for (ParseObject parseLesson : results_lessons) {
                    Lesson lesson = null;
                    if (parseLesson.get("Day") != null && parseLesson.get("StartTime") != null && parseLesson.get("EndTime") != null && parseLesson.get("Room") != null) {
                        ParseObject current_course = parseLesson.getParseObject("CourseId");

                        if (current_course != null) {
                            Course course = new Course();
                            course.setId(current_course.getObjectId());
                            if (current_course.get("Name") != null)
                                course.setName(current_course.get("Name").toString());
                            if (current_course.get("StartDate") != null)
                                course.setStartDate(current_course.getDate("StartDate"));
                            if (current_course.get("EndDate") != null)
                                course.setEndDate(current_course.getDate("EndDate"));

                            ParseObject current_teacher = current_course.getParseObject("TeacherId");
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
                            DateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
                            Date date = dayFormat.parse(parseLesson.get("Day").toString());
                            Calendar c = Calendar.getInstance();
                            c.setTime(date);
                            Calendar current_day = Calendar.getInstance();
                            current_day.set(Calendar.DAY_OF_WEEK, c.get(Calendar.DAY_OF_WEEK));

                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                            String actualDate = dateFormat.format(current_day.getTime());

                            DateFormat completeDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
                            Date start = completeDateFormat.parse(actualDate + " " + parseLesson.get("StartTime"));
                            Date end = completeDateFormat.parse(actualDate + " " + parseLesson.get("EndTime"));

                            lesson = new Lesson(parseLesson.getObjectId(), course,start, end, parseLesson.get("Room").toString());
                        }
                    }
                    lessons.add(lesson);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            Collections.sort(lessons, new LessonComparator());
            
            for (Lesson l : lessons) {
                if (!daysHash.contains(l.getDay())) {
                    daysHash.add(l.getDay());
                    ExtendedLesson viDay = new ExtendedLesson(l, Type.DAY_SEPARATOR);
                    coursesToShow.add(viDay);
                }
                ExtendedLesson viLesson = new ExtendedLesson(l, Type.LESSON, Utils.retrieveColor(l.getCourse().getName()));
                coursesToShow.add(viLesson);
            }
            daysHash.clear();

            return coursesToShow;
        }

        @Override
        protected void onPostExecute(ArrayList<ExtendedLesson> lessons) {
            super.onPostExecute(lessons);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            CourseAdapter cAdapter = new CourseAdapter(StudentSchedule.this, lessons);

            ListView list_students = (ListView) findViewById(R.id.listCourses);

            Button newSearchButton = new Button(StudentSchedule.this);

            Drawable background = getResources().getDrawable(R.drawable.background_color);

            if (android.os.Build.VERSION.SDK_INT >= 16)
                newSearchButton.setBackground(background);
            else
                newSearchButton.setBackgroundDrawable(background);

            newSearchButton.setHeight(getResources().getDimensionPixelSize(R.dimen.button_height));
            newSearchButton.setWidth(getResources().getDimensionPixelSize(R.dimen.width_buttons));
            newSearchButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size));
            newSearchButton.setTextColor(Color.WHITE);
            newSearchButton.setTypeface(null, Typeface.BOLD);
            newSearchButton.setText(R.string.backsearch_button);

            newSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            list_students.addFooterView(newSearchButton);
            list_students.setAdapter(cAdapter);
            list_students.setEmptyView(findViewById(R.id.emptyView));


        }
    }

    public void goBack(View view) {
        finish();
    }

}
