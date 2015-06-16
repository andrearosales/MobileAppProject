package com.example.arosales.mobileappproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProfileProfessor extends AppCompatActivity {

    private static final String INFO_NAME = "com.example.arosales.mobileappproject.INFO_NAME";
    private static final String INFO_SURNAME = "com.example.arosales.mobileappproject.INFO_SURNAME";
    private static final String INFO_DEPARTMENT = "com.example.arosales.mobileappproject.INFO_DEPARTMENT";
    private static final String INFO_DESCRIPTION = "com.example.arosales.mobileappproject.INFO_DESCRIPTION";
    private static final String INFO_EDIT = "com.example.arosales.mobileappproject.INFO_EDIT";

    private EditText NameView;
    private EditText SurnameView;
    private Spinner DepartmentView;
    private EditText DescriptionView;

    private ArrayAdapter<String> adapterDepartment;

    private boolean isEditable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_professor);

        NameView = (EditText) findViewById(R.id.textName);
        SurnameView = (EditText) findViewById(R.id.textSurname);
        DepartmentView = (Spinner) findViewById(R.id.spinnerDepartment);
        DescriptionView = (EditText) findViewById(R.id.descriptionText);

        adapterDepartment = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayDepartment));
        adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DepartmentView.setAdapter(adapterDepartment);

        ParseQuery query = new ParseQuery("Teacher");
        query.whereEqualTo("TeacherId", ParseUser.getCurrentUser());

        try {
            ParseObject teacher = query.getFirst();
            NameView.setText(teacher.getString("Name"));
            SurnameView.setText(teacher.getString("Surname"));
            DepartmentView.setSelection(adapterDepartment.getPosition(teacher.getString("Department")));

            if (teacher.getString("Description") != null) {
                DescriptionView.setText(teacher.getString("Description"));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        disableFields();

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

        NameView = (EditText) findViewById(R.id.textName);
        SurnameView = (EditText) findViewById(R.id.textSurname);
        DepartmentView = (Spinner) findViewById(R.id.spinnerDepartment);
        DescriptionView = (EditText) findViewById(R.id.descriptionText);

        outState.putString(INFO_NAME, NameView.getText().toString());
        outState.putString(INFO_SURNAME, SurnameView.getText().toString());

        if (!DepartmentView.getSelectedItem().toString().equals("-"))
            outState.putString(INFO_DEPARTMENT, DepartmentView.getSelectedItem().toString());

        if (!DescriptionView.getText().toString().equals(""))
            outState.putString(INFO_DESCRIPTION, DescriptionView.getText().toString());

        outState.putBoolean(INFO_EDIT, isEditable);

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        NameView.setText(savedInstanceState.getString(INFO_NAME));
        SurnameView.setText(savedInstanceState.getString(INFO_SURNAME));

        if (savedInstanceState.containsKey(INFO_DEPARTMENT))
            DepartmentView.setSelection(adapterDepartment.getPosition(savedInstanceState.getString(INFO_DEPARTMENT)));

        if (savedInstanceState.containsKey(INFO_DESCRIPTION))
            DescriptionView.setText(savedInstanceState.getString(INFO_DESCRIPTION));

        if (savedInstanceState.containsKey(INFO_EDIT)) {
            if (savedInstanceState.getBoolean(INFO_EDIT)) {
                enableFields();
            } else {
                disableFields();
                isEditable = false;
            }
        }

    }

    private void enableFields() {
        Button editButton = (Button) findViewById(R.id.editButton);
        editButton.setVisibility(View.GONE);

        DescriptionView.setFocusable(true);
        DescriptionView.setFocusableInTouchMode(true);
        DescriptionView.setClickable(true);

        isEditable = true;

    }

    private void disableFields() {
        NameView.setFocusable(false);
        NameView.setFocusableInTouchMode(false);
        NameView.setClickable(false);

        SurnameView.setFocusable(false);
        SurnameView.setFocusableInTouchMode(false);
        SurnameView.setClickable(false);

        DepartmentView.setFocusable(false);
        DepartmentView.setFocusableInTouchMode(false);
        DepartmentView.setClickable(false);
        DepartmentView.setEnabled(false);

        DescriptionView.setFocusable(false);
        DescriptionView.setFocusableInTouchMode(false);
        DescriptionView.setClickable(false);

    }

    public void editProfile(View view) {
        enableFields();
    }

    public void saveProfile(View view) {

        // Set up a progress dialog
        final ProgressDialog dlg = new ProgressDialog(ProfileProfessor.this);
        dlg.setTitle(R.string.saveprofiletitle);
        dlg.setMessage(getString(R.string.saveprofilemessage));
        dlg.show();
        //save the data to parse.com


        ParseQuery teacherQuery = new ParseQuery("Teacher");
        teacherQuery.whereEqualTo("TeacherId", ParseUser.getCurrentUser());
        try {
            ParseObject teacher = teacherQuery.getFirst();

            if(!DescriptionView.getText().toString().equals(""))
                teacher.put("Description", DescriptionView.getText().toString());
            else
                teacher.remove("Description");

            teacher.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    dlg.dismiss();
                    Intent intent = new Intent(ProfileProfessor.this, ProfileProfessor.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void deleteProfile(View view) {

        try {

            ParseQuery<ParseObject> queryTeacher = ParseQuery.getQuery("Teacher");
            queryTeacher.include("TeacherId");
            queryTeacher.whereEqualTo("TeacherId", ParseUser.getCurrentUser());

            ParseObject teacher = queryTeacher.getFirst();

            ParseQuery<ParseObject> queryCourse =ParseQuery.getQuery("Course");
            queryCourse.include("TeacherId");
            queryCourse.whereEqualTo("TeacherId",teacher);

            List<ParseObject> result_courses=queryCourse.find();
            for(ParseObject c:result_courses){
                c.remove("TeacherId");
                c.save();
            }

            teacher.delete();
            ParseUser.getCurrentUser().delete();
            //String objectId = ParseUser.getCurrentUser().getObjectId();
            ParseUser.logOut();



            startActivity(new Intent(this, ManageSession.class));


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
