package com.example.arosales.mobileappproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
/**
 * Created by aRosales on 28/05/2015.
 */
public class ApplicantAdapter extends BaseAdapter implements View.OnClickListener{

    public static final String APPLICANT = "com.example.arosales.mobileappproject.APPLICANT";
    private LayoutInflater inflater;
    private Activity activity;
    private List<Application> listApplications;

    public ApplicantAdapter(Activity activity, List<Application> listApplications) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.listApplications = listApplications;
    }

    @Override
    public int getCount() {
        return listApplications.size();
    }

    @Override
    public Object getItem(int position) {
        return listApplications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vholder;
        View v = convertView;

        if (listApplications.size() > 0) {
            if (v == null) {
                v = inflater.inflate(R.layout.applicant_row, parent, false);
                vholder = new ViewHolder();
                vholder.textPosition = (TextView) v.findViewById(R.id.textPosition);
                vholder.textStudent = (TextView) v.findViewById(R.id.textStudent);
                vholder.textDescription = (TextView) v.findViewById(R.id.textDescription);
                v.setTag(vholder);
            } else {
                vholder = (ViewHolder) v.getTag();
            }
            vholder.textPosition.setText(listApplications.get(position).getPosition());
            String name = listApplications.get(position).getStudent().getName() + " " + listApplications.get(position).getStudent().getSurname();
            if(name.length()>78)
                vholder.textStudent.setText(name.substring(0, 78) + "...");
            else
                vholder.textStudent.setText(name);
            if(listApplications.get(position).getStudent().getDescription()!=null){
                if (listApplications.get(position).getStudent().getDescription().length() > 78)
                    vholder.textDescription.setText(listApplications.get(position).getStudent().getDescription().substring(0, 78) + "...");
                else
                    vholder.textDescription.setText(listApplications.get(position).getStudent().getDescription());
            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(activity,ApplicantDescription.class);
                    intent.putExtra(APPLICANT, listApplications.get(position));
                    activity.startActivity(intent);
                }
            });
        }
        return v;
    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder {
        public TextView textPosition;
        public TextView textStudent;
        public TextView textDescription;
    }


}
