package com.fnwang.project_resume;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fnwang.project_resume.model.Education;
import com.fnwang.project_resume.util.DateUtils;

import java.util.Arrays;

/**
 * Created by ou on 18/1/22.
 */

public class EducationEditActivity extends EditBaseActivity<Education> {
    public static final String KEY_EDUCATION = "education";
    public static final String KEY_EDUCATION_ID = "education_id";

    @Override
    protected void saveAndExit(Education data) {
        if(data == null){
            data = new Education();
        }
        data.school = ((EditText)findViewById(R.id.education_edit_school)).getText().toString();
        data.major = ((EditText)findViewById(R.id.education_edit_major)).getText().toString();
        data.GPA = ((EditText)findViewById(R.id.education_edit_gpa)).getText().toString();
        data.startDate = DateUtils.stringToDate(
                ((TextView)findViewById(R.id.education_edit_start_date)).getText().toString());
        data.endDate = DateUtils.stringToDate(
                ((TextView)findViewById(R.id.education_edit_end_date)).getText().toString());
        data.courses = Arrays.asList(TextUtils.split(
                ((EditText)findViewById(R.id.education_edit_courses)).getText().toString(),"\n"));
        //date picker
        Intent resultIntent = new Intent();//hash_map key value pair
        resultIntent.putExtra(KEY_EDUCATION, data);//serialize deserialize
        //serialize is the conversion of an object to a series of bytes
        //纯数据传递，而不需要把reference传递过去，对gc有好处
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected Education initializeData() {
        //return null;
        return getIntent().getParcelableExtra(KEY_EDUCATION);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_education_edit;
    }

    @Override
    protected void setupUIForEdit(final Education data) {
        ((EditText)findViewById(R.id.education_edit_school)).setText(data.school);
        ((EditText)findViewById(R.id.education_edit_major)).setText(data.major);
        ((EditText)findViewById(R.id.education_edit_gpa)).setText(data.GPA);
        ((EditText)findViewById(R.id.education_edit_courses)).setText(TextUtils.join("\n",data.courses));
        ((EditText)findViewById(R.id.education_edit_start_date)).setText(DateUtils.dateToString(data.startDate));
        ((EditText)findViewById(R.id.education_edit_end_date)).setText(DateUtils.dateToString(data.endDate));
        findViewById(R.id.education_edit_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra(KEY_EDUCATION_ID, data.id);
                setResult(Activity.RESULT_OK,result);
                finish();
            }
        });
    }

    @Override
    protected void setupUIForCreate() {
        findViewById(R.id.education_edit_delete).setVisibility(View.GONE);

    }
}
