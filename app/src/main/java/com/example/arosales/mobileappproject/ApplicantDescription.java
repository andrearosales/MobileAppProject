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


public class ApplicantDescription extends AppCompatActivity {

    private static final String NAME = "com.example.arosales.mobileappproject.STUDENTNAME";
    private static final String INDUSTRY = "com.example.arosales.mobileappproject.STUDENTINDUSTRY";
    private static final String DESCRIPTION = "com.example.arosales.mobileappproject.STUDENTDESCRIPTION";
    private static final String TECHSKILLS = "com.example.arosales.mobileappproject.STUDENTTECHSKILLS";
    private static final String EXPERIENCE = "com.example.arosales.mobileappproject.STUDENTEXPERIENCE";
    private static final String DEGREE = "com.example.arosales.mobileappproject.STUDENTDEGREE";
    private static final String INTERESTS = "com.example.arosales.mobileappproject.STUDENTINTERESTS";
    private static final String COMPANY = "com.example.arosales.mobileappproject.STUDENTCOMPANY";
    private static final String LANGUAGES = "com.example.arosales.mobileappproject.STUDENTLANGUAGES";

    public void goBack(View view) {
        Intent intent= new Intent(this,ListApplicant.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_description);

        Intent intent = getIntent();
        Application application = (Application) intent.getSerializableExtra(ApplicantAdapter.APPLICANT);

        TextView name = (TextView) findViewById(R.id.studentName);
        if(application.getStudent().getName()!=null && application.getStudent().getSurname()!=null)
            name.setText(application.getStudent().getName() + " " + application.getStudent().getSurname());
        TextView description = (TextView) findViewById(R.id.studentDescription);
        if(application.getStudent().getDescription()!=null)
            description.setText(description.getText().toString()+"\n"+application.getStudent().getDescription()+"\n");
        TextView industry = (TextView) findViewById(R.id.studentIndustry);
        if(application.getStudent().getIndustry()!=null)
            industry.setText(industry.getText().toString()+"\n"+application.getStudent().getIndustry()+"\n");
        TextView techSkills = (TextView) findViewById(R.id.techSkills);
        if(application.getStudent().getSkills()!=null) {
            for (int i = 0; i < application.getStudent().getSkills().size(); i++) {
                techSkills.setText(techSkills.getText().toString() + "\n" + application.getStudent().getSkills().get(i));
            }
            techSkills.setText(techSkills.getText().toString() + "\n");
        }
        TextView experience = (TextView) findViewById(R.id.yearsExperience);
        if(application.getStudent().getExperienceyears()>0)
            experience.setText(experience.getText().toString()+ "\n" +Integer.toString(application.getStudent().getExperienceyears())+ "\n");
        TextView degree = (TextView) findViewById(R.id.typeOfDegree);
        if(application.getStudent().getDegree()!=null)
            degree.setText(degree.getText().toString()+ "\n"+application.getStudent().getDegree()+ "\n");
        TextView interests = (TextView) findViewById(R.id.interests);
        if(application.getStudent().getInterests()!=null) {
            for (int i = 0; i < application.getStudent().getInterests().size(); i++) {
                interests.setText(interests.getText().toString() + "\n" + application.getStudent().getInterests().get(i));
            }
            interests.setText(interests.getText().toString() + "\n");
        }
        TextView company = (TextView) findViewById(R.id.currentCompany);
        if(application.getStudent().getCurrent_company()!=null)
            company.setText(company.getText().toString()+ "\n"+ application.getStudent().getCurrent_company().getName()+"\n");
        TextView languages = (TextView) findViewById(R.id.languages);
        if(application.getStudent().getLanguages()!=null) {
            for (int i = 0; i < application.getStudent().getLanguages().size(); i++) {
                languages.setText(languages.getText().toString() + "\n" + application.getStudent().getLanguages().get(i));
            }
            languages.setText(languages.getText().toString() + "\n");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        TextView name = (TextView) findViewById(R.id.studentName);
        TextView description = (TextView) findViewById(R.id.studentDescription);
        TextView industry = (TextView) findViewById(R.id.studentIndustry);
        TextView techSkills = (TextView) findViewById(R.id.techSkills);
        TextView experience = (TextView) findViewById(R.id.yearsExperience);
        TextView degree = (TextView) findViewById(R.id.typeOfDegree);
        TextView interests = (TextView) findViewById(R.id.interests);
        TextView company = (TextView) findViewById(R.id.currentCompany);
        TextView languages = (TextView) findViewById(R.id.languages);

        savedInstanceState.putString(NAME, name.getText().toString());
        savedInstanceState.putString(INDUSTRY, description.getText().toString());
        savedInstanceState.putString(DESCRIPTION, industry.getText().toString());
        savedInstanceState.putString(TECHSKILLS, techSkills.getText().toString());
        savedInstanceState.putString(EXPERIENCE, experience.getText().toString());
        savedInstanceState.putString(DEGREE, degree.getText().toString());
        savedInstanceState.putString(INTERESTS, interests.getText().toString());
        savedInstanceState.putString(COMPANY, company.getText().toString());
        savedInstanceState.putString(LANGUAGES, languages.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        TextView name = (TextView) findViewById(R.id.studentName);
        TextView description = (TextView) findViewById(R.id.studentDescription);
        TextView industry = (TextView) findViewById(R.id.studentIndustry);
        TextView techSkills = (TextView) findViewById(R.id.techSkills);
        TextView experience = (TextView) findViewById(R.id.yearsExperience);
        TextView degree = (TextView) findViewById(R.id.typeOfDegree);
        TextView interests = (TextView) findViewById(R.id.interests);
        TextView company = (TextView) findViewById(R.id.currentCompany);
        TextView languages = (TextView) findViewById(R.id.languages);

        name.setText(savedInstanceState.getString(NAME));
        description.setText(savedInstanceState.getString(DESCRIPTION));
        industry.setText(savedInstanceState.getString(INDUSTRY));
        techSkills.setText(savedInstanceState.getString(TECHSKILLS));
        experience.setText(savedInstanceState.getString(EXPERIENCE));
        degree.setText(savedInstanceState.getString(DEGREE));
        interests.setText(savedInstanceState.getString(INTERESTS));
        company.setText(savedInstanceState.getString(COMPANY));
        languages.setText(savedInstanceState.getString(LANGUAGES));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_applicant_description, menu);
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
