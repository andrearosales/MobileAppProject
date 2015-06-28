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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class ResultStudents extends AppCompatActivity {

    private String searchType;
    public static final String STUDENT = "com.example.arosales.mobileappproject.STUDENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_students);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        new RetrieveFromDatabase().execute((HashMap<String, String>) b.getSerializable(SearchStudents.HASHMAP));

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


    private class RetrieveFromDatabase extends AsyncTask<HashMap<String, String>, Void, ArrayList<Student>> {

        private ProgressDialog progressDialog = new ProgressDialog(ResultStudents.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lockScreenOrientation();
            progressDialog.setTitle("Loading students");
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        @Override
        protected ArrayList<Student> doInBackground(HashMap<String, String>... params) {


            ArrayList<Student> students = new ArrayList<Student>();
            HashMap<String, String> search_data = params[0];
            searchType=search_data.get(SearchStudents.INFO_SEARCHTYPE);

            if (search_data.get(SearchStudents.INFO_SEARCHTYPE).equals("Search")) {
                //Search
                ParseQuery<ParseObject> searchStudentQuery = ParseQuery.getQuery("Student");

                //Name filter
                if(search_data.containsKey(SearchStudents.NAME)){
                    searchStudentQuery.whereContains("Name",search_data.get(SearchStudents.NAME));
                }

                //Surname filter
                if(search_data.containsKey(SearchStudents.SURNAME)){
                    searchStudentQuery.whereContains("Surname",search_data.get(SearchStudents.SURNAME));
                }

                //Location Filter
                if (search_data.containsKey(SearchStudents.LOCATION)) {
                    searchStudentQuery.whereEqualTo("Location", search_data.get(SearchStudents.LOCATION));
                }

                //Industry Filter
                if (search_data.containsKey(SearchStudents.INDUSTRY)) {
                    searchStudentQuery.whereEqualTo("Industry", search_data.get(SearchStudents.INDUSTRY));
                }

                //Technical Skills
                if(search_data.containsKey(SearchStudents.TECHSKILLS)){
                    HashSet<String> techskills= new HashSet<String>();
                    List<String> initial_techskills = Arrays.asList(search_data.get(SearchStudents.TECHSKILLS).replaceAll("\\s+","").split(","));
                    ParseQuery<ParseObject> searchTechSkillsQuery = ParseQuery.getQuery("TechnicalSkill");
                    for(String s:initial_techskills){
                        searchTechSkillsQuery.whereContains("Keyword",s);
                        try {
                            List<ParseObject> tech_results=searchTechSkillsQuery.find();
                            for(ParseObject p:tech_results){
                                techskills.addAll((ArrayList<String>) p.get("Associatedvalues"));
                            }
                            techskills.addAll(initial_techskills);
                            List<String> final_techskills = new ArrayList<String>(techskills);
                            searchStudentQuery.whereContainedIn("TechnicalSkills",final_techskills);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                }

                //Experience Years Filter
                if (search_data.containsKey(SearchStudents.EXPERIENCE)) {
                    String[] arrayExperience = getResources().getStringArray(R.array.arrayExperience);
                    if(search_data.get(SearchStudents.EXPERIENCE).equals(arrayExperience[1])){
                        searchStudentQuery.whereEqualTo("ExperienceYears",0);
                    }else if(search_data.get(SearchStudents.EXPERIENCE).equals(arrayExperience[2])){
                        searchStudentQuery.whereEqualTo("ExperienceYears",1);
                    }else if(search_data.get(SearchStudents.EXPERIENCE).equals(arrayExperience[3])){
                        searchStudentQuery.whereEqualTo("ExperienceYears",2);
                    }else if(search_data.get(SearchStudents.EXPERIENCE).equals(arrayExperience[4])){
                        searchStudentQuery.whereGreaterThanOrEqualTo("ExperienceYears",3);
                    }
                }

                //Degree filter
                if (search_data.containsKey(SearchStudents.DEGREE)) {
                    searchStudentQuery.whereEqualTo("TypeOfDegree",search_data.get(SearchStudents.DEGREE));
                }

                //Interests filter
                if(search_data.containsKey(SearchStudents.INTERESTS)){
                    List<String> interests = Arrays.asList(search_data.get(SearchStudents.INTERESTS).replaceAll("\\s+","").split(","));
                    searchStudentQuery.whereContainsAll("Interests",interests);
                }

                if(search_data.containsKey(SearchStudents.LANGUAGES)){

                    List<String> languages = Arrays.asList(search_data.get(SearchStudents.LANGUAGES).split(","));
                    searchStudentQuery.whereContainsAll("Languages",languages);
                }


                try {
                    searchStudentQuery.include("CurrentCompany");
                    List<ParseObject> results=searchStudentQuery.find();

                    for(ParseObject parseStudent:results){
                        ParseObject current_company=parseStudent.getParseObject("CurrentCompany");
                        if(current_company==null){
                            Student student= new Student();
                            student.setId(parseStudent.getObjectId());
                            if(parseStudent.get("Name")!=null){
                                student.setName(parseStudent.getString("Name").toUpperCase());
                            }
                            if(parseStudent.get("Surname")!=null){
                                student.setSurname(parseStudent.getString("Surname").toUpperCase());
                            }
                            if(parseStudent.get("Location")!=null){
                                student.setLocation(parseStudent.getString("Location"));
                            }
                            if(parseStudent.get("Industry")!=null){
                                student.setIndustry(parseStudent.getString("Industry"));
                            }
                            if(parseStudent.get("Birthdate")!=null){
                                student.setBirthdate(parseStudent.getDate("Birthdate"));
                            }
                            if(parseStudent.get("Birthplace")!=null){
                                student.setBirthplace(parseStudent.getString("Birthplace"));
                            }
                            if(parseStudent.get("Description")!=null){
                                student.setDescription(parseStudent.getString("Description"));
                            }
                            if(parseStudent.get("ExperienceYears")!=null){
                                student.setExperienceyears(parseStudent.getInt("ExperienceYears"));
                            }
                            if(parseStudent.get("Languages")!=null){
                                ArrayList<String> languages = (ArrayList<String>)parseStudent.get("Languages");
                                student.setLanguages(languages);
                            }
                            if(parseStudent.get("PhoneNumber")!=null){
                                student.setPhonenumber(parseStudent.getString("PhoneNumber"));
                            }
                            if(parseStudent.get("TechnicalSkills")!=null){
                                ArrayList<String> techskills = (ArrayList<String>)parseStudent.get("TechnicalSkills");
                                student.setSkills(techskills);
                            }
                            if(parseStudent.get("Interests")!=null){
                                ArrayList<String> interests = (ArrayList<String>)parseStudent.get("Interests");
                                student.setInterests(interests);
                            }
                            if(parseStudent.get("TypeOfDegree")!=null){
                                student.setDegree(parseStudent.getString("TypeOfDegree"));
                            }
                            students.add(student);
                        }
                    }

                    if(!search_data.containsKey(SearchStudents.CHECK)){
                        Log.d("Size", String.valueOf(results.size()));
                        for(ParseObject parseStudent:results){
                            ParseObject current_company=parseStudent.getParseObject("CurrentCompany");
                            if(current_company!=null){
                                Student student= new Student();
                                student.setId(parseStudent.getObjectId());
                                if(parseStudent.get("Name")!=null){
                                    student.setName(parseStudent.getString("Name").toUpperCase());
                                }
                                if(parseStudent.get("Surname")!=null){
                                    student.setSurname(parseStudent.getString("Surname").toUpperCase());
                                }
                                if(parseStudent.get("Location")!=null){
                                    student.setLocation(parseStudent.getString("Location"));
                                }
                                if(parseStudent.get("Industry")!=null){
                                    student.setIndustry(parseStudent.getString("Industry"));
                                }
                                if(parseStudent.get("Birthdate")!=null){
                                    student.setBirthdate(parseStudent.getDate("Birthdate"));
                                }
                                if(parseStudent.get("Birthplace")!=null){
                                    student.setBirthplace(parseStudent.getString("Birthplace"));
                                }
                                if(parseStudent.get("Description")!=null){
                                    student.setDescription(parseStudent.getString("Description"));
                                }
                                if(parseStudent.get("ExperienceYears")!=null){
                                    student.setExperienceyears(parseStudent.getInt("ExperienceYears"));
                                }
                                if(parseStudent.get("Languages")!=null){
                                    ArrayList<String> languages = (ArrayList<String>)parseStudent.get("Languages");
                                    student.setLanguages(languages);
                                }
                                if(parseStudent.get("PhoneNumber")!=null){
                                    student.setPhonenumber(parseStudent.getString("PhoneNumber"));
                                }
                                if(parseStudent.get("TechnicalSkills")!=null){
                                    ArrayList<String> techskills = (ArrayList<String>)parseStudent.get("TechnicalSkills");
                                    student.setSkills(techskills);
                                }
                                if(parseStudent.get("Interests")!=null){
                                    ArrayList<String> interests = (ArrayList<String>)parseStudent.get("Interests");
                                    student.setInterests(interests);
                                }
                                if(parseStudent.get("TypeOfDegree")!=null){
                                    student.setDegree(parseStudent.getString("TypeOfDegree"));
                                }

                                Company company = new Company();
                                if (current_company.get("CompanyId") != null)
                                    company.setId(current_company.getObjectId().toString());
                                if (current_company.get("Name") != null)
                                    company.setName(current_company.get("Name").toString());
                                if (current_company.get("Location") != null)
                                    company.setLocation(current_company.get("Location").toString());
                                if (current_company.get("Address") != null)
                                    company.setAddress(current_company.get("Address").toString());
                                if (current_company.get("Industry") != null)
                                    company.setIndustry(current_company.get("Industry").toString());
                                if (current_company.get("Description") != null)
                                    company.setDescription(current_company.get("Description").toString());
                                if (current_company.get("Size") != null)
                                    company.setCompany_size(current_company.getInt("Size"));
                                if (current_company.get("Website") != null)
                                    company.setWebsite(current_company.get("Website").toString());
                                if (current_company.get("Clients") != null)
                                    company.setClients(current_company.get("Clients").toString());


                                student.setCurrent_company(company);
                                students.add(student);
                            }
                        }

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            } else {
                //Saved Students

                //Retrieve rows from SavedStudent table with the CompanyId equals to the id of the current user
                ParseQuery<ParseObject> savedStudentQuery = ParseQuery.getQuery("SavedStudent");
                ParseQuery<ParseObject> companyQuery = ParseQuery.getQuery("Company");

                try {
                    /*
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("objectId", "2AM7fmxH5S");
                    ParseUser user = query.getFirst();*/


                    companyQuery.include("CompanyId");
                    companyQuery.whereEqualTo("CompanyId", ParseUser.getCurrentUser());

                    ParseObject company_result = companyQuery.getFirst();

                    savedStudentQuery.include("CompanyId");
                    savedStudentQuery.whereEqualTo("CompanyId", company_result);


                    savedStudentQuery.include("StudentId");
                    savedStudentQuery.include("StudentId.CurrentCompany");

                    List<ParseObject> results = savedStudentQuery.find();

                    for (ParseObject p : results) {

                        //For each offer in the SavedStudent, retrieve the data contained in the Student table with all the information
                        // of the specific student
                        ParseObject student_result = p.getParseObject("StudentId");

                        Student student= new Student();
                        student.setId(student_result.getObjectId());
                        if(student_result.get("Name")!=null){
                            student.setName(student_result.getString("Name"));
                        }
                        if(student_result.get("Surname")!=null){
                            student.setSurname(student_result.getString("Surname"));
                        }
                        if(student_result.get("Location")!=null){
                            student.setLocation(student_result.getString("Location"));
                        }
                        if(student_result.get("Industry")!=null){
                            student.setIndustry(student_result.getString("Industry"));
                        }
                        if(student_result.get("Birthdate")!=null){
                            student.setBirthdate(student_result.getDate("Birthdate"));
                        }
                        if(student_result.get("Birthplace")!=null){
                            student.setBirthplace(student_result.getString("Birthplace"));
                        }
                        if(student_result.get("Description")!=null){
                            student.setDescription(student_result.getString("Description"));
                        }
                        if(student_result.get("ExperienceYears")!=null){
                            student.setExperienceyears(student_result.getInt("ExperienceYears"));
                        }
                        if(student_result.get("Languages")!=null){
                            ArrayList<String> languages = (ArrayList<String>)student_result.get("Languages");
                            student.setLanguages(languages);
                        }
                        if(student_result.get("PhoneNumber")!=null){
                            student.setPhonenumber(student_result.getString("PhoneNumber"));
                        }
                        if(student_result.get("TechnicalSkills")!=null){
                            ArrayList<String> techskills = (ArrayList<String>)student_result.get("TechnicalSkills");
                            student.setSkills(techskills);
                        }
                        if(student_result.get("Interests")!=null){
                            ArrayList<String> interests = (ArrayList<String>)student_result.get("Interests");
                            student.setInterests(interests);
                        }
                        if(student_result.get("TypeOfDegree")!=null){
                            student.setDegree(student_result.getString("TypeOfDegree"));
                        }



                        //Each student id is related with a company through the currentcompany. Retrieve the company related
                        //by searching it on the Company table
                        ParseObject current_company = student_result.getParseObject("CurrentCompany");

                        if(current_company!=null) {

                            Company company = new Company();
                            if (current_company.get("CompanyId") != null)
                                company.setId(current_company.getObjectId().toString());
                            if (current_company.get("Name") != null)
                                company.setName(current_company.get("Name").toString());
                            if (current_company.get("Location") != null)
                                company.setLocation(current_company.get("Location").toString());
                            if (current_company.get("Address") != null)
                                company.setAddress(current_company.get("Address").toString());
                            if (current_company.get("Industry") != null)
                                company.setIndustry(current_company.get("Industry").toString());
                            if (current_company.get("Description") != null)
                                company.setDescription(current_company.get("Description").toString());
                            if (current_company.get("Size") != null)
                                company.setCompany_size(current_company.getInt("Size"));
                            if (current_company.get("Website") != null)
                                company.setWebsite(current_company.get("Website").toString());
                            if (current_company.get("Clients") != null)
                                company.setClients(current_company.get("Clients").toString());

                            student.setCurrent_company(company);
                        }
                        students.add(student);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return students;
        }

        @Override
        protected void onPostExecute(ArrayList<Student> students) {
            super.onPostExecute(students);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            unlockScreenOrientation();

            StudentAdapter sAdapter = new StudentAdapter(ResultStudents.this, students,searchType);

            ListView list_students = (ListView) findViewById(R.id.listResults);

            Button newSearchButton = new Button(ResultStudents.this);

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
                    Intent intent= new Intent(ResultStudents.this,SearchStudents.class);
                    startActivity(intent);
                }
            });

            list_students.addFooterView(newSearchButton);

            list_students.setAdapter(sAdapter);
            list_students.setEmptyView(findViewById(R.id.emptyView));


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
        Intent intent= new Intent(ResultStudents.this,SearchStudents.class);
        startActivity(intent);
    }
}
