package com.example.arosales.mobileappproject;

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
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.HashMap;


public class JobSearch extends AppCompatActivity {

    public final static String INFO_HASH = "com.example.arosales.mobileappproject.HASH";
    public final static String INFO_SEARCHTYPE = "com.example.arosales.mobileappproject.SEARCHTYPE";
    public final static String INFO_KEYWORDS = "com.example.arosales.mobileappproject.KEYWORDS";
    public final static String INFO_COMPANY = "com.example.arosales.mobileappproject.COMPANY";
    public final static String INFO_LOCATION = "com.example.arosales.mobileappproject.LOCATION";
    public final static String INFO_INDUSTRY = "com.example.arosales.mobileappproject.INDUSTRY";
    public final static String INFO_JOBTYPE = "com.example.arosales.mobileappproject.JOBTYPE";
    public final static String INFO_SALARY = "com.example.arosales.mobileappproject.SALARY";
    public final static String INFO_DURATION = "com.example.arosales.mobileappproject.DURATION";
    public final static String INFO_CONTRACT_TYPE = "com.example.arosales.mobileappproject.CONTRACTTYPE";

    private ArrayAdapter<String> adapterLocation;
    private ArrayAdapter<String> adapterIndustry;
    private ArrayAdapter<String> adapterJobType;
    private ArrayAdapter<String> adapterSalary;
    private ArrayAdapter<String> adapterDuration;
    private ArrayAdapter<String> adapterContractType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);

        Spinner location = (Spinner) findViewById(R.id.spinnerLocation);
        Spinner industry = (Spinner) findViewById(R.id.spinnerIndustry);
        Spinner job_type = (Spinner) findViewById(R.id.spinnerTypeJob);
        Spinner salary = (Spinner) findViewById(R.id.spinnerSalary);
        Spinner duration = (Spinner) findViewById(R.id.spinnerDuration);
        Spinner contract_type = (Spinner) findViewById(R.id.spinnerTypeOfContract);

        adapterLocation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayLocation));
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterIndustry = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayIndustry));
        adapterIndustry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterJobType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayJobType));
        adapterJobType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSalary = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arraySalary));
        adapterSalary.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDuration = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayDuration));
        adapterDuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterContractType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayContractType));
        adapterContractType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        location.setAdapter(adapterLocation);
        industry.setAdapter(adapterIndustry);
        job_type.setAdapter(adapterJobType);
        salary.setAdapter(adapterSalary);
        duration.setAdapter(adapterDuration);
        contract_type.setAdapter(adapterContractType);

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
        }else if (id == R.id.action_notifications) {
            Intent intent = new Intent(this, ViewNotifications.class);
            startActivity(intent);
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

    public void searchJobs(View view){
        Intent intent = new Intent(this, JobSearchResults.class);

        HashMap<String,String> search_filters= new HashMap<String,String>();

        EditText keywords_filter= (EditText) findViewById(R.id.editTextKeywords);
        EditText company_filter=(EditText) findViewById(R.id.editTextCompany);
        Spinner spinnerLocation=(Spinner) findViewById(R.id.spinnerLocation);
        Spinner spinnerIndustry= (Spinner) findViewById(R.id.spinnerIndustry);
        Spinner spinnerJobType= (Spinner) findViewById(R.id.spinnerTypeJob);
        Spinner spinnerSalary= (Spinner) findViewById(R.id.spinnerSalary);
        Spinner spinnerDuration= (Spinner) findViewById(R.id.spinnerDuration);
        Spinner spinnerContractType= (Spinner) findViewById(R.id.spinnerTypeOfContract);


        search_filters.put(INFO_SEARCHTYPE,"Search");

        if(!keywords_filter.getText().toString().equals("")){
            search_filters.put(INFO_KEYWORDS, keywords_filter.getText().toString().toLowerCase());
        }

        if(!company_filter.getText().toString().equals("")){
            search_filters.put(INFO_COMPANY,company_filter.getText().toString().toLowerCase());
        }

        if(!spinnerLocation.getSelectedItem().toString().equals("-")){
            search_filters.put(INFO_LOCATION,spinnerLocation.getSelectedItem().toString());
        }

        if(!spinnerIndustry.getSelectedItem().toString().equals("-")){
            search_filters.put(INFO_INDUSTRY,spinnerIndustry.getSelectedItem().toString());
        }

        if(!spinnerJobType.getSelectedItem().toString().equals("-")){
            search_filters.put(INFO_JOBTYPE,spinnerJobType.getSelectedItem().toString());
        }

        if(!spinnerSalary.getSelectedItem().toString().equals("-")){
            search_filters.put(INFO_SALARY,spinnerSalary.getSelectedItem().toString());
        }

        if(!spinnerDuration.getSelectedItem().toString().equals("-")){
            search_filters.put(INFO_DURATION, spinnerDuration.getSelectedItem().toString());
        }

        if(!spinnerContractType.getSelectedItem().toString().equals("-")){
            search_filters.put(INFO_CONTRACT_TYPE, spinnerContractType.getSelectedItem().toString());
        }

        Bundle b = new Bundle();
        b.putSerializable(INFO_HASH,search_filters);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void goSavedJobs(View view){
        Intent intent = new Intent(this, JobSearchResults.class);

        HashMap<String,String> search_filters= new HashMap<String,String>();
        search_filters.put(INFO_SEARCHTYPE,"Saved Jobs");

        Bundle b = new Bundle();
        b.putSerializable(INFO_HASH,search_filters);
        intent.putExtras(b);
        startActivity(intent);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        EditText keywords= (EditText) findViewById(R.id.editTextKeywords);
        EditText company=(EditText) findViewById(R.id.editTextCompany);
        Spinner spinnerLocation=(Spinner) findViewById(R.id.spinnerLocation);
        Spinner spinnerIndustry= (Spinner) findViewById(R.id.spinnerIndustry);
        Spinner spinnerJobType= (Spinner) findViewById(R.id.spinnerTypeJob);
        Spinner spinnerSalary= (Spinner) findViewById(R.id.spinnerSalary);
        Spinner spinnerDuration= (Spinner) findViewById(R.id.spinnerDuration);
        Spinner spinnerContractType= (Spinner) findViewById(R.id.spinnerTypeOfContract);

        outState.putString(INFO_KEYWORDS,keywords.getText().toString());
        outState.putString(INFO_COMPANY,company.getText().toString());
        outState.putString(INFO_LOCATION,spinnerLocation.getSelectedItem().toString());
        outState.putString(INFO_INDUSTRY,spinnerIndustry.getSelectedItem().toString());
        outState.putString(INFO_JOBTYPE,spinnerJobType.getSelectedItem().toString());
        outState.putString(INFO_SALARY,spinnerSalary.getSelectedItem().toString());
        outState.putString(INFO_DURATION,spinnerDuration.getSelectedItem().toString());
        outState.putString(INFO_CONTRACT_TYPE,spinnerContractType.getSelectedItem().toString());


        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        EditText keywords= (EditText) findViewById(R.id.editTextKeywords);
        EditText company=(EditText) findViewById(R.id.editTextCompany);
        Spinner spinnerLocation=(Spinner) findViewById(R.id.spinnerLocation);
        Spinner spinnerIndustry= (Spinner) findViewById(R.id.spinnerIndustry);
        Spinner spinnerJobType= (Spinner) findViewById(R.id.spinnerTypeJob);
        Spinner spinnerSalary= (Spinner) findViewById(R.id.spinnerSalary);
        Spinner spinnerDuration= (Spinner) findViewById(R.id.spinnerDuration);
        Spinner spinnerContractType= (Spinner) findViewById(R.id.spinnerTypeOfContract);

        keywords.setText(savedInstanceState.getString(INFO_KEYWORDS));
        company.setText(savedInstanceState.getString(INFO_COMPANY));

        spinnerLocation.setSelection(adapterLocation.getPosition(savedInstanceState.getString(INFO_LOCATION)));
        spinnerIndustry.setSelection(adapterIndustry.getPosition(savedInstanceState.getString(INFO_INDUSTRY)));
        spinnerJobType.setSelection(adapterJobType.getPosition(savedInstanceState.getString(INFO_JOBTYPE)));
        spinnerSalary.setSelection(adapterSalary.getPosition(savedInstanceState.getString(INFO_SALARY)));
        spinnerDuration.setSelection(adapterDuration.getPosition(savedInstanceState.getString(INFO_DURATION)));
        spinnerContractType.setSelection(adapterContractType.getPosition(savedInstanceState.getString(INFO_CONTRACT_TYPE)));


    }
}
