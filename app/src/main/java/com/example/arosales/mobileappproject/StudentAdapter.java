package com.example.arosales.mobileappproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aRosales on 28/05/2015.
 */
public class StudentAdapter extends BaseAdapter {

    public static final String SEARCH_TYPE = "com.example.arosales.mobileappproject.SEARCH_TYPE";
    public static final String STUDENT = "com.example.arosales.mobileappproject.STUDENT";
    private List<Student> listStudents;
    private Activity activity;
    private String searchType;
    private BaseAdapter adapter;
    private LayoutInflater inflater;

    public StudentAdapter(Activity activity, ArrayList<Student> listStudents, String searchType) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listStudents = listStudents;
        this.activity = activity;
        this.searchType = searchType;
        this.adapter = this;
    }

    @Override
    public int getCount() {
        return listStudents.size();
    }

    @Override
    public Student getItem(int position) {
        return listStudents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder vholder;
        View v = convertView;

        if (listStudents.size() > 0) {
            if (v == null) {
                v = inflater.inflate(R.layout.list_students_item, parent, false);
                vholder = new ViewHolder();
                vholder.textName = (TextView) v.findViewById(R.id.studentName);
                vholder.textDescription = (TextView) v.findViewById(R.id.studentIndustry);
                vholder.textIndustry = (TextView) v.findViewById(R.id.studentDescription);
                vholder.buttonView = (Button) v.findViewById(R.id.viewButton);
                vholder.buttonSaveDelete = (Button) v.findViewById(R.id.saveDeleteButton);
                v.setTag(vholder);
            } else {
                vholder = (ViewHolder) v.getTag();
            }

            vholder.textName.setText(listStudents.get(position).getName().substring(0, 1).toUpperCase() + listStudents.get(position).getName().substring(1).toLowerCase()+
                    " "+listStudents.get(position).getSurname().substring(0,1).toUpperCase()+listStudents.get(position).getSurname().substring(1).toLowerCase());

            if (listStudents.get(position).getDescription() != null && !listStudents.get(position).getDescription().equals("")) {
                if (listStudents.get(position).getDescription().length() > 78)
                    vholder.textDescription.setText(listStudents.get(position).getDescription().substring(0, 78) + "...");
                else
                    vholder.textDescription.setText(listStudents.get(position).getDescription());
            }

            if (listStudents.get(position).getIndustry() != null && !listStudents.get(position).getIndustry().equals(""))
                vholder.textIndustry.setText(listStudents.get(position).getIndustry().substring(0, 1).toUpperCase() + listStudents.get(position).getIndustry().substring(1));

            if (searchType.equals("Search"))
                vholder.buttonSaveDelete.setText(R.string.save_button);
            else
                vholder.buttonSaveDelete.setText(R.string.remove_button);

            vholder.buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ViewStudent.class);
                    intent.putExtra(SEARCH_TYPE, searchType);
                    intent.putExtra(STUDENT, listStudents.get(position));
                    activity.startActivity(intent);
                }
            });

            vholder.buttonSaveDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        /*ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("objectId", "2AM7fmxH5S");
                        ParseUser user = query.getFirst();*/


                        ParseQuery<ParseObject> queryCompany = ParseQuery.getQuery("Company");
                        queryCompany.include("CompanyId");
                        queryCompany.whereEqualTo("CompanyId", ParseUser.getCurrentUser());

                        ParseQuery<ParseObject> queryStudent = ParseQuery.getQuery("Student");
                        queryStudent.include("StudentId");
                        queryStudent.include("CurrentCompany");
                        queryStudent.whereEqualTo("objectId", listStudents.get(position).getId());

                        ParseObject student = queryStudent.getFirst();
                        ParseObject company = queryCompany.getFirst();

                        ParseQuery<ParseObject> querySavedStudent = ParseQuery.getQuery("SavedStudent");
                        querySavedStudent.whereEqualTo("StudentId", student);
                        querySavedStudent.whereEqualTo("CompanyId", company);

                        if (searchType.equals("Search")) {
                            String message = null;
                            if (querySavedStudent.count() == 0) {
                                ParseObject savedStudent = new ParseObject("SavedStudent");
                                savedStudent.put("StudentId", student);
                                savedStudent.put("CompanyId", company);
                                savedStudent.saveInBackground();
                                message = activity.getString(R.string.addedSavedStudentMessage);
                            } else {
                                message = activity.getString(R.string.existingSavedStudentMessage);

                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Save students");
                            builder.setMessage(message);
                            builder.setCancelable(true);
                            builder.setNeutralButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            querySavedStudent.getFirst().deleteInBackground();

                            String message = activity.getString(R.string.removedSavedJobMessage);

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Remove job");
                            builder.setMessage(message);
                            builder.setCancelable(true);
                            builder.setNeutralButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            listStudents.remove(position);
                                            adapter.notifyDataSetChanged();
                                        }
                                    });

                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });


        }

        return v;
    }

    public static class ViewHolder {
        public TextView textName;
        public TextView textDescription;
        public TextView textIndustry;
        public Button buttonView;
        public Button buttonSaveDelete;
    }
}

