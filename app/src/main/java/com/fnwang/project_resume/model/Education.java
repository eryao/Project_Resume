package com.fnwang.project_resume.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fnwang.project_resume.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by ou on 18/1/22.
 */

public class Education implements Parcelable {
    //赋予education可打散的能力，make it as serializable object
    public String id;

    public String school;

    public String major;

    public String GPA;

    public Date startDate;

    public Date endDate;

    public List<String> courses;

    public Education() {
        //生成唯一的id
        id = UUID.randomUUID().toString();
    }
    //拼装
    protected Education(Parcel in){
        id = in.readString();
        school = in.readString();
        major = in.readString();
        GPA = in.readString();
//        startDate = DateUtils.stringToDate(in.readString());
//        endDate = DateUtils.stringToDate(in.readString());
        startDate = new Date(in.readLong());
        endDate = new Date(in.readLong());
        courses = in.createStringArrayList();

    }
    public static final Creator<Education> CREATOR = new Creator<Education>() {
        @Override
        public Education createFromParcel(Parcel in) {
            return new Education(in);
        }

        @Override
        public Education[] newArray(int size) {
            return new Education[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //打散
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(school);
        dest.writeString(major);
        dest.writeString(GPA);
//        dest.writeString(DateUtils.dateToString(startDate));
//        dest.writeString(DateUtils.dateToString(endDate));
        dest.writeLong(startDate.getTime());
        dest.writeLong(endDate.getTime());
        dest.writeStringList(courses);
    }
}
