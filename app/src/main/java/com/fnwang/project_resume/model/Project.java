package com.fnwang.project_resume.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by ou on 18/1/22.
 */

public class Project implements Parcelable{
    public String id;
    public String title;
    public String course;
    public Date startDate;
    public Date endDate;
    public List<String> jobs;

    public Project() {
        id = UUID.randomUUID().toString();
    }

    protected Project(Parcel in) {
        id = in.readString();
        title = in.readString();
        course = in.readString();
        startDate = new Date(in.readLong());
        endDate = new Date(in.readLong());
        jobs = in.createStringArrayList();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(course);
        dest.writeLong(startDate.getTime());
        dest.writeLong(endDate.getTime());
        dest.writeStringList(jobs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
}
