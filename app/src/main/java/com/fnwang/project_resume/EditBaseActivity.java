package com.fnwang.project_resume;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by ou on 18/1/23.
 */

public abstract class EditBaseActivity<T> extends AppCompatActivity {
    private T data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_education_edit);
        setContentView(getLayoutId());

        //弄出返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = initializeData();
        if(data != null){
            setupUIForEdit(data);
        }
        else{
            setupUIForCreate();
        }
    }

    //让对号出现在EducationEdit的Activity里
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }
    //使返回按钮生效,被framework调用
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //menu item就是action bar里可以被点击的东西
        //返回键
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        //保存键
        else if(item.getItemId() == R.id.action_save){
            saveAndExit(data);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract void saveAndExit(T data);

    protected abstract T initializeData();

    protected abstract int getLayoutId();

    protected abstract void setupUIForEdit(T data);

    protected abstract void setupUIForCreate();
}
