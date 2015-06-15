package com.example.arosales.mobileappproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;


public class ViewJob extends AppCompatActivity {

    public final static String INFO_HASH = "com.example.arosales.mobileappproject.HASH";
    public final static String INFO_SEARCHTYPE = "com.example.arosales.mobileappproject.SEARCHTYPE";
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

    private Job job;
    private String save_delete_type;

    public void applyNow(View view) {

        try {
            ParseQuery<ParseObject> queryStudent = ParseQuery.getQuery("Student");
            queryStudent.include("StudentId");
            queryStudent.include("CurrentCompany");
            queryStudent.whereEqualTo("StudentId", ParseUser.getCurrentUser());

            ParseQuery<ParseObject> queryJob = ParseQuery.getQuery("JobOffer");
            queryJob.include("CompanyId");
            queryJob.whereEqualTo("objectId", job.getId());
            ParseObject student = queryStudent.getFirst();
            ParseObject job = queryJob.getFirst();


            ParseQuery<ParseObject> queryApply = ParseQuery.getQuery("ApplyJob");
            queryApply.whereEqualTo("StudentId", student);
            queryApply.whereEqualTo("JobId", job);
            String message = null;

            if (queryApply.count() == 0) {
                ParseObject applyJob = new ParseObject("ApplyJob");
                applyJob.put("StudentId", student);
                applyJob.put("JobId", job);
                applyJob.saveInBackground();
                message = getString(R.string.addedApplyMessage);

            } else {
                message = getString(R.string.existingApplication);

            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Apply Jobs");
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setNeutralButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void saveJob(View view) {
        try {
            ParseQuery<ParseObject> queryStudent = ParseQuery.getQuery("Student");
            queryStudent.include("StudentId");
            queryStudent.include("CurrentCompany");
            queryStudent.whereEqualTo("StudentId", ParseUser.getCurrentUser());

            ParseQuery<ParseObject> queryJob = ParseQuery.getQuery("JobOffer");
            queryJob.include("CompanyId");
            queryJob.whereEqualTo("objectId", job.getId());

            ParseObject student = queryStudent.getFirst();
            ParseObject job = queryJob.getFirst();

            ParseQuery<ParseObject> querySavedJob = ParseQuery.getQuery("SavedJobOffer");
            querySavedJob.whereEqualTo("StudentId", student);
            querySavedJob.whereEqualTo("OfferId", job);
            if (save_delete_type.equals("Search")) {
                String message = null;
                if (querySavedJob.count() == 0) {
                    ParseObject saveJob = new ParseObject("SavedJobOffer");
                    saveJob.put("StudentId", student);
                    saveJob.put("OfferId", job);
                    saveJob.saveInBackground();
                    message = getString(R.string.addedSavedJobMessage);
                } else {
                    message = getString(R.string.existingSavedJobMessage);

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Save jobs");
                builder.setMessage(message);
                builder.setCancelable(true);
                builder.setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            } else {
                querySavedJob.getFirst().deleteInBackground();
                Intent intent = new Intent(this, JobSearchResults.class);

                HashMap<String,String> search_filters= new HashMap<String,String>();
                search_filters.put(INFO_SEARCHTYPE,"Saved Jobs");

                Bundle b = new Bundle();
                b.putSerializable(INFO_HASH,search_filters);
                intent.putExtras(b);
                startActivity(intent);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void goBack(View view) {
        ViewJob.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job);

        Intent intent = getIntent();
        job = (Job) intent.getSerializableExtra(JobAdapter.JOB);
        save_delete_type = (String) intent.getSerializableExtra(JobAdapter.SEARCH_TYPE);

        TextView position = (TextView) findViewById(R.id.jobPosition);
        if(job.getPosition()!=null)
            position.setText(position.getText().toString()+"\n"+ job.getPosition()+"\n");
        TextView company = (TextView) findViewById(R.id.companyName);
        if(job.getCompany()!=null)
            company.setText(company.getText().toString()+"\n"+ job.getCompany().getName()+"\n");
        TextView industry = (TextView) findViewById(R.id.companyIndustry);
        if(job.getIndustry()!=null)
            industry.setText(industry.getText().toString()+"\n"+job.getIndustry()+"\n");
        TextView date = (TextView) findViewById(R.id.datePosted);
        if(job.getDate()!=null)
            date.setText(date.getText().toString()+"\n"+job.getDate()+"\n");
        TextView description = (TextView) findViewById(R.id.jobDescription);
        if(job.getDescription()!=null)
            description.setText(description.getText().toString()+"\n"+ job.getDescription()+"\n");
        TextView salary = (TextView) findViewById(R.id.salary);
        if(job.getSalary()!=null)
            salary.setText(salary.getText().toString()+"\n"+  job.getSalary()+"\n");
        TextView location = (TextView) findViewById(R.id.jobLocation);
        if(job.getLocation()!=null)
            location.setText(location.getText().toString()+"\n"+ job.getLocation()+"\n");
        TextView type = (TextView) findViewById(R.id.jobType);
        if(job.getTypeJob()!=null)
            type.setText(type.getText().toString()+"\n"+ job.getTypeJob()+"\n");
        TextView duration = (TextView) findViewById(R.id.jobDuration);
        if(job.getDuration()>0)
            duration.setText(duration.getText().toString()+"\n"+ String.valueOf(job.getDuration())+"\n");
        TextView contract = (TextView) findViewById(R.id.typeOfContract);
        if(job.getContractType()!=null)
            contract.setText(contract.getText().toString()+"\n"+ job.getContractType()+"\n");

        Button save_delete = (Button) findViewById(R.id.saveButton);
        if(save_delete_type.equals("Search")){
            save_delete.setText(getResources().getString(R.string.save_job_button));
        }
        else {
            save_delete.setText(getResources().getString(R.string.remove_job_button));
        }


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
}

