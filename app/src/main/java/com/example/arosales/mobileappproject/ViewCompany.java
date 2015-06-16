package com.example.arosales.mobileappproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.HashMap;

public class ViewCompany extends AppCompatActivity {

    private static final String INFO_HASH = "com.example.arosales.mobileappproject.HASH";
    private static final String INFO_SEARCHTYPE = "com.example.arosales.mobileappproject.SEARCHTYPE";
    private static final String NAME = "com.example.arosales.mobileappproject.COMPANYNAME";
    private static final String INDUSTRY = "com.example.arosales.mobileappproject.COMPANYINDUSTRY";
    private static final String DESCRIPTION = "com.example.arosales.mobileappproject.COMPANYDESCRIPTION";
    private static final String LOCATION = "com.example.arosales.mobileappproject.COMPANYLOCATION";
    private static final String SIZE = "com.example.arosales.mobileappproject.COMPANYSIZE";
    private static final String WEB = "com.example.arosales.mobileappproject.COMPANYWEBSITE";
    private static final String CLIENTS = "com.example.arosales.mobileappproject.COMPANYCLIENTS";
    public static final String RECEIVER = "com.example.arosales.mobileappproject.RECEIVER";
    public static final String RECEIVERTYPE = "com.example.arosales.mobileappproject.RECEIVERTYPE";

    private Company company;
    private String save_delete_type;

    public void saveCompany(View view) {

        try {
            ParseQuery<ParseObject> queryStudent = ParseQuery.getQuery("Student");
            queryStudent.include("StudentId");
            queryStudent.include("CurrentCompany");
            queryStudent.whereEqualTo("StudentId", ParseUser.getCurrentUser());
            ParseQuery<ParseObject> queryCompany = ParseQuery.getQuery("Company");
            queryCompany.include("CompanyId");
            queryCompany.whereEqualTo("objectId", company.getId());

            ParseObject student = queryStudent.getFirst();
            ParseObject company = queryCompany.getFirst();

            ParseQuery<ParseObject> querySavedCompany = ParseQuery.getQuery("SavedCompany");
            querySavedCompany.whereEqualTo("StudentId", student);
            querySavedCompany.whereEqualTo("CompanyId", company);

            if(save_delete_type.equals("Search")) {
                String message = null;

                if (querySavedCompany.count() == 0) {
                    ParseObject savedCompany = new ParseObject("SavedCompany");
                    savedCompany.put("StudentId", student);
                    savedCompany.put("CompanyId", company);
                    savedCompany.saveInBackground();
                    message = getString(R.string.addedSavedCompanyMessage);

                } else {
                    message = getString(R.string.existingSavedCompanyMessage);

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Save companies");
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
            }
            else{
                querySavedCompany.getFirst().deleteInBackground();

                Intent intent = new Intent(this, CompanySearchResults.class);
                HashMap<String,String> search_filters= new HashMap<String,String>();
                search_filters.put(INFO_SEARCHTYPE,"Saved Companies");
                Bundle b = new Bundle();
                b.putSerializable(INFO_HASH,search_filters);
                intent.putExtras(b);
                startActivity(intent);

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, SendMessage.class);
        intent.putExtra(RECEIVER, company.getId());
        intent.putExtra(RECEIVERTYPE, "Company");
        startActivity(intent);
    }

    public void backToResults(View view) {
        ViewCompany.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_company);

        Intent intent = getIntent();
        company = (Company) intent.getSerializableExtra(CompanyAdapter.COMPANY);
        save_delete_type = (String) intent.getSerializableExtra(CompanyAdapter.SEARCH_TYPE);

        TextView name = (TextView) findViewById(R.id.companyName);
        name.setText(company.getName().toUpperCase());
        TextView industry = (TextView) findViewById(R.id.companyIndustry);
        if(company.getIndustry()!=null)
            industry.setText(industry.getText().toString()+"\n"+ company.getIndustry()+"\n");
        TextView description = (TextView) findViewById(R.id.companyDescription);
        if(company.getDescription()!=null)
            description.setText(description.getText().toString()+"\n"+company.getDescription()+"\n");
        TextView location = (TextView) findViewById(R.id.companyLocation);
        location.setText(location.getText().toString()+"\n"+company.getLocation()+"\n");
        TextView size = (TextView) findViewById(R.id.companySize);
        if(company.getCompany_size()>0)
            size.setText(size.getText().toString()+"\n"+ String.valueOf(company.getCompany_size()) + "\n");
        TextView website = (TextView) findViewById(R.id.website);
        if(company.getWebsite()!=null)
            website.setText(website.getText().toString()+"\n"+ company.getWebsite()+"\n");
        TextView clients = (TextView) findViewById(R.id.clients);
        if(company.getClients()!=null)
            clients.setText(clients.getText().toString()+"\n"+company.getClients()+"\n");

        Button save_delete = (Button) findViewById(R.id.saveButton);
        if(save_delete_type.equals("Search")){
            save_delete.setText(getResources().getString(R.string.save_company_button));
        }
        else {
            save_delete.setText(getResources().getString(R.string.remove_company_button));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        TextView name = (TextView) findViewById(R.id.companyName);
        TextView industry = (TextView) findViewById(R.id.companyIndustry);
        TextView description = (TextView) findViewById(R.id.companyDescription);
        TextView location = (TextView) findViewById(R.id.companyLocation);
        TextView size = (TextView) findViewById(R.id.companySize);
        TextView website = (TextView) findViewById(R.id.website);
        TextView clients = (TextView) findViewById(R.id.clients);

        savedInstanceState.putString(NAME, name.getText().toString());
        savedInstanceState.putString(INDUSTRY, industry.getText().toString());
        savedInstanceState.putString(DESCRIPTION, description.getText().toString());
        savedInstanceState.putString(LOCATION, location.getText().toString());
        savedInstanceState.putString(SIZE, size.getText().toString());
        savedInstanceState.putString(WEB, website.getText().toString());
        savedInstanceState.putString(CLIENTS, clients.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        TextView name = (TextView) findViewById(R.id.companyName);
        TextView industry = (TextView) findViewById(R.id.companyIndustry);
        TextView description = (TextView) findViewById(R.id.companyDescription);
        TextView location = (TextView) findViewById(R.id.companyLocation);
        TextView size = (TextView) findViewById(R.id.companySize);
        TextView website = (TextView) findViewById(R.id.website);
        TextView clients = (TextView) findViewById(R.id.clients);

        name.setText(savedInstanceState.getString(NAME));
        industry.setText(savedInstanceState.getString(INDUSTRY));
        description.setText(savedInstanceState.getString(DESCRIPTION));
        location.setText(savedInstanceState.getString(LOCATION));
        size.setText(savedInstanceState.getString(SIZE));
        website.setText(savedInstanceState.getString(WEB));
        clients.setText(savedInstanceState.getString(CLIENTS));

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

}
