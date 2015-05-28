package com.example.arosales.mobileappproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ListApplicant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_applicant);

        try {
            //
                /*Parse.initialize(this, "H9NFC1K9LmahxGcCrMOdT0qMaE0lDGT6BgbrSOAc", "4K2VfxRGIyk69KlQJ2B8NMnD71llrlkEPLdTNh9M");
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("objectId", "y7djJxGraH");
                ParseUser user = query.getFirst();*/
            //

            ParseQuery<ParseObject> queryStudent = ParseQuery.getQuery("Company");
            queryStudent.include("CompanyId");
            queryStudent.whereEqualTo("CompanyId", ParseUser.getCurrentUser());

            ParseObject company = queryStudent.getFirst();


            new RetrieveFromDatabase().execute(company);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_applicant, menu);
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

    private class RetrieveFromDatabase extends AsyncTask<ParseObject,Void,ArrayList<Application>> {

        private ProgressDialog progressDialog= new ProgressDialog(ListApplicant.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Loading applicants");
            if(!progressDialog.isShowing()){
                progressDialog.show();
            }
        }

        @Override
        protected ArrayList<Application> doInBackground(ParseObject... params) {
            ArrayList<Application> result_applications=new ArrayList<Application>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplyJob");
            query.include("StudentId");
            query.include("StudentId.CurrentCompany");
            query.include("JobId");
            query.include("JobId.CompanyId");
            //query.whereEqualTo("JobId.CompanyId", params[0]);
            try {
                List<ParseObject> results=query.find();
                for(ParseObject p:results){

                    ParseObject student_result = p.getParseObject("StudentId");
                    ParseObject job_result = p.getParseObject("JobId");

                    ParseObject companyCurrent = job_result.getParseObject("CompanyId");

                    //
                    /*
                    Log.d("companyID params ",params[0].getObjectId());
                    Log.d("student_result ",student_result.getObjectId());

                    Log.d("job_result ",job_result.getObjectId());
                    Log.d("companyCurrent de job ",companyCurrent.getObjectId());*/
                    //

                    if(companyCurrent!=null && companyCurrent.getObjectId().equals(params[0].getObjectId())){

                        Application app = new Application();
                        app.setPosition(job_result.getString("Position"));
                        Student student = new Student();
                        if(student_result.getObjectId() != null)
                            student.setId(student_result.getObjectId());
                        if(student_result.getString("Name")!=null)
                            student.setName(student_result.getString("Name").toUpperCase());
                        if(student_result.getString("Surname")!=null)
                            student.setSurname(student_result.getString("Surname").toUpperCase());
                        if(student_result.getString("Location")!=null)
                            student.setLocation(student_result.getString("Location"));
                        if(student_result.getString("Industry")!=null)
                            student.setIndustry(student_result.getString("Industry"));
                        if(student_result.getDate("Birthdate")!=null)
                            student.setBirthdate(student_result.getDate("Birthdate"));
                        if(student_result.getString("Birthplace")!=null)
                            student.setBirthplace(student_result.getString("Birthplace"));
                        if(student_result.getString("Description")!=null)
                            student.setDescription(student_result.getString("Description"));
                        if(student_result.getString("TypeOfDegree")!=null)
                            student.setDegree(student_result.getString("TypeOfDegree"));
                        if(student_result.getNumber("PhoneNumber")!=null)
                            student.setPhonenumber(student_result.getNumber("PhoneNumber").toString());
                        if(student_result.getNumber("ExperienceYears")!=null)
                            student.setExperienceyears(student_result.getNumber("ExperienceYears").intValue());

                        ParseObject currentC = student_result.getParseObject("CurrentCompany");
                        if(currentC!=null){
                            Company company = new Company();
                            if (currentC.get("CompanyId") != null)
                                company.setId(currentC.getObjectId());
                            if (currentC.get("Name") != null)
                                company.setName(currentC.get("Name").toString());
                            if (currentC.get("Location") != null)
                                company.setLocation(currentC.get("Location").toString());
                            if (currentC.get("Address") != null)
                                company.setAddress(currentC.get("Address").toString());
                            if (currentC.get("Industry") != null)
                                company.setIndustry(currentC.get("Industry").toString());
                            if (currentC.get("Description") != null)
                                company.setDescription(currentC.get("Description").toString());
                            if (currentC.get("Size") != null)
                                company.setCompany_size(currentC.getInt("Size"));
                            if (currentC.get("Website") != null)
                                company.setWebsite(currentC.get("Website").toString());
                            if (currentC.get("Clients") != null)
                                company.setClients(currentC.get("Clients").toString());
                            student.setCurrent_company(company);
                        }
                        else
                            student.setCurrent_company(null);

                        if(student_result.get("TechnicalSkills")!=null)
                            student.setSkills((ArrayList<String>) student_result.get("TechnicalSkills"));
                        if(student_result.get("Languages")!=null)
                            student.setLanguages((ArrayList<String>) student_result.get("Languages"));
                        if(student_result.get("Interests")!=null)
                            student.setInterests((ArrayList<String>) student_result.get("Interests"));

                        app.setStudent(student);
                        result_applications.add(app);
                    }


                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return result_applications;
        }

        @Override
        protected void onPostExecute(ArrayList<Application> aps) {
            super.onPostExecute(aps);
            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            ApplicantAdapter jAdapter= new ApplicantAdapter(ListApplicant.this,aps);

            ListView list_applicants= (ListView) findViewById(R.id.listApplicants);
            list_applicants.setAdapter(jAdapter);
            list_applicants.setEmptyView(findViewById(R.id.emptyView));

        }
    }

}
