package com.example.arosales.mobileappproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class ViewNotice extends AppCompatActivity {

    public static final String RECEIVER = "com.example.arosales.mobileappproject.RECEIVER";
    public static final String RECEIVERTYPE = "com.example.arosales.mobileappproject.RECEIVERTYPE";
    public static final String NAMERECEIVER = "com.example.arosales.mobileappproject.NAMERECEIVER";

    private String noticeID;
    private ParseObject notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notice);

        Intent intent = getIntent();
        noticeID = intent.getStringExtra(NoticeAdapter.NOTICE);

        TextView title = (TextView) findViewById(R.id.noticeTitle);
        TextView category = (TextView) findViewById(R.id.noticeCategory);
        TextView description = (TextView) findViewById(R.id.noticeDescription);
        TextView sendMessage = (TextView) findViewById(R.id.sendMessage);
        TextView phone = (TextView) findViewById(R.id.phoneNumber);
        TextView email = (TextView) findViewById(R.id.emailOwner);
        SpannableString content1 = new SpannableString(getResources().getString(R.string.send_button));
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        sendMessage.setText(content1);

        try {
            ParseQuery queryNotice = ParseQuery.getQuery("Notice");
            queryNotice.include("StudentId");
            queryNotice.whereEqualTo("objectId", noticeID);
            notice = queryNotice.getFirst();

            ParseQuery queryStudent = ParseQuery.getQuery("Student");
            queryStudent.include("StudentId");
            queryStudent.whereEqualTo("StudentId", ParseUser.getCurrentUser());
            ParseObject student = queryStudent.getFirst();
            if(!student.getObjectId().equals(notice.getParseObject("StudentId").getObjectId())) {
                Button deleteButton = (Button) findViewById(R.id.delete_button);
                deleteButton.setVisibility(View.GONE);
            }
            title.setText(notice.getString("Title"));
            category.setText(category.getText().toString() + ": " + notice.getString("Category"));
            description.setText(notice.getString("Description"));
            if(notice.getParseObject("StudentId").getString("PhoneNumber")!=null) {
                SpannableString content = new SpannableString(notice.getParseObject("StudentId").getString("PhoneNumber"));
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                phone.setText(content);
                //phone.setText(notice.getPublisher().getPhonenumber());
            }
            else
                phone.setClickable(false);
            if(notice.getParseObject("StudentId").getString("Email")!=null) {
                SpannableString content = new SpannableString(notice.getParseObject("StudentId").getString("Email"));
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                email.setText(content);

                //email.setText(notice.getPublisher().getEmail());
            }
            else
                email.setClickable(false);
            ImageView image = (ImageView) findViewById(R.id.noticeImage);
            if(notice.getParseFile("Photo")!=null){
                try {
                    ParseFile photo = notice.getParseFile("Photo");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(photo.getData(), 0, photo.getData().length);
                    image.setImageBitmap(bitmap);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else
                image.setVisibility(View.GONE);
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
            Utils.unsubscribeCourses();
            ParseUser.logOut();
            Intent intent= new Intent(this,LogIn.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteNotice(View view) {
        try {
            ParseQuery queryNotice = ParseQuery.getQuery("Notice");
            queryNotice.whereEqualTo("objectId", notice.getObjectId());
            ParseObject notice = queryNotice.getFirst();
            notice.deleteInBackground();
            Intent intent = new Intent(this, Noticeboard.class);
            startActivity(intent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, Noticeboard.class);
        startActivity(intent);
    }

    public void dialPhoneNumber(View view) {
        TextView phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        if(phoneNumber.getText()!=null) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber.getText().toString()));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
    public void sendEmail(View view) {
        TextView email = (TextView) findViewById(R.id.emailOwner);
        TextView title = (TextView) findViewById(R.id.noticeTitle);
        String subject = "Contact for notice: "+title.getText().toString();
        if(email.getText()!=null) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + email.getText().toString()));
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, SendMessage.class);
        intent.putExtra(RECEIVER, notice.getParseObject("StudentId").getObjectId());
        intent.putExtra(RECEIVERTYPE, "Student");
        startActivity(intent);
    }
}
