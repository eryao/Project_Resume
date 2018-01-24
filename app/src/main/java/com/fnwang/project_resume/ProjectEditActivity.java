package com.fnwang.project_resume;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fnwang.project_resume.model.Experience;
import com.fnwang.project_resume.model.Project;
import com.fnwang.project_resume.util.DateUtils;

import java.util.Arrays;

public class ProjectEditActivity extends EditBaseActivity<Project> {
    public static final String KEY_PROJECT = "project";
    public static final String KEY_PROJECT_ID = "project_id";



    @Override
    protected void saveAndExit(Project data) {
        if(data == null){
            data = new Project();
        }
        data.title = ((EditText)findViewById(R.id.edit_project_title)).getText().toString();
        data.course = ((EditText)findViewById(R.id.edit_project_course)).getText().toString();
        data.startDate = DateUtils.stringToDate(
                ((EditText)findViewById(R.id.edit_project_start_date)).getText().toString());
        data.endDate = DateUtils.stringToDate(
                ((EditText)findViewById(R.id.edit_project_end_date)).getText().toString());
        data.jobs = Arrays.asList(TextUtils.split(
                ((EditText)findViewById(R.id.edit_project_job)).getText().toString(),"\n"));
        //date picker
        Intent resultIntent = new Intent();//hash_map key value pair
        resultIntent.putExtra(KEY_PROJECT, data);//serialize deserialize
        //serialize is the conversion of an object to a series of bytes
        //纯数据传递，而不需要把reference传递过去，对gc有好处
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected Project initializeData() {
        return getIntent().getParcelableExtra(KEY_PROJECT);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_project_edit;
    }

    @Override
    protected void setupUIForEdit(final Project data) {
        ((EditText) findViewById(R.id.edit_project_title))
                .setText(data.title);
        ((EditText) findViewById(R.id.edit_project_course))
                .setText(data.course);
        ((EditText) findViewById(R.id.edit_project_start_date))
                .setText(DateUtils.dateToString(data.startDate));
        ((EditText) findViewById(R.id.edit_project_end_date))
                .setText(DateUtils.dateToString(data.endDate));
        ((EditText) findViewById(R.id.edit_project_job))
                .setText(TextUtils.join("\n", data.jobs));

        findViewById(R.id.edit_experience_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_PROJECT_ID, data.id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void setupUIForCreate() {
        findViewById(R.id.edit_project_delete).setVisibility(View.GONE);
    }
}
