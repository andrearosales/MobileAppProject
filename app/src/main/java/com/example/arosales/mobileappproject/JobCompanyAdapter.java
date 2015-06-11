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

import java.util.List;
/**
 * Created by aRosales on 28/05/2015.
 */
public class JobCompanyAdapter extends BaseAdapter implements View.OnClickListener{
    public static final String JOB = "com.example.arosales.mobileappproject.JOB";;
    private LayoutInflater inflater;
    private Activity activity;
    private List<Job> listJobs;
    private BaseAdapter adapter;

    public JobCompanyAdapter(Activity activity, List<Job> listJobs) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.listJobs = listJobs;
        adapter=this;
    }

    @Override
    public int getCount() {
        return listJobs.size();
    }

    @Override
    public Object getItem(int position) {
        return listJobs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vholder;
        View v = convertView;

        if (listJobs.size() > 0) {
            if (v == null) {
                v = inflater.inflate(R.layout.list_offers_item, parent, false);
                vholder = new ViewHolder();
                vholder.jobPosition = (TextView) v.findViewById(R.id.jobPosition);
                vholder.jobLocation = (TextView) v.findViewById(R.id.jobLocation);
                vholder.buttonEdit = (Button) v.findViewById(R.id.editButton);
                vholder.buttonRemove = (Button) v.findViewById(R.id.removeButton);
                v.setTag(vholder);
            } else {
                vholder = (ViewHolder) v.getTag();
            }
            if(listJobs.get(position).getPosition()!=null)
                vholder.jobPosition.setText(listJobs.get(position).getPosition());
            else
                vholder.jobPosition.setText("");
            if(listJobs.get(position).getLocation()!=null)
                vholder.jobLocation.setText(listJobs.get(position).getLocation());
            else
                vholder.jobLocation.setText("");

            vholder.buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, UpdateOffer.class);
                    intent.putExtra(JOB, listJobs.get(position));
                    activity.startActivity(intent);
                }
            });

            vholder.buttonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        /*
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("objectId", "2AM7fmxH5S");
                        ParseUser user = query.getFirst();*/

                        ParseQuery<ParseObject> queryJob = ParseQuery.getQuery("JobOffer");
                        queryJob.whereEqualTo("objectId", listJobs.get(position).getId());
                        ParseObject job = queryJob.getFirst();

                        ParseQuery<ParseObject> queryApply = ParseQuery.getQuery("ApplyJob");
                        queryApply.include("JobId");
                        queryApply.whereEqualTo("JobId", job);
                        List<ParseObject> resultsApplyJob=queryApply.find();
                        for(ParseObject p:resultsApplyJob){
                            p.deleteInBackground();
                        }

                        ParseQuery<ParseObject> querySaved = ParseQuery.getQuery("SavedJobOffer");
                        querySaved.include("OfferId");
                        querySaved.whereEqualTo("OfferId", job);
                        List<ParseObject> resultsSavedJobOffer=querySaved.find();
                        for(ParseObject p:resultsSavedJobOffer){
                            p.deleteInBackground();
                        }

                        job.deleteInBackground();

                        String message = activity.getString(R.string.removedSavedJobMessage);

                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Remove job");
                        builder.setMessage(message);
                        builder.setCancelable(true);
                        builder.setNeutralButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        listJobs.remove(position);
                                        adapter.notifyDataSetChanged();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return v;
    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder {
        public TextView jobPosition;
        public TextView jobLocation;
        public Button buttonEdit;
        public Button buttonRemove;
    }

}

