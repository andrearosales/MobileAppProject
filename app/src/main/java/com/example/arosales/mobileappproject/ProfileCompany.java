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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class ProfileCompany extends AppCompatActivity {

    private static final String INFO_NAME = "com.example.arosales.mobileappproject.INFO_NAME";
    private static final String INFO_LOCATION = "com.example.arosales.mobileappproject.INFO_LOCATION";
    private static final String INFO_ADDRESS = "com.example.arosales.mobileappproject.INFO_ADDRESS";
    private static final String INFO_INDUSTRY = "com.example.arosales.mobileappproject.INFO_INDUSTRY";
    private static final String INFO_DESCRIPTION = "com.example.arosales.mobileappproject.INFO_DESCRIPTION";
    private static final String INFO_SIZE = "com.example.arosales.mobileappproject.INFO_SIZE";
    private static final String INFO_WEBSITE = "com.example.arosales.mobileappproject.INFO_WEBSITE";
    private static final String INFO_CLIENTS = "com.example.arosales.mobileappproject.INFO_CLIENTS";
    private static final String INFO_EDIT = "com.example.arosales.mobileappproject.INFO_EDIT";

    private EditText NameView;
    private Spinner LocationView;
    private EditText AddressView;
    private Spinner IndustryView;
    private EditText DescriptionView;
    private EditText CompanySizeView;
    private EditText WebsiteView;
    private EditText ClientsView;

    private ArrayAdapter<String> adapterLocation;
    private ArrayAdapter<String> adapterIndustry;

    private boolean isEditable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_company);

        NameView = (EditText) findViewById(R.id.textName);
        AddressView = (EditText) findViewById(R.id.textAddress);
        LocationView = (Spinner) findViewById(R.id.spinnerLocation);
        adapterLocation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayLocation));
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LocationView.setAdapter(adapterLocation);

        IndustryView = (Spinner) findViewById(R.id.spinnerIndustry);
        adapterIndustry = new ArrayAdapter<String>(ProfileCompany.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayIndustry));
        adapterIndustry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        IndustryView.setAdapter(adapterIndustry);

        DescriptionView = (EditText) findViewById(R.id.descriptionText);
        CompanySizeView = (EditText) findViewById(R.id.textSize);
        WebsiteView = (EditText) findViewById(R.id.textWeb);
        ClientsView = (EditText) findViewById(R.id.textClients);

        ParseQuery query = new ParseQuery("Company");
        query.whereEqualTo("CompanyId", ParseUser.getCurrentUser());

        try {
            ParseObject company = query.getFirst();
            NameView.setText(company.getString("Name"));
            AddressView.setText(company.getString("Address"));

            if (company.get("Location") == null) {
                LocationView.setSelection(0);
            } else {
                LocationView.setSelection(adapterLocation.getPosition(company.getString("Location")));
            }

            if (company.get("Description") != null) {
                DescriptionView.setText(company.getString("Description"));
            }

            if (company.get("Industry") == null) {
                IndustryView.setSelection(0);
            } else {
                IndustryView.setSelection(adapterIndustry.getPosition(company.getString("Industry")));
            }

            if (company.get("Size") != null && company.getInt("Size") != 0) {
                int csize = company.getInt("Size");
                CompanySizeView.setText(String.valueOf(csize));
            }

            if (company.getString("Website") != null) {
                WebsiteView.setText(company.getString("Website"));
            }

            if (company.getString("Clients") != null) {
                ClientsView.setText(company.getString("Clients"));
            }

            disableFields();
        } catch (ParseException e)
        {
            e.printStackTrace();
        }


    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        NameView = (EditText) findViewById(R.id.textName);
        AddressView = (EditText) findViewById(R.id.textAddress);
        LocationView = (Spinner) findViewById(R.id.spinnerLocation);
        IndustryView = (Spinner) findViewById(R.id.spinnerIndustry);
        DescriptionView = (EditText) findViewById(R.id.descriptionText);
        CompanySizeView = (EditText) findViewById(R.id.textSize);
        WebsiteView = (EditText) findViewById(R.id.textWeb);
        ClientsView = (EditText) findViewById(R.id.textClients);

        outState.putString(INFO_NAME, NameView.getText().toString());
        outState.putString(INFO_ADDRESS, AddressView.getText().toString());
        outState.putString(INFO_LOCATION, LocationView.getSelectedItem().toString());

        if (!IndustryView.getSelectedItem().toString().equals("-"))
            outState.putString(INFO_INDUSTRY, IndustryView.getSelectedItem().toString());

        if (!DescriptionView.getText().toString().equals(""))
            outState.putString(INFO_DESCRIPTION, DescriptionView.getText().toString());

        if (!CompanySizeView.getText().toString().equals(""))
            outState.putString(INFO_SIZE, CompanySizeView.getText().toString());

        if (!WebsiteView.getText().toString().equals(""))
            outState.putString(INFO_WEBSITE, WebsiteView.getText().toString());

        if (!ClientsView.getText().toString().equals(""))
            outState.putString(INFO_CLIENTS, ClientsView.getText().toString());

        outState.putBoolean(INFO_EDIT, isEditable);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        NameView.setText(savedInstanceState.getString(INFO_NAME));
        AddressView.setText(savedInstanceState.getString(INFO_ADDRESS));
        LocationView.setSelection(adapterLocation.getPosition(savedInstanceState.getString(INFO_LOCATION)));

        if (savedInstanceState.containsKey(INFO_INDUSTRY)) {
            IndustryView.setSelection(adapterIndustry.getPosition(savedInstanceState.getString(INFO_INDUSTRY)));
        }

        if (savedInstanceState.containsKey(INFO_DESCRIPTION)) {
            DescriptionView.setText(savedInstanceState.getString(INFO_DESCRIPTION));
        }

        if (savedInstanceState.containsKey(INFO_SIZE)) {
            CompanySizeView.setText(savedInstanceState.getString(INFO_SIZE));
        }

        if (savedInstanceState.containsKey(INFO_WEBSITE)) {
            WebsiteView.setText(savedInstanceState.getString(INFO_WEBSITE));
        }

        if (savedInstanceState.containsKey(INFO_CLIENTS)) {
            ClientsView.setText(savedInstanceState.getString(INFO_CLIENTS));
        }


        if (savedInstanceState.containsKey(INFO_EDIT)) {
            if (savedInstanceState.getBoolean(INFO_EDIT)) {
                enableFields();
            } else {
                disableFields();
                isEditable = false;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_company, menu);
        return true;
    }

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
            ParseUser.logOut();
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveProfile(View view) {

        // Set up a progress dialog
        final ProgressDialog dlg = new ProgressDialog(ProfileCompany.this);
        dlg.setTitle(R.string.saveprofiletitle);
        dlg.setMessage(getString(R.string.saveprofilemessage));
        dlg.show();
        //save the data to parse.com

        try {
            ParseQuery query = new ParseQuery("Company");
            query.whereEqualTo("CompanyId", ParseUser.getCurrentUser());
            ParseObject company = query.getFirst();

            if (!IndustryView.getSelectedItem().toString().equals("-")) {
                company.put("Industry", IndustryView.getSelectedItem().toString());
            } else {
                company.remove("Industry");
            }
            if (!DescriptionView.getText().toString().equals(""))
                company.put("Description", DescriptionView.getText().toString());
            else
                company.remove("Description");

            if (!CompanySizeView.getText().toString().equals(""))
                company.put("Size", Integer.valueOf(CompanySizeView.getText().toString()));
            else
                company.remove("Size");

            if (!WebsiteView.getText().toString().equals(""))
                company.put("Website", WebsiteView.getText().toString());
            else
                company.remove("Website");

            if (!ClientsView.getText().toString().equals(""))
                company.put("Clients", ClientsView.getText().toString());
            else
                company.remove("Clients");


            company.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Intent intent = new Intent(ProfileCompany.this, ProfileCompany.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    public void editProfile(View view) {
        enableFields();
    }

    private void enableFields() {
        Button editButton = (Button) findViewById(R.id.editButton);
        editButton.setVisibility(View.GONE);

        DescriptionView.setFocusable(true);
        DescriptionView.setFocusableInTouchMode(true);
        DescriptionView.setClickable(true);

        IndustryView.setFocusable(true);
        IndustryView.setFocusableInTouchMode(true);
        IndustryView.setClickable(true);
        IndustryView.setEnabled(true);

        CompanySizeView.setFocusable(true);
        CompanySizeView.setFocusableInTouchMode(true);
        CompanySizeView.setClickable(true);

        WebsiteView.setFocusable(true);
        WebsiteView.setFocusableInTouchMode(true);
        WebsiteView.setClickable(true);

        ClientsView.setFocusable(true);
        ClientsView.setFocusableInTouchMode(true);
        ClientsView.setClickable(true);

        isEditable = true;
    }

    private void disableFields() {
        NameView.setFocusable(false);
        NameView.setFocusableInTouchMode(false);
        NameView.setClickable(false);

        AddressView.setFocusable(false);
        AddressView.setFocusableInTouchMode(false);
        AddressView.setClickable(false);

        LocationView.setFocusable(false);
        LocationView.setFocusableInTouchMode(false);
        LocationView.setClickable(false);
        LocationView.setEnabled(false);


        DescriptionView.setFocusable(false);
        DescriptionView.setFocusableInTouchMode(false);
        DescriptionView.setClickable(false);

        IndustryView.setFocusable(false);
        IndustryView.setFocusableInTouchMode(false);
        IndustryView.setClickable(false);
        IndustryView.setEnabled(false);

        CompanySizeView.setFocusable(false);
        CompanySizeView.setFocusableInTouchMode(false);
        CompanySizeView.setClickable(false);

        WebsiteView.setFocusable(false);
        WebsiteView.setFocusableInTouchMode(false);
        WebsiteView.setClickable(false);

        ClientsView.setFocusable(false);
        ClientsView.setFocusableInTouchMode(false);
        ClientsView.setClickable(false);
    }

    public void deleteProfile(View view) {

        try {
            //
                /*Parse.initialize(this, "H9NFC1K9LmahxGcCrMOdT0qMaE0lDGT6BgbrSOAc", "4K2VfxRGIyk69KlQJ2B8NMnD71llrlkEPLdTNh9M");
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("objectId", "2AM7fmxH5Sk");
                ParseUser user = query.getFirst();*/
            //

            ParseQuery<ParseObject> queryCompany = ParseQuery.getQuery("Company");
            queryCompany.include("CompanyId");
            queryCompany.whereEqualTo("CompanyId", ParseUser.getCurrentUser());

            ParseObject company = queryCompany.getFirst();

            ParseQuery<ParseObject> queryApplyJob = ParseQuery.getQuery("ApplyJob");
            queryApplyJob.include("JobId");
            queryApplyJob.include("JobId.CompanyId");
            //queryApplyJob.whereEqualTo("JobId.CompanyId", company);

            List<ParseObject> resultsApplyJob = queryApplyJob.find();
            for (ParseObject p : resultsApplyJob) {
                ParseObject job_result = p.getParseObject("JobId");

                ParseObject companyCurrent = job_result.getParseObject("CompanyId");
                if (companyCurrent != null && companyCurrent.getObjectId().equals(company.getObjectId())) {
                    p.delete();
                }
            }

            ParseQuery<ParseObject> querySavedCompany = ParseQuery.getQuery("SavedCompany");
            querySavedCompany.include("CompanyId");
            querySavedCompany.whereEqualTo("CompanyId", company);

            List<ParseObject> resultsSavedCompany = querySavedCompany.find();
            for (ParseObject p : resultsSavedCompany) {
                p.delete();
            }

            ParseQuery<ParseObject> querySavedJobOffer = ParseQuery.getQuery("SavedJobOffer");
            querySavedJobOffer.include("OfferId");
            querySavedJobOffer.include("OfferId.CompanyId");
            //querySavedJobOffer.whereEqualTo("OfferId.CompanyId", company);

            List<ParseObject> resultsSavedJobOffer = querySavedJobOffer.find();
            for (ParseObject p : resultsSavedJobOffer) {
                ParseObject job_result = p.getParseObject("OfferId");

                ParseObject companyCurrent = job_result.getParseObject("CompanyId");
                if (companyCurrent != null && companyCurrent.getObjectId().equals(company.getObjectId())) {
                    p.delete();
                }
            }

            ParseQuery<ParseObject> querySavedStudent = ParseQuery.getQuery("SavedStudent");
            querySavedStudent.include("CompanyId");
            querySavedStudent.whereEqualTo("CompanyId", company);

            List<ParseObject> resultsSavedStudent = querySavedStudent.find();
            for (ParseObject p : resultsSavedStudent) {
                p.delete();
            }

            ParseQuery<ParseObject> queryJobOffer = ParseQuery.getQuery("JobOffer");
            queryJobOffer.include("CompanyId");
            queryJobOffer.whereEqualTo("CompanyId", company);

            List<ParseObject> resultsJobOffer = queryJobOffer.find();
            for (ParseObject p : resultsJobOffer) {
                p.delete();
            }


            ParseQuery<ParseObject> queryMessage = ParseQuery.getQuery("Message");
            queryMessage.whereEqualTo("SenderId", ParseUser.getCurrentUser().getObjectId());
            List<ParseObject> resultsMessage = queryMessage.find();
            for (ParseObject p : resultsMessage) {
                p.delete();
            }

            queryMessage = ParseQuery.getQuery("Message");
            queryMessage.whereEqualTo("ReceiverId", ParseUser.getCurrentUser().getObjectId());
            resultsMessage = queryMessage.find();
            for (ParseObject p : resultsMessage) {
                p.delete();
            }

            company.delete();
            ParseUser.getCurrentUser().delete();
            //String objectId = ParseUser.getCurrentUser().getObjectId();
            ParseUser.logOut();


            startActivity(new Intent(this, ManageSession.class));


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
