package com.example.arosales.mobileappproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ProfileStudent extends AppCompatActivity {

    private static final String INFO_NAME = "com.example.arosales.mobileappproject.INFO_NAME";
    private static final String INFO_SURNAME = "com.example.arosales.mobileappproject.INFO_SURNAME";
    private static final String INFO_LOCATION = "com.example.arosales.mobileappproject.INFO_LOCATION";
    private static final String INFO_INDUSTRY = "com.example.arosales.mobileappproject.INFO_INDUSTRY";
    private static final String INFO_BIRTHDATE = "com.example.arosales.mobileappproject.INFO_BIRTHDATE";
    private static final String INFO_BIRTHPLACE = "com.example.arosales.mobileappproject.INFO_BIRTHPLACE";
    private static final String INFO_DESCRIPTION = "com.example.arosales.mobileappproject.INFO_DESCRIPTION";
    private static final String INFO_TECHSKILLS = "com.example.arosales.mobileappproject.INFO_TECHSKILLS";
    private static final String INFO_ENGLISH = "com.example.arosales.mobileappproject.INFO_ENGLISH";
    private static final String INFO_FRENCH = "com.example.arosales.mobileappproject.INFO_FRENCH";
    private static final String INFO_GERMAN = "com.example.arosales.mobileappproject.INFO_GERMAN";
    private static final String INFO_ITALIAN = "com.example.arosales.mobileappproject.INFO_ITALIAN";
    private static final String INFO_MANDARIN = "com.example.arosales.mobileappproject.INFO_MANDARIN";
    private static final String INFO_PORTUGUESE = "com.example.arosales.mobileappproject.INFO_PORTUGUESE";
    private static final String INFO_SPANISH = "com.example.arosales.mobileappproject.INFO_SPANISH";
    private static final String INFO_INTERESTS = "com.example.arosales.mobileappproject.INFO_INTERESTS";
    private static final String INFO_EXPERIENCE = "com.example.arosales.mobileappproject.INFO_EXPERIENCE";
    private static final String INFO_DEGREE = "com.example.arosales.mobileappproject.INFO_DEGREE";
    private static final String INFO_PHONE = "com.example.arosales.mobileappproject.INFO_PHONE";
    private static final String INFO_CURRENT_COMPANY = "com.example.arosales.mobileappproject.INFO_CURRENT_COMPANY";
    private static final String INFO_EDIT = "com.example.arosales.mobileappproject.INFO_EDIT";


    private EditText NameView;
    private EditText SurnameView;
    private Spinner LocationView;
    private Spinner IndustryView;
    private EditText DateOfBirthView;
    private EditText PlaceOfBirthView;
    private EditText DescriptionView;
    private EditText TechnicalSkillsView;
    private CheckBox language_eng;
    private CheckBox language_fra;
    private CheckBox language_ger;
    private CheckBox language_ita;
    private CheckBox language_man;
    private CheckBox language_por;
    private CheckBox language_spa;
    private EditText InterestsView;
    private EditText YearOfExpView;
    private Spinner TypeOfDegreeView;
    private EditText PhoneNumberView;
    private Spinner CurrentCompanyView;

    private ArrayAdapter<String> adapterLocation;
    private ArrayAdapter<String> adapterIndustry;
    private ArrayAdapter<String> adapterDegree;
    private ArrayAdapter<String> adapterCurrentCompany;

    private boolean isEditable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_student);

        NameView = (EditText) findViewById(R.id.textName);
        SurnameView = (EditText) findViewById(R.id.textSurname);
        LocationView = (Spinner) findViewById(R.id.spinnerLocation);
        adapterLocation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayLocation));
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LocationView.setAdapter(adapterLocation);


        IndustryView = (Spinner) findViewById(R.id.spinnerIndustry);
        adapterIndustry = new ArrayAdapter<String>(ProfileStudent.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayIndustry));
        adapterIndustry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        IndustryView.setAdapter(adapterIndustry);

        DateOfBirthView = (EditText) findViewById(R.id.dateBirthText);
        PlaceOfBirthView = (EditText) findViewById(R.id.placeBirthText);
        DescriptionView = (EditText) findViewById(R.id.descriptionText);
        TechnicalSkillsView = (EditText) findViewById(R.id.textTechSkills);

        language_eng = ((CheckBox) findViewById(R.id.checkbox_english));
        language_fra = ((CheckBox) findViewById(R.id.checkbox_french));
        language_ger = ((CheckBox) findViewById(R.id.checkbox_german));
        language_ita = ((CheckBox) findViewById(R.id.checkbox_italian));
        language_man = ((CheckBox) findViewById(R.id.checkbox_mandarin));
        language_por = ((CheckBox) findViewById(R.id.checkbox_portuguese));
        language_spa = ((CheckBox) findViewById(R.id.checkbox_spanish));

        InterestsView = (EditText) findViewById(R.id.textInterests);
        YearOfExpView = (EditText) findViewById(R.id.textExperience);
        TypeOfDegreeView = (Spinner) findViewById(R.id.spinnerDegree);
        String[] TypeOfDegree = getResources().getStringArray(R.array.arrayDegree);
        adapterDegree = new ArrayAdapter<String>(ProfileStudent.this, android.R.layout.simple_spinner_item, TypeOfDegree);
        adapterDegree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TypeOfDegreeView.setAdapter(adapterDegree);

        PhoneNumberView = (EditText) findViewById(R.id.textPhone);
        CurrentCompanyView = (Spinner) findViewById(R.id.spinnerCurrentCompany);

        ParseQuery companyQuery = new ParseQuery("Company");
        List<String> displayCompanies = new ArrayList<String>();
        displayCompanies.add("-");
        try {
            List<ParseObject> existing_companies = companyQuery.find();
            for (ParseObject p : existing_companies) {
                displayCompanies.add(p.getString("Name"));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        adapterCurrentCompany = new ArrayAdapter<String>(ProfileStudent.this, android.R.layout.simple_spinner_item, displayCompanies);
        adapterCurrentCompany.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CurrentCompanyView.setAdapter(adapterCurrentCompany);


        ParseQuery query = new ParseQuery("Student");
        query.whereEqualTo("StudentId", ParseUser.getCurrentUser());
        query.include("CurrentCompany");

        try {
            ParseObject student = query.getFirst();

            NameView.setText(student.getString("Name"));
            SurnameView.setText(student.getString("Surname"));


            if (student.get("Location") == null) {
                LocationView.setSelection(0);
            } else {
                LocationView.setSelection(adapterLocation.getPosition(student.getString("Location")));
            }

            if (student.get("Industry") == null) {
                IndustryView.setSelection(0);
            } else {
                IndustryView.setSelection(adapterIndustry.getPosition(student.getString("Industry")));
            }

            if (student.getDate("Birthdate") != null) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date birthdate = student.getDate("Birthdate");
                DateOfBirthView.setText(df.format(birthdate));
            }

            if (student.getString("Birthplace") != null) {
                PlaceOfBirthView.setText(student.getString("Birthplace"));
            }

            if (student.getString("Description") != null) {
                DescriptionView.setText(student.getString("Description"));
            }

            if (student.get("TechnicalSkills") != null){
                ArrayList<String> technical_skills = (ArrayList<String>) student.get("TechnicalSkills");
                if(technical_skills.size()>0)
                    TechnicalSkillsView.setText(technical_skills.toString().substring(1, technical_skills.toString().length() - 1));
            }

            if (student.get("Languages") != null) {
                ArrayList<String> languages = (ArrayList<String>) student.get("Languages");
                if (languages.contains("English")) {
                    language_eng.setChecked(true);
                }
                if (languages.contains("French")) {
                    language_fra.setChecked(true);
                }
                if (languages.contains("German")) {
                    language_ger.setChecked(true);
                }
                if (languages.contains("Italian")) {
                    language_ita.setChecked(true);
                }
                if (languages.contains("Mandarin")) {
                    language_man.setChecked(true);
                }
                if (languages.contains("Portuguese")) {
                    language_por.setChecked(true);
                }
                if (languages.contains("Spanish")) {
                    language_spa.setChecked(true);
                }
            }

            if (student.get("Interests") != null) {
                ArrayList<String> interests = (ArrayList<String>) student.get("Interests");
                if(interests.size()>0)
                    InterestsView.setText(interests.toString().substring(1, interests.toString().length() - 1));
            }

            if (student.get("ExperienceYears") != null && student.getInt("ExperienceYears") != 0) {
                int yearExperience = student.getInt("ExperienceYears");
                YearOfExpView.setText(String.valueOf(yearExperience));
            }

            if (student.get("TypeOfDegree") == null) {
                TypeOfDegreeView.setSelection(0);
            } else {
                TypeOfDegreeView.setSelection(adapterDegree.getPosition(student.getString("TypeOfDegree")));
            }

            if (student.get("PhoneNumber") != null) {
                PhoneNumberView.setText(student.getString("PhoneNumber"));
            }

            ParseObject current_company = student.getParseObject("CurrentCompany");
            if (current_company == null) {
                CurrentCompanyView.setSelection(0);
            } else {
                CurrentCompanyView.setSelection(adapterCurrentCompany.getPosition(current_company.getString("Name")));
            }

            disableFields();


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        NameView = (EditText) findViewById(R.id.textName);
        SurnameView = (EditText) findViewById(R.id.textSurname);
        LocationView = (Spinner) findViewById(R.id.spinnerLocation);
        IndustryView = (Spinner) findViewById(R.id.spinnerIndustry);
        DateOfBirthView = (EditText) findViewById(R.id.dateBirthText);
        PlaceOfBirthView = (EditText) findViewById(R.id.placeBirthText);
        DescriptionView = (EditText) findViewById(R.id.descriptionText);
        TechnicalSkillsView = (EditText) findViewById(R.id.textTechSkills);
        language_eng = ((CheckBox) findViewById(R.id.checkbox_english));
        language_fra = ((CheckBox) findViewById(R.id.checkbox_french));
        language_ger = ((CheckBox) findViewById(R.id.checkbox_german));
        language_ita = ((CheckBox) findViewById(R.id.checkbox_italian));
        language_man = ((CheckBox) findViewById(R.id.checkbox_mandarin));
        language_por = ((CheckBox) findViewById(R.id.checkbox_portuguese));
        language_spa = ((CheckBox) findViewById(R.id.checkbox_spanish));
        InterestsView = (EditText) findViewById(R.id.textInterests);
        YearOfExpView = (EditText) findViewById(R.id.textExperience);
        TypeOfDegreeView = (Spinner) findViewById(R.id.spinnerDegree);
        PhoneNumberView = (EditText) findViewById(R.id.textPhone);
        CurrentCompanyView = (Spinner) findViewById(R.id.spinnerCurrentCompany);

        outState.putString(INFO_NAME, NameView.getText().toString());
        outState.putString(INFO_SURNAME, SurnameView.getText().toString());

        if (!LocationView.getSelectedItem().toString().equals("-"))
            outState.putString(INFO_LOCATION, LocationView.getSelectedItem().toString());

        if (!IndustryView.getSelectedItem().toString().equals("-"))
            outState.putString(INFO_INDUSTRY, IndustryView.getSelectedItem().toString());

        if (!DateOfBirthView.getText().toString().equals(""))
            outState.putString(INFO_BIRTHDATE, DateOfBirthView.getText().toString());

        if (!PlaceOfBirthView.getText().toString().equals(""))
            outState.putString(INFO_BIRTHPLACE, PlaceOfBirthView.getText().toString());

        if (!DescriptionView.getText().toString().equals(""))
            outState.putString(INFO_DESCRIPTION, DescriptionView.getText().toString());

        if (!TechnicalSkillsView.getText().toString().equals(""))
            outState.putString(INFO_TECHSKILLS, TechnicalSkillsView.getText().toString());

        if (language_eng.isChecked())
            outState.putString(INFO_ENGLISH, "TRUE");

        if (language_fra.isChecked())
            outState.putString(INFO_FRENCH, "TRUE");

        if (language_ger.isChecked())
            outState.putString(INFO_GERMAN, "TRUE");

        if (language_ita.isChecked())
            outState.putString(INFO_ITALIAN, "TRUE");

        if (language_man.isChecked())
            outState.putString(INFO_MANDARIN, "TRUE");

        if (language_por.isChecked())
            outState.putString(INFO_PORTUGUESE, "TRUE");

        if (language_spa.isChecked())
            outState.putString(INFO_SPANISH, "TRUE");

        if (!InterestsView.getText().toString().equals(""))
            outState.putString(INFO_INTERESTS, InterestsView.getText().toString());

        if (!YearOfExpView.getText().toString().equals(""))
            outState.putString(INFO_EXPERIENCE, YearOfExpView.getText().toString());

        if (!TypeOfDegreeView.getSelectedItem().toString().equals("-"))
            outState.putString(INFO_DEGREE, TypeOfDegreeView.getSelectedItem().toString());

        if (!PhoneNumberView.getText().toString().equals(""))
            outState.putString(INFO_PHONE, PhoneNumberView.getText().toString());

        if (!CurrentCompanyView.getSelectedItem().toString().equals("-"))
            outState.putString(INFO_CURRENT_COMPANY, CurrentCompanyView.getSelectedItem().toString());

        outState.putBoolean(INFO_EDIT, isEditable);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        NameView.setText(savedInstanceState.getString(INFO_NAME));
        SurnameView.setText(savedInstanceState.getString(INFO_SURNAME));

        if (savedInstanceState.containsKey(INFO_LOCATION))
            LocationView.setSelection(adapterLocation.getPosition(savedInstanceState.getString(INFO_LOCATION)));

        if (savedInstanceState.containsKey(INFO_INDUSTRY))
            IndustryView.setSelection(adapterIndustry.getPosition(savedInstanceState.getString(INFO_INDUSTRY)));

        if (savedInstanceState.containsKey(INFO_BIRTHDATE))
            DateOfBirthView.setText(savedInstanceState.getString(INFO_BIRTHDATE));

        if (savedInstanceState.containsKey(INFO_BIRTHPLACE))
            PlaceOfBirthView.setText(savedInstanceState.getString(INFO_BIRTHPLACE));

        if (savedInstanceState.containsKey(INFO_DESCRIPTION))
            DescriptionView.setText(savedInstanceState.getString(INFO_DESCRIPTION));

        if (savedInstanceState.containsKey(INFO_TECHSKILLS))
            TechnicalSkillsView.setText(savedInstanceState.getString(INFO_TECHSKILLS));

        if (savedInstanceState.containsKey(INFO_ENGLISH))
            language_eng.setChecked(true);

        if (savedInstanceState.containsKey(INFO_FRENCH))
            language_fra.setChecked(true);

        if (savedInstanceState.containsKey(INFO_GERMAN))
            language_ger.setChecked(true);

        if (savedInstanceState.containsKey(INFO_ITALIAN))
            language_ita.setChecked(true);

        if (savedInstanceState.containsKey(INFO_MANDARIN))
            language_man.setChecked(true);

        if (savedInstanceState.containsKey(INFO_SPANISH))
            language_spa.setChecked(true);

        if (savedInstanceState.containsKey(INFO_PORTUGUESE))
            language_por.setChecked(true);

        if (savedInstanceState.containsKey(INFO_INTERESTS))
            InterestsView.setText(savedInstanceState.getString(INFO_INTERESTS));

        if (savedInstanceState.containsKey(INFO_EXPERIENCE))
            YearOfExpView.setText(savedInstanceState.getString(INFO_EXPERIENCE));

        if (savedInstanceState.containsKey(INFO_DEGREE))
            TypeOfDegreeView.setSelection(adapterDegree.getPosition(savedInstanceState.getString(INFO_DEGREE)));

        if (savedInstanceState.containsKey(INFO_PHONE))
            PhoneNumberView.setText(savedInstanceState.getString(INFO_PHONE));

        if (savedInstanceState.containsKey(INFO_CURRENT_COMPANY))
            CurrentCompanyView.setSelection(adapterCurrentCompany.getPosition(savedInstanceState.getString(INFO_CURRENT_COMPANY)));

        if (savedInstanceState.containsKey(INFO_EDIT)) {
            if (savedInstanceState.getBoolean(INFO_EDIT)) {
                enableFields();
            }
            else{
                disableFields();
                isEditable=false;
            }
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
    
    public void editProfile(View view) {
        enableFields();
    }

    public void saveProfile(View view) {

        Date birthDate = null;
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getResources().getString(R.string.error_intro));
        if (!DateOfBirthView.getText().toString().equals("")) {
            if(DateOfBirthView.getText().toString().matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    birthDate = formatter.parse(DateOfBirthView.getText().toString());
                } catch (java.text.ParseException e) {
                    validationError = true;
                    validationErrorMessage.append(getResources().getString(R.string.error_invalid_DateOfBirth));
                }
            }
            else{
                validationError = true;
                validationErrorMessage.append(getResources().getString(R.string.error_invalid_DateOfBirth));
            }
        }

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(ProfileStudent.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }



        ArrayList<String> languages = new ArrayList<String>();
        if (language_eng.isChecked()) {
            languages.add(language_eng.getText().toString());
        }
        if (language_fra.isChecked()) {
            languages.add(language_fra.getText().toString());
        }
        if (language_ita.isChecked()) {
            languages.add(language_ita.getText().toString());
        }
        if (language_ger.isChecked()) {
            languages.add(language_ger.getText().toString());
        }
        if (language_man.isChecked()) {
            languages.add(language_man.getText().toString());
        }
        if (language_por.isChecked()) {
            languages.add(language_por.getText().toString());
        }
        if (language_spa.isChecked()) {
            languages.add(language_spa.getText().toString());
        }

        // Set up a progress dialog
        final ProgressDialog dlg = new ProgressDialog(ProfileStudent.this);
        dlg.setTitle(R.string.saveprofiletitle);
        dlg.setMessage(getString(R.string.saveprofilemessage));
        dlg.show();
        //save the data to parse.com


        ParseQuery studentQuery = new ParseQuery("Student");
        studentQuery.whereEqualTo("StudentId", ParseUser.getCurrentUser());
        try {
            ParseObject student = studentQuery.getFirst();

            if (!LocationView.getSelectedItem().toString().equals("-")) {
                student.put("Location", LocationView.getSelectedItem().toString());
            } else {
                student.remove("Location");
            }

            if (!IndustryView.getSelectedItem().toString().equals("-")) {
                student.put("Industry", IndustryView.getSelectedItem().toString());
            } else {
                student.remove("Industry");
            }

            if(birthDate!=null){
                student.put("Birthdate",birthDate);
            }
            else{
                student.remove("Birthdate");
            }

            if(!PlaceOfBirthView.getText().toString().equals(""))
                student.put("Birthplace", PlaceOfBirthView.getText().toString());
            else
                student.remove("Birthplace");

            if(!DescriptionView.getText().toString().equals(""))
                student.put("Description", DescriptionView.getText().toString());
            else
                student.remove("Description");

            if (!TechnicalSkillsView.getText().toString().equals("")) {
                List<String> initial_techskills = Arrays.asList(TechnicalSkillsView.getText().toString().toLowerCase().replaceAll("\\s+", "").split(","));
                student.put("TechnicalSkills", initial_techskills);
            } else {
                student.remove("TechnicalSkills");
            }

            if(languages.size()>0)
                student.put("Languages", languages);
            else
                student.remove("Languages");

            if (!InterestsView.getText().toString().equals("")) {
                List<String> interests = Arrays.asList(InterestsView.getText().toString().toLowerCase().replaceAll("\\s+", "").split(","));
                student.put("Interests", interests);
            }else{
                student.remove("Interests");
            }

            if (!YearOfExpView.getText().toString().equals("")) {
                student.put("ExperienceYears", Integer.valueOf(YearOfExpView.getText().toString()));
            }else{
                student.remove("ExperienceYears");
            }

            if (!TypeOfDegreeView.getSelectedItem().toString().equals("-")) {
                student.put("TypeOfDegree", TypeOfDegreeView.getSelectedItem().toString());
            }else{
                student.remove("TypeOfDegree");
            }


            if (!PhoneNumberView.getText().toString().equals("")) {
                student.put("PhoneNumber", PhoneNumberView.getText().toString());
            }else{
                student.remove("PhoneNumber");
            }


            if (!CurrentCompanyView.getSelectedItem().toString().equals("-")) {
                ParseQuery companyQuery = new ParseQuery("Company");
                companyQuery.whereEqualTo("Name", CurrentCompanyView.getSelectedItem().toString());
                ParseObject c_company = companyQuery.getFirst();
                student.put("CurrentCompany", c_company);
            }
            else{
                student.remove("CurrentCompany");
            }

            student.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    dlg.dismiss();
                    Intent intent = new Intent(ProfileStudent.this, ProfileStudent.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


    private void enableFields(){
        Button editButton= (Button) findViewById(R.id.editButton);
        editButton.setVisibility(View.GONE);

        LocationView.setFocusable(true);
        LocationView.setFocusableInTouchMode(true);
        LocationView.setClickable(true);
        LocationView.setEnabled(true);

        IndustryView.setFocusable(true);
        IndustryView.setFocusableInTouchMode(true);
        IndustryView.setClickable(true);
        IndustryView.setEnabled(true);

        DateOfBirthView.setFocusable(true);
        DateOfBirthView.setFocusableInTouchMode(true);
        DateOfBirthView.setClickable(true);

        PlaceOfBirthView.setFocusable(true);
        PlaceOfBirthView.setFocusableInTouchMode(true);
        PlaceOfBirthView.setClickable(true);

        DescriptionView.setFocusable(true);
        DescriptionView.setFocusableInTouchMode(true);
        DescriptionView.setClickable(true);

        TechnicalSkillsView.setFocusable(true);
        TechnicalSkillsView.setFocusableInTouchMode(true);
        TechnicalSkillsView.setClickable(true);


        language_eng.setClickable(true);
        language_fra.setClickable(true);
        language_ger.setClickable(true);
        language_ita.setClickable(true);
        language_man.setClickable(true);
        language_por.setClickable(true);
        language_spa.setClickable(true);

        InterestsView.setFocusable(true);
        InterestsView.setFocusableInTouchMode(true);
        InterestsView.setClickable(true);

        YearOfExpView.setFocusable(true);
        YearOfExpView.setFocusableInTouchMode(true);
        YearOfExpView.setClickable(true);

        TypeOfDegreeView.setFocusable(true);
        TypeOfDegreeView.setFocusableInTouchMode(true);
        TypeOfDegreeView.setClickable(true);
        TypeOfDegreeView.setEnabled(true);

        PhoneNumberView.setFocusable(true);
        PhoneNumberView.setFocusableInTouchMode(true);
        PhoneNumberView.setClickable(true);

        CurrentCompanyView.setFocusable(true);
        CurrentCompanyView.setFocusableInTouchMode(true);
        CurrentCompanyView.setClickable(true);
        CurrentCompanyView.setEnabled(true);

        isEditable = true;

    }

    private void disableFields(){
        NameView.setFocusable(false);
        NameView.setFocusableInTouchMode(false);
        NameView.setClickable(false);

        SurnameView.setFocusable(false);
        SurnameView.setFocusableInTouchMode(false);
        SurnameView.setClickable(false);

        LocationView.setFocusable(false);
        LocationView.setFocusableInTouchMode(false);
        LocationView.setClickable(false);
        LocationView.setEnabled(false);


        IndustryView.setFocusable(false);
        IndustryView.setFocusableInTouchMode(false);
        IndustryView.setClickable(false);
        IndustryView.setEnabled(false);

        DateOfBirthView.setFocusable(false);
        DateOfBirthView.setFocusableInTouchMode(false);
        DateOfBirthView.setClickable(false);


        PlaceOfBirthView.setFocusable(false);
        PlaceOfBirthView.setFocusableInTouchMode(false);
        PlaceOfBirthView.setClickable(false);

        DescriptionView.setFocusable(false);
        DescriptionView.setFocusableInTouchMode(false);
        DescriptionView.setClickable(false);


        TechnicalSkillsView.setFocusable(false);
        TechnicalSkillsView.setFocusableInTouchMode(false);
        TechnicalSkillsView.setClickable(false);

        language_eng.setClickable(false);
        language_fra.setClickable(false);
        language_ger.setClickable(false);
        language_ita.setClickable(false);
        language_man.setClickable(false);
        language_por.setClickable(false);
        language_spa.setClickable(false);

        InterestsView.setFocusable(false);
        InterestsView.setFocusableInTouchMode(false);
        InterestsView.setClickable(false);

        YearOfExpView.setFocusable(false);
        YearOfExpView.setFocusableInTouchMode(false);
        YearOfExpView.setClickable(false);

        TypeOfDegreeView.setFocusable(false);
        TypeOfDegreeView.setFocusableInTouchMode(false);
        TypeOfDegreeView.setClickable(false);
        TypeOfDegreeView.setEnabled(false);

        PhoneNumberView.setFocusable(false);
        PhoneNumberView.setFocusableInTouchMode(false);
        PhoneNumberView.setClickable(false);


        CurrentCompanyView.setFocusable(false);
        CurrentCompanyView.setFocusableInTouchMode(false);
        CurrentCompanyView.setClickable(false);
        CurrentCompanyView.setEnabled(false);

    }

    public void deleteProfile(View view) {

        try {
            //
                /*Parse.initialize(this, "H9NFC1K9LmahxGcCrMOdT0qMaE0lDGT6BgbrSOAc", "4K2VfxRGIyk69KlQJ2B8NMnD71llrlkEPLdTNh9M");
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("objectId", "2AM7fmxH5Sk");
                ParseUser user = query.getFirst();*/
            //

            ParseQuery<ParseObject> queryStudent = ParseQuery.getQuery("Student");
            queryStudent.include("StudentId");
            queryStudent.whereEqualTo("StudentId", ParseUser.getCurrentUser());

            ParseObject student = queryStudent.getFirst();

            ParseQuery<ParseObject> queryApplyJob = ParseQuery.getQuery("ApplyJob");
            queryApplyJob.include("StudentId");
            queryApplyJob.whereEqualTo("StudentId", student);

            List<ParseObject> resultsApplyJob = queryApplyJob.find();
            for (ParseObject p : resultsApplyJob) {
                p.delete();
            }

            ParseQuery<ParseObject> querySavedCompany = ParseQuery.getQuery("SavedCompany");
            querySavedCompany.include("StudentId");
            querySavedCompany.whereEqualTo("StudentId", student);

            List<ParseObject> resultsSavedCompany = querySavedCompany.find();
            for (ParseObject p : resultsSavedCompany) {
                p.delete();
            }

            ParseQuery<ParseObject> querySavedJobOffer = ParseQuery.getQuery("SavedJobOffer");
            querySavedJobOffer.include("StudentId");
            querySavedJobOffer.whereEqualTo("StudentId", student);

            List<ParseObject> resultsSavedJobOffer = querySavedJobOffer.find();
            for (ParseObject p : resultsSavedJobOffer) {
                p.delete();
            }

            ParseQuery<ParseObject> querySavedStudent = ParseQuery.getQuery("SavedStudent");
            querySavedStudent.include("StudentId");
            querySavedStudent.whereEqualTo("StudentId", student);

            List<ParseObject> resultsSavedStudent = querySavedStudent.find();
            for (ParseObject p : resultsSavedStudent) {
                p.delete();
            }

            ParseQuery<ParseObject> queryMessage = ParseQuery.getQuery("Message");
            queryMessage.whereEqualTo("SenderId", ParseUser.getCurrentUser());
            List<ParseObject> resultsMessage = queryMessage.find();
            for (ParseObject p : resultsMessage) {
                p.delete();
            }

            queryMessage = ParseQuery.getQuery("Message");
            queryMessage.whereEqualTo("ReceiverIds", ParseUser.getCurrentUser().getObjectId());
            resultsMessage = queryMessage.find();
            ArrayList<String> id = new ArrayList<>();
            id.add(ParseUser.getCurrentUser().getObjectId());
            for (ParseObject p : resultsMessage) {
                //p.delete();
                p.removeAll("ReceiverIds", id);
                p.save();
            }

            student.delete();
            ParseUser.getCurrentUser().delete();
            //String objectId = ParseUser.getCurrentUser().getObjectId();
            ParseUser.logOut();



            /*ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("objectId", objectId);
            ParseUser user = query.getFirst();
            user.delete();*/

            startActivity(new Intent(this, ManageSession.class));


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
