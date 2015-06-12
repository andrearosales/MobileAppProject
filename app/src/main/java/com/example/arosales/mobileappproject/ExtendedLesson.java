package com.example.arosales.mobileappproject;

/**
 * Created by ricardogarcia on 12/03/15.
 */
public class ExtendedLesson extends Lesson {

    private Type type;
    private Integer color;

    public ExtendedLesson(Lesson lesson, Type type) {
        super(lesson.getCourse(), lesson.getStartTime(), lesson.getEndTime(), lesson.getRoom());
        this.type = type;
    }


    public ExtendedLesson(Lesson lesson, Type type, Integer color) {
        super(lesson.getCourse(), lesson.getStartTime(), lesson.getEndTime(), lesson.getRoom());
        this.type = type;
        this.color = color;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}

enum Type {
    DAY_SEPARATOR,
    LESSON,
    NO_RESULTS
}

