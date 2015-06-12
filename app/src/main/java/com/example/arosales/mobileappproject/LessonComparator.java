package com.example.arosales.mobileappproject;

import java.util.Comparator;

/**
 * Created by ricardogarcia on 12/03/15.
 */
public class LessonComparator implements Comparator<Lesson> {

    @Override
    public int compare(Lesson l1, Lesson l2) {

        return l1.getStartTime().compareTo(l2.getStartTime());
    }
}