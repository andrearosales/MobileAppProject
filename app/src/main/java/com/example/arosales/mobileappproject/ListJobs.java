package com.example.arosales.mobileappproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ListJobs extends AppCompatActivity {

    public static final String APPLICATION_ID = "qT7ozC6SpBUGiaKxHQwZHRyNfT0GX2xECCVsJYyv";
    public static final String CLIENT_KEY = "jLWrQSMm4uC97tkzwPZLeJ3000GQS8cDm65uXQnc";

    public void publishOffer (View view){
        Intent intent = new Intent(this, PublishOffer.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_jobs);


        try {
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
    public boolean onCreateOptionsMenu (Menu menu){
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

        private ProgressDialog progressDialog= new ProgressDialog(ListJobs.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Loading job offers");
            if(!progressDialog.isShowing()){
                progressDialog.show();
            }
        }

        @Override
        protected ArrayList<Job> doInBackground(ParseObject... params) {
            ArrayList<Job> result_jobs=new ArrayList<Job>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("JobOffer");
            query.include("CompanyId");
            query.whereEqualTo("CompanyId", params[0]);
            try {
                List<ParseObject> results=query.find();
                for(ParseObject p:results){
                    Job job = new Job();
                    job.setId(p.getObjectId());
                    if(p.getString("Position")!=null)
                        job.setPosition(p.getString("Position"));
                    if(p.getString("Industry")!=null)
                        job.setIndustry(p.getString("Industry"));
                    if(p.getString("Description")!=null)
                        job.setDescription(p.getString("Description"));
                    if(p.getString("Location")!=null)
                        job.setLocation(p.getString("Location"));
                    if (p.getNumber("Salary")!=null)
                        job.setSalary(p.getNumber("Salary").toString());
                    if(p.getString("JobType")!=null)
                        job.setTypeJob(p.getString("JobType"));
                    if(p.getString("ContractType")!=null)
                        job.setContractType(p.getString("ContractType"));
                    if(p.getNumber("Duration")!=null)
                        job.setDuration(p.getNumber("Duration").intValue());
                    if(p.getDate("createdAt")!=null)
                        job.setDate(p.getDate("createdAt").toString());
                    ParseObject currentC = p.getParseObject("CompanyId");
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
                        job.setCompany(company);
                    }

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

            JobCompanyAdapter jAdapter= new JobCompanyAdapter(ListJobs.this,jobs);

            ListView list_jobs= (ListView) findViewById(R.id.listResult);

            final Button publishOffer = new Button(ListJobs.this);
            Drawable background = getResources().getDrawable(R.drawable.background_color);

            if (android.os.Build.VERSION.SDK_INT >= 16)
                publishOffer.setBackground(background);
            else
                publishOffer.setBackgroundDrawable(background);

            publishOffer.setHeight(getResources().getDimensionPixelSize(R.dimen.button_height));
            publishOffer.setWidth(getResources().getDimensionPixelSize(R.dimen.width_buttons));
            publishOffer.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size));
            publishOffer.setTextColor(Color.WHITE);
            publishOffer.setTypeface(null, Typeface.BOLD);
            publishOffer.setText(getResources().getString(R.string.publish_offer));

            publishOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    publishOffer(publishOffer);
                }
            });

            list_jobs.addFooterView(publishOffer);

            list_jobs.setAdapter(jAdapter);
            list_jobs.setEmptyView(findViewById(R.id.emptyView));

        }
    }

}
