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
 * Created by ricardogarcia on 12/06/15.
 */
public class ConsultingHourAdapter extends BaseAdapter{

    private static final String CONSULTING_HOUR = "com.example.arosales.mobileappproject.CONSULTING_HOUR";
    private LayoutInflater inflater;
    private Activity activity;
    private List<ConsultingHour> listHours;
    private BaseAdapter adapter;

    public ConsultingHourAdapter(Activity activity, ArrayList list) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.listHours = list;
        adapter = this;
    }

    @Override
    public int getCount() {
        return listHours.size();
    }

    @Override
    public Object getItem(int position) {
        return listHours.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ParseUser currentUser = ParseUser.getCurrentUser();
        String typeUser = currentUser.getString("TypeUser");
        if (typeUser.equals("Student")) {

            ViewHolder vholder;
            View v = convertView;

            if (listHours.size() > 0) {
                if (v == null) {
                    v = inflater.inflate(R.layout.consultinghour_row, parent, false);
                    vholder = new ViewHolder();
                    vholder.textCourse = (TextView) v.findViewById(R.id.textCourse);
                    vholder.textTime = (TextView) v.findViewById(R.id.textTime);
                    vholder.textPlace = (TextView) v.findViewById(R.id.textPlace);
                    vholder.buttonEdit = (Button) v.findViewById(R.id.buttonEdit);
                    v.setTag(vholder);
                } else {
                    vholder = (ViewHolder) v.getTag();
                }

                vholder.textCourse.setText(listHours.get(position).getCourse().getName());
                if (listHours.get(position).getTime() != null && !listHours.get(position).getTime().equals(""))
                    vholder.textTime.setText(listHours.get(position).getTime());
                else
                    vholder.textTime.setText("");

                if (listHours.get(position).getPlace()!= null && !listHours.get(position).getPlace().equals(""))
                    vholder.textPlace.setText(listHours.get(position).getPlace());
                else
                    vholder.textPlace.setText("");

                vholder.buttonEdit.setVisibility(View.INVISIBLE);

            }
            return v;

        } else if (typeUser.equals("Teacher")) {

            ViewHolder vholder;
            View v = convertView;

            if (listHours.size() > 0) {
                if (v == null) {
                    v = inflater.inflate(R.layout.consultinghour_row, parent, false);
                    vholder = new ViewHolder();
                    vholder.textCourse = (TextView) v.findViewById(R.id.textCourse);
                    vholder.textTime = (TextView) v.findViewById(R.id.textTime);
                    vholder.textPlace = (TextView) v.findViewById(R.id.textPlace);
                    vholder.buttonEdit = (Button) v.findViewById(R.id.buttonEdit);
                    v.setTag(vholder);
                } else {
                    vholder = (ViewHolder) v.getTag();
                }

                vholder.textCourse.setText(listHours.get(position).getCourse().getName());
                if (listHours.get(position).getTime() != null && !listHours.get(position).getTime().equals(""))
                    vholder.textTime.setText(listHours.get(position).getTime());
                else
                    vholder.textTime.setText("");

                if (listHours.get(position).getPlace()!= null && !listHours.get(position).getPlace().equals(""))
                    vholder.textPlace.setText(listHours.get(position).getPlace());
                else
                    vholder.textPlace.setText("");


                vholder.buttonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(activity,UpdateConsultingHours.class);
                        intent.putExtra(CONSULTING_HOUR, listHours.get(position));
                        activity.startActivity(intent);
                    }
                });

            }
            return v;

        }

        return null;
    }


    public static class ViewHolder {
        public TextView textCourse;
        public TextView textTime;
        public TextView textPlace;
        public Button buttonEdit;
    }


}
