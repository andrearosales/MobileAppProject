package com.example.arosales.mobileappproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class Noticeboard extends AppCompatActivity {

    private ArrayAdapter<String> adapterCategories;
    private Spinner sort;
    private ArrayList<Notice> listNotices;
    private NoticeAdapter noticeAdapter;
    private ListView list_notices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticeboard);

        adapterCategories = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayNotices));
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /*listNotices = new ArrayList<>();

        noticeAdapter = new NoticeAdapter(Noticeboard.this, listNotices);

        list_notices = (ListView) findViewById(R.id.listNotices);

        sort = new Spinner(Noticeboard.this);
        sort.setAdapter(adapterCategories);

        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new RetrieveFromDatabase().execute(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        list_notices.addHeaderView(sort);
        list_notices.setAdapter(noticeAdapter);*/
        new RetrieveFromDatabase().execute(0);

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

    public void publishNotice(View view) {
        Intent intent= new Intent(this,PublishNotice.class);
        startActivity(intent);
    }

    private class RetrieveFromDatabase extends AsyncTask<Integer,Void,ArrayList<Notice>> {

        private ProgressDialog progressDialog= new ProgressDialog(Noticeboard.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Loading notices");
            if(!progressDialog.isShowing()){
                progressDialog.show();
            }
        }

        @Override
        protected ArrayList<Notice> doInBackground(Integer... params) {
            ArrayList<Notice> result_notices=new ArrayList<Notice>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Notice");
            query.include("StudentId");
            try {
                List<ParseObject> results=query.find();
                for(ParseObject p:results){
                    Notice notice = new Notice();
                    notice.setId(p.getObjectId());
                    notice.setTitle(p.getString("Title"));
                    notice.setCategory(p.getString("Category"));
                    notice.setDescription(p.getString("Description"));

                    ParseObject student = p.getParseObject("StudentId");
                    Student publisher = new Student();
                    publisher.setId(student.getObjectId());
                    if(student.getString("PhoneNumber")!=null)
                        publisher.setPhonenumber(student.getString("PhoneNumber"));
                    if(student.getString("Email")!=null)
                        publisher.setEmail(student.getString("Email"));

                    notice.setPublisher(publisher);

                    if(p.getParseFile("Photo")!=null)
                        notice.setPhoto(p.getParseFile("Photo"));

                    result_notices.add(notice);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return result_notices;
        }

        @Override
        protected void onPostExecute(ArrayList<Notice> notices) {
            super.onPostExecute(notices);
            if(progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            noticeAdapter = new NoticeAdapter(Noticeboard.this, notices);

            list_notices = (ListView) findViewById(R.id.listNotices);

            sort = new Spinner(Noticeboard.this);
            sort.setAdapter(adapterCategories);

            sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    noticeAdapter.modifySearch(adapterCategories.getItem(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            list_notices.addHeaderView(sort);
            list_notices.setAdapter(noticeAdapter);

        }
    }

}
