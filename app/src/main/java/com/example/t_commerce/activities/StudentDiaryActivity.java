package com.example.t_commerce.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.t_commerce.R;
import com.example.t_commerce.adaptor.StandardListAdaptor;
import com.example.t_commerce.adaptor.StudentListAdaptor;
import com.example.t_commerce.models.StandardListModel;
import com.example.t_commerce.models.StudentDetails;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class StudentDiaryActivity extends AppCompatActivity {

    private CardView cardViewStandardGroup;
    private CardView cardViewStudentListGroup;
    private RecyclerView recyclerViewStandard;
    private RecyclerView recyclerViewStudents;
    private AVLoadingIndicatorView swipeLoader;
    private ArrayList<StandardListModel> Standards;
    private ArrayList<StudentDetails> details;
    private SwitchMaterial cbseSwitch, icseSwitch, sscSwitch;

    public static int previousSelected = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_diary);

        Toolbar mTool = (Toolbar) findViewById(R.id.toolbarStudentDiary);
        setSupportActionBar(mTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Student Diary");

        swipeLoader = findViewById(R.id.aviStudentList);
        swipeLoader.hide();
        cardViewStandardGroup = findViewById(R.id.groupStandardList);
        cardViewStandardGroup.setVisibility(View.INVISIBLE);
        cardViewStudentListGroup = findViewById(R.id.groupStudentList);
        cardViewStudentListGroup.setVisibility(View.INVISIBLE);
        recyclerViewStandard = findViewById(R.id.standardRecyclerView);
        recyclerViewStudents = findViewById(R.id.listStudentRecyclerView);
        cbseSwitch = findViewById(R.id.cbseFilterSwitch);
        icseSwitch = findViewById(R.id.icseFilterSwitch);
        sscSwitch = findViewById(R.id.sscFilterSwitch);

        details = MainActivity.Students;



        loadStandardData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewStandard.setLayoutManager(linearLayoutManager);
        StandardListAdaptor adaptor = new StandardListAdaptor(this,Standards);
        recyclerViewStandard.setAdapter(adaptor);
        adaptor.notifyDataSetChanged();

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewStudents.setLayoutManager(linearLayoutManager1);

        studentListCreation();

        loadAnimations();





        cbseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    icseSwitch.setChecked(false);
                    sscSwitch.setChecked(false);

                }
            }
        });


        icseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    cbseSwitch.setChecked(false);
                    sscSwitch.setChecked(false);
                }
            }
        });


        sscSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    cbseSwitch.setChecked(false);
                    icseSwitch.setChecked(false);
                }
            }
        });

    }

    public void loadAnimations()
    {
        cardViewStandardGroup.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        cardViewStandardGroup.startAnimation(animation);
        cardViewStudentListGroup.setVisibility(View.VISIBLE);
        cardViewStudentListGroup.startAnimation(animation);
    }

    public void loadStandardData()
    {
        Standards = new ArrayList<>();

        for(int i = -1 ; i <= 10 ; i++)
        {
            StandardListModel s;
            if(i == -1)
                s = new StandardListModel(i,true);
            else
                s = new StandardListModel(i,false);
            Standards.add(s);
        }

    }

    public void studentListCreation()
    {
        StudentListAdaptor studentListAdaptor = new StudentListAdaptor(this,details);
        recyclerViewStudents.setAdapter(studentListAdaptor);
        studentListAdaptor.notifyDataSetChanged();
    }

}