package com.example.arosales.mobileappproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class ListApplication extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_application);

        try {
            //
                /*Parse.initialize(this, "H9NFC1K9LmahxGcCrMOdT0qMaE0lDGT6BgbrSOAc", "4K2VfxRGIyk69KlQJ2B8NMnD71llrlkEPLdTNh9M");
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("objectId", "2AM7fmxH5Sk");
                ParseUser user = query.getFirst();*/
            //

            ParseQuery<ParseObject> queryStudent = ParseQuery.getQuery("Student");
            queryStudent.include("StudentId");
            queryStudent.include("CurrentCompany");
            queryStudent.whereEqualTo("StudentId", ParseUser.getCurrentUser());

            ParseObject student = queryStudent.getFirst();

            new RetrieveFromDatabase().execute(student);

        } catch (ParseException e) {
            e.printStackTrace();
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
            ParseUser.logOut();
            Intent intent= new Intent(this,LogIn.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class RetrieveFromDatabase extends AsyncTask<ParseObject,Void,ArrayList<Job>> {

        private ProgressDialog progressDialog= new ProgressDialog(ListApplication.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Loading applications");
            if(!progressDialog.isShowing()){
                progressDialog.show();
            }
        }

        @Override
        protected ArrayList<Job> doInBackground(ParseObject... params) {
            ArrayList<Job> result_jobs=new ArrayList<Job>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ApplyJob");
            query.include("StudentId");
            query.include("JobId");
            query.include("JobId.CompanyId");
            query.whereEqualTo("StudentId",params[0]);
            try {
                List<ParseObject> results=query.find();
                for(ParseObject p:results){

                    ParseObject job_result = p.getParseObject("JobId");

                    Job job = new Job();
                    job.setId(job_result.getObjectId());
                    if (job_result.get("Position") != null)
                        job.setPosition(job_result.get("Position").toString());
                    if (job_result.get("Industry") != null)
                        job.setIndustry(job_result.get("Industry").toString());
                    if (job_result.get("Description") != null)
                        job.setDescription(job_result.get("Description").toString());
                    if (job_result.get("Location") != null)
                        job.setLocation(job_result.get("Location").toString());
                    if (job_result.get("Salary") != null)
                        job.setSalary(job_result.get("Salary").toString());
                    if (job_result.get("JobType") != null)
                        job.setTypeJob(job_result.get("JobType").toString());
                    if (job_result.get("Duration") != null)
                        job.setDuration(job_result.getInt("Duration"));
                    if (job_result.get("ContractType") != null)
                        job.setContractType(job_result.get("ContractType").toString());
                    if (job_result.getCreatedAt() != null)
                        job.setDate(job_result.getCreatedAt().toString());

                    ParseObject company_result = job_result.getParseObject("CompanyId");
                    Company company = new Company();
                    if (company_result.get("CompanyId") != null)
                        company.setId(company_result.getObjectId());
                    if (company_result.get("Name") != null)
                        company.setName(company_result.get("Name").toString());
                    if (company_result.get("Location") != null)
                        company.setLocation(company_result.get("Location").toString());
                    if (company_result.get("Address") != null)
                        company.setAddress(company_result.get("Address").toString());
                    if (company_result.get("Industry") != null)
                        company.setIndustry(company_result.get("Industry").toString());
                    if (company_result.get("Description") != null)
                        company.setDescription(company_result.get("Description").toString());
                    if (company_result.get("Size") != null)
                        company.setCompany_size(company_result.getInt("Size"));
                    if (company_result.get("Website") != null)
                        company.setWebsite(company_result.get("Website").toString());
                    if (company_result.get("Clients") != null)
                        company.setClients(company_result.get("Clients").toString());

                    job.setCompany(company);

                    result_jobs.add(job);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return result_jobs;
        }

        @Override
        protected void onPostExecute(ArrayList<Job> jobs) {
            super.onPostExecute(jobs);
            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            ApplicationAdapter jAdapter= new ApplicationAdapter(ListApplication.this,jobs);

            ListView list_applications= (ListView) findViewById(R.id.listApplications);
            list_applications.setAdapter(jAdapter);
            list_applications.setEmptyView(findViewById(R.id.emptyView));

        }
    }

}
