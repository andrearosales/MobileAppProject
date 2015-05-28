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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class SearchStudents extends AppCompatActivity {

    public static final String HASHMAP = "com.example.arosales.mobileappproject.HASHMAP";
    public final static String INFO_SEARCHTYPE = "com.example.arosales.mobileappproject.SEARCHTYPE";
    public static final String NAME = "com.example.arosales.mobileappproject.STUDENTNAME";
    public static final String SURNAME = "com.example.arosales.mobileappproject.STUDENTSURNAME";
    public static final String LOCATION = "com.example.arosales.mobileappproject.STUDENTLOCATION";
    public static final String INDUSTRY = "com.example.arosales.mobileappproject.STUDENTINDUSTRY";
    public static final String TECHSKILLS = "com.example.arosales.mobileappproject.STUDENTTECHSKILLS";
    public static final String EXPERIENCE = "com.example.arosales.mobileappproject.STUDENTEXPERIENCE";
    public static final String DEGREE = "com.example.arosales.mobileappproject.STUDENTDEGREE";
    public static final String INTERESTS = "com.example.arosales.mobileappproject.STUDENTINTERESTS";
    public static final String LANGUAGES = "com.example.arosales.mobileappproject.STUDENTLANGUAGES";
    public static final String CHECK = "com.example.arosales.mobileappproject.STUDENTCHECK";
    public static final String CHECK_ENGLISH = "com.example.arosales.mobileappproject.STUDENTCHECKENGLISH";
    public static final String CHECK_FRENCH = "com.example.arosales.mobileappproject.STUDENTCHECKFRENCH";
    public static final String CHECK_GERMAN = "com.example.arosales.mobileappproject.STUDENTCHECKGERMAN";
    public static final String CHECK_ITALIAN = "com.example.arosales.mobileappproject.STUDENTCHECKITALIAN";
    public static final String CHECK_MANDARIN = "com.example.arosales.mobileappproject.STUDENTCHECKMANDARIN";
    public static final String CHECK_PORTUGUESE = "com.example.arosales.mobileappproject.STUDENTCHECKPORTUGUESE";
    public static final String CHECK_SPANISH = "com.example.arosales.mobileappproject.STUDENTCHECKSPANISH";


    private ArrayAdapter<String> adapterIndustry;
    private ArrayAdapter<String> adapterExperience;
    private ArrayAdapter<String> adapterDegree;
    private ArrayAdapter<String> adapterLocation;

    public void search(View view) {
        HashMap<String, String> filters = new HashMap<>();

        EditText name = (EditText) findViewById(R.id.textName);
        EditText surname = (EditText) findViewById(R.id.textSurname);
        Spinner location = (Spinner) findViewById(R.id.spinnerLocation);
        Spinner industry = (Spinner) findViewById(R.id.spinnerIndustry);
        EditText techSkills = (EditText) findViewById(R.id.textTechSkills);
        Spinner experience = (Spinner) findViewById(R.id.spinnerExperience);
        Spinner degree = (Spinner) findViewById(R.id.spinnerDegree);
        EditText interests = (EditText) findViewById(R.id.textInterests);
        CheckBox check = (CheckBox) findViewById(R.id.checkBoxAvailability);
        CheckBox check_english = (CheckBox) findViewById(R.id.checkbox_english);
        CheckBox check_french = (CheckBox) findViewById(R.id.checkbox_french);
        CheckBox check_german = (CheckBox) findViewById(R.id.checkbox_german);
        CheckBox check_italian = (CheckBox) findViewById(R.id.checkbox_italian);
        CheckBox check_mandarin = (CheckBox) findViewById(R.id.checkbox_mandarin);
        CheckBox check_portuguese = (CheckBox) findViewById(R.id.checkbox_portuguese);
        CheckBox check_spanish = (CheckBox) findViewById(R.id.checkbox_spanish);


        filters.put(INFO_SEARCHTYPE,"Search");


        if(!name.getText().toString().equals("")) {
            filters.put(NAME,name.getText().toString().toLowerCase());
        }
        if(!surname.getText().toString().equals("")) {
            filters.put(SURNAME,surname.getText().toString().toLowerCase());
        }
        if(!location.getSelectedItem().toString().equals("-")) {
            filters.put(LOCATION,location.getSelectedItem().toString());
        }
        if(!industry.getSelectedItem().toString().equals("-")) {
            filters.put(INDUSTRY, industry.getSelectedItem().toString());
        }
        if(!techSkills.getText().toString().equals("")) {
            filters.put(TECHSKILLS,techSkills.getText().toString().toLowerCase());
        }
        if(!experience.getSelectedItem().toString().equals("-")) {
            filters.put(EXPERIENCE, experience.getSelectedItem().toString());
        }
        if(!degree.getSelectedItem().toString().equals("-")) {
            filters.put(DEGREE, degree.getSelectedItem().toString());
        }
        if(!interests.getText().toString().equals("")) {
            filters.put(INTERESTS,interests.getText().toString().toLowerCase());
        }

        List<String> list_languages=new ArrayList<String>();
        if(check_english.isChecked()) {
            list_languages.add("English");
        }
        if(check_french.isChecked()) {
            list_languages.add("French");
        }
        if(check_german.isChecked()) {
            list_languages.add("German");
        }
        if(check_italian.isChecked()) {
            list_languages.add("Italian");
        }
        if(check_mandarin.isChecked()) {
            list_languages.add("Mandarin");
        }
        if(check_portuguese.isChecked()) {
            list_languages.add("Portuguese");
        }
        if(check_spanish.isChecked()) {
            list_languages.add("Spanish");
        }

        if(list_languages.size()>0){
            String langs="";
            for(int i=0;i<list_languages.size();i++){
                langs+=list_languages.get(i);
                if(i<list_languages.size()-1){
                    langs+=",";
                }
            }
            filters.put(LANGUAGES,langs);
        }



        if(check.isChecked()) {
            filters.put(CHECK, "TRUE");
        }

        Intent intent = new Intent(this, ResultStudents.class);
        intent.putExtra(HASHMAP,filters);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_students);

        Spinner industry = (Spinner) findViewById(R.id.spinnerIndustry);
        Spinner location = (Spinner) findViewById(R.id.spinnerLocation);
        Spinner experience = (Spinner) findViewById(R.id.spinnerExperience);
        Spinner degree = (Spinner) findViewById(R.id.spinnerDegree);

        adapterIndustry = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayIndustry));
        adapterIndustry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLocation = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayLocation));
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterExperience = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayExperience));
        adapterExperience.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDegree = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayDegree));
        adapterDegree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        industry.setAdapter(adapterIndustry);
        location.setAdapter(adapterLocation);
        experience.setAdapter(adapterExperience);
        degree.setAdapter(adapterDegree);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        EditText name = (EditText) findViewById(R.id.textName);
        EditText surname = (EditText) findViewById(R.id.textSurname);
        Spinner location = (Spinner) findViewById(R.id.spinnerLocation);
        Spinner industry = (Spinner) findViewById(R.id.spinnerIndustry);
        EditText techSkills = (EditText) findViewById(R.id.textTechSkills);
        Spinner experience = (Spinner) findViewById(R.id.spinnerExperience);
        Spinner degree = (Spinner) findViewById(R.id.spinnerDegree);
        EditText interests = (EditText) findViewById(R.id.textInterests);
        CheckBox check = (CheckBox) findViewById(R.id.checkBoxAvailability);
        CheckBox check_english = (CheckBox) findViewById(R.id.checkbox_english);
        CheckBox check_french = (CheckBox) findViewById(R.id.checkbox_french);
        CheckBox check_german = (CheckBox) findViewById(R.id.checkbox_german);
        CheckBox check_italian = (CheckBox) findViewById(R.id.checkbox_italian);
        CheckBox check_mandarin = (CheckBox) findViewById(R.id.checkbox_mandarin);
        CheckBox check_portuguese = (CheckBox) findViewById(R.id.checkbox_portuguese);
        CheckBox check_spanish = (CheckBox) findViewById(R.id.checkbox_spanish);

        savedInstanceState.putString(NAME, name.getText().toString());
        savedInstanceState.putString(SURNAME, surname.getText().toString());
        if(!location.getSelectedItem().toString().equals("-")) {
            savedInstanceState.putString(LOCATION, location.getSelectedItem().toString());
        }
        if(!industry.getSelectedItem().toString().equals("-")) {
            savedInstanceState.putString(INDUSTRY, industry.getSelectedItem().toString());
        }
        savedInstanceState.putString(TECHSKILLS, techSkills.getText().toString());
        if(!experience.getSelectedItem().toString().equals("-")) {
            savedInstanceState.putString(EXPERIENCE, experience.getSelectedItem().toString());
        }
        if(!degree.getSelectedItem().toString().equals("-")) {
            savedInstanceState.putString(DEGREE, degree.getSelectedItem().toString());
        }
        savedInstanceState.putString(INTERESTS, interests.getText().toString());

        if(check_english.isChecked()) {
            savedInstanceState.putString(CHECK_ENGLISH, "TRUE");
        }

        if(check_french.isChecked()) {
            savedInstanceState.putString(CHECK_FRENCH, "TRUE");
        }

        if(check_german.isChecked()) {
            savedInstanceState.putString(CHECK_GERMAN, "TRUE");
        }

        if(check_italian.isChecked()) {
            savedInstanceState.putString(CHECK_ITALIAN, "TRUE");
        }

        if(check_mandarin.isChecked()) {
            savedInstanceState.putString(CHECK_MANDARIN, "TRUE");
        }

        if(check_portuguese.isChecked()) {
            savedInstanceState.putString(CHECK_PORTUGUESE, "TRUE");
        }

        if(check_spanish.isChecked()) {
            savedInstanceState.putString(CHECK_SPANISH, "TRUE");
        }

        if(check.isChecked()) {
            savedInstanceState.putString(CHECK, "TRUE");
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    public void goSavedStudents(View view){
        Intent intent = new Intent(this, ResultStudents.class);

        HashMap<String,String> search_filters= new HashMap<String,String>();
        search_filters.put(INFO_SEARCHTYPE,"Saved Students");

        Bundle b = new Bundle();
        b.putSerializable(HASHMAP,search_filters);
        intent.putExtras(b);
        startActivity(intent);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        EditText name = (EditText) findViewById(R.id.textName);
        EditText surname = (EditText) findViewById(R.id.textSurname);
        Spinner location = (Spinner) findViewById(R.id.spinnerLocation);
        Spinner industry = (Spinner) findViewById(R.id.spinnerIndustry);
        EditText techSkills = (EditText) findViewById(R.id.textTechSkills);
        Spinner experience = (Spinner) findViewById(R.id.spinnerExperience);
        Spinner degree = (Spinner) findViewById(R.id.spinnerDegree);
        EditText interests = (EditText) findViewById(R.id.textInterests);
        CheckBox check = (CheckBox) findViewById(R.id.checkBoxAvailability);
        CheckBox check_english = (CheckBox) findViewById(R.id.checkbox_english);
        CheckBox check_french = (CheckBox) findViewById(R.id.checkbox_french);
        CheckBox check_german = (CheckBox) findViewById(R.id.checkbox_german);
        CheckBox check_italian = (CheckBox) findViewById(R.id.checkbox_italian);
        CheckBox check_mandarin = (CheckBox) findViewById(R.id.checkbox_mandarin);
        CheckBox check_portuguese = (CheckBox) findViewById(R.id.checkbox_portuguese);
        CheckBox check_spanish = (CheckBox) findViewById(R.id.checkbox_spanish);

        name.setText(savedInstanceState.getString(NAME));
        surname.setText(savedInstanceState.getString(SURNAME));
        if(savedInstanceState.containsKey(LOCATION)) {
            location.setSelection(adapterLocation.getPosition(savedInstanceState.getString(LOCATION)));
        }
        if(savedInstanceState.containsKey(INDUSTRY)) {
            industry.setSelection(adapterIndustry.getPosition(savedInstanceState.getString(INDUSTRY)));
        }
        techSkills.setText(savedInstanceState.getString(TECHSKILLS));
        if(savedInstanceState.containsKey(EXPERIENCE)) {
            experience.setSelection(adapterExperience.getPosition(savedInstanceState.getString(EXPERIENCE)));
        }
        if(savedInstanceState.containsKey(DEGREE)) {
            degree.setSelection(adapterDegree.getPosition(savedInstanceState.getString(DEGREE)));
        }
        interests.setText(savedInstanceState.getString(INTERESTS));

        if(savedInstanceState.containsKey(CHECK_ENGLISH)){
            check_english.setSelected(true);
        }

        if(savedInstanceState.containsKey(CHECK_FRENCH)){
            check_french.setSelected(true);
        }

        if(savedInstanceState.containsKey(CHECK_GERMAN)){
            check_german.setSelected(true);
        }

        if(savedInstanceState.containsKey(CHECK_ITALIAN)){
            check_italian.setSelected(true);
        }

        if(savedInstanceState.containsKey(CHECK_PORTUGUESE)){
            check_portuguese.setSelected(true);
        }

        if(savedInstanceState.containsKey(CHECK_MANDARIN)){
            check_mandarin.setSelected(true);
        }

        if(savedInstanceState.containsKey(CHECK_SPANISH)){
            check_spanish.setSelected(true);
        }

        if(savedInstanceState.containsKey(CHECK)) {
            check.setSelected(true);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_students, menu);
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
