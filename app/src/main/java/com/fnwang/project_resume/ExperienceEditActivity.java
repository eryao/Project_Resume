package com.fnwang.project_resume;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fnwang.project_resume.model.Education;
import com.fnwang.project_resume.model.Experience;
import com.fnwang.project_resume.util.DateUtils;

import java.util.Arrays;

public class ExperienceEditActivity extends EditBaseActivity<Experience> {
    public static final String KEY_EXPERIENCE = "experience";
    public static final String KEY_EXPERIENCE_ID = "experience_id";


    @Override
    protected void saveAndExit(Experience data) {
        if(data == null){
            data = new Experience();
        }
        data.company = ((EditText)findViewById(R.id.edit_experience_company)).getText().toString();
        data.title = ((EditText)findViewById(R.id.edit_experience_title)).getText().toString();
        data.startDate = DateUtils.stringToDate(
                ((TextView)findViewById(R.id.edit_experience_start_date)).getText().toString());
        data.endDate = DateUtils.stringToDate(
                ((TextView)findViewById(R.id.edit_experience_end_date)).getText().toString());
        data.jobs = Arrays.asList(TextUtils.split(
                ((EditText)findViewById(R.id.edit_experience_jobs)).getText().toString(),"\n"));
        //date picker
        Intent resultIntent = new Intent();//hash_map key value pair
        resultIntent.putExtra(KEY_EXPERIENCE, data);//serialize deserialize
        //serialize is the conversion of an object to a series of bytes
        //纯数据传递，而不需要把reference传递过去，对gc有好处
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected Experience initializeData() {
        return getIntent().getParcelableExtra(KEY_EXPERIENCE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_experience_edit;
    }

    @Override
    protected void setupUIForEdit(final Experience data) {
        ((EditText) findViewById(R.id.edit_experience_company))
                .setText(data.company);
        ((EditText) findViewById(R.id.edit_experience_title))
                .setText(data.title);

        ((EditText) findViewById(R.id.edit_experience_start_date))
                .setText(DateUtils.dateToString(data.startDate));
        ((EditText) findViewById(R.id.edit_experience_end_date))
                .setText(DateUtils.dateToString(data.endDate));
        ((EditText) findViewById(R.id.edit_experience_jobs))
                .setText(TextUtils.join("\n", data.jobs));

        findViewById(R.id.edit_experience_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_EXPERIENCE_ID, data.id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void setupUIForCreate() {
        findViewById(R.id.edit_experience_delete).setVisibility(View.GONE);
    }
}
