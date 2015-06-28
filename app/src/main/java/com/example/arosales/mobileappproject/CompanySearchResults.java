package com.example.arosales.mobileappproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CompanySearchResults extends AppCompatActivity {

    private String searchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search_results);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        new RetrieveFromDatabase().execute((HashMap<String, String>) b.getSerializable(CompanySearch.INFO_HASH));
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
            Intent intent= new Intent(this, ViewNotifications.class);
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

    private class RetrieveFromDatabase extends AsyncTask<HashMap<String, String>, Void, ArrayList<Company>> {

        private ProgressDialog progressDialog = new ProgressDialog(CompanySearchResults.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lockScreenOrientation();
            progressDialog.setTitle("Loading companies");
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        @Override
        protected ArrayList<Company> doInBackground(HashMap<String, String>... params) {

            ArrayList<Company> companies = new ArrayList<Company>();
            HashMap<String, String> search_data = params[0];

            searchType=search_data.get(CompanySearch.INFO_SEARCHTYPE);

            if (search_data.get(CompanySearch.INFO_SEARCHTYPE).equals("Search")) {
                //Search
                ParseQuery<ParseObject> searchCompanyQuery = ParseQuery.getQuery("Company");

                //Industry Filter
                if (search_data.containsKey(CompanySearch.INFO_INDUSTRY)) {
                    searchCompanyQuery.whereEqualTo("Industry", search_data.get(CompanySearch.INFO_INDUSTRY));
                }

                //Location Filter
                if (search_data.containsKey(CompanySearch.INFO_LOCATION)) {
                    searchCompanyQuery.whereEqualTo("Location", search_data.get(CompanySearch.INFO_LOCATION));
                }

                //Size Filter
                if (search_data.containsKey(CompanySearch.INFO_SIZE)) {
                    String[] arraySize = getResources().getStringArray(R.array.arraySize);
                    if(search_data.get(CompanySearch.INFO_SIZE).equals(arraySize[1]))
                        searchCompanyQuery.whereGreaterThanOrEqualTo("Size", 100);
                    else if(search_data.get(CompanySearch.INFO_SIZE).equals(arraySize[2]))
                        searchCompanyQuery.whereGreaterThanOrEqualTo("Size", 500);
                    else if(search_data.get(CompanySearch.INFO_SIZE).equals(arraySize[3]))
                        searchCompanyQuery.whereGreaterThanOrEqualTo("Size", 1000);
                    else if(search_data.get(CompanySearch.INFO_SIZE).equals(arraySize[4]))
                        searchCompanyQuery.whereGreaterThanOrEqualTo("Size", 10000);
                }

                //Name Filter
                if (search_data.containsKey(CompanySearch.INFO_NAME)) {
                    searchCompanyQuery.whereContains("Name", search_data.get(CompanySearch.INFO_NAME));
                }

                try {
                    List<ParseObject> result_companies = searchCompanyQuery.find();
                    for (ParseObject c : result_companies) {
                        Company company = new Company();
                        if (c.get("CompanyId") != null)
                            company.setId(c.getObjectId());
                        if (c.get("Name") != null)
                            company.setName(c.get("Name").toString());
                        if (c.get("Location") != null)
                            company.setLocation(c.get("Location").toString());
                        if (c.get("Address") != null)
                            company.setAddress(c.get("Address").toString());
                        if (c.get("Industry") != null)
                            company.setIndustry(c.get("Industry").toString());
                        if (c.get("Description") != null)
                            company.setDescription(c.get("Description").toString());
                        if (c.get("Size") != null)
                            company.setCompany_size(c.getInt("Size"));
                        if (c.get("Website") != null)
                            company.setWebsite(c.get("Website").toString());
                        if (c.get("Clients") != null)
                            company.setClients(c.get("Clients").toString());

                        companies.add(company);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else {
                //Saved companies

                ParseQuery<ParseObject> savedCompanyQuery = ParseQuery.getQuery("SavedCompany");
                ParseQuery<ParseObject> studentQuery = ParseQuery.getQuery("Student");


                try {

                    /*
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("objectId", "2AM7fmxH5S");
                    ParseUser user = query.getFirst();*/

                    studentQuery.include("StudentId");
                    studentQuery.whereEqualTo("StudentId", ParseUser.getCurrentUser());

                    ParseObject student_result = studentQuery.getFirst();


                    savedCompanyQuery.include("StudentId");
                    savedCompanyQuery.include("CompanyId");
                    savedCompanyQuery.whereEqualTo("StudentId", student_result);

                    List<ParseObject> results = savedCompanyQuery.find();

                    for (ParseObject parseCompany : results) {
                        ParseObject company_result = parseCompany.getParseObject("CompanyId");

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

                        companies.add(company);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }


            return companies;
        }

        @Override
        protected void onPostExecute(ArrayList<Company> companies) {
            super.onPostExecute(companies);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            unlockScreenOrientation();
            CompanyAdapter cAdapter = new CompanyAdapter(CompanySearchResults.this, companies,searchType);

            ListView list_companies = (ListView) findViewById(R.id.listResults);

            Button newSearchButton = new Button(CompanySearchResults.this);

            Drawable background = getResources().getDrawable(R.drawable.background_color);

            if (android.os.Build.VERSION.SDK_INT >= 16)
                newSearchButton.setBackground(background);
            else
                newSearchButton.setBackgroundDrawable(background);


            newSearchButton.setHeight(getResources().getDimensionPixelSize(R.dimen.button_height));
            newSearchButton.setWidth(getResources().getDimensionPixelSize(R.dimen.width_buttons));
            newSearchButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size));
            newSearchButton.setTextColor(Color.WHITE);
            newSearchButton.setTypeface(null, Typeface.BOLD);

            if(searchType.equals("Search"))
                newSearchButton.setText(getResources().getString(R.string.new_search_button).toUpperCase());
            else
                newSearchButton.setText(getResources().getString(R.string.backsearch_button).toUpperCase());


            newSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(CompanySearchResults.this,CompanySearch.class);
                    startActivity(intent);
                }
            });

            list_companies.addFooterView(newSearchButton);

            list_companies.setAdapter(cAdapter);
            list_companies.setEmptyView(findViewById(R.id.emptyView));


        }
    }

    private void lockScreenOrientation() {
        int orientation = getRequestedOrientation();
        int rotation = ((WindowManager) getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
            case Surface.ROTATION_90:
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;
            case Surface.ROTATION_180:
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            default:
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
        }

        setRequestedOrientation(orientation);
    }

    private void unlockScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    public void newSearch(View view){
        finish();
    }

}
