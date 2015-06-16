package com.example.arosales.mobileappproject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;


public class ApplicationDescription extends AppCompatActivity {

    private static final String POSITION = "com.example.arosales.mobileappproject.POSITION";
    private static final String COMPANY = "com.example.arosales.mobileappproject.COMPANY";
    private static final String INDUSTRY = "com.example.arosales.mobileappproject.INDUSTRY";
    private static final String DATE = "com.example.arosales.mobileappproject.DATE";
    private static final String DESCRIPTION = "com.example.arosales.mobileappproject.DESCRIPTION";
    private static final String SALARY = "com.example.arosales.mobileappproject.SALARY";
    private static final String LOCATION = "com.example.arosales.mobileappproject.LOCATION";
    private static final String TYPE = "com.example.arosales.mobileappproject.TYPE";
    private static final String DURATION = "com.example.arosales.mobileappproject.DURATION";
    private static final String CONTRACT = "com.example.arosales.mobileappproject.CONTRACT";

    public void goBack(View view) {
        Intent intent = new Intent(this, ListApplication.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_description);

        Intent intent = getIntent();
        Job job = (Job) intent.getSerializableExtra(ApplicationAdapter.APPLICATION);
        TextView position = (TextView) findViewById(R.id.jobPosition);
        position.setText(job.getPosition());
        TextView company = (TextView) findViewById(R.id.companyName);
        company.setText(job.getCompany().getName());
        TextView industry = (TextView) findViewById(R.id.companyIndustry);
        industry.setText(job.getIndustry());
        TextView date = (TextView) findViewById(R.id.datePosted);
        date.setText(job.getDate());
        TextView description = (TextView) findViewById(R.id.jobDescription);
        description.setText(job.getDescription());
        TextView salary = (TextView) findViewById(R.id.salary);
        salary.setText(job.getSalary());
        TextView location = (TextView) findViewById(R.id.jobLocation);
        location.setText(job.getLocation());
        TextView type = (TextView) findViewById(R.id.jobType);
        type.setText(job.getTypeJob());
        TextView duration = (TextView) findViewById(R.id.jobDuration);
        duration.setText(String.valueOf(job.getDuration()));
        TextView contract = (TextView) findViewById(R.id.typeOfContract);
        contract.setText(job.getContractType());

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        TextView position = (TextView) findViewById(R.id.jobPosition);
        TextView company = (TextView) findViewById(R.id.companyName);
        TextView industry = (TextView) findViewById(R.id.companyIndustry);
        TextView date = (TextView) findViewById(R.id.datePosted);
        TextView description = (TextView) findViewById(R.id.jobDescription);
        TextView salary = (TextView) findViewById(R.id.salary);
        TextView location = (TextView) findViewById(R.id.jobLocation);
        TextView type = (TextView) findViewById(R.id.jobType);
        TextView duration = (TextView) findViewById(R.id.jobDuration);
        TextView contract = (TextView) findViewById(R.id.typeOfContract);

        savedInstanceState.putString(POSITION, position.getText().toString());
        savedInstanceState.putString(COMPANY, company.getText().toString());
        savedInstanceState.putString(INDUSTRY, industry.getText().toString());
        savedInstanceState.putString(DATE, date.getText().toString());
        savedInstanceState.putString(DESCRIPTION, description.getText().toString());
        savedInstanceState.putString(SALARY, salary.getText().toString());
        savedInstanceState.putString(LOCATION, location.getText().toString());
        savedInstanceState.putString(TYPE, type.getText().toString());
        savedInstanceState.putString(DURATION, duration.getText().toString());
        savedInstanceState.putString(CONTRACT, contract.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        TextView position = (TextView) findViewById(R.id.jobPosition);
        TextView company = (TextView) findViewById(R.id.companyName);
        TextView industry = (TextView) findViewById(R.id.companyIndustry);
        TextView date = (TextView) findViewById(R.id.datePosted);
        TextView description = (TextView) findViewById(R.id.jobDescription);
        TextView salary = (TextView) findViewById(R.id.salary);
        TextView location = (TextView) findViewById(R.id.jobLocation);
        TextView type = (TextView) findViewById(R.id.jobType);
        TextView duration = (TextView) findViewById(R.id.jobDuration);
        TextView contract = (TextView) findViewById(R.id.typeOfContract);

        position.setText(savedInstanceState.getString(POSITION));
        company.setText(savedInstanceState.getString(COMPANY));
        industry.setText(savedInstanceState.getString(INDUSTRY));
        date.setText(savedInstanceState.getString(DATE));
        description.setText(savedInstanceState.getString(DESCRIPTION));
        salary.setText(savedInstanceState.getString(SALARY));
        location.setText(savedInstanceState.getString(LOCATION));
        type.setText(savedInstanceState.getString(TYPE));
        duration.setText(savedInstanceState.getString(DURATION));
        contract.setText(savedInstanceState.getString(CONTRACT));

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
            Intent intent= new Intent(this, ViewNotifications.class);
            startActivity(intent);
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
}

