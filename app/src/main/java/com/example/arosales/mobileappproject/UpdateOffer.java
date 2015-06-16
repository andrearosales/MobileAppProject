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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import com.parse.ParseException;
import java.util.ArrayList;
import java.util.List;



public class UpdateOffer extends AppCompatActivity {

    //public static final String APPLICATION_ID = "H9NFC1K9LmahxGcCrMOdT0qMaE0lDGT6BgbrSOAc";
    //public static final String CLIENT_KEY = "4K2VfxRGIyk69KlQJ2B8NMnD71llrlkEPLdTNh9M";
    private static final String POSITION = "com.example.arosales.mobileappproject.POSITION";
    private static final String INDUSTRY = "com.example.arosales.mobileappproject.INDUSTRY";
    private static final String DESCRIPTION = "com.example.arosales.mobileappproject.DESCRIPTION";
    private static final String SALARY = "com.example.arosales.mobileappproject.SALARY";
    private static final String LOCATION = "com.example.arosales.mobileappproject.LOCATION";
    private static final String TYPE = "com.example.arosales.mobileappproject.TYPE";
    private static final String DURATION = "com.example.arosales.mobileappproject.DURATION";
    private static final String CONTRACT = "com.example.arosales.mobileappproject.CONTRACT";
    private ArrayAdapter<String> adapterLocation;
    private ArrayAdapter<String> adapterJobType;
    private ArrayAdapter<String> adapterIndustry;
    private ArrayAdapter<String> adapterContract;
    private Job job;

    public void goBack(View view) {
        Intent intent= new Intent(UpdateOffer.this,ListJobs.class);
        startActivity(intent);
    }

    public void update(View view) {
        if(validateRegisterInput()){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("JobOffer");
            query.getInBackground(job.getId(), new GetCallback<ParseObject>() {
                public void done(ParseObject jobOffer, ParseException e) {
                    if (e == null) {
                        EditText position = (EditText) findViewById(R.id.textJob);
                        jobOffer.put("Position",position.getText().toString());
                        Spinner location = (Spinner) findViewById(R.id.spinnerLocation);
                        if(!location.getSelectedItem().toString().equals("-"))
                            jobOffer.put("Location",location.getSelectedItem().toString());
                        else
                            jobOffer.remove("Location");
                        EditText description = (EditText) findViewById(R.id.textDescription);
                        if(!description.getText().toString().equals(""))
                            jobOffer.put("Description",description.getText().toString());
                        else
                            jobOffer.remove("Description");
                        EditText salary = (EditText) findViewById(R.id.textSalary);
                        if(!salary.getText().toString().equals(""))
                            jobOffer.put("Salary", Integer.valueOf(salary.getText().toString()));
                        else
                            jobOffer.remove("Salary");
                        Spinner typeJob = (Spinner) findViewById(R.id.spinnerJobType);
                        if(!typeJob.getSelectedItem().toString().equals("-"))
                            jobOffer.put("JobType", typeJob.getSelectedItem().toString());
                        else
                            jobOffer.remove("JobType");
                        EditText duration = (EditText) findViewById(R.id.textDuration);
                        if(!duration.getText().toString().equals(""))
                            jobOffer.put("Duration", Integer.valueOf(duration.getText().toString()));
                        else
                            jobOffer.remove("Duration");
                        Spinner industry = (Spinner) findViewById(R.id.spinnerJobIndustry);
                        if(!industry.getSelectedItem().toString().equals("-"))
                            jobOffer.put("Industry", industry.getSelectedItem().toString());
                        else
                            jobOffer.remove("Industry");
                        Spinner contract = (Spinner) findViewById(R.id.spinnerTypeOfContract);
                        if(!contract.getSelectedItem().toString().equals("-"))
                            jobOffer.put("ContractType", contract.getSelectedItem().toString());
                        else
                            jobOffer.remove("ContractType");

                        jobOffer.saveInBackground();

                    }
                }
            });
            String message = "The job offer was updated successfully";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Update Job offer");
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setNeutralButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //dialog.cancel();
                            Intent intent= new Intent(UpdateOffer.this,ListJobs.class);
                            startActivity(intent);
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private boolean validateRegisterInput()    {
        // Validate the sign up data
        boolean validationError = false;
        StringBuilder validationErrorMessage =
                new StringBuilder(getResources().getString(R.string.error_intro));
        EditText position = (EditText) findViewById(R.id.textJob);
        if(position.getText().toString().equals("")) {
            validationError = true;
            validationErrorMessage.append(" "+getResources().getString(R.string.error_blank_position));
        }
        validationErrorMessage.append(getResources().getString(R.string.error_end));

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(getBaseContext(), validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
            return false;//invalid input
        }
        return true;//valid input
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_offer);
        //Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

        Intent intent = getIntent();
        job = (Job) intent.getSerializableExtra(JobCompanyAdapter.JOB);

        EditText position = (EditText) findViewById(R.id.textJob);
        if(job.getPosition()!=null)
            position.setText(job.getPosition());
        Spinner location = (Spinner) findViewById(R.id.spinnerLocation);
        adapterLocation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayLocation));
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(adapterLocation);
        if(job.getLocation()!=null)
            location.setSelection(adapterLocation.getPosition(job.getLocation()));
        EditText description = (EditText) findViewById(R.id.textDescription);
        if(job.getDescription()!=null)
            description.setText(job.getDescription());
        EditText salary = (EditText) findViewById(R.id.textSalary);
        if(job.getSalary()!=null && !job.getSalary().equals(""))
            salary.setText(job.getSalary());
        Spinner typeJob = (Spinner) findViewById(R.id.spinnerJobType);
        adapterJobType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayJobType));
        adapterJobType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeJob.setAdapter(adapterJobType);
        if(job.getTypeJob()!=null)
            typeJob.setSelection(adapterJobType.getPosition(job.getTypeJob()));
        EditText duration = (EditText) findViewById(R.id.textDuration);
        if(job.getDuration()>0)
            duration.setText(String.valueOf(job.getDuration()));
        Spinner industry = (Spinner) findViewById(R.id.spinnerJobIndustry);
        adapterIndustry = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayIndustry));
        adapterIndustry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        industry.setAdapter(adapterIndustry);
        if(job.getIndustry()!=null)
            industry.setSelection(adapterIndustry.getPosition(job.getIndustry()));
        Spinner contract = (Spinner) findViewById(R.id.spinnerTypeOfContract);
        adapterContract = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayContractType));
        adapterContract.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contract.setAdapter(adapterContract);
        if(job.getContractType()!=null)
            contract.setSelection(adapterContract.getPosition(job.getContractType()));

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        EditText position = (EditText) findViewById(R.id.textJob);
        Spinner location = (Spinner) findViewById(R.id.spinnerLocation);
        EditText description = (EditText) findViewById(R.id.textDescription);
        EditText salary = (EditText) findViewById(R.id.textSalary);
        Spinner typeJob = (Spinner) findViewById(R.id.spinnerJobType);
        EditText duration = (EditText) findViewById(R.id.textDuration);
        Spinner industry = (Spinner) findViewById(R.id.spinnerJobIndustry);
        Spinner contract = (Spinner) findViewById(R.id.spinnerTypeOfContract);

        savedInstanceState.putString(POSITION, position.getText().toString());
        if(!location.getSelectedItem().toString().equals("-")) {
            savedInstanceState.putString(LOCATION, location.getSelectedItem().toString());
        }
        savedInstanceState.putString(DESCRIPTION, description.getText().toString());
        savedInstanceState.putString(SALARY, salary.getText().toString());
        if(!typeJob.getSelectedItem().toString().equals("-")) {
            savedInstanceState.putString(TYPE, typeJob.getSelectedItem().toString());
        }
        savedInstanceState.putString(DURATION, duration.getText().toString());
        if(!industry.getSelectedItem().toString().equals("-")) {
            savedInstanceState.putString(INDUSTRY, industry.getSelectedItem().toString());
        }
        if(!contract.getSelectedItem().toString().equals("-")) {
            savedInstanceState.putString(CONTRACT, contract.getSelectedItem().toString());
        }

        super.onSaveInstanceState(savedInstanceState);
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
            typeJob.setSelection(adapterJobType.getPosition(savedInstanceState.getString(TYPE)));
        }
        duration.setText(savedInstanceState.getString(DURATION));
        if(savedInstanceState.containsKey(CONTRACT)){
            contract.setSelection(adapterContract.getPosition(savedInstanceState.getString(CONTRACT)));
        }

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
            Utils.unsubscribeCourses();
            ParseUser.logOut();
            Intent intent= new Intent(this,LogIn.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
