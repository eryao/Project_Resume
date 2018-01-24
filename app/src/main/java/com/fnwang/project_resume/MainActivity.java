package com.fnwang.project_resume;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fnwang.project_resume.model.BasicInfo;
import com.fnwang.project_resume.model.Education;
import com.fnwang.project_resume.model.Experience;
import com.fnwang.project_resume.model.Project;
import com.fnwang.project_resume.util.DateUtils;
import com.fnwang.project_resume.util.ImageUtils;
import com.fnwang.project_resume.util.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int REQ_CODE_EDUCATION_EDIT = 100;
    private static final int REQ_CODE_EXPERIENCE_EDIT = 101;
    private static final int REQ_CODE_PROJECT_EDIT = 102;
    private static final int REQ_CODE_BASICINFO_EDIT = 103;

    private static final String MODEL_BASIC_INFO = "basic_info";
    private static final String MODEL_EDUCATIONS = "educations";
    private static final String MODEL_EXPERIENCES = "experiences";
    private static final String MODEL_PROJECTS = "projects";

    private BasicInfo basicInfo;
    private List<Education> educationList;
    private List<Experience> experienceList;
    private List<Project> projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fakeData();
        loadData();
        setupUI();
    }

    //这个函数用来做activity之间数据沟通
    //mainActivity做为主调activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode == REQ_CODE_EDUCATION_EDIT && resultCode == Activity.RESULT_OK){
//            Education result = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
//            educationList.add(result);
//            //下一步是保证数据与界面的同步，要educationlist能够出现在界面里
//            setupEducationUI();
//        }
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case REQ_CODE_BASICINFO_EDIT:
                    BasicInfo basicInfo = data.getParcelableExtra(BasicInfoEditActivity.KEY_BASIC_INFO);
                    updateBasicInfo(basicInfo);
                    break;
                case REQ_CODE_EDUCATION_EDIT:
                    String educationId = data.getStringExtra(EducationEditActivity.KEY_EDUCATION_ID);
                    if(educationId != null){
                        deleteEducation(educationId);
                    }
                    else{
                        Education education = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
                        updateEducation(education);
                    }
                    break;
                case REQ_CODE_EXPERIENCE_EDIT:
                    String experienceId = data.getStringExtra(ExperienceEditActivity.KEY_EXPERIENCE_ID);
                    if(experienceId != null){
                        deleteExperience(experienceId);
                    }
                    else{
                        Experience experience = data.getParcelableExtra(ExperienceEditActivity.KEY_EXPERIENCE);
                        updateExperience(experience);
                    }
                    break;
                case REQ_CODE_PROJECT_EDIT:
                    String projectId = data.getStringExtra(ProjectEditActivity.KEY_PROJECT_ID);
                    if(projectId != null){
                        deleteProject(projectId);
                    }
                    else{
                        Project poject = data.getParcelableExtra(ProjectEditActivity.KEY_PROJECT);
                        updateProject(poject);
                    }
                    break;
            }
        }
    }


    private void setupUI(){
        setContentView(R.layout.activity_main);
        ImageButton addEducationBtn = (ImageButton)findViewById(R.id.add_education_btn);
        addEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,EducationEditActivity.class);
                //startActivity(intent);
                //因为要被调activity的结果所以要用别的方法,需要requestCode
                startActivityForResult(intent, REQ_CODE_EDUCATION_EDIT);
            }
        });
        ImageButton addExperienceBtn = (ImageButton)findViewById(R.id.add_experience_btn);
        addExperienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ExperienceEditActivity.class);
                //startActivity(intent);
                //因为要被调activity的结果所以要用别的方法,需要requestCode
                startActivityForResult(intent, REQ_CODE_EXPERIENCE_EDIT);
            }
        });
        ImageButton addProjectBtn = (ImageButton)findViewById(R.id.add_project_btn);
        addProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ProjectEditActivity.class);
                //startActivity(intent);
                //因为要被调activity的结果所以要用别的方法,需要requestCode
                startActivityForResult(intent, REQ_CODE_PROJECT_EDIT);
            }
        });

        setupBasicInfoUI();
        setupEducationUI();
        setupExperienceUI();
        setupProjectUI();
    }

    private void setupBasicInfoUI(){
//        ((TextView)findViewById(R.id.user_name)).setText(basicInfo.name);
//        ((TextView)findViewById(R.id.user_Email)).setText(basicInfo.email);
        ((TextView)findViewById(R.id.user_name)).setText(TextUtils.isEmpty(basicInfo.name) ? "Your name" : basicInfo.name);
        ((TextView)findViewById(R.id.user_Email)).setText(TextUtils.isEmpty(basicInfo.email) ? "Your Email" : basicInfo.email);
        ImageView userPicture = (ImageView)findViewById(R.id.user_picture);
        if(basicInfo.imageUri != null){
            ImageUtils.loadImage(this,basicInfo.imageUri,userPicture);
        }
        else{
            userPicture.setImageResource(R.drawable.user_ghost);
        }
        findViewById(R.id.edit_basic_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BasicInfoEditActivity.class);
                intent.putExtra(BasicInfoEditActivity.KEY_BASIC_INFO, basicInfo);
                startActivityForResult(intent,REQ_CODE_BASICINFO_EDIT);
            }
        });
    }
    private void setupEducationUI(){
        LinearLayout educationContainer = (LinearLayout)findViewById(R.id.education_list);
        educationContainer.removeAllViews();
        for(Education education : educationList){
            View educationView = getLayoutInflater().inflate(R.layout.education_item, null);
            getEducationView(educationView, education);
            educationContainer.addView(educationView);
        }
    }
    private void getEducationView(View view, final Education education){
        //把linear layout返回，返回成一个view
        String dateString = DateUtils.dateToString(education.startDate) + " ~ " + DateUtils.dateToString(education.endDate);
        //fake_data
        //View view = getLayoutInflater().inflate(R.layout.education_item, null);
        ((TextView)view.findViewById(R.id.education_school)).setText(education.school + " ("+ dateString + ")");
        ((TextView)view.findViewById(R.id.education_major)).setText(education.major);
        ((TextView)view.findViewById(R.id.education_gpa)).setText(" ("+education.GPA+")");
        ((TextView) view.findViewById(R.id.education_courses))
                .setText(formatItems(education.courses));
        ImageButton editEducationBtn = (ImageButton)findViewById(R.id.edit_education_btn);
//        editEducationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //启动editEducation activity
//                //task stack
//                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
//                intent.putExtra(EducationEditActivity.KEY_EDUCATION,education);
//                //startActivity(intent);
//                startActivityForResult(intent, REQ_CODE_EDUCATION_EDIT);
//            }
//        });
    }

    private String formatItems(List<String> courses) {
        StringBuilder sb = new StringBuilder();
        for(String course : courses){
            sb.append(' ').append('-').append(' ').append(course).append('\n');
        }
        if(sb.length() > 0){
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }

    private void setupExperienceUI() {
        LinearLayout experienceContainer = (LinearLayout)findViewById(R.id.experience_list);
        experienceContainer.removeAllViews();
        for(Experience experience : experienceList){
            View experienceView = getLayoutInflater().inflate(R.layout.experience_item,null);
            getExperienceView(experienceView, experience);
            experienceContainer.addView(experienceView);
        }
    }

    private void getExperienceView(View view, final Experience experience) {
        String dataString = DateUtils.dateToString(experience.startDate) + " ~ " + DateUtils.dateToString(experience.endDate);
        ((TextView)view.findViewById(R.id.experience_company)).setText(experience.company + " ("+dataString+")");
        ((TextView)view.findViewById(R.id.experience_title)).setText(experience.title);
        ((TextView)view.findViewById(R.id.experience_jobs)).setText(formatItems(experience.jobs));
        ImageButton editExperienceBtn = (ImageButton)findViewById(R.id.edit_experience_btn);
//        editExperienceBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
//                intent.putExtra(ExperienceEditActivity.KEY_EXPERIENCE, experience);
//                startActivityForResult(intent, REQ_CODE_EXPERIENCE_EDIT);
//            }
//        });
    }

    private void setupProjectUI() {
        LinearLayout projectContainer = (LinearLayout)findViewById(R.id.project_list);
        projectContainer.removeAllViews();
        for(Project project : projectList){
            View projectView = getLayoutInflater().inflate(R.layout.project_item,null);
            getProjectView(projectView,project);
            projectContainer.addView(projectView);

        }
    }

    private void getProjectView(View view, final Project project) {
        String dataString = DateUtils.dateToString(project.startDate) + " ~ " + DateUtils.dateToString(project.endDate);
        ((TextView)view.findViewById(R.id.project_title)).setText(project.title + " ("+dataString+")");
        ((TextView)view.findViewById(R.id.project_course)).setText(project.course);
        ((TextView)view.findViewById(R.id.project_jobs)).setText(formatItems(project.jobs));
        ImageButton editProjectBtn = (ImageButton)findViewById(R.id.edit_project_btn);
//        editProjectBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
//                intent.putExtra(ProjectEditActivity.KEY_PROJECT, project);
//                startActivityForResult(intent, REQ_CODE_PROJECT_EDIT);
//            }
//        });

    }

    private void loadData() {
        BasicInfo savedBasicInfo = ModelUtils.read(this, MODEL_BASIC_INFO, new TypeToken<BasicInfo>(){});
        basicInfo = savedBasicInfo == null ? new BasicInfo() : savedBasicInfo;

        List<Education> savedEducation = ModelUtils.read(this, MODEL_EDUCATIONS, new TypeToken<List<Education>>(){});
        educationList = savedEducation == null ? new ArrayList<Education>() : savedEducation;

        List<Experience> savedExperience = ModelUtils.read(this, MODEL_EXPERIENCES, new TypeToken<List<Experience>>(){});
        experienceList = savedExperience == null ? new ArrayList<Experience>() : savedExperience;

        List<Project> savedProject = ModelUtils.read(this, MODEL_PROJECTS, new TypeToken<List<Project>>(){});
        projectList= savedEducation == null ? new ArrayList<Project>() : savedProject;
    }

    private void updateBasicInfo(BasicInfo basicinfo){
        ModelUtils.save(this, MODEL_BASIC_INFO, basicinfo);

        this.basicInfo = basicinfo;
        setupBasicInfoUI();

    }
    private void updateEducation(Education education){
        boolean found = false;
        for(int i = 0; i <educationList.size(); ++i){
            Education e = educationList.get(i);
            if(TextUtils.equals(e.id, education.id)){
                found = true;
                educationList.set(i,education);
                break;
            }
        }
        if(!found){
            educationList.add(education);
        }
        ModelUtils.save(this, MODEL_EDUCATIONS,educationList);
        setupEducationUI();

    }
    private void updateExperience(Experience experience){
        boolean found = false;
        for(int i = 0; i < experienceList.size(); ++i){
            Experience e = experienceList.get(i);
            if(TextUtils.equals(e.id,experience.id)){
                found = true;
                experienceList.set(i,experience);
                break;
            }
        }
        if(!found){
            experienceList.add(experience);
        }
        ModelUtils.save(this, MODEL_EXPERIENCES,experienceList);
        setupExperienceUI();

    }
    private void updateProject(Project project){
        boolean found = false;
        for(int i = 0; i < projectList.size(); ++i){
            Project e = projectList.get(i);
            if(TextUtils.equals(e.id,project.id)){
                found = true;
                projectList.set(i,project);
                break;
            }
        }
        if(!found){
            projectList.add(project);
        }
        ModelUtils.save(this, MODEL_PROJECTS,projectList);
        setupProjectUI();

    }
    private void deleteEducation(String educationId){
        for(int i = 0; i < educationList.size(); ++i){
            if(TextUtils.equals(educationList.get(i).id, educationId)){
                educationList.remove(i);
                break;
            }
        }
        ModelUtils.save(this, MODEL_EDUCATIONS,educationList);
        setupEducationUI();
    }
    private void deleteExperience(String experienceId){
        for(int i = 0; i < experienceList.size(); ++i){
            if(TextUtils.equals(experienceList.get(i).id, experienceId)){
                experienceList.remove(i);
                break;
            }
        }
        ModelUtils.save(this, MODEL_EXPERIENCES,experienceList);
        setupExperienceUI();

    }
    private void deleteProject(String projectId){
        for(int i = 0; i < projectList.size(); ++i){
            if(TextUtils.equals(projectList.get(i).id, projectId)){
                projectList.remove(i);
                break;
            }
        }
        ModelUtils.save(this, MODEL_PROJECTS,projectList);
        setupProjectUI();

    }


    private void fakeData(){
        basicInfo = new BasicInfo();
        basicInfo.name = "NNW";
        basicInfo.email = "nnw111@gmail.com";

        Education e1 = new Education();
        Education e2 = new Education();

        e1.school = "Northeastern University";
        e1.major = "Computer Science";
        e1.startDate = DateUtils.stringToDate("09/2012");
        e1.endDate = DateUtils.stringToDate("07/2016");
        e1.GPA = "3.4";
        e1.courses = new ArrayList<>();
        e1.courses.add("Algorithm");
        e1.courses.add("Database");
        e2.school = "UIC";
        e2.major = "ECE";
        e2.courses = new ArrayList<>();
        e2.courses.add("Neural Network");
        e2.courses.add("Image Processing");

        e2.startDate = DateUtils.stringToDate("08/2015");
        e2.endDate = DateUtils.stringToDate("06/2017");
        e2.GPA = "3.9";
        educationList = new ArrayList<>();
        educationList.add(e1);
        educationList.add(e2);

    }
}
