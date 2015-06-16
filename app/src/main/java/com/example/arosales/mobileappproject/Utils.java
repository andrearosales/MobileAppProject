package com.example.arosales.mobileappproject;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

/**
 * Created by ricardogarcia on 12/03/15.
 */
public class Utils {

    private static HashMap<String, Integer> colors = new HashMap<String, Integer>();
    private static Stack<Integer> predefined_Colors = new Stack<Integer>();

    public static void fillPredefinedColors() {

        predefined_Colors.push(Color.rgb(255, 202, 127));
        predefined_Colors.push(Color.rgb(220, 191, 255));
        predefined_Colors.push(Color.rgb(185, 127, 255));
        predefined_Colors.push(Color.rgb(191, 255, 209));
        predefined_Colors.push(Color.rgb(127, 255, 164));
        predefined_Colors.push(Color.rgb(255, 253, 145));
        predefined_Colors.push(Color.rgb(255, 196, 185));

    }

    public static int retrieveColor(String course_name) {


        int color = 0;
        if (colors.size() <= 0 || !colors.containsKey(course_name)) {
            while (predefined_Colors.size() > 0) {
                color = predefined_Colors.pop();
                if (!colors.containsValue(color)) {
                    colors.put(course_name, color);
                    return color;
                }
            }

            while (true) {
                Random ram = new Random();
                int rColor = ram.nextInt(128) + 127;
                int gColor = ram.nextInt(128) + 127;
                int bColor = ram.nextInt(128) + 127;
                int randomColor = Color.rgb(rColor, gColor, bColor);

                if (!colors.containsValue(randomColor)) {
                    colors.put(course_name, randomColor);
                    return randomColor;
                }
            }
        } else {
            color = colors.get(course_name);
        }

        return color;
    }

    /*
     public static String getDateFromDay(Activity activity, ExtendedLesson lesson) {

         String[] shownDays = activity.getResources().getStringArray(R.array.week_days);
         DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
         return shownDays[lesson.getDay() - 2] + " " + dateFormat.format(lesson.getStartTime());

     }
 */
    public static long searchCalendarId(Activity activity) {

        long calendarId;
        String[] projection = new String[]{CalendarContract.Calendars._ID,
                CalendarContract.Calendars.NAME, CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.ACCOUNT_TYPE};
        Cursor calCursor = activity.getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, projection,
                CalendarContract.Calendars.VISIBLE + "=1", null, CalendarContract.Calendars._ID + " ASC");

        if (calCursor.moveToFirst()) {
            do {
                if (calCursor.getString(1).equals("Lecture Timetable Calendar")) {
                    calendarId = calCursor.getLong(0);
                    return calendarId;
                }
            } while (calCursor.moveToNext());
        }
        return -1;
    }

    public static long searchEventOnCalendar(Activity activity, String name, Date startTime, Date endTime, String room) {
        String[] projections = new String[]{
                CalendarContract.Events._ID
        };

        String selection = "((" + CalendarContract.Events.TITLE + " = ?) AND ("
                + CalendarContract.Events.DTSTART + " = ?) AND ("
                + CalendarContract.Events.DURATION + " = ?) AND ("
                + CalendarContract.Events.EVENT_LOCATION + " = ?))";

        Calendar stTime = new GregorianCalendar();
        stTime.setTime(startTime);

        long diff = endTime.getTime() - startTime.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);

        String[] selectionArgs = new String[]{name, String.valueOf(stTime.getTimeInMillis()), "PT" + String.valueOf(diffHours) + "H" + String.valueOf(diffMinutes) + "M" + String.valueOf(diffSeconds) + "S", room};

        Cursor cursor = activity.getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                projections,
                selection,
                selectionArgs,
                null);

        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }

        return -1;
    }

    public static void unsubscribeCourses() {

        ParseUser currentUser = ParseUser.getCurrentUser();
        String typeUser = currentUser.getString("TypeUser");

        if (typeUser.equals("Student")) {
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.remove("channels");
            try {
                installation.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void subscribeCourses() {
        ParseQuery<ParseObject> courseQuery = ParseQuery.getQuery("Course");
        ParseQuery<ParseObject> studentQuery = ParseQuery.getQuery("Student");
        studentQuery.include("StudentId");
        studentQuery.include("CurrentStudyCourse");
        studentQuery.whereEqualTo("StudentId", ParseUser.getCurrentUser());

        try {
            ParseObject currentStudent = studentQuery.getFirst();
            if (currentStudent != null) {
                ParseObject currentStudyCourse = currentStudent.getParseObject("CurrentStudyCourse");
                if (currentStudyCourse != null) {
                    if (currentStudyCourse.get("Courses") != null) {
                        ArrayList<String> courses = (ArrayList<String>) currentStudyCourse.get("Courses");
                        if (courses.size() > 0) {
                            courseQuery.whereContainedIn("objectId", courses);
                        }
                        List<ParseObject> results_courses = courseQuery.find();
                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        installation.save();
                        for (ParseObject p : results_courses) {
                            if (p.get("Name") != null) {
                                ParsePush.subscribeInBackground(p.getString("Name").replaceAll("\\s", "").replaceAll("-", ""));
                            }
                        }


                    }

                }


            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
