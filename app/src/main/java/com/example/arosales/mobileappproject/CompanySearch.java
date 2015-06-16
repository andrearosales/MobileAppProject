package com.example.arosales.mobileappproject;

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

import com.parse.Parse;
import com.parse.ParseUser;

import java.util.HashMap;


public class CompanySearch extends AppCompatActivity {

    public static final String INFO_HASH = "com.example.arosales.mobileappproject.HASH";
    public static final String INFO_SEARCHTYPE = "com.example.arosales.mobileappproject.SEARCHTYPE";
    public static final String INFO_NAME = "com.example.arosales.mobileappproject.NAME";
    public static final String INFO_INDUSTRY = "com.example.arosales.mobileappproject.INDUSTRY";
    public static final String INFO_LOCATION = "com.example.arosales.mobileappproject.LOCATION";
    public static final String INFO_SIZE = "com.example.arosales.mobileappproject.SIZE";


    private ArrayAdapter<String> adapterIndustry;
    private ArrayAdapter<String> adapterLocation;
    private ArrayAdapter<String> adapterSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search);

        Spinner location = (Spinner) findViewById(R.id.spinnerLocation);
        Spinner industry = (Spinner) findViewById(R.id.spinnerIndustry);
        Spinner size = (Spinner) findViewById(R.id.spinnerCompanySize);

        adapterLocation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayLocation));
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterIndustry = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayIndustry));
        adapterIndustry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSize = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arraySize));
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        location.setAdapter(adapterLocation);
        industry.setAdapter(adapterIndustry);
        size.setAdapter(adapterSize);

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        EditText name= (EditText) findViewById(R.id.editTextName);
        Spinner spinnerLocation=(Spinner) findViewById(R.id.spinnerLocation);
        Spinner spinnerIndustry= (Spinner) findViewById(R.id.spinnerIndustry);
        Spinner spinnerSize=(Spinner) findViewById(R.id.spinnerCompanySize);

        outState.putString(INFO_NAME,name.getText().toString());
        outState.putString(INFO_LOCATION,spinnerLocation.getSelectedItem().toString());
        outState.putString(INFO_INDUSTRY,spinnerIndustry.getSelectedItem().toString());
        outState.putString(INFO_SIZE,spinnerSize.getSelectedItem().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        EditText name= (EditText) findViewById(R.id.editTextName);
        Spinner spinnerLocation=(Spinner) findViewById(R.id.spinnerLocation);
        Spinner spinnerIndustry= (Spinner) findViewById(R.id.spinnerIndustry);
        Spinner spinnerSize=(Spinner) findViewById(R.id.spinnerCompanySize);

        name.setText(savedInstanceState.getString(INFO_NAME));
        spinnerLocation.setSelection(adapterLocation.getPosition(savedInstanceState.getString(INFO_LOCATION)));
        spinnerIndustry.setSelection(adapterIndustry.getPosition(savedInstanceState.getString(INFO_INDUSTRY)));
        spinnerSize.setSelection(adapterSize.getPosition(savedInstanceState.getString(INFO_SIZE)));

    }


    public void searchCompanies(View view){
        Intent intent = new Intent(this, CompanySearchResults.class);

        HashMap<String,String> search_filters= new HashMap<String,String>();

        EditText name_filter= (EditText) findViewById(R.id.editTextName);
        Spinner spinnerLocation=(Spinner) findViewById(R.id.spinnerLocation);
        Spinner spinnerIndustry= (Spinner) findViewById(R.id.spinnerIndustry);
        Spinner spinnerSize= (Spinner) findViewById(R.id.spinnerCompanySize);


        search_filters.put(INFO_SEARCHTYPE,"Search");


        if(!name_filter.getText().toString().equals("")){
            search_filters.put(INFO_NAME, name_filter.getText().toString().toLowerCase());
        }

        if(!spinnerLocation.getSelectedItem().toString().equals("-")){
            search_filters.put(INFO_LOCATION,spinnerLocation.getSelectedItem().toString());
        }

        if(!spinnerIndustry.getSelectedItem().toString().equals("-")){
            search_filters.put(INFO_INDUSTRY,spinnerIndustry.getSelectedItem().toString());
        }

        if(!spinnerSize.getSelectedItem().toString().equals("-")){
            search_filters.put(INFO_SIZE,spinnerSize.getSelectedItem().toString());
        }

        Bundle b = new Bundle();
        b.putSerializable(INFO_HASH,search_filters);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void goSavedCompanies(View view){
        Intent intent = new Intent(this, CompanySearchResults.class);

        HashMap<String,String> search_filters= new HashMap<String,String>();
        search_filters.put(INFO_SEARCHTYPE,"Saved Companies");

        Bundle b = new Bundle();
        b.putSerializable(INFO_HASH,search_filters);
        intent.putExtras(b);
        startActivity(intent);

    }

}
