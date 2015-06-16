package com.example.arosales.mobileappproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class PublishNotice extends AppCompatActivity {

    private static final String CATEGORY = "com.example.arosales.getrent.CATEGORY";
    private static final String DESCRIPTION = "com.example.arosales.getrent.DESCRIPTION";
    private static final String TITLE = "com.example.arosales.getrent.TITLE";
    private static final String PHOTO = "com.example.arosales.getrent.PHOTO";
    private static final int REQUEST_IMAGE_GET = 1;

    private ArrayAdapter<String> adapterCategories;
    private ImageView image;
    private String path = "";
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_notice);

        image = (ImageView) findViewById(R.id.noticeImage);

        Spinner type = (Spinner) findViewById(R.id.spinnerCategory);
        adapterCategories = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.arrayNotices));
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapterCategories);

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        EditText title = (EditText) findViewById(R.id.textName);
        Spinner category = (Spinner) findViewById(R.id.spinnerCategory);
        EditText description = (EditText) findViewById(R.id.descriptionText);


        if(!category.getSelectedItem().toString().equals("-")) {
            outState.putString(CATEGORY, category.getSelectedItem().toString());
        }
        outState.putString(TITLE, title.getText().toString());
        outState.putString(DESCRIPTION, description.getText().toString());
        if(bitmap!=null){
            outState.putParcelable(PHOTO, bitmap);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ImageView photo = (ImageView) findViewById(R.id.noticeImage);
        EditText title = (EditText) findViewById(R.id.textName);
        Spinner category = (Spinner) findViewById(R.id.spinnerCategory);
        EditText description = (EditText) findViewById(R.id.descriptionText);

        if(savedInstanceState.containsKey(CATEGORY)) {
            category.setSelection(adapterCategories.getPosition(savedInstanceState.getString(CATEGORY)));
        }
        description.setText(savedInstanceState.getString(DESCRIPTION));
        title.setText(savedInstanceState.getString(TITLE));

        if(savedInstanceState.containsKey(PHOTO)){
            bitmap = savedInstanceState.getParcelable(PHOTO);
            photo.setImageBitmap(bitmap);
        }

    }

    public void uploadPhotos(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK){
            if(data.getData() != null){
                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                path = cursor.getString(columnIndex);
                cursor.close();
                final int THUMBNAIL_SIZE = 64;

                try {
                    InputStream is = getContentResolver().openInputStream(selectedImage);
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    bitmap = Bitmap.createScaledBitmap(bitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

                    ExifInterface ei = new ExifInterface(path);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    Matrix matrix = new Matrix();
                    switch (orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            break;
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, Noticeboard.class);
        startActivity(intent);
    }

    public void publishNotice(View view) {

        EditText title = (EditText) findViewById(R.id.textName);
        Spinner category = (Spinner) findViewById(R.id.spinnerCategory);
        EditText description = (EditText) findViewById(R.id.descriptionText);

        // Validate the inserted data
        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getResources().getString(R.string.error_intro)+"\n");
        if (category.getSelectedItem().toString().equals("-")) {
            validationError = true;
            validationErrorMessage.append(getResources().getString(R.string.error_invalid_notice_category)+"\n");
        }
        if (title.getText().toString().equals("")) {
            validationError = true;
            validationErrorMessage.append(getResources().getString(R.string.error_invalid_title)+"\n");
        }
        if (description.getText().toString().equals("")) {
            validationError = true;
            validationErrorMessage.append(getResources().getString(R.string.error_insert_description)+"\n");
        }

        // If there is a validation error, display the error
        if (validationError) {
            Toast.makeText(PublishNotice.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up a progress dialog
        final ProgressDialog dlg = new ProgressDialog(PublishNotice.this);
        dlg.setTitle(R.string.publication_progress);
        dlg.setMessage(getString(R.string.publish_notice_message));
        dlg.show();

        //save the data to parse.com

        try {
            ParseObject notice = new ParseObject("Notice");
            ParseQuery studentQuery= new ParseQuery("Student");
            studentQuery.whereEqualTo("StudentId", ParseUser.getCurrentUser());
            ParseObject student=studentQuery.getFirst();

            notice.put("StudentId", student);

            notice.put("Title", title.getText().toString());
            notice.put("Category", category.getSelectedItem().toString());
            notice.put("Description", description.getText().toString());

            if(bitmap!=null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
                byte[] imageData = baos.toByteArray();
                ParseFile parseFile = new ParseFile("Photo", imageData);
                parseFile.save();
                notice.put("Photo", parseFile);
            }

            notice.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException e) {
                    dlg.dismiss();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, Noticeboard.class);
        startActivity(intent);
    }
}
