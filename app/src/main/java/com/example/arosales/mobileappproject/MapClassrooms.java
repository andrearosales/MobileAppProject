package com.example.arosales.mobileappproject;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class MapClassrooms extends AppCompatActivity {

    private ScrollView mScrollView;
    private String campus;
    private String classroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_classrooms);

        Intent intent=getIntent();
        campus= (String) intent.getSerializableExtra(SearchClassrooms.CAMPUS);
        classroom= (String) intent.getSerializableExtra(SearchClassrooms.CLASSROOM);

        GoogleMap mapFragment = ((ClassroomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        mScrollView = (ScrollView) findViewById(R.id.scroll);

        ParseQuery campusQuery = new ParseQuery("Campus");
        campusQuery.whereEqualTo("Name",campus);

        try {
            ParseObject parseCampus=campusQuery.getFirst();

            if(parseCampus!=null) {
                ParseQuery classroomQuery = new ParseQuery("Classroom");
                classroomQuery.include("CampusId");
                classroomQuery.whereEqualTo("CampusId", parseCampus);
                classroomQuery.whereEqualTo("Name", classroom);
                ParseObject parseClassroom = classroomQuery.getFirst();
                if(parseClassroom!=null){
                    ParseGeoPoint geopoint= parseClassroom.getParseGeoPoint("Location");
                    if(geopoint!=null){
                        mapFragment.addMarker(new MarkerOptions().position(new LatLng(geopoint.getLatitude(),geopoint.getLongitude())).title(classroom));
                        mapFragment.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(geopoint.getLatitude(),geopoint.getLongitude()), 20));
                    }
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        ((ClassroomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).setListener(new ClassroomMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                mScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_classrooms, menu);
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

    public void goBack(View view){
        finish();
    }
}

