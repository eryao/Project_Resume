package com.fnwang.project_resume;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.fnwang.project_resume.model.BasicInfo;
import com.fnwang.project_resume.util.ImageUtils;
import com.fnwang.project_resume.util.PermissionUtils;

public class BasicInfoEditActivity extends EditBaseActivity<BasicInfo> {
    public static final String KEY_BASIC_INFO = "basic_info";
    private static final int REQ_CODE_PICK_IMAGE = 100;


    @Override
    protected void saveAndExit(BasicInfo data) {
        if (data == null) {
            data = new BasicInfo();
        }

        data.name = ((EditText) findViewById(R.id.edit_user_name)).getText().toString();
        data.email = ((EditText) findViewById(R.id.edit_user_email)).getText().toString();
        data.imageUri = (Uri) findViewById(R.id.edit_basic_info_user_picture).getTag();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_BASIC_INFO, data);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected BasicInfo initializeData() {
        return getIntent().getParcelableExtra(KEY_BASIC_INFO);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_basic_info_edit;
    }

    @Override
    protected void setupUIForEdit(BasicInfo data) {
        ((EditText)findViewById(R.id.edit_user_name)).setText(data.name);
        ((EditText)findViewById(R.id.edit_user_email)).setText(data.email);

        if(data.imageUri != null){
            showImage(data.imageUri);
        }
        findViewById(R.id.edit_basic_info_user_picture_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!PermissionUtils.checkPermission(BasicInfoEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    PermissionUtils.requestReadExternalStoragePermission(BasicInfoEditActivity.this);
                }
                else{
                    pickImage();
                }
            }
        });
    }

    @Override
    protected void setupUIForCreate() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK){
            Uri imageUri = data.getData();
            if(imageUri != null){
                showImage(imageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermissionUtils.REQ_CODE_WRITE_EXTERNAL_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            pickImage();
        }
    }

    private void showImage(Uri imageUri) {
        ImageView imageView = (ImageView) findViewById(R.id.edit_basic_info_user_picture);
        //imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setTag(imageUri);
        ImageUtils.loadImage(this,imageUri,imageView);
    }
    private void pickImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select picture"),
                REQ_CODE_PICK_IMAGE);
    }
}
