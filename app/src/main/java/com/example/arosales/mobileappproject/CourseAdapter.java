package com.example.arosales.mobileappproject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.parse.ParseUser;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by ricardogarcia on 12/06/15.
 */
public class CourseAdapter extends BaseAdapter {

    public static final String COURSE = "com.example.arosales.mobileappproject.COURSE";
    private LayoutInflater inflater;
    private List<ExtendedLesson> list;
    private Activity activity;


    public CourseAdapter(Activity activity, ArrayList list) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public int getItemViewType(int position) {

        if (list.get(position).getType().equals(Type.DAY_SEPARATOR))
            return 0;
        else if (list.get(position).getType().equals(Type.LESSON))
            return 1;

        return 2;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = this.getItemViewType(position);

        switch (type) {

            case 0:
                ViewHolder holderDay;
                View vDay = convertView;
                vDay = inflater.inflate(R.layout.day_row, parent, false);
                holderDay = new ViewHolder();
                holderDay.text = (TextView) vDay.findViewById(R.id.textDayOfWeek);
                vDay.setTag(holderDay);

                if (list.size() > 0) {
                    ExtendedLesson lesson = list.get(position);
                    String[] shownDays = activity.getResources().getStringArray(R.array.week_days);
                    holderDay.text.setText(shownDays[lesson.getDay() - 2]);
                }

                return vDay;

            case 1:
                ViewHolder holderLesson;
                View vLesson = convertView;
                vLesson = inflater.inflate(R.layout.lesson_row, parent, false);
                holderLesson = new ViewHolder();
                holderLesson.text = (TextView) vLesson.findViewById(R.id.textCourseName);
                holderLesson.lesson_time = (TextView) vLesson.findViewById(R.id.textLessonTime);
                holderLesson.room_text = (TextView) vLesson.findViewById(R.id.textRoom);
                vLesson.setTag(holderLesson);

                if (list.size() > 0) {

                    ExtendedLesson lesson = list.get(position);

                    if (lesson.getCourse().getName() != null && !lesson.getCourse().getName().equals(""))
                        holderLesson.text.setText(lesson.getCourse().getName());

                    if (lesson.getStartTime() != null && lesson.getEndTime() != null) {
                        Time start = new Time(lesson.getStartTime().getTime());
                        Time end = new Time(lesson.getEndTime().getTime());
                        holderLesson.lesson_time.setText(start.toString() + "-" + end.toString());
                    }

                    if (lesson.getRoom() != null && !lesson.getRoom().equals(""))
                        holderLesson.room_text.setText(lesson.getRoom());

                    if (list.get(position).getColor() != null)
                        vLesson.setBackgroundColor(list.get(position).getColor());


                    vLesson.setOnClickListener(new CourseAdapter.OnItemClickListener(position, vLesson, parent));

                }

                return vLesson;
        }

        return null;

    }


    public class OnItemClickListener implements View.OnClickListener {

        private int position;
        private View view;
        private ViewGroup parent;

        OnItemClickListener(int position, View contentView, ViewGroup parent) {
            this.position = position;
            this.view = contentView;
            this.parent = parent;
        }


        @Override
        public void onClick(View v) {

            ParseUser currentUser = ParseUser.getCurrentUser();
            String typeUser = currentUser.getString("TypeUser");
            if (typeUser.equals("Student")) {
                final PopupWindow popupmessage;


                LayoutInflater inflater = activity.getLayoutInflater();
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.lesson_description, (ViewGroup) activity.findViewById(R.id.popup));


                TextView textTeacher = (TextView) layout.findViewById(R.id.textTeacherName);
                TextView textRoom = (TextView) layout.findViewById(R.id.textRoom);
                TextView textTime = (TextView) layout.findViewById(R.id.textTime);
                TextView textCourse = (TextView) layout.findViewById(R.id.textCourseName);

                if (list.get(position).getCourse().getTeacher().getName() != null && !list.get(position).getCourse().getTeacher().getName().equals(""))
                    textTeacher.setText(list.get(position).getCourse().getTeacher().getName() + " " + list.get(position).getCourse().getTeacher().getSurname());

                if (list.get(position).getStartTime() != null && list.get(position).getEndTime() != null) {
                    Time start = new Time(list.get(position).getStartTime().getTime());
                    Time end = new Time(list.get(position).getEndTime().getTime());
                    textTime.setText(start.toString() + "-" + end.toString());
                }

                if (list.get(position).getRoom() != null && !list.get(position).getRoom().equals(""))
                    textRoom.setText(list.get(position).getRoom());

                if (list.get(position).getCourse().getName() != null && !list.get(position).getCourse().getName().equals(""))
                    textCourse.setText(list.get(position).getCourse().getName());

                layout.setPadding(30, 30, 30, 30);

                //Creation of export button
                Button exportButton = new Button(activity);

                exportButton.setText(activity.getString(R.string.export_button));
                exportButton.setBackgroundColor(Color.BLACK);
                exportButton.setTextColor(Color.WHITE);
                exportButton.setTypeface(null, Typeface.BOLD);
                exportButton.setPadding(10, 0, 10, 0);
                exportButton.setHeight(activity.getResources().getDimensionPixelSize(R.dimen.button_height));
                exportButton.setWidth(activity.getResources().getDimensionPixelSize(R.dimen.width_buttons));
                exportButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, activity.getResources().getDimensionPixelSize(R.dimen.text_size));


                layout.addView(exportButton);

                popupmessage = new PopupWindow(layout, (int) activity.getResources().getDimension(R.dimen.width_coursepopup), (int) activity.getResources().getDimension(R.dimen.height_coursepopup));
                popupmessage.setOutsideTouchable(true);
                popupmessage.setBackgroundDrawable(new ShapeDrawable());
                popupmessage.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            popupmessage.dismiss();
                            return true;
                        }
                        return false;
                    }
                });

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupmessage.dismiss();
                    }
                });
                popupmessage.showAtLocation(layout, Gravity.CENTER, 0, 0);

                //Runs after clicking on export button
                exportButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupmessage.dismiss();

                        long calendarId = Utils.searchCalendarId(activity);
                        String message = null;

                        if (calendarId == -1) {
                            //Non existing calendar
                            //Create calendar and add events
                            ContentValues values = new ContentValues();
                            values.put(CalendarContract.Calendars.ACCOUNT_NAME, "local");
                            values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
                            values.put(CalendarContract.Calendars.NAME, "Lecture Timetable Calendar");
                            values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Lecture Timetable Calendar");
                            values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0xffff0000);
                            values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
                            values.put(CalendarContract.Calendars.OWNER_ACCOUNT, "local");
                            values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, "Europe/Rome");
                            values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
                            Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();
                            builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "local");
                            builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
                            builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true");
                            Uri uri = activity.getContentResolver().insert(builder.build(), values);
                            calendarId = Long.valueOf(uri.getLastPathSegment());
                        }


                        //Search events-Add events
                        for (ExtendedLesson exL : list) {
                            if (exL.getCourse().getName().equals(list.get(position).getCourse().getName())) {

                                long event_id = Utils.searchEventOnCalendar(activity, exL.getCourse().getName(), exL.getStartTime(),
                                        exL.getEndTime(), exL.getRoom());
                                if (event_id == -1) {
                                    Calendar cal_start = new GregorianCalendar();
                                    cal_start.setTime(exL.getStartTime());
                                    ContentValues values = new ContentValues();
                                    values.put(CalendarContract.Events.DTSTART, cal_start.getTimeInMillis());

                                    long diff = exL.getEndTime().getTime() - exL.getStartTime().getTime();
                                    long diffSeconds = diff / 1000 % 60;
                                    long diffMinutes = diff / (60 * 1000) % 60;
                                    long diffHours = diff / (60 * 60 * 1000);

                                    values.put(CalendarContract.Events.DURATION, "PT" + String.valueOf(diffHours) + "H" + String.valueOf(diffMinutes) + "M" + String.valueOf(diffSeconds) + "S");
                                    Calendar c = new GregorianCalendar();
                                    c.set(Calendar.DAY_OF_WEEK, exL.getDay());
                                    String dayOfWeek = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
                                    DateFormat first_part = new SimpleDateFormat("yyyyMMdd");
                                    DateFormat second_part = new SimpleDateFormat("HHmmss");
                                    String until = first_part.format(exL.getCourse().getEndDate()) + "T" + second_part.format(exL.getCourse().getEndDate())
                                            + "Z";
                                    values.put(CalendarContract.Events.RRULE, "FREQ=WEEKLY;BYDAY=" + dayOfWeek.toUpperCase().substring(0, 2) + ";WKST=SU;UNTIL=" + until);
                                    values.put(CalendarContract.Events.TITLE, exL.getCourse().getName());
                                    values.put(CalendarContract.Events.EVENT_LOCATION, exL.getRoom());
                                    values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
                                    values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Rome");
                                    values.put(CalendarContract.Events.EVENT_COLOR, list.get(position).getColor());
                                    Uri uri = activity.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
                                    message = list.get(position).getCourse().getName() + " " + activity.getString(R.string.message_course_not_found);
                                } else {
                                    message = list.get(position).getCourse().getName() + " " + activity.getString(R.string.message_course_found);
                                }
                            }

                        }

                        //Alert after clicking on export
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("Export Calendar");
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

                    }
                });

            }else if(typeUser.equals("Teacher")){
                Intent intent= new Intent(activity,ProfessorNotification.class);
                intent.putExtra(COURSE,list.get(position).getCourse());
                activity.startActivity(intent);
            }

        }
    }


    public static class ViewHolder {
        public TextView text;
        public TextView lesson_time;
        public TextView room_text;
    }


}

