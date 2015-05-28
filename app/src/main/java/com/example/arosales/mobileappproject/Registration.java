package com.example.arosales.mobileappproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;


public class Registration extends AppCompatActivity {

    public final static String INFO_STUDENTNAME = "com.example.ricardogarcia.politojobs.STUDENTNAME";
    public final static String INFO_STUDENTPASSWORD = "com.example.ricardogarcia.politojobs.STUDENTPASSWORD";
    public final static String INFO_STUDENTSURNAME = "com.example.ricardogarcia.politojobs.STUDENTSURNAME";
    public final static String INFO_COMPANYNAME = "com.example.ricardogarcia.politojobs.COMPANYNAME";
    public final static String INFO_COMPANYPASSWORD = "com.example.ricardogarcia.politojobs.COMPANYPASSWORD";
    public final static String INFO_COMPANYADDRESS = "com.example.ricardogarcia.politojobs.COMPANYADDRESS";
    public final static String INFO_COMPANYLOCATION = "com.example.ricardogarcia.politojobs.COMPANYLOCATION";

    public final static String CURRENTAB = "com.example.ricardogarcia.politojobs.CURRENTAB";

    private EditText studentName;
    private EditText studentSurname;
    private EditText studentEmail;
    private ArrayAdapter<String> adapterStudentCourse;
    private Spinner studentCourse;
    private EditText studentPassword;

    private EditText companyName;
    private EditText companyPassword;
    private EditText address;
    private Spinner location;
    private ArrayAdapter<String> adapterLocation;

    private EditText teacherName;
    private EditText teacherSurname;
    private ArrayAdapter<String> adapterTeacherCourse;
    private Spinner teacherCourse;
    private EditText teacherPassword;

    private TabHost tabHost;
    private String defaultTab = null;
    private int defaultTabIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Spinner courseStudent = (Spinner) findViewById(R.id.spinnerCourse);
        final ArrayList<String> courses = new ArrayList<>();
        courses.add("-");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Course");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> courseList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i <courseList.size(); i++) {
                        ParseObject course = courseList.get(i);
                        courses.add(course.getString("Name"));
                    }
                }
            }
        });
        adapterStudentCourse = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courses);
        adapterStudentCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseStudent.setAdapter(adapterStudentCourse);

        Spinner location = (Spinner) findViewById(R.id.spinnerLocation);
        adapterLocation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayLocation));
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(adapterLocation);

        Spinner courseTeacher = (Spinner) findViewById(R.id.spinnerCourseTeacher);
        final ArrayList<String> coursesTeacher = new ArrayList<>();
        coursesTeacher.add("-");
        ParseQuery<ParseObject> queryTeacher = ParseQuery.getQuery("Course");
        queryTeacher.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> courseList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i <courseList.size(); i++) {
                        ParseObject course = courseList.get(i);
                        coursesTeacher.add(course.getString("Name"));
                    }
                }
            }
        });
        adapterTeacherCourse = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, coursesTeacher);
        adapterTeacherCourse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseTeacher.setAdapter(adapterStudentCourse);

        //final TabHost tabHost= (TabHost) findViewById(R.id.mainTabbHost);
        tabHost= (TabHost) findViewById(R.id.mainTabbHost);
        tabHost.setup();

        createTabs(tabHost);
        init();
    }

    private void init()
    {
        studentName = (EditText) findViewById(R.id.studentNameTxt);
        studentSurname = (EditText) findViewById(R.id.studentSrnameTxt);
        studentEmail = (EditText) findViewById(R.id.studentEmailTxt);
        studentCourse = (Spinner) findViewById(R.id.spinnerCourse);
        studentPassword = (EditText) findViewById(R.id.studentPasswordTxt);

        companyName = (EditText) findViewById(R.id.companyNameTxt);
        address = (EditText) findViewById(R.id.companyAddressTxt);
        companyPassword = (EditText) findViewById(R.id.companyPasswordTxt);
        location=(Spinner) findViewById(R.id.spinnerLocation);

        teacherName = (EditText) findViewById(R.id.TeacherNameTxt);
        teacherSurname = (EditText) findViewById(R.id.TeacherSrnameTxt);
        teacherCourse = (Spinner) findViewById(R.id.spinnerCourseTeacher);
        teacherPassword = (EditText) findViewById(R.id.teacherPasswordTxt);
    }

    public void onRegisterCompanyClick(View v)
    {
        String []data=  new String[2];
        // Check for input data validation error, display the error
        //false used as a flag to say company
        if (validateRegisterInput("Company")) {
            data[0] = location.getSelectedItem().toString();
            data[1] = address.getText().toString();
            registerUser("Company",companyName.getText().toString(),companyPassword.getText().toString(),data);
        }
    }

    public void onRegisterTeacherClick(View v)
    {
        String []data=  new String[3];
        // Check for input data validation error, display the error
        //false used as a flag to say company
        if (validateRegisterInput("Teacher")) {
            data[0] = teacherName.getText().toString();
            data[1] = teacherSurname.getText().toString();
            data[2] = teacherCourse.getSelectedItem().toString();
            String username=(teacherName.getText().toString().toLowerCase()+teacherSurname.getText().toString().toLowerCase()).replaceAll("\\s+","");
            registerUser("Teacher", username, teacherPassword.getText().toString(), data);
        }
    }

    public void onRegisterStudentClick(View v)
    {
        String []data=  new String[4];
        // Check for input data validation error, display the error
        //true used as a flag to say student
        if (validateRegisterInput("Student")) {
            data[0] = studentName.getText().toString();
            data[1] = studentSurname.getText().toString();
            data[2] = studentEmail.getText().toString();
            data[3] = studentCourse.getSelectedItem().toString();
            String username=(studentName.getText().toString().toLowerCase()+studentSurname.getText().toString().toLowerCase()).replaceAll("\\s+","");
            registerUser("Student", username, studentPassword.getText().toString(), data);
        }
    }

    //use type value = true for student and false for company data
    private boolean validateRegisterInput(String type)
    {
        // Validate the sign up data
        boolean validationError = false;
        StringBuilder validationErrorMessage =
                new StringBuilder(getResources().getString(R.string.error_intro)+"\n");
        if(type.equals("Student")) {
            if (isEmpty(studentName)) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_blank_name)+"\n");
            }
            if (isEmpty(studentSurname)) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_blank_surname)+"\n");
            }
            if (isEmpty(studentEmail)) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_blank_email)+"\n");
            }
            if (studentCourse.getSelectedItem().toString().equals("-")) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_blank_course)+"\n");
            }
            if (isEmpty(studentPassword)) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_blank_password)+"\n");
            }
        }
        else if(type.equals("Company")) {
            if (isEmpty(companyName)) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_blank_name) + "\n");
            }
            if (isEmpty(address)) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_blank_address) + "\n");
            }
            if (location.getSelectedItem().toString().equals("-")) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_blank_location) + "\n");
            }
            if (isEmpty(companyPassword)) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_blank_password) + "\n");
            }
        }
        else if(type.equals("Teacher")){
            if (isEmpty(teacherName)) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_blank_name)+"\n");
            }
            if (isEmpty(teacherSurname)) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_blank_surname)+"\n");
            }
            if (teacherCourse.getSelectedItem().toString().equals("-")) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_blank_course)+"\n");
            }
            if (isEmpty(teacherPassword)) {
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_blank_password)+"\n");
            }
        }

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(getBaseContext(), validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
            return false;//invalid input
        }
        return true;//valid input
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createTabs(final TabHost tabHost)
    {
        String []tabNames = getResources().getStringArray(R.array.tabNames);
        TabHost.TabSpec spec1 = tabHost.newTabSpec("tab1");
        spec1.setContent(R.id.scroller1);
        spec1.setIndicator(tabNames[0]);//"Mon", null);//res.getDrawable(R..drawable.tab_icon);
        tabHost.addTab(spec1);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("tab2");
        spec2.setContent(R.id.scroller2);
        spec2.setIndicator(tabNames[1]);//"Mon", null);//res.getDrawable(R..drawable.tab_icon);
        tabHost.addTab(spec2);

        TabHost.TabSpec spec3 = tabHost.newTabSpec("tab3");
        spec3.setContent(R.id.scroller3);
        spec3.setIndicator(tabNames[2], null);
        tabHost.addTab(spec3);

        tabHost.setCurrentTabByTag("tab1");

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String arg0) {
                Log.i("***Selected Tab", "Im currently in tab with index::" + tabHost.getCurrentTab());
            }
        });
        //populateListView(tabHost,"");
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    protected void registerUser(final String type, final String username,String password,String[] data) {
        ParseUser user = new ParseUser();
        final ParseObject registerStudent = new ParseObject("Student");
        final ParseObject registerCompany = new ParseObject("Company");
        final ParseObject registerTeacher = new ParseObject("Teacher");

        user.setUsername(username);
        user.setPassword(password);
        // String id =  getObjectID(username,password);
        if(type.equals("Student"))
        {
            registerStudent.put("Name",data[0].toLowerCase());//data[0] name of student
            registerStudent.put("Surname", data[1].toLowerCase());//data[1] surname of student
            registerStudent.put("Email", data[2]);//data[2] email of student
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Course");
            query.whereEqualTo("Name", data[3]);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (object != null) {
                        registerStudent.put("Course", object);
                    }
                }
            });
            user.put("TypeUser", "Student");
        } else if (type.equals("Company")) {
            registerCompany.put("Name",username.toLowerCase());
            registerCompany.put("Location",data[0]);//data[0] is Company Location
            registerCompany.put("Address", data[1]);//data[1] is address of company
            user.put("TypeUser","Company");
        }
        else {
            registerTeacher.put("Name",data[0].toLowerCase());//data[0] name of student
            registerTeacher.put("Surname", data[1].toLowerCase());//data[1] surname of student
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Course");
            query.whereEqualTo("Name",data[2]);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (object != null) {
                        registerTeacher.put("Course", object);
                    }
                }
            });
            user.put("TypeUser", "Teacher");
        }



        // Set up a progress dialog
        final ProgressDialog dlg = new ProgressDialog(Registration.this);
        dlg.setTitle("Please wait.");
        dlg.setMessage("Signing up.  Please wait.");
        dlg.show();


        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {//Sign up succeed
                    dlg.dismiss();
                    if (type.equals("Student")) {
                        //Toast.makeText(ParseApplication.this, ParseUser.getCurrentUser().toString(), Toast.LENGTH_SHORT).show();
                        registerStudent.put("StudentId", ParseUser.getCurrentUser());//act like a foreign key
                        registerStudent.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                                builder.setTitle(R.string.username);
                                builder.setMessage(getString(R.string.title_activity_username) + " " + username);
                                builder.setCancelable(true);
                                builder.setNeutralButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                Intent intent = new Intent(Registration.this, StudentHome.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();


                            }
                        });
                    } else if (type.equals("Company")) {
                        // Toast.makeText(ParseApplication.this, ParseUser.getCurrentUser().toString(), Toast.LENGTH_SHORT).show();
                        registerCompany.put("CompanyId", ParseUser.getCurrentUser());//act like a foreign key
                        registerCompany.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                                builder.setTitle(R.string.username);
                                builder.setMessage(getString(R.string.title_activity_username) + " " + username);
                                builder.setCancelable(true);
                                builder.setNeutralButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                Intent intent = new Intent(Registration.this, CompanyHome.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();


                            }
                        });
                    } else {
                        registerTeacher.put("TeacherId", ParseUser.getCurrentUser());//act like a foreign key
                        registerTeacher.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
                                builder.setTitle(R.string.username);
                                builder.setMessage(getString(R.string.title_activity_username) + " " + username);
                                builder.setCancelable(true);
                                builder.setNeutralButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                Intent intent = new Intent(Registration.this, ProfessorHome.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();


                            }
                        });
                    }
                } else {
                    // Sign up didn't succeed.
                    String errorMessage = "";
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    switch (e.getCode()) {
                        case ParseException.USERNAME_TAKEN:
                            errorMessage = "Sorry, this username has already been taken.";
                            break;
                        case ParseException.USERNAME_MISSING:
                            errorMessage = "Sorry, you must supply a username to register.";
                            break;
                        case ParseException.PASSWORD_MISSING:
                            errorMessage = "Sorry, you must supply a password to register.";
                            break;
                        default:
                            errorMessage = e.getLocalizedMessage();
                    }
                    Toast.makeText(Registration.this, errorMessage, Toast.LENGTH_LONG).show();
                    dlg.dismiss();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        String currentTabTag = tabHost.getCurrentTabTag();
        if (currentTabTag != null) {
            savedInstanceState.putString(CURRENTAB, currentTabTag);
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ensureTabHost();
        String cur = savedInstanceState.getString(CURRENTAB);
        if (cur != null) {
            tabHost.setCurrentTabByTag(cur);
        }
        if (tabHost.getCurrentTab() < 0) {
            if (defaultTab != null) {
                tabHost.setCurrentTabByTag(defaultTab);
            } else if (defaultTabIndex >= 0) {
                tabHost.setCurrentTab(defaultTabIndex);
            }
        }
    }

    private void ensureTabHost() {
        if (tabHost == null) {
            this.setContentView(R.layout.activity_registration);
        }
    }

}
