package com.fnwang.project_resume.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Created by ou on 18/1/22.
 */

public class Experience implements Parcelable{
    public String id;
    public String company;
    public String title;
    public Date startDate;
    public Date endDate;
    public List<String> jobs;

    public Experience() {
        id = UUID.randomUUID().toString();
    }

    protected Experience(Parcel in) {
        id = in.readString();
        company = in.readString();
        title = in.readString();
        startDate = new Date(in.readLong());
        endDate = new Date(in.readLong());
        jobs = in.createStringArrayList();
    }

    public static final Creator<Experience> CREATOR = new Creator<Experience>() {
        @Override
        public Experience createFromParcel(Parcel in) {
            return new Experience(in);
        }

        @Override
        public Experience[] newArray(int size) {
            return new Experience[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(company);
        parcel.writeString(title);
        parcel.writeLong(startDate.getTime());
        parcel.writeLong(endDate.getTime());
        parcel.writeStringList(jobs);
    }
}
