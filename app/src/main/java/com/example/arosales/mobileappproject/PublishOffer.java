package com.example.arosales.mobileappproject;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class PublishOffer extends AppCompatActivity {

    private static final String POSITION = "com.example.arosales.mobileappproject.POSITION";
    private static final String INDUSTRY = "com.example.arosales.mobileappproject.INDUSTRY";
    private static final String DESCRIPTION = "com.example.arosales.mobileappproject.DESCRIPTION";
    private static final String SALARY = "com.example.arosales.mobileappproject.SALARY";
    private static final String LOCATION = "com.example.arosales.mobileappproject.LOCATION";
    private static final String TYPE = "com.example.arosales.mobileappproject.TYPE";
    private static final String DURATION = "com.example.arosales.mobileappproject.DURATION";
    private static final String CONTRACT = "com.example.arosales.mobileappproject.CONTRACT";

    private EditText JobView;
    private Spinner LocationView;
    private EditText DescriptionView;
    private EditText SalaryView;
    private Spinner TypeJobView;
    private EditText DurationView;
    private Spinner IndustryView;
    private Spinner TypeOfContractView;
    private ArrayAdapter<String> adapterLocation;
    private ArrayAdapter<String> adapterTypeJob;
    private ArrayAdapter<String> adapterIndustry;
    private ArrayAdapter<String> adapterTypeContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_offer);

        JobView = (EditText) findViewById(R.id.textJob);
        LocationView = (Spinner) findViewById(R.id.spinnerLocation);
        String[] Location = getResources().getStringArray(R.array.arrayLocation);
        adapterLocation = new ArrayAdapter<String>(PublishOffer.this, android.R.layout.simple_list_item_1, Location);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LocationView.setAdapter(adapterLocation);

        DescriptionView = (EditText) findViewById(R.id.textDescription);
        SalaryView = (EditText) findViewById(R.id.textSalary);

        TypeJobView = (Spinner) findViewById(R.id.spinnerJobType);
        String[] JobType = getResources().getStringArray(R.array.arrayJobType);
        adapterTypeJob = new ArrayAdapter<String>(PublishOffer.this, android.R.layout.simple_list_item_1, JobType);
        adapterTypeJob.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TypeJobView.setAdapter(adapterTypeJob);

        DurationView = (EditText) findViewById(R.id.textDuration);

        IndustryView = (Spinner) findViewById(R.id.spinnerJobIndustry);
        String[] Industry = getResources().getStringArray(R.array.arrayIndustry);
        adapterIndustry = new ArrayAdapter<String>(PublishOffer.this, android.R.layout.simple_list_item_1, Industry);
        adapterIndustry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        IndustryView.setAdapter(adapterIndustry);


        TypeOfContractView = (Spinner) findViewById(R.id.spinnerTypeOfContract);
        String[] TypeOfContract = getResources().getStringArray(R.array.arrayContractType);
        adapterTypeContract = new ArrayAdapter<String>(PublishOffer.this, android.R.layout.simple_list_item_1, TypeOfContract);
        adapterTypeContract.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TypeOfContractView.setAdapter(adapterTypeContract);


    }
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publish_offer, menu);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        EditText position = (EditText) findViewById(R.id.textJob);
        Spinner location = (Spinner) findViewById(R.id.spinnerLocation);
        EditText description = (EditText) findViewById(R.id.textDescription);
        EditText salary = (EditText) findViewById(R.id.textSalary);
        Spinner typeJob = (Spinner) findViewById(R.id.spinnerJobType);
        EditText duration = (EditText) findViewById(R.id.textDuration);
        Spinner industry = (Spinner) findViewById(R.id.spinnerJobIndustry);
        Spinner contract = (Spinner) findViewById(R.id.spinnerTypeOfContract);

        outState.putString(POSITION, position.getText().toString());
        if(!location.getSelectedItem().toString().equals("-")) {
            outState.putString(LOCATION, location.getSelectedItem().toString());
        }
        outState.putString(DESCRIPTION, description.getText().toString());
        outState.putString(SALARY, salary.getText().toString());
        if(!typeJob.getSelectedItem().toString().equals("-")) {
            outState.putString(TYPE, typeJob.getSelectedItem().toString());
        }
        outState.putString(DURATION, duration.getText().toString());
        if(!industry.getSelectedItem().toString().equals("-")) {
            outState.putString(INDUSTRY, industry.getSelectedItem().toString());
        }
        if(!contract.getSelectedItem().toString().equals("-")) {
            outState.putString(CONTRACT, contract.getSelectedItem().toString());
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        EditText position = (EditText) findViewById(R.id.textJob);
        Spinner location = (Spinner) findViewById(R.id.spinnerLocation);
        EditText description = (EditText) findViewById(R.id.textDescription);
        EditText salary = (EditText) findViewById(R.id.textSalary);
        Spinner typeJob = (Spinner) findViewById(R.id.spinnerJobType);
        EditText duration = (EditText) findViewById(R.id.textDuration);
        Spinner industry = (Spinner) findViewById(R.id.spinnerJobIndustry);
        Spinner contract = (Spinner) findViewById(R.id.spinnerTypeOfContract);

        position.setText(savedInstanceState.getString(POSITION));
        if(savedInstanceState.containsKey(INDUSTRY)) {
            industry.setSelection(adapterIndustry.getPosition(savedInstanceState.getString(INDUSTRY)));
        }
        description.setText(savedInstanceState.getString(DESCRIPTION));
        salary.setText(savedInstanceState.getString(SALARY));
        if(savedInstanceState.containsKey(LOCATION)) {
            location.setSelection(adapterLocation.getPosition(savedInstanceState.getString(LOCATION)));
        }
        if(savedInstanceState.containsKey(TYPE)){
            typeJob.setSelection(adapterTypeJob.getPosition(savedInstanceState.getString(TYPE)));
        }
        duration.setText(savedInstanceState.getString(DURATION));
        if(savedInstanceState.containsKey(CONTRACT)){
            contract.setSelection(adapterTypeContract.getPosition(savedInstanceState.getString(CONTRACT)));
        }
    }

    public void publishOffer(View view){

        // Validate the sign up data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getResources().getString(R.string.error_intro));
        if (JobView.getText().toString().equals("")) {
            validationError = true;
            validationErrorMessage.append(getResources().getString(R.string.error_invalid_Jobposition));
        }

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(PublishOffer.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dlg = new ProgressDialog(PublishOffer.this);
        dlg.setTitle(R.string.saveprofiletitle);
        dlg.setMessage(getString(R.string.publishoffermessage));
        dlg.show();

        //save the data to parse.com

        ParseObject jobOffer = new ParseObject("JobOffer");

        jobOffer.put("Position", JobView.getText().toString().toLowerCase());

        if(!LocationView.getSelectedItem().toString().equals("-"))
            jobOffer.put("Location", LocationView.getSelectedItem().toString());

        if(!DescriptionView.getText().toString().equals(""))
            jobOffer.put("Description", DescriptionView.getText().toString());

        if(!SalaryView.getText().toString().equals(""))
            jobOffer.put("Salary", Integer.valueOf(SalaryView.getText().toString()));

        if(!TypeJobView.getSelectedItem().toString().equals("-"))
            jobOffer.put("JobType", TypeJobView.getSelectedItem().toString());


        try {
            ParseQuery companyQuery= new ParseQuery("Company");
            companyQuery.whereEqualTo("CompanyId",ParseUser.getCurrentUser());
            ParseObject company=companyQuery.getFirst();
            jobOffer.put("CompanyId", company);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(!DurationView.getText().toString().equals(""))
            jobOffer.put("Duration", Integer.valueOf(DurationView.getText().toString()));

        if(!IndustryView.getSelectedItem().toString().equals("-"))
            jobOffer.put("Industry", IndustryView.getSelectedItem().toString());

        if(!TypeOfContractView.getSelectedItem().toString().equals("-"))
            jobOffer.put("ContractType", TypeOfContractView.getSelectedItem().toString());

        jobOffer.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                dlg.dismiss();
            }
        });

        Intent intent = new Intent(PublishOffer.this, ListJobs.class);
        startActivity(intent);
    }

    public void cancelOffer(View view){
        finish();
    }
}
